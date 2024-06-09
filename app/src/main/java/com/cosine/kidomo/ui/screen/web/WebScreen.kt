package com.cosine.kidomo.ui.screen.web

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun WebScreen() {
    Scaffold {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(WebAppInterface(context), "Android")
                    loadUrl("file:///android_asset/index.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private class WebAppInterface(val context: Context) {
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun logMessage(message: String) {
        Log.d("WebAppInterface", message)
    }
}