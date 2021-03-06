package com.HurryHereNow.HHN.data;

import java.io.Serializable;

/**
 * Created by iaindownie on 08/12/2015.
 * Simple POJO to hold the promotion data from JSON via API
 * Take Retailer and Offer objects
 */
public class RetailerOffers implements Serializable {
    private int storeId;
    private int retailerId;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String postcode;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String site;
    private Retailer retailer;
    private Offer[] offers;
    private int category;

    private String distanceTo;

    /**
     * The follow relate to UserSubmitted offers only and reuse the
     * same latitude and longitude values as Retailer Offers above
     */
    private int userOfferId;
    private String storeName;
    private String description;
    private String date;
    private String status;

    public RetailerOffers() {

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Offer[] getOffers() {
        return offers;
    }

    public void setOffers(Offer[] offers) {
        this.offers = offers;
    }

    public int getUserOfferId() {
        return userOfferId;
    }

    public void setUserOfferId(int userOfferId) {
        this.userOfferId = userOfferId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(String distanceTo) {
        this.distanceTo = distanceTo;
    }
}