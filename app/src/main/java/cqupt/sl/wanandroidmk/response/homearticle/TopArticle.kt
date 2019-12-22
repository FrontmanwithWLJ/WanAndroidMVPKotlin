package cqupt.sl.wanandroidmk.response.homearticle

import cqupt.sl.wanandroidmk.home.ArticleItem

data class TopArticle(
    val `data`: List<ArticleItem>,
    val errorCode: Int,
    val errorMsg: String
)