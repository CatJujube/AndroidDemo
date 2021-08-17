import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ContextRouteDemo extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Context路由测试"),
      ),
      body: Container(
        child: Builder(builder: (context){
          Scaffold? scaffold = context.findAncestorWidgetOfExactType<Scaffold>();
          return (scaffold?.appBar as AppBar).title!;
        }),
      ),
    );
  }
}