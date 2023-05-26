package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.model_class.products;
import com.example.medic.productview;

import java.util.ArrayList;

public class productadapter extends RecyclerView.Adapter<productadapter.ViewHolder> {
    ArrayList<products> list;
    Context context;
    public  productadapter(ArrayList<products> list,Context context){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public productadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.toplayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull productadapter.ViewHolder holder, int position) {
            products data=list.get(position);
            Glide.with(context)
                    .load(data.getUrl())
                    .into(holder.img);
            holder.name.setText(data.getName());
            holder.mrp.setText("₹"+data.getMrp());
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Intent i =new Intent(context, productview.class);
                 i.putExtra("Id",data.getId());
                 context.startActivity(i);

                }
            });
            holder.click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(context, productview.class);
                    i.putExtra("Id",data.getId());
                    context.startActivity(i);
                }
            });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<products> newlist){
        list=newlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,mrp;
        Button button;
        ConstraintLayout click;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.medicine_name);
            mrp=itemView.findViewById(R.id.Amount);
            button=itemView.findViewById(R.id.AddCart);
            click=itemView.findViewById(R.id.product_click);
        }
    }
}
