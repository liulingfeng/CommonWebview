package com.lxs.webviewcontainer.lifecycle

import android.os.Looper
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebView

/**
 * @author liuxiaoshuai
 * @date 2019-07-16
 * @desc
 * @email liulingfeng@mistong.com
 */
class DefaultWebLifeCycleImpl constructor(mWebView: WebView) : WebLifeCycle {
    private var webView: WebView? = mWebView

    override fun onResume() {
        webView?.onResume()
        webView?.resumeTimers()
    }

    override fun onPause() {
        webView?.onPause()
        webView?.pauseTimers() //全局停止js的定时器/layout/parsing
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        webView?.let {
            if (keyCode == KeyEvent.KEYCODE_BACK && it.canGoBack()) {
                it.goBack()
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return
        }
        webView?.loadUrl("about:blank")
        webView?.stopLoading()
        webView?.handler?.removeCallbacksAndMessages(null)
        webView?.removeAllViews()
        (webView?.parent as? ViewGroup)?.removeView(webView)
        webView?.webViewClient = null
        webView?.webChromeClient = null
        webView?.clearHistory()
        webView?.destroy()
    }
}