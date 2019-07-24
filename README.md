# CommonWebview
jsbridge源码 https://github.com/lzyzsd/JsBridge </br>
<img src="https://github.com/liulingfeng/CommonWebview/blob/master/screenshot/JsBridge.png" width="300" height="530">

1.基于jsbridge，处理外部设置WebViewClient导致jsbridge失效的问题</br>
2.隔离jsbridge的实现
3.使用说明
<pre>
interface WebApi {
    @H5ToNative(methodName = "submitFromWeb")
    fun submitFromWeb(@Field returnData: String, @CallBack h5CallBack: H5CallBack)

    @NativeToH5(methodName = "functionInJs")
    fun functionInJs(@Field responseData: String, @CallBack h5CallBack: H5CallBack)
}
</pre>


