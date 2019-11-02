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
import android.os.Bundle;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static GoogleMap map;
    public static Marker currentMarker;
    public static Circle currentCircle;
    public static CircleOptions circle;
    public static int searchRadius = 10000;

    public String searchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_ui);

        // References
        RecyclerView location_list = findViewById(R.id.location_list);
        EditText input_location = findViewById(R.id.input_location);
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
                // TEMPORARY CODE
                LocationInfo[] test = new LocationInfo[3];
                test[0] = new LocationInfo();
                test[1] = new LocationInfo();
                test[2] = new LocationInfo();
                // TEMPORARY CODE
                RecyclerView location_list = findViewById(R.id.location_list);

                if (location_list.getVisibility() == View.INVISIBLE) {
                    location_list.setVisibility(View.VISIBLE);
                    fab_search.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

                    EditText input_location = findViewById(R.id.input_location);
                    location_list.setVisibility(View.VISIBLE);
                    location_list.setHasFixedSize(false);

                    // Create LayoutManager
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    location_list.setLayoutManager(mLayoutManager);

                    // Create/Specify Adapters
                    RecyclerView.Adapter mAdapter = new MyAdapter(test, input_location.getText().toString());
                    location_list.setAdapter(mAdapter);

                    // Start file download

                } else {
                    location_list.setVisibility(View.INVISIBLE);
                    fab_search.setImageResource(android.R.drawable.ic_menu_search);
                }

            }
        });
    }

    @Override
    // If RecycleView is open, close it instead. Else, close the app.
    public void onBackPressed() {
        RecyclerView location_list = findViewById(R.id.location_list);
        if (location_list.getVisibility() == View.VISIBLE) {
            location_list.setVisibility(View.INVISIBLE);
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
        private LocationInfo[] sample;
        private String userInput;

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
        public MyAdapter(LocationInfo[] myDataset, String input) {
            sample = myDataset;
            userInput = input;
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
            holder.location_name.setText(sample[position].name + position);
            holder.location_otherinfo.setText(sample[position].description + userInput);
            holder.location_picture.setImageResource(R.drawable.ic_launcher_background);
        }

        @Override
        public int getItemCount() {
            return sample.length;
        }
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
