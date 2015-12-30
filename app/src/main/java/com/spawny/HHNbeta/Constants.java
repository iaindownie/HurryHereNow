package com.spawny.HHNbeta;

/**
 * Created by iaindownie on 14/12/2015.
 * Class to hold some constants for whole project
 */
public class Constants {

    // Static var for preventing uploads if in development mode
    public static boolean IS_DEBUG = true;

    // Set default distance for promotions API
    public static int PROMOTIONS_DISTANCE = 50;
    // Set base URLs for use throughout App
    public static String API_BASE_URL = "http://api.hurryherenow.com";
    public static String WWW_BASE_URL = "http://www.hurryherenow.com";

    // Some static content for helping with varying screen sizes
    public static int SCREENWIDTH = 0;
    public static int SCREENHEIGHT = 0;
    public static int getScreenWidth() {
        return SCREENWIDTH;
    }
    public static void setScreenWidth(int screenWidth) {
        Constants.SCREENWIDTH = screenWidth;
    }
    public static int getScreenHeight() {
        return SCREENHEIGHT;
    }
    public static void setScreenHeight(int screenHeight) {
        Constants.SCREENHEIGHT = screenHeight;
    }

    // Static vars for centering on Cambridge
    public static double CAMBRIDGE_LAT = 52.2068236;
    public static double CAMBRIDGE_LON = 0.1187916;
}
