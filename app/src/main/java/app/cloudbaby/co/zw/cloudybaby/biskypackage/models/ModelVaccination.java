package app.cloudbaby.co.zw.cloudybaby.biskypackage.models;

/**
 * Created by bisky on 6/26/2017.
 */

public class ModelVaccination {

    String _id, vaccination, drugAdministered, amount, description, date, centreId;

    public ModelVaccination(String _id, String vaccination, String drugAdministered, String amount, String description, String date, String centreId) {
        this._id = _id;
        this.vaccination = vaccination;
        this.drugAdministered = drugAdministered;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.centreId = centreId;
    }

    public String get_id() {
        return _id;
    }

    public String getVaccination() {
        return vaccination;
    }

    public String getDrugAdministered() {
        return drugAdministered;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCentreId() {
        return centreId;
    }
}
