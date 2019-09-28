package com.example.mapapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.ArrayList;

public class ProfileInfo {

    // Also for icon
    String name, numOfPlaces, budget, time;
    int position; // For recycle view/Edit profile
    // Bitmap icon;
    ArrayList<String> tags;

    public ProfileInfo(Intent data) {
        name = data.getStringExtra("P_name");
        numOfPlaces = data.getStringExtra("P_numOfPlaces");
        budget = data.getStringExtra("P_budget");
        time = data.getStringExtra("P_time");
        tags = data.getStringArrayListExtra("P_tags");

        if (data.hasExtra("P_position")) {
            position = data.getIntExtra("P_position", -1);
        }
        // icon = data.getParcelableExtra("icon");
    }

    public ProfileInfo() {
        name = "placeholder";
        numOfPlaces = "placeholder";
        budget = "placeholder";
        time = "placeholder";
    }

}
