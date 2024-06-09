package com.cosine.kidomo.ui.screen.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.cosine.kidomo.util.showToast

@Composable
fun HomeScreen(
    gotoWebView: (taskId: Int) -> Unit,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current

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