package com.example.medic;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medic.Buyer.Home;
import com.example.medic.adapters.comment_adapter;
import com.example.medic.model_class.rating_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class productview extends AppCompatActivity {
    ImageCarousel carousel;
    TextView title,manufacturer,country,description,price,totalstars,expiry_date;
    AppCompatButton cart,back;
    DatabaseReference ref;
    String Id;
    FirebaseUser fuser;
    DataSnapshot ds;
    Integer Total;
    RatingBar main;
    ProgressBar star_5,star_4,star_3,star_2,star_1;
    TextView total_reviews,star5,star4,star3,star2,star1;
    RecyclerView recyclerView;
    ArrayList<rating_class> list;
    comment_adapter adapter;
    Integer total_ratings=0,total_comments=0,onestar=0,twostar=0,threestar=0,fourstar=0,fivestar=0;
    Float rating_data=Float.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productview);
        carousel=findViewById(R.id.productviewcarousel);
        title=findViewById(R.id.title);
        manufacturer=findViewById(R.id.manufacturer);
        country=findViewById(R.id.country);
        description=findViewById(R.id.description);
        expiry_date=findViewById(R.id.expiry_date);
        price=findViewById(R.id.rate);
        cart=findViewById(R.id.addcart);
        back=findViewById(R.id.backtohome);
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        ref=  FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        Id=getIntent().getStringExtra("Id");
        ref.child("products").child(Id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ds=task.getResult();
                    if(ds.exists()){
                        carousel.addData(new CarouselItem(ds.child("img1").getValue().toString()));
                        if(!ds.child("img2").getValue().toString().equals("")){
                            carousel.addData(new CarouselItem(ds.child("img2").getValue().toString()));
                        }
                        if(!ds.child("img3").getValue().toString().equals("")){
                            carousel.addData(new CarouselItem(ds.child("img3").getValue().toString()));
                        }
                        title.setText(ds.child("title").getValue().toString());
                        manufacturer.setText(ds.child("manufacturer").getValue().toString());
                        country.setText(ds.child("country").getValue().toString());
                        description.setText(String.valueOf(ds.child("description").getValue()));
                        price.setText("₹"+ds.child("actual_price").getValue().toString());
                        expiry_date.setText(String.valueOf(ds.child("Expiry date").getValue()));
                    }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(productview.this, Home.class));
                finish();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Cart").child(fuser.getUid()).child(ds.child("id").getValue().toString()).setValue("");
            }
        });
        main=findViewById(R.id.productview_ratingbar);
        star_1=findViewById(R.id.progressbar_1);
        star_2=findViewById(R.id.progressbar_2);
        star_3=findViewById(R.id.progressbar_3);
        star_4=findViewById(R.id.progressbar_4);
        star_5=findViewById(R.id.progressbar_5);
        star1=findViewById(R.id.star1_text);
        star2=findViewById(R.id.star2_text);
        star3=findViewById(R.id.star3_text);
        star4=findViewById(R.id.star4_text);
        star5=findViewById(R.id.star5_text);
        totalstars=findViewById(R.id.productview_startotal);
        total_reviews=findViewById(R.id.total_ratings_reviews);
        recyclerView=findViewById(R.id.productview_comments_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        adapter=new comment_adapter(list,this);
        ref.child("Rating").child(Id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot=task.getResult();
                if(snapshot.exists()){
                         for(DataSnapshot d1:snapshot.getChildren()){
                             if(!String.valueOf(d1.child("Rating").getValue()).equals("")){
                                 total_ratings=total_ratings+1;
                                 rating_data=rating_data+parseFloat(String.valueOf(d1.child("Rating").getValue()));
                                 Toast.makeText(productview.this, ""+(int)Math.floor(parseFloat(d1.child("Rating").getValue().toString())), Toast.LENGTH_SHORT).show();
                                 switch ((int)Math.floor(parseFloat(d1.child("Rating").getValue().toString()))){
                                     case 1 : onestar=onestar+1;
                                                break;
                                     case 2 : twostar=twostar+1;
                                                break;
                                     case 3 : threestar=threestar+1;
                                                break;
                                     case 4 : fourstar=fourstar+1;
                                                break;
                                     case 5 : fivestar=fivestar+1;
                                                break;
                                 }
                             }
                             if(!String.valueOf(d1.child("Comment").getValue()).equals("")) {
                                 total_comments = total_comments + 1;
                                 addclass(d1.getKey(), String.valueOf(d1.child("Rating").getValue()), String.valueOf(d1.child("Comment").getValue()), String.valueOf(d1.child("Likes").getChildrenCount()), String.valueOf(d1.child("Dis hhhhhhhhhhhhhhhhhgLikes").getChildrenCount()));
                             }
                         }
                }
                total_reviews.setText(total_ratings+" ratings &"+total_comments+" comments");
                main.setRating(rating_data/Float.valueOf(total_ratings));
                totalstars.setText(String.valueOf(rating_data/Float.valueOf(total_ratings)));
                star_1.setProgress((int) ((double)onestar/total_ratings*100));
                star_2.setProgress((int) ((double)twostar/total_ratings*100));
                star_3.setProgress((int) ((double)threestar/total_ratings*100));
                star_4.setProgress((int) ((double)fourstar/total_ratings*100));
                star_5.setProgress((int) ((double)fivestar/total_ratings*100));
                star1.setText((int) ((double)onestar/total_ratings*100)+"%");
                star2.setText((int) ((double)twostar/total_ratings*100)+"%");
                star3.setText((int) ((double)threestar/total_ratings*100)+"%");
                star4.setText((int) ((double)fourstar/total_ratings*100)+"%");
                star5.setText((int) ((double)fivestar/total_ratings*100)+"%");
            }
        });

    }

    private void addclass(String key, String rating, String comment, String like, String dislike) {
            ref.child("users").child("Buyer").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot d=task.getResult();
                    rating_class data=new rating_class(Id,rating,comment,String.valueOf(d.child("Name").getValue()),like,dislike);
                    list.add(data);
                    recyclerView.setAdapter(adapter);
                }
            });
    }
}