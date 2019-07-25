package com.lxs.webviewcontainer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.fragment_common_webview.*

/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
class CommonWebViewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCustomSetting()
        webView.setTitleView(toolbar_title)
    }

    private fun setCustomSetting() {
        /**
         * 对加载图片优化、延后加载
         */
        webView.getWebSetting()?.loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19
        webView.getWebSetting()?.cacheMode = WebSettings.LOAD_DEFAULT //根据cache-control决定是否从网络上取数据/默认行为
        webView.getWebSetting()?.setAppCacheEnabled(false) //构建离线应用、需要指定缓存地址。官方不推荐使用
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        webView.getWebLifeCycle()?.onResume()
        webView.getWebSetting()?.javaScriptEnabled = true
        super.onResume()
    }

    override fun onPause() {
        webView.getWebLifeCycle()?.onPause()
        super.onPause()
    }

    override fun onStop() {
        webView.getWebSetting()?.javaScriptEnabled = false //防止在后台疯狂执行js
        super.onStop()
    }

    override fun onDestroy() {
        webView.getWebLifeCycle()?.onDestroy()
        super.onDestroy()
    }

    fun getWebView(): CommonWebView {
        return webView
    }
}