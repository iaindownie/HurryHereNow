package com.spawny.HHNbeta.data;

import java.io.Serializable;

/**
 * Created by iaindownie on 09/12/2015.
 * Simple POJO to hold the retailer object from JSON
 */
public class Retailer implements Serializable {

    private int retailerId;
    private String name;
    private String logo;
    private String smallImage;
    private String largeImage;

    public Retailer() {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }
}
