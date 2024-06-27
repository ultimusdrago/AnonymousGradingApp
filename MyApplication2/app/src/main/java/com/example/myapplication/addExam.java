package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class addExam extends AppCompatActivity {
    private EditText examTypeEditText;
    //private LinearLayout barcodeContainer;
    private SharedPreferences sharedPreferences;

    private boolean isTrue;

    private boolean flag;

    Button genbutton;

    FloatingActionButton bckbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        //clearSharedPreferences();

        bckbtn = findViewById(R.id.backbutton);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });


        examTypeEditText = findViewById(R.id.examTypeEditText);
       // barcodeContainer = findViewById(R.id.barcodeContainer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isTrue = false;
        flag = false;
        genbutton = findViewById(R.id.generateBarcodeButton);
        genbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBarcodesForStudents();
                saveData();
            }
        });

       // findViewById(R.id.generateBarcodeButton).setOnClickListener(v -> generateBarcodesForStudents());
    }

    public void saveData() {
        EditText editText = findViewById(R.id.examTypeEditText);
        String dataToSend = editText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EXTRA_DATA", dataToSend);
        editor.apply();
    }

    private void generateBarcodesForStudents() {
        String examType = examTypeEditText.getText().toString().trim();
        if (examType.isEmpty()) {
            examTypeEditText.setError("Please enter the exam type");
            return;
        }

        String combinedData = sharedPreferences.getString("studentData", "");
        if (combinedData.isEmpty()) {
            Log.d("GenerateBarcodes", "No data found in SharedPreferences.");
            return;
        }
        else{
            //isTrue = true;


            savedata(true);
            saveBooleanValue(true);
           // Intent intent = new Intent(addExam.this, barCodeNameMaps.class);
            //intent.putExtra("isTrue", isTrue);
            //startActivity(intent);


        }

        String[] records = combinedData.split(";;");
        //barcodeContainer.removeAllViews();

        for (String record : records) {
            String[] parts = record.split(",");
            if (parts.length == 2) {
                String name = parts[0];
                String id = parts[1];
                Bitmap barcodeBitmap = generateBarcodeBitmap(id, 600, 300);
               /* if (barcodeBitmap != null) {
                    View barcodeView = getLayoutInflater().inflate(R.layout.barcode_item, barcodeContainer, false);
                    TextView studentNameTextView = barcodeView.findViewById(R.id.studentNameTextView);
                    ImageView barcodeImageView = barcodeView.findViewById(R.id.barcodeImageView);

                    studentNameTextView.setText(String.format("%s (%s)", name, examType));
                    barcodeImageView.setImageBitmap(barcodeBitmap);

                    barcodeContainer.addView(barcodeView);
                } */
            }
        }

    }

    public void savedata(boolean value) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putBoolean("YourFlag", value);
        editor.apply();
    }
    public void saveBooleanValue(boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("YourBooleanKey", value);
        editor.apply();
    }

    private Bitmap generateBarcodeBitmap(String data, int width, int height) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.CODE_128, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            Log.e("GenerateBarcodes", "Error generating barcode", e);
            return null;
        }
    }

    @Override
    public void onBackPressed() {

    }
}

