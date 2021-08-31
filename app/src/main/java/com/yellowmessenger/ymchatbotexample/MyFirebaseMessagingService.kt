package com.yellowmessenger.ymchatbotexample

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    final var TAG: String = "YMLog"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG + " Remote message", remoteMessage.toString())
        Log.i(TAG + " Remote message", remoteMessage.data.toString())
        super.onMessageReceived(remoteMessage)
    }
}