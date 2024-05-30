package com.cosine.kidomo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class MessagingService: FirebaseMessagingService() {
    var TAG = "MesaggingService"
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "Notification Message Body: " + message.notification!!.body)
        if (message.data.size > 0) {
            Log.d(TAG, "Message Data payload: " + message.data)
        }
    }

}