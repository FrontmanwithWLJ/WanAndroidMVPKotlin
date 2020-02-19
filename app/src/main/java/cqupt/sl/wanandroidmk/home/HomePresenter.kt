package cqupt.sl.wanandroidmk.home

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import cqupt.sl.wanandroidmk.response.home.item.ArticleItem
import cqupt.sl.wanandroidmk.response.home.item.BannerItem
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.home.Article
import cqupt.sl.wanandroidmk.response.home.Banner
import cqupt.sl.wanandroidmk.response.home.TopArticle
import okhttp3.ResponseBody


class HomePresenter(val context: Context,private val iView: HomeContract.View) : HomeContract.Presenter {

    var noInternet = false
    private val homeModel :HomeModel by lazy { HomeModel(context) }
    override fun getBanner() {
        if (noInternet){
            homeModel.loadBannerWithStroge(object : NetCallBack<Banner> {
                override fun onSuccess(response: Banner) {
                    if (iView.isActive())
                        iView.onShowBanner(response.data as ArrayList<BannerItem>)
                }
                override fun onFailure(errorBody: ResponseBody?) {
                }
            })
            return
        }
        homeModel.loadBanner(object : NetCallBack<Banner> {
            override fun onSuccess(response: Banner) {
                if (iView.isActive())
                    iView.onShowBanner(response.data as ArrayList<BannerItem>)
            }
            override fun onFailure(errorBody: ResponseBody?) {
            }
        })
    }

    override fun getTopArticle() {
        homeModel.loadTopArticle(object : NetCallBack<TopArticle> {
            override fun onSuccess(response: TopArticle) {
                response.data.forEach {
                    it.isTop = true
                }
                val arrayList = response.data as ArrayList<ArticleItem>
                //arrayList.add(0, arrayList[0])
                if (iView.isActive())
                    iView.onShowArticle(arrayList)
            }

            override fun onFailure(errorBody: ResponseBody?) {
            }
        })
    }

    override fun getArticle(page: Int) {
        homeModel.loadArticle(page, object : NetCallBack<Article> {
            override fun onSuccess(response: Article) {
                val arrayList = ArrayList<ArticleItem>()
                response.data.datas.forEach {
                    it.isTop = false
                    arrayList.add(it)
                }
                if (iView.isActive())
                    iView.onShowArticle(arrayList)
            }

            override fun onFailure(errorBody: ResponseBody?) {
            }
        })
    }

    override fun start() {

    }
}