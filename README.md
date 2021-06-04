# YmChatBot-Android- Demo App
This demo app demonstrates how chatbot can be integrated in a native android app using gradle
The chatbot SDK can be found at https://github.com/yellowmessenger/YMChatbot-Android

- [Installation](#installation)
- [Usage](#usage)

# Installation
## Gradle
To integrate YMChat into your Android project using gradle, specify the following configurations in App level `build.gradle` file
```gradle
repositories {
    jcenter()
    // Add these two lines 
    maven { url "https://jitpack.io" }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

...
...
...

dependencies {
    ...
    ...
	   implementation 'com.github.yellowmessenger:YMChatbot-Android:v1.3.1
}
```
  
# Usage
## Basic
Import the YMChat library in your Activity.
```java
import com.yellowmessenger.ymchat.YMChat;
import com.yellowmessenger.ymchat.YMConfig;
```

After the library is imported the basic bot can be presented with few lines as below 

Example `onCreate` method of the Activity:
```kotlin

 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChatBot()
    }

 private fun initChatBot() {
        val botId = "x1587041004122"
        //Get YMChat instance
        val ymChat: YMChat = YMChat.getInstance()
        ymChat.config = YMConfig(botId)
        //Payload attributes
        val payloadData = HashMap<String, Any>()
        //Setting Payload Data
        payloadData["some-key"] = "some-value"
        ymChat.config.payload = payloadData
        ymChat.config.enableHistory = true

        //setting event listener
        ymChat.onEventFromBot { botEvent: YMBotEventResponse ->
            when (botEvent.code) {
                "event-name" -> {
                }
            }
        }
        
        ymChat.onBotClose { Log.d("Example App", "Bot Was closed") }


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

```

## YMConfig
YMConfig configures chatbot before it presented on the screen. It is recommended to set appropriate config before presenting the bot

### Initialize
YMConfig requires a botID to initialize. All other settings can be changed after config has been initialised
```kotlin
ymChat.config = YMConfig("<BOT-ID>");
```

### Speech recognition
Speech to text can be enabled by setting the enableSpeech flag present in config. Default value is `false`
```kotlin
ymChat.config.enableSpeech = true
```

### Payload
Additional payload can be added in the form of key value pair, which is then passed to the bot. The value of payload can be either Primitive type or json convertible value
```kotlin
 //Payload attributes
  val payloadData = HashMap<String, Any>()
 //Setting Payload Data
  payloadData["some-key"] = "some-value"
  ymChat.config.payload = payloadData
```

### History
Chat history can be enabled by setting the `enableHistory` flag present in YMConfig. Default value is `false`
```kotlin
ymChat.config.enableHistory = true
```

## Starting the bot
Chat bot can be presented by calling `startChatbot()` and passing your Activity context as an argument
```kotlin
ymChat.startChatbot(this)
```


## Close bot
Bot can be programatically closed using `closeBot()` function
```kotlin
ymChat.closeBot()
```

## Bot close event
Bot close event is separetly sent and it can be handled by listening to onBotClose event as mentioned below.

```kotlin
ymChat.onBotClose { 
            Log.d("Example App", "Bot Was closed") 
        }
```

## Events from bot
Events from bot can be handled using event Listeners.  Simply define the `onSuccess` method of `onEventFromBot` listener.

```kotlin
ymChat.onEventFromBot { botEvent: YMBotEventResponse ->
            when (botEvent.code) {
                "event-name" -> {
                }
            }
        }
```
## Custom URL configuration (for on premise deployments)
Base url for the bot can be customized by setting `config.customBaseUrl` parameter. Use the same url used for on-prem deployment.

```kotlin
ymChat.config.customBaseUrl = "<CUSTOM-BASE-URL>";
```


## Important
If facing problem in release build, add the following configuration in the app's proguard-rules.pro file.
```kotlin
-keep public class com.example.ymwebview.** {
   *;
}
```


