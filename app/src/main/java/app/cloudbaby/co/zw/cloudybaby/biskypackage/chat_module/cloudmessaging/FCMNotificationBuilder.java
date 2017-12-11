package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.cloudmessaging;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Kudzai Chasinda
 */

public class FCMNotificationBuilder {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FCMNotificationBuilder";
    private static final String SERVER_API_KEY = "AAAAnsP0f2Q:APA91bGy7r4i-KD8x4Y4QhLF7aq7MBkdAZh2aOnGqO-pqekWeDFTfX4NlGbBuCJ4a5_lD825VWRKUfwiZwKK0nGxgP-HqQg9k94o8YQf_vEjubWsXqAIA_sf7YRlC1E26sWM4PTlxu3J";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATA = "data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_UID = "uid";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_RECEIVER = "receiver_token";
    private static final String CHAT_ID = "chat_id";

    private String mTitle;
    private String mMessage;
    private String mUsername;
    private String mUid;
    private String mFirebaseToken;
    private String mReceiverFirebaseToken;
    private String mChatId;

    private FCMNotificationBuilder() {

    }

    public static FCMNotificationBuilder initialize() {
        return new FCMNotificationBuilder();
    }

    public FCMNotificationBuilder title(String title) { //sendername
        mTitle = title;
        return this;
    }

    public FCMNotificationBuilder message(String message) { //msg txt
        mMessage = message;
        return this;
    }

    public FCMNotificationBuilder username(String username) { //sendername
        mUsername = username;
        return this;
    }

    public FCMNotificationBuilder uid(String uid) { //sender id
        mUid = uid;
        return this;
    }

    public FCMNotificationBuilder firebaseToken(String firebaseToken) { //firebasetoken
        mFirebaseToken = firebaseToken;
        return this;
    }

    public FCMNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken; //receiver token not necessary
        return this;
    }

    public FCMNotificationBuilder chatId(String chatId) {
        mChatId = chatId;
        return this;
    }

    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_TITLE, mTitle);
        jsonObjectData.put(KEY_TEXT, mMessage);
        jsonObjectData.put(KEY_USERNAME, mUsername);
        jsonObjectData.put(CHAT_ID, mChatId);
        jsonObjectData.put(KEY_UID, mUid);
        jsonObjectData.put(KEY_RECEIVER, mReceiverFirebaseToken);
        jsonObjectData.put(KEY_FCM_TOKEN, mFirebaseToken);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);

        return jsonObjectBody;
    }
}
