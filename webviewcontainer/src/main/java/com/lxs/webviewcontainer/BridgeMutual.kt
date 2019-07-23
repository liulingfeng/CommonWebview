package com.lxs.webviewcontainer

import com.lxs.jsbridge.BridgeWebView
import java.lang.ref.WeakReference

/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
class BridgeMutual constructor(bridgeWebView: BridgeWebView) : IMutual {
    private var bridgeWebView: WeakReference<BridgeWebView> = WeakReference(bridgeWebView)

    override fun registerNativeMethod(methodName: String, returnData: String, h5CallBack: H5CallBack) {
        bridgeWebView.get()?.registerHandler(methodName) { data, function ->
            h5CallBack.callBack(data)
            function.onCallBack(returnData)
        }
    }

    override fun callH5Method(methodName: String, data: String, h5CallBack: H5CallBack) {
        bridgeWebView.get()?.callHandler(methodName, data) {
            h5CallBack.callBack(it)
        }
    }
}