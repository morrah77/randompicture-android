package com.morrah77.randompicture;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ConfigService extends Service {
    private static ConfigService instance;
    Bundle metaDataBundle;
    String backendUrl;

    public ConfigService() {
        Log.d(this.getClass().getSimpleName(), "ConfigService created without args");
    }

    private ConfigService(Bundle metaDataBundle) {
        Log.d(this.getClass().getSimpleName(), "ConfigService created with metaDataBundle");
        this.metaDataBundle = metaDataBundle;
        this.backendUrl = metaDataBundle.getString("backendUrl");
    }

    public static ConfigService getInstance() {
        Log.d(ConfigService.class.getSimpleName(), "getInstance");
        return withMetaDataBundle(null);
    }

    public static ConfigService withMetaDataBundle(Bundle metaDataBundle) {
        Log.d(ConfigService.class.getSimpleName(), "withMetaDataBundle");
//        checkIfInitializedAndThrow(metaDataBundle);
        if (instance == null) {
            instance = new ConfigService(metaDataBundle);
        }
        return instance;
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    private static void checkIfInitializedAndThrow(Bundle metaDataBundle) {
        if ((metaDataBundle != null) && (instance != null)) {
            throw new RuntimeException(String.format("Service already initialized: %s", ConfigService.class.getSimpleName()));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
