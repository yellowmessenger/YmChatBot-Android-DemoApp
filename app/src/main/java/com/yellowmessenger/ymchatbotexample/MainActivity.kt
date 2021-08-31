package com.yellowmessenger.ymchatbotexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging
import com.yellowmessenger.ymchat.YMChat
import com.yellowmessenger.ymchat.YMConfig
import com.yellowmessenger.ymchat.models.YMBotEventResponse
import java.util.*

class MainActivity : AppCompatActivity() {

    private var TAG: String = "YMLog"
    private var botId = "x1608615889375"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val theme = this.theme
        showExtraNotificationData()
        initChatBot()
    }

    private fun getDataFromIntent(): MutableMap<String, Any?> {
        val data = mutableMapOf<String, Any?>()
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                data[key] = value
            }
            this.intent.putExtras(Bundle.EMPTY)
        }
        return data
    }

    private fun showExtraNotificationData() {
        if (intent.extras != null && intent.extras?.getString("botId") != null) {
            val data = getDataFromIntent()
            showNotificationAlert(intent.extras?.getString("botId").toString())
        }
    }

    private fun showNotificationAlert(botId: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this);
        alertDialog.setTitle("Notification")
        alertDialog.setMessage("Bot id : $botId")
        alertDialog.setPositiveButton("Ok") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        alertDialog.show()

    }

    private fun initChatBot() {

        //Get YMChat instance
        val ymChat: YMChat = YMChat.getInstance()
        ymChat.config = YMConfig(botId)
        //Payload attributes
        val payloadData = HashMap<String, Any>()
        //Setting Payload Data
        payloadData["some-key"] = "some-value"
        payloadData["UserId"] = "qweqwe123214qwee23424"
        ymChat.config.payload = payloadData
        ymChat.config.enableHistory = true

        // To Change the color of status bar, by default it will pick app theme
        ymChat.config.statusBarColor = R.color.teal_700
        // To Change the color of close button, default color is white
        ymChat.config.closeButtonColor = R.color.white

        //setting event listener
        ymChat.onEventFromBot { botEvent: YMBotEventResponse ->
            when (botEvent.code) {
                "event-name" -> {
                }
            }
        }
        ymChat.onBotClose { Log.d("Example App", "Bot Was closed") }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, token.toString())
            ymChat.config.deviceToken = token.toString()
        })


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
            //Starting the bot activity
            try {
                ymChat.startChatbot(this)
            } catch (e: Exception) {
                //Catch and handle the exception
                e.printStackTrace()
            }
        }
    }
}