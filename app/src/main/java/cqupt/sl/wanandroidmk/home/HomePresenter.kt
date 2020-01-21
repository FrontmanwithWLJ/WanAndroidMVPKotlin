package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.homearticle.Article
import cqupt.sl.wanandroidmk.response.homearticle.Banner
import cqupt.sl.wanandroidmk.response.homearticle.TopArticle
import okhttp3.ResponseBody


class HomePresenter(private val iView: HomeContract.View) : HomeContract.Presenter {

    private val homeModel = HomeModel
    override fun getBanner() {
        homeModel.loadBanner(object : NetCallBack<Banner> {
            override fun onSuccess(response: Banner) {
                val banners = ArrayList<String>()
                response.data.forEach {
                    banners.add(it.imagePath)
                }
                if (iView.isActive())
                    iView.onShowBanner(banners)
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
                    //Log.e("SL", "实际是： $it")
                }
                val arrayList = response.data as ArrayList<ArticleItem>
                arrayList.add(0, arrayList[0])
                if (iView.isActive())
                    iView.onShowArticle(arrayList)
            }

            override fun onFailure(errorBody: ResponseBody?) {
            }
        })
    }


    /**
     * @param page =-1 加载置顶文章另外在加载一页普通文章
     */
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