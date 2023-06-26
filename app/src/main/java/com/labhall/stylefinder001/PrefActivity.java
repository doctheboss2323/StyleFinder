package com.labhall.stylefinder001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PrefActivity extends AppCompatActivity {
    private RadioGroup radioGroupStyle;
    private RadioGroup radioGroupPrice;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_activity);
        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();}

        radioGroupStyle = findViewById(R.id.radioGroupStyle);
        radioGroupPrice = findViewById(R.id.radioGroupPrice);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedStyleId = radioGroupStyle.getCheckedRadioButtonId();
                RadioButton selectedStyleRadioButton = findViewById(selectedStyleId);
                String selectedStyle = selectedStyleRadioButton.getText().toString();

                int selectedPriceId = radioGroupPrice.getCheckedRadioButtonId();
                RadioButton selectedPriceRadioButton = findViewById(selectedPriceId);
                String selectedPrice = selectedPriceRadioButton.getText().toString();

                Intent intent = new Intent(PrefActivity.this, RecActivity.class);
                intent.putExtra("selectedStyle", selectedStyle);
                intent.putExtra("selectedPrice", selectedPrice);
                startActivity(intent);
            }
        });
    }
}
