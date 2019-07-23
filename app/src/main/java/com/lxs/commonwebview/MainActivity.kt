package com.lxs.commonwebview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.google.gson.Gson
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

        commonFragment?.getWebView()?.registerNativeMethod(
            "submitFromWeb",
            "submitFromWeb exe, response data 中文 from Java",
            object : H5CallBack {
                override fun callBack(data: String) {
                    Log.e("德玛返回值", data)
                }

            })

        val user = User("大头鬼", 23)
        commonFragment?.getWebView()?.callH5Method("functionInJs", Gson().toJson(user), object : H5CallBack {
            override fun callBack(data: String) {
                Log.e("德玛H5返回值", data)
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
