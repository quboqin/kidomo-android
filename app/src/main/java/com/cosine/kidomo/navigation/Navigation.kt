package com.cosine.kidomo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cosine.kidomo.ui.screen.home.HomeScreen
import com.cosine.kidomo.ui.screen.splash.Splash
import com.cosine.kidomo.ui.screen.web.WebScreen
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.cosine.kidomo.util.AppHolder
import com.cosine.kidomo.util.AppHolder.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {

        composable(
            route = SPLASH_SCREEN,
        ) {
            Splash(gotoHomeScreen = screen.gotoHomeFromSplash)
        }

        composable(
            route = AppHolder.HOME_SCREEN
        ) {
            HomeScreen(
                gotoWebView = screen.gotoWebView,
                mainViewModel = mainViewModel
            )
        }

        composable(
            route = AppHolder.WEBVIEW_SCREEN,
            arguments = listOf(navArgument(AppHolder.WEBVIEW_ARG_KEY) {
                type = NavType.IntType
            })
        ) {
            WebScreen()
        }
    }
}