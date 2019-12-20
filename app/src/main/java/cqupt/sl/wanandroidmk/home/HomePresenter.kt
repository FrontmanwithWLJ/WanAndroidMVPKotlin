package cqupt.sl.wanandroidmk.home

import cqupt.sl.wanandroidmk.base.BasePresenter

class HomePresenter() : BasePresenter<HomeContract.View>(),HomeContract.Presenter {

    val homeModel = HomeModel()

    init {

    }

    override fun getBanner() {
    }

    override fun getArticle(page: Int) {

    }

}