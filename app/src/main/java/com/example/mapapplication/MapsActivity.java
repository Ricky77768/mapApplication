package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static GoogleMap map;
    public static Marker searchCenter;
    public static Marker singleSearchMarker;
    public static String searchURL;
    public static ArrayList<Marker> searchMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_ui);

        // References
        final RecyclerView location_list = findViewById(R.id.location_list);
        final EditText input_location = findViewById(R.id.input_location);
        final FloatingActionButton fab_settings = findViewById(R.id.fab_settings);
        final FloatingActionButton fab_profiles = findViewById(R.id.fab_profiles);
        final FloatingActionButton fab_help = findViewById(R.id.fab_help);
        final FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);
        final FloatingActionButton fab_search = findViewById(R.id.fab_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Initialize the map
        mapFragment.getMapAsync(this);

        // Modify certain Views
        location_list.setVisibility(View.INVISIBLE);
        fab_marker_delete.setVisibility(View.INVISIBLE);

        // Click Listeners
        fab_profiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
                fab_profiles.setEnabled(false);
                fab_settings.setEnabled(false);
                fab_help.setEnabled(false);
                fab_profiles.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_profiles.setEnabled(true);
                        fab_settings.setEnabled(true);
                        fab_help.setEnabled(true);
                    }
                }, 500);
            }
        });

        fab_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
                fab_profiles.setEnabled(false);
                fab_settings.setEnabled(false);
                fab_help.setEnabled(false);
                fab_profiles.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_profiles.setEnabled(true);
                        fab_settings.setEnabled(true);
                        fab_help.setEnabled(true);
                    }
                }, 500);
            }
        });

        fab_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_profiles.setEnabled(false);
                fab_settings.setEnabled(false);
                fab_help.setEnabled(false);
                fab_profiles.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_profiles.setEnabled(true);
                        fab_settings.setEnabled(true);
                        fab_help.setEnabled(true);
                    }
                }, 500);

                AlertDialog.Builder ADbuilder = new AlertDialog.Builder(MapsActivity.this);
                ADbuilder.setMessage("Tap anywhere on a map to get search results nearby the dropped pin (if there are any). You can drag the marker around, and delete it using the garbage can button");
                ADbuilder.setCancelable(true);

                ADbuilder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = ADbuilder.create();
                alert.show();
            }
        });

        fab_marker_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchCenter.remove();
                searchCenter = null;
                fab_marker_delete.setVisibility(View.INVISIBLE);
            }
        });

        fab_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_search.setEnabled(false);
                fab_search.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_search.setEnabled(true);
                    }
                }, 500);

                // Replace spaces in search input with %20 to support spaces in search term
                String search_result = input_location.getText().toString();
                search_result = search_result.trim();
                search_result = search_result.replaceAll("\\s", "%20");

                // Create Search URL
                searchURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
                searchURL += search_result;
                if (searchCenter == null) {
                    // Dummy Location (In the middle of nowhere)
                    searchURL += "&location=35,-170";
                } else {
                    searchURL += "&location=" + searchCenter.getPosition().latitude + "," + searchCenter.getPosition().longitude;
                }
                searchURL += "&radius=50000&key=" + getString(R.string.google_maps_key);

                if (location_list.getVisibility() == View.INVISIBLE) {

                    // Start file download
                    DownloadLocation downloadLocation = new DownloadLocation();
                    downloadLocation.execute(searchURL);
                    fab_search.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    location_list.setHasFixedSize(false);

                    // Create LayoutManager
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    location_list.setLayoutManager(mLayoutManager);

                } else {
                    location_list.setVisibility(View.INVISIBLE);
                    fab_search.setImageResource(android.R.drawable.ic_menu_search);
                    for (Marker x : searchMarkers) {
                        x.remove();
                    }
                    searchMarkers = new ArrayList<>();
                }

            }
        });

    }

    @Override
    // If RecycleView is open, close it instead. Else, close the app.
    public void onBackPressed() {
        RecyclerView location_list = findViewById(R.id.location_list);
        FloatingActionButton fab_search = findViewById(R.id.fab_search);
        if (location_list.getVisibility() == View.VISIBLE) {
            location_list.setVisibility(View.INVISIBLE);
            fab_search.setImageResource(android.R.drawable.ic_menu_search);
            for (Marker x : searchMarkers) {
                x.remove();
            }
            searchMarkers = new ArrayList<>();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        final FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Add a marker and a search radius
            @Override
            public void onMapClick(LatLng latLng) {
                if (searchCenter != null) {
                    searchCenter.remove();
                }
                searchCenter = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                searchCenter.setDraggable(true);
                fab_marker_delete.setVisibility(View.VISIBLE);
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) { }

            @Override
            public void onMarkerDrag(Marker marker) { }

            @Override
            public void onMarkerDragEnd(Marker marker) { }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        // Move and zoom the camera to current location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
    }

    // Adapter for RecycleView
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<LocationInfo> dataSet;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView location_name;
            public TextView location_otherinfo;
            public ImageView location_picture;

            public MyViewHolder(View v) {
                super(v);
                location_name = v.findViewById(R.id.location_name);
                location_otherinfo = v.findViewById(R.id.location_otherinfo);
                location_picture = v.findViewById(R.id.location_picture);
            }
        }

        // Provide data to initialize
        public MyAdapter(ArrayList<LocationInfo> myDataset) {
            dataSet = myDataset;
        }

        // Create new View (by Layout Manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace a View (by Layout Manager) (position is the current index of dataset based on which View)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.location_name.setText(dataSet.get(position).name);
            holder.location_otherinfo.setText(dataSet.get(position).address);
            holder.location_picture.setImageResource(R.drawable.ic_launcher_background);
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    // File Download
    private class DownloadLocation extends AsyncTask<String, Integer, String> {
        protected String doInBackground (String... urls) {
            int count = 0;
            try {
                URL url = new URL(urls[0].toString());
                URLConnection connection = url.openConnection();
                connection.connect();

                // Useful for progress bar
                int lengthOfFile = connection.getContentLength();

                // Download file from URL(s)
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/search_data.json", false);

                byte[] data = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {

                    // Publishing the progress. After this onProgressUpdate will be called
                    publishProgress(0);

                    // Write data to the file
                    output.write(data, 0, count);
                }

                // Flushing output and Closing streams
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {}
            return null;
        }
        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}

        @Override
        protected void onPostExecute(String s) {
            try {
                parseInfo();
            } catch (Exception e) {
                Log.e("Ricky", e.getMessage());
            }
        }

    }

    // Function to parse single location search data
    public void parseInfo() throws Exception {
        String name, address, lat, lng;
        RecyclerView location_list = findViewById(R.id.location_list);
        FloatingActionButton fab_search = findViewById(R.id.fab_search);

        // Convert file to JSON String
        File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
        File data = new File(path + "/search_data.json");
        FileInputStream iStream = new FileInputStream(data);
        String info = convertStreamToString(iStream);
        iStream.close();

        JSONObject reader = new JSONObject(info);
        ArrayList<LocationInfo> searchData = new ArrayList<>();
        if (reader.getString("status").equals("OK")) {
            JSONArray resultArr = reader.getJSONArray("results");

            Log.d("Ricky", resultArr.length() + " xdddd");

            for (int i = 0; i < resultArr.length(); i++) {
                JSONObject result = resultArr.getJSONObject(i);
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                name = result.getString("name");
                address = result.getString("formatted_address");
                lat = String.valueOf(location.getDouble("lat"));
                lng = String.valueOf(location.getDouble("lng"));

                String[] fields = new String[4];
                fields[0] = name;
                fields[1] = address;
                fields[2] = lat;
                fields[3] = lng;

                searchData.add(new LocationInfo(fields));
            }

            // Create/Specify Adapters
            RecyclerView.Adapter mAdapter = new MyAdapter(searchData);
            location_list.setAdapter(mAdapter);
            location_list.setVisibility(View.VISIBLE);
        } else {
            fab_search.setImageResource(android.R.drawable.ic_menu_search);
            AlertDialog.Builder ADbuilder = new AlertDialog.Builder(MapsActivity.this);
            ADbuilder.setMessage("An Error Occured (" + reader.getString("status") + ")");
            ADbuilder.setCancelable(true);

            ADbuilder.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = ADbuilder.create();
            alert.show();
        }
    }

    // Function to convert file to a String
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
