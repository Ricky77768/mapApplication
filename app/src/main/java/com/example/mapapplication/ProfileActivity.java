package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    final int CREATE_PROFILE = 2;
    final int EDIT_PROFILE = 3;
    public static ArrayList<ProfileInfo> profiles = new ArrayList<>();
    RecyclerView.Adapter mAdapter = new ProfileActivity.MyAdapter(profiles);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_ui);
        getSupportActionBar().setTitle("Profiles");

        // References
        final FloatingActionButton fab_profile_create = findViewById(R.id.profile_create);

        // Click Listener
        fab_profile_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab_profile_create.setEnabled(false);
                fab_profile_create.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab_profile_create.setEnabled(true);
                    }
                }, 500);
                Intent intent = new Intent(ProfileActivity.this, ProfileCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        sharedPref.edit().clear().commit();
        Gson gson = new Gson();

        for (int i = 0; i < profiles.size(); i++) {
            String profile = gson.toJson(profiles.get(i));
            editor.putString("profile" + i, profile);
        }

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new Gson();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int index = 0;
        String profile = "";

        profiles.clear();
        while (true) {
            profile = sharedPref.getString("profile" + index, null);
            if (profile == null) { break; }
            profiles.add(gson.fromJson(profile, ProfileInfo.class));
            index++;
        }

        RecyclerView profile_list = findViewById(R.id.profile_list);
        profile_list.setHasFixedSize(false);

        // Check if the function is triggered by a "profile create" or "profile edit" or screen refresh
        if (ProfileCreateActivity.status == CREATE_PROFILE) {
            profiles.add(new ProfileInfo(ProfileCreateActivity.savedData));
            ProfileCreateActivity.status = 0;
        } else if (ProfileCreateActivity.status == EDIT_PROFILE) {
            profiles.set(ProfileCreateActivity.savedData.getIntExtra("P_position", -1), new ProfileInfo(ProfileCreateActivity.savedData));
            ProfileCreateActivity.status = 0;
        }

        // Create LayoutManager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        profile_list.setLayoutManager(mLayoutManager);
        profile_list.setAdapter(mAdapter);
    }

    // Adapter for RecycleView
    public class MyAdapter extends RecyclerView.Adapter<ProfileActivity.MyAdapter.MyViewHolder> {
        private ArrayList<ProfileInfo> data;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView profile_list_name;
            public TextView profile_list_text_numOfPlaces;
            public TextView profile_list_text_time;
            public TextView profile_list_text_budget;
            public TextView profile_list_text_rating;
            public TextView profile_list_tags;
            public ImageView profile_list_picture;
            public Button profile_list_delete;
            public Button profile_list_edit;
            public Button profile_list_select;

            public MyViewHolder(View v) {
                super(v);
                profile_list_name = v.findViewById(R.id.profile_list_name);
                profile_list_text_numOfPlaces = v.findViewById(R.id.profile_list_text_numOfPlaces);
                profile_list_text_budget = v.findViewById(R.id.profile_list_text_budget);
                profile_list_text_time = v.findViewById(R.id.profile_list_text_time);
                profile_list_text_rating = v.findViewById(R.id.profile_list_text_rating);
                profile_list_tags = v.findViewById(R.id.profile_list_tags);
                profile_list_edit = v.findViewById(R.id.profile_list_edit);
                profile_list_delete = v.findViewById(R.id.profile_list_delete);
                profile_list_select = v.findViewById(R.id.profile_list_select);
                profile_list_picture = v.findViewById(R.id.profile_list_picture);
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

            // Display related information to each profile
            holder.profile_list_name.setText(data.get(position).name);
            holder.profile_list_text_numOfPlaces.setText(data.get(position).numOfPlaces + " Place(s)");
            holder.profile_list_text_budget.setText(data.get(position).budget);

            if (data.get(position).time.equals("Does Not Matter")) {
                holder.profile_list_text_time.setText("Does Not Matter");
            } else {
                holder.profile_list_text_time.setText(data.get(position).time + " Hour(s)");
            }

            if (data.get(position).rating.equals("Does Not Matter")) {
                holder.profile_list_text_rating.setText("Does Not Matter");
            } else {
                holder.profile_list_text_rating.setText(data.get(position).rating + " Star(s)");
            }

            String tags_description = "Visit: ";
            for (String x :data.get(position).tags) {
                tags_description += x + ", ";
            }
            holder.profile_list_tags.setText(tags_description);

            switch (data.get(position).icon) {
                case 1:
                    holder.profile_list_picture.setImageResource(R.drawable.profile_icon_food);
                    break;
                case 2:
                    holder.profile_list_picture.setImageResource(R.drawable.profile_icon_sports);
                    break;
                case 3:
                    holder.profile_list_picture.setImageResource(R.drawable.profile_icon_nightlife);
                    break;
                case 4:
                    holder.profile_list_picture.setImageResource(R.drawable.profile_icon_sightseeing);
                    break;
            }

            // It will pass current profile's information into a ProfileCreateActivity
            holder.profile_list_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.profile_list_select.setEnabled(false);
                    holder.profile_list_edit.setEnabled(false);
                    holder.profile_list_delete.setEnabled(false);
                    holder.profile_list_select.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.profile_list_select.setEnabled(true);
                            holder.profile_list_edit.setEnabled(true);
                            holder.profile_list_delete.setEnabled(true);
                        }
                    }, 500);

                    Intent intent = new Intent(ProfileActivity.this, ProfileCreateActivity.class);
                    intent.putExtra("P_edit_name", profiles.get(holder.getAdapterPosition()).name);
                    intent.putExtra("P_edit_icon", profiles.get(holder.getAdapterPosition()).icon);
                    intent.putExtra("P_edit_numOfPlaces", profiles.get(holder.getAdapterPosition()).numOfPlaces);
                    intent.putExtra("P_edit_time", profiles.get(holder.getAdapterPosition()).rawTime);
                    intent.putExtra("P_edit_budget", profiles.get(holder.getAdapterPosition()).rawBudget);
                    intent.putExtra("P_edit_rating", profiles.get(holder.getAdapterPosition()).rawRating);
                    intent.putExtra("P_edit_position", holder.getAdapterPosition());
                    intent.putStringArrayListExtra("P_edit_tags", profiles.get(holder.getAdapterPosition()).tags);
                    startActivity(intent);
                }
            });

            // Prompts a warning box about deleting the selected profile
            holder.profile_list_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.profile_list_select.setEnabled(false);
                    holder.profile_list_edit.setEnabled(false);
                    holder.profile_list_delete.setEnabled(false);
                    holder.profile_list_select.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.profile_list_select.setEnabled(true);
                            holder.profile_list_edit.setEnabled(true);
                            holder.profile_list_delete.setEnabled(true);
                        }
                    }, 500);

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

            holder.profile_list_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.profile_list_select.setEnabled(false);
                    holder.profile_list_edit.setEnabled(false);
                    holder.profile_list_delete.setEnabled(false);
                    holder.profile_list_select.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.profile_list_select.setEnabled(true);
                            holder.profile_list_edit.setEnabled(true);
                            holder.profile_list_delete.setEnabled(true);
                        }
                    }, 500);

                    for (ProfileInfo x : data) {
                        x.selected = false;
                    }
                    data.get(holder.getAdapterPosition()).selected = true;
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }

}
