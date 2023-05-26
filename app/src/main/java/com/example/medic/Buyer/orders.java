package com.example.medic.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medic.R;
import com.example.medic.adapters.order_adapter;
import com.example.medic.model_class.order_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class orders extends AppCompatActivity {
    BottomNavigationView nav;
    FirebaseUser user;
    RecyclerView recyclerView;
    DatabaseReference ref;
    TextView total_count;
    order_adapter adapter;
    ArrayList<order_class> list;
    DataSnapshot product_snapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        total_count=findViewById(R.id.orders_total);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        recyclerView=findViewById(R.id.orders_recycler);
        list=new ArrayList<>();
        adapter=new order_adapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                     product_snapshot= task.getResult();
            }
        });
        ref.child("Orders").child(user.getUid()).get ().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot=task.getResult();
                total_count.setText(String.valueOf(snapshot.getChildrenCount()));
                        for(DataSnapshot ds1:snapshot.getChildren()){
                            String Total= ds1.child("Total").getValue().toString();
                            String status=ds1.child("Prescription_Status").getValue().toString();
                            String date=ds1.child("Date").getValue().toString();
                            String imgurl="";
                            String Product1="",Product2="";
                            for(DataSnapshot ds2:ds1.getChildren()){
                                if(product_snapshot.child(ds2.getKey()).exists()){
                                    if(Product1.equals("")){
                                        Product1=product_snapshot.child(ds2.getKey()).child("title").getValue().toString();
                                        imgurl=product_snapshot.child(ds2.getKey()).child("img1").getValue().toString();
                                    }
                                    else if(!Product1.isEmpty() && !Product2.isEmpty()){
                                        break;
                                    }
                                    else{
                                        Product2=product_snapshot.child(ds2.getKey()).child("title").getValue().toString();
                                    }
                                }
                            }
                            order_class data=new order_class(Product1,Product2,Total,status, ds1.getKey(), date,imgurl);
                            list.add(data);
                            recyclerView.setAdapter(adapter);
                        }
                }
        });











        nav=findViewById(R.id.nav_view);
        nav.setSelectedItemId(R.id.Order);
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_page:
                    Intent I1=new Intent(orders.this, Home.class);
                    Bundle b1= ActivityOptions.makeSceneTransitionAnimation(orders.this).toBundle();
                    startActivity(I1,b1);
                    return true;
                case R.id.Profile:
                    Intent I2=new Intent(orders.this, profile.class);
                    Bundle b2= ActivityOptions.makeSceneTransitionAnimation(orders.this).toBundle();
                    startActivity(I2,b2);
                    return true;
                case R.id.Order:  return true;
                case R.id.Settings:
                    Intent I3=new Intent(orders.this, settings.class);
                    Bundle b3= ActivityOptions.makeSceneTransitionAnimation(orders.this).toBundle();
                    startActivity(I3,b3);
                    return true;
                default:return true;
            }
        });

    }
}