package com.example.mapapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationInfo {
    String name, address, lat, lng;
    LatLng position;
    MarkerOptions markerOption;

    public LocationInfo(String[] data) {
        name = data[0];
        address = data[1];
        lat = data[2];
        lng = data[3];

        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        markerOption = new MarkerOptions().position(position);
        MapsActivity.singleSearchMarker = MapsActivity.map.addMarker(markerOption);
    }

}
