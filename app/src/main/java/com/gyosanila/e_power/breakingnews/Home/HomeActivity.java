package com.gyosanila.e_power.breakingnews.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gyosanila.e_power.breakingnews.R;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeView;

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private String[] LIST_ITEM_TEXT_CITIES = {"Los Angeles", "Chicago",
            "Indianapolis", "San Francisco", "Oklahoma City", "Washington"};

    private String[] LIST_ITEM_TEXT_MORE_CITIES = {"Phoenix", "San Antonio",
            "San Jose", "Nashville", "Las Vegas", "Virginia Beach"};

    private List<String> cityList;

    // variable to toggle city values on refresh
    boolean refreshToggle = true;

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
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used

        cityList = Arrays.asList(LIST_ITEM_TEXT_CITIES);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.item_home, cityList);
        listView.setAdapter(adapter);
        listView.requestLayout();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (refreshToggle) {
                refreshToggle = false;
                cityList = Arrays.asList(LIST_ITEM_TEXT_MORE_CITIES);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.item_home, cityList);
            } else {
                refreshToggle = true;
                cityList = Arrays.asList(LIST_ITEM_TEXT_CITIES);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.item_home, cityList);
            }
            listView.setAdapter(adapter);

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
}
