package cqupt.sl.wanandroidmk.activity.main.fragment.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import cqupt.sl.wanandroidmk.res.Res
import cqupt.sl.wanandroidmk.response.home.item.BannerItem
import cqupt.sl.wanandroidmk.activity.web.WebActivity
import cqupt.sl.wanandroidmk.widget.banner.BannerAdapter
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

class BannerAdapter(private val mContext:Context,private val pictureList: ArrayList<BannerItem>) : BannerAdapter<BannerItem>(pictureList) {
    private val TAG = "BannerPicture"

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = pictureList[position]
        val image = ImageView(container.context)
        val file = loadPic(item.id)
        if (file!=null){
            //加载本地缓存
            Glide.with(container.context)
                .load(file)
                .into(image)
        }else {
            Glide.with(container.context)
                .asBitmap()
                .load(item.imagePath)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        savePic(position, checkNotNull(resource))
                        //返回true图片不显示
                        return false
                    }
                })
                .into(image)
        }
        container.addView(image)
        //image.setOnClickListener {  }
        image.setOnClickListener{
            WebActivity.goToWeb(mContext,item.url,false)
        }
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

    private fun savePic(position: Int, picture: Bitmap) {
        GlobalScope.launch(Dispatchers.IO) {
        val file = File(Res.BASE_PATH, "${pictureList[position].id}.jpg")
        if (!file.exists()) {
            if (!file.createNewFile()){
                Log.w(TAG,"can not create file")
                return@launch
            }
        }
        val out = FileOutputStream(file)
        picture.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        }
    }

    private fun loadPic(id: Int): File? {
        val file = File(Res.BASE_PATH,"$id.jpg")
        if (file.exists()) {
            //Log.e("SL","利用缓存${Res.BASE_PATH}")
            return file
        }
        return null
    }
}