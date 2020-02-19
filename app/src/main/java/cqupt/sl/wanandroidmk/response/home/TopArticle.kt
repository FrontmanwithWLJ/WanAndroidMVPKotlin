package cqupt.sl.wanandroidmk.response.home

import cqupt.sl.wanandroidmk.response.home.item.ArticleItem

data class TopArticle(
    val `data`: List<ArticleItem>,
    val errorCode: Int,
    val errorMsg: String
)