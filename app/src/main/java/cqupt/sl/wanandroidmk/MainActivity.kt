package cqupt.sl.wanandroidmk

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
   private lateinit var viewPager : ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        //设置状态栏字体颜色
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        viewPager = findViewById(R.id.fragment)
        viewPager.adapter = ViewPagerAdapter(this,2)
    }
}
