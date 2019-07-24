package com.lxs.webviewcontainer

/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
interface IMutual {
    //注册native方法
    fun registerNativeMethod(methodName: String, returnData: String?, h5CallBack: H5CallBack)

    //调用h5方法
    fun callH5Method(methodName: String, data: String, h5CallBack: H5CallBack)
}