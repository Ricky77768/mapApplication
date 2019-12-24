package com.example.mapapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationInfo {

    // Test Laptop Commit
    String name, address, lat, lng;
    LatLng position;
    MarkerOptions markerOption;
    Marker locationMarker;

    public LocationInfo(String[] data) {
        name = data[0];
        address = data[1];
        lat = data[2];
        lng = data[3];

        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        markerOption = new MarkerOptions().position(position);
        locationMarker = MapsActivity.map.addMarker(markerOption);
        MapsActivity.searchMarkers.add(locationMarker);
    }

}
