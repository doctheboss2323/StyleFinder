package com.labhall.stylefinder001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import com.labhall.stylefinder001.ui.Store;


public class RecActivity extends AppCompatActivity {

    private LinearLayout container;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);
        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();}

        String selectedStyle="";
        String selectedPrice="";
        Store[] stores = new Store[10];

        container = findViewById(R.id.container);

        Intent intent = getIntent();
        if (intent != null) {
            selectedStyle = intent.getStringExtra("selectedStyle").toLowerCase(Locale.ROOT);
            selectedPrice = intent.getStringExtra("selectedPrice");
        }


        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("stores_list.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            i=0;
            while ((line = reader.readLine()) != null) {
                // Process each line of the CSV file
                String[] data = line.split(","); // Assuming comma-separated values
                stores[i]=new Store(data[0],data[1],Integer.parseInt(data[2]),data[3]);
                i++;
            }

            // Close the reader after reading the file
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        stores=giveScores(stores,selectedStyle,selectedPrice);
        stores=sortStores(stores);

        for (Store store : stores) {
            LinearLayout storeLayout = createStoreLayout(store);
            container.addView(storeLayout);
        }

        //now show the stores according to score
        //then keep preferences per user
    }



    public Store[] giveScores(Store[] stores,String style,String price){
        int p=0,count;
        if(price.equals("$"))  p=1;
        if(price.equals("$$"))  p=2;
        if(price.equals("$$$"))  p=3;

        for (int i = 0; i < 10; i++) {
            count=0;
            if(stores[i].getStyle().equals(style)) count+=2;
            if(stores[i].getPrice()==p) count+=2;
            else if((Math.abs(stores[i].getPrice()-p))==1) count+=1;

            stores[i].setScore(count);
        }
        return stores;
    }

    public Store[] sortStores(Store[] stores){
        int count=0;
        ArrayList<Store>[] lists = new ArrayList[5];
        for (int i = 0; i < lists.length; i++) {
            lists[i]= new ArrayList<Store>();
        }
        for (int i = 0; i < 10; i++) {
            lists[stores[i].getScore()].add(stores[i]);
        }
        for (int i = 4; i >=0; i--) {
            for (int j = 0; j < lists[i].size(); j++) {
                stores[count]=lists[i].get(j);
                count++;
            }
        }
        return stores;
    }

    private LinearLayout createStoreLayout(Store store) {
        LinearLayout storeLayout = new LinearLayout(this);
        storeLayout.setOrientation(LinearLayout.VERTICAL);
        storeLayout.setPadding(16, 16, 16, 16);

        TextView nameTextView = new TextView(this);
        nameTextView.setText("Name: " + store.getName());


        TextView styleTextView = new TextView(this);
        styleTextView.setText("Style: " + store.getStyle());

        TextView priceTextView = new TextView(this);
        priceTextView.setText("Price: " + Integer.toString(store.getPrice()));

        TextView addressTextView = new TextView(this);
        addressTextView.setText("Address: " + store.getAddress());

        storeLayout.addView(nameTextView);
        storeLayout.addView(styleTextView);
        storeLayout.addView(priceTextView);
        storeLayout.addView(addressTextView);

         //Create an ImageView for each image in the store's images array
        String shop= store.getName();
        shop = shop.replace(" ", "_");
        shop = shop.replace("&", "");
        shop = shop.toLowerCase();

        for (int i=0;i<5;i++) {
            try {
                ImageView imageView = new ImageView(this);
                String imageName=shop+Integer.toString(i);
                String resourceName = "drawable/" + imageName;

                int resId = getResources().getIdentifier(resourceName, null, getPackageName());
                imageView.setImageResource(resId);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 16, 0, 16);

                storeLayout.addView(imageView, layoutParams);
            } finally {
                // Handling code for the exception
            }
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);

        storeLayout.setLayoutParams(layoutParams);
        storeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pink_nude));

        return storeLayout;
    }
}