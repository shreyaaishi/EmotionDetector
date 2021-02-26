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

import com.example.emotiondetector.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.Category;
//import org.tensorflow.lite.task.vision.classifier.Classifications;
//import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
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
        bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        ImageView image = (ImageView) findViewById(R.id.imageDisplay);
        image.setImageBitmap(bmp);

    }

    public void returnLaunch(View view) {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
    public void analyzeImage(View view) {
        try {
            TensorImage tfImage = new TensorImage(DataType.FLOAT32);
            tfImage.load(bmp);
            ImageProcessor imageProcessor = new ImageProcessor.Builder().
                    add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).
                    build();
            tfImage = imageProcessor.process(tfImage);
            Model model = Model.newInstance(view.getContext());
            Model.Outputs outputs = model.process(tfImage);
            List<Category> probability = outputs.getProbabilityAsCategoryList();
            for (int i = 0; i < probability.size(); i++) {
                Log.e("category", String.valueOf(probability.get(i)));
            }

        } catch(IOException e) {
            Log.e("MainActivity", "IOException");
        }
    }
}