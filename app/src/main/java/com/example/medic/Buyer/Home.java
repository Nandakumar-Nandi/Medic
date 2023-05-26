package com.example.medic.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.medic.R;
import com.example.medic.adapters.category_adapter;
import com.example.medic.adapters.productadapter;
import com.example.medic.model_class.category_class;
import com.example.medic.model_class.products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    BottomNavigationView nav;
    ArrayList<products> productlist;
    ArrayList<category_class> categorylist;
    DatabaseReference ref;
    FirebaseUser user;
    RecyclerView recyclerView1,recyclerView2;
    Toolbar bar;
    ImageCarousel imageCarousel;
    SearchView searchView;
    productadapter adapter;
    ArrayList<products> newlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bar=findViewById(R.id.action_bar);
        setSupportActionBar(bar);
        nav=findViewById(R.id.nav_view);
        nav.setSelectedItemId(R.id.home_page);
        imageCarousel=findViewById(R.id.carousel);
        imageCarousel.addData(new CarouselItem("https://firebasestorage.googleapis.com/v0/b/medic-1c997.appspot.com/o/Carousels%2FHome%2FCarousel1.jpg?alt=media&token=760dfe18-e37d-4fb3-8b3e-73af382f427a"));
        imageCarousel.addData(new CarouselItem("https://firebasestorage.googleapis.com/v0/b/medic-1c997.appspot.com/o/Carousels%2FHome%2Fcarousel2.jpg?alt=media&token=af1c75c8-b0c6-4449-9c6b-1ee417fd57a2"));
        searchView=findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setadapter(newText);
                return true;
            }
        });
        recyclerView1=findViewById(R.id.recyclerhome);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView2=findViewById(R.id.category_recycler);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        productlist=new ArrayList<>();
        adapter=new productadapter(productlist,this);
        categorylist=new ArrayList<>();
        category_adapter category_adapter=new category_adapter(categorylist,this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                if(ds.exists()){
                    for(DataSnapshot dataSnapshot: ds.getChildren()){
                        products data=new products(dataSnapshot.child("img1").getValue().toString(),dataSnapshot.child("title").getValue().toString(),dataSnapshot.child("actual_price").getValue().toString(),dataSnapshot.child("id").getValue().toString(),dataSnapshot.child("manufacturer").getValue().toString(),dataSnapshot.child("category").getValue().toString());
                        productlist.add(data);
                    }
                    recyclerView1.setAdapter(adapter);
                }
            }
        });
        ref.child("Product Category").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot dataSnapshot=task.getResult();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            category_class data=new category_class(ds.child("Image").getValue().toString(),ds.child("Title").getValue().toString());
                            categorylist.add(data);
                            recyclerView2.setAdapter(category_adapter);
                        }
                    }
            }
        });
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_page: return true;
                case R.id.Profile:
                    Intent I1=new Intent(Home.this, profile.class);
                    Bundle b1= ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle();
                    startActivity(I1,b1);
                return true;
                case R.id.Order:
                    Intent I2=new Intent(Home.this, orders.class);
                    Bundle b2= ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle();
                    startActivity(I2,b2);
                    return true;
                case R.id.Settings:
                    Intent I3=new Intent(Home.this, settings.class);
                    Bundle b3= ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle();
                    startActivity(I3,b3);
                    return true;
                default:return true;
            }
        });
        BroadcastReceiver receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    ArrayList<products> new_category_list=new ArrayList<>();
                    String title=intent.getStringExtra("Category");
                for(products product:productlist){
                    if(product.getCategory().toLowerCase().contains(title.toLowerCase())){
                        new_category_list.add(product);
                    }
                }
                adapter.setList(new_category_list);

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter("category-listener"));
    }

    public void setadapter(String newText) {
        newlist=new ArrayList<>();
        for(products product:productlist){
            if(product.getName().toLowerCase().contains(newText.toLowerCase())){
                newlist.add(product);
            }
        }
        adapter.setList(newlist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.cart){
            startActivity(new Intent(this, cart.class));
        }
        else{
            startActivity(new Intent(this,cart.class));
        }
        return super.onOptionsItemSelected(item);
    }
}