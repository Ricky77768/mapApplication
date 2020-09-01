package com.example.mapapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationInfo {
    int type; // 1 = Initial Search, 2 = POI Search
    String name, address, shortAddress, lat, lng;
    LatLng position;
    MarkerOptions markerOption;
    Marker locationMarker;

    public LocationInfo(String[] data) {
        type = Integer.parseInt(data[0]);
        name = data[1];
        address = data[2];
        lat = data[3];
        lng = data[4];

        shortAddress = address;
        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        markerOption = new MarkerOptions().title(name).snippet(shortAddress).position(position);
        locationMarker = MapsActivity.map.addMarker(markerOption);

        if (type == 1) {
            MapsActivity.searchMarkers.add(locationMarker);
        } else if (type == 2) {
            MapsActivity.POISearchMarkers.add(locationMarker);
        }
    }

}
