package com.google.ar.sceneform.samples.helloar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Target;
import java.net.URI;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;

public class take_photo extends AppCompatActivity {

    public Double max;
    public int index;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final TextView red = findViewById(R.id.red);
        final TextView green = findViewById(R.id.green);
        final TextView blue = findViewById(R.id.blue);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photoFromCam = (Bitmap) extras.get("data");
            final ImageView roomPicture = findViewById(R.id.room_pic);
            roomPicture.setImageBitmap(photoFromCam);

            Mat rgba = new Mat();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(photoFromCam, 500, 500, false);
            Utils.bitmapToMat(resizedBitmap, rgba);

            // Get the bitmap size.
            Size rgbaSize = rgba.size();

            Log.v("Array Height", String.valueOf(rgbaSize.height));
            Log.v("Array Width", String.valueOf(rgbaSize.width));
            Log.v("Array Area", String.valueOf(rgbaSize.area()));
            Log.v("Array secondPix", String.valueOf(rgba.get(0,0)[1]));

            ArrayList<Double> reds = new ArrayList<Double>();
            ArrayList<Double> blues = new ArrayList<Double>();
            ArrayList<Double> greens = new ArrayList<Double>();


            for (int i = 0; i < rgbaSize.height; i++) {
                //Log.v("height count", String.valueOf(i) + " BGR values: " + String.valueOf(rgba.get(i, 0)[0]));
                for (int j = 0; j < rgbaSize.width; j++) {
                    reds.add(rgba.get(i, j)[0]);
                    greens.add(rgba.get(i, j)[1]);
                    blues.add(rgba.get(i, j)[2]);
                }
            }

            double sumReds = 0;
            for(Double r : reds)
                sumReds += r/(255*rgbaSize.area());

            double sumGreens = 0;
            for(Double g : greens)
                sumGreens += g/(255*rgbaSize.area());

            double sumBlues = 0;
            for(Double b : blues)
                sumBlues += b/(255*rgbaSize.area());

            ArrayList<Double> finalResult = new ArrayList<>();
            finalResult.add(sumReds);
            finalResult.add(sumGreens);
            finalResult.add(sumBlues);

            max = Collections.max(finalResult);
            index = finalResult.indexOf(max);

            Log.v("Colors!", String.valueOf(sumReds) +" ,"+ String.valueOf(sumGreens) + " ," +String.valueOf(sumBlues));
            red.setText("Red: " + String.valueOf(Math.round(sumReds*100)));
            green.setText("Green: " + String.valueOf(Math.round(sumGreens*100)));
            blue.setText("Blue: " + String.valueOf(Math.round(sumBlues*100)));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        OpenCVLoader.initDebug();
        final Button getPhotoButton = findViewById(R.id.get_photo);
        final ImageView furniturePicture = findViewById(R.id.furniture_pic);
        final Button nextButton = findViewById(R.id.next);

        nextButton.setVisibility(View.GONE);

        getPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                Toast.makeText(getApplicationContext(), "HIIII", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.VISIBLE);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelloSceneformActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });

    }
}
