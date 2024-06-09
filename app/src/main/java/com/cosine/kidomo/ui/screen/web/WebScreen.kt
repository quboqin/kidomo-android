package com.cosine.kidomo.ui.screen.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect

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
fun WebScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    onBackButtonPressed: () -> Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val density = LocalDensity.current

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the camera permission
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                settings.javaScriptEnabled = true
                addJavascriptInterface(
                    WebAppInterface(
                        context,
                        this,
                        mainViewModel,
                        onBackButtonPressed,
                        showDialogCallback = { show ->
                            showDialog = show
                        }), "Android"
                )
                loadUrl("file:///android_asset/index.html")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { getStatusBarHeight(context).toDp() })
    )

    if (showDialog) {
        ActionSheet(onDismiss = { showDialog = false }, takePhoto = {
            println("take photo")
            // Permission Request Logic
        })
    }
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

@Composable
fun ActionSheet(onDismiss: () -> Unit, takePhoto: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp
        ) {
            Column {
                Text(
                    text = "Action 1",
                    modifier = Modifier.padding(16.dp).clickable { takePhoto() }
                )
                Divider()
                Text(
                    text = "Action 2",
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onDismiss() }
                )
            }
        }
    }
}

private class WebAppInterface(
    val context: Context,
    val webView: WebView,
    val viewModel: MainViewModel,
    val onBackButtonPressed: () -> Boolean,
    val showDialogCallback: (Boolean) -> Unit
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
    fun showActionSheet(show: Boolean) {
        showDialogCallback(show)
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