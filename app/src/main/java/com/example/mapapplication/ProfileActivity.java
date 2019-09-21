package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    final int CREATE_PROFILE = 1;
    // ProfileInfo[] profiles = new ProfileInfo[7];
    ArrayList<ProfileInfo> profiles = new ArrayList<>();
    RecyclerView.Adapter mAdapter = new ProfileActivity.MyAdapter(profiles);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_ui);
        getSupportActionBar().hide();

        // EventListener for FAB
        final FloatingActionButton fab_profile = findViewById(R.id.profile_create);
        fab_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_profile.setEnabled(false);
                fab_profile.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_profile.setEnabled(true);
                    }
                }, 500);
                Intent intent = new Intent(ProfileActivity.this, ProfileCreateActivity.class);
                startActivityForResult(intent, CREATE_PROFILE);
            }
        });

        RecyclerView profile_list = findViewById(R.id.profile_list);
        profile_list.setHasFixedSize(false);

        // Create LayoutManager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        profile_list.setLayoutManager(mLayoutManager);
        profile_list.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_PROFILE && resultCode == RESULT_OK) {
            profiles.add(new ProfileInfo(data));
            mAdapter.notifyDataSetChanged();
        }
    }

    // Adapter for RecycleView
    public class MyAdapter extends RecyclerView.Adapter<ProfileActivity.MyAdapter.MyViewHolder> {
        private ArrayList<ProfileInfo> data;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView profile_name;
            public TextView profile_text_numOfPlaces;
            public TextView profile_text_time;
            public TextView profile_text_budget;
            public TextView profile_tags;
            public ImageView profile_picture;

            public MyViewHolder(View v) {
                super(v);
                profile_name = v.findViewById(R.id.profile_name);
                profile_text_numOfPlaces = v.findViewById(R.id.profile_text_numOfPlaces);
                profile_text_budget = v.findViewById(R.id.profile_text_budget);
                profile_text_time = v.findViewById(R.id.profile_text_time);
                profile_tags = v.findViewById(R.id.profile_tags);
                profile_picture = v.findViewById(R.id.profile_picture);
            }
        }

        // Provide data to initialize
        public MyAdapter(ArrayList<ProfileInfo> myDataset) {
            data = myDataset;
        }

        // Create new View (by Layout Manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace a View (by Layout Manager) (position is the current index of dataset based on which View)
        @Override
        public void onBindViewHolder(ProfileActivity.MyAdapter.MyViewHolder holder, int position) {
            holder.profile_name.setText(data.get(position).name);
            holder.profile_text_numOfPlaces.setText("Places to Visit: " + data.get(position).numOfPlaces);
            holder.profile_text_budget.setText("Budget ($): " + data.get(position).budget);
            holder.profile_text_time.setText("Time Allowed (Hrs): " + data.get(position).time);
            holder.profile_picture.setImageResource(R.drawable.ic_launcher_background);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
