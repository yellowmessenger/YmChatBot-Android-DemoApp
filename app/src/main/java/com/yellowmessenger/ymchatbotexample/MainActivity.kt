package com.yellowmessenger.ymchatbotexample

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yellowmessenger.ymchat.YMChat
import com.yellowmessenger.ymchat.YMConfig
import com.yellowmessenger.ymchat.models.YMBotEventResponse
import com.yellowmessenger.ymchat.models.YellowCallback

class MainActivity : AppCompatActivity() {

    private var TAG: String = "YMLog"
    private var botId = "x1645602443989"
    private var deviceToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showExtraNotificationData()
        initListener()
    }

    private fun initListener() {
        val start = findViewById<Button>(R.id.startbtn)
        start.setOnClickListener {
            startChatBot()
        }

        val removeToken = findViewById<Button>(R.id.button)
        removeToken.setOnClickListener {
            //Starting the bot activity
            try {
                unlinkDevice()
            } catch (e: Exception) {
                //Catch and handle the exception
                e.printStackTrace()
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            deviceToken = token.toString()
        })
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
            val botId = data["botId"]?.toString()
            val ymAuthenticationToken = data["ymAuthenticationToken"]?.toString()
            showNotificationAlert(botId,ymAuthenticationToken)
        }
    }

    private fun showNotificationAlert(botId: String?, ymAuthenticationToken: String?) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this);
        alertDialog.setTitle("Notification")
        alertDialog.setMessage("Notification received for the Bot id : $botId and ymAuthenticationToken : $ymAuthenticationToken")
        alertDialog.setPositiveButton("Ok") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialog.show()

    }

    private fun startChatBot() {

        //Get YMChat instance
        val ymChat: YMChat = YMChat.getInstance()
        ymChat.config = YMConfig(botId)

        //Payload attributes
        val payloadData = HashMap<String, Any>()
        //Setting Payload Data
        payloadData["some-key"] = "some-value"
        ymChat.config.payload = payloadData

        // To Change the color of status bar, by default it will pick app theme
        ymChat.config.statusBarColor = R.color.teal_700

        // To Change the color of close button, default color is white
        ymChat.config.closeButtonColor = R.color.white

        // Start
        // For fetching the history-
        // For bot on app.yellow.ai, make sure Configuration -> Channels -> Chat Widget -> General -> Reset Context for every load is "Not checked"
        // For Bot on cloud.yellow.ai, make sure Channels -> Web -> Setting -> Other Settings -< Show History, option is enabled

        //Please pass unique and secure ymAuthenticationToken, this is used to identify user
        ymChat.config.ymAuthenticationToken = "11341adse3werwerw"

        // End

        /**
         *  To enable Speach to text feature please uncomment below line
         */
        // ymChat.config.enableSpeech = true

        /**
         *  To use v2 widget for bot please uncomment below line
         */
         ymChat.config.version = 2

        /**
         * If your bot is deployed on On-premise or in specific region please set the url in `customBaseUrl`
         */
        //ymChat.config.customBaseUrl = "Https:/on.prem.url"

        /**
         * To use custom image as chat bot loader please set following parameter
         * Note- Image/Gif should be deployed on public url and should be light weight for better performance
         * */
        //ymChat.config.customLoaderUrl = "https://example.com/test.svg"

        /**
         * Setting Event Listener
         * You will receive all the events here
         * botEvent.code can be used to identify the event
         * botEvent.data will contain the data in String format
         *
         */
        ymChat.onEventFromBot { botEvent: YMBotEventResponse ->
            when (botEvent.code) {
                "event-name" -> {
                }
            }
        }

        //Bot close event
        ymChat.onBotClose { Log.d("Example App", "Bot Was closed") }

        if (!TextUtils.isEmpty(deviceToken)) {
            ymChat.config.deviceToken = deviceToken
        }

        if (isInternetAvailable()) {
            //Starting the bot activity
            try {
                ymChat.startChatbot(this)
            } catch (e: Exception) {
                //Catch and handle the exception
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Network issue, Please check your network connection", Toast.LENGTH_LONG).show()
        }

    }

    private fun unlinkDevice() {
        try {
            val ymChat = YMChat.getInstance()
            //Get this from Account setting section of app.yellow.ai or My Profile Section of cloud.yellow.ai
            val apiKey = "your api key"
            ymChat.unlinkDeviceToken(botId, apiKey, deviceToken, object : YellowCallback {
                override fun success() {
                    Toast.makeText(this@MainActivity, "Token unlinked", Toast.LENGTH_SHORT).show()
                }

                override fun failure(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: java.lang.Exception) {
            //Catch and handle the exception
            e.printStackTrace()
        }
    }

    private fun isInternetAvailable(): Boolean {
        applicationContext?.let {
            val connectivityManager =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }
        return false
    }
}