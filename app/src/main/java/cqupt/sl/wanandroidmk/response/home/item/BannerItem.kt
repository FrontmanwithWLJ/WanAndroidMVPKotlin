package cqupt.sl.wanandroidmk.response.home.item

data class BannerItem (
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)