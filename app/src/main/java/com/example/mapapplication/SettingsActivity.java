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

        Spinner map_type = findViewById(R.id.setting_map_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.map_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        map_type.setAdapter(adapter);
        map_type.setOnItemSelectedListener(this);
        map_type.setSelection(MapsActivity.map.getMapType() - 1);
    }

    @Override // Switches map type
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String map_type = parent.getItemAtPosition(position).toString();
        switch (map_type) {
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
