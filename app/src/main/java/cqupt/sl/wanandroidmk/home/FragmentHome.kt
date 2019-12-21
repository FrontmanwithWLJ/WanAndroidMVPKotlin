package cqupt.sl.wanandroidmk.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cqupt.sl.wanandroidmk.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentHome : Fragment(), ViewPager.OnPageChangeListener,HomeContract.View {

    //轮播图
    private val bannerList= ArrayList<String>()
    private lateinit var banner: ViewPager
    private lateinit var bannerAdapter:BannerAdapter
    private var bannerRun = false

    private var homePresenter = HomePresenter(this)

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            Log.e("SL","Receive msg")
            super.handleMessage(msg)
            when(msg.what){
                1-> {
                    bannerAdapter.notifyDataSetChanged()
                    //开启轮播
                    startBanner(0)
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, null)
        init(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        //开始加载初始数据
        homePresenter.start()
    }

    private fun init(view: View) {

        banner = view.findViewById(R.id.banner)
        banner.addOnPageChangeListener(this)
        bannerAdapter= BannerAdapter(bannerList, banner.context)
        banner.adapter = bannerAdapter
        //禁止用户滑动
        banner.setOnTouchListener { _, _ -> false }

    }

    //协程来实现轮播图
    private fun startBanner(i:Int){
        if (bannerRun){
            Log.v("SL","banner is running.")
            return
        }
        var index = i
        Log.e("SL","Begin to launch")
        GlobalScope.launch (Dispatchers.Main) {
            bannerRun = true
            while (true) {
                banner.currentItem = index++
                if (index == bannerAdapter.count+1) {
                    banner.setCurrentItem(0, false)
                    index = 1
                }
                delay(3000)
            }
        }
        bannerRun = false
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onShowBanner(banners: ArrayList<String>) {
        banners.forEach {
            bannerList.add(it)
        }
        handler.sendMessage(Message.obtain().apply { what=1 })
    }

    override fun onShowArticle(article: ArrayList<ArticleItem>) {
        
    }

    override fun isActive(): Boolean {
        return isAdded
    }

}