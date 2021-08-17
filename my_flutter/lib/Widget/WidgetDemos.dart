import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../EasyList.dart';

class WidgetDemos extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => _WidgetDemos();
}

class _WidgetDemos extends State<WidgetDemos>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Widget的测试用例"),
      ),
      body: Container(
        child: ListView(
            padding: EdgeInsets.all(20),
            children: <Widget>[
            ListItem(ListItemData("Text Demo",(){
              Navigator.of(context).pushNamed("text_demo");
            },Colors.blueAccent)),

            ListItem(ListItemData("Button Demo",(){
              Navigator.of(context).pushNamed("button_demo");
            },Colors.blueAccent)),

            ListItem(ListItemData("Image Demo",(){
              Navigator.of(context).pushNamed("image_demos");
            },Colors.blueAccent)),
        ])
      ),
    );
  }
}