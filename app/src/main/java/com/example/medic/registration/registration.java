package com.example.medic.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {
    Button signup,login;
    TextInputEditText name,email,phone,password;
    String str_name,str_email,str_phone,str_password;
    DatabaseReference ref;
    DataSnapshot ds;
    FirebaseAuth mAuth;
    Boolean buy=false,sell=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        signup=findViewById(R.id.reg_signup);
        login=findViewById(R.id.reg_login);
        name=findViewById(R.id.reg_name);
        email=findViewById(R.id.reg_email);
        phone=findViewById(R.id.reg_mobile);
        password=findViewById(R.id.reg_password);
        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str_email = email.getText().toString();
                        str_name = name.getText().toString();
                        str_phone = "+91" + phone.getText().toString();
                        str_password=password.getText().toString();
                        if (str_name.equals("") || str_email.equals("") || str_phone.equals("") || str_password.equals("")) {
                            Toast.makeText(registration.this, "Field's can't be Empty", Toast.LENGTH_SHORT).show();
                        }
                        else{
                                mAuth.createUserWithEmailAndPassword(str_email,str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            FirebaseUser user=mAuth.getCurrentUser();
                                            mAuth.updateCurrentUser(user);
                                            Intent I = new Intent(registration.this, usertype.class);
                                            I.putExtra("name", str_name);
                                            I.putExtra("email", str_email);
                                            I.putExtra("mobile",str_phone);
                                            I.putExtra("password", str_password);
                                            startActivity(I);
                                        }
                                        else{
                                            Toast.makeText(registration.this, "Account not created try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        }

                    }
                });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registration.this, login.class));
            }
        });
    }
}