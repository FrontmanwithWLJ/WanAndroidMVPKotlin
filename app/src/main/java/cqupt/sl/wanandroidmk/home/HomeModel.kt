package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.net.request.NetUtils
import cqupt.sl.wanandroidmk.response.homearticle.Article
import cqupt.sl.wanandroidmk.response.homearticle.Banner
import cqupt.sl.wanandroidmk.response.homearticle.TopArticle

object HomeModel:HomeContract.Model {

    override fun loadBanner(netCallBack: NetCallBack<Banner>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("/banner/json", mapOf("" to ""),netCallBack,Banner::class.java)

    override fun loadArticle(page: Int,netCallBack: NetCallBack<Article>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("article/list/$page/json", mapOf("" to ""), netCallBack,Article::class.java)

    override fun loadTopArticle(netCallBack: NetCallBack<TopArticle>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("/article/top/json", mapOf("" to ""),netCallBack,TopArticle::class.java)

}