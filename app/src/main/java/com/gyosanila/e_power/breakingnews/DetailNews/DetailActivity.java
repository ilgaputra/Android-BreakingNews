package com.gyosanila.e_power.breakingnews.DetailNews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.gyosanila.e_power.breakingnews.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {

    public TextView title,description,source,author,publish,link;
    Toolbar toolbar;
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar();
        initView();
        setView();
        FloatingAction();

    }

    public void Toolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //appBarLayout.setTitle(getIntent().getStringExtra("source"));
    }

    public void initView()
    {
        title = (TextView) findViewById(R.id.tvTitleDetail);
        description = (TextView) findViewById(R.id.tvDescription);
        source = (TextView) findViewById(R.id.tvSourceDetail);
        author = (TextView) findViewById(R.id.tvAuthorDetail);
        publish = (TextView) findViewById(R.id.tvPublishDetail);
        link = (TextView) findViewById(R.id.tvLink);
    }

    public void setView()
    {
        title.setText(getIntent().getStringExtra("title"));
        String Description = getIntent().getStringExtra("description");
        if(Description.equals("null"))
        {
            Description = "Don't have Description";
        }
        description.setText(Description);
        source.setText(getIntent().getStringExtra("source"));
        author.setText(getIntent().getStringExtra("author"));
        publish.setText(getIntent().getStringExtra("publish"));
        Url = getIntent().getStringExtra("url");

        final AppBarLayout layout = (AppBarLayout) findViewById(R.id.app_bar);
        Picasso.get().load(getIntent().getStringExtra("urlImage")).into(new Target(){
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Drawable d = new BitmapDrawable(getResources(), bitmap);
                layout.setBackground(d);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
    }

    public void FloatingAction()
    {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void clickHandler(View v){
        if (v.getId() == R.id.tvLink) {
            Log.e("Home",Url);
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Url));
            startActivity(browserIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

}
