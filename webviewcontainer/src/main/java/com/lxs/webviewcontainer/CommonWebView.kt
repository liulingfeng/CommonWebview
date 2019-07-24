package com.lxs.webviewcontainer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.lxs.jsbridge.BridgeWebView
import com.lxs.webviewcontainer.lifecycle.DefaultWebLifeCycleImpl
import com.lxs.webviewcontainer.lifecycle.WebLifeCycle
import com.lxs.webviewcontainer.widget.WebIndicator

/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
class CommonWebView : FrameLayout, IMutual {
    private var bridgeWebView: BridgeWebView? = null
    private var progressView: WebIndicator? = null
    private var webLifeCycle: WebLifeCycle? = null
    private var titleView: TextView? = null
    private var iMutual: IMutual? = null

    override fun registerNativeMethod(methodName: String, returnData: String?, h5CallBack: H5CallBack) {
        iMutual?.registerNativeMethod(methodName, returnData, h5CallBack)
    }

    override fun callH5Method(methodName: String, data: String, h5CallBack: H5CallBack) {
        iMutual?.callH5Method(methodName, data, h5CallBack)
    }

    public fun setWebViewClient(client: WebViewClient?) {
        bridgeWebView?.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return shouldOverrideUrlLoading(view, request?.url.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return client?.shouldOverrideUrlLoading(view, url) ?: super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    public fun setWebChromeClient(client: WebChromeClient?) {
        bridgeWebView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                if (client == null) {
                    when {
                        newProgress < 0 -> progressView?.reset()
                        newProgress in 1..10 -> progressView?.show()
                        newProgress in 11..94 -> progressView?.setProgress(newProgress)
                        else -> {
                            progressView?.setProgress(newProgress)
                            progressView?.hide()
                        }
                    }
                } else {
                    client.onProgressChanged(view, newProgress)
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                titleView?.text = title
                client?.onReceivedTitle(view, title)
                super.onReceivedTitle(view, title)
            }
        }
    }

    fun loadUrl(url: String) {
        bridgeWebView?.loadUrl(url)
    }

    fun getWebSetting(): WebSettings? {
        return bridgeWebView?.settings
    }

    fun getWebLifeCycle(): WebLifeCycle? {
        return webLifeCycle
    }

    fun setTitleView(titleView: TextView) {
        this.titleView = titleView
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @SuppressLint("InflateParams")
    private fun init(context: Context?) {
        val view: View? = LayoutInflater.from(context).inflate(R.layout.layout_common_webview, null)
        bridgeWebView = view?.findViewById(R.id.bridgeWebView)
        progressView = view?.findViewById(R.id.progress)
        progressView?.visibility = View.GONE
        bridgeWebView?.let {
            iMutual = BridgeMutual(it)
            webLifeCycle = DefaultWebLifeCycleImpl(it)
        }
        setWebChromeClient(null)
        addView(view)
    }
}