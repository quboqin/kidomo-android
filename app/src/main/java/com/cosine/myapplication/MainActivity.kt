package com.cosine.myapplication

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.just.agentweb.AgentWeb
import androidx.compose.ui.tooling.preview.Preview
import com.cosine.myapplication.ui.theme.MyApplicationTheme
import android.webkit.JavascriptInterface
import android.widget.Toast

class MainActivity : ComponentActivity() {
    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AndroidView(factory = { context ->
                        // Create a container for the AgentWeb
                        val container = FrameLayout(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                        agentWeb = AgentWeb.with(this@MainActivity).setAgentWebParent(
                            container, ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        ).useDefaultIndicator().createAgentWeb().ready().go("file:///android_asset/index.html")

                        // Add JavaScript interface
                        agentWeb.jsInterfaceHolder.addJavaObject("android", AndroidInterface(this))

                        agentWeb.webCreator.webView
                        container
                    }, modifier = Modifier.fillMaxSize(), update = { webView ->
                        // You can interact with the WebView here if needed
                        (webView.parent as? ViewGroup)?.removeView(webView)
                    })
                }
            }
        }
    }

    private class AndroidInterface(val context: MainActivity) {
        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
    MyApplicationTheme {
        WebViewScreen()
    }
}