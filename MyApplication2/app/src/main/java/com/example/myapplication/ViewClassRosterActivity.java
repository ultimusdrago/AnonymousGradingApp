package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewClassRosterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ListView listView;
    FloatingActionButton bckbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_roster);
        bckbtn = findViewById(R.id.backbutton);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addCourse.class);
                startActivity(intent);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listView = findViewById(R.id.listView);

        List<String> roster = getClassRoster();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roster);
        listView.setAdapter(adapter);
    }

    private List<String> getClassRoster() {
        List<String> roster = new ArrayList<>();
        String data = sharedPreferences.getString("studentData", "");
        if (!data.isEmpty()) {
            String[] records = data.split(";;");
            for (String record : records) {
                roster.add(record.replace(",", " - "));
            }
        }
        return roster;
    }
}
