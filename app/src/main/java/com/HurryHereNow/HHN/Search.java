package com.HurryHereNow.HHN;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by iaindownie on 11/12/2015.
 * Main search_screen Activity (not a Fragment). Reopens
 * Main Activity once used, passing Category to Bundle and
 * Shared Preferences
 */
public class Search extends Activity {
    SharedPreferences prefs;

    private ImageButton groceries, offies, pubs, cafes, food, hair, other, spot;
    private TextView groceriesTxt, offiesTxt, pubsTxt, cafesTxt, foodTxt, hairTxt, otherTxt, spotTxt;
    private Button all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_screen);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        groceries = (ImageButton) findViewById(R.id.imgBtnGroceries);
        offies = (ImageButton) findViewById(R.id.imgBtnOffLicence);
        pubs = (ImageButton) findViewById(R.id.imgBtnPubsAndBars);
        cafes = (ImageButton) findViewById(R.id.imgBtnCoffeeAndCafe);
        food = (ImageButton) findViewById(R.id.imgBtnFood);
        hair = (ImageButton) findViewById(R.id.imgBtnHairAndBeauty);
        other = (ImageButton) findViewById(R.id.imgBtnOther);
        spot = (ImageButton) findViewById(R.id.imgBtnSpotAndShare);
        all = (Button) findViewById(R.id.btnReset);

        groceriesTxt = (TextView) findViewById(R.id.txtGroceries);
        offiesTxt = (TextView) findViewById(R.id.txtOffLicence);
        pubsTxt = (TextView) findViewById(R.id.txtPubsAndBars);
        cafesTxt = (TextView) findViewById(R.id.txtCoffeeAndCafe);
        foodTxt = (TextView) findViewById(R.id.txtFood);
        hairTxt = (TextView) findViewById(R.id.txtHairAndBeauty);
        otherTxt = (TextView) findViewById(R.id.txtOther);
        spotTxt = (TextView) findViewById(R.id.txtSpotAndShare);

        groceries.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("1"));
                Search.this.finish();
            }
        });
        offies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("2"));
                Search.this.finish();
            }
        });
        pubs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("3"));
                Search.this.finish();
            }
        });
        cafes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("4"));
                Search.this.finish();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("5"));
                Search.this.finish();
            }
        });
        hair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("6"));
                Search.this.finish();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("7"));
                Search.this.finish();
            }
        });
        spot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("99"));
                Search.this.finish();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("0"));
                Search.this.finish();
            }
        });

        groceriesTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("1"));
                Search.this.finish();
            }
        });
        offiesTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("2"));
                Search.this.finish();
            }
        });
        pubsTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("3"));
                Search.this.finish();
            }
        });
        cafesTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("4"));
                Search.this.finish();
            }
        });
        foodTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("5"));
                Search.this.finish();
            }
        });
        hairTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("6"));
                Search.this.finish();
            }
        });
        otherTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("7"));
                Search.this.finish();
            }
        });
        spotTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getCategoryIntent("99"));
                Search.this.finish();
            }
        });


    }

    public Intent getCategoryIntent(String str) {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        Bundle b = new Bundle();
        b.putString("CATEGORY", str);
        i.putExtras(b);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("CATEGORY", str);
        ed.apply();
        return i;
    }
}
