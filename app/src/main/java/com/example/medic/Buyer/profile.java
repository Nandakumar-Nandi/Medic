package com.example.medic.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.medic.R;
import com.example.medic.imageviewdialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profile extends AppCompatActivity {
    BottomNavigationView nav;
    ConstraintLayout fl;
    ImageView pro_pic,edit_email,edit_mob,edit_pass;
    EditText email,mob,password;
    TextView name,type,view_pic,change_pic;
    DatabaseReference ref;
    StorageReference sref;
    FirebaseUser user;
    String Name="",Type="",Mobile="",Email="",Password="",real_id,Url="";
    Task<DataSnapshot> buyert,sellert;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Navigation Bar
        nav=findViewById(R.id.nav_view);
         nav.setSelectedItemId(R.id.Profile);
         nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_page:
                    Intent I1=new Intent(profile.this, Home.class);
                    Bundle b1= ActivityOptions.makeSceneTransitionAnimation(profile.this).toBundle();
                    startActivity(I1,b1);
                    return true;
                case R.id.Profile:  return true;
                case R.id.Order:
                    Intent I2=new Intent(profile.this, orders.class);
                    Bundle b2= ActivityOptions.makeSceneTransitionAnimation(profile.this).toBundle();
                    startActivity(I2,b2);
                    return true;
                case R.id.Settings:
                    Intent I3=new Intent(profile.this, settings.class);
                    Bundle b3= ActivityOptions.makeSceneTransitionAnimation(profile.this).toBundle();
                    startActivity(I3,b3);
                    return true;
                default:return true;
            }
        });
        //End
        //Other Declaration
        pro_pic=findViewById(R.id.profile_pic);
        fl=findViewById(R.id.img_opt);
        email=findViewById(R.id.profile_email);
        mob=findViewById(R.id.profile_mobile);
        password=findViewById(R.id.profile_password);
        name=findViewById(R.id.profile_username);
        type=findViewById(R.id.profile_acctype);
        edit_email=findViewById(R.id.edit_email);
        edit_mob=findViewById(R.id.edit_mobile);
        edit_pass=findViewById(R.id.edit_password);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref=  FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        save=findViewById(R.id.profile_save);
        view_pic=findViewById(R.id.view_pic);
        change_pic=findViewById(R.id.change_pic);
        ref.child("uploads").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds = task.getResult();
                if (ds.exists()) {
                    Url=ds.child("url").getValue().toString();
                    Glide.with(profile.this)
                            .load(Url)
                            .centerCrop()
                            .transform(new CircleCrop())
                            .into(pro_pic);
                }
            }
        });
        view_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(profile.this, imageviewdialog.class);
                i.putExtra("Url",Url);
                startActivity(i);
            }
        });
        //End
        //Fetching user Details
        //1.Checking the user type
        real_id=user.getUid();
        //1.End
        //2.Setting Textviews
        buyert=ref.child("users").child("Buyer").child(real_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot buyerds=buyert.getResult();
                if(buyerds.exists()){
                    Name=buyerds.hasChild("Name")?buyerds.child("Name").getValue().toString() :"";
                    Type=buyerds.hasChild("Type") ? buyerds.child("Type").getValue().toString() : "";
                    Email=buyerds.hasChild("Email") ? buyerds.child("Email").getValue().toString() : "";
                    Mobile=buyerds.hasChild("Mobile") ? buyerds.child("Mobile").getValue().toString() : "";
                    Password=buyerds.hasChild("Password") ? buyerds.child("Password").getValue().toString() : "";
                    name.setText(Name);
                    type.setText(Type);
                    email.setText(Email);
                    mob.setText(Mobile);
                    password.setText(Password);
                }
            }
        });
        sellert=ref.child("users").child("Seller").child(real_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot sellerds= sellert.getResult();
                if(sellerds.exists()){
                    Name=(sellerds.hasChild("Name")) ? sellerds.child("Name").getValue().toString() : "";
                    Type=(sellerds.hasChild("Type")) ? sellerds.child("Type").getValue().toString() : "";
                    Email=(sellerds.hasChild("Email")) ? sellerds.child("Email").getValue().toString() : "";
                    Mobile=(sellerds.hasChild("Mobile")) ? sellerds.child("Mobile").getValue().toString() : "";
                    Password=(sellerds.hasChild("Password")) ? sellerds.child("Password").getValue().toString() : "";
                    name.setText(Name);
                    type.setText(Type);
                    email.setText(Email);
                    mob.setText(Mobile);
                    password.setText(Password);
                }
            }
        });
        //2.End
        //End

        //Enabling Editext boxes
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setEnabled(true);
                email.requestFocus();
            }
        });
        edit_mob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mob.setEnabled(true);
                mob.requestFocus();
            }
        });
        edit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setEnabled(true);
                password.requestFocus();
            }
        });

        //Saving Changes
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("users").child(Type).child(real_id).child("Email").setValue(email.getText().toString());
                ref.child("users").child(Type).child(real_id).child("Mobile").setValue(mob.getText().toString());
                ref.child("users").child(Type).child(real_id).child("Password").setValue(password.getText().toString());
            }
        });
        //Profile Picture operations
        fl.setElevation(Float.valueOf(2));
        pro_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fl.getVisibility() == View.GONE){
                    fl.setVisibility(View.VISIBLE);
                    fl.setElevation(5);
                }
                else{
                    fl.setVisibility(View.GONE);
                }
            }
        });
        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(i.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Choose Your image.."),1);
            }
        });

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
        sref=FirebaseStorage.getInstance().getReference();
        StorageReference storageReference=sref.child("uploads/"+user.getUid()+"/profile.jpg");
        storageReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri=task.getResult();
                        ref.child("uploads").child(user.getUid()).child("url").setValue(uri.toString());
                        Toast.makeText(profile.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded:"+(int)progress+"%");
            }
        });

    }
}