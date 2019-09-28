package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    final int CREATE_PROFILE = 1;
    final int EDIT_PROFILE = 2;
    ArrayList<ProfileInfo> profiles = new ArrayList<>();
    RecyclerView.Adapter mAdapter = new ProfileActivity.MyAdapter(profiles);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_ui);
        getSupportActionBar().hide();

        // References
        final FloatingActionButton fab_profile = findViewById(R.id.profile_create);

        // Click Listener
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

        // Create a profile
        if (requestCode == CREATE_PROFILE && resultCode == RESULT_OK) {
            profiles.add(new ProfileInfo(data));
        }

        // Editing an existing profile
        if (requestCode == EDIT_PROFILE && resultCode == RESULT_OK) {
            profiles.set(data.getIntExtra("P_position", -1), new ProfileInfo(data));
        }

        mAdapter.notifyDataSetChanged();
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
            public Button profile_delete;
            public Button profile_edit;

            public MyViewHolder(View v) {
                super(v);
                profile_name = v.findViewById(R.id.profile_name);
                profile_text_numOfPlaces = v.findViewById(R.id.profile_text_numOfPlaces);
                profile_text_budget = v.findViewById(R.id.profile_text_budget);
                profile_text_time = v.findViewById(R.id.profile_text_time);
                profile_tags = v.findViewById(R.id.profile_tags);
                profile_picture = v.findViewById(R.id.profile_picture);
                profile_edit = v.findViewById(R.id.profile_edit);
                profile_delete = v.findViewById(R.id.profile_delete);
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

        // Replace a View (by Layout Manager)
        @Override
        public void onBindViewHolder(final ProfileActivity.MyAdapter.MyViewHolder holder, int position) {

            // Display related information onto each profile
            holder.profile_name.setText(data.get(position).name);
            holder.profile_text_numOfPlaces.setText("Places to Visit: " + data.get(position).numOfPlaces);
            holder.profile_text_budget.setText("Budget ($): " + data.get(position).budget);
            holder.profile_text_time.setText("Time Allowed (Hrs): " + data.get(position).time);
            // holder.profile_picture.setImageBitmap(data.get(position).icon);

            String tags_description = "Visit: ";
            for (String x :data.get(position).tags) {
                tags_description += x + ", ";
            }
            holder.profile_tags.setText(tags_description);

            // Click Listeners

            // It will pass current profile's information into a ProfileCreateActivity
            holder.profile_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileCreateActivity.class);
                    intent.putExtra("PC_name", profiles.get(holder.getAdapterPosition()).name);
                    intent.putStringArrayListExtra("PC_tags", profiles.get(holder.getAdapterPosition()).tags);
                    intent.putExtra("PC_numOfPlaces", profiles.get(holder.getAdapterPosition()).numOfPlaces);

                    if (profiles.get(holder.getAdapterPosition()).time.equals("Unlimited")) {
                        intent.putExtra("PC_time", "Unlimited");
                    } else {
                        intent.putExtra("PC_time", profiles.get(holder.getAdapterPosition()).time);
                    }

                    if (profiles.get(holder.getAdapterPosition()).budget.equals("Unlimited")) {
                        intent.putExtra("PC_budget", "Unlimited");
                    }
                    intent.putExtra("PC_budget", profiles.get(holder.getAdapterPosition()).budget);
                    intent.putExtra("PC_position", holder.getAdapterPosition());
                    startActivityForResult(intent, EDIT_PROFILE);
                }
            });

            // Prompts a warning box about deleting the selected profile
            holder.profile_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder ADbuilder = new AlertDialog.Builder(ProfileActivity.this);
                    ADbuilder.setMessage("Are you sure you want to delete this profile?");
                    ADbuilder.setCancelable(true);

                    ADbuilder.setPositiveButton(
                            "Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    // Remove Data
                                    data.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), data.size());
                                }
                            });

                    ADbuilder.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = ADbuilder.create();
                    alert.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
}
