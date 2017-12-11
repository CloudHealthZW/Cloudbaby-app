package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.events;

import java.util.List;

import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;

/**
 * @author Kudzai Chasinda
 */

public class OnContactsListLoadedEvent {

    public List<UserModel> getFilteredContacts() {
        return filteredContacts;
    }

    public void setFilteredContacts(List<UserModel> filteredContacts) {
        this.filteredContacts = filteredContacts;
    }

    private List<UserModel> filteredContacts;

    public OnContactsListLoadedEvent(List<UserModel> filteredContacts) {
        this.filteredContacts = filteredContacts;
    }
}
