package com.example.emotiondetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotiondetector.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.Category;

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

        Button button = (Button) findViewById(R.id.analyzeButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView tv = MainActivity.this.findViewById(R.id.textView4);
                tv.setVisibility(View.VISIBLE);
            }
        });

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
            float maxprobability= probability.get(0).getScore();
            int maxIndex= 0;
            for (int i = 0; i < probability.size(); i++) {
                if(probability.get(i).getScore()>maxprobability){
                    maxprobability= probability.get(i).getScore();
                    maxIndex= i;
                }
                Log.e("category", String.valueOf(probability.get(i)));
            }
            TextView textView= findViewById(R.id.textView4);
            textView.setText(probability.get(maxIndex).getLabel());

        } catch(IOException e) {
            Log.e("MainActivity", "IOException");
        }
    }
}