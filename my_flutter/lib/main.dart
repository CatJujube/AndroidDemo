import 'dart:developer';
import 'dart:ffi';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_flutter/Navigator/SimpleNavigatorDemo.dart';
import 'package:my_flutter/Navigator/TipRoute.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
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
            ])));
  }
}

typedef OnClick = Function();

class ListItemData{
  String mTitle="";
  Color mColor=Colors.white;
  OnClick mClickCallback;
  ListItemData(this.mTitle, this.mClickCallback, this.mColor);
}

class ListItem extends StatefulWidget{
  ListItemData mData;
  ListItem(ListItemData this.mData);

  @override
  State<StatefulWidget> createState() => _ListItem(mData);

}


class _ListItem extends State<ListItem>{
  static String TAG = "ListItem_Msg";
  ListItemData mData;

  _ListItem( this.mData);

  void setTitle(String title){
    setState(() {
      mData.mTitle = title;
    });
  }

  void setColor(Color color){
    setState(() {
      mData.mColor = color;
    });
  }

  @override
  Widget build(BuildContext context) =>
          TextButton(style: ButtonStyle(backgroundColor: MaterialStateProperty.all<Color>(mData.mColor)),
          onPressed: mData.mClickCallback,
          child: Text(
            mData.mTitle,
            style: TextStyle(color: Colors.white))
      );
}
