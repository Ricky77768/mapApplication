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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static GoogleMap map;
    public static Marker currentMarker;
    public static Marker singleSearchMarker;
    public static Marker[] nearbySearchMarkers;
    public static Circle currentCircle;
    public static CircleOptions circle;
    public static String searchURL;
    public static int searchRadius = 10000;

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
        final FloatingActionButton fab_search = findViewById(R.id.fab_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Initialize the map
        mapFragment.getMapAsync(this);

        // Modify certain Views
        location_list.setVisibility(View.INVISIBLE);

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
                AlertDialog.Builder ADbuilder = new AlertDialog.Builder(MapsActivity.this);
                ADbuilder.setMessage("Drop a pin at a location to conduct a nearby search.");
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

        fab_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Replace spaces in search input with %20 to support spaces in search term
                String search_result = input_location.getText().toString();
                search_result = search_result.trim();
                search_result = search_result.replaceAll("\\s", "%20");

                searchURL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=";
                searchURL += search_result;
                searchURL += "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=";
                searchURL += getString(R.string.google_maps_key);

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
                    singleSearchMarker.remove();
                    fab_search.setImageResource(android.R.drawable.ic_menu_search);
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
            singleSearchMarker.remove();
            location_list.setVisibility(View.INVISIBLE);
            fab_search.setImageResource(android.R.drawable.ic_menu_search);
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
        circle = new CircleOptions()
                .radius(searchRadius)
                .strokeColor(Color.BLACK)
                .fillColor(0x220000FF)
                .strokeWidth(2);

        // If extra function buttons are displayed, tapping the map will collapse them
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Add a marker and a search radius
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                if (currentCircle != null) {
                    currentCircle.remove();
                }

                circle.center(latLng);
                circle.radius(searchRadius);
                currentCircle = map.addCircle(circle);
                currentMarker = map.addMarker(new MarkerOptions().position(latLng));
                currentMarker.setDraggable(true);
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                currentCircle.remove();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                circle.center(marker.getPosition());
                currentCircle = map.addCircle(circle);
            }

            @Override
            public void onMarkerDrag(Marker marker) {}
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
        private LocationInfo[] dataSet;

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
        public MyAdapter(LocationInfo[] myDataset) {
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
            holder.location_name.setText(dataSet[position].name);
            holder.location_otherinfo.setText(dataSet[position].address);
            holder.location_picture.setImageResource(R.drawable.ic_launcher_background);
        }

        @Override
        public int getItemCount() {
            return dataSet.length;
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
                        + "/single_location_search.json", false);

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
                parseSingleLocation();
            } catch (Exception e) {}
        }

    }

    // Function to parse single location search data
    public void parseSingleLocation() throws Exception {
        String name, address, lat, lng;
        RecyclerView location_list = findViewById(R.id.location_list);

        // Convert file to JSON String
        File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
        File data = new File(path + "/single_location_search.json");
        FileInputStream iStream = new FileInputStream(data);
        String info = convertStreamToString(iStream);
        iStream.close();

        JSONObject reader = new JSONObject(info);
        if (reader.getString("status").equals("OK")) {
            JSONArray candidatesArr = reader.getJSONArray("candidates");
            JSONObject candidates = candidatesArr.getJSONObject(0);
            JSONObject geometry = candidates.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");

            name = candidates.getString("name");
            address = candidates.getString("formatted_address");
            lat = String.valueOf(location.getDouble("lat"));
            lng = String.valueOf(location.getDouble("lng"));

        } else {
            name = "ERROR - " + reader.getString("status");
            address = "ERROR - " + reader.getString("status");
            lat = "ERROR - " + reader.getString("status");
            lng = "ERROR - " + reader.getString("status");
        }

        String[] fields = new String[4];
        fields[0] = name;
        fields[1] = address;
        fields[2] = lat;
        fields[3] = lng;

        LocationInfo[] singleSearch = new LocationInfo[1];
        singleSearch[0] = new LocationInfo(fields);

        // Create/Specify Adapters
        RecyclerView.Adapter mAdapter = new MyAdapter(singleSearch);
        location_list.setAdapter(mAdapter);
        location_list.setVisibility(View.VISIBLE);
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

    // Change circle that indicates search radius
    public static void changeRadius() {
        circle.radius(searchRadius);
        if (currentCircle != null) {
            currentCircle.remove();
            currentCircle = map.addCircle(circle);
        }
    }

}
