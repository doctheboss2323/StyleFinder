package com.labhall.stylefinder001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
        Store[] stores = new Store [10];

        container = findViewById(R.id.container);

        Intent intent = getIntent();
        if (intent != null) {
            selectedStyle = intent.getStringExtra("selectedStyle");
            selectedPrice = intent.getStringExtra("selectedPrice");
        }

        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("stores_list.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            String[] nextLine;
            for (int i = 0; i < stores.length; i++) {
                nextLine = reader.readNext();
                Store store= new Store(nextLine[0],nextLine[1],Integer.valueOf(nextLine[2]),nextLine[3]);
                stores[i]=store;
            }

            reader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        stores=addImgs(stores);
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
        ArrayList<Integer> imageList = new ArrayList<>();
        Resources res = getResources();

        // Get the identifier of the folder
        int folderId = res.getIdentifier(folderName, "drawable", getPackageName());

        // Get the list of all resources in the folder
        TypedArray typedArray = res.obtainTypedArray(folderId);

        for (int i = 0; i < typedArray.length(); i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            imageList.add(resourceId);
        }

        typedArray.recycle();

        // Convert the ArrayList to an array
        int[] imageArray = new int[5];
        for (int i = 0; i < imageList.size(); i++) {
            if(imageList.get(i)!=null){
                imageArray[i] = imageList.get(i);}
            else{
                imageArray[i]=0;}
        }
        return imageArray;
    }

    public Store[] addImgs(Store[] stores){
        stores[0].setImages(getImagesFromFolder("Flashback"));
        stores[1].setImages(getImagesFromFolder("hayo_haya"));
        stores[2].setImages(getImagesFromFolder("zara"));
        stores[3].setImages(getImagesFromFolder("bitch_please"));
        stores[4].setImages(getImagesFromFolder("bolenat"));
        stores[5].setImages(getImagesFromFolder("ruby_bay"));
        stores[6].setImages(getImagesFromFolder("hm"));
        stores[7].setImages(getImagesFromFolder("jiffa"));
        stores[8].setImages(getImagesFromFolder("street_dolls"));
        stores[9].setImages(getImagesFromFolder("studio_pasha"));
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

        // Create an ImageView for each image in the store's images array
        int[] images = store.getImages();
        for (int imageResId : images) {
            if(imageResId!=0){
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(imageResId);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 16, 0, 16);

                storeLayout.addView(imageView, layoutParams);
        }
        }

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