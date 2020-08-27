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

        // Save all profiles
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

        // Load all profiles
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
            setIcon(data.get(position).icon, holder);

            // Disables select profile button if already selected
            if (data.get(position).selected) { holder.profile_list_select.setEnabled(false); }

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

    // Set Icon
    public void setIcon(int index, ProfileActivity.MyAdapter.MyViewHolder holder) {
        switch (index) {
            case 1:
                holder.profile_list_picture.setImageResource(R.drawable.air_balloon);
                break;
            case 2:
                holder.profile_list_picture.setImageResource(R.drawable.aircraft);
                break;
            case 3:
                holder.profile_list_picture.setImageResource(R.drawable.anchor);
                break;
            case 4:
                holder.profile_list_picture.setImageResource(R.drawable.ancient_temple);
                break;
            case 5:
                holder.profile_list_picture.setImageResource(R.drawable.bag);
                break;
            case 6:
                holder.profile_list_picture.setImageResource(R.drawable.baggage);
                break;
            case 7:
                holder.profile_list_picture.setImageResource(R.drawable.barbeque);
                break;
            case 8:
                holder.profile_list_picture.setImageResource(R.drawable.beach_shoes);
                break;
            case 9:
                holder.profile_list_picture.setImageResource(R.drawable.bed);
                break;
            case 10:
                holder.profile_list_picture.setImageResource(R.drawable.binocular);
                break;
            case 11:
                holder.profile_list_picture.setImageResource(R.drawable.bird);
                break;
            case 12:
                holder.profile_list_picture.setImageResource(R.drawable.boat);
                break;
            case 13:
                holder.profile_list_picture.setImageResource(R.drawable.bycicle);
                break;
            case 14:
                holder.profile_list_picture.setImageResource(R.drawable.cafe);
                break;
            case 15:
                holder.profile_list_picture.setImageResource(R.drawable.call_bell);
                break;
            case 16:
                holder.profile_list_picture.setImageResource(R.drawable.camera);
                break;
            case 17:
                holder.profile_list_picture.setImageResource(R.drawable.campfire);
                break;
            case 18:
                holder.profile_list_picture.setImageResource(R.drawable.camping);
                break;
            case 19:
                holder.profile_list_picture.setImageResource(R.drawable.car);
                break;
            case 20:
                holder.profile_list_picture.setImageResource(R.drawable.cocktail);
                break;
            case 21:
                holder.profile_list_picture.setImageResource(R.drawable.compass);
                break;
            case 22:
                holder.profile_list_picture.setImageResource(R.drawable.credit_card);
                break;
            case 23:
                holder.profile_list_picture.setImageResource(R.drawable.cruise);
                break;
            case 24:
                holder.profile_list_picture.setImageResource(R.drawable.currency_exchange);
                break;
            case 25:
                holder.profile_list_picture.setImageResource(R.drawable.direction);
                break;
            case 26:
                holder.profile_list_picture.setImageResource(R.drawable.diving);
                break;
            case 27:
                holder.profile_list_picture.setImageResource(R.drawable.documents);
                break;
            case 28:
                holder.profile_list_picture.setImageResource(R.drawable.dolphin);
                break;
            case 29:
                holder.profile_list_picture.setImageResource(R.drawable.earth);
                break;
            case 30:
                holder.profile_list_picture.setImageResource(R.drawable.exotic_food);
                break;
            case 31:
                holder.profile_list_picture.setImageResource(R.drawable.gopro);
                break;
            case 32:
                holder.profile_list_picture.setImageResource(R.drawable.guide);
                break;
            case 33:
                holder.profile_list_picture.setImageResource(R.drawable.guitar);
                break;
            case 34:
                holder.profile_list_picture.setImageResource(R.drawable.hang_glider);
                break;
            case 35:
                holder.profile_list_picture.setImageResource(R.drawable.hotel);
                break;
            case 36:
                holder.profile_list_picture.setImageResource(R.drawable.island);
                break;
            case 37:
                holder.profile_list_picture.setImageResource(R.drawable.lantern);
                break;
            case 38:
                holder.profile_list_picture.setImageResource(R.drawable.lighthouse);
                break;
            case 39:
                holder.profile_list_picture.setImageResource(R.drawable.map);
                break;
            case 40:
                holder.profile_list_picture.setImageResource(R.drawable.marine_star);
                break;
            case 41:
                holder.profile_list_picture.setImageResource(R.drawable.mountains);
                break;
            case 42:
                holder.profile_list_picture.setImageResource(R.drawable.photo_cards);
                break;
            case 43:
                holder.profile_list_picture.setImageResource(R.drawable.picnic);
                break;
            case 44:
                holder.profile_list_picture.setImageResource(R.drawable.plane);
                break;
            case 45:
                holder.profile_list_picture.setImageResource(R.drawable.pool);
                break;
            case 46:
                holder.profile_list_picture.setImageResource(R.drawable.quadcopter);
                break;
            case 47:
                holder.profile_list_picture.setImageResource(R.drawable.shoes);
                break;
            case 48:
                holder.profile_list_picture.setImageResource(R.drawable.shopping);
                break;
            case 49:
                holder.profile_list_picture.setImageResource(R.drawable.sun);
                break;
            case 50:
                holder.profile_list_picture.setImageResource(R.drawable.sun_protection);
                break;
            case 51:
                holder.profile_list_picture.setImageResource(R.drawable.sunglasses);
                break;
            case 52:
                holder.profile_list_picture.setImageResource(R.drawable.tickets);
                break;
            case 53:
                holder.profile_list_picture.setImageResource(R.drawable.trailer);
                break;
            case 54:
                holder.profile_list_picture.setImageResource(R.drawable.train);
                break;
        }
    }

}
