package cqupt.sl.wanandroidmk.home

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.net.request.NetUtils
import cqupt.sl.wanandroidmk.res.Res
import cqupt.sl.wanandroidmk.response.home.Article
import cqupt.sl.wanandroidmk.response.home.Banner
import cqupt.sl.wanandroidmk.response.home.TopArticle
import java.io.*

class HomeModel(val context: Context) :HomeContract.Model {
    override fun loadBanner(netCallBack: NetCallBack<Banner>) =
        NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("/banner/json", mapOf("" to ""),netCallBack,Banner::class.java)
    override fun loadBannerWithStroge(netCallBack: NetCallBack<Banner>) {
        val input = context.openFileInput(Res.HOME_PATH+Res.HOME_BANNER)
        var byte = ByteArray(1024)
        var json = ""
        while (input.read(byte)!=-1){
            json+=String(byte)
        }
        Log.e("SL",json)
        netCallBack.onSuccess(Gson().fromJson(json,Banner::class.java))
    }
    override fun saveBanner(json: String) {
        val out = context.openFileOutput(Res.HOME_PATH+Res.HOME_BANNER,Context.MODE_PRIVATE)
        val writer = BufferedWriter(OutputStreamWriter(out))
        writer.write(json)
    }


    override fun loadArticle(page: Int,netCallBack: NetCallBack<Article>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("article/list/$page/json", mapOf("" to ""), netCallBack,Article::class.java)
    override fun loadArticleWithStroge(page: Int, netCallBack: NetCallBack<Article>) {

    }
    override fun saveArticle(page:Int,json: String) {
        val out = context.openFileOutput(Res.HOME_PATH+Res.HOME_ARTICLE,Context.MODE_PRIVATE)
        val writer = BufferedWriter(OutputStreamWriter(out))
        writer.write(json)
    }


    override fun loadTopArticle(netCallBack: NetCallBack<TopArticle>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("/article/top/json", mapOf("" to ""),netCallBack,TopArticle::class.java)
    override fun loadTopArticleWithStroge(netCallBack: NetCallBack<TopArticle>) {

    }
    override fun saveTopArticle(json: String) {
        val out = context.openFileOutput(Res.HOME_PATH+Res.HOME_TOP_ARTICLE,Context.MODE_PRIVATE)
        val writer = BufferedWriter(OutputStreamWriter(out))
        writer.write(json)
    }


}