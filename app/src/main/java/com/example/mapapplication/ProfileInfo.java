package com.example.mapapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.ArrayList;

public class ProfileInfo {
    int position; // For RecycleView - Determine which profile to edit
    int icon; // For selecting icon to display
    String name, numOfPlaces, budget, time, rating; // Values to display
    String rawBudget, rawTime, rawRating; // Integer values for internal calculations
    ArrayList<String> tags;

    // Profile Creation
    public ProfileInfo(Intent data) {
        name = data.getStringExtra("P_name");
        numOfPlaces = data.getStringExtra("P_numOfPlaces");

        rawBudget = data.getStringExtra("P_budget");
        switch (rawBudget) {
            case "0":
                budget = "Minimal";
                break;
            case "1":
                budget = "Small";
                break;
            case "2":
                budget = "Medium";
                break;
            case "3":
                budget = "Large";
                break;
            case "4":
                budget = "Very Large";
                break;
        }

        rawTime = data.getStringExtra("P_time");
        if (rawTime.equals("11")) {
            time = "Does Not Matter";
        } else {
            time = rawTime;
        }

        rawRating = data.getStringExtra("P_rating");
        if (Integer.parseInt(rawRating) <= 10) {
            rating = "Does Not Matter";
        } else {
            rating = String.valueOf(Integer.parseInt(rawRating) / 10.0);
        }

        if (data.hasExtra("P_position")) {
            position = data.getIntExtra("P_position", -1);
        }

        icon = data.getIntExtra("P_icon", -1);
        tags = data.getStringArrayListExtra("P_tags");
    }

}
