package com.HurryHereNow.HHN.data;

import java.io.Serializable;

/**
 * Created by iaindownie on 09/12/2015.
 * Simple POJO to hold the offer object from JSON
 */
public class Offer implements Serializable {

    private int offerId;
    private int retailerId;
    private int storeId;
    private int offerCategoryId;
    private String description;
    private String startDate;
    private String endDate;
    private int pve;
    private int nve;

    public Offer() {
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOfferCategoryId() {
        return offerCategoryId;
    }

    public void setOfferCategoryId(int offerCategoryId) {
        this.offerCategoryId = offerCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPve() {
        return pve;
    }

    public void setPve(int pve) {
        this.pve = pve;
    }

    public int getNve() {
        return nve;
    }

    public void setNve(int nve) {
        this.nve = nve;
    }
}
