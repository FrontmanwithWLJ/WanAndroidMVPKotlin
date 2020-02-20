package cqupt.sl.wanandroidmk.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 轮播控件
 */
class Banner : ViewPager {
    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet?):super(context,attributeSet)
    private val TAG = "Banner"
    private var pagerAdapter:PagerAdapter? = null
    //默认时间为3秒
    private var intervalTime:Long = 3000
    //记录是否正在轮播
    private var bannerRunning = false

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        pagerAdapter = adapter
    }

    fun setIntervalTime(time:Long){
        intervalTime = if (time < 0) {
            Log.w(TAG,"time is incorrect")
            3000
        } else time
    }

    fun startBanner(){
        if (pagerAdapter==null){
            throw NullPointerException("banner pagerAdapter is null")
        }
        if (bannerRunning) return
        bannerRunning = true
        GlobalScope.launch(Dispatchers.Main) {
            delay(intervalTime)
            //循环轮播
            while (bannerRunning){
                currentItem++
                delay(intervalTime)
                if (currentItem== pagerAdapter!!.count-1){
                    //平滑过渡到第一张
                    setCurrentItem(0,false)
                }
            }
        }
    }

    fun stopBanner(){
        bannerRunning = false
    }
}