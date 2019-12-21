package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.base.BasePresenter
import cqupt.sl.wanandroidmk.base.BaseView
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.homearticle.Article
import cqupt.sl.wanandroidmk.response.homearticle.Banner

interface HomeContract {
    interface Presenter:BasePresenter{
        fun getBanner()
        fun getArticle(page:Int)
    }

    interface View : BaseView<Presenter>{
        //这里的更新操作，
        fun onShowBanner(banners: ArrayList<String>)
        fun onShowArticle(article: ArrayList<ArticleItem>)
    }

    interface Model{
        fun loadBanner(netCallBack: NetCallBack<Banner>)
        fun loadArticle(page:Int,netCallBack: NetCallBack<Article>)
    }
}