enum HttpMethod{
  GET,
  POST,
  DELETE
}
abstract class BaseRequest{
  var pathParams;
  var useHttps=true;

  String authority(){
    return "api.devio.org";
  }

  HttpMethod httpMethod();

  String path();

  String url(){
    Uri uri;
    var pathStr=path();
    //contact path params
    if(pathParams!=null) {
      if (path().endsWith("/")) {
        pathStr = "${path()}$pathParams";
      } else {
        pathStr = "${path()}/$pathParams";
      }
    }
    //using http or https
    if(useHttps){
      uri=Uri.https(authority(), pathStr,params);
    }else{
      uri=Uri.http(authority(), pathStr,params);
    }
    print("uri=${uri.toString()}");
    return uri.toString();
  }

  bool needLogin();

  Map<String,String> params=Map();

  ///add params
  BaseRequest add(String k,Object v){
    params[k]=v.toString();
    return this;
  }

  Map<String,dynamic> header=Map();

  ///add header
  BaseRequest addHeader(String k, Object v){
    params[k]=v.toString();
    return this;
  }

}