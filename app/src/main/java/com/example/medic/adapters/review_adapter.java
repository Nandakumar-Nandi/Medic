package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.model_class.products;
import com.example.medic.productview;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class review_adapter extends RecyclerView.Adapter<review_adapter.viewholder> {
    DatabaseReference ref;
    ArrayList<products> list;
    Context context;
    String Order_id;
    FirebaseUser user;

    public review_adapter( ArrayList<products> list, Context context,String Order) {
        this.list = list;
        this.context = context;
        this.Order_id=Order;
    }

    @NonNull
    @Override
    public review_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.review_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull review_adapter.viewholder holder, int position) {
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        user= FirebaseAuth.getInstance().getCurrentUser();
        products data=list.get(position);
        Glide.with(context)
                .load(data.getUrl())
                .fitCenter()
                .into(holder.product_image);
        holder.title.setText(data.getName());
        holder.manufacturer.setText(data.getManufacturer());
        holder.amount.setText(data.getMrp());
        holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ref.child("Rating").child(data.getId()).child(user.getUid()).child("Rating").setValue(String.valueOf(v));
            }
        });
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Rating").child(data.getId()).child(user.getUid()).child("Comment").setValue(holder.comment.getText().toString());
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, productview.class);
                i.putExtra("Id",data.getId());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView product_image;
        TextView title,manufacturer,amount;
        TextInputEditText comment;
        AppCompatButton send;
        RatingBar rating;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.review_product_layout);
            product_image=itemView.findViewById(R.id.review_product_image);
            title=itemView.findViewById(R.id.review_product_title);
            manufacturer=itemView.findViewById(R.id.review_maufacturer_title);
            amount=itemView.findViewById(R.id.review_product_rate);
            comment=itemView.findViewById(R.id.review_product_comment);
            send=itemView.findViewById(R.id.review_comment_button);
            rating=itemView.findViewById(R.id.review_ratingbar);
        }
    }
}
