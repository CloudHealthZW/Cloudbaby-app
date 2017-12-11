package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Prefs;

/**
 * @author Kudzai Chasinda
 *         Login activity
 *         TODO rename to something more proper
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLoginFB;

    private CustomLoaderDialog customLoaderDialog;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String TAG = "LoginActivity";
    private FirebaseDatabase database;
    private boolean isUserExist = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initObjects();
        initListners();
    }

    private void initObjects() {
        database = FirebaseDatabase.getInstance();
        imgLoginFB = (ImageView) findViewById(R.id.imgLoginFB);

        mAuth = FirebaseAuth.getInstance();

    }

    private void initListners() {
        final boolean[] chatListCalled = {false};
        imgLoginFB.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && isUserExist && !chatListCalled[0]) {
                    // User is signed in
                    chatListCalled[0] = true;
                    Intent intent = new Intent(LoginActivity.this, ChatConversationList.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == imgLoginFB) {
            isUserExist = false;
            loginWithFacebook();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void loginWithFacebook() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken() {

    }

    private void checkForUserAndSignuUp(final FirebaseUser currentUser) {

        final DatabaseReference firebase = database.getReference().child("users").child(currentUser.getUid());
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                customLoaderDialog.hide();
                // firebase.removeEventListener(this);
                if (snapshot.getValue() != null) {
                    /*GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> hashMap = snapshot.getValue(genericTypeIndicator );

*/
                    try {
                        Map<String, String> hashMap = snapshot.getValue(HashMap.class);
                        Prefs.setUserId(LoginActivity.this, currentUser.getUid());
                        Prefs.setUSERNAME(LoginActivity.this, getValuesWithValid(hashMap, "displayName"));
                        Prefs.setEMAIL(LoginActivity.this, getValuesWithValid(hashMap, "email"));
                        Prefs.setPhotoUri(LoginActivity.this, getValuesWithValid(hashMap, "profileImageUri"));
                    } catch (Exception e) {

                    }
                    isUserExist = true;
                    if (customLoaderDialog != null)
                        customLoaderDialog.hide();
                    Intent intent = new Intent(LoginActivity.this, ChatConversationList.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (customLoaderDialog != null)
                        customLoaderDialog.hide();
                    isUserExist = false;
                    UserModel userModel = new UserModel("" + currentUser.getUid(), "offline", "" + currentUser.getDisplayName(), "", "Android", "" + currentUser.getPhotoUrl(), 0,"TOKEN");
                    userModel.setEmail(currentUser.getEmail());
                    firebase.setValue(userModel);
                    Prefs.setUserId(LoginActivity.this, currentUser.getUid());
                    Prefs.setUSERNAME(LoginActivity.this, currentUser.getDisplayName());
                    Prefs.setEMAIL(LoginActivity.this, currentUser.getEmail());
                    Prefs.setPhotoUri(LoginActivity.this, currentUser.getPhotoUrl() + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                customLoaderDialog.hide();
                isUserExist = false;
                UserModel userModel = new UserModel("" + currentUser.getUid(), "offline", "" + currentUser.getDisplayName(), "", "Android", "" + currentUser.getPhotoUrl(), 0,"TOKEN");
                userModel.setEmail(currentUser.getEmail());
                Prefs.setUserId(LoginActivity.this, currentUser.getUid());
                Prefs.setUSERNAME(LoginActivity.this, currentUser.getDisplayName());
                Prefs.setEMAIL(LoginActivity.this, currentUser.getEmail());
                Prefs.setPhotoUri(LoginActivity.this, currentUser.getPhotoUrl() + "");
                firebase.setValue(userModel);
            }

        });
    }

    private String getValuesWithValid(Map<String, String> hashMap, String displayName) {
        if (hashMap.containsKey("" + displayName) && hashMap.get("" + displayName).length() > 0) {
            return hashMap.get("" + displayName) + "";
        } else {
            return "";
        }
    }


}
