package com.example.muve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    ImageView im5;
    ImageView im6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        im1 = findViewById(R.id.stt1);
        im2 = findViewById(R.id.stt2);
        im3 = findViewById(R.id.stt3);
        im4 = findViewById(R.id.stt4);
        im5 = findViewById(R.id.stt5);
        im6 = findViewById(R.id.stt6);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 0;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 1;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 2;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 3;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 4;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        im6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mySt = 5;
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
    }
}
