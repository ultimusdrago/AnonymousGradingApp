package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {
    FloatingActionButton backbutton;

    Button btn;

    TextView txtview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.signinbutton);

        //if();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //_____________________________________________________________

                //Revert the changes to drawyerlayout.class for navigation drawer
                //--------------------------------------------------------------

                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });
        txtview = (TextView) findViewById(R.id.signup);
        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}