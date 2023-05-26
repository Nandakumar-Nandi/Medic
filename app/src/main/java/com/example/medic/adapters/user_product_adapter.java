package com.example.medic.adapters;

import android.content.Context;
import android.media.Image;
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
import com.example.medic.model_class.user_product_class;

import java.util.ArrayList;

public class user_product_adapter extends RecyclerView.Adapter<user_product_adapter.viewholder> {
    ArrayList<user_product_class> list;
    Context context;

    public user_product_adapter(ArrayList<user_product_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public user_product_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_product_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull user_product_adapter.viewholder holder, int position) {
        user_product_class data=list.get(position);
        Glide.with(context)
                .load(data.getImg())
                .fitCenter()
                .into(holder.img);
        holder.title.setText(data.getName());
        holder.price.setText(data.getRate());
        holder.stock.setText(data.getStock());
        holder.prescription.setText(data.getPrescription());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView img;
        ConstraintLayout layout;
        TextView title,stock,prescription,price;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.user_product_image);
            layout=itemView.findViewById(R.id.user_product_layout);
            title=itemView.findViewById(R.id.user_product_name);
            stock=itemView.findViewById(R.id.user_product_stock);
            prescription=itemView.findViewById(R.id.user_product_status);
            price=itemView.findViewById(R.id.user_product_price);
        }
    }
}
