package com.example.medic.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.medic.R;
import com.example.medic.registration.login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class settings extends AppCompatActivity {
    BottomNavigationView nav;
    TextView signout;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        signout=findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(settings.this, login.class));
                finish();
            }
        });
        nav=findViewById(R.id.nav_view);
        nav.setSelectedItemId(R.id.Settings);
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_page:
                    Intent I1=new Intent(settings.this, Home.class);
                    Bundle b1= ActivityOptions.makeSceneTransitionAnimation(settings.this).toBundle();
                    startActivity(I1,b1);
                    return true;
                case R.id.Profile:
                    Intent I2=new Intent(settings.this, profile.class);
                    Bundle b2= ActivityOptions.makeSceneTransitionAnimation(settings.this).toBundle();
                    startActivity(I2,b2);
                    return true;
                case R.id.Order:
                    Intent I3=new Intent(settings.this, orders.class);
                    Bundle b3= ActivityOptions.makeSceneTransitionAnimation(settings.this).toBundle();
                    startActivity(I3,b3);
                    return true;
                case R.id.Settings:  return true;
                default:return true;
            }
        });

    }
}