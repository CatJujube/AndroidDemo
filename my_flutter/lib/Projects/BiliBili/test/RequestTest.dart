import 'package:flutter/material.dart';
import 'package:my_flutter/Projects/BiliBili/http/core/hi_net.dart';
import 'package:my_flutter/Projects/BiliBili/http/request/test_request.dart';

Future<void> requestTest() async {
  TestRequest request = TestRequest();
  request.add("aa","ddd").add("bb", "333");
  var result = await HiNet.getInstance().fire(request);
  print(result);
}