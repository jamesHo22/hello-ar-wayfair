package com.google.ar.sceneform.samples.helloar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;

public class take_photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        OpenCVLoader.initDebug();
        final Button getPhotoButton = findViewById(R.id.get_photo);
        final ImageView roomPicture = findViewById(R.id.room_pic);
        final ImageView furniturePicture = findViewById(R.id.furniture_pic);
        final Button nextButton = findViewById(R.id.next);
        nextButton.setVisibility(View.GONE);
        final String artistImageUrl = "https://cdn.patchcdn.com/users/22847585/stock/T800x600/2016015694502bc5366.jpg";



        getPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "HIIII", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext())
                        .load(artistImageUrl)
                        .into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                // Create the required arrays and convert the bitmap (our image)
                                // so that it fits the array.
                                Mat rgba = new Mat();
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
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

                                Log.v("Colors!", String.valueOf(sumReds) +" ,"+ String.valueOf(sumGreens) + " ," +String.valueOf(sumBlues));

                                Picasso.with(getApplicationContext())
                                        .load(artistImageUrl)
                                        .into(new com.squareup.picasso.Target() {
                                            @Override
                                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                            /* Save the bitmap or do something with it here */

                                                // Set it in the ImageView
                                                roomPicture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelloSceneformActivity.class);
                startActivity(intent);
            }
        });

    }
}
