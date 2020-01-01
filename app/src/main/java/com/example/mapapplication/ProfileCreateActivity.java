package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileCreateActivity extends AppCompatActivity {
    int icon = 1; // For icon selection
    final int SELECT_ICON = 1;

    // For passing data to ProfileActivity
    public static Intent savedData;
    public static int status;
    final int CREATE_PROFILE = 2;
    final int EDIT_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_ui);
        getSupportActionBar().hide();

        // References
        final SeekBar profile_create_seekbar_numOfPlaces = findViewById(R.id.profile_create_seekbar_numOfPlaces);
        final SeekBar profile_create_seekbar_budget = findViewById(R.id.profile_create_seekbar_budget);
        final SeekBar profile_create_seekbar_time = findViewById(R.id.profile_create_seekbar_time);
        final SeekBar profile_create_seekbar_rating = findViewById(R.id.profile_create_seekbar_rating);
        final TextView profile_create_text_numOfPlaces = findViewById(R.id.profile_create_text_numOfPlaces);
        final TextView profile_create_text_budget = findViewById(R.id.profile_create_text_budget);
        final TextView profile_create_text_time = findViewById(R.id.profile_create_text_time);
        final TextView profile_create_text_rating = findViewById(R.id.profile_create_text_rating);
        final EditText profile_create_profile_name = findViewById(R.id.profile_create_profile_name);
        final Button profile_create_save = findViewById(R.id.profile_create_save);
        final Button profile_create_cancel = findViewById(R.id.profile_create_cancel);
        final ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
        final ChipGroup profile_create_chipgroup = findViewById(R.id.profile_create_chipgroup);

        // Click Listeners
        profile_create_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                profile_create_save.setEnabled(false);
                profile_create_cancel.setEnabled(false);
                profile_create_save.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile_create_save.setEnabled(true);
                        profile_create_cancel.setEnabled(true);
                    }
                }, 500);

                // Passes all data into an intent for profile creation
                Intent data = new Intent();
                String name = profile_create_profile_name.getText().toString();
                String numOfPlaces = Integer.toString(profile_create_seekbar_numOfPlaces.getProgress());
                String budget = Integer.toString(profile_create_seekbar_budget.getProgress());
                String time = Integer.toString(profile_create_seekbar_time.getProgress());
                String rating = Integer.toString(profile_create_seekbar_rating.getProgress());

                ArrayList<String> tags = new ArrayList<>();

                // Check for tags selected
                for (int i = 0; i < profile_create_chipgroup.getChildCount(); i++) {
                    View view = profile_create_chipgroup.getChildAt(i);
                    if (view instanceof Chip) {
                        if (((Chip) view).isChecked()) {
                            tags.add(((Chip) view).getText().toString());
                        }
                    }
                }

                data.putExtra("P_name", name);
                data.putExtra("P_icon", icon);
                data.putExtra("P_numOfPlaces", numOfPlaces);
                data.putExtra("P_time",time);
                data.putExtra("P_budget", budget);
                data.putExtra("P_rating", rating);
                data.putStringArrayListExtra("P_tags", tags);

                // If editing a profile, then get the position of the profile in the RecycleView
                Intent previousIntent = getIntent();
                if (previousIntent.hasExtra("P_edit_position")) {
                    data.putExtra("P_position", previousIntent.getIntExtra("P_edit_position", -1));
                    status = EDIT_PROFILE;
                } else {
                    status = CREATE_PROFILE;
                }

                savedData = data;

                setResult(RESULT_OK, data);
                finish();
            }
        });

        profile_create_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                profile_create_save.setEnabled(false);
                profile_create_cancel.setEnabled(false);
                profile_create_save.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile_create_save.setEnabled(true);
                        profile_create_cancel.setEnabled(true);
                    }
                }, 500);
                onBackPressed();
            }
        });

        createSeekBar(profile_create_seekbar_numOfPlaces, profile_create_text_numOfPlaces, 0, 5, "NumofPlaces");
        createSeekBar(profile_create_seekbar_budget, profile_create_text_budget, 0, 4, "Budget");
        createSeekBar(profile_create_seekbar_time, profile_create_text_time, 0, 11, "Time");
        createSeekBar(profile_create_seekbar_rating, profile_create_text_rating, 0, 50, "Rating");

        // Check if this is to edit a existing profile
        Intent intent = getIntent();
        if (intent.hasExtra("P_edit_name")) {
            setValues();
        }

        profile_create_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileCreateActivity.this, ProfileCreateIconActivity.class);
                startActivityForResult(intent, SELECT_ICON);
            }
        });

    }

    @Override // To handle icon selection
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_ICON && resultCode == RESULT_OK) {
            ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
            int icon_number = data.getIntExtra("icon_number", 1);

            switch (icon_number) {
                case 1:
                    icon = 1;
                    profile_create_icon.setImageResource(R.drawable.profile_icon_food);
                    break;
                case 2:
                    icon = 2;
                    profile_create_icon.setImageResource(R.drawable.profile_icon_sports);
                    break;
                case 3:
                    icon = 3;
                    profile_create_icon.setImageResource(R.drawable.profile_icon_nightlife);
                    break;
                case 4:
                    icon = 4;
                    profile_create_icon.setImageResource(R.drawable.profile_icon_sightseeing);
                    break;
            }
        }

    }

    @Override // Creates an alert to warn user that created profile will not be saved
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("This profile will NOT be saved, exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ProfileCreateActivity.super.onBackPressed();
                    }
                });

        builder1.setNegativeButton(
                "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void createSeekBar(final SeekBar sb, final TextView progressText, final int minValue, final int maxValue, final String type){
        sb.setMin(minValue);
        sb.setMax(maxValue);
        sb.setProgress(maxValue);

        // Set Default values of seekbars & texts
        progressText.setText(sb.getProgress() + "");
        if (type.equals("Time")) {
            progressText.setText("Does Not Matter");
        }

        if (type.equals("Rating")) {
            progressText.setText("Does Not Matter");
            sb.setProgress(0);
        }

        if (type.equals("Budget")) {
            progressText.setText("Medium");
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Default Text change
                progressText.setText(progress + "");

                // Seekbar specific change
                if (type.equals("Time")) {
                    if (sb.getProgress() == sb.getMax()) {
                        progressText.setText("Does Not Matter");
                    }
                }

                if (type.equals("Budget")) {
                    switch (sb.getProgress()) {
                        case 0:
                            progressText.setText("Minimal");
                            break;
                        case 1:
                            progressText.setText("Small");
                            break;
                        case 2:
                            progressText.setText("Medium");
                            break;
                        case 3:
                            progressText.setText("Large");
                            break;
                        case 4:
                            progressText.setText("Very Large");
                            break;
                    }
                }

                if (type.equals("Rating")) {
                    progressText.setText(sb.getProgress() / 10.0 + " Star(s)");

                    if (sb.getProgress() <= 10) {
                        progressText.setText("Does Not Matter");
                    }
                }

        }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    // Method to set the values of the existing profile
    public void setValues() {
        Intent passedData = getIntent();

        // References
        final SeekBar profile_create_seekbar_numOfPlaces = findViewById(R.id.profile_create_seekbar_numOfPlaces);
        final SeekBar profile_create_seekbar_budget = findViewById(R.id.profile_create_seekbar_budget);
        final SeekBar profile_create_seekbar_time = findViewById(R.id.profile_create_seekbar_time);
        final SeekBar profile_create_seekbar_rating = findViewById(R.id.profile_create_seekbar_rating);
        final EditText profile_create_profile_name = findViewById(R.id.profile_create_profile_name);
        final ChipGroup profile_create_chipgroup = findViewById(R.id.profile_create_chipgroup);

        profile_create_profile_name.setText(passedData.getStringExtra("P_edit_name"));
        profile_create_seekbar_numOfPlaces.setProgress(Integer.parseInt(passedData.getStringExtra("P_edit_numOfPlaces")));
        profile_create_seekbar_time.setProgress(Integer.parseInt(passedData.getStringExtra("P_edit_time")));
        profile_create_seekbar_budget.setProgress(Integer.parseInt(passedData.getStringExtra("P_edit_budget")));
        profile_create_seekbar_rating.setProgress(Integer.parseInt(passedData.getStringExtra("P_edit_rating")));

        for (int i = 0; i < profile_create_chipgroup.getChildCount(); i++) {
            View view = profile_create_chipgroup.getChildAt(i);
            if (view instanceof Chip) {
                if (passedData.getStringArrayListExtra("P_edit_tags").indexOf(((Chip) view).getText().toString()) != -1) {
                    ((Chip) view).setChecked(true);
                }
            }
        }
        setIcon(passedData.getIntExtra("P_edit_icon", -1));

    }

    // Set Icon
    public void setIcon(int index) {
        final ImageView profile_create_icon = findViewById(R.id.profile_create_icon);

        switch (index) {
            case 1:
                icon = 1;
                profile_create_icon.setImageResource(R.drawable.profile_icon_food);
                break;
            case 2:
                icon = 2;
                profile_create_icon.setImageResource(R.drawable.profile_icon_sports);
                break;
            case 3:
                icon = 3;
                profile_create_icon.setImageResource(R.drawable.profile_icon_nightlife);
                break;
            case 4:
                icon = 4;
                profile_create_icon.setImageResource(R.drawable.profile_icon_sightseeing);
                break;
        }
    }

}
