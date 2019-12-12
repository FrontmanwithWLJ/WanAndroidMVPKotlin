package cqupt.sl.wanandroidmk.net.cookies

import android.content.Context

class CookiesUtils {
    private val COOKIESNAME = "WanAndroid"
    companion object{
        var cookies = ""
    }

    /**
     * 程序启动阶段调用，加载登录信息
     */
    fun load(context: Context){
        val sharedPreferences = context.getSharedPreferences(COOKIESNAME, Context.MODE_PRIVATE)
        cookies = sharedPreferences.getString(COOKIESNAME,"").toString()
    }

    /**
     * 保存Cookies到本地
     */
    fun save(context: Context) {
        if ("" == cookies) return
        val sharedPreferences = context.getSharedPreferences(COOKIESNAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(COOKIESNAME,cookies)
        editor.apply()
    }

    /**
     * 从本地移除Cookies
     */

    fun remove(context: Context) {
        val sharedPreferences = context.getSharedPreferences(COOKIESNAME, Context.MODE_PRIVATE)
        //如果存在cookies就删除它
        if("null"!=sharedPreferences.getString(COOKIESNAME,"null")) {
            val editor = sharedPreferences.edit()
            editor.remove(COOKIESNAME)
            editor.apply()
        }
    }
}