
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

//简单路由跳转Demo
class SimpleNavigatorDemo extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>  Scaffold(
        appBar: AppBar(
          title: Text("New Route"),
        ),
        body: Center(
          child: Text("This is a ne route page"),
      ),
      );
}