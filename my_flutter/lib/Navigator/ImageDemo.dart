import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ImageDemo extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("图片测试Demo"),
      ),
      body:Image.asset('images/img.png')

      // DecoratedBox(
      //   decoration: BoxDecoration(
      //     image: DecorationImage(
      //       image: AssetImage('images/img.png')
      //     )
      //   ),
      // ),
    );
  }
}