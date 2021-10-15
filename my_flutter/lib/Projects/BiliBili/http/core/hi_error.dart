///login error
class NeedLogin extends HiNetError{
  NeedLogin({int code:401,String message:"please login"}):super(code,message);

}

///authority error
class NeedAuth extends HiNetError{
  NeedAuth(String message,{int code:403,dynamic data}):super(code,message,data: data);

}

///network error uniform format class
class HiNetError implements Exception{
  final int code;
  final String message;
  final dynamic data;

  HiNetError(this.code,this.message,{this.data});
}