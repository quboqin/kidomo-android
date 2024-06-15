package com.cosine.kidomo.util

object AppHolder {
    const val SPLASH_DELAY = 3000L

    const val SPLASH_SCREEN = "splash"
    const val HOME_SCREEN = "home"
    const val SCANNER_SCREEN = "scanner"
    const val WEBVIEW_BASE = "web view/"
    const val WEBVIEW_ARG_KEY = "isLocalUri"
    const val WEBVIEW_SCREEN = "${WEBVIEW_BASE}{${WEBVIEW_ARG_KEY}}"

    const val IS_LOCAL_URI = true
    const val LOCAL_URI = "file:///android_asset/index.html"
    const val REMOTE_URI = "https://m-saas.opsfast.com"
}