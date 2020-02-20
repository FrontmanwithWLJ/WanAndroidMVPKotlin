package cqupt.sl.wanandroidmk.widget.banner

import android.view.View
import androidx.viewpager.widget.PagerAdapter

abstract class BannerAdapter<T>(private val picUrlList: ArrayList<T>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return picUrlList.size
    }
    override fun notifyDataSetChanged() {
        //每次刷新数据，都在末尾添加头部
        if (picUrlList.size == 1 || (picUrlList.size > 0 && picUrlList[0] != picUrlList[picUrlList.size - 1]))
        //只有一个数据或者头尾不相等
            picUrlList.add(picUrlList[0])
        super.notifyDataSetChanged()
    }

}