# YmChatBot-Android- Demo App
    This demo app demonstrates how chatbot can be integrated in a native android app using gradle

## Chatbot SDK link
    The chatbot SDK can be found at https://github.com/yellowmessenger/YMChatbot-Android

## Documentation
    Detailed documentation can be found at https://docs.yellow.ai/docs/platform_concepts/channelConfiguration/android

## Setup push notifications
1. Clone this repository
2. Add your [google-service.json](https://firebase.google.com/docs/cloud-messaging/android/client#add_a_firebase_configuration_file) file in app folder.
3. Change application id in app level gradle file as per google-service.json.
4. Share FCM Server Key with Yellow.ai and asked them to map that against your Bot Id.
5. First Test by sending notification from FCM console.
6. Create a ticket and assign it to Support Agent. Once assigned, kill the app.
7. Now, when Agent will send a message you should receive a notification.
