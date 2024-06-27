package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

public class assignGrades extends AppCompatActivity {
    FloatingActionButton backbutton;
    Button camerabtn;
    EditText markscode;

    Button assignGradeBtn, showClassMarksBtn;
    EditText studentIdEditText, marksEditText;
    SharedPreferences sharedPreferences6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_grades);
        backbutton = findViewById(R.id.backbutton);
        markscode = findViewById(R.id.scancode);
        camerabtn = findViewById(R.id.barcodescannerbtn);
        assignGradeBtn = findViewById(R.id.signinbutton);
        showClassMarksBtn = findViewById(R.id.downloadlist);
        studentIdEditText = findViewById(R.id.scancode); // Assuming this is where the student ID is shown after scanning
        marksEditText = findViewById(R.id.marks); // EditText for entering marks
        SharedPreferences sharedPreferences1 = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean receivedValue = sharedPreferences1.getBoolean("YourFlag", false);
        SharedPreferences sharedPreferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        String receivedData = sharedPreferences.getString("EXTRA_DATA", ""); // "" is the default value if key not found

        TextView textView = findViewById(R.id.examNameAssign);
        sharedPreferences6 = getSharedPreferences("CourseData", MODE_PRIVATE);

        if(receivedValue) {
            TextView txtview1 = findViewById(R.id.examNameAssign);
            txtview1.setText(receivedData);
        }
        camerabtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                scanCode();

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });

        assignGradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudentGrade();
            }
        });
        showClassMarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showClassMarks();
                downloadClassMarks();
            }
        });
    }

    private void downloadClassMarks() {
        StringBuilder csvContent = new StringBuilder("Student ID,Student Name,Marks\n");
        Map<String, ?> allEntries = sharedPreferences6.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().equals("studentData") || entry.getKey().equals("EXTRA_DATA") || entry.getKey().equals("YourFlag") || entry.getKey().equals("YourBooleanKey"))
                continue;
            String[] details = entry.getValue().toString().split(",");
            if (details.length == 3) {
                csvContent.append(details[0]).append(",").append(details[1]).append(",").append(details[2]).append("\n");
            }
        }

        // Use MediaStore for Android 10 and above, File for below
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveCsvWithMediaStore(csvContent.toString());
        } else {
            saveCsvWithFile(csvContent.toString());
        }
    }

    private void saveCsvWithMediaStore(String csvContent) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "ClassMarks.csv");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        }
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            out.write(csvContent.getBytes());
            Toast.makeText(this, "Marks downloaded to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to download marks.", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveCsvWithFile(String csvContent) {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, "ClassMarks.csv");
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(csvContent.getBytes());
            Toast.makeText(this, "Marks downloaded to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to download marks.", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveStudentGrade() {
        String studentId = studentIdEditText.getText().toString().trim();
        String marks = marksEditText.getText().toString().trim();
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String studentData = sharedPreferences.getString("studentData", "");
       // Log.d("Treshold", "Student Data: " + studentData);

        if (!studentId.isEmpty() && !marks.isEmpty()) {
            //String studentData4 = sharedPreferences6.getString("studentData", "");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String studentData = sharedPreferences.getString("studentData", "");
            Log.d("assignGrades", "Student Data: " + studentData);
            // Assuming studentData format is "id,name;;id,name;;"
            String[] students = studentData.split(";;");
            boolean found = false;
            for (String record : students) {
                String[] details = record.split(",");
                Log.d("details", "Student Data: " + details);
                if (details.length == 2 && details[1].trim().equals(studentId)) { // Adjusted the index and added trim()
                    String studentName = details[0].trim();

                    sharedPreferences6.edit().putString("Grade_" + studentId, studentId + "," + studentName + "," + marks).apply();
                    Toast.makeText(this, "Grade assigned successfully.", Toast.LENGTH_SHORT).show();
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(this, "Student ID not found.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Student ID or marks cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showClassMarks() {
        StringBuilder allGrades = new StringBuilder();
        Map<String, ?> allEntries = sharedPreferences6.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains("studentData")) continue; // Skip the entry that contains all student data
            // Format of saved data is "id,name,marks"
            String[] details = entry.getValue().toString().split(",");
            if (details.length == 3) { // Make sure the data is in the expected format
                allGrades.append("ID: ").append(details[0]).append(", Name: ").append(details[1]).append(", Marks: ").append(details[2]).append("\n");
            }
        }

        // Display all grades, for example, in a dialog
        new AlertDialog.Builder(this)
                .setTitle("Class Marks")
                .setMessage(allGrades.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                    }
                })
                .show();
    }

    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){

                String codeopt = result.getContents();
                markscode.setText(codeopt);

            }
            else {
                Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }


    }
    @Override
    public void onBackPressed() {

    }

}