package cqupt.sl.wanandroidmk.activity.main.fragment.home.adapter

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import com.sak.ultilviewlib.adapter.BaseHeaderAdapter

class HeaderAdapter(val context: Context?) : BaseHeaderAdapter(context) {

    val view by lazy {
        TextView(context)
    }

    override fun releaseViewToRefresh(deltaY: Int) {
        view.text = "松开手刷新"
    }

    override fun headerRefreshComplete() {
        view.text = "加载完毕"
        if (view.visibility == View.VISIBLE)
            setVisibility(View.GONE)
    }

    override fun headerRefreshing() {
        view.text = "正在加载"
    }

    override fun pullViewToRefresh(deltaY: Int) {
        view.text = "继续上拉加载"
        if (view.visibility == View.GONE)
            setVisibility(View.VISIBLE)
    }

    override fun getHeaderView(): View {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.textAlignment = View.TEXT_ALIGNMENT_CENTER
            view.visibility = View.GONE
        }
        return view
    }
    @Synchronized
    private fun setVisibility(visible:Int){
        view.visibility = visible
    }
}