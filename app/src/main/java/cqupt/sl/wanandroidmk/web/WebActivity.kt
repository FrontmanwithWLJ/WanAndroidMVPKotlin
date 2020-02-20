package cqupt.sl.wanandroidmk.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cqupt.sl.wanandroidmk.R
import kotlinx.android.synthetic.main.activity_web.*
import java.lang.Exception

class WebActivity : AppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var url: String? = ""
    private var collected = false

    companion object {
        fun goToWeb(context: Context, url: String, collected: Boolean) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("collected", collected)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        web_refresh.setColorSchemeColors(Color.GRAY, Color.BLACK)
        web_progress.hide()
        web_collect.setOnClickListener(this)
        web_quit.setOnClickListener(this)
        web_back_parent.setOnClickListener(this)
        web_forward_parent.setOnClickListener(this)
        web_refresh.setOnRefreshListener(this)

        collect(collected)
        getInfFromIntent(intent)
        //允许JS
        web_webview.settings.javaScriptEnabled = true
        web_webview.webViewClient = MyWebViewClient()
        web_webview.webChromeClient = MyChromeClient()
        loadUrl(url)
    }

    private fun loadUrl(url: String?) {
        web_webview.loadUrl(url)
    }

    private fun getInfFromIntent(intent: Intent) {
        url = intent.getStringExtra("url")
        collected = intent.getBooleanExtra("collected", false)
    }

    private fun collect(collected: Boolean) {
        web_collect.isEnabled = !collected
    }

    //拦截返回按钮
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web_webview.canGoBack()) {
            web_webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        //恢复活跃状态
        web_webview.onResume()
        //与pauseTimers相对
        web_webview.resumeTimers()
        super.onResume()
    }

    override fun onPause() {
        //暂停所有动作
        web_webview.onPause()
        //暂停显示，解析，降低CPU功耗
        web_webview.pauseTimers()
        super.onPause()
    }

    override fun onDestroy() {
        //清理缓存，历史记录，表单信息
        web_webview.clearCache(true)
        web_webview.clearHistory()
        web_webview.clearFormData()
        //加载空数据
        web_webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        //从父容易移除webView
        (web_webview.parent as ViewGroup).removeView(web_webview)

        web_webview.destroy()
        super.onDestroy()
    }

    private inner class MyWebViewClient : WebViewClient() {
        @SuppressLint("NewApi")
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (isStartWithHttp(request!!.url.toString()))
                return false
            try {
                val intent = Intent(Intent.ACTION_VIEW, request.url)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(applicationContext,"并未安装此应用",Toast.LENGTH_SHORT).show()
                return true
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            web_progress.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            web_progress.hide()
            if (web_refresh.isRefreshing) {
                web_refresh.isRefreshing = false
            }
            web_forward.isActivated = !web_webview.canGoForward()
            web_forward_parent.isClickable = web_webview.canGoForward()
        }

        private fun isStartWithHttp(url:String):Boolean{
            Log.e("WEB",url)
            return url.substring(0,3) == "http"
        }
    }

    private inner class MyChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            web_title.text = title
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                web_progress.setProgress(newProgress, true)
            } else {
                web_progress.progress = newProgress
            }
            super.onProgressChanged(view, newProgress)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.web_quit -> {
                finish()
            }
            R.id.web_collect -> {
                collected = !collected
                collect(collected)
            }
            R.id.web_back_parent -> {
                if (web_webview.canGoBack())
                    web_webview.goBack()
                else
                    finish()
            }
            R.id.web_forward_parent -> {
                if (web_webview.canGoForward())
                    web_webview.goForward()
            }
        }
    }

    override fun onRefresh() {
        web_webview.reload()
    }
}
