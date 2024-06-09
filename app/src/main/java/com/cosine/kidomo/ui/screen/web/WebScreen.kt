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
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun WebScreen(
    mainViewModel: MainViewModel
) {
    Scaffold {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(WebAppInterface(context, this, mainViewModel), "Android")
                    loadUrl("file:///android_asset/index.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private class WebAppInterface(val context: Context, val webView: WebView, val viewModel: MainViewModel) {
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun logMessage(message: String) {
        Log.d("WebAppInterface", message)
    }

    @JavascriptInterface
    fun callFromJavascript(jsonString: String) {
        val gson = Gson()
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val jsonObject: Map<String, Any> = gson.fromJson(jsonString, type)
        println(jsonObject)

        val callback = jsonObject["callback"] as? String
        val jsonString = viewModel.nativeTask(jsonObject)

        // Call the JavaScript callback function with the response
        webView.post {
            webView.evaluateJavascript("javascript:$callback($jsonString);", null)
        }
    }

}