import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../EasyList.dart';


class BilibiliMain extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return _BilibiliMain_State();
  }
}

class _BilibiliMain_State extends State<BilibiliMain>{

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
                  ListItem(ListItemData("Test Demo",() {
                    Navigator.pushNamed(context, "bilibili_test_main");
                  }, Colors.blueAccent)),

                ])));
  }

}