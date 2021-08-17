import 'dart:developer';
import 'dart:ffi';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_flutter/Exception/SyncException.dart';
import 'package:my_flutter/Navigator/ImageDemo.dart';
import 'package:my_flutter/Navigator/SimpleNavigatorDemo.dart';
import 'package:my_flutter/Navigator/TipRoute.dart';
import 'package:my_flutter/Widget/ButtonDemo.dart';
import 'package:my_flutter/Widget/ContextRouteDemo.dart';
import 'package:my_flutter/Widget/WidgetDemos.dart';

import 'EasyList.dart';
import 'Navigator/RouteTableDemo.dart';
import 'Navigator/RouteTableWithArgs.dart';
import 'Widget/ImageDemo.dart';
import 'Widget/TextDemo.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      routes: {
        "new_page":(context) => RouteTableDemo(),
        "route_with_args": (context) => RouteWithArgs(),
        "image_demo": (context) => ImageDemo(),
        "sync_exception":(context) => SyncException(),
        "context_route_demo":(context) => ContextRouteDemo(),
        "widget_demos":(context) => WidgetDemos(),

        "text_demo":(context) => TextDemo(),
        "button_demo":(context) => ButtonDemo(),
        "image_demos":(context) => ImageDemos()
      },
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  get child => null;

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
              //简单路由demo
              ListItem(ListItemData("简单路由Demo",(){
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return SimpleNavigatorDemo();
                },
                    maintainState: true,
                    fullscreenDialog: true
                ));
                }, Colors.blueAccent)),

              //路由传参
              ListItem(ListItemData("路由传参Demo",() async {
                var result = Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return TipRoute("我是从HomePage传递过来的参数888  ");
                },
                    maintainState: true,
                    fullscreenDialog: true
                ));
                print(result);
                }, Colors.blueAccent)),

              ListItem(ListItemData("路由表Demo",() {
                 Navigator.pushNamed(context, "new_page");
              }, Colors.blueAccent)),

              ListItem(ListItemData("路由表传参Demo",() {
                Navigator.of(context).pushNamed("route_with_args",arguments: "从HomePage传递过来的参数");
              }, Colors.blueAccent)),

              ListItem(ListItemData("图片测试Demo",() {
                Navigator.of(context).pushNamed("image_demo");
              }, Colors.blueAccent)),

              ListItem(ListItemData("同步异常捕获Demo",() {
                Navigator.of(context).pushNamed("sync_exception");
              }, Colors.blueAccent)),

              ListItem(ListItemData("Context的路由测试Demo",() {
                Navigator.of(context).pushNamed("context_route_demo");
              }, Colors.blueAccent)),

              ListItem(ListItemData("Widget测试Demos",() {
                Navigator.of(context).pushNamed("widget_demos");
              }, Colors.blueAccent)),
            ])));
  }
}


