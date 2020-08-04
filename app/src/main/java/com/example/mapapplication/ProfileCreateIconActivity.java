package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfileCreateIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_icon_ui);
        getSupportActionBar().setTitle("Choose an Icon");

        // References
        ImageButton profile_create_icon_select1 = findViewById(R.id.profile_create_icon_select1);
        ImageButton profile_create_icon_select2 = findViewById(R.id.profile_create_icon_select2);
        ImageButton profile_create_icon_select3 = findViewById(R.id.profile_create_icon_select3);
        ImageButton profile_create_icon_select4 = findViewById(R.id.profile_create_icon_select4);

        // Click listeners
        profile_create_icon_select1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon_select2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 2);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon_select3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 3);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon_select4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 4);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
