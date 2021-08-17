import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

class SyncException extends StatelessWidget{

  var logger = Logger();
  String TAG = "SyncException_LogMsg";

  @override
  Widget build(BuildContext context) {
    print(TAG+" build");
    try{
      var i = 1/0;
    }catch(e,stack){
      print(TAG+" division zero error!");
    }

    return Scaffold(
      appBar: AppBar(
        title: Text("同步异常捕获Demo"),
      ),
      body: Container(
        child: Text(""),
      ),
    );
  }
}