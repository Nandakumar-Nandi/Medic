package com.example.medic.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.example.medic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class seller_profile extends AppCompatActivity {
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        navigationView=findViewById(R.id.nav_view_seller);
        navigationView.setSelectedItemId(R.id.Profile);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_page:
                    Intent I2 = new Intent(seller_profile.this, seller_home.class);
                    Bundle b2 = ActivityOptions.makeSceneTransitionAnimation(seller_profile.this).toBundle();
                    startActivity(I2, b2);
                    return true;
                case R.id.products:
                    Intent I1 = new Intent(seller_profile.this, seller_products.class);
                    Bundle b1 = ActivityOptions.makeSceneTransitionAnimation(seller_profile.this).toBundle();
                    startActivity(I1, b1);
                    return true;
                case R.id.Profile:
                    return true;
                case R.id.Settings:
                    Intent I3 = new Intent(seller_profile.this, seller_settings.class);
                    Bundle b3 = ActivityOptions.makeSceneTransitionAnimation(seller_profile.this).toBundle();
                    startActivity(I3, b3);
                    return true;

            }
            return false;
        });
    }

}