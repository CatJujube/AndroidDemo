import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class TextDemo extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Text Demo"),
      ),
      body: Container(
        child: ListView(
          padding: EdgeInsets.all(18),
          children: <Widget>[
            Text("hello world",
            textAlign: TextAlign.left
            ),

            Text(
              "hello world"*10,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),

            Text(
              "ellipsis",
              textScaleFactor: 2.0
            ),

            Text("ellipsis",
            textAlign: TextAlign.center,
            ),

            Text(
              "ellipsis",
              style: TextStyle(
                color: Colors.blueAccent,
                fontSize: 18,
                height: 2,
                fontFamily: "Courier",
                background: new Paint()..color=Colors.red,
                decoration: TextDecoration.underline,
                decorationStyle: TextDecorationStyle.dashed
              ),
            ),

            /**
             * 连续使用TextSpan来构建文本
             *
             */
            Text.rich(TextSpan(
              children: [
                TextSpan(
                  text: "Home: "
                ),
                TextSpan(
                  text: "www.baidu.com",
                  style: TextStyle(
                    color: Colors.blueAccent
                  ),
                  // recognizer: _tapRecognizer
                )
              ]
            )),

            /**
             * 可被继承的文本样式控件，DefaultTextStyle
             */
            DefaultTextStyle(style: TextStyle(
              color: Colors.red,
              fontSize: 20.0
            ),
                textAlign: TextAlign.start,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text("hello world"),
                    Text("I am Jack"),
                    Text("I am Jack",
                    style: TextStyle(
                      inherit: false,
                      color: Colors.greenAccent
                    ),
                    )
                  ],
            )),
          ],
        ),
      ),
    );
  }
}