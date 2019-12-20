package cqupt.sl.wanandroidmk.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cqupt.sl.wanandroidmk.R
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.net.request.NetUtils
import cqupt.sl.wanandroidmk.response.homearticle.Banner
import okhttp3.ResponseBody

class FragmentHome(context: Context) : Fragment() {
    //轮播图
    private val bannerList = arrayListOf<String>()
    private lateinit var banner: ViewPager
    private lateinit var bannerAdapter:BannerAdapter

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1->bannerAdapter.notifyDataSetChanged()
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

    private fun init(view: View) {
        banner = view.findViewById(R.id.banner)
        bannerAdapter= BannerAdapter(bannerList, banner.context)
        banner.adapter = bannerAdapter
        NetUtils
            .baseUrl("https://www.wanandroid.com")
            .get("/banner/json", mapOf("" to ""),object : NetCallBack<Banner>{
                override fun onSuccess(response: Banner) {
                    for (data in response.data) {
                        bannerList.add(data.imagePath)
                        //println(data.imagePath)
                    }
                    handler.sendMessage(Message.obtain().apply { what=1 })
                }

                override fun onFailure(errorBody: ResponseBody?) {
                }

            },Banner::class.java)
    }

}