import 'package:my_flutter/Projects/BiliBili/http/core/hi_adpter.dart';
import 'package:my_flutter/Projects/BiliBili/http/request/base_request.dart';
///测试适配器，mock数据
class MockAdpter extends HiNetAdapter{
  @override
  Future<HiNetResponse<T>> send<T>(BaseRequest request) {
    return Future<HiNetResponse<T>>.delayed(Duration(microseconds: 1000),(){
      return HiNetResponse<T>(data: null,statusCode: 403);
    });
  }
}