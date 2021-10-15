import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../EasyList.dart';

class ProjectsMain extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return _ProjectsMain_State();
  }
}

class _ProjectsMain_State extends State<ProjectsMain>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
        title: Text("Flutter Navigation Page"),
    ),
    body:
    Container(
        child: ListView(
            padding: EdgeInsets.all(20),
            children: <Widget>[
              ListItem(ListItemData("Bilibili Demo",() {
                Navigator.pushNamed(context, "bilibili_main");
              }, Colors.blueAccent)),

            ])));
  }

}