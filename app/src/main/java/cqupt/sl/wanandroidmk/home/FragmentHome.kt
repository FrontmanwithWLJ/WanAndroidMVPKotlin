package cqupt.sl.wanandroidmk.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import cqupt.sl.wanandroidmk.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import java.util.concurrent.locks.ReentrantLock

class FragmentHome : Fragment(), ViewPager.OnPageChangeListener,HomeContract.View,
    View.OnTouchListener {

    //toolbar动画锁
    private val tLook = ReentrantLock()

    //轮播图
    private lateinit var banner:ViewPager
    private val bannerList= ArrayList<String>()
    private lateinit var bannerAdapter:BannerAdapter
    private var bannerRun = false

    //文章列表
    private val articleList = ArrayList<ArticleItem>()
    private lateinit var articleAdapter: ArticleAdapter
    //当前文章加载到哪一页
    private var currentIndex = -1
    //记录用户触摸位置
    private var oldY:Float = 0f


    private var homePresenter = HomePresenter(this)

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1-> {
                    bannerAdapter.notifyDataSetChanged()
                    //开启轮播
                    startBanner(0)
                }
                2->articleAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onResume() {
        super.onResume()
        init()
        //开始加载初始数据
        homePresenter.start()
    }

    @SuppressLint("ResourceType")
    private fun init() {
        //将toolbar放在所有师徒的前面，不被遮挡
        toolbar.bringToFront()

        val xmlPullParser = resources.getXml(R.layout.home_banner)
        val attr = Xml.asAttributeSet(xmlPullParser)
        banner = ViewPager(context!!,attr)
        banner.addOnPageChangeListener(this)
        bannerAdapter= BannerAdapter(bannerList, banner.context)
        banner.adapter = bannerAdapter


        //禁止用户滑动
        banner.setOnTouchListener { _, _ -> false }
        banner.visibility = View.GONE

        //val view = context?.let { ViewPager(it) }
        //view?.adapter = bannerAdapter
        //home_article.addView(view,0)

        homePresenter.getBanner()
        val linearLayoutManager = object :LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        home_article.layoutManager = linearLayoutManager
        //设置分割线
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let { it -> ContextCompat.getDrawable(it,R.drawable.home_article_divider)?.let { divider.setDrawable(it) } }
        home_article.addItemDecoration(divider)

        articleAdapter = ArticleAdapter(articleList,this)
        home_article.adapter = articleAdapter
        //首次加载传入-1加载置顶文章加载一页普通文章
        homePresenter.getArticle(currentIndex)

        home_article.setOnTouchListener(this)
    }

    private fun hideTab(){
        if (tLook.isLocked || toolbar.visibility == View.GONE) return
        tLook.lock()
        val hide = TranslateAnimation(toolbar.x,toolbar.x,toolbar.y,toolbar.y-toolbar.height)
        hide.duration = 500
        toolbar.animation = hide
        toolbar.startAnimation(hide)
        hide.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                //移动位置
                toolbar.y = toolbar.y-toolbar.height
                //隐藏
                toolbar.visibility = View.GONE
                toolbar.clearAnimation()
                tLook.unlock()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

    private fun showTab(){
        if(tLook.isLocked || toolbar.visibility != View.GONE) return
        tLook.lock()
        val show = TranslateAnimation(toolbar.x,toolbar.x,toolbar.y,toolbar.y+toolbar.height)
        show.duration = 500
        toolbar.visibility = View.VISIBLE
        toolbar.animation = show
        toolbar.startAnimation(show)
        show.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                //隐藏
                toolbar.clearAnimation()
                tLook.unlock()
            }

            override fun onAnimationStart(animation: Animation?) {
                //移动位置
                toolbar.y = toolbar.y+toolbar.height
            }
        })
    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action==MotionEvent.ACTION_DOWN){
            oldY = event.rawY
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            Log.e("SL", "event=${event.rawY}")
            when {
                oldY == 0f -> {
                    oldY = event.rawY
                    return false
                }
                event.rawY - oldY > 30 -> {
                    showTab()
                }
                event.rawY - oldY < -30 -> {
                    hideTab()
                }
            }
        }

        return false

    }
    //协程来实现轮播图
    private fun startBanner(i:Int){
        if (bannerRun){
            return
        }
        var index = i
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
        bannerList.add(banners[0])
        handler.sendMessage(Message.obtain().apply { what=1 })
    }

    override fun onShowArticle(article: ArrayList<ArticleItem>) {
        article.forEach {
            articleList.add(it)
        }
        articleList.add(article[0])
        handler.sendMessage(Message.obtain().apply { what=2 })
    }

    @SuppressLint("ShowToast")
    override fun onShowError(msg: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT)
        }
    }

    override fun isActive(): Boolean {
        return isAdded
    }
    fun getBanner(): ViewPager {
        return banner
    }
}