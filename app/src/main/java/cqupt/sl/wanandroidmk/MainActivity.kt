package cqupt.sl.wanandroidmk

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout : androidx.appcompat.widget.Toolbar
    private val tabLayoutHeight:Float by lazy {
        tabLayout.height.toFloat()
    }
    private val tabLayoutY:Float by lazy {
        tabLayout.y
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        //设置状态栏字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        viewPager = findViewById(R.id.fragment)
        tabLayout = tab
        viewPager.adapter = ViewPagerAdapter(this,1)
    }

    /**
     * @param offsetY 正负代表上下移动tab，数值代表移动距离
     */
    fun moveTabLayout(offsetY:Float) {
        val newY = tabLayout.y
        val bottomLine = tabLayoutY + tabLayoutHeight
        if (offsetY < 0 && newY in tabLayoutY..bottomLine) {
            tabLayout.y = when {
                tabLayout.y - offsetY > bottomLine -> bottomLine
                else -> tabLayout.y - offsetY
            }
        } else if (offsetY > 0 && newY in tabLayoutY..bottomLine) {
            tabLayout.y = when {
                tabLayout.y - offsetY < tabLayoutY -> tabLayoutY
                else -> tabLayout.y - offsetY
            }
        }
    }
}
