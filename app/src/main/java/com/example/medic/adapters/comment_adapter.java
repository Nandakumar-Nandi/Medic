package com.example.medic.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.R;
import com.example.medic.model_class.rating_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class comment_adapter extends RecyclerView.Adapter<comment_adapter.viewholder> {
    ArrayList<rating_class>list;
    Context context;
    DatabaseReference ref=FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");;
    public comment_adapter(ArrayList<rating_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public comment_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.comment_layout,parent,false);
        return new viewholder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull comment_adapter.viewholder holder, int position) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        rating_class data=list.get(position);
        holder.user.setText(data.getUser());
        holder.comment.setText(data.getComment());
        holder.rating.setText(data.getRating());
        holder.like_count.setText(data.getLike());
        holder.dislike_count.setText(data.getDislike());
        ref.child("Rating").child(data.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                for(DataSnapshot snapshot:ds.getChildren()) {
                    if(data.getComment().equals(String.valueOf(snapshot.child("Comment").getValue()))) {
                            if (snapshot.child("Likes").child(user.getUid()).exists()) {
                                holder.like.setColorFilter(ContextCompat.getColor(context, com.facebook.R.color.com_facebook_blue), PorterDuff.Mode.SRC_IN);
                            }
                            if (snapshot.child("DisLikes").child(user.getUid()).exists()) {
                                holder.dislike.setColorFilter(ContextCompat.getColor(context, com.facebook.R.color.com_facebook_blue), PorterDuff.Mode.SRC_IN);
                            }
                    }
                }
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Rating").child(data.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot d1=task.getResult(),snapshot1;
                        for(DataSnapshot snapshot: d1.getChildren()) {
                            if(data.getComment().equals(String.valueOf(snapshot.child("Comment").getValue()))) {
                                snapshot1=snapshot;
                            if (snapshot1.child("Likes").child(user.getUid()).exists()) {
                                ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("Likes").child(user.getUid()).removeValue();
                                Integer count=Integer.parseInt(holder.like_count.getText().toString())-1;
                                if(count<0){
                                    count=0;
                                }
                                holder.like_count.setText(String.valueOf(count));
                                holder.like.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                            } else {
                                ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("Likes").child(user.getUid()).setValue("");
                                Integer count=Integer.parseInt(holder.like_count.getText().toString())+1;
                                holder.like_count.setText(String.valueOf(count));
                                holder.like.setColorFilter(ContextCompat.getColor(context, com.facebook.R.color.com_facebook_blue), PorterDuff.Mode.SRC_IN);
                                if (snapshot1.child("DisLikes").child(user.getUid()).exists()) {
                                    ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("DisLikes").child(user.getUid()).removeValue();
                                    Integer count1=Integer.parseInt(holder.dislike_count.getText().toString())-1;
                                    if(count1<0){
                                        count1=0;
                                    }
                                    holder.dislike_count.setText(String.valueOf(count1));
                                    holder.dislike.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                                }
                            }
                        }
                        }
                    }
                });
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Rating").child(data.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot d1=task.getResult(),snapshot1;
                        for(DataSnapshot snapshot: d1.getChildren()) {
                            if(data.getComment().equals(String.valueOf(snapshot.child("Comment").getValue()))) {
                                snapshot1=snapshot;
                                if (snapshot1.child("DisLikes").child(user.getUid()).exists()) {
                                    ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("DisLikes").child(user.getUid()).removeValue();
                                    Integer count=Integer.parseInt(holder.dislike_count.getText().toString())-1;
                                    if(count<0){
                                        count=0;
                                    }
                                    holder.dislike_count.setText(String.valueOf(count));
                                    holder.dislike.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                                } else {
                                    ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("DisLikes").child(user.getUid()).setValue("");
                                    Integer count=Integer.parseInt(holder.dislike_count.getText().toString())+1;
                                    holder.dislike_count.setText(String.valueOf(count));
                                    holder.dislike.setColorFilter(ContextCompat.getColor(context, com.facebook.R.color.com_facebook_blue), PorterDuff.Mode.SRC_IN);
                                    if (snapshot1.child("Likes").child(user.getUid()).exists()) {
                                        ref.child("Rating").child(data.getId()).child(snapshot1.getKey()).child("Likes").child(user.getUid()).removeValue();
                                        Integer count1=Integer.parseInt(holder.like_count.getText().toString())-1;
                                        if(count1<0){
                                            count1=0;
                                        }
                                        holder.like_count.setText(String.valueOf(count1));
                                        holder.like.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        TextView comment,user,rating,like_count,dislike_count;
        ImageView like,dislike;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment);
            user=itemView.findViewById(R.id.comment_user);
            rating=itemView.findViewById(R.id.comment_rating);
            like=itemView.findViewById(R.id.comment_like);
            dislike=itemView.findViewById(R.id.comment_dislike);
            like_count=itemView.findViewById(R.id.comment_like_count);
            dislike_count=itemView.findViewById(R.id.comment_dislike_count);
        }
    }
}
