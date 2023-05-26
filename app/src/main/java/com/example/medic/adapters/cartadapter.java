package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.model_class.products;
import com.example.medic.productview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cartadapter  extends RecyclerView.Adapter<cartadapter.Viewholder> {
    ArrayList<products> list;
    Context context;
    DatabaseReference ref;
    FirebaseUser user;
    public cartadapter(ArrayList<products> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public cartadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cartlayout,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cartadapter.Viewholder holder, int position) {
        products data=list.get(position);
        Glide.with(context)
                .load(data.getUrl())
                .fitCenter()
                .into(holder.product_image);
        holder.title.setText(data.getName());
        holder.manufacturer.setText(data.getManufacturer());
        holder.rate.setText(data.getMrp());
        holder.increement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer n=Integer.parseInt(holder.total.getText().toString())+1;
                holder.total.setText(String.valueOf(n));
                Intent i=new Intent("custom-message");
                i.putExtra("Id",data.getId());
                i.putExtra("Type","Increement");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                notifyDataSetChanged();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.total.getText().toString().equals("1")){
                        user=FirebaseAuth.getInstance().getCurrentUser();
                        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
                        Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
                        ref.child("Cart").child(user.getUid()).child(data.getId()).removeValue();
                }
                else {
                    Integer n = Integer.parseInt(holder.total.getText().toString()) - 1;
                    holder.total.setText(String.valueOf(n));
                    Intent i=new Intent("custom-message");
                    i.putExtra("Id",data.getId());
                    i.putExtra("Type","Decreement");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    notifyDataSetChanged();
                }

            }
        });
        holder.total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Integer n=Integer.parseInt(holder.total.getText().toString());
                if(n>1){
                    holder.delete.setImageResource(R.drawable.sub);
                }
                else{
                    holder.delete.setImageResource(R.drawable.ic_baseline_delete_24);
                }
                notifyDataSetChanged();
            }

        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,productview.class);
                i.putExtra("Id",data.getId());
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView product_image,delete,increement;
        TextView title,manufacturer,rate,total;
        ConstraintLayout layout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.review_product_image);
            delete=itemView.findViewById(R.id.del);
            increement=itemView.findViewById(R.id.increement);
            title=itemView.findViewById(R.id.review_product_title);
            manufacturer=itemView.findViewById(R.id.review_maufacturer_tiitle);
            rate=itemView.findViewById(R.id.review_product_rate);
            total=itemView.findViewById(R.id.cart_product_quantity);
            layout=itemView.findViewById(R.id.cart_product_layout);
        }
    }
}
