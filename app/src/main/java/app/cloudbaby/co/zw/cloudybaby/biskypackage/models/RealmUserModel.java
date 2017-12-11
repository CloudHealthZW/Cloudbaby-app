package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kudzai Chasinda on 7/22/2017.
 */

public class RealmUserModel extends RealmObject {
    @PrimaryKey
    private String userId;
    private String status;
    private String firstName;
    private String lastName;
    private String Platform;
    private String profileImageUri;
    private long badge;
    private String email;
    private String phone;
    private String firebaseToken;

    public RealmUserModel(String userId, String status, String firstName, String lastName,
                          String platform, String profileImageUri, long badge, String email,
                          String phone, String firebaseToken) {
        this.userId = userId;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        Platform = platform;
        this.profileImageUri = profileImageUri;
        this.badge = badge;
        this.email = email;
        this.phone = phone;
        this.firebaseToken = firebaseToken;
    }

    public RealmUserModel() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public long getBadge() {
        return badge;
    }

    public void setBadge(long badge) {
        this.badge = badge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }


}
