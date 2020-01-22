package cqupt.sl.wanandroidmk.widget

import android.content.Context
import android.view.MotionEvent
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 自定义ViewPager，禁止滑动
 */
class MyViewPager(context: Context) : ViewPager(context) {
     override fun onInterceptTouchEvent(ev: MotionEvent):Boolean{
         return false
     }

    override fun onTouchEvent(ev:MotionEvent) :Boolean {
        return  false
    }

}