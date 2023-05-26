package com.example.medic.Buyer;

import static android.view.View.VISIBLE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medic.R;
import com.example.medic.adapters.addressadapter;
import com.example.medic.adapters.cartadapter;
import com.example.medic.model_class.address_class;
import com.example.medic.model_class.products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class cart extends AppCompatActivity implements PaymentResultListener {
    RecyclerView recyclerView;
    RecyclerView address_recycler;
    DatabaseReference ref;
    ArrayList<products> list;
    ArrayList<address_class> address_list;
    addressadapter addressadapter;
    cartadapter adapter;
    FirebaseUser user;
    StorageReference storage;
    DataSnapshot cartsnapshot,productsnapshot;
    Integer Total=0;
    Integer finalamount;
    TextView TotalAmount,prescription_text;
    AppCompatButton addaddress;
    String broadcast_Id,broadcast_type;
    AppCompatButton placeorder,upload_prescription,prescription_status;
    ImageView prescription_image;
    ItemClickListener itemClickListener;
    address_class final_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        BroadcastReceiver newReciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                broadcast_Id=intent.getStringExtra("Id");
                broadcast_type=intent.getStringExtra("Type");
                settotal();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(newReciever,new IntentFilter("custom-message"));
        recyclerView=findViewById(R.id.cart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        TotalAmount=findViewById(R.id.cart_rate);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref=  FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        list=new ArrayList<>();
        adapter=new cartadapter(list,this);
        //Snapshot for products
        ref.child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                productsnapshot=task.getResult();
            }
        });
        ref.child("Cart").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                cartsnapshot= task.getResult();
                if(cartsnapshot.exists()){
                    for(DataSnapshot ds:cartsnapshot.getChildren()) {
                        if (productsnapshot.child(ds.getKey()).exists()){
                            ref.child("products").child(ds.getKey().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task1) {
                                    DataSnapshot snapshot = task1.getResult();
                                    if (snapshot.child("actual_price").exists()) {
                                        Total = Total + Integer.parseInt(String.valueOf(snapshot.child("actual_price").getValue().toString()));
                                    }
                                    TotalAmount.setText("₹"+String.valueOf(Total));
                                    products data = new products(snapshot.child("img1").getValue().toString(), snapshot.child("title").getValue().toString(), snapshot.child("actual_price").getValue().toString(), snapshot.child("id").getValue().toString(), snapshot.child("manufacturer").getValue().toString(), snapshot.child("category").getValue().toString());
                                    list.add(data);
                                    recyclerView.setAdapter(adapter);
                                }

                            });
                        }
                    }
                }
            }
        });
        //address radiobutton
        itemClickListener=new ItemClickListener() {
            @Override
            public void onClick(address_class x) {
                address_recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        addressadapter.notifyDataSetChanged();
                    }

                });
                final_address=x;
            }
        };
        address_recycler=findViewById(R.id.address_recycler);
        address_recycler.setLayoutManager(new LinearLayoutManager(this));
        address_recycler.setHasFixedSize(true);
        address_list=new ArrayList<>();
        addressadapter=new addressadapter(address_list,this,itemClickListener);
        ref.child("Delivery").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot=task.getResult();
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        address_class data=new address_class(ds.child("Name").getValue().toString(),
                                ds.child("Flat_no").getValue().toString(),
                                ds.child("Address").getValue().toString(),
                                ds.child("Mobile").getValue().toString());
                        address_list.add(data);
                    }
                    address_recycler.setAdapter(addressadapter);

                }
            }
        });
        addaddress=findViewById(R.id.addaddress);
        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cart.this, newaddress.class));
            }
        });
        //prescription
        prescription_text=findViewById(R.id.prescription_description);
        prescription_status=findViewById(R.id.prescription_status);
        prescription_image=findViewById(R.id.prescription_image);
        upload_prescription=findViewById(R.id.prescription_upload);
        ref.child("Cart").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot ds=task.getResult();
                    if(ds.exists()){
                        if(ds.child("Prescription_Url").exists()) {
                            Glide.with(cart.this)
                                    .load(ds.child("Prescription_Url").getValue().toString())
                                    .fitCenter()
                                    .into(prescription_image);
                            prescription_text.setVisibility(View.GONE);
                        }
                    }
            }
        });
        ref.child("Cart").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot ds=task.getResult();
                    if(ds.exists()){
                        if(ds.child("Prescription_Status").exists()){
                            prescription_status.setText(ds.child("Prescription_Status").getValue().toString());
                        }
                    }
            }
        });
        upload_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(i.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Upload Prescription as Image only.."),1);
            }
        });

        //Payment
        placeorder=findViewById(R.id.Place_Order);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalamount=Total*100;
                if(final_address!=null && prescription_text.getVisibility()!= VISIBLE){
                    startPayment();
                }
                else{
                    Toast.makeText(cart.this, "Select a Delivery Address & Upload Prescription to Proceed Payment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Checkout.preload(getApplicationContext());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadfiles(data.getData());
        }
    }

    private void uploadfiles(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        storage=FirebaseStorage.getInstance().getReference();
        ref.child("Orders").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds= task.getResult();
                    Integer count=Integer.parseInt(String.valueOf(ds.getChildrenCount()))+1;
                    StorageReference storageReference = storage.child("Prescription_uploads/" + user.getUid()+"/"+String.valueOf(count)+ "/Prescription.jpg");
                    storageReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri = task.getResult();
                                    ref.child("Cart").child(user.getUid()).child("Prescription_Url").setValue(uri.toString());
                                    ref.child("Cart").child(user.getUid()).child("Prescription_Status").setValue("Not verified");
                                    ref.child("Cart").child(user.getUid()).child("Deliver_address").setValue(final_address);
                                    Toast.makeText(cart.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    recreate();
                                }
                            });
                        }
                    }).addOnPausedListener(new OnPausedListener() {
                        @Override
                        public void onPaused(@NonNull Object snapshot) {
                            Log.d(TAG, "onPaused: "+snapshot);
                            Toast.makeText(cart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+e);
                            Toast.makeText(cart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                        }
                    });
                }
        });

    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_YjGMr2Sjkz8Vdh");
        checkout.setImage(R.mipmap.logo);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Medic");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", finalamount);//pass amount in currency subunits
            options.put("prefill.email", "ple.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    public void settotal() {
        Integer Initial=Total;
        ref.child("products").child(broadcast_Id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            DataSnapshot ds = task.getResult();
            if(broadcast_type.equals("Increement")) {
                ref.child("Cart").child(user.getUid()).child(broadcast_Id).child("Count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot countsnapshot=task.getResult();
                        Integer n;
                        if(countsnapshot.exists()) {
                            n = Integer.parseInt(String.valueOf(countsnapshot.getValue()));
                        }
                        else{
                            n=1;
                        }
                        n=n+1;
                        ref.child("Cart").child(user.getUid()).child(broadcast_Id).child("Count").setValue(n);
                    }
                });
                Integer new_total = Initial + (Integer.parseInt(ds.child("actual_price").getValue().toString()));
                TotalAmount.setText("₹"+String.valueOf(new_total));
            }
            else{
                ref.child("Cart").child(user.getUid()).child(broadcast_Id).child("Count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot countsnapshot=task.getResult();
                        Integer n;
                        if(countsnapshot.exists()) {
                            n = Integer.parseInt(String.valueOf(countsnapshot.getValue()));
                        }
                        else{
                            n=1;
                        }
                        n=n-1;
                        ref.child("Cart").child(user.getUid()).child(broadcast_Id).child("Count").setValue(n);
                    }
                });
                Integer new_total = Initial - (Integer.parseInt(ds.child("actual_price").getValue().toString()));
                TotalAmount.setText("₹"+String.valueOf(new_total));
            }
        }});
    }

    Integer order_count;
    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        ref.child("Orders").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                order_count=Integer.parseInt(String.valueOf(ds.getChildrenCount()));
                if(order_count>1){
                    ref.child("Orders").child(user.getUid()).child("1").setValue(cartsnapshot.getValue());
                    ref.child("Orders").child(user.getUid()).child("1").child("Total").setValue(Total);
                    Date date=new Date();
                    ref.child("Orders").child(user.getUid()).child("1").child("Date").setValue(date.toString());
                    ref.child("Cart").child(user.getUid()).removeValue();
                    add_prescription(1);
                    Intent i=new Intent(cart.this,review_page.class);
                    i.putExtra("Id","1");
                    startActivity(i);
                }
                else{
                    order_count=order_count+1;
                    ref.child("Orders").child(user.getUid()).child(""+order_count).setValue(cartsnapshot.getValue());
                    ref.child("Orders").child(user.getUid()).child(""+order_count).child("Total").setValue(Total);
                    Date date=new Date();
                    ref.child("Orders").child(user.getUid()).child(""+order_count).child("Date").setValue(date.toString());
                    ref.child("Cart").child(user.getUid()).removeValue();
                    add_prescription(order_count);
                    Intent i=new Intent(cart.this,review_page.class);
                    i.putExtra("Id",order_count.toString());
                    startActivity(i);
                }
            }
        });
    }

    private void add_prescription(Integer order_count) {
        for(DataSnapshot ds:cartsnapshot.getChildren()){
                if(productsnapshot.child(ds.getKey()).exists()){
                    ref.child("User_Products").child(productsnapshot.child(ds.getKey()).
                            child("Seller Id").getValue().toString()).child(ds.getKey()).
                            child("Prescription_Verification").child(user.getUid()).
                            child(String.valueOf(order_count)).child("Prescription_Url").
                            setValue(cartsnapshot.child("Prescription_Url").getValue().toString());
                }
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("Payment Error","Error:"+s);
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
    }
}