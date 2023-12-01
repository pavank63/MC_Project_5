package com.example.finalproject;

import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;

public class AddData {
    Post post = Post.builder()
            .title("Create an Amplify DataStore app")
            .status(PostStatus.ACTIVE)
            .build();

    Amplify.DataStore.save(post,
        result -> Log.i("MyAmplifyApp", "Created a new post successfully"),
        error -> Log.e("MyAmplifyApp",  "Error creating post", error)
                );
