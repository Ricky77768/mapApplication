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

import id.zelory.compressor.Compressor;

public class ProfileCreateActivity extends AppCompatActivity {
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_ui);
        getSupportActionBar().hide();

        // References
        final SeekBar profile_create_seekbar_numOfPlaces = findViewById(R.id.profile_create_seekbar_numOfPlaces);
        final SeekBar profile_create_seekbar_budget = findViewById(R.id.profile_create_seekbar_budget);
        final SeekBar profile_create_seekbar_time = findViewById(R.id.profile_create_seekbar_time);
        final TextView profile_create_text_numOfPlaces = findViewById(R.id.profile_create_text_numOfPlaces);
        final TextView profile_create_text_budget = findViewById(R.id.profile_create_text_budget);
        final TextView profile_create_text_time = findViewById(R.id.profile_create_text_time);
        final EditText profile_create_profile_name = findViewById(R.id.profile_create_profile_name);
        final Button profile_create_save = findViewById(R.id.profile_create_save);
        final Button profile_create_cancel = findViewById(R.id.profile_create_cancel);
        final ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
        final CheckBox profile_create_checkbox_time = findViewById(R.id.profile_create_checkbox_time);
        final CheckBox profile_create_checkbox_budget = findViewById(R.id.profile_create_checkbox_budget);
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

                // TODO - Profile Picture OR simplify down to choose a set of icons
                // Passes all data into an intent for profile creation
                Intent data = new Intent();
                String name = profile_create_profile_name.getText().toString();
                String numOfPlaces = Integer.toString(profile_create_seekbar_numOfPlaces.getProgress());
                String budget = Integer.toString(profile_create_seekbar_budget.getProgress());
                String time = Integer.toString(profile_create_seekbar_time.getProgress());
                ArrayList<String> tags = new ArrayList<>();

                // Check if checkboxes are checked, which indicate that parameter does not matter
                if (profile_create_checkbox_time.isChecked()) {
                    time = "Unlimited";
                }

                if (profile_create_checkbox_budget.isChecked()) {
                    budget = "Unlimited";
                }

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
                data.putExtra("P_numOfPlaces", numOfPlaces);
                data.putExtra("P_time",time);
                data.putExtra("P_budget", budget);
                data.putStringArrayListExtra("P_tags", tags);

                // If editing a profile, then get the position of the profile in the RecycleView
                Intent previousIntent = getIntent();
                if (previousIntent.hasExtra("P_edit_position")) {
                    data.putExtra("P_position", previousIntent.getIntExtra("P_edit_position", -1));
                }

                // data.putExtra("icon", *SOMETHING*);
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

        profile_create_checkbox_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    profile_create_seekbar_time.setEnabled(false);
                    profile_create_text_time.setVisibility(View.INVISIBLE);
                } else {
                    profile_create_seekbar_time.setEnabled(true);
                    profile_create_text_time.setVisibility(View.VISIBLE);
                }
            }
        });

        profile_create_checkbox_budget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    profile_create_seekbar_budget.setEnabled(false);
                    profile_create_text_budget.setVisibility(View.INVISIBLE);
                } else {
                    profile_create_seekbar_budget.setEnabled(true);
                    profile_create_text_budget.setVisibility(View.VISIBLE);
                }
            }
        });

        createSeekBar(profile_create_seekbar_numOfPlaces, profile_create_text_numOfPlaces, 0, 5);
        createSeekBar(profile_create_seekbar_budget, profile_create_text_budget, 0, 1000);
        createSeekBar(profile_create_seekbar_time, profile_create_text_time, 0, 12);

        // Check if this is to edit a existing profile
        Intent intent = getIntent();
        if (intent.hasExtra("PC_name")) {
            setValues();
        }

        // For profile pictures (MAY BE REMOVED)
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        profile_create_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"Take a Picture", "Choose from Album"};
                builder.setTitle("Upload Profile Picture");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selected) {
                        if (selected == 0) {
                            takePicture();
                        } else {
                            choosePicture();
                        }
                    }
                });

                builder.show();
            }
        });



    }

    // For taking picture (MAY BE REMOVED)
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // For choosing image from album (MAY BE REMOVED)
    public void choosePicture() {
        Intent intent = new Intent();

        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // To handle picture selection (MAY BE REMOVED)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
            profile_create_icon.setImageBitmap(imageBitmap);
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                // TODO: Add loading screen when compressing image
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                File f = new File(this.getCacheDir(), "Sample");
                f.createNewFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
                profile_create_icon.setImageBitmap(new Compressor(this).compressToBitmap(f));

            } catch (IOException e) { }

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

    public void createSeekBar(final SeekBar sb, final TextView progressText, final int minValue, int maxValue){
        sb.setMin(minValue);
        sb.setMax(maxValue);
        sb.setProgress( (minValue + maxValue) / 2);
        progressText.setText(sb.getProgress() + "");

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(progress + "");
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
        final EditText profile_create_profile_name = findViewById(R.id.profile_create_profile_name);
        final ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
        final CheckBox profile_create_checkbox_time = findViewById(R.id.profile_create_checkbox_time);
        final CheckBox profile_create_checkbox_budget = findViewById(R.id.profile_create_checkbox_budget);
        final ChipGroup profile_create_chipgroup = findViewById(R.id.profile_create_chipgroup);

        profile_create_profile_name.setText(passedData.getStringExtra("PC_name"));
        profile_create_seekbar_numOfPlaces.setProgress(Integer.parseInt(passedData.getStringExtra("PC_numOfPlaces")));

        if (passedData.getStringExtra("PC_time").equals("Unlimited")) {
            profile_create_checkbox_time.setChecked(true);
        } else {
            profile_create_seekbar_time.setProgress(Integer.parseInt(passedData.getStringExtra("PC_time")));
        }

        if (passedData.getStringExtra("PC_budget").equals("Unlimited")) {
            profile_create_checkbox_budget.setChecked(true);
        } else {
            profile_create_seekbar_budget.setProgress(Integer.parseInt(passedData.getStringExtra("PC_budget")));
        }

        for (int i = 0; i < profile_create_chipgroup.getChildCount(); i++) {
            View view = profile_create_chipgroup.getChildAt(i);
            if (view instanceof Chip) {
                if (passedData.getStringArrayListExtra("PC_tags").indexOf(((Chip) view).getText().toString()) != -1) {
                    ((Chip) view).setChecked(true);
                }
            }
        }

    }
}
