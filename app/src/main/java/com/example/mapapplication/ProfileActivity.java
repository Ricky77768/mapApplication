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

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_ui);
        getSupportActionBar().hide();

        // EventListener for FAB
        FloatingActionButton fab_profile = findViewById(R.id.profile_create);
        fab_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileCreateActivity.class);
                startActivity(intent);
            }
        });

        // TEMPORARY CODE
        ProfileInfo[] test2 =  new ProfileInfo[15];
        test2[0] = new ProfileInfo();
        test2[1] = new ProfileInfo();
        test2[2] = new ProfileInfo();
        test2[3] = new ProfileInfo();
        test2[4] = new ProfileInfo();
        test2[5] = new ProfileInfo();
        test2[6] = new ProfileInfo();
        test2[7] = new ProfileInfo();
        test2[8] = new ProfileInfo();
        test2[9] = new ProfileInfo();
        test2[10] = new ProfileInfo();
        test2[11] = new ProfileInfo();
        test2[12] = new ProfileInfo();
        test2[13] = new ProfileInfo();
        test2[14] = new ProfileInfo();
        // TEMPORARY CODE

        RecyclerView profile_list = findViewById(R.id.profile_list);
        profile_list.setHasFixedSize(false);

        // Create LayoutManager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        profile_list.setLayoutManager(mLayoutManager);

        // Create/Specify Adapters
        RecyclerView.Adapter mAdapter = new ProfileActivity.MyAdapter(test2);
        profile_list.setAdapter(mAdapter);
    }

    // Adapter for RecycleView
    public class MyAdapter extends RecyclerView.Adapter<ProfileActivity.MyAdapter.MyViewHolder> {
        private ProfileInfo[] sample;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView profile_name;
            public TextView profile_otherInfo;
            public ImageView profile_picture;

            public MyViewHolder(View v) {
                super(v);
                profile_name = v.findViewById(R.id.profile_name);
                profile_otherInfo = v.findViewById(R.id.profile_otherinfo);
                profile_picture = v.findViewById(R.id.profile_picture);
            }
        }

        // Provide data to initialize
        public MyAdapter(ProfileInfo[] myDataset) {
            sample = myDataset;
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
            holder.profile_name.setText(sample[position].name + position);
            holder.profile_otherInfo.setText(sample[position].description);
            holder.profile_picture.setImageResource(R.drawable.ic_launcher_background);
        }

        @Override
        public int getItemCount() {
            return sample.length;
        }
    }
}
