package app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.events;

/**
 * @author Kudzai Chasinda
 */

public class PhoneNumberEnteredEvent {
    public String code;
    public String phoneNumber;

    public PhoneNumberEnteredEvent(String code, String phoneNumber) {
        this.code = code;
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumberEnteredEvent() {
    }
}
