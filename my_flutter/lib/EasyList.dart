import 'dart:ui';

import 'package:flutter/material.dart';

typedef OnClick = Function();

class ListItemData{
  String mTitle="";
  Color mColor=Colors.white;
  OnClick mClickCallback;
  ListItemData(this.mTitle, this.mClickCallback, this.mColor);
}

class ListItem extends StatefulWidget{
  ListItemData mData;
  ListItem(this.mData);

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