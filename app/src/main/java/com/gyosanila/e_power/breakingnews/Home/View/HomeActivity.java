package com.gyosanila.e_power.breakingnews.Home.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gyosanila.e_power.breakingnews.Database.Database;
import com.gyosanila.e_power.breakingnews.DetailNews.DetailActivity;
import com.gyosanila.e_power.breakingnews.Home.Model.ItemClickListener;
import com.gyosanila.e_power.breakingnews.Home.Model.MyHolder;
import com.gyosanila.e_power.breakingnews.Home.Model.News;
import com.gyosanila.e_power.breakingnews.R;
import com.gyosanila.e_power.breakingnews.Utils.InternetUtils;
import com.gyosanila.e_power.breakingnews.app.AppConfig;
import com.gyosanila.e_power.breakingnews.app.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.JsonObjectRequest;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private String title,description,source,author,publish,urlImage,url;

    private SwipeRefreshLayout swipeView;
    private RecyclerView rvHome;
    private Toolbar toolbar;

    List<News> mNews = new ArrayList<>();
    HomeActivity.NewsAdapter adapterHome;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        initSwipeRefresh();
        initRecyclerView();
        initDrawerLayout();
        initNavigation();
        getDataServer();

    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initSwipeRefresh()
    {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_home);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(40);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
    }

    public void initRecyclerView()
    {
        rvHome = (RecyclerView) findViewById(R.id.rvHome);
        rvHome.setLayoutManager(new LinearLayoutManager(this));
        rvHome.setItemAnimator(new DefaultItemAnimator());
    }

    public void initDrawerLayout()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void initNavigation()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRefresh() {
        swipeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeView.setRefreshing(true);
                handler.sendEmptyMessage(0);
            }
        }, 1000);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            getDataServer();
            swipeView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "News list refreshed", Toast.LENGTH_SHORT).show();
                    swipeView.setRefreshing(false);
                }
            }, 1000);
        }

        ;
    };

    private void getDataServer(){
        db = new Database(getApplicationContext());
        if(InternetUtils.isConnectedToInternet(getApplicationContext())){
            db.clearEvent(0);
            getAllNews(AppConfig.Server);
        }
        else
        {

            showData(db.getNews(0));
        }
    }

    private void getAllNews(final String URL ) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.e("Home", jsonObject.toString());
                            if (jsonObject.getString("status").equals("ok")) {
                                mNews.clear();
                                JSONArray jArray = new JSONArray(jsonObject.getString("articles"));
                                int arrayLength = jArray.length();
                                for(int i=0;i<arrayLength;i++) {
                                    // Get current json object
                                    JSONObject objArticles = jArray.getJSONObject(i);
                                    JSONObject objSource = new JSONObject(objArticles.getString("source"));

                                    title = objArticles.getString("title");
                                    description = objArticles.getString("description");
                                    if(description.equals("null"))
                                    {
                                        description = "Don't have Description";
                                    }
                                    source = objSource.getString("name");
                                    author = objArticles.getString("author");
                                    if(author.equals("null"))
                                    {
                                        author= "Unknown";
                                    }
                                    publish = objArticles.getString("publishedAt");
                                    String delims = "[T]";
                                    String delims1 = "[.]";
                                    String[] tokens = publish.split(delims);
                                    String[] date = tokens[1].split(delims1);
                                    publish = tokens[0] +"  " + date[0];
                                    urlImage = objArticles.getString("urlToImage");
                                    url = objArticles.getString("url");

                                    mNews.add(new News(title,description,author,source,publish,urlImage,url));
                                }
                                db.insertNews(mNews,0);
                                showData(db.getNews(0));
                            }
                        } catch (JSONException e) {
                            Log.e("SaveLocal:",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        AppController.getInstance().addToRequestQueue(request, "getNews");
    }

    class NewsAdapter extends RecyclerView.Adapter<MyHolder> {
        List<News> items;
        HomeActivity context;
        int pos;
        View v;

        public NewsAdapter(HomeActivity context, List<News> items){
            this.context = context;
            this.items = items;
        }
        @Override
        //membuat layout item jadi anak  layoutnya
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,null);
            MyHolder newsViewHolder = new MyHolder(v);
            return newsViewHolder;
        }

        @Override
        //proses untuk menampilkan
        public void onBindViewHolder(final MyHolder holder, final int position) {
            pos = position;
            holder.item_title.setText(items.get(position).getTitle()+"");
            holder.item_publish.setText(items.get(position).getPublish()+"");
            holder.item_source.setText(items.get(position).getSource()+"");
            holder.item_author.setText(items.get(position).getAuthor()+"");
            Picasso.get().load(items.get(position).getUrlImage()).into(holder.item_img);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    //CREATE INTENT
                    Intent i=new Intent(getApplicationContext(),DetailActivity.class);
                    Log.e("Home",String.valueOf(pos));
                    i.putExtra("title", items.get(pos).getTitle());
                    i.putExtra("description", items.get(pos).getDescription());
                    i.putExtra("source", items.get(pos).getSource());
                    i.putExtra("author", items.get(pos).getAuthor());
                    i.putExtra("publish", items.get(pos).getPublish());
                    i.putExtra("urlImage", items.get(pos).getUrlImage());
                    i.putExtra("url", items.get(pos).getUrl());
                   startActivity(i);
                }

            });
        }
        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    public void showData(List<News> mNews)
    {
        adapterHome = new HomeActivity.NewsAdapter(HomeActivity.this,mNews);
        rvHome.setAdapter(adapterHome);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Searching...");
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    Log.e("Search", newText);
                    String query = newText;
                    if(query.equals("")){
                        showData(db.getNews(0));
                    }else{
                        showData(db.searchingNews(query, 0));
                    }

                } catch (Exception e) {

                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search)
        {
            //Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
