package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.model_class.category_class;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {
    ArrayList<category_class>list;
    Context context;

    public category_adapter(ArrayList<category_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public category_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull category_adapter.ViewHolder holder, int position) {
            category_class data=list.get(position);
            holder.Title.setText(data.getTitle());
            Glide.with(context)
                .load(data.getImgurl())
                .centerCrop()
                .into(holder.img);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent("category-listener");
                    i.putExtra("Category",data.getTitle());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        ShapeableImageView img;
        ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.category_constraint);
            Title=itemView.findViewById(R.id.category_title);
            img=itemView.findViewById(R.id.category_image);
        }
    }
}
