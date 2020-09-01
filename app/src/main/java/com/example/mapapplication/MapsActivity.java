package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

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
    public static Marker POISearchCentre;
    public static Marker originalDestination;
    public static String searchURL;
    public static ArrayList<Marker> searchMarkers = new ArrayList<>();
    public static ArrayList<Marker> POISearchCentres = new ArrayList<>();
    public static ArrayList<Marker> POISearchMarkers = new ArrayList<>();
    public static ArrayList<LocationInfo> searchData = new ArrayList<>();
    public static ArrayList<LocationInfo> POISearchData = new ArrayList<>();
    public static ProfileInfo currentProfile;
    public static LatLng currentLocation;
    boolean canPutMarker = true;

    // 0 = Awaiting search
    // 1 = Awaiting destination selection
    // 2 = Awaiting POI selections
    int appState = 0;

    // Runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create activity's view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_ui);

        // References
        final RecyclerView location_list = findViewById(R.id.location_list);
        final EditText input_location = findViewById(R.id.input_location);
        final TextView search_tip = findViewById(R.id.search_tip);
        final TextView profile_name = findViewById(R.id.profile_name);
        final ImageButton button_profiles = findViewById(R.id.button_profiles);
        final FloatingActionButton fab_settings = findViewById(R.id.fab_settings);
        final FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);
        final FloatingActionButton fab_search = findViewById(R.id.fab_search);

        // Obtain SupportMapFragment and get notified when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Initialize the map
        mapFragment.getMapAsync(this);

        // Modify certain view's initial state
        location_list.setVisibility(View.INVISIBLE);
        fab_marker_delete.setVisibility(View.INVISIBLE);
        search_tip.setVisibility(View.INVISIBLE);
        location_list.setFocusable(false);

        // Load previously selected profile
        Gson gson = new Gson();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String profile = sharedPref.getString("current_profile", null);
        if (profile != null) {
            currentProfile = gson.fromJson(profile, ProfileInfo.class);
            loadProfile(currentProfile);
        } else {
            currentProfile = null;
            loadProfile(currentProfile);
        }

        // Click listeners
        profile_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                disableButtons(500);
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        button_profiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                disableButtons(500);
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        fab_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                disableButtons(500);
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        fab_marker_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (searchCenter != null) {
                    searchCenter.remove();
                    searchCenter = null;
                }
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

                // Replace all spaces in input to match API call requirements
                String search_input = input_location.getText().toString();
                search_input = search_input.trim().replaceAll("\\s", "%20");

                // Create Search URL
                searchURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
                searchURL += search_input;

                // If the search centre pin is not used, use a default location (middle of ocean)
                if (searchCenter == null) {
                    searchURL += "&location=35,-170";
                } else {
                    searchURL += "&location=" + searchCenter.getPosition().latitude + "," + searchCenter.getPosition().longitude;
                }
                searchURL += "&radius=50000&key=" + getString(R.string.google_maps_key);

                if (location_list.getVisibility() == View.INVISIBLE) {
                    appState++;

                    // Start file download
                    DownloadFile downloadFile = new DownloadFile(1);
                    downloadFile.execute(searchURL);
                    fab_search.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    location_list.setHasFixedSize(false);

                    // Create LayoutManager
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    location_list.setLayoutManager(mLayoutManager);

                } else {
                    appState--;

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

    // Runs when the activity resumes from out of focus
    @Override
    protected void onResume() {
        super.onResume();

        // References
        final TextView profile_name = findViewById(R.id.profile_name);
        final ImageButton button_profiles = findViewById(R.id.button_profiles);

        // Check which profile was previously selected and loads it
        for (ProfileInfo x : ProfileActivity.profiles) {
            if (x.selected) {
                currentProfile = x;
            }
        }
        loadProfile(currentProfile);
    }

    // Runs when the activity becomes out of focus
    @Override
    protected void onStop() {
        super.onStop();

        // Save the currently selected profile
        for (ProfileInfo x : ProfileActivity.profiles) {
            if (x.selected) {
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                sharedPref.edit().clear().commit();
                Gson gson = new Gson();
                editor.putString("current_profile" , gson.toJson(currentProfile));
                editor.apply();
            }
        }
    }

    // Functions to change the activity based on current app state
    @Override
    public void onBackPressed() {

        // References
        RecyclerView location_list = findViewById(R.id.location_list);
        FloatingActionButton fab_search = findViewById(R.id.fab_search);
        FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);
        EditText input_location = findViewById(R.id.input_location);
        TextView search_tip = findViewById(R.id.search_tip);

        // Awaiting POI selections
        if (appState == 2) {
            appState--;
            canPutMarker = true;
            if (searchCenter != null) { searchCenter.setVisible(true); }
            if (originalDestination != null) { originalDestination.remove(); }
            for (Marker x : searchMarkers) { x.setVisible(true); }
            for (Marker x : POISearchMarkers) { x.remove(); }
            POISearchMarkers = new ArrayList<>();
            POISearchData = new ArrayList<>();
            search_tip.setVisibility(View.VISIBLE);
            fab_search.setVisibility(View.VISIBLE);
            fab_marker_delete.setVisibility(View.VISIBLE);
            input_location.setVisibility(View.VISIBLE);

        // Awaiting destination selection
        } else if (appState == 1) {
            appState--;
            search_tip.setVisibility(View.INVISIBLE);
            location_list.setVisibility(View.INVISIBLE);
            fab_search.setImageResource(android.R.drawable.ic_menu_search);
            for (Marker x : searchMarkers) {
                x.remove();
            }
            searchMarkers = new ArrayList<>();
            searchData = new ArrayList<>();

        // Awaiting search
        } else if (appState == 0) {
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

        // Reference
        final FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);

        // Initial setup of the map
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        // If the user clicks on the map, a pin will be dropped
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Add a marker
            @Override
            public void onMapClick(LatLng latLng) {
                if (!canPutMarker) { return; }

                if (searchCenter != null) {
                    searchCenter.remove();
                }
                searchCenter = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                searchCenter.setDraggable(true);
                fab_marker_delete.setVisibility(View.VISIBLE);
            }
        });

        // Template functions on certain marker actions
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

            // Template functions on certain locationChange actions
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                currentLocation = userLocation;
            }

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
        currentLocation = userLocation;
    }

    // Adapter for RecycleView
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<LocationInfo> dataSet;

        // Provide a reference to the views for each data item. Complex data items may need more than one view per item
        // You provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {

            // Class variables
            public TextView location_name;
            public TextView location_otherinfo;
            public Button location_go;

            // Find views
            public MyViewHolder(View v) {
                super(v);
                location_name = v.findViewById(R.id.location_name);
                location_otherinfo = v.findViewById(R.id.location_otherinfo);
                location_go = v.findViewById(R.id.location_go);
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
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.location_name.setText(dataSet.get(position).name);
            holder.location_otherinfo.setText(dataSet.get(position).address);

            holder.location_go.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    holder.location_go.setEnabled(false);
                    holder.location_go.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.location_go.setEnabled(true);
                        }
                    }, 500);

                    // References
                    final RecyclerView location_list = findViewById(R.id.location_list);
                    final EditText input_location = findViewById(R.id.input_location);
                    final FloatingActionButton fab_marker_delete = findViewById(R.id.fab_marker_delete);
                    final FloatingActionButton fab_search = findViewById(R.id.fab_search);
                    final TextView search_tip = findViewById(R.id.search_tip);

                    // TODO: Once ready for POI selection, remove this temporary solution
                    if (appState != 1) { return; }

                    appState++;
                    canPutMarker = false;

                    // Remove search tip
                    search_tip.setVisibility(View.INVISIBLE);

                    LatLng coordinates = new LatLng(Double.parseDouble(dataSet.get(holder.getAdapterPosition()).lat), Double.parseDouble(dataSet.get(holder.getAdapterPosition()).lng));
                    originalDestination = map.addMarker(new MarkerOptions().position(coordinates));

                    if (searchCenter != null) {
                        searchCenter.setVisible(false);
                    }
                    for (Marker x : searchMarkers) {
                        x.setVisible(false);
                    }

                    fab_search.setVisibility(View.INVISIBLE);
                    fab_marker_delete.setVisibility(View.INVISIBLE);
                    input_location.setVisibility(View.INVISIBLE);

                    // Reset array to prevent duplication
                    POISearchCentres = new ArrayList<>();
                    createPOISearchCentres(currentLocation, coordinates);

                    for (int i = 0; i < POISearchCentres.size(); i++) {
                        double markerLat = POISearchCentres.get(i).getPosition().latitude;
                        double markerLong = POISearchCentres.get(i).getPosition().longitude;
                        searchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
                        searchURL += markerLat + "," + markerLong + "&radius=50000&key=" + getString(R.string.google_maps_key);
                        DownloadFile downloadFile = new DownloadFile(2);
                        downloadFile.execute(searchURL);
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    // File Download
    private class DownloadFile extends AsyncTask<String, Integer, String> {
        int type; // 1 = Initial Search, 2 = POIs Search
        public DownloadFile(int x) { type = x; }

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
                OutputStream output = new FileOutputStream(getApplicationContext().getFilesDir().toString() + "/search_data.json", false);

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

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}

        @Override
        protected void onPostExecute(String s) {
            try {
                parseSearchInfo(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Function to parse location search data (1 = initial search, 2 = POI search)
    public void parseSearchInfo(int type) throws Exception {
        String name = "", address = "", lat = "", lng = "";

        // References
        TextView search_tip = findViewById(R.id.search_tip);
        RecyclerView location_list = findViewById(R.id.location_list);
        FloatingActionButton fab_search = findViewById(R.id.fab_search);

        // Display search tip if needed
        if (type == 1) {
            if (searchCenter == null) {
                search_tip.setText(R.string.search_tip1);
            } else {
                search_tip.setText(R.string.search_tip2);
            }
            search_tip.setVisibility(View.VISIBLE);
        }

        // Convert file to JSON String
        File data = new File(getApplicationContext().getFilesDir().toString() + "/search_data.json");
        FileInputStream iStream = new FileInputStream(data);
        String info = convertStreamToString(iStream);
        iStream.close();

        JSONObject reader = new JSONObject(info);
        if (reader.getString("status").equals("OK")) {
            JSONArray resultArr = reader.getJSONArray("results");

            for (int i = 0; i < resultArr.length(); i++) {
                JSONObject result = resultArr.getJSONObject(i);

                if (type == 1) {
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    name = result.getString("name");
                    address = result.getString("formatted_address");
                    lat = String.valueOf(location.getDouble("lat"));
                    lng = String.valueOf(location.getDouble("lng"));

                } else if (type == 2) {
                    JSONArray tags = result.getJSONArray("types");

                    // Ignore results with tag "lodging" or "political"
                    if (tags.toString().contains("lodging") || tags.toString().contains("political")) {
                        Log.e("Ricky", "Illogical Tag Detected");
                        continue;
                    }

                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    name = result.getString("name");
                    address = result.getString("vicinity");
                    lat = String.valueOf(location.getDouble("lat"));
                    lng = String.valueOf(location.getDouble("lng"));
                }

                String[] fields = new String[5];
                fields[0] = Integer.toString(type);
                fields[1] = name;
                fields[2] = address;
                fields[3] = lat;
                fields[4] = lng;

                if (type == 1) {
                    searchData.add(new LocationInfo(fields));
                } else if (type == 2) {
                    POISearchData.add(new LocationInfo(fields));
                }
            }

            RecyclerView.Adapter mAdapterS = new MyAdapter(searchData);
            RecyclerView.Adapter mAdapterP = new MyAdapter(POISearchData);

            if (type == 1) {
                location_list.setAdapter(mAdapterS);
            } else { // type 2
                location_list.setAdapter(mAdapterP);
            }
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
        data.delete();
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

    // Function to load a profile
    public void loadProfile(ProfileInfo x) {
        final TextView profile_name = findViewById(R.id.profile_name);
        final ImageButton button_profiles = findViewById(R.id.button_profiles);

        if (x != null) {
            profile_name.setText(currentProfile.name);
            setIcon(currentProfile.icon);
            return;
        }

        // Default case
        profile_name.setText("*No Profile Selected*");
        setIcon(-1);
    }

    // Function to set the icon for a profile
    public void setIcon(int index) {
        final ImageButton button_profiles = findViewById(R.id.button_profiles);

        switch (index) {
            case 1:
                button_profiles.setImageResource(R.drawable.air_balloon);
                break;
            case 2:
                button_profiles.setImageResource(R.drawable.aircraft);
                break;
            case 3:
                button_profiles.setImageResource(R.drawable.anchor);
                break;
            case 4:
                button_profiles.setImageResource(R.drawable.ancient_temple);
                break;
            case 5:
                button_profiles.setImageResource(R.drawable.bag);
                break;
            case 6:
                button_profiles.setImageResource(R.drawable.baggage);
                break;
            case 7:
                button_profiles.setImageResource(R.drawable.barbeque);
                break;
            case 8:
                button_profiles.setImageResource(R.drawable.beach_shoes);
                break;
            case 9:
                button_profiles.setImageResource(R.drawable.bed);
                break;
            case 10:
                button_profiles.setImageResource(R.drawable.binocular);
                break;
            case 11:
                button_profiles.setImageResource(R.drawable.bird);
                break;
            case 12:
                button_profiles.setImageResource(R.drawable.boat);
                break;
            case 13:
                button_profiles.setImageResource(R.drawable.bycicle);
                break;
            case 14:
                button_profiles.setImageResource(R.drawable.cafe);
                break;
            case 15:
                button_profiles.setImageResource(R.drawable.call_bell);
                break;
            case 16:
                button_profiles.setImageResource(R.drawable.camera);
                break;
            case 17:
                button_profiles.setImageResource(R.drawable.campfire);
                break;
            case 18:
                button_profiles.setImageResource(R.drawable.camping);
                break;
            case 19:
                button_profiles.setImageResource(R.drawable.car);
                break;
            case 20:
                button_profiles.setImageResource(R.drawable.cocktail);
                break;
            case 21:
                button_profiles.setImageResource(R.drawable.compass);
                break;
            case 22:
                button_profiles.setImageResource(R.drawable.credit_card);
                break;
            case 23:
                button_profiles.setImageResource(R.drawable.cruise);
                break;
            case 24:
                button_profiles.setImageResource(R.drawable.currency_exchange);
                break;
            case 25:
                button_profiles.setImageResource(R.drawable.direction);
                break;
            case 26:
                button_profiles.setImageResource(R.drawable.diving);
                break;
            case 27:
                button_profiles.setImageResource(R.drawable.documents);
                break;
            case 28:
                button_profiles.setImageResource(R.drawable.dolphin);
                break;
            case 29:
                button_profiles.setImageResource(R.drawable.earth);
                break;
            case 30:
                button_profiles.setImageResource(R.drawable.exotic_food);
                break;
            case 31:
                button_profiles.setImageResource(R.drawable.gopro);
                break;
            case 32:
                button_profiles.setImageResource(R.drawable.guide);
                break;
            case 33:
                button_profiles.setImageResource(R.drawable.guitar);
                break;
            case 34:
                button_profiles.setImageResource(R.drawable.hang_glider);
                break;
            case 35:
                button_profiles.setImageResource(R.drawable.hotel);
                break;
            case 36:
                button_profiles.setImageResource(R.drawable.island);
                break;
            case 37:
                button_profiles.setImageResource(R.drawable.lantern);
                break;
            case 38:
                button_profiles.setImageResource(R.drawable.lighthouse);
                break;
            case 39:
                button_profiles.setImageResource(R.drawable.map);
                break;
            case 40:
                button_profiles.setImageResource(R.drawable.marine_star);
                break;
            case 41:
                button_profiles.setImageResource(R.drawable.mountains);
                break;
            case 42:
                button_profiles.setImageResource(R.drawable.photo_cards);
                break;
            case 43:
                button_profiles.setImageResource(R.drawable.picnic);
                break;
            case 44:
                button_profiles.setImageResource(R.drawable.plane);
                break;
            case 45:
                button_profiles.setImageResource(R.drawable.pool);
                break;
            case 46:
                button_profiles.setImageResource(R.drawable.quadcopter);
                break;
            case 47:
                button_profiles.setImageResource(R.drawable.shoes);
                break;
            case 48:
                button_profiles.setImageResource(R.drawable.shopping);
                break;
            case 49:
                button_profiles.setImageResource(R.drawable.sun);
                break;
            case 50:
                button_profiles.setImageResource(R.drawable.sun_protection);
                break;
            case 51:
                button_profiles.setImageResource(R.drawable.sunglasses);
                break;
            case 52:
                button_profiles.setImageResource(R.drawable.tickets);
                break;
            case 53:
                button_profiles.setImageResource(R.drawable.trailer);
                break;
            case 54:
                button_profiles.setImageResource(R.drawable.train);
                break;
            default:
                button_profiles.setImageResource(R.mipmap.ic_launcher_round);
                break;
        }
    }

    // Function to create POI search centre markers
    public void createPOISearchCentres(LatLng current, LatLng destination) {
        double lat1 = current.latitude;
        double long1 = current.longitude;
        double lat2 = destination.latitude;
        double long2 = destination.longitude;

        // Compute the number of markers based on distance
        int points = (int) (haversine(lat1, long1, lat2, long2) / 250) + 1;
        if (points > 10) {
           points = 10;
        }

        // Compute the distance between each POI search centre marker
        double convertlat1 = convertLat(false, lat1);
        double convertlat2 = convertLat(false, lat2);
        double convertlong1 = convertLong(false, long1);
        double convertlong2 = convertLong(false, long2);
        double latChange = (convertlat2 - convertlat1) / (points + 1);
        double longChange = (convertlong2 - convertlong1) / (points + 1);
        double curLat = convertlat1;
        double curLong = convertlong1;

        // Create the POI search centre markers
        for (int i = 0; i < points; i++) {
            curLat += latChange;
            curLong += longChange;

            POISearchCentre = map.addMarker(new MarkerOptions().position(new LatLng(convertLat(true, curLat), convertLong(true, curLong))));
            POISearchCentres.add(POISearchCentre);
            POISearchCentre.remove();
        }
    }

    // Function to convert longitude values for easier calculation
    public static double convertLong(boolean toStandard, double longValue) {
        if (toStandard) {
            if (longValue > 180) {
                return (360 - longValue) * -1;
            } else {
                return longValue;
            }

        } else {
            if (longValue < 0) {
                return 360 - (longValue * -1);
            } else {
                return longValue;
            }
        }
    }

    // Function to convert latitude values for easier calculation
    public static double convertLat(boolean toStandard, double latValue) {
        if (toStandard) {
            return latValue - 90;
        } else {
            return latValue + 90;
        }
    }

    // Function to calcualte distance between for pairs of coordinates
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6372.8; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    // Function to disable certain buttons for a brief period to prevent multiple triggers
    public void disableButtons(int millis) {

        // References
        final TextView profile_name = findViewById(R.id.profile_name);
        final ImageButton button_profiles = findViewById(R.id.button_profiles);
        final FloatingActionButton fab_settings = findViewById(R.id.fab_settings);

        profile_name.setEnabled(false);
        button_profiles.setEnabled(false);
        fab_settings.setEnabled(false);
        button_profiles.postDelayed(new Runnable() {
            @Override
            public void run() {
                profile_name.setEnabled(true);
                button_profiles.setEnabled(true);
                fab_settings.setEnabled(true);
            }
        }, millis);
    }

}
