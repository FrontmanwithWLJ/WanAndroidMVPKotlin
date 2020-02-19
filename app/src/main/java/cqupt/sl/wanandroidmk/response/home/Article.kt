package cqupt.sl.wanandroidmk.response.home

import cqupt.sl.wanandroidmk.response.home.item.ArticleItem

data class Article(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
) {
    data class Data(
        val curPage: Int,
        val datas: List<ArticleItem>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
    )
}