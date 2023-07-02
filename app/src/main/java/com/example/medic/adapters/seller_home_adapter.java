package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.Seller.verification_list;
import com.example.medic.model_class.seller_home_product;

import java.util.ArrayList;

public class seller_home_adapter extends RecyclerView.Adapter<seller_home_adapter.viewholder> {
    ArrayList<seller_home_product> list;
    Context context;

    public seller_home_adapter(ArrayList<seller_home_product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public seller_home_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.seller_home_product,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull seller_home_adapter.viewholder holder, int position) {
        seller_home_product data=list.get(position);
        holder.title.setText(data.getTitle());
        holder.price.setText(data.getPrice());
        holder.count.setText(data.getCount());
        Glide.with(context)
                .load(data.getImage_URL())
                .into(holder.image);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, verification_list.class);
                i.putExtra("Id",data.getProduct_id());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setlist(ArrayList<seller_home_product> list2) {
        list=list2;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,price,count;
        ImageView image;
        ConstraintLayout layout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.seller_product_name);
            price=itemView.findViewById(R.id.seller_product_price);
            count=itemView.findViewById(R.id.seller_pending_verification_count);
            image=itemView.findViewById(R.id.seller_home_product_image);
            layout=itemView.findViewById(R.id.seller_product_layout);
        }
    }
}
