package com.example.finalproject;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class MainActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Add this line to initialize Amplify
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Amplify initialized successfully");
        } catch (AmplifyException exception) {
            Log.e("Amplify", "Could not initialize Amplify", exception);
            // Handle the exception appropriately, e.g., show an error message
        }
    }
}
