package cqupt.sl.wanandroidmk.web

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import cqupt.sl.wanandroidmk.R

class FragmentWeb(context:Context) : Fragment() {
    lateinit var webView :WebView
    lateinit var title : TextView
    lateinit var back : ImageView
    lateinit var collect : ImageView
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web,container,false)
        init(view)
        return view
    }

    private fun init(view:View){
        title = view.findViewById(R.id.web_title)
        back = view.findViewById(R.id.web_back)
        collect = view.findViewById(R.id.web_collect)
        webView = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.web_progress)

    }
}