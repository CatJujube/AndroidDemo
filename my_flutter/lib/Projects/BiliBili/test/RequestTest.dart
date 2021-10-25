import 'package:flutter/material.dart';
import 'package:my_flutter/Projects/BiliBili/http/core/hi_error.dart';
import 'package:my_flutter/Projects/BiliBili/http/core/hi_net.dart';
import 'package:my_flutter/Projects/BiliBili/http/request/test_request.dart';

Future<void> requestTest() async {
  TestRequest request = TestRequest();
  request.add("aa","ddd").add("bb", "333");
  try{
    var result = await HiNet.getInstance().fire(request);
    print(result);
  }on NeedAuth catch(e){
    print(e);
  }on NeedLogin catch(e){
    print(e);
  }on HiNetError catch(e){
    print(e);
  }
}