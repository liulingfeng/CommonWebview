# CommonWebview
jsbridge源码 https://github.com/lzyzsd/JsBridge </br>
<img src="https://github.com/liulingfeng/CommonWebview/blob/master/screenshot/JsBridge.png" width="300" height="530">

1.基于jsbridge，处理外部设置WebViewClient导致jsbridge失效的问题</br>
2.隔离jsbridge的实现
<h3>使用说明</h3>
<pre>
interface WebApi {
    @H5ToNative(methodName = "submitFromWeb")
    fun submitFromWeb(@Field returnData: String, @CallBack h5CallBack: H5CallBack)

    @NativeToH5(methodName = "functionInJs")
    fun functionInJs(@Field responseData: String, @CallBack h5CallBack: H5CallBack)
}
</pre>

<h3>业务方调用</h3>
<pre>
 val webApi = H5Retrofit.getInstance(commonFragment?.getWebView()).create(WebApi::class.java)
 webApi?.functionInJs(Gson().toJson(user),object :H5CallBack{
       override fun callBack(data: String) {
          Log.e("德玛","h5返回数据$data")
         }
       })
 webApi?.submitFromWeb("submitFromWeb exe, response data 中文 from Java",object :H5CallBack{
       override fun callBack(data: String) {
           Log.e("德玛","h5返回参数$data")
          }
        })
</pre>

