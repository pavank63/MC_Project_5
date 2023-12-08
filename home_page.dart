import 'package:flutter/material.dart';
import 'main_page.dart';
import 'database_helper.dart';
import 'profile_page.dart';

class HomePage extends StatefulWidget {
  final User user;

  HomePage({required this.user});

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final TextEditingController messageController = TextEditingController();
  List<ChatMessage> chatMessages = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('MindfulChat'),
        actions: [
          IconButton(
            onPressed: () {
              // Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => MainPage()));
            },
            icon: Icon(Icons.exit_to_app),
          ),
          IconButton(
            onPressed: () {
              // Navigate to the ProfilePage
              Navigator.push(context, MaterialPageRoute(builder: (context) => ProfilePage(user: widget.user)));
            },
            icon: Icon(Icons.person),
          ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: chatMessages.length,
              itemBuilder: (context, index) {
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
                  child: ChatBubble(message: chatMessages[index]),
                );
              },
            ),
          ),
          ChatInputBar(
            messageController: messageController,
            onSendMessage: () {
              if (messageController.text.isNotEmpty) {
                setState(() {
                  chatMessages.add(ChatMessage(
                    text: messageController.text,
                    isUser: true,
                    sender: widget.user.username,
                  ));
                  messageController.clear();
                });
              }
            },
          ),
        ],
      ),
    );
  }
}

class ChatBubble extends StatelessWidget {
  final ChatMessage message;

  ChatBubble({required this.message});

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: message.isUser ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        padding: EdgeInsets.all(12.0),
        margin: EdgeInsets.symmetric(vertical: 4.0),
        decoration: BoxDecoration(
          color: message.isUser ? Colors.blue : Colors.grey,
          borderRadius: BorderRadius.circular(12.0),
        ),
        child: Text(
          message.text,
          style: TextStyle(color: Colors.white),
        ),
      ),
    );
  }
}

class ChatInputBar extends StatelessWidget {
  final TextEditingController messageController;
  final VoidCallback onSendMessage;

  ChatInputBar({required this.messageController, required this.onSendMessage});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Row(
        children: [
          Expanded(
            child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(24.0),
                color: Colors.grey[200],
              ),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: TextField(
                  controller: messageController,
                  decoration: InputDecoration(
                    hintText: 'Type your message...',
                    border: InputBorder.none,
                  ),
                ),
              ),
            ),
          ),
          SizedBox(width: 8.0),
          Material(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(24.0),
            child: IconButton(
              icon: Icon(Icons.send, color: Colors.white),
              onPressed: onSendMessage,
            ),
          ),
        ],
      ),
    );
  }
}

class ChatMessage {
  final String text;
  final bool isUser;
  final String sender;

  ChatMessage({required this.text, required this.isUser, required this.sender});
}
