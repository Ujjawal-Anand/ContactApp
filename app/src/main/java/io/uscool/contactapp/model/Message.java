package io.uscool.contactapp.model;

/**
 * Created by ujjawal on 16/7/17.
 */

public class Message {
    private Contact mContact; // contact associated with message
    private String mOtpCode;  // otp code that has been sent on number
    private String mTimestamp; // timestamp at which message has been sent

    public Message(Contact contact, String otpCode, String timestamp) {
        this.mContact = contact;
        this.mOtpCode = otpCode;
        this.mTimestamp = timestamp;
    }

    public Contact getContact() {
        return mContact;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public String getOtpCode() {
        return mOtpCode;
    }
}
