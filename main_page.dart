import 'package:flutter/material.dart';
import 'registration_page.dart';
import 'login_page.dart';
import 'database_helper.dart';

class MainPage extends StatelessWidget {
  final DatabaseHelper dbHelper;

  MainPage({required this.dbHelper});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Main Page'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) => RegistrationPage(dbHelper: dbHelper)));
              },
              child: Text('Register'),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) => LoginPage(dbHelper: dbHelper)));
              },
              child: Text('Login'),
            ),
          ],
        ),
      ),
    );
  }
}
