package cqupt.sl.wanandroidmk.activity.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import cqupt.sl.wanandroidmk.R

import cqupt.sl.wanandroidmk.permission.EasyRequest
import cqupt.sl.wanandroidmk.res.Res
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.Multipart


class MainActivity : AppCompatActivity() {
    private val REQUEST_STORAGE = 2
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: androidx.appcompat.widget.Toolbar
    private val tabLayoutHeight: Float by lazy {
        tabLayout.height.toFloat()
    }
    private val tabLayoutY: Float by lazy {
        tabLayout.y
    }
    private var allowBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Thread {
            Res.BASE_PATH = filesDir
            Res.BASE_CACHE_PATH = cacheDir
        }.run()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyRequest.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyRequest.request(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    REQUEST_STORAGE
                )
            } else {
                init()
            }
        } else {
            init()
        }
    }

    private fun init() {
        //设置状态栏字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        viewPager = findViewById(R.id.fragment)
        tabLayout = tab
        viewPager.adapter = ViewPagerAdapter(this, 1)
    }

    /**
     * @param offsetY 正负代表上下移动tab，数值代表移动距离
     */
    fun moveTabLayout(offsetY: Float) {
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (allowBack){
                finish()
                return true
            }
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show()
            allowBack = true
            GlobalScope.launch {
                delay(1000)
                allowBack = false
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    init()
                } else {
                    Toast.makeText(this, "未授权,请手动授权", Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(this)
                        .setTitle("请求授权")
                        .setMessage("是否手动授权")
                        .setPositiveButton("是") { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton("否") { _, _ -> finish() }
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED){
            if (!EasyRequest.checkPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                init()
            }else{
                finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_null,R.anim.scale_to_min)
    }
}
