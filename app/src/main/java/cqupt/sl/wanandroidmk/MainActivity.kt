package cqupt.sl.wanandroidmk

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import cqupt.sl.wanandroidmk.home.FragmentHome
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init()

    }

    private fun init() {
        //设置状态栏字体颜色
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val viewPager = findViewById<ViewPager2>(R.id.fragment)
        //viewPager.adapter = object : RecyclerView.Adapter<>()

//        val fragments = ArrayList<Fragment>()
//        fragments.add(FragmentHome(this))
//        fragments.add(FragmentHome(this))
//        val fragmentManager = supportFragmentManager
//        fragmentManager.putFragment(Bundle(),"home",FragmentHome(this))
//        //viewPager.adapter = object : FragmentPagerAdapter()
////            override fun getItem(position: Int): Fragment {
////                return fragments[position]
////            }
////
////            override fun getCount(): Int {
////                return fragments.size
////            }
////
////        }

    }
}
