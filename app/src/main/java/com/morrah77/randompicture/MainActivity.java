package com.morrah77.randompicture;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ConfigService configService;
    private RandomPictureService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle metaDataBundle;
        try {
            metaDataBundle = getApplicationContext().getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            Log.d(this.getClass().getSimpleName(), metaDataBundle.getString("backendUrl"));
            configService = ConfigService.withMetaDataBundle(metaDataBundle);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "Could not get metadata", e);
        }
        super.onCreate(savedInstanceState);
        service = RandomPictureService.getInstance();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Log.d(this.getClass().getSimpleName(), "Will set an empty picture");
        setDefaultImage();
//        setPicture(Bitmap.createBitmap(600, 800, Bitmap.Config.RGB_565));

        ImageView view = findViewById(R.id.imageView);
        view.setOnClickListener(v -> refreshImage());

        refreshImage();
    }

    private void setDefaultImage() {
        ImageView view = findViewById(R.id.imageView);
        view.setImageResource(R.drawable.ic_launcher_foreground);
    }

    private void refreshImage() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        service.callBackend(displayMetrics, bitmap1 -> runOnUiThread(() -> {
            Log.d(this.getClass().getSimpleName(), "Will set a picture");
            setPicture(bitmap1);
        }));
    }

    private void setPicture(Bitmap bitmap) {
        Log.d(this.getClass().getSimpleName(), "Setting a picture");
        ImageView view = findViewById(R.id.imageView);
        view.setImageBitmap(bitmap);
        Log.d(this.getClass().getSimpleName(), "Picture set successfully");
    }

}
