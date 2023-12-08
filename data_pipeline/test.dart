import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart'; // Import your app's main package here

void main() {
  testWidgets('useAppContext test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(MyApp()); // Replace MyApp with the main widget of your Flutter app

    // Verify that the app context has the correct package name.
    expect(find.text('com.example.main'), findsOneWidget);
  });
}
