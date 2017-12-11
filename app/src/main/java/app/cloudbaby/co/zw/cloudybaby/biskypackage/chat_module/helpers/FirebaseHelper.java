package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.cloudmessaging.FCMNotificationBuilder;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Constants;

/**
 * @author Kudzai Chasinda
 */

public class FirebaseHelper {

    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //Root Ref to our db


    public FirebaseHelper(Context context) {
        this.context = context;
    }

    public static FirebaseHelper newInstance(Context context) {
        return new FirebaseHelper(context);
    }

    public void addUserToDatabase(Context context, UserModel userModel) {
        //We need firebase User
        UserModel user = userModel;
        databaseReference.child(Constants.CHAT_USER_BADGE)
                .child(user.getUserId())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //We have successfully added a user to DB
                            Log.e("FIREBASE", "Success");
                        } else {
                            //Failed to Add a user
                            Log.e("FIREBASE", "Failure  " + task.getException().toString());

                        }
                    }
                });
    }

    public void sendPushNotificationToReceiver(String senderName,
                                               String message,
                                               String senderUID,
                                               String firebaseToken,
                                               String receiverFirebaseToken,String chatId) {

        Toast.makeText(context, "Send Push Notif", Toast.LENGTH_LONG).show();

        FCMNotificationBuilder.initialize()
                .title(senderName)
                .message(message)
                .username(senderName)
                .uid(senderUID)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .chatId(chatId)
                .send();
    }
}
