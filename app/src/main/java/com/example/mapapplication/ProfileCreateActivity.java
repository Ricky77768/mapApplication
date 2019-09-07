package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class ProfileCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_ui);
        getSupportActionBar().hide();

        // TODO: Like/Dislike - Chip/ChipGroup, Profile Picture - Select/Take a Picture

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
