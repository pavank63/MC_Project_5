import 'package:amplify_flutter/amplify.dart';
import 'package:finalproject/models/Post.dart'; // Import the correct model class
import 'package:finalproject/models/PostStatus.dart'; // Import the correct status class
import 'dart:developer' as developer;

class AddData {
  void addPost() {
    final post = Post(
      title: "Create a Flutter DataStore app",
      status: PostStatus.ACTIVE,
    );

    Amplify.DataStore.save(
      post,
      onSuccess: (savedItem, {mutatedItems}) =>
          developer.log('Created a new post successfully', name: 'MyAmplifyApp'),
      onError: (error) =>
          developer.log('Error creating post: $error', name: 'MyAmplifyApp'),
    );
  }
}
