import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class User {
  late String username;
  late String password;

  User({required this.username, required this.password});

  Map<String, dynamic> toMap() {
    return {'username': username, 'password': password};
  }
}


class DatabaseHelper {
  late Database _database;

  Future open() async {
    _database = await openDatabase(
      join(await getDatabasesPath(), 'user_database.db'),
      version: 1,
      onCreate: (Database db, int version) async {
        await db.execute(
          'CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, password TEXT)',
        );
      },
    );
  }

  Future<int> insertUser(User user) async {
    // Omit the 'id' field from the insert statement to allow SQLite to auto-generate it
    return await _database.insert('users', user.toMap());
  }


  Future<User?> getUser(String username, String password) async {
    List<Map<String, dynamic>> maps = await _database.query('users',
        where: 'username = ? AND password = ?', whereArgs: [username, password]);

    if (maps.isNotEmpty) {
      return User(
        username: maps[0]['username'],
        password: maps[0]['password'],
      );
    } else {
      return null;
    }
  }
}
