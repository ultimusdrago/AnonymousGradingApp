package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.IntegerWidth;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class addCourse extends AppCompatActivity {

    FloatingActionButton backbutton;
    private static final int PICK_CSV_FILE = 1;
    private SharedPreferences sharedPreferences;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_course);

       // ____________________Messing with shared Preferences. Delete below code to revert
       // SharedPreferences sharedPreferences = getSharedPreferences("CSVData", MODE_PRIVATE);
       // SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("EXTRA_DATA", dataToSend);
        //editor.apply();


        //__________________
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        findViewById(R.id.uploadCsvButton).setOnClickListener(v -> openCsvFilePicker());

        findViewById(R.id.viewClassRosterButton).setOnClickListener(v -> navigateToViewClassRoster());

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });


    }
    private void openCsvFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_CSV_FILE);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            readCsvFile(uri);
        }
    }
    private void readCsvFile(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(";;");
            }
            saveStudentDataToPreferences(stringBuilder.toString());
        } catch (Exception e) {
            Log.e("CSVUpload", "Error reading CSV file", e);
        }
    }
    private void saveStudentDataToPreferences(String data) {
        sharedPreferences.edit().putString("studentData", data).apply();
    }



    private void navigateToViewClassRoster() {
        Intent intent = new Intent(this, ViewClassRosterActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }


}
