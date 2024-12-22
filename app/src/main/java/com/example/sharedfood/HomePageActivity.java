package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // כפתור Share Food
        Button shareFoodButton = findViewById(R.id.shareFoodButton);
        shareFoodButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ShareYourFoodActivity.class);
            startActivity(intent);
        });

        // כפתור Personal Area
        Button personalAreaButton = findViewById(R.id.personalAreaButton);
        personalAreaButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, PersonalAreaActivity.class);
            startActivity(intent);
        });
    }
}
