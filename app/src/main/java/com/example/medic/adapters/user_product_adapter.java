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
import com.example.medic.Seller.edit_product;
import com.example.medic.model_class.user_product_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import android.os.Handler;

public class user_product_adapter extends RecyclerView.Adapter<user_product_adapter.viewholder> {
    ArrayList<user_product_class> list;
    Context context;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public user_product_adapter(ArrayList<user_product_class> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList (ArrayList <user_product_class> newlist) {
        list = newlist;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public user_product_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_product_layout,parent,false);
        return new viewholder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull user_product_adapter.viewholder holder, int position) {
        user_product_class data = list.get(position);
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
                Intent i = new Intent(context, edit_product.class);
                i.putExtra("Id", data.getId());
                context.startActivity(i);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("products").child(data.getId()).removeValue();
                ref.child("User_Products").child(user.getUid()).child(data.getId()).removeValue();
                notifyDataSetChanged();
            }
        });
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.delete.setVisibility(View.GONE);
            }
        },5000);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView img,delete;
        ConstraintLayout layout;
        TextView title,stock,prescription,price;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.user_product_image);
            delete=itemView.findViewById(R.id.user_product_delete);
            layout=itemView.findViewById(R.id.seller_product_layout);
            title=itemView.findViewById(R.id.seller_product_name);
            stock=itemView.findViewById(R.id.seller_pending_verification_count);
            prescription=itemView.findViewById(R.id.user_product_status);
            price=itemView.findViewById(R.id.seller_product_price);
        }
    }
}
