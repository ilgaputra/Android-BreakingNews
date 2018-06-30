package com.gyosanila.e_power.breakingnews;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gyosanila.e_power.breakingnews.Home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private final int splashInterval=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);

                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);
    }
}
