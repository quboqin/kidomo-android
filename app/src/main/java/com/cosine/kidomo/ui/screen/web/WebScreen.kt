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
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cosine.kidomo.ui.screen.scan.EMPTY_IMAGE_URI
import com.cosine.kidomo.ui.viewmodels.Header
import com.cosine.kidomo.ui.viewmodels.PreferenceHelper
import org.json.JSONObject
import com.cosine.kidomo.util.encodeImageUriToBase64
import org.json.JSONException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WebScreen(
    uriString: String,
    onBackButtonPressed: () -> Unit,
    gotoScannerScreen: () -> Unit,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    Scaffold(
        content = { _ ->
            WebView(
                mainViewModel,
                onBackButtonPressed,
                gotoScannerScreen,
                uriString,
                navController
            )
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    mainViewModel: MainViewModel,
    onBackButtonPressed: () -> Unit,
    gotoScannerScreen: () -> Unit,
    uriString: String,
    navController: NavHostController
) {
    val imageUri by mainViewModel.imageUri
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val density = LocalDensity.current
    val webViewState = remember { mutableStateOf<WebView?>(null) }
    val isWebViewLoaded = remember { mutableStateOf(false) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val result = savedStateHandle?.getLiveData<Boolean>("resultKey")?.observeAsState()

    AndroidView(
        factory = { context ->
            isWebViewLoaded.value = false
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isWebViewLoaded.value = true
                    }
                }
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
                loadUrl(uriString)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { getStatusBarHeight(context).toDp() })
    ) { webView ->
        webViewState.value = webView
    }

    LaunchedEffect(webViewState.value, result?.value, isWebViewLoaded.value) {
        webViewState.value?.let { webView ->
            isWebViewLoaded.value.let { loaded ->
                result?.value?.let { event ->
                    if (event && imageUri != EMPTY_IMAGE_URI) {
                        // Clear the result after handling it
                        savedStateHandle["resultKey"] = false

                        val imageString = encodeImageUriToBase64(context, imageUri)
                        val jsonObject = JSONObject()
                        jsonObject.put("taskId", mainViewModel.taskId.value)
                        jsonObject.put("image", imageString)
                        val jsonString = jsonObject.toString()

                        webView.post {
                            Log.w("WebScreen", "webView call nativeImageData with $jsonObject")
                            webView.evaluateJavascript(
                                "javascript:nativeImageData($jsonString);",
                                null
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ActionSheet(onDismiss = { showDialog = false }, takePhoto = {
            println("take photo")
            gotoScannerScreen()
        }, imageUri = imageUri)
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
fun ActionSheet(onDismiss: () -> Unit, takePhoto: () -> Unit, imageUri: Uri) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp
        ) {
            Column {
                Text(
                    text = "take photo",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { takePhoto() }
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
    val onBackButtonPressed: () -> Unit,
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

        val action = jsonObject["action"] as? String


        var jsonString = ""

        when (action) {
            "back" -> {
                val callback = jsonObject["callback"] as? String
                jsonString = viewModel.nativeTask(jsonObject, onBackButtonPressed)
                // Call the JavaScript callback function with the response
                webView.post {
                    webView.evaluateJavascript("javascript:$callback($jsonString);", null)
                }
            }

            "set_header" -> {
                try {
                    val authObject = jsonObject["auth"] as? Map<*, *>

                    val bladeAuth = authObject?.get("BladeAuth") as String
                    val authorization = authObject["Authorization"] as String

                    val headerPreferenceHelper =
                        PreferenceHelper<Header>(context, "KidomoPreferences", "HeaderKey")

                    val header = Header(bladeAuth, authorization)

                    headerPreferenceHelper.saveObject(header)

                    val retrievedHeader: Header? = headerPreferenceHelper.getObject(Header::class.java)
                    retrievedHeader?.let {
                        Log.d(
                            "WebAppInterface",
                            "BladeAuth: ${it.BladeAuth}, Authorization: ${it.Authorization}"
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            "camera" -> {
                val data = jsonObject["data"] as? Map<*, *> ?: throw IllegalArgumentException("Data is not a map")
                val newTaskId = data["taskId"] as? Double ?: throw IllegalArgumentException("Task ID is not found or not a Double")
                viewModel.updateTaskId(newTaskId.toInt())
                showDialogCallback(true)
            }

            else -> {
                return
            }
        }


    }
}
