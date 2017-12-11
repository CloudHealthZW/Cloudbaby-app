package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.cloudmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.HomeNav;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.ChatConversation;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.TipsModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.MyNotificationManager;
import io.realm.Realm;


/**
 * @author Kudzai Chasinda
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.e("received", "Data Payload: " + remoteMessage.getData().toString());

            if (remoteMessage.getData().containsKey("image")) {
                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    sendPushNotification(json);
                } catch (Exception e) {
                    Log.e("ERROR", "Exception: " + e.getMessage());
                }
            } else {
                // Check if message contains a data payload.
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("text");
                String username = remoteMessage.getData().get("username");
                String uid = remoteMessage.getData().get("uid");
                String receiverToken = remoteMessage.getData().get("receiver_token");
                String chatId = remoteMessage.getData().get("chat_id");
                String fcmToken = remoteMessage.getData().get("fcm_token");

                sendNotification(title,
                        message,
                        username,
                        uid,
                        receiverToken, chatId);
                // Don't show notification if chat activity is open.
//                    if (!MyApplication.isChatActivityOpen()) {
//
//                    } else {
//                        //Do something with event e.g Observe in a subscriber
//                        EventBus.getDefault().post(new PushNotificationEvent(title,
//                                message,
//                                username,
//                                uid,
//                                fcmToken));
//                    }

            }

        }
/*


        */
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //save to realm
            TipsModel tipsModel = new TipsModel(message, title, System.currentTimeMillis());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(tipsModel);
            realm.commitTransaction();

            Log.e("REALM", "SAVED");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), HomeNav.class);

            //if there is no image
            if (imageUrl.equals("null")) {
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            } else {
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
            //    saveNotification(title, message, System.currentTimeMillis());
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken, String chatId) {

        /*
        *     intent.putExtra("chat_id", "" + userModel.getChat_id());
            intent.putExtra("reciverUserName", "" + userModel.getDisplayName());
            intent.putExtra("reciverProfilePic", "" + userModel.getProfilePic());
            intent.putExtra("reciverUid", "" + userModel.getUser_id());
            intent.putExtra("receiverToken", userModel.getFirebaseToken());
        * */

        //TODO check back
        Intent intent = new Intent(this, ChatConversation.class);

        intent.putExtra("chat_id", chatId);
        intent.putExtra("reciverProfilePic", "");
        intent.putExtra("reciverUserName", receiver);
        intent.putExtra("reciverUid", receiverUid);
        intent.putExtra("receiverToken", firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
