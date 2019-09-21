package com.example.mapapplication;

import android.content.Intent;

public class ProfileInfo {

    // Also for icon
    String name, numOfPlaces, budget, time;
    String[] tags;

    public ProfileInfo(Intent data) {
        name = data.getStringExtra("P_name");
        numOfPlaces = data.getStringExtra("P_numOfPlaces");
        budget = data.getStringExtra("P_budget");
        time = data.getStringExtra("P_time");
    }

    public ProfileInfo() {
        name = "placeholder";
        numOfPlaces = "placeholder";
        budget = "placeholder";
        time = "placeholder";
    }

}
