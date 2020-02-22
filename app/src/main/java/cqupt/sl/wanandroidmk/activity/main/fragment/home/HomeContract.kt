package cqupt.sl.wanandroidmk.activity.main.fragment.home

import cqupt.sl.wanandroidmk.base.BasePresenter
import cqupt.sl.wanandroidmk.base.BaseView
import cqupt.sl.wanandroidmk.response.home.item.ArticleItem
import cqupt.sl.wanandroidmk.response.home.item.BannerItem
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.home.Article
import cqupt.sl.wanandroidmk.response.home.Banner
import cqupt.sl.wanandroidmk.response.home.TopArticle

interface HomeContract {
    interface Presenter:BasePresenter{
        fun getBanner()
        fun getTopArticle()
        fun getArticle(page:Int)
    }

    interface View : BaseView<Presenter>{
        //这里的更新操作，
        fun onShowBanner(banners: ArrayList<BannerItem>)
        fun onShowTopArticle(article: ArrayList<ArticleItem>)
        fun onShowArticle(article: ArrayList<ArticleItem>)
        fun onShowError(msg:String)
    }

    interface Model{
        fun loadBanner(netCallBack: NetCallBack<Banner>)
        fun loadBannerWithStroge(netCallBack: NetCallBack<Banner>)
        fun saveBanner(json:String)

        fun loadArticle(page:Int,netCallBack: NetCallBack<Article>)
        fun loadArticleWithStroge(page:Int,netCallBack: NetCallBack<Article>)
        fun saveArticle(page:Int,json:String)

        fun loadTopArticle(netCallBack: NetCallBack<TopArticle>)
        fun loadTopArticleWithStroge(netCallBack: NetCallBack<TopArticle>)
        fun saveTopArticle(json:String)
    }
}