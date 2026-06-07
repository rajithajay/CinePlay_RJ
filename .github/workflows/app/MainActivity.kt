package com.example.cineplay

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState: Bundle?)
        
        webView = WebView(this)
        setContentView(webView)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.isFocusable = true
        webView.isFocusableInTouchMode = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false

                if (url.contains("imdb.com/title/")) {
                    val correctedUrl = url.replace("imdb.com", "playimdb.com")
                    view?.loadUrl(correctedUrl)
                    return true
                }

                if (url.contains("doubleclick") || url.contains("adservice") || url.contains("popads")) {
                    return true 
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view: WebView, url)
                val adBusterCode = """
                    javascript:(function() {
                        var style = document.createElement('style');
                        style.innerHTML = 'iframe, [id*="ad"], [class*="ad-"], .popunder, .popup { display: none !important; }';
                        document.head.appendChild(style);
                    })()
                """.trimIndent()
                view.loadUrl(adBusterCode)
            }
        }
        webView.loadUrl("https://www.google.com/search?q=site:imdb.com+")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
