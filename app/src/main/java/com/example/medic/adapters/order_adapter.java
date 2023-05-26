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
import com.example.medic.Buyer.review_page;
import com.example.medic.R;
import com.example.medic.model_class.order_class;

import java.util.ArrayList;

public class order_adapter extends RecyclerView.Adapter<order_adapter.viewholder> {
    ArrayList<order_class> list;
    Context context;

    public order_adapter(ArrayList<order_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.orders_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        order_class data=list.get(position);
        holder.title.setText(data.getProduct1()+","+data.getProduct2());
        holder.date.setText(data.getDate());
        holder.status.setText(data.getStatus());
        holder.amount.setText("Rs"+data.getTotal());
        Glide.with(context)
                .load(data.getImage_url())
                .fitCenter()
                .into(holder.img);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, review_page.class);
                i.putExtra("Id",data.getOrder_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,amount,date,status;
        ConstraintLayout layout;
        ImageView img;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.user_product_name);
            amount=itemView.findViewById(R.id.user_product_price);
            date=itemView.findViewById(R.id.user_product_stock);
            status=itemView.findViewById(R.id.user_product_status_count);
            img=itemView.findViewById(R.id.user_product_image);
            layout=itemView.findViewById(R.id.order_layout);
        }
    }
}
