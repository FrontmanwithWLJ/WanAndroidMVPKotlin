package cqupt.sl.wanandroidmk.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cqupt.sl.wanandroidmk.R

class ArticleAdapter(private val articles: ArrayList<ArticleItem>, private val mContext: FragmentHome) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val BANNER = 0
    private val COMMON = 1
    private val FOOT = 2
    private var tips = "正在加载更多数据..."

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //mContext.inflate()方式加载会导致item不占满屏幕
        return when (viewType) {
            BANNER->{
                val view = LayoutInflater.from(mContext.context).inflate(R.layout.home_banner,parent,false)
                BannerViewHolder(view)
            }
            COMMON -> {
                val view = LayoutInflater.from(mContext.context).inflate(R.layout.home_article_item,parent,false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(mContext.context).inflate(R.layout.home_foottip,parent,false)
                FootViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = articles[position]
        if (holder is BannerViewHolder){
            holder.banner = mContext.getBanner()
        }
        if (holder is ViewHolder) {
            holder.author.text = item.author
            holder.title.text = item.title
            holder.date.text = item.niceDate
            if (item.isTop){
                holder.top.visibility = View.VISIBLE
            }
            val tags = item.tags
            tags.forEach {
                val textView = TextView(mContext.context)
                textView.text = it.name
                textView.setBackgroundResource(R.drawable.home_tag_item)
                holder.tags.addView(textView)
            }
        } else if (holder is FootViewHolder) {
            holder.tips.text = tips
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1)
            return FOOT
        if (position == 0)
            return BANNER
        return COMMON
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var author: TextView = itemView.findViewById(R.id.author)
        var date: TextView = itemView.findViewById(R.id.date)
        var tags: LinearLayout = itemView.findViewById(R.id.tags)
        var top:TextView = itemView.findViewById(R.id.home_article_top)
    }

    class FootViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tips: TextView = itemView.findViewById(R.id.tips)
    }

    class BannerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var banner:ViewPager = itemView.findViewById(R.id.banner)
    }
}