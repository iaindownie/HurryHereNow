package com.HurryHereNow.HHN;

import android.util.Log;

import com.HurryHereNow.HHN.data.Offer;
import com.HurryHereNow.HHN.data.Retailer;
import com.HurryHereNow.HHN.data.RetailerOfferForList;
import com.HurryHereNow.HHN.data.RetailerOffers;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by iaindownie on 27/11/2015.
 */
public class JSONUtilities {

    /**
     * Method to get Talk data from SERVER via API
     *
     * @param path - typically http://api.hurryherenow.com/api/talk?latitude=52.415127&longitude=0.7504132
     * @return - a String formatted as JSON
     * @throws Exception
     */
    public static String downloadAllTalksFromURL(String path) throws Exception {
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpGet myConnection = new HttpGet(path);
        try {
            response = myClient.execute(myConnection);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                Log.e("==>", "Failed to extract talks...");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Method to PARSE the Talk JSON String to individual Talk items
     *
     * @param str - Takes the JSON String
     * @return - ArrayList containing the individual talk items as bunch of HashMaps
     * @throws Exception
     */
    public static ArrayList<HashMap> convertJSONTalksToArrayList(String str) throws Exception {
        JSONObject json = null;
        ArrayList<HashMap> aList = new ArrayList<HashMap>();

        try {
            JSONArray jsonMainArr = new JSONArray(str);

            for (int i = 0; i < jsonMainArr.length(); i++) { // **line 2**
                JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("comment", childJSONObject.getString("comment"));
                hm.put("description", childJSONObject.getString("description"));
                hm.put("date", childJSONObject.getString("date"));
                hm.put("name", childJSONObject.getString("name"));
                hm.put("type", childJSONObject.getString("type"));
                aList.add(hm);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Method to upload the Spot & Share details to SERVER via API
     * http://api.hurryherenow.com/api/user-offer? then parameters
     *
     * @param sN    - Store name
     * @param sDesc - Offer description
     * @param lat   - Store latitude
     * @param lon   - Store longitude
     * @return - Success or fail with error message
     */
    public static String uploadSpotAndShare(String sN, String sDesc, String lat, String lon) {
        String rootUrl = Constants.API_BASE_URL + "/api/user-offer?";
        int statusCode = 0;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(rootUrl);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("storeName", sN));
            urlParameters.add(new BasicNameValuePair("description", sDesc));
            urlParameters.add(new BasicNameValuePair("latitude", lat));
            urlParameters.add(new BasicNameValuePair("longitude", lon));

            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters, "utf-8"));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

        } catch (UnsupportedEncodingException e) {
            return "fail: UnsupportedEncodingException";
        } catch (MalformedURLException e) {
            return "fail: MalformedURLException";
        } catch (IOException e) {
            return "fail: IOException";
        }

        if (statusCode == 200) {
            return "success";
        } else {
            return "fail: catchall";
        }
    }


    public static String uploadRating(String comment, String offerId, String type) {
        //System.out.println("Calling + positive rating JSON Utilities + " + offerId);
        String rootUrl = Constants.API_BASE_URL + "/api/rate?";
        int statusCode = 0;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(rootUrl);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("offerId", offerId));
            urlParameters.add(new BasicNameValuePair("type", type));
            urlParameters.add(new BasicNameValuePair("comment", comment));

            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters, "utf-8"));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

        } catch (UnsupportedEncodingException e) {
            return "fail: UnsupportedEncodingException";
        } catch (MalformedURLException e) {
            return "fail: MalformedURLException";
        } catch (IOException e) {
            return "fail: IOException";
        }

        if (statusCode == 200) {
            return "success";
        } else {
            return "fail: catchall";
        }
    }



    /**
     * Method to get Offer data from SERVER via API
     *
     * @param path - typically http://api.hurryherenow.com/api/promotions?latitude=52.415127&longitude=0.7504132&distance=50
     * @return - a String formatted as JSON
     * @throws Exception
     */
    public static String downloadAllPromotionsFromURL(String path) throws Exception {
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpGet myConnection = new HttpGet(path);
        try {
            response = myClient.execute(myConnection);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                Log.e("==>", "Failed to extract talks...");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Method to PARSE the Offer JSON String to individual Offer items
     *
     * @param str      - Takes the JSON String
     * @param category - Takes a category to return subsets if needed
     * @return - ArrayList of all Retailers (as POJO's) for all/individual categories
     * @throws Exception
     */
    public static ArrayList<RetailerOffers> convertJSONSpotAndSharePromotionsToArrayList(String str, String category) throws Exception {
        JSONObject json = new JSONObject(str);
        ArrayList everything = new ArrayList();

        JSONObject json2 = json.getJSONObject("userSubmitted");
        Iterator it = json2.keys();
        while (it.hasNext()) {
            RetailerOffers p = new RetailerOffers();
            JSONObject childJSONObject = json2.getJSONObject((String) it.next());
            p.setCategory(99);
            p.setUserOfferId(childJSONObject.getInt("userOfferId"));
            p.setStoreName(childJSONObject.getString("storeName"));
            p.setDescription(childJSONObject.getString("description"));
            p.setLatitude(childJSONObject.getDouble("latitude"));
            p.setLongitude(childJSONObject.getDouble("longitude"));
            p.setDate(childJSONObject.getString("date"));
            everything.add(p);
        }

        Iterator otherIterator = json.keys();
        while (otherIterator.hasNext()) {
            String primaryKey = (String) otherIterator.next();
            if (!primaryKey.equals("userSubmitted")) {

                JSONObject childJSONObject = json.getJSONObject(primaryKey);
                RetailerOffers p = new RetailerOffers();
                int cat = 7;

                p.setStoreId(childJSONObject.getInt("storeId"));
                p.setRetailerId(childJSONObject.getInt("retailerId"));
                p.setName(childJSONObject.getString("name"));
                p.setAddress1(childJSONObject.getString("address1"));
                p.setAddress2(childJSONObject.getString("address2"));
                p.setCity(childJSONObject.getString("city"));
                p.setPostcode(childJSONObject.getString("postcode"));
                p.setLatitude(childJSONObject.getDouble("latitude"));
                p.setLongitude(childJSONObject.getDouble("longitude"));
                p.setPhone(childJSONObject.getString("phone"));
                p.setSite(childJSONObject.getString("site"));

                JSONObject retailerObject = childJSONObject.getJSONObject("retailer");
                Retailer r = new Retailer();
                r.setRetailerId(retailerObject.getInt("retailerId"));
                r.setName(retailerObject.getString("name"));
                r.setLogo(retailerObject.getString("logo"));
                r.setSmallImage(retailerObject.getString("smallImage"));
                r.setLargeImage(retailerObject.getString("largeImage"));
                p.setRetailer(r);

                JSONArray offersArray = childJSONObject.getJSONArray("offers");
                Offer[] offers = new Offer[offersArray.length()];
                for (int i = 0; i < offersArray.length(); i++) { // **line 2**
                    JSONObject anOffer = offersArray.getJSONObject(i);
                    Offer o = new Offer();
                    cat = anOffer.getInt("offerCategoryId");
                    o.setOfferId(anOffer.getInt("offerId"));
                    o.setRetailerId(anOffer.getInt("retailerId"));
                    o.setStoreId(anOffer.getInt("storeId"));
                    o.setOfferCategoryId(cat);
                    o.setDescription(anOffer.getString("description"));
                    o.setStartDate(anOffer.getString("startDate"));
                    o.setEndDate(anOffer.getString("endDate"));
                    o.setPve(anOffer.getInt("pve"));
                    o.setNve(anOffer.getInt("nve"));
                    offers[i] = o;
                }
                p.setCategory(cat);
                p.setOffers(offers);
                everything.add(p);

            }
        }

        if (category.equals("0")) {
            return everything;
        } else {
            ArrayList subset = new ArrayList();
            for (int i = 0; i < everything.size(); i++) {
                RetailerOffers p = (RetailerOffers) everything.get(i);
                int cat = p.getCategory();
                if (cat == (Integer.valueOf(category))) {
                    subset.add(p);
                }
            }
            return subset;
        }

    }

    /**
     * Helper method to split the base Retailer POJO to represent individual Offers
     *
     * @param aList - An ArrayList of Retailers
     * @return - An ArrayList of Offers
     * @throws Exception
     */
    public static ArrayList<RetailerOffers> expandPromotionsArrayList(ArrayList aList) throws Exception {
        ArrayList fullList = new ArrayList();
        for (int i = 0; i < aList.size(); i++) {
            RetailerOffers ro = (RetailerOffers) aList.get(i);
            Offer[] offers = ro.getOffers();
            if (ro.getCategory() < 99) {
                for (int j = 0; j < offers.length; j++) {
                    Offer o = offers[j];
                    RetailerOfferForList roList = new RetailerOfferForList(ro, o);
                    fullList.add(roList);
                }
            }
        }

        return fullList;
    }

}
