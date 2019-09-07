package com.example.mapapplication;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_ui);

        // Get References
        RecyclerView location_list = findViewById(R.id.location_list);
        final Button dropdown_settings = findViewById(R.id.dropdown_settings);
        final Button dropdown_profiles = findViewById(R.id.dropdown_profiles);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Hides map before current location is found and zoomed in
        showHideFragment(getSupportFragmentManager().findFragmentById(R.id.map));

        // Initialize the map
        mapFragment.getMapAsync(this);



        // Hide certain Views
        location_list.setVisibility(View.INVISIBLE);
        dropdown_profiles.setVisibility(View.INVISIBLE);
        dropdown_settings.setVisibility(View.INVISIBLE);

        dropdown_profiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
                dropdown_profiles.setEnabled(false);
                dropdown_settings.setEnabled(false);
                dropdown_profiles.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropdown_profiles.setEnabled(true);
                        dropdown_settings.setEnabled(true);
                    }
                }, 500);
            }
        });

        dropdown_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
                dropdown_profiles.setEnabled(false);
                dropdown_settings.setEnabled(false);
                dropdown_profiles.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dropdown_profiles.setEnabled(true);
                        dropdown_settings.setEnabled(true);
                    }
                }, 500);
            }
        });

        FloatingActionButton fab_extra_functions = findViewById(R.id.button_extra_functions);
        fab_extra_functions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showHideFragment(getSupportFragmentManager().findFragmentById(R.id.map));
                if (dropdown_profiles.getVisibility() == View.VISIBLE) {
                    dropdown_profiles.setVisibility(View.INVISIBLE);
                    dropdown_settings.setVisibility(View.INVISIBLE);
                } else {
                    dropdown_profiles.setVisibility(View.VISIBLE);
                    dropdown_settings.setVisibility(View.VISIBLE);
                }
            }
        });

        // EventListener for "search" button, brings up the previously hidden RecycleView
        Button search = findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TEMPORARY CODE
                LocationInfo[] test = new LocationInfo[5];
                test[0] = new LocationInfo();
                test[1] = new LocationInfo();
                test[2] = new LocationInfo();
                test[3] = new LocationInfo();
                test[4] = new LocationInfo();
                // TEMPORARY CODE

                RecyclerView location_list = findViewById(R.id.location_list);
                EditText input_location = findViewById(R.id.input_location);
                location_list.setVisibility(View.VISIBLE);
                location_list.setHasFixedSize(false);

                // Create LayoutManager
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                location_list.setLayoutManager(mLayoutManager);

                // Create/Specify Adapters
                RecyclerView.Adapter mAdapter = new MyAdapter(test, input_location.getText().toString());
                location_list.setAdapter(mAdapter);
            }
        });
    }

    @Override
    // If RecycleView is open, close it. Else exits the app.
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

        final Button dropdown_profiles = findViewById(R.id.dropdown_profiles);
        final Button dropdown_settings = findViewById(R.id.dropdown_settings);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                dropdown_profiles.setVisibility(View.INVISIBLE);
                dropdown_settings.setVisibility(View.INVISIBLE);
            }
        });
        // TODO: Find a way for camera to move to current location automatically on start
    }

    // TODO: fragment is NULL
    public void showHideFragment(final androidx.fragment.app.Fragment fragment) {
        androidx.fragment.app.FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

        //TEST
            Log.d("Ricky", fragTransaction.toString());
        //TEST

        fragTransaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);

        if (fragment.isHidden()) {
            fragTransaction.show(fragment);
        } else {
            fragTransaction.hide(fragment);
        }
        fragTransaction.commit();
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
}
