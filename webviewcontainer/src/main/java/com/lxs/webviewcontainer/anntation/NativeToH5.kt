package com.lxs.webviewcontainer.anntation


/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NativeToH5(val methodName: String) {
}