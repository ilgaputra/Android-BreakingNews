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
import com.gyosanila.e_power.breakingnews.DetailNews.DetailActivity;
import com.gyosanila.e_power.breakingnews.Home.Model.ItemClickListener;
import com.gyosanila.e_power.breakingnews.Home.Model.MyHolder;
import com.gyosanila.e_power.breakingnews.Home.Model.HomeClass;
import com.gyosanila.e_power.breakingnews.R;
import com.gyosanila.e_power.breakingnews.Utils.InternetUtils;
import com.gyosanila.e_power.breakingnews.app.AppConfig;
import com.gyosanila.e_power.breakingnews.app.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.JsonObjectRequest;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private String title,description,source,author,publish,urlImage,url;

    private SwipeRefreshLayout swipeView;
    private RecyclerView rvHome;
    private Toolbar toolbar;

    List<HomeClass> interestConnect = new ArrayList<>();
    HomeActivity.NewsAdapter adapterHome;

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
                            "city list refreshed", Toast.LENGTH_SHORT).show();
                    swipeView.setRefreshing(false);
                }
            }, 1000);
        }

        ;
    };

    private void getDataServer(){
        if(InternetUtils.isConnectedToInternet(getApplicationContext())){
            //db = new Database(getApplicationContext());
            //db.clearEvent(0,Database.ALLEVENT);
            getAllNews(AppConfig.Server);
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
                                interestConnect.clear();
                                JSONArray jArray = new JSONArray(jsonObject.getString("articles"));
                                int arrayLength = jArray.length();
                                for(int i=0;i<arrayLength;i++) {
                                    // Get current json object
                                    JSONObject objArticles = jArray.getJSONObject(i);
                                    JSONObject objSource = new JSONObject(objArticles.getString("source"));

                                    title = objArticles.getString("title");
                                    description = objArticles.getString("description");
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

                                    interestConnect.add(new HomeClass(HomeActivity.this,title,description,source,author,publish,urlImage,url));
                                }
                                adapterHome = new HomeActivity.NewsAdapter(HomeActivity.this,interestConnect);
                                rvHome.setAdapter(adapterHome);
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
        List<HomeClass> items;
        HomeActivity context;
        int pos;
        View v;

        public NewsAdapter(HomeActivity context, List<HomeClass> items){
            this.context = context;
            this.items = items;
        }
        @Override
        //membuat layout item jadi anak  layoutnya
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,null);
            MyHolder interestViewHolder = new MyHolder(v);
            return interestViewHolder;
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
                    //Intent i=new Intent(getApplicationContext(),DetailActivity.class);
                    Log.e("Home",String.valueOf(pos));
                    //i.putExtra(Config.TAG_id, String.valueOf(lists.get(pos).getId()));
                    //i.putExtra(Config.TAG_menu, "tampilpesanan");
                  // startActivity(i);
                    //finish();
                }

            });
        }
        @Override
        public int getItemCount() {
            return items.size();
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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
