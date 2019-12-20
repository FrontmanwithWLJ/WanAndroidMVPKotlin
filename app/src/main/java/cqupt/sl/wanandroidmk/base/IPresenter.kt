package cqupt.sl.wanandroidmk.base

interface IPresenter<V:IView> {
    fun attachView(v:V)
    fun detachView()
    fun isAttach():Boolean
    fun getView(): V?
}