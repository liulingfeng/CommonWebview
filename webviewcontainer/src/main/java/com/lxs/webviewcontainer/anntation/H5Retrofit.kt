package com.lxs.webviewcontainer.anntation

import com.lxs.webviewcontainer.CommonWebView
import com.lxs.webviewcontainer.H5CallBack
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import java.lang.reflect.Proxy.newProxyInstance

/**
 * @author liuxiaoshuai
 * @date 2019-07-24
 * @desc
 * @email liulingfeng@mistong.com
 */
@Suppress("UNCHECKED_CAST")
class H5Retrofit private constructor() {

    companion object {
        private var commonWebView: WeakReference<CommonWebView?>? = null
        fun getInstance(commonWebView: CommonWebView?): H5Retrofit {
            Companion.commonWebView = WeakReference(commonWebView)
            return SingletonHolder.holder
        }
    }

    private object SingletonHolder {
        val holder = H5Retrofit()
    }

    fun <T> create(service: Class<T>): T? {
        //检查是不是接口
        validateServiceInterface(service)

        return newProxyInstance(
            service.classLoader, arrayOf<Class<*>>(service)
        ) { _, method, args ->
            val annotations = method?.annotations

            annotations?.forEach {
                when (it) {
                    is H5ToNative -> {
                        commonWebView?.get()
                            ?.registerNativeMethod(it.methodName, getParam(method, args), object : H5CallBack {
                                override fun callBack(data: String) {
                                    getCallBackParam(method, args)?.callBack(data)
                                }

                            })
                    }
                    is NativeToH5 -> {
                        commonWebView?.get()
                            ?.callH5Method(it.methodName, getParam(method, args), object : H5CallBack {
                                override fun callBack(data: String) {
                                    getCallBackParam(method, args)?.callBack(data)
                                }

                            })
                    }
                }
            }
        } as? T
    }

    private fun getParam(method: Method, args: Array<out Any>): String {
        var param = ""
        val paramAnnotation = method.parameterAnnotations
        val types = method.parameterTypes
        if (paramAnnotation.isEmpty()) {
            param = ""
            return param
        }

        for ((index, annotation) in paramAnnotation.withIndex()) {
            annotation.forEach {
                if (it is Field && String::class.java.name == types[index].name) {
                    param = args[index].toString() //取到第一个符合的参数就返回
                    return@forEach
                }
            }
        }

        return param
    }

    private fun getCallBackParam(method: Method, args: Array<out Any>): H5CallBack? {
        var h5CallBack: H5CallBack? = null
        val paramAnnotation = method.parameterAnnotations
        val types = method.parameterTypes
        if (paramAnnotation.isEmpty()) {
            h5CallBack = null
            return h5CallBack
        }

        for ((index, annotation) in paramAnnotation.withIndex()) {
            annotation.forEach {
                if (it is CallBack && H5CallBack::class.java.name == types[index].name) {
                    h5CallBack = args[index] as H5CallBack //取到第一个符合的参数就返回
                    return@forEach
                }
            }
        }
        return h5CallBack
    }

    private fun <T> validateServiceInterface(service: Class<T>) {
        if (!service.isInterface) {
            throw IllegalArgumentException("API declarations must be interfaces.")
        }

        if (service.interfaces.isNotEmpty()) {
            throw IllegalArgumentException("API interfaces must not extend other interfaces.")
        }
    }
}