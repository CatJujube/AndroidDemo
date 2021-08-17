import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class RouteTableDemo extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>  Scaffold(
    appBar: AppBar(
      title: Text("路由表测试页面"),
    ),
    body: Center(
      child: Text("This is a ne route page"),
    ),
  );
}