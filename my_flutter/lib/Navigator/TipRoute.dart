import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class TipRoute extends StatelessWidget{
  String mText = "";

  TipRoute(this.mText);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("路由传参测试"),
      ),
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Center(
          child: Column(
            children: <Widget>[
              Text(mText),
              RaisedButton(onPressed: () => Navigator.pop(context,"从TipRoute返回的值110"),
              child: Text("返回"),
              )
            ],
          ),
        ),
      ),
    );
  }
}