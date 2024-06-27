package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearSharedPreferences();
        clearStudentdata();
        clearBooleanValues();


        btn1  = findViewById(R.id.login);
        btn2 = findViewById(R.id.signup);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

    }

    private void clearStudentdata() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
    private void clearBooleanValues() {
        SharedPreferences preferences2 = getSharedPreferences("AppSettings", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences2.edit();
        editor.clear();
        editor.apply();
    }
    private void clearSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        //SharedPreferences preferences2 = getSharedPreferences("com.example.myapplication_preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    @Override
    public void onBackPressed() {

    }
}