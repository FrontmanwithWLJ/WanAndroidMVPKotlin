package cqupt.sl.wanandroidmk.net.interceptor

import cqupt.sl.wanandroidmk.net.cookies.CookiesUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 获取Cookies，存储到静态变量中
 */
class ReceiveCookies :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val response = chain.proceed(chain.request())
        var headers = response.headers("Set-Cookies")
        if (!headers.isNullOrEmpty()){
            for (header in headers) {
                CookiesUtils.cookies = header.substring(header.indexOf("JSESSIONID"),header.indexOf(","))
            }
        }
        return response
    }

}