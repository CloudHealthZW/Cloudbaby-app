package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

/**
 * Created by bisky on 6/25/2017.
 */

public class ModelAilments {

    String id, ailments, description, type, severity, date;


    public ModelAilments(String id, String ailments, String description, String type, String severity, String date) {
        this.id = id;
        this.ailments = ailments;
        this.description = description;
        this.type = type;
        this.severity = severity;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getAilments() {
        return ailments;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDate() {
        return date;
    }
}
