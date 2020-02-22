package cqupt.sl.wanandroidmk.activity.main.fragment.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cqupt.sl.wanandroidmk.R
import cqupt.sl.wanandroidmk.response.home.item.ArticleItem
import cqupt.sl.wanandroidmk.activity.main.fragment.home.FragmentHome
import cqupt.sl.wanandroidmk.res.Res
import cqupt.sl.wanandroidmk.texthelper.TextHelper
import cqupt.sl.wanandroidmk.activity.web.WebActivity
import cqupt.sl.wanandroidmk.widget.banner.Banner

class ArticleAdapter(
    private val articles: ArrayList<ArticleItem?>,
    private val mContext: FragmentHome,
    private val bannerAdapter: BannerAdapter
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val BANNER = 0
    private val COMMON = 1
    private val TOP = 2
    private var tips = "正在加载更多数据..."

    //banner是否离开了页面
    private var isBannerAttach = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //mContext.inflate()方式加载会导致item不占满屏幕
        return when (viewType) {
            BANNER -> {
                val view = LayoutInflater.from(mContext.context)
                    .inflate(R.layout.home_banner, parent, false)
                BannerViewHolder(
                    view
                )
            }
            COMMON -> {
                val view = LayoutInflater.from(mContext.context)
                    .inflate(R.layout.home_article_item, parent, false)
                ViewHolder(view)
            }
            TOP -> {
                val view = LayoutInflater.from(mContext.context)
                    .inflate(R.layout.home_article_item, parent, false)
                TopViewHolder(
                    view
                )
            }
            else -> {
                val view = LayoutInflater.from(mContext.context)
                    .inflate(R.layout.home_foottip, parent, false)
                FootViewHolder(
                    view
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = articles[position]
        when (holder) {
            is BannerViewHolder -> {
                holder.banner.adapter = bannerAdapter
                holder.banner.setIntervalTime(Res.intervalTime)
                holder.banner.setOnTouchListener { _, _ -> false }
            }
            is TopViewHolder -> {
                holder.author.text = item!!.author
                holder.title.text = TextHelper.replaceStr(item.title)
                holder.date.text = item.niceDate
                holder.top.visibility = View.VISIBLE
                holder.tags.removeAllViews()
                item.tags.forEach {
                    val textView = TextView(mContext.context)
                    textView.text = it.name
                    textView.setBackgroundResource(R.drawable.home_tag_item)
                    holder.tags.addView(textView)
                }
                (holder.title.parent as ViewGroup).setOnClickListener {
                    WebActivity.goToWeb(mContext.context!!,item.link,item.collect)
                }
            }
            is ViewHolder -> {
                holder.author.text = item!!.author
                holder.title.text = TextHelper.replaceStr(item.title)
                holder.date.text = item.niceDate
                //加载标签之前把标签区的内容清空，避免重复
                holder.tags.removeAllViews()
                item.tags.forEach {
                    val textView = TextView(mContext.context)
                    textView.text = it.name
                    textView.setBackgroundResource(R.drawable.home_tag_item)
                    holder.tags.addView(textView)
                }
                (holder.title.parent as ViewGroup).setOnClickListener {
                    WebActivity.goToWeb(mContext.context!!,item.link,item.collect)
                }
            }
            is FootViewHolder -> {
                holder.tips.text = tips
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is BannerViewHolder) {
            isBannerAttach = true
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is BannerViewHolder) {
            isBannerAttach = false
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return BANNER
//        if (position == itemCount - 1)
//            return FOOT
        if (articles[position]!!.isTop)
            return TOP
        return COMMON
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var author: TextView = itemView.findViewById(R.id.author)
        var date: TextView = itemView.findViewById(R.id.date)
        var tags: LinearLayout = itemView.findViewById(R.id.tags)
    }

    class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var author: TextView = itemView.findViewById(R.id.author)
        var date: TextView = itemView.findViewById(R.id.date)
        var tags: LinearLayout = itemView.findViewById(R.id.tags)
        var top: TextView = itemView.findViewById(R.id.home_article_top)
    }

    class FootViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tips: TextView = itemView.findViewById(R.id.tips)
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var banner: Banner = itemView.findViewById(R.id.banner)
    }
}