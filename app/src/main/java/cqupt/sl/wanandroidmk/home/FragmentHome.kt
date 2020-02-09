package cqupt.sl.wanandroidmk.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cqupt.sl.wanandroidmk.MainActivity
import cqupt.sl.wanandroidmk.R
import cqupt.sl.wanandroidmk.home.adapter.ArticleAdapter
import cqupt.sl.wanandroidmk.home.adapter.BannerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentHome(private val mainActivity: MainActivity) : Fragment(), HomeContract.View,
    View.OnTouchListener {
    //轮播图
    private val bannerList = ArrayList<String>()
    private lateinit var bannerAdapter: BannerAdapter
    //文章列表
    private val articleList = ArrayList<ArticleItem>()
    private lateinit var articleAdapter: ArticleAdapter
    //当前文章加载到哪一页
    private var currentIndex = -1
    //记录用户触摸位置
    private var oldY: Float = 0f
    private var homePresenter = HomePresenter(this)
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    bannerAdapter.notifyDataSetChanged()
                    //开启轮播
                    //articleAdapter.startBanner(0)
                }
                2 -> {
                    articleAdapter.notifyDataSetChanged()
                    articleAdapter.startBanner(0)
                }
            }
            if (pull.isRefreshing)
                pull.isRefreshing = false
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

    override fun onStart() {
        super.onStart()
        init()
        homePresenter.start()
    }

    @SuppressLint("ResourceType")
    private fun init() {
        //将toolbar放在所有视图的前面，不被遮挡
        toolbar.bringToFront()
        //轮播图
        bannerAdapter =
            BannerAdapter(bannerList)
        val linearLayoutManager = LinearLayoutManager(context)
        home_article.layoutManager = linearLayoutManager
        //设置分割线
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let { it ->
            ContextCompat.getDrawable(it, R.drawable.home_article_divider)
                ?.let { divider.setDrawable(it) }
        }
        home_article.addItemDecoration(divider)
        //文章
        articleAdapter = ArticleAdapter(
            articleList,
            this,
            bannerAdapter
        )
        home_article.adapter = articleAdapter
        home_article.setOnTouchListener(this)
        //下拉控件
        pull.setOnRefreshListener {
            if(pull.isRefreshing){
                refresh()
            }
        }
        //自动加载
        pull.isRefreshing = true
        refresh()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            oldY = event.rawY
            return false
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            val offsetY = event.rawY - oldY
            if (offsetY in -2f..2f)
                return false
            oldY = event.rawY
            Thread { mainActivity.moveTabLayout(offsetY) }.run()
        }

        return false
    }

    //清空文章和轮播图信息重新加载
    private fun refresh(){
        currentIndex = -1
        bannerList.clear()
        articleList.clear()

        homePresenter.getBanner()
        homePresenter.getTopArticle()
        homePresenter.getArticle(currentIndex++)
    }

    /**
     * @param offsetY 正负代表上下移动tab，数值代表移动距离
     */
    private fun moveToolbar(offsetY: Float) {
        val oldY = toolbar.y
        val height = toolbar.height.toFloat()
        if (offsetY < 0 && oldY in -height..0f) {
            toolbar.y = when {
                toolbar.y + offsetY < -height -> -height
                else -> toolbar.y + offsetY
            }
        } else if (offsetY > 0 && oldY in -height..0f) {
            toolbar.y = when {
                toolbar.y + offsetY > 0f -> 0f
                else -> toolbar.y + offsetY
            }
        }
    }

    @Synchronized
    override fun onShowBanner(banners: ArrayList<String>) {
        banners.forEach {
            bannerList.add(it)
        }
        bannerList.add(banners[0])
        handler.sendMessage(Message.obtain().apply { what = 1 })
    }

    @Synchronized
    override fun onShowArticle(article: ArrayList<ArticleItem>) {
        article.forEach {
            articleList.add(it)
        }
        articleList.add(article[0])
        handler.sendMessage(Message.obtain().apply { what = 2 })
    }

    @SuppressLint("ShowToast")
    override fun onShowError(msg: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
    }

    override fun isActive(): Boolean {
        return isAdded
    }
}