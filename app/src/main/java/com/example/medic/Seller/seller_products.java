package com.example.medic.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.example.medic.R;
import com.example.medic.adapters.user_product_adapter;
import com.example.medic.model_class.user_product_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class seller_products extends AppCompatActivity {
    BottomNavigationView navigationView;
    RecyclerView recyclerView1,recyclerView2;
    DatabaseReference ref;
    FirebaseUser user;
    user_product_adapter adapter1,adapter2;
    ArrayList<user_product_class> list1,list2;
    DataSnapshot productsnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView1=findViewById(R.id.Ostock_products_recycler);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setHasFixedSize(true);
        recyclerView2=findViewById(R.id.Instock_products_recycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setHasFixedSize(true);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        adapter1=new user_product_adapter(list1,this);
        adapter2=new user_product_adapter(list2,this);
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                productsnapshot=task.getResult();
            }
        });
        ref.child("User_Products").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot=task.getResult();
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Integer.parseInt(ds.child("Stock").getValue().toString())==0 || ds.child("Stock").getValue().toString().equals("")){
                            user_product_class data=new user_product_class(productsnapshot.child(ds.getKey()).child("title").getValue().toString(),
                                    "null",
                                    "null",
                                    productsnapshot.child(ds.getKey()).child("actual_price").getValue().toString(),
                                    productsnapshot.child(ds.getKey()).child("img1").getValue().toString(),
                                    ds.getKey());
                            list1.add(data);
                    }
                }
                recyclerView1.setAdapter(adapter1);
            }
        });
        ref.child("User_Products").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot=task.getResult();
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Integer.parseInt(ds.child("Stock").getValue().toString())!=0|| !ds.child("Stock").getValue().toString().equals("")){
                        user_product_class data=new user_product_class(productsnapshot.child(ds.getKey()).child("title").getValue().toString(),
                                ds.child("Stock").getValue().toString(),
                                String.valueOf(ds.child("Prescription_Verfication").getChildrenCount()),
                                productsnapshot.child(ds.getKey()).child("actual_price").getValue().toString(),
                                productsnapshot.child(ds.getKey()).child("img1").getValue().toString(),
                                ds.getKey());
                        list2.add(data);
                    }
                }
                recyclerView2.setAdapter(adapter2);
            }
        });
        navigationView=findViewById(R.id.nav_view_seller);
        navigationView.setSelectedItemId(R.id.home_page);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_page:
                    return true;
                case R.id.products:
                    Intent I1 = new Intent(seller_products.this, seller_products.class);
                    Bundle b1 = ActivityOptions.makeSceneTransitionAnimation(seller_products.this).toBundle();
                    startActivity(I1, b1);
                    return true;
                case R.id.Profile:
                    Intent I2 = new Intent(seller_products.this, seller_profile.class);
                    Bundle b2 = ActivityOptions.makeSceneTransitionAnimation(seller_products.this).toBundle();
                    startActivity(I2, b2);
                    return true;
                case R.id.Settings:
                    Intent I3 = new Intent(seller_products.this, seller_settings.class);
                    Bundle b3 = ActivityOptions.makeSceneTransitionAnimation(seller_products.this).toBundle();
                    startActivity(I3, b3);
                    return true;

            }
            return false;
        });
    }
}