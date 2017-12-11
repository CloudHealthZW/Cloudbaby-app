package app.cloudbaby.co.zw.cloudybaby.biskypackage.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bisky on 5/6/2017.
 * TODO check if the chat activity is open to maintain the relationship
 * move all the prefs to the utilties and refactor appropriately
 */

public class MyApplication extends Application {
    static MyApplication application;
    private static RequestQueue requestQueue;

    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        application.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);

        /*
            RealmResults<BlockedNumber> realmResults = realm.where(BlockedNumber.class).equalTo("friendPhoneNumber", friend.getPhoneNumber()).findAll();

           Realm realm = Realm.getDefaultInstance();
         realm.beginTransaction();
                        realm.copyToRealmOrUpdate(chatRealm);
                        realm.commitTransaction();
         */
    }

    public static MyApplication getInsatnce() {

        return application;
    }

    public static Context getContext() {

        return application.getApplicationContext();
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(getContext());
        }

  return requestQueue;
}

    public static String getParentId() {
        String DEFAULT = "N/A";
        SharedPreferences preferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        String username = preferences.getString("parentId", DEFAULT);
        return username;
    }


    public static boolean getLogin() {
        boolean DEFAULT = false;
        SharedPreferences preferences = getContext().getSharedPreferences("login", MODE_PRIVATE);
        boolean boolLog = preferences.getBoolean("success", DEFAULT);

        return boolLog;
    }

    public static String getName() {
        String DEFAULT = "N/A";
        SharedPreferences preferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        String boolLog = preferences.getString("names", DEFAULT);

        return boolLog;
    }

    public static String getPhone() {
        String DEFAULT = "N/A";
        SharedPreferences preferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        String boolLog = preferences.getString("phone", DEFAULT);

        return boolLog;
    }

    public static String getEmail() {
        String DEFAULT = "N/A";
        SharedPreferences preferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        String boolLog = preferences.getString("email", DEFAULT);

        return boolLog;
    }


    public static void clearShared(Context context) {

        SharedPreferences preferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().clear().apply();
        SharedPreferences preferencesLogin = getContext().getSharedPreferences("login", MODE_PRIVATE);
        preferencesLogin.edit().clear().apply();

    }
}
