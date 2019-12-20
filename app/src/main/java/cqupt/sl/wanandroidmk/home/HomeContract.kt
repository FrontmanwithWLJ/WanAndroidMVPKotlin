package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.base.IView
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.homearticle.ArticleList
import cqupt.sl.wanandroidmk.response.homearticle.Banner

interface HomeContract {
    interface Presenter{
        fun getBanner()
        fun getArticle(page: Int)
    }

    interface View : IView{
        //这里的更新操作，
        fun onBanner()
        fun onArticle()
    }

    interface Model{
        fun loadBanner(netCallBack: NetCallBack<Banner>)
        fun loadArticle(page:Int,netCallBack: NetCallBack<ArticleList>)
    }
}