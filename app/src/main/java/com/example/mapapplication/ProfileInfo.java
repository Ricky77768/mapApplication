package com.example.mapapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.ArrayList;

// TODO: Implement Icon Sync once sorted out
public class ProfileInfo {

    int position; // For RecycleView - Determine which profile to edit
    String name, numOfPlaces, budget, time;
    ArrayList<String> tags;
    // Bitmap icon;

    // Profile Creation
    public ProfileInfo(Intent data) {
        name = data.getStringExtra("P_name");
        numOfPlaces = data.getStringExtra("P_numOfPlaces");
        budget = data.getStringExtra("P_budget");
        time = data.getStringExtra("P_time");
        tags = data.getStringArrayListExtra("P_tags");
        // icon = data.getParcelableExtra("icon");

        if (data.hasExtra("P_position")) {
            position = data.getIntExtra("P_position", -1);
        }
    }

}
