package com.example.finalproject;

import android.util.Log;

import com.amplifyframework.core.Amplify;

import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;


public class ViewData {
    Amplify.DataStore.query(Post.class,
    queryMatches -> {
        if (queryMatches.hasNext()) {
            Log.i("MyAmplifyApp", "Successful query, found posts.");
        } else {
            Log.i("MyAmplifyApp", "Successful query, but no posts.");
        }
    },
    error -> Log.e("MyAmplifyApp",  "Error retrieving posts", error)
            );
}
