import 'package:my_flutter/Projects/BiliBili/http/core/hi_adpter.dart';
import 'package:my_flutter/Projects/BiliBili/http/request/base_request.dart';

class MockAdpter extends HiNetAdapter{
  @override
  Future<HiNetResponse<T>> send<T>(BaseRequest request) {
    // TODO: implement send
    throw UnimplementedError();
  }
}