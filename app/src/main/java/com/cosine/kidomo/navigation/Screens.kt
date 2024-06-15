package com.cosine.kidomo.navigation

import androidx.navigation.NavHostController
import com.cosine.kidomo.util.AppHolder
import com.cosine.kidomo.util.AppHolder.HOME_SCREEN
import com.cosine.kidomo.util.AppHolder.SPLASH_SCREEN
import com.cosine.kidomo.util.AppHolder.WEBVIEW_SCREEN
import com.cosine.kidomo.util.AppHolder.SCANNER_SCREEN

class Screens(navController: NavHostController) {
    val gotoHomeFromSplash: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }

    val gotoHomeScreen: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(HOME_SCREEN) { inclusive = true }
        }
    }

    val gotoScannerScreen: () ->Unit = {
        navController.navigate(route = SCANNER_SCREEN) {
            popUpTo(SCANNER_SCREEN) { inclusive = true }
        }
    }

    val onBackButtonPressed: () -> Unit = {
        navController.previousBackStackEntry?.savedStateHandle?.set("resultKey", true)
        navController.popBackStack()
    }

    val gotoWebView: (Boolean) -> Unit = { isLocalUri ->
        navController.navigate(route = "${AppHolder.WEBVIEW_BASE}$isLocalUri") {
            popUpTo(WEBVIEW_SCREEN) { inclusive = true }
        }
    }
}