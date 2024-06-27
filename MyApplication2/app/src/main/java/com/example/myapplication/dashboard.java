package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
       // clearSharedPreferences();

        LinearLayout lyt1,lyt2,lyt3,lyt4,lyt5,lyt6;
        lyt1=findViewById(R.id.viewlayout);
        lyt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), viewCourse.class);
                startActivity(intent);
            }
        });

        lyt2=findViewById(R.id.addcourselayout);
        lyt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addCourse.class);
                startActivity(intent);
            }
        });
        lyt3=findViewById(R.id.addexamlayout);
        lyt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addExam.class);
                startActivity(intent);
            }
        });
        lyt4=findViewById(R.id.barcodelayout);
        lyt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), barCodeNameMaps.class);
                startActivity(intent);
            }
        });
        lyt5=findViewById(R.id.assigngradelayout);
        lyt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), assignGrades.class);
                startActivity(intent);
            }
        });
        lyt6=findViewById(R.id.logoutlayout);
        lyt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}