import 'package:amplify_flutter/amplify.dart';
import 'package:finalproject/models/Post.dart'; // Import the correct model class
import 'dart:developer' as developer;

class ViewData {
  void queryPosts() {
    Amplify.DataStore.query(
      Post.classType,
      onData: (List<Post> queryMatches, {bool hasNextToken}) {
        if (queryMatches.isNotEmpty) {
          developer.log('Successful query, found posts.', name: 'MyAmplifyApp');
        } else {
          developer.log('Successful query, but no posts.', name: 'MyAmplifyApp');
        }
      },
      onError: (error) =>
          developer.log('Error retrieving posts: $error', name: 'MyAmplifyApp'),
    );
  }
}
