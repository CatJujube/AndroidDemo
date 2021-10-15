import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../../EasyList.dart';
import 'RequestTest.dart';



class BilibiliTestMain extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return _BilibiliTestMain_State();
  }
}

class _BilibiliTestMain_State extends State<BilibiliTestMain>{
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
                  ListItem(ListItemData("request test",() {
                    requestTest();
                  }, Colors.blueAccent)),

                ])));
  }

}