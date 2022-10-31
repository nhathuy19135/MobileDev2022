package com.example.mobiledev2022;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mobiledev2022.databinding.ActivityHomePageBinding;

public class AllFeaturesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_features);



        TextView allfeature = findViewById(R.id.title_view);
    }
}