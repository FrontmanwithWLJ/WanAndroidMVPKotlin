package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.net.request.NetUtils
import cqupt.sl.wanandroidmk.response.homearticle.ArticleList
import cqupt.sl.wanandroidmk.response.homearticle.Banner

class HomeModel:HomeContract.Model {
    //首页轮播图的图片url合集
    val bannerList = ArrayList<String>()
    //轮播图适配器
    //val bannerAdapter = BannerAdapter(bannerList)

    //首页文章的信息合集
    val articleList = ArrayList<Article>()

    override fun loadBanner(netCallBack: NetCallBack<Banner>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("/banner/json", mapOf("" to ""),netCallBack,Banner::class.java)

    override fun loadArticle(page: Int,netCallBack: NetCallBack<ArticleList>) = NetUtils
        .baseUrl("https://www.wanandroid.com")
        .get("article/list/$page/json", mapOf("" to ""),netCallBack,ArticleList::class.java)
}