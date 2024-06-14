package com.cosine.kidomo.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    gotoWebView: (isLocal: Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            HomeAppBar(
            )
        },
        content = {
            HomeContent(
                gotoWebView = gotoWebView
            )
        }
    )
}