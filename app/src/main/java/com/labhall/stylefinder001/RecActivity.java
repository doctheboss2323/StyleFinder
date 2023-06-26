package com.labhall.stylefinder001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.labhall.stylefinder001.ui.Store;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


public class RecActivity extends AppCompatActivity {

    private LinearLayout container;

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
            selectedStyle = intent.getStringExtra("selectedStyle");
            selectedPrice = intent.getStringExtra("selectedPrice");
        }




        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("stores_list.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Process each line of the CSV file
                String[] data = line.split(","); // Assuming comma-separated values

                LinearLayout storeLayout = createStoreLayoutNoImg(data[0],data[1],data[3]);
                container.addView(storeLayout);
            }

            // Close the reader after reading the file
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



//        stores=addImgs(stores);
//        stores=giveScores(stores,selectedStyle,selectedPrice);
//        stores=sortStores(stores);
//
//        for (Store store : stores) {
//            LinearLayout storeLayout = createStoreLayout(store);
//            container.addView(storeLayout);
//        }


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
        ArrayList<Store>[] lists = new ArrayList[4];
        for (int i = 0; i < lists.length; i++) {
            lists[i]= new ArrayList<Store>();
        }
        for (int i = 0; i < stores.length; i++) {
            lists[stores[i].getScore()].add(stores[i]);
        }
        for (int i = 0; i < lists.length; i++) {
            for (int j = 0; j < lists[i].size(); j++) {
                stores[i]=lists[i].get(j);
            }
        }
        return stores;
    }

    private int[] getImagesFromFolder(String folderName) {
        AssetManager assetManager = getAssets();
        String[] imagePaths;
        int[] imageResources = new int[5];

        try {
            imagePaths = assetManager.list(folderName);

            if (imagePaths != null && imagePaths.length >= 5) {  //Later change it back to 5
                for (int i = 0; i < 4; i++) { //here from 4 to 5 and uncomment the lower version
                    String imagePath = "images/" + folderName + "/" + imagePaths[i];
                    InputStream inputStream = assetManager.open(imagePath);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    imageResources[i] = getResources().getIdentifier(imagePath, null, getPackageName());
                }
            }
//            if (imagePaths != null && imagePaths.length == 4) {  //  because flashback has 4
//                for (int i = 0; i < 4; i++) {
//                    String imagePath = "images/" + folderName + "/" + imagePaths[i];
//                    InputStream inputStream = assetManager.open(imagePath);
//                    Drawable drawable = Drawable.createFromStream(inputStream, null);
//                    imageResources[i] = getResources().getIdentifier(imagePath, null, getPackageName());
//                }
//                imageResources[4]=0;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageResources;
    }


    public Store[] addImgs(Store[] stores){
        stores[0].setImages(getImagesFromFolder("images/Flashback"));
        stores[1].setImages(getImagesFromFolder("images/hayo_haya"));
        stores[2].setImages(getImagesFromFolder("images/zara"));
        stores[3].setImages(getImagesFromFolder("images/bitch_please"));
        stores[4].setImages(getImagesFromFolder("images/bolenat"));
        stores[5].setImages(getImagesFromFolder("images/ruby_bay"));
        stores[6].setImages(getImagesFromFolder("images/hm"));
        stores[7].setImages(getImagesFromFolder("images/jiffa"));
        stores[8].setImages(getImagesFromFolder("images/street_dolls"));
        stores[9].setImages(getImagesFromFolder("images/studio_pasha"));
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
        priceTextView.setText("Price: " + store.getPrice());
        // add adress

        // Create an ImageView for each image in the store's images array
//        int[] images = store.getImages();
//        for (int imageResId : images) {
//            if(imageResId!=0){
//                ImageView imageView = new ImageView(this);
//                imageView.setImageResource(imageResId);
//                imageView.setAdjustViewBounds(true);
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                layoutParams.setMargins(0, 16, 0, 16);
//
//                storeLayout.addView(imageView, layoutParams);
//        }
//        }

        storeLayout.addView(nameTextView);
        storeLayout.addView(styleTextView);
        storeLayout.addView(priceTextView);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);

        storeLayout.setLayoutParams(layoutParams);
        storeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pink_nude));

        return storeLayout;
    }

    private LinearLayout createStoreLayoutNoImg(String s1,String s2,String s3) {
        LinearLayout storeLayout = new LinearLayout(this);
        storeLayout.setOrientation(LinearLayout.VERTICAL);
        storeLayout.setPadding(16, 16, 16, 16);

        TextView nameTextView = new TextView(this);
        nameTextView.setText("Name: " + s1);

        TextView styleTextView = new TextView(this);
        styleTextView.setText("Style: " + s2);

        TextView priceTextView = new TextView(this);
        priceTextView.setText("Price: " + s3);

        storeLayout.addView(nameTextView);
        storeLayout.addView(styleTextView);
        storeLayout.addView(priceTextView);

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