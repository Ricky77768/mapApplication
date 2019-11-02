package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ui);
        getSupportActionBar().setTitle("Settings");

        // Map Type - Spinner
        Spinner setting_map_type = findViewById(R.id.settings_map_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.map_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setting_map_type.setAdapter(adapter);
        setting_map_type.setOnItemSelectedListener(this);
        setting_map_type.setSelection(MapsActivity.map.getMapType() - 1);

        // Search Radius - Spinner
        Spinner settings_search_radius = findViewById(R.id.settings_search_radius);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.search_radius, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settings_search_radius.setAdapter(adapter2);
        settings_search_radius.setOnItemSelectedListener(this);
        switch (MapsActivity.searchRadius) {
            case 10000:
                settings_search_radius.setSelection(0);
                break;
            case 25000:
                settings_search_radius.setSelection(1);
                break;
            case 50000:
                settings_search_radius.setSelection(2);
                break;
        }
    }

    @Override // Switches map type/search radius
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
            case "Small (10km)":
                MapsActivity.searchRadius = 10000;
                MapsActivity.changeRadius();
                break;
            case "Medium (25km)":
                MapsActivity.searchRadius = 25000;
                MapsActivity.changeRadius();
                break;
            case "Large (50km)":
                MapsActivity.searchRadius = 50000;
                MapsActivity.changeRadius();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
