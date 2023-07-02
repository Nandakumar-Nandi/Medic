package com.example.medic.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import com.example.medic.R;
import com.example.medic.adapters.seller_home_adapter;
import com.example.medic.model_class.seller_home_product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class seller_home extends AppCompatActivity {
    BottomNavigationView navigationView;
    RecyclerView recyclerView;
    seller_home_adapter adapter;
    ArrayList<seller_home_product> list;
    FirebaseUser user;
    DatabaseReference ref;
    DataSnapshot product_snapshot;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        recyclerView=findViewById(R.id.seller_home_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        adapter=new seller_home_adapter(list,this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                product_snapshot =task.getResult();
            }
        });
        ref.child("User_Products").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot result=task.getResult();
                for(DataSnapshot res:result.getChildren()){
                    if(res.child("Prescription_Verification").hasChildren()){
                        seller_home_product data=new seller_home_product(res.getKey(),String.valueOf(res.child("Prescription_Verification").getChildrenCount()),product_snapshot.child(res.getKey()).child("img1").getValue().toString(),product_snapshot.child(res.getKey()).child("actual_price").getValue().toString(),product_snapshot.child(res.getKey()).child("title").getValue().toString());
                        list.add(data);
                    }
                }
                recyclerView.setAdapter(adapter);
            }
        });
        navigationView=findViewById(R.id.nav_view_seller);
        navigationView.setSelectedItemId(R.id.home_page);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_page:
                    return true;
                case R.id.products:
                    Intent I1 = new Intent(seller_home.this, seller_products.class);
                    Bundle b1 = ActivityOptions.makeSceneTransitionAnimation(seller_home.this).toBundle();
                    startActivity(I1, b1);
                    return true;
                case R.id.Profile:
                    Intent I2 = new Intent(seller_home.this, seller_profile.class);
                    Bundle b2 = ActivityOptions.makeSceneTransitionAnimation(seller_home.this).toBundle();
                    startActivity(I2, b2);
                    return true;
                case R.id.Settings:
                    Intent I3 = new Intent(seller_home.this, seller_settings.class);
                    Bundle b3 = ActivityOptions.makeSceneTransitionAnimation(seller_home.this).toBundle();
                    startActivity(I3, b3);
                    return true;

            }
            return false;
        });

        searchView=findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setlist(newText);
                return false;
            }
        });
    }

    private void setlist(String newText) {
        ArrayList<seller_home_product> list2=new ArrayList<>();
        for(seller_home_product data:list){
            if(data.getTitle().toLowerCase().contains(newText.toLowerCase())){
                list2.add(data);
            }
        }
        adapter.setlist(list2);
    }
}