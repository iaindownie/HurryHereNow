package com.HurryHereNow.HHN;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by iaindownie on 11/12/2015.
 */
public class Search extends Activity {

    private SharedPreferences prefs;
    private ImageButton groceries, offies, pubs, cafes, food, hair, other, spot;
    private Button all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);

        groceries = (ImageButton) findViewById(R.id.imgBtnGroceries);
        offies = (ImageButton) findViewById(R.id.imgBtnOffLicence);
        pubs = (ImageButton) findViewById(R.id.imgBtnPubsAndBars);
        cafes = (ImageButton) findViewById(R.id.imgBtnCoffeeAndCafe);
        food = (ImageButton) findViewById(R.id.imgBtnFood);
        hair = (ImageButton) findViewById(R.id.imgBtnHairAndBeauty);
        other = (ImageButton) findViewById(R.id.imgBtnOther);
        spot = (ImageButton) findViewById(R.id.imgBtnSpotAndShare);
        all = (Button) findViewById(R.id.btnReset);

        groceries.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("1"));
                Search.this.finish();
            }
        });
        offies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("2"));
                Search.this.finish();
            }
        });
        pubs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("3"));
                Search.this.finish();
            }
        });
        cafes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("4"));
                Search.this.finish();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("5"));
                Search.this.finish();
            }
        });
        hair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("6"));
                Search.this.finish();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("7"));
                Search.this.finish();
            }
        });
        spot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("99"));
                Search.this.finish();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntent("0"));
                Search.this.finish();
            }
        });


    }

    public Intent getIntent(String str) {
        Intent i = new Intent(getBaseContext(), FragmentOffer.class);
        Bundle b = new Bundle();
        b.putString("CATEGORY", str);
        i.putExtras(b);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CATEGORY", str);
        editor.apply();

        return i;
    }
}
