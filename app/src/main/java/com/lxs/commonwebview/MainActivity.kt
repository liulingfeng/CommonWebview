package com.lxs.commonwebview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.google.gson.Gson
import com.lxs.webviewcontainer.anntation.H5Retrofit
import com.lxs.webviewcontainer.CommonWebViewFragment
import com.lxs.webviewcontainer.H5CallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var commonFragment: CommonWebViewFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        commonFragment = fragment as CommonWebViewFragment
        commonFragment?.getWebView()?.loadUrl("file:///android_asset/demo.html")

        val user = User("刘小帅", 23)

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
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        commonFragment?.getWebView()?.getWebLifeCycle()?.let {
            if (it.onKeyDown(keyCode, event)) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
