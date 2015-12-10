package com.HurryHereNow.HHN;

import java.util.HashMap;

/**
 * Created by iaindownie on 08/12/2015.
 */
public class Utilities {
    /**
     * Worker list for getting the category string from category number
     *
     * @return
     */
    public static HashMap<Integer, String> getCategoryFromNumber() {
        HashMap<Integer, String> translation = new HashMap<Integer, String>();
        translation.put(new Integer(1), "Groceries");
        translation.put(new Integer(2), "Groceries");

        return translation;
    }

}
