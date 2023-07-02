package com.example.medic.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medic.Buyer.Home;
import com.example.medic.R;
import com.example.medic.Seller.seller_home;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
    Button signup,login,phone_auth;
    SignInButton gmail;
    LoginButton fb;
    GoogleSignInOptions gopt;
    GoogleSignInClient gclient;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CallbackManager mCallbackManager;
    DatabaseReference myRef ;
    TextInputEditText uid,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        gmail = findViewById(R.id.google);
        fb = findViewById(R.id.facebook);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myRef =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://medic-1c997-default-rtdb.firebaseio.com/");
        signup = findViewById(R.id.signup);
        login=findViewById(R.id.login_btn_1);
        uid=findViewById(R.id.login_uid);
        pass=findViewById(R.id.login_password);
        phone_auth=findViewById(R.id.phone_auth);
        /* login with credentials */
        if(mUser!=null){
            myRef.child("users").child("Buyer").child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task){
                    DataSnapshot ds=task.getResult();
                    if(ds.exists()){
                        startActivity(new Intent(login.this, Home.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(login.this, seller_home.class));
                        finish();
                    }
                }
            });
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String email = uid.getText().toString();
                 String password = pass.getText().toString();
                 mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             mUser = mAuth.getCurrentUser();
                             myRef.child("users").child("Buyer").child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<DataSnapshot> task){
                                     DataSnapshot ds=task.getResult();
                                     if(ds.exists()){
                                         startActivity(new Intent(login.this, Home.class));
                                         finish();
                                     }
                                     else{
                                         startActivity(new Intent(login.this, seller_home.class));
                                         finish();
                                     }
                                 }
                             });
                         }
                         else{
                             Toast.makeText(login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                         }

                     }
                 });
            }
        });
        //End

        //Phone Auth
        phone_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, mob_login.class));

            }
        });
        //Checking if user is already logged in
        //End


        //Login with user data

        //Signup page
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(login.this, registration.class);
                i.putExtra("Type","Phone");
                startActivity(i);
            }
        });
        //End

        //Login with gmail button
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gopt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                gclient = GoogleSignIn.getClient(login.this, gopt);
                signin();
            }
        });
        //End
        // Facebook Login button
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackManager = CallbackManager.Factory.create();
                fb.setReadPermissions("email", "public_profile");
                fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(login.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(login.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //End
    }
    //Token handling for facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("",""+token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(login.this, usertype.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //End

    //Google login starts
    private void signin() {
     Intent i = gclient.getSignInIntent();
        startActivityForResult(i, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                auth(acc.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "ERROR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void auth(String idToken) {
        AuthCredential cred= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(cred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(login.this, usertype.class));
                        } else {
                            Toast.makeText(login.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
        }
    }
    //Google Login ends
