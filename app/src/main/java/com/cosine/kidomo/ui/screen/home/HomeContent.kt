package com.cosine.kidomo.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cosine.kidomo.util.AppHolder

@Composable
fun HomeContent(
    gotoWebView: (isLocal: Boolean) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = {
                    gotoWebView(AppHolder.IS_LOCAL_URI)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Goto Local Web Page")
            }
            Button(
                onClick = {
                    gotoWebView(false)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Goto Remote Web Page")
            }
        }
    }

}
