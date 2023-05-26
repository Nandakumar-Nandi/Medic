package com.example.medic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.Buyer.ItemClickListener;
import com.example.medic.R;
import com.example.medic.model_class.address_class;

import java.util.ArrayList;

public class addressadapter extends RecyclerView.Adapter<addressadapter.viewholder> {
    ArrayList<address_class> list;
    Context context;
    ItemClickListener itemClickListener;
    Integer selectedposition=-1;

    public addressadapter(ArrayList<address_class> list, Context context,ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public addressadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.addresslayout,parent,false);
        return  new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull addressadapter.viewholder holder, int position) {
        Integer pos=position;
        address_class data=list.get(position);
        holder.Name.setText(data.getName());
        holder.address1.setText(data.getFlat_no());
        holder.address2.setText(data.getAddress());
        holder.phone.setText("Phone Number"+data.getMobile());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(pos==selectedposition);
            }
        });
        holder.radioButton.setChecked(pos==selectedposition);
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedposition=holder.getAdapterPosition();
                    itemClickListener.onClick(data);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        ConstraintLayout layout;
        TextView Name,address1,address2,phone;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radiobutton);
            layout=itemView.findViewById(R.id.address_layout);
            Name=itemView.findViewById(R.id.delivery_name);
            address1=itemView.findViewById(R.id.delivery_flat_no);
            address2=itemView.findViewById(R.id.delivery_addressline);
            phone=itemView.findViewById(R.id.delivery_mobile);
        }
    }
}
