package com.morrah77.randompicture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

public class RandomPictureService extends Service {
    private static RandomPictureService instance;

    public RandomPictureService() {
        Log.d(this.getClass().getSimpleName(), "RandomPictureService created");
    }

    public static RandomPictureService getInstance() {
        Log.d(RandomPictureService.class.getSimpleName(), "getInstance");
        if (instance == null) {
            instance = new RandomPictureService();
        }
        return instance;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void callBackend(Consumer<Bitmap> bitmapConsumer) {
        callBackend(null, bitmapConsumer);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void callBackend(DisplayMetrics displayMetrics, Consumer<Bitmap> bitmapConsumer) {
        Thread thread = new Thread(
                () -> {
                    try {
                        String backendUrl = getBackendUrlForDimensions(displayMetrics);
                        Log.d(this.getClass().getSimpleName(), backendUrl);
                        URL url = new URL(backendUrl);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        Bitmap bitmap;
                        try {
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            bitmap = readStream(in);
                            Log.d(this.getClass().getSimpleName(), "got a bitmap");
                        } finally {
                            Log.d(this.getClass().getSimpleName(), "closing a connection");
                            urlConnection.disconnect();
                        }
                        Log.d(this.getClass().getSimpleName(), "accepting a bitmap");
                        bitmapConsumer.accept(bitmap);
                    } catch (Throwable e) {
                        Log.e(this.getClass().getSimpleName(), "callBackend: ", e);
                    }
                }
        );
        thread.start();
    }

    private String getBackendUrlForDimensions(DisplayMetrics displayMetrics) {
        String backendUrl = ConfigService.getInstance().getBackendUrl();
        if (Objects.nonNull(displayMetrics)) {
            backendUrl = String.format(backendUrl + "?w=%d&h=%d", displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        return backendUrl;
    }

    private Bitmap readStream(InputStream in) {
        return BitmapFactory.decodeStream(in);
    }
}
