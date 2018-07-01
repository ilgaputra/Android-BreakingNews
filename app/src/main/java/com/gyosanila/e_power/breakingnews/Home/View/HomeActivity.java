package com.gyosanila.e_power.breakingnews.Home.View;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gyosanila.e_power.breakingnews.Home.Model.ItemClickListener;
import com.gyosanila.e_power.breakingnews.Home.Model.MyHolder;
import com.gyosanila.e_power.breakingnews.Home.Model.HomeClass;
import com.gyosanila.e_power.breakingnews.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeView;

    private ArrayAdapter<String> adapter;


    HomeActivity.InterestAdapter adapterHome;

    private String[] LIST_ITEM_TEXT_CITIES = {"Los Angeles", "Chicago",
            "Indianapolis", "San Francisco", "Oklahoma City", "Washington"};

    private String[] LIST_ITEM_TEXT_MORE_CITIES = {"Phoenix", "San Antonio",
            "San Jose", "Nashville", "Las Vegas", "Virginia Beach"};

    private List<String> cityList;

    // variable to toggle city values on refresh
    boolean refreshToggle = true;
    private RecyclerView rvHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_home);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(40);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used

        rvHome = (RecyclerView) findViewById(R.id.rvHome);
        rvHome.setLayoutManager(new LinearLayoutManager(this));
        rvHome.setItemAnimator(new DefaultItemAnimator());

        List<HomeClass> interestConnect = new ArrayList<>();
        interestConnect.add(new HomeClass(this,"Galaxy S4", "Samsung","tv","Blue","2012", "www.google.com", "www.facebook.com"));
        interestConnect.add(new HomeClass(this,"Galaxy S4", "Samsung","tv","Blue","2012", "www.google.com", "www.facebook.com"));
        interestConnect.add(new HomeClass(this,"Galaxy S4", "Samsung","tv","Blue","2012", "www.google.com", "www.facebook.com"));

       adapterHome = new HomeActivity.InterestAdapter(this,interestConnect);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (refreshToggle) {
                refreshToggle = false;
                cityList = Arrays.asList(LIST_ITEM_TEXT_MORE_CITIES);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.item_home, cityList);
            } else {
                refreshToggle = true;
                rvHome.setAdapter(adapterHome);
                cityList = Arrays.asList(LIST_ITEM_TEXT_CITIES);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.item_home, cityList);
            }
            //listView.setAdapter(adapter);

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

    class InterestAdapter extends RecyclerView.Adapter<MyHolder> {
        List<HomeClass> items;
        HomeActivity context;
        int pos;
        View v;

        public InterestAdapter(HomeActivity context, List<HomeClass> items){
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
            URL url = null;
            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.item_img);


            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    //CREATE INTENT
                    //Intent i=new Intent(getApplicationContext(),DetailScreen.class);
                    //LOAD DATA
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
