package com.example.androidapkbuilder

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private lateinit var loader: ProgressBar

    private val channelList = arrayOf(
        arrayOf("T Sports Live", "https://freestreams-live1.tv/t-sports/"),
        arrayOf("Sony Ten 1", "https://freestreams-live1.tv/sony-ten-1/"),
        arrayOf("Fox Sports 1", "https://freestreams-live1.tv/fox-sports-1/"),
        arrayOf("beIN Sports 1", "https://freestreams-live1.tv/bein-sports-1/"),
        arrayOf("Willow Cricket", "https://freestreams-live1.tv/willow-cricket/"),
        arrayOf("Sky Sports HD", "https://freestreams-live1.tv/sky-sports-football/")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#121212"))
        }

        val header = TextView(this).apply {
            text = "RAFIM LIVE SPORTS"
            setTextColor(Color.WHITE)
            textSize = 20f
            setBackgroundColor(Color.parseColor("#E50914"))
            setPadding(35, 35, 35, 35)
        }
        rootLayout.addView(header)

        val playerFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 650)
            setBackgroundColor(Color.BLACK)
        }

        webView = WebView(this).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        loader = ProgressBar(this).apply {
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.CENTER
            layoutParams = params
        }

        playerFrame.addView(webView)
        playerFrame.addView(loader)
        rootLayout.addView(playerFrame)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                loader.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loader.visibility = View.GONE
                view?.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header')[0].style.display='none'; " +
                        "document.getElementsByTagName('footer')[0].style.display='none'; " +
                        "})()")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && (url.contains("freestreams-live1.tv") || url.contains("m3u8") || url.contains("cdn"))) {
                    return false
                }
                return true
            }
        }

        val gridView = GridView(this).apply {
            numColumns = 2
            horizontalSpacing = 15
            verticalSpacing = 15
            setPadding(20, 20, 20, 20)
        }

        gridView.adapter = object : BaseAdapter() {
            override fun getCount(): Int = channelList.size
            override fun getItem(position: Int): Any = channelList[position]
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val btn = TextView(this@MainActivity).apply {
                    text = channelList[position][0]
                    setTextColor(Color.WHITE)
                    textSize = 16f
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.parseColor("#222222"))
                    setPadding(20, 45, 20, 45)
                    setOnClickListener {
                        loader.visibility = View.VISIBLE
                        webView.loadUrl(channelList[position][1])
                    }
                }
                return btn
            }
        }

        rootLayout.addView(gridView)
        setContentView(rootLayout)

        webView.loadUrl(channelList[0][1])
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
