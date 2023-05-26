package com.example.medic.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medic.R;
import com.example.medic.adapters.review_adapter;
import com.example.medic.model_class.products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class review_page extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<products> list;
    review_adapter adapter;
    String id;
    FirebaseUser user;
    DataSnapshot ds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        recyclerView=findViewById(R.id.review_recycler);
        list=new ArrayList<>();
        adapter=new review_adapter(list,this,id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        user= FirebaseAuth.getInstance().getCurrentUser();
        id=getIntent().getStringExtra("Id");
        ref.child("Orders").child(user.getUid()).child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task1) {
                    ds=task1.getResult();
                    if(ds.exists()) {
                        for (DataSnapshot snapshot :ds.getChildren()){
                            if(String.valueOf(snapshot.getValue()).equals("")){
                                ref.child("products").child(String.valueOf(snapshot.getKey())).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                        DataSnapshot dataSnapshot= task2.getResult();
                                        products data = new products(dataSnapshot.child("img1").getValue().toString(), dataSnapshot.child("title").getValue().toString(), dataSnapshot.child("actual_price").getValue().toString(), dataSnapshot.child("id").getValue().toString(), dataSnapshot.child("manufacturer").getValue().toString(),dataSnapshot.child("category").getValue().toString());
                                        list.add(data);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    }
            }
        });
    }
}