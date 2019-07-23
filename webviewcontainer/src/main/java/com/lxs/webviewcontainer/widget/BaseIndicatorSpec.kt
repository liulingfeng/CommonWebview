package com.lxs.webviewcontainer.widget

/**
 * @author liuxiaoshuai
 * @date 2019-07-10
 * @desc
 * @email liulingfeng@mistong.com
 */
interface BaseIndicatorSpec {
    fun show()
    fun hide()
    fun reset()
    fun setProgress(newProgress: Int)
}