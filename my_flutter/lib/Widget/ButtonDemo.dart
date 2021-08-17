import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ButtonDemo extends StatelessWidget{
  final ButtonStyle style =
  ElevatedButton.styleFrom(textStyle: const TextStyle(fontSize: 20));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("ButtonDemo"),
      ),
      body: Container(
        child: ListView(
          padding: EdgeInsets.all(18),
          children: <Widget>[
            /**
             * IconButton
             */
            IconButton(onPressed: (){}, icon: Icon(Icons.thumb_up)),

            /**
             * ElevatedButton
             */
            ElevatedButton(onPressed: (){}, child: Text("ElevatedButton"),style: style,),

            /**
             * TextButton
             */
            TextButton(onPressed: (){}, child: Text("Text Button",
              style: TextStyle(
                color: Colors.white
              ),
            ),
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all<Color>(Colors.blueAccent),
              shape: MaterialStateProperty.all<OutlinedBorder>(RoundedRectangleBorder(borderRadius: BorderRadius.circular(20))),
            ),
            ),
          ],
        ),
      ),
    );
  }
}