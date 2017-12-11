package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bisky on 7/12/2017.
 */

public class TipsModel extends RealmObject {
    private String message;
    private String title;
    @PrimaryKey
    private long time;

    public TipsModel() {
    }

    public TipsModel(String message, String title, long time) {
        this.message = message;
        this.title = title;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public long getTime() {
        return time;
    }
}
