package com.example.medic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class imageviewdialog extends AppCompatActivity {
    ImageView pic,close,fullscreeen;
    Integer width,height;
    Boolean Extended=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewdialog);
        close=findViewById(R.id.close);
        fullscreeen=findViewById(R.id.full_screen);
        pic=findViewById(R.id.dialog_pic);
        String Url=getIntent().getStringExtra("Url");
        Glide.with(this)
                .load(Url)
                .into(pic);
        imageviewdialog.this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        fullscreeen.setImageResource(R.drawable.ic_baseline_close_fullscreen_24);
        fullscreeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Extended){
                    fullscreeen.setImageResource(R.drawable.fullscreen);
                    imageviewdialog.this.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    Extended=false;
                }
                else{
                    imageviewdialog.this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    fullscreeen.setImageResource(R.drawable.ic_baseline_close_fullscreen_24);
                    Extended=true;
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}