package app.cloudbaby.co.zw.cloudybaby.biskypackage.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.json.JSONObject;

/**
 * @author Kudzai Chasinda
 *         General Utilities Adhoc
 */

public class GeneralUtils {
    public static String USER_ID;

    public static boolean contains(JSONObject jsonObject, String key) {
        //Just checks the existence of a key during parsing
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    public static String getUniqueImageFilename() {
        return "IMG_" + System.currentTimeMillis() + ".jpg";
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static String formatPhoneNumber(String minPhone) {
        return "263" + minPhone;
    }

    public static String displayPhoneNumber(String formatPhoneNumber) {
        return "+" + formatPhoneNumber;
    }

    public static String prepPhoneNumber(String phoneNumber) {
        String newPhoneNumber = "";
        if (phoneNumber.contains("+")) {
            newPhoneNumber = phoneNumber.replace("+", "");
            return newPhoneNumber;
        } else {
            newPhoneNumber = phoneNumber;
            return phoneNumber;
        }
    }

    public static String sanitizePhoneNumber(String contact) {
        //Some string manipulation is required here
        if (contact.replaceAll("[\\s+()-]", "").startsWith("0")) {
            return contact.replaceAll("[\\s+()-]", "").replace("0", "263");
        } else {
            return contact.replaceAll("[\\s+()-]", "");
        }
    }

}
