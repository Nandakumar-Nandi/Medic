package com.example.medic.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.R;
import com.example.medic.Seller.order_verification;
import com.example.medic.model_class.verification_list_class;

import java.util.ArrayList;

public class verification_list_adapter extends RecyclerView.Adapter<verification_list_adapter.viewholder> {

    ArrayList<verification_list_class> list;
    Context context;

    public verification_list_adapter(ArrayList<verification_list_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public verification_list_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.verification_list_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull verification_list_adapter.viewholder holder, int position) {
        verification_list_class data=list.get(position);
        holder.name.setText(data.getUsername());
        holder.date.setText(data.getDate());
        if(data.getCount().equals("")){
            holder.count.setText("1");
        }
        else {
            holder.count.setText(data.getCount());
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, order_verification.class);
                i.putExtra("Data",data);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView name,date,count;
        ConstraintLayout layout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.verification_layout_user);
            date=itemView.findViewById(R.id.verification_layout_order_date);
            count=itemView.findViewById(R.id.verification_layout_product_count);
            layout=itemView.findViewById(R.id.verification_layout);

        }
    }
}
