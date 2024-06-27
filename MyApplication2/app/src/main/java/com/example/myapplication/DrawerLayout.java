package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DrawerLayout extends AppCompatActivity {


    androidx.drawerlayout.widget.DrawerLayout drwlayout;
    ImageButton btn;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        btn= findViewById(R.id.buttondrawer);
        navigationView = findViewById(R.id.navigationview);
        drwlayout = findViewById(R.id.drawerlayout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drwlayout.open();
                drwlayout.openDrawer(navigationView);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.viewClasses)
                {
                    //Toast.makeText(DrawerLayout.this,"ViewClassesclicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), viewCourse.class);
                startActivity(intent);

                }

                if(itemId == R.id.addClass)
                {
                    //Toast.makeText(DrawerLayout.this,"addclasses clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), addCourse.class);
                    startActivity(intent);
                }

                if(itemId == R.id.addExam)
                {
                    //Toast.makeText(DrawerLayout.this,"add exam clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), addExam.class);
                    startActivity(intent);

                }
                if(itemId == R.id.nameMaps)
                {
                    //Toast.makeText(DrawerLayout.this,"add exam clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), barCodeNameMaps.class);
                    startActivity(intent);

                }
                if(itemId == R.id.assignGrades)
                {
                    //Toast.makeText(DrawerLayout.this,"add exam clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), assignGrades.class);
                    startActivity(intent);

                }

                if(itemId == R.id.logout)
                {
                   // Toast.makeText(DrawerLayout.this,"ViewClassesclicked",Toast.LENGTH_SHORT).show();

                   // Logout();


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();







                }


                drwlayout.close();
                return false;
            }

        });


        drwlayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }


}