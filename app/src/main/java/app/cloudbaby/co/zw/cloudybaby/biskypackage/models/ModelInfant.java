package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

/**
 * Created by bisky on 6/25/2017.
 */

public class ModelInfant {

    private String names, type, profilePhoto, id;

    public ModelInfant(String names, String type, String profilePhoto, String id) {
        this.names = names;
        this.type = type;
        this.profilePhoto = profilePhoto;
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public String getType() {
        return type;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getId() {
        return id;
    }
}
