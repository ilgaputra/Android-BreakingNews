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
import android.widget.Toast;

import com.gyosanila.e_power.breakingnews.Database.Database;
import com.gyosanila.e_power.breakingnews.Home.Model.News;
import com.gyosanila.e_power.breakingnews.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public TextView title,description,source,author,publish,link;
    Toolbar toolbar;
    String Title,Description,Source,Author,Publish,Link,Url,UrlImage;
    Database db;

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
        Title = getIntent().getStringExtra("title");
        Description = getIntent().getStringExtra("description");
        Source = getIntent().getStringExtra("source");
        Author = getIntent().getStringExtra("author");
        Publish = getIntent().getStringExtra("publish");
        Url = getIntent().getStringExtra("url");
        UrlImage = getIntent().getStringExtra("urlImage");

        title.setText(Title);
        description.setText(Description);
        source.setText(Source);
        author.setText(Author);
        publish.setText(Publish);


        final AppBarLayout layout = (AppBarLayout) findViewById(R.id.app_bar);
        Picasso.get().load(UrlImage).into(new Target(){
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
                db = new Database(getApplicationContext());
                List<News> mNews = new ArrayList<>();
                mNews.add(new News(Title,Description,Author,Source,Publish,UrlImage,Url));
                db.insertNews(mNews,1);
                Toast.makeText(getApplicationContext(),"News Save",Toast.LENGTH_SHORT).show();
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
