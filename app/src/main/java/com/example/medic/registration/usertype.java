package com.example.medic.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medic.Buyer.Home;
import com.example.medic.R;
import com.example.medic.Seller.seller_home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class   usertype extends AppCompatActivity {
    Button buyer,seller;
    FirebaseUser user;
    DatabaseReference ref;
    String c_token;
    DataSnapshot ds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);
        buyer=findViewById(R.id.buyer);
        seller=findViewById(R.id.seller);
        ref=  FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref.child("users").child("Buyer").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ds=task.getResult();
                if(ds.exists()){
                    startActivity(new Intent(usertype.this, Home.class));
                }
            }
        });
        ref.child("users").child("Seller").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ds=task.getResult();
                if(ds.exists()){
                    startActivity(new Intent(usertype.this, seller_home.class));
                }
            }
        });
        user.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
             c_token=task.getResult().getToken();
            }
        });
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("Mobile")!=null){
                    ref.child("users").child("Buyer").child(user.getUid()).child("Id").setValue(user.getUid());
                    ref.child("users").child("Buyer").child(user.getUid()).child("Mobile").setValue(user.getPhoneNumber());
                }
                else {
                    if(getIntent().getStringExtra("name")!=null){
                        ref.child("users").child("Buyer").child(user.getUid()).child("Name").setValue(getIntent().getStringExtra("name"));
                        ref.child("users").child("Buyer").child(user.getUid()).child("Email").setValue(user.getEmail());
                        ref.child("users").child("Buyer").child(user.getUid()).child("Id").setValue(user.getUid());
                    }
                    else {
                        ref.child("users").child("Buyer").child(user.getUid()).child("Name").setValue(user.getDisplayName());
                        ref.child("users").child("Buyer").child(user.getUid()).child("Email").setValue(user.getEmail());
                        ref.child("users").child("Buyer").child(user.getUid()).child("Id").setValue(user.getUid());
                    }
                }
                ref.child("users").child("Buyer").child(user.getUid()).child("Type").setValue("Buyer");
                startActivity(new Intent(usertype.this,Home.class));

            }
        });
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("Mobile") != null) {
                    ref.child("users").child("Seller").child(user.getUid()).child("Id").setValue(user.getUid());
                    ref.child("users").child("Seller").child(user.getUid()).child("Mobile").setValue(user.getPhoneNumber());
                } else {
                    if (getIntent().getStringExtra("name") != null) {
                        ref.child("users").child("Seller").child(user.getUid()).child("Name").setValue(getIntent().getStringExtra("name"));
                        ref.child("users").child("Seller").child(user.getUid()).child("Email").setValue(user.getEmail());
                        ref.child("users").child("Seller").child(user.getUid()).child("Id").setValue(user.getUid());
                    } else {
                        ref.child("users").child("Seller").child(user.getUid()).child("Name").setValue(user.getDisplayName());
                        ref.child("users").child("Seller").child(user.getUid()).child("Email").setValue(user.getEmail());
                        ref.child("users").child("Seller").child(user.getUid()).child("Id").setValue(user.getUid());
                    }
                    ref.child("users").child("Seller").child(user.getUid()).child("Type").setValue("Seller");
                    startActivity(new Intent(usertype.this, Home.class));
                }
            }
        });
    }
}