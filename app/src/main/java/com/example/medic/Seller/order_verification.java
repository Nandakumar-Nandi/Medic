
package com.example.medic.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.imageviewdialog;
import com.example.medic.model_class.verification_list_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class order_verification extends AppCompatActivity {
    TextView title,price,count,stock,astock,user_name,date;
    ImageView product_image,prescription_image;
    Button confirm,cancel;
    DatabaseReference ref;
    FirebaseUser user;
    verification_list_class data;
    String order_id="",url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_verification);
        title=findViewById(R.id.order_verification_product_name);
        price=findViewById(R.id.order_verification_product_rate);
        count=findViewById(R.id.order_verification_count);
        stock=findViewById(R.id.order_verification_cstock);
        astock=findViewById(R.id.order_verification_astock);
        user_name=findViewById(R.id.order_verification_user);
        date=findViewById(R.id.order_verification_date);
        product_image=findViewById(R.id.order_verification_product_image);
        prescription_image=findViewById(R.id.order_verification_prescription);
        confirm=findViewById(R.id.order_verification_confirm_Order);
        cancel=findViewById(R.id.order_verification_cancel_Order);
        user=FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        data= (verification_list_class) getIntent().getSerializableExtra("Data");
        ref.child("User_Products").child(user.getUid()).child(data.getProduct_id()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                Integer n=Integer.parseInt(ds.child("Stock").getValue().toString());
                stock.setText(n.toString());
                n=n-Integer.parseInt(data.getCount().equals("")?"1":data.getCount());
                count.setText(data.getCount().equals("")?"1":data.getCount());
                astock.setText(n.toString());
                user_name.setText(data.getUsername());
                date.setText(data.getDate());
                for(DataSnapshot res:ds.child("Prescription_Verification").child(data.getUser_id()).getChildren()) {
                    order_id = res.getKey();
                    url = res.child("Prescription_Url").getValue().toString();
                }
                Glide.with(order_verification.this)
                                .load(url)
                                        .fitCenter()
                                                .into(prescription_image);
                setdata();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Orders").child(data.getUser_id()).child(order_id).child(data.getProduct_id()).child("Prescription").setValue("Verified");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Orders").child(data.getUser_id()).child(order_id).child(data.getProduct_id()).child("Prescription").setValue("Prescription Not Valid");
            }
        });
        prescription_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(order_verification.this, imageviewdialog.class);
                i.putExtra("Url",url);
                startActivity(i);
            }
        });


    }

    private void setdata() {
        ref.child("products").child(data.getProduct_id()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                title.setText(ds.child("title").getValue().toString());
                price.setText(ds.child("actual_price").getValue().toString());
                Glide.with(order_verification.this)
                        .load(ds.child("img1").getValue().toString())
                        .fitCenter()
                        .into(product_image);
            }
        });
    }
}