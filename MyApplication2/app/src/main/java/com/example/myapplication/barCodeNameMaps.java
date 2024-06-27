package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

public class barCodeNameMaps extends AppCompatActivity {

    FloatingActionButton backbutton;
    private LinearLayout barcodeContainer;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_name_maps);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), dashboard.class);
            startActivity(intent);
            finish();
        });

        barcodeContainer = findViewById(R.id.barcodeContainer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkPermissions();

        String combinedData = sharedPreferences.getString("studentData", "");
        if (combinedData.isEmpty()) {
            Log.d("GenerateBarcodes", "No data found in SharedPreferences.");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean receivedValue = sharedPreferences.getBoolean("YourBooleanKey", false);

        if(receivedValue) {
            generateBarcodesForStudents();
        }

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(v -> downloadBarcodes());
    }

    private void generateBarcodesForStudents() {
        String combinedData = sharedPreferences.getString("studentData", "");
        if (combinedData.isEmpty()) {
            Log.d("GenerateBarcodes", "No data found in SharedPreferences.");
            return;
        }

        String[] records = combinedData.split(";;");
        barcodeContainer.removeAllViews();

        for (String record : records) {
            String[] parts = record.split(",");
            if (parts.length == 2) {
                String name = parts[0];
                String id = parts[1];
                Bitmap barcodeBitmap = generateBarcodeBitmap(id, 600, 300);
                if (barcodeBitmap != null) {
                    View barcodeView = getLayoutInflater().inflate(R.layout.barcode_item, barcodeContainer, false);
                    TextView studentNameTextView = barcodeView.findViewById(R.id.studentNameTextView);
                    ImageView barcodeImageView = barcodeView.findViewById(R.id.barcodeImageView);

                    studentNameTextView.setText(String.format("%s - %s", name, id));
                    barcodeImageView.setImageBitmap(barcodeBitmap);

                    barcodeContainer.addView(barcodeView);
                }
            }
        }
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

    private void downloadBarcodes() {
        String combinedData = sharedPreferences.getString("studentData", "");
        if (combinedData.isEmpty()) {
            Toast.makeText(this, "No data to download", Toast.LENGTH_SHORT).show();
            return;
        }

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            String[] records = combinedData.split(";;");
            int count = 0;

            for (String record : records) {
                if (count % 2 == 0 && count > 0) {
                    document.newPage();  // Start a new page after every two barcodes
                }
                String[] parts = record.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    String id = parts[1];
                    Bitmap barcodeBitmap = generateBarcodeBitmap(id, 600, 300);

                    if (barcodeBitmap != null) {
                        Image image = Image.getInstance(bitmapToByteArray(barcodeBitmap));
                        image.scaleToFit(600, 300); // Ensure the image fits in 600x300 dimensions
                        document.add(new Paragraph(name + " - " + id));
                        document.add(image);
                        count++;  // Increment the counter after adding a barcode
                    }
                }
            }

            document.close();
            savePdfToFile(byteArrayOutputStream.toByteArray(), "Student_Barcodes.pdf");
        } catch (DocumentException e) {
            Log.e("PDFCreation", "DocumentException", e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void savePdfToFile(byte[] pdfBytes, String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        try {
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                outputStream.write(pdfBytes);
                outputStream.close();
                Toast.makeText(this, "PDF saved to Downloads folder", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("SavePDF", "Error saving PDF", e);
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
