package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    static int map_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ui);
        getSupportActionBar().setTitle("Settings");

        // Map Type - Spinner
        Spinner setting_map_type = findViewById(R.id.setting_map_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.map_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setting_map_type.setAdapter(adapter);
        setting_map_type.setOnItemSelectedListener(this);

        // Search Radius - Spinner
        Spinner settings_searching_radius = findViewById(R.id.settings_searching_radius);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.search_radius, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settings_searching_radius.setAdapter(adapter2);
        settings_searching_radius.setOnItemSelectedListener(this);

        // TODO: Remember to change default spinner item based on previous selection
    }

    @Override // Switches map type
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String input = parent.getItemAtPosition(position).toString();
        switch (input) {

            // Map Type
            case "Roadmap":
                MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Satellite":
                MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "Terrain":
                MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case "Hybird":
                MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

             // Search Radius
            case "Very Small (3km)":
                MapsActivity.radius = 3000;
                break;
            case "Small (5km)":
                MapsActivity.radius = 5000;
                break;
            case "Medium (10km)":
                MapsActivity.radius = 10000;
                break;
            case "Large (25km)":
                MapsActivity.radius = 25000;
                break;
            case "Very Large (50km)":
                MapsActivity.radius = 50000;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
