import 'package:flutter/material.dart';
import 'database_helper.dart';

class ProfilePage extends StatelessWidget {
  final User user;

  ProfilePage({required this.user});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Profile'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Username: ${user.username}', style: TextStyle(fontSize: 18.0)),
            SizedBox(height: 8.0),
            Text('Password: ${user.password}', style: TextStyle(fontSize: 18.0)),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () {
                // Add functionality to update profile
              },
              child: Text('Update Profile'),
            ),
          ],
        ),
      ),
    );
  }
}
