package cqupt.sl.wanandroidmk.response.home

import cqupt.sl.wanandroidmk.response.home.item.BannerItem

data class Banner(
    val `data`: List<BannerItem>,
    val errorCode: Int,
    val errorMsg: String
)