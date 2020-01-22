package cqupt.sl.wanandroidmk.home

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class BannerAdapter(private val pictureList:List<String>): PagerAdapter() {

    var isLoading = true
    override fun instantiateItem(container : ViewGroup, position : Int):Any {
        val image = ImageView(container.context)
        if (position == count-1) {
            Glide.with(container.context)
                .load(pictureList[0])
                .into(image)
        } else {
            Glide.with(container.context)
                .load(pictureList[position])
                .into(image)
        }
        container.addView(image)
        return image
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return pictureList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun notifyDataSetChanged(){
        super.notifyDataSetChanged()
        isLoading = false
    }
}