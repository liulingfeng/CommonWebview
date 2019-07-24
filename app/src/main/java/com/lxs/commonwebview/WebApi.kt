package com.lxs.commonwebview

import com.lxs.webviewcontainer.H5CallBack
import com.lxs.webviewcontainer.anntation.CallBack
import com.lxs.webviewcontainer.anntation.Field
import com.lxs.webviewcontainer.anntation.H5ToNative
import com.lxs.webviewcontainer.anntation.NativeToH5

/**
 * @author liuxiaoshuai
 * @date 2019-07-23
 * @desc
 * @email liulingfeng@mistong.com
 */
interface WebApi {
    @H5ToNative(methodName = "submitFromWeb")
    fun submitFromWeb(@Field returnData: String, @CallBack h5CallBack: H5CallBack)

    @NativeToH5(methodName = "functionInJs")
    fun functionInJs(@Field responseData: String, @CallBack h5CallBack: H5CallBack)
}