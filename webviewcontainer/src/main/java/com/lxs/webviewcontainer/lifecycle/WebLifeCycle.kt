package com.lxs.webviewcontainer.lifecycle

import android.view.KeyEvent

/**
 * @author liuxiaoshuai
 * @date 2019-07-16
 * @desc
 * @email liulingfeng@mistong.com
 */
interface WebLifeCycle {
    fun onResume()
    fun onPause()
    fun onKeyDown(keyCode: Int, event: KeyEvent?):Boolean
    fun onDestroy()
}