package cqupt.sl.wanandroidmk.home

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import cqupt.sl.wanandroidmk.MainActivity
import cqupt.sl.wanandroidmk.R
import cqupt.sl.wanandroidmk.home.adapter.ArticleAdapter
import cqupt.sl.wanandroidmk.home.adapter.BannerAdapter
import cqupt.sl.wanandroidmk.response.home.item.ArticleItem
import cqupt.sl.wanandroidmk.response.home.item.BannerItem
import cqupt.sl.wanandroidmk.widget.pullrefresh.CanScroll
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentHome(private val mainActivity: MainActivity) : Fragment(), HomeContract.View,
    View.OnTouchListener {
    //轮播图
    private val bannerList = ArrayList<BannerItem>()
    private lateinit var bannerAdapter: BannerAdapter
    //文章列表
    private val articleList = ArrayList<ArticleItem?>()
    private lateinit var articleAdapter: ArticleAdapter
    //当前文章加载到哪一页
    private var currentIndex = -1
    //记录用户触摸位置
    private var oldY: Float = 0f
    //记录当前tab的位置
    private var tabCurrentPosition = 0f
    private val homePresenter by lazy { HomePresenter(context!!,this) }
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    bannerAdapter.notifyDataSetChanged()
                }
                2 -> {
                    articleAdapter.notifyDataSetChanged()
                }
            }
            if (down_refresh.isRefreshing)
                down_refresh.isRefreshing = false
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

        //上拉加载
        var scrollState = RecyclerView.SCROLL_STATE_SETTLING
        home_article.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollState = newState
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Log.e("SL","scrollstate = $scrollState,position = ${linearLayoutManager.findLastVisibleItemPosition()},complete position = ${linearLayoutManager.findLastCompletelyVisibleItemPosition()},itemcount = ${articleAdapter.itemCount}")
                if (scrollState == RecyclerView.SCROLL_STATE_SETTLING && linearLayoutManager.findLastVisibleItemPosition() == articleAdapter.itemCount - 1) {
                    homePresenter.getArticle(++currentIndex)
                }
            }
        })

        //下拉控件
        down_refresh.setOnRefreshListener {
            if(down_refresh.isRefreshing){
                refresh()
            }
        }
        //自动加载
        down_refresh.isRefreshing = true
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
            mainActivity.moveTabLayout(offsetY)
        }
        return false
    }

    //清空文章和轮播图信息重新加载
    private fun refresh(){
        currentIndex = -1
        bannerList.clear()
        articleList.clear()
        //占位,banner
        articleList.add(null)

        homePresenter.getBanner()
        homePresenter.getTopArticle()
        homePresenter.getArticle(++currentIndex)
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
    override fun onShowBanner(banners: ArrayList<BannerItem>) {
        banners.forEach {
            bannerList.add(it)
        }
        handler.sendMessage(Message.obtain().apply { what = 1 })
    }

    @Synchronized
    override fun onShowArticle(article: ArrayList<ArticleItem>) {
        article.forEach {
            articleList.add(it)
        }
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