package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

/**
 * Created by bisky on 6/25/2017.
 */

public class ModelOptions {

    String id, title, infantId;

    public ModelOptions(String id, String title, String infantId) {
        this.id = id;
        this.title = title;
        this.infantId = infantId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInfantId() {
        return infantId;
    }
}
