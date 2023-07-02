package com.example.medic.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medic.R;
import com.example.medic.imageviewdialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class add_products extends AppCompatActivity {
    DatabaseReference ref;
    EditText stock;
    TextInputEditText title, manufacturer, mfd, expiry, actual_rate, crossed_rate, country, category, description;
    Button save, reset;
    ImageCarousel carousel;
    String product_id;
    Integer carousel_image_count = 0;
    FirebaseUser user;
    ArrayList<CarouselItem> images;
    ImageView edit, delete;
    CardView image_properties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        stock = findViewById(R.id.add_product_stock);
        title = findViewById(R.id.add_product_title);
        manufacturer = findViewById(R.id.add_product_manuacturer);
        mfd = findViewById(R.id.add_product_mfd);
        expiry = findViewById(R.id.add_product_expiry);
        actual_rate = findViewById(R.id.add_product_price);
        crossed_rate = findViewById(R.id.add_product_crossed_price);
        country = findViewById(R.id.add_product_country);
        category = findViewById(R.id.add_product_category);
        description = findViewById(R.id.add_product_description);
        save = findViewById(R.id.add_product_changes);
        reset = findViewById(R.id.add_product_reset);
        carousel = findViewById(R.id.add_product_images);
        product_id = getIntent().getStringExtra("Id");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        image_properties = findViewById(R.id.edit_product_edit_image);
        edit = findViewById(R.id.image_edit);
        delete = findViewById(R.id.image_delete);
        images = new ArrayList<>();
        images.add(new CarouselItem(R.drawable.add));
        images.add(new CarouselItem(R.drawable.add));
        images.add(new CarouselItem(R.drawable.add));
        carousel.addData(images);
        carousel.setCarouselListener(new CarouselListener() {
            @Override
            public void onClick(int i, @NonNull CarouselItem carouselItem) {
                image_properties.setVisibility(View.GONE);
                Intent in = new Intent(add_products.this, imageviewdialog.class);
                in.putExtra("Url", carouselItem.getImageUrl());
                startActivity(in);
            }

            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

            }

            @Override
            public void onLongClick(int i, @NonNull CarouselItem carouselItem) {
                carousel_image_count = carousel.getCurrentPosition() + 1;
                image_properties.setVisibility(View.VISIBLE);

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeimage();
                image_properties.setVisibility(View.GONE);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("products").child(product_id).child("img" + carousel_image_count).setValue("");
                images.set(carousel_image_count - 1, new CarouselItem(R.drawable.add));
                carousel.setData(images);
                image_properties.setVisibility(View.GONE);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setdata();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
            }
        });
    }

    private void changeimage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null && resultCode == RESULT_OK) {
            uploadimage(data.getData());
        } else {
            Toast.makeText(this, "Select A Valid File", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadimage(Uri data) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading Image");
        dialog.show();
        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://medic-1c997.appspot.com");
        StorageReference storageReference = storage.child("Products").child(product_id).child((carousel_image_count) + ".jpg");
        storageReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        ref.child("products").child(product_id).child("img" + carousel_image_count).setValue(uri.toString());
                        Toast.makeText(add_products.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        images.set(carousel_image_count - 1, new CarouselItem(uri.toString()));
                        carousel.setData(images);
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded:" + (int) progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_products.this, "Something Went Wrong!! Try Again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    private void getdata() {
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                product_id= String.valueOf(ds.getChildrenCount());
            }
        });
        ref.child("User_Products").child(user.getUid()).child(product_id).child("Stock").setValue(stock.getText().toString());
        ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/products/" + product_id + "/");
        ref.child("title").setValue(title.getText().toString());
        ref.child("manufacturer").setValue(manufacturer.getText().toString());
        ref.child("actual_price").setValue(actual_rate.getText().toString());
        ref.child("crossed_price").setValue(crossed_rate.getText().toString());
        ref.child("Manufactured Date").setValue(mfd.getText().toString());
        ref.child("Expiry date").setValue(expiry.getText().toString());
        ref.child("category").setValue(category.getText().toString());
        ref.child("country").setValue(country.getText().toString());
        ref.child("Description").setValue(description.getText().toString());
    }
    private void setdata() {
        title.setText("");
        manufacturer.setText("");
        mfd.setText("");
        expiry.setText("");
        actual_rate.setText("");
        crossed_rate.setText("");
        country.setText("");
        description.setText("");
        category.setText("");
        images.add(new CarouselItem(R.drawable.add));
        images.add(new CarouselItem(R.drawable.add));
        images.add(new CarouselItem(R.drawable.add));
        carousel.addData(images);
    }



}