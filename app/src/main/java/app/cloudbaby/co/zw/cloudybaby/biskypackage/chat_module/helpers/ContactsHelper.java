package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.ContactModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.RealmUserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Constants;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import io.realm.Realm;

/**
 * @author Kudzai Chasinda
 */

public class ContactsHelper {
    private Context context;

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //Root Ref to our db
    private DatabaseReference databaseReference;

    private static PreferencesUtils mUtils;

    public ContactsHelper(Context context) {
        this.context = context;
    }

    public static ContactsHelper newInstance(Context context) {
        mUtils = PreferencesUtils.getInstance(context);
        return new ContactsHelper(context);
    }

    private List<ContactModel> getContacts(Context context) {
        List<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = GeneralUtils.sanitizePhoneNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        info.photo = photo;
                        info.photoURI = pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
        }
        return list;
    }

    private List<UserModel> getUserList() {
        //We should get all the users from firebase
        final List<UserModel> userList = new ArrayList<>();
        databaseReference = database.getReference();

        databaseReference.child(Constants.CHAT_USER_BADGE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        if (!TextUtils.equals(user.getUserId(), mUtils.getUserId())) {
                            userList.add(user);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return userList;
    }

    public List<UserModel> displayList(Context context) {
        //This is the list we will present finally in the end of it all

        List<UserModel> displayList = new ArrayList<>();
        List<UserModel> allUsers = getUserList();
        List<ContactModel> allContacts = getContacts(context);

        //Use a set or something to filter out contact
        RealmUserModel realmUserModel = new RealmUserModel();
        Realm realm = Realm.getDefaultInstance();

        for (UserModel user : allUsers) {
            for (ContactModel contact : allContacts) {
                if (user.getUserId().equals(contact.mobileNumber)) {
                    Log.e("FOUNDMATCH!", "Yipeeeeeeeeeee");
                    realmUserModel.setUserId(user.getUserId());
                    realmUserModel.setFirstName(contact.name);
                    realmUserModel.setBadge(user.getBadge());
                    realmUserModel.setEmail(user.getEmail());
                    realmUserModel.setLastName(user.getLastName());
                    realmUserModel.setFirebaseToken(user.getFirebaseToken());
                    realmUserModel.setPhone(user.getPhone());
                    realmUserModel.setPlatform(user.getPlatform());
                    realmUserModel.setStatus(user.getStatus());
                    realmUserModel.setProfileImageUri(user.getProfileImageUri());
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(realmUserModel);
                    realm.commitTransaction();

                    Log.e("REALM", "SAVED!!!!!!!!!!!!!!!!");
                }
            }
            Log.e("USERMODEL", user.getUserId());
        }

        for (ContactModel user : allContacts) {
            Log.e("CONTACTMODEL", user.mobileNumber);
        }

        return displayList;
    }


}
