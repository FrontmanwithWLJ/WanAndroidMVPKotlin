package cqupt.sl.wanandroidmk.widget.pullview
//自定义什么时候父控件可以下拉上拉
interface PullCallBack {
    fun isCanPullDown():Boolean
    fun isCanPullUp():Boolean
}