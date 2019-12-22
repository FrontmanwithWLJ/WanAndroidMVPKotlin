package cqupt.sl.wanandroidmk.home

/**
 * {"apkLink":"","audit":1,"author":"鸿洋","chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":11058,"link":"https://mp.weixin.qq.com/s/gzHjLJsAePgI6PrasYuU9g","niceDate":"2019-12-18 00:00","niceShareDate":"2天前","origin":"","prefix":"","projectLink":"","publishTime":1576598400000,"selfVisible":0,"shareDate":1576759884000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"Android 一些值得你深入的细节 | Window 篇","type":0,"userId":-1,"visible":1,"zan":0}
 */
data class ArticleItem(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    var isTop: Boolean = false
){
    data class Tag(
        val name: String,
        val url : String
    )
}