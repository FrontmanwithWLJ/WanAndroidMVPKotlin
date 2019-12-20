package cqupt.sl.wanandroidmk.base

import java.lang.ref.WeakReference

abstract class BasePresenter<V:IView> : IPresenter<V> {
    lateinit var iView : WeakReference<V>

    override fun attachView(v: V) {
        iView = WeakReference(v)
    }

    override fun detachView() {
        iView.clear()
    }

    override fun isAttach(): Boolean {
        return iView.get()!=null
    }

    override fun getView(): V? {
        return iView.get()
    }
}