package app.cloudbaby.co.zw.cloudybaby.biskypackage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * @author Kudzai Chasinda
 *         For shared preferences
 */

public class PreferencesUtils {

    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String IS_FIRST_TIME = "is_first_time";
    public static final String PREFS_PASSWORD = "prefs_password";
    public static final String PREFS_REGNUM = "prefs_regnum";
    public static final String PREFS_PROFILE_IMAGE_LOCATION = "prefs_profile_img_location";
    public static final String PREFS_USER_PHONE_NUMBER = "user_phone_number";
    public static final String PREFS_USERID = "user_id";
    public static final String PREFS_FIRST_NAME = "first_name";
    public static final String PREFS_LAST_NAME = "last_name";
    public static final String PREFS_USER_EMAIL = "user_email";
    public static final String PREFS_USER_NATID = "user_national_id";
    public static final String PREFS_IS_USERINAPP = "user_is_in_app";
    public static final String PREFS_FIREBASE_TOKEN = "firebase_token";


    //TODO fix greyed out preference strings

    private static PreferencesUtils mInstance;

    private static SharedPreferences sharedPreferences;

    public PreferencesUtils(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Alternatively we could use this *sharedPreferences = context.getSharedPreferences()* ?? Developer choice.
    }

    public static final PreferencesUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferencesUtils(context.getApplicationContext());
        }
        return mInstance;
    }

    private void setLogin(final String key, final boolean value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setFirstName(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setLastName(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setFirebaseToken(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setUserId(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setUserPhoneNumber(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setUserEmail(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setNationalID(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }


    private void setRegNum(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setPassword(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            //Write to our shared prefs in the background for
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    private void setProfileImage(final String key, final String value) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
                return null;
            }
        }.execute();
    }

    public void setProfileImageLocationPrefs(String value) {
        setProfileImage(PREFS_PROFILE_IMAGE_LOCATION, value);
    }

    public void setPrefsUserid(String value) {
        setUserId(PREFS_USERID, value);
    }

    public void setPrefsFirstName(String value) {
        setFirstName(PREFS_FIRST_NAME, value);
    }

    public void setPrefsLastName(String value) {
        setLastName(PREFS_LAST_NAME, value);
    }

    public void setPrefsUserEmail(String value) {
        setUserEmail(PREFS_USER_EMAIL, value);
    }

    public void setPrefsUserPhoneNumber(String value) {
        setUserPhoneNumber(PREFS_USER_PHONE_NUMBER, value);
    }

    public void setPrefsUserNatid(String value) {
        setNationalID(PREFS_USER_NATID, value);
    }

    public final String getUserLastName() {
        return sharedPreferences.getString(PREFS_LAST_NAME, "");
    }

    public final String getUserFirstName() {
        return sharedPreferences.getString(PREFS_FIRST_NAME, "");
    }

    public final String getUserEmail() {
        return sharedPreferences.getString(PREFS_USER_EMAIL, "");
    }

    public final String getUserNatId() {
        return sharedPreferences.getString(PREFS_USER_NATID, "");
    }

    public final String getUserPhoneNumber() {
        return sharedPreferences.getString(PREFS_USER_PHONE_NUMBER, "");
    }

    public final String getUserId() {
        return sharedPreferences.getString(PREFS_USERID, "");
    }

    public void setPasswordPrefs(String value) {
        setPassword(PREFS_PASSWORD, value);
    }

    public void setRegNumPref(String value) {
        setRegNum(PREFS_REGNUM, value);
    }

    //Sets our login state.
    public void setLoginState(boolean value) {
        setLogin(IS_LOGGED_IN, value);
    }

    public void setPrefsFirebaseToken(String value) {
        setFirebaseToken(PREFS_FIREBASE_TOKEN, value);
    }

    public final String getFirebaseToken() {
        return sharedPreferences.getString(PREFS_FIREBASE_TOKEN, "");
    }

    //We get the state from the user here
    /*This may not be the perfect model , we may need to perform a network check each time
    * editing the shared prefs value using tokens maybe*/
    public final boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public final String userPassword() {
        return sharedPreferences.getString(PREFS_PASSWORD, "");
    }

    public final String userRegNum() {
        return sharedPreferences.getString(PREFS_REGNUM, "");
    }

    public final String profileImageLocation() {
        return sharedPreferences.getString(PREFS_PROFILE_IMAGE_LOCATION, "");
    }
}
