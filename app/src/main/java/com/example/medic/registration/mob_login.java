package com.example.medic.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medic.R;
import com.google.android.material.textfield.TextInputEditText;

public class mob_login extends AppCompatActivity {
    TextInputEditText mob;
    Button cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_login);
        mob=findViewById(R.id.mobile_no);
        cont=findViewById(R.id.continue_mob);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile_no="+91"+mob.getText().toString();
                Intent i=new Intent(mob_login.this, Otpverification.class);
                i.putExtra("phone",mobile_no);
                startActivity(i);
            }
        });
    }
}