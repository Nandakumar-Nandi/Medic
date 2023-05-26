package com.example.medic.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newaddress extends AppCompatActivity {
    DatabaseReference ref;
    TextInputEditText name,phone,flat_no,address;
    AppCompatButton add;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaddress);
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        user= FirebaseAuth.getInstance().getCurrentUser();
        name=findViewById(R.id.new_name);
        phone=findViewById(R.id.new_phone);
        flat_no=findViewById(R.id.new_flatno);
        address=findViewById(R.id.new_address);
        add=findViewById(R.id.new_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Delivery").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot ds=task.getResult();
                        if(ds.hasChildren()) {
                            Integer count = Integer.parseInt(String.valueOf(ds.getChildrenCount()));
                            count = count + 1;
                            ref.child("Delivery").child(user.getUid()).child(String.valueOf(count)).child("Name").setValue(name.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child(String.valueOf(count)).child("Flat_no").setValue(flat_no.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child(String.valueOf(count)).child("Address").setValue(address.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child(String.valueOf(count)).child("Mobile").setValue(phone.getText().toString());
                        }
                        else{
                            ref.child("Delivery").child(user.getUid()).child("1").child("Name").setValue(name.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child("1").child("Flat_no").setValue(flat_no.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child("1").child("Address").setValue(address.getText().toString());
                            ref.child("Delivery").child(user.getUid()).child("1").child("Mobile").setValue(phone.getText().toString());
                        }
                        startActivity(new Intent(newaddress.this, cart.class));
                    }
                });
            }
        });
    }
}