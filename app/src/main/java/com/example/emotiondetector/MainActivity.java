package com.example.emotiondetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get file path from Intent and use it to retrieve Bitmap and set it as the Image View
        Bundle extras = getIntent().getExtras();
        String filePath = extras.getString("path");
        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        ImageView image = (ImageView) findViewById(R.id.imageDisplay);
        image.setImageBitmap(bmp);

    }

    public void returnLaunch(View view) {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
    public void analyzeImage(View view) {
        try {
            String modelPath = "assets/model.tflite";
            TensorImage image = TensorImage.fromBitmap(bmp);
            ImageClassifier.ImageClassifierOptions options = ImageClassifier.ImageClassifierOptions.builder().setMaxResults(1).build();
            ImageClassifier imageClassifier = ImageClassifier.createFromFileAndOptions(this, modelPath, options);

            // Run inference
            List<Classifications> results = imageClassifier.classify(image);
            TextView textview = findViewById(R.id.textView4);
            textview.setText((CharSequence) results.get(0));
        } catch(IOException e) {
            Log.e("MainActivity", "IOException");
        }

    }
}