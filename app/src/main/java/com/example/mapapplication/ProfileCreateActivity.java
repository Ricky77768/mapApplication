package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;

public class ProfileCreateActivity extends AppCompatActivity {
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_ui);
        getSupportActionBar().hide();

        // TODO: Like/Dislike - Chip/ChipGroup

        // EventListener for Buttons
        Button profile_create_save = findViewById(R.id.profile_create_save);
        profile_create_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button profile_create_cancel = findViewById(R.id.profile_create_cancel);
        profile_create_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
        CheckBox profile_create_checkbox_time = findViewById(R.id.profile_create_checkbox_time);
        CheckBox profile_create_checkbox_budget = findViewById(R.id.profile_create_checkbox_budget);
        final EditText profile_create_time = findViewById(R.id.profile_create_time);
        final EditText profile_create_budget = findViewById(R.id.profile_create_budget);

        profile_create_checkbox_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    profile_create_time.setEnabled(false);
                } else {
                    profile_create_time.setEnabled(true);
                }
            }
        });

        profile_create_checkbox_budget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    profile_create_budget.setEnabled(false);
                } else {
                    profile_create_budget.setEnabled(true);
                }
            }
        });

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

    // For taking picture
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // For choosing picture from album
    public void choosePicture() {
        Intent intent = new Intent();

        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // To handle picture selection
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
                // TODO: Need to compress chosen image to prevent lag
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView profile_create_icon = findViewById(R.id.profile_create_icon);
                profile_create_icon.setImageBitmap(bitmap);
            } catch (IOException e) { }

        }
    }

    @Override
    // Creates an alert to warn user that created profile will not be saved
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
}
