package com.example.medic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.medic.registration.login;

public class MainActivity extends AppCompatActivity {
    Animation a1;
    ImageView logo;
    TextView title;
    ProgressBar loaderr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a1= AnimationUtils.loadAnimation(this,R.anim.top);
        logo=findViewById(R.id.s1);
        title=findViewById(R.id.Title);
        loaderr=findViewById(R.id.loader);
        loaderr.setVisibility(View.GONE);
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loaderr.setVisibility(View.VISIBLE);
                Handler h1=new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent I= new Intent(MainActivity.this, login.class);
                        Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                        startActivity(I,b);
                    }
                },1000);

            }
        },1500);
        logo.setAnimation(a1);
        title.setAnimation(a1);
    }
}