package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.response.homearticle.Banner
import okhttp3.ResponseBody


class HomePresenter(val iView: HomeContract.View) : HomeContract.Presenter {

    private val homeModel = HomeModel

    override fun getBanner() {
        homeModel.loadBanner(object : NetCallBack<Banner>{
            override fun onSuccess(response: Banner) {
                val banners = ArrayList<String>()
                response.data.forEach{
                    banners.add(it.imagePath)
                }
                banners.add(response.data[0].imagePath)
                iView.onShowBanner(banners)
            }

            override fun onFailure(errorBody: ResponseBody?) {
            }

        })
    }

    override fun getArticle(page: Int) {
    }

    override fun start() {

    }

}