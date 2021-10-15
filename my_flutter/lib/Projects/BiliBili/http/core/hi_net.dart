import 'package:flutter/material.dart';
import 'package:my_flutter/Projects/BiliBili/http/core/hi_adpter.dart';
import 'package:my_flutter/Projects/BiliBili/http/core/hi_error.dart';
import 'package:my_flutter/Projects/BiliBili/http/request/base_request.dart';

class HiNet{
  HiNet._();
  static HiNet _instance =HiNet._();
  ///lazy mode
  static HiNet getInstance(){
    // if(_instance == null){
    //   _instance=HiNet._();
    // }
    return _instance;
  }

  Future fire(BaseRequest request) async{
    HiNetResponse? response;
    var error;
    try{
      response = await send(request);
    } on HiNetError catch(e){
      error=e;
      response=e.data;
      printlog(e.message);
    }catch(e){
      ///other error
      error=e;
      printlog(e);
    }
    if(response==null){
      printlog(error);
    }
    var result = response!.data;
    var status=response.statusCode;
    switch(status){
      case 200:       ///ok
        return result;
      case 401:       ///
        NeedLogin();
        break;
      case 403:
        NeedAuth(result.toString(),data: result);
        break;
      default:
        throw HiNetError(status, result.toString(),data: result);
    }
    printlog(result);
    return result;
  }

  Future<dynamic> send<T>(BaseRequest request) async{
    printlog('url:${request.url()}');
    printlog('method:${request.httpMethod()}');
    request.addHeader("token", "123");
    printlog('header:${request.header}');
    return Future.value({'statusCode':200,'data':{"code":0,"message":"success"}});
  }

  void printlog(log){
    print('hi_net:${log.toString()}');
  }
}