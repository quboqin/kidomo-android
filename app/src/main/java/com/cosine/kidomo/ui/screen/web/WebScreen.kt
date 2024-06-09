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
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun WebScreen(
    onBackButtonPressed: () -> Boolean,
    mainViewModel: MainViewModel
) {
    Scaffold(
        content = { innerPadding ->
            WebScreen(Modifier.padding(innerPadding), mainViewModel, onBackButtonPressed)
        }
    )
}

@Composable
fun WebScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel, onBackButtonPressed: ()-> Boolean) {
    val context = LocalContext.current
    val density = LocalDensity.current

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                settings.javaScriptEnabled = true
                addJavascriptInterface(WebAppInterface(context, this, mainViewModel, onBackButtonPressed), "Android")
                loadUrl("file:///android_asset/index.html")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { getStatusBarHeight(context).toDp() })
    )
}

@SuppressLint("InternalInsetResource")
fun getStatusBarHeight(context: android.content.Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

private class WebAppInterface(
    val context: Context,
    val webView: WebView,
    val viewModel: MainViewModel,
    val onBackButtonPressed: () -> Boolean
) {
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
        val jsonString = viewModel.nativeTask(jsonObject, onBackButtonPressed)

        // Call the JavaScript callback function with the response
        webView.post {
            webView.evaluateJavascript("javascript:$callback($jsonString);", null)
        }
    }

}