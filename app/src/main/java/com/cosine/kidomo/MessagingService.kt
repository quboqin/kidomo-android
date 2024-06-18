package com.cosine.kidomo

import android.util.Log
import com.cosine.kidomo.ui.viewmodels.Header
import com.cosine.kidomo.ui.viewmodels.PreferenceHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MessagingService : FirebaseMessagingService() {
    private var TAG = "MessagingService"
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)

        val headerPreferenceHelper =
            PreferenceHelper<Header>(this, "KidomoPreferences", "HeaderKey")
        val retrievedHeader: Header? = headerPreferenceHelper.getObject(Header::class.java)

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://saas-test.opsfast.com/api/blade-common/firebase-token/update-token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Blade-Auth", retrievedHeader?.BladeAuth ?: "")
            .header(
                "Authorization",
                retrievedHeader?.Authorization ?: "Basic cmlkZXI6c2Fhc19wcm9kX3NlY3JldA=="
            )
            .post(
                okhttp3.FormBody.Builder()
                    .add("platform", "android")
                    .add("token", token)
                    .build()
            )
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // Handle the error
                } else {
                    // Handle the response
                }
            }
        })
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "Notification Message Body: " + message.notification!!.body)
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message Data payload: " + message.data)
        }
    }

}