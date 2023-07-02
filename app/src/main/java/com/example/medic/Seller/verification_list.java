package com.example.medic.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.medic.R;
import com.example.medic.adapters.verification_list_adapter;
import com.example.medic.model_class.verification_list_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class verification_list extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseUser user;
    RecyclerView recyclerView;
    String product_id;
    verification_list_adapter adapter;
    ArrayList<verification_list_class> list;
    DataSnapshot username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_list);
        product_id=getIntent().getStringExtra("Id");
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.verification_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        adapter=new verification_list_adapter(list,this);
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        ref.child("users").child("Buyer").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                username=task.getResult();
            }
        });
        ref.child("User_Products").child(user.getUid()).child(product_id).child("Prescription_Verification").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot=task.getResult();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String user_id=ds.getKey(),order_id="";
                        for(DataSnapshot result:ds.getChildren()) {
                            order_id =result.getKey();
                        }
                    Toast.makeText(verification_list.this, ""+order_id, Toast.LENGTH_SHORT).show();
                    ref.child("Orders").child(user_id).child(order_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task1) {
                            DataSnapshot res=task1.getResult();
                            Toast.makeText(verification_list.this, ""+res.toString(), Toast.LENGTH_SHORT).show();
                                verification_list_class data=new verification_list_class(product_id,user_id,username.child(user_id).child("Name").getValue().toString(),res.child("Date").getValue().toString(),!res.child(product_id).child("Count").exists()?"1":res.child(product_id).child("Count").getValue().toString());
                             Toast.makeText(verification_list.this, ""+data, Toast.LENGTH_SHORT).show();
                                list.add(data);
                                recyclerView.setAdapter(adapter);
                        }
                    });

                }
            }
        });
    }
}