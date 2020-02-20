package cqupt.sl.wanandroidmk.res

import android.Manifest
import android.annotation.TargetApi
import android.os.Build
import java.io.File

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
object Res {
    //运行权限
    val permissions = ArrayList<String>().apply {
        add(Manifest.permission.READ_EXTERNAL_STORAGE)
        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        add(Manifest.permission.CALL_PHONE)
    }

    const val PICTURE_CACHE_PATH = "/picture/"
    const val JSON_CACHE_PATH = "/json/"

    //数据库名，版本号
    const val DATABASE_NAME = "WanAndroidMVPKotlin.db"
    const val DATABASE_VERSION = 1

    //主页数据列名
    var BASE_PATH: File? = null
    var BASE_CACHE_PATH:File? = null
    var HOME_PATH = "home/"
    var HOME_ARTICLE = "home_article.json"
    var HOME_TOP_ARTICLE = "home_top_article.json"
    var HOME_BANNER = "home_banner_"
    var HOME_HOT_KEY = "home_hot_key.json"

    //轮播间隔时间
    const val intervalTime = 3000L
}