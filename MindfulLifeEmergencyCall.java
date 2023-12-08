package com.example.app.ui.emergency;

public class CustomEmergencyContact {

    private String contactName; 
    private long contactPhoneNumber;
    private String contactEmail; 
    private String contactId;
    private static int contactIdCounter;

    public CustomEmergencyContact() {

    }

    public CustomEmergencyContact(String name, long phoneNumber, String email) {
        this.contactName = name;
        this.contactPhoneNumber = phoneNumber;
        this.contactEmail = email;

        this.contactIdCounter++;
        this.contactId = Integer.toString(this.contactIdCounter);
    }

    public String getContactName() {
        return this.contactName;
    }

    public long getContactPhoneNumber() {
        return this.contactPhoneNumber;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactName(String name) {
        this.contactName = name;
    }

    public void setContactPhoneNumber(long phoneNumber) {
        this.contactPhoneNumber = phoneNumber;
    }

    public void setContactEmail(String email) {
        this.contactEmail = email;
    }

    public void setContactId(String id) {
        this.contactId = id;
    }
}
