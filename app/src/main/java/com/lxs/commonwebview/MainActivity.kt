package com.lxs.commonwebview

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.lxs.jsbridge.CallBackFunction
import com.lxs.jsbridge.DefaultHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bridgeWebView.setDefaultHandler(DefaultHandler())
        bridgeWebView.loadUrl("file:///android_asset/demo.html")

        bridgeWebView.registerHandler("submitFromWeb") { _, function ->
            function.onCallBack("submitFromWeb exe, response data 中文 from Java")
        }

        bridgeWebView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return shouldOverrideUrlLoading(view, request?.url?.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.e("外部webViewClient", "你好啊")
                return false
            }
        }

        val user = User("大头鬼", 23)
        bridgeWebView.callHandler("functionInJs", Gson().toJson(user)) {
            Log.e("h5调用返回", it)
        }
        bridgeWebView.send("hello")
    }

    override fun onBackPressed() {
        if (bridgeWebView.canGoBack()) {
            bridgeWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        bridgeWebView?.loadUrl("about:blank")
        bridgeWebView?.stopLoading()
        bridgeWebView?.handler?.removeCallbacksAndMessages(null)
        bridgeWebView?.removeAllViews()
        (bridgeWebView?.parent as? ViewGroup)?.removeView(bridgeWebView)
        bridgeWebView?.webViewClient = null
        bridgeWebView?.webChromeClient = null
        bridgeWebView?.clearHistory()
        bridgeWebView?.destroy()
        super.onDestroy()
    }
}
