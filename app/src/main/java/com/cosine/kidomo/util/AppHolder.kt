package com.cosine.kidomo.util

import android.app.Application

object AppHolder {
    const val SPLASH_DELAY = 3000L

    const val SPLASH_SCREEN = "splash"
    const val HOME_SCREEN = "home"
    const val WEBVIEW_BASE = "web view/"
    const val WEBVIEW_ARG_KEY = "webId"
    const val WEBVIEW_SCREEN = "${WEBVIEW_BASE}{${WEBVIEW_ARG_KEY}}"

    lateinit var appContext: Application
}