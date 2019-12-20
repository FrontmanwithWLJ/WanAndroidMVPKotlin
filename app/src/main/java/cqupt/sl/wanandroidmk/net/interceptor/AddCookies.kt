package cqupt.sl.wanandroidmk.net.interceptor

import cqupt.sl.wanandroidmk.net.cookies.CookiesUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 向网络请求中添加Cookies
 */
class AddCookiesUtils :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var builder = chain.request().newBuilder()
        if (!CookiesUtils.cookies.isEmpty()){
            builder.addHeader("Cookie",CookiesUtils.cookies)
        }
        return chain.proceed(builder.build())
    }

}