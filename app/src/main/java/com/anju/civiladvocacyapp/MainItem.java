package com.anju.civiladvocacyapp;

import java.io.Serializable;

public class MainItem implements Serializable {
    private String name;
    private String office;
    private String address;
    private String party;
    private String phoneNumber;
    private String website;
    private String email;
    private String photoUrl;
    private String facebookId;
    private String twitterId;
    private String youtubeId;
    private String city;
    private String state;
    private String zip;

    public MainItem(String name, String address, String party, String phoneNumber, String website, String email, String photoUrl, String office, String facebookId, String twitterId, String youtubeId, String city,String state, String zip) {
        this.name = name;
        this.address = address;
        this.party = party;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.email = email;
        this.photoUrl = photoUrl;
        this.office = office;
        this.facebookId = facebookId;
        this.twitterId = twitterId;
        this.youtubeId = youtubeId;
        this.city = city;
        this.state=state;
                this.zip=zip;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    // Getters and setters for the data model fields
    // You can generate these automatically in most IDEs or write them manually
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

