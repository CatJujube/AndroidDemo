import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class RouteWithArgs extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    var args = ModalRoute.of(context)?.settings.arguments;
    return Scaffold(
      appBar: AppBar(
          title: Text("路由表传参测试页面")
      ),
      body:       Container(
          child: Text(args as String)
      ),
    ) ;
  }
}