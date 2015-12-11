package com.HurryHereNow.HHN;

import android.util.Log;

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
import com.HurryHereNow.HHN.data.MySimpleMarker;
import com.HurryHereNow.HHN.data.Offer;
import com.HurryHereNow.HHN.data.Retailer;

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

    public static String uploadSpotAndShare(String sN, String sDesc, String lat, String lon) {
        String rootUrl = "http://api.hurryherenow.com/api/user-offer?";
        int statusCode = 0;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(rootUrl);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("storeName", sN));
            urlParameters.add(new BasicNameValuePair("description", sDesc));
            urlParameters.add(new BasicNameValuePair("latitude", lat));
            urlParameters.add(new BasicNameValuePair("longitude", lon));

            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

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

    public static ArrayList<MySimpleMarker> convertJSONSpotAndSharePromotionsToArrayList(String str, String category) throws Exception {
        System.out.println("In convertJSONSpotAndSharePromotionsToArrayList");
        JSONObject json = new JSONObject(str);
        ArrayList everything = new ArrayList();

        JSONObject json2 = json.getJSONObject("userSubmitted");
        System.out.println("Processing userSubmitted");
        Iterator it = json2.keys();
        while (it.hasNext()) {
            MySimpleMarker p = new MySimpleMarker();
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
                MySimpleMarker p = new MySimpleMarker();
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

        if(category.equals("0")){
            return everything;
        }else{
            ArrayList subset = new ArrayList();
            for (int i = 0; i < everything.size(); i++) {
                MySimpleMarker p = (MySimpleMarker)everything.get(i);
                int cat = p.getCategory();
                if(cat==(Integer.valueOf(category))){
                    subset.add(p);
                }
            }
            return subset;
        }

        //return everything;
    }

}
