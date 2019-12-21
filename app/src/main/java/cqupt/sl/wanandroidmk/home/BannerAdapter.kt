package cqupt.sl.wanandroidmk.home

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class BannerAdapter(private val pictureList:List<String>, private val context: Context): PagerAdapter() {

    override fun instantiateItem(container : ViewGroup, position : Int):Any {
        //Log.e("SL","POSITION=$position URL=${pictureList[0]}")
        val image = ImageView(context)
        if (position == count-1) {
            Glide.with(context)
                .load(pictureList[0])
                .into(image)
        } else {
            Glide.with(context)
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
}