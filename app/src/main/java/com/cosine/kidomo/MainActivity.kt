package com.cosine.kidomo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cosine.kidomo.ui.theme.KidomoTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import com.cosine.kidomo.home.HomeTopAppBar
import com.cosine.kidomo.util.showToast

class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var TAG = "MainActivity"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, token)
            Toast.makeText(this@MainActivity, "get a token", Toast.LENGTH_SHORT).show()
        })

        askNotificationPermission()
        
        setContent {
            KidomoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        HomeTopAppBar(
                            onDeleteAllConfirmed = {
                                showToast(this, "所有任务都已清空!")
                            }
                        )
                    }
                ) { _ ->
                    AndroidView(factory = { context ->
                        val mainView: View =layoutInflater.inflate(R.layout.main, null)
                        webView = mainView.findViewById<WebView>(R.id.webview)

                        // Enable JavaScript
                        webView.settings.javaScriptEnabled = true
                        // Set WebViewClient to handle loading URLs
                        webView.webViewClient = WebViewClient()
                        // Set WebChromeClient to handle JavaScript dialogs, titles, etc.
                        webView.webChromeClient = WebChromeClient()
                        // Add JavaScript interface
                        webView.addJavascriptInterface(WebAppInterface(this), "Android")
                        // Load a web page
                        webView.loadUrl("file:///android_asset/index.html")

                        val button = mainView.findViewById<Button>(R.id.button)
                        button.setOnClickListener {
                            callJavaScriptFunction()
                        }

                        mainView
                    }, modifier = Modifier.fillMaxSize(), update = { mainView ->
                        // You can interact with the WebView here if needed
                        (mainView.parent as? ViewGroup)?.removeView(mainView)
                    })
                }
            }
        }
    }

    private class WebAppInterface(val context: ComponentActivity) {
        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun logMessage(message: String) {
            Log.d("WebAppInterface", message)
        }
    }

    private fun callJavaScriptFunction() {
        val jsCode = "javascript:alert('Hello from Kotlin!')"
        webView.evaluateJavascript(jsCode) { result ->
            // Handle the result if needed
            println("JavaScript executed: $result")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Composable
fun WebViewScreen() {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KidomoTheme {
        WebViewScreen()
    }
}