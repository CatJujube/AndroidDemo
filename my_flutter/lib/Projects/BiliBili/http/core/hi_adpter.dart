import 'package:my_flutter/Projects/BiliBili/http/request/base_request.dart';
import 'dart:convert';

abstract class HiNetAdapter{
  Future<HiNetResponse<T>> send<T>(BaseRequest request);
}

class HiNetResponse<T> {
  HiNetResponse(
       {this.data,
        this.request,
        this.statusCode:0,
        this.message:"",
        this.extra});

  T? data;
  BaseRequest? request;
  int statusCode;
  String message;
  dynamic extra;

  @override
  String toString() {
    if(data is Map){
      return json.encode(data);
    }
    return data.toString();
  }
}