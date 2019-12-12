package cqupt.sl.wanandroidmk.net.callback

import okhttp3.ResponseBody

interface NetCallBack<T> {
    fun onSuccess(response : T)
    fun onFailure(errorBody: ResponseBody?)
}