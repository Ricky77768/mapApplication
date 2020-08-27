package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfileCreateIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_create_icon_ui);
        getSupportActionBar().setTitle("Choose an Icon");

        // References
        ImageButton profile_create_icon1 = findViewById(R.id.profile_create_icon1);
        ImageButton profile_create_icon2 = findViewById(R.id.profile_create_icon2);
        ImageButton profile_create_icon3 = findViewById(R.id.profile_create_icon3);
        ImageButton profile_create_icon4 = findViewById(R.id.profile_create_icon4);
        ImageButton profile_create_icon5 = findViewById(R.id.profile_create_icon5);
        ImageButton profile_create_icon6 = findViewById(R.id.profile_create_icon6);
        ImageButton profile_create_icon7 = findViewById(R.id.profile_create_icon7);
        ImageButton profile_create_icon8 = findViewById(R.id.profile_create_icon8);
        ImageButton profile_create_icon9 = findViewById(R.id.profile_create_icon9);
        ImageButton profile_create_icon10 = findViewById(R.id.profile_create_icon10);
        ImageButton profile_create_icon11 = findViewById(R.id.profile_create_icon11);
        ImageButton profile_create_icon12 = findViewById(R.id.profile_create_icon12);
        ImageButton profile_create_icon13 = findViewById(R.id.profile_create_icon13);
        ImageButton profile_create_icon14 = findViewById(R.id.profile_create_icon14);
        ImageButton profile_create_icon15 = findViewById(R.id.profile_create_icon15);
        ImageButton profile_create_icon16 = findViewById(R.id.profile_create_icon16);
        ImageButton profile_create_icon17 = findViewById(R.id.profile_create_icon17);
        ImageButton profile_create_icon18 = findViewById(R.id.profile_create_icon18);
        ImageButton profile_create_icon19 = findViewById(R.id.profile_create_icon19);
        ImageButton profile_create_icon20 = findViewById(R.id.profile_create_icon20);
        ImageButton profile_create_icon21 = findViewById(R.id.profile_create_icon21);
        ImageButton profile_create_icon22 = findViewById(R.id.profile_create_icon22);
        ImageButton profile_create_icon23 = findViewById(R.id.profile_create_icon23);
        ImageButton profile_create_icon24 = findViewById(R.id.profile_create_icon24);
        ImageButton profile_create_icon25 = findViewById(R.id.profile_create_icon25);
        ImageButton profile_create_icon26 = findViewById(R.id.profile_create_icon26);
        ImageButton profile_create_icon27 = findViewById(R.id.profile_create_icon27);
        ImageButton profile_create_icon28 = findViewById(R.id.profile_create_icon28);
        ImageButton profile_create_icon29 = findViewById(R.id.profile_create_icon29);
        ImageButton profile_create_icon30 = findViewById(R.id.profile_create_icon30);
        ImageButton profile_create_icon31 = findViewById(R.id.profile_create_icon31);
        ImageButton profile_create_icon32 = findViewById(R.id.profile_create_icon32);
        ImageButton profile_create_icon33 = findViewById(R.id.profile_create_icon33);
        ImageButton profile_create_icon34 = findViewById(R.id.profile_create_icon34);
        ImageButton profile_create_icon35 = findViewById(R.id.profile_create_icon35);
        ImageButton profile_create_icon36 = findViewById(R.id.profile_create_icon36);
        ImageButton profile_create_icon37 = findViewById(R.id.profile_create_icon37);
        ImageButton profile_create_icon38 = findViewById(R.id.profile_create_icon38);
        ImageButton profile_create_icon39 = findViewById(R.id.profile_create_icon39);
        ImageButton profile_create_icon40 = findViewById(R.id.profile_create_icon40);
        ImageButton profile_create_icon41 = findViewById(R.id.profile_create_icon41);
        ImageButton profile_create_icon42 = findViewById(R.id.profile_create_icon42);
        ImageButton profile_create_icon43 = findViewById(R.id.profile_create_icon43);
        ImageButton profile_create_icon44 = findViewById(R.id.profile_create_icon44);
        ImageButton profile_create_icon45 = findViewById(R.id.profile_create_icon45);
        ImageButton profile_create_icon46 = findViewById(R.id.profile_create_icon46);
        ImageButton profile_create_icon47 = findViewById(R.id.profile_create_icon47);
        ImageButton profile_create_icon48 = findViewById(R.id.profile_create_icon48);
        ImageButton profile_create_icon49 = findViewById(R.id.profile_create_icon49);
        ImageButton profile_create_icon50 = findViewById(R.id.profile_create_icon50);
        ImageButton profile_create_icon51 = findViewById(R.id.profile_create_icon51);
        ImageButton profile_create_icon52 = findViewById(R.id.profile_create_icon52);
        ImageButton profile_create_icon53 = findViewById(R.id.profile_create_icon53);
        ImageButton profile_create_icon54 = findViewById(R.id.profile_create_icon54);

        // Click listeners
        profile_create_icon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 2);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 3);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 4);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 5);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 6);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 7);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 8);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 9);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 10);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 11);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 12);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 13);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 14);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 15);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 16);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 17);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 18);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon19.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 19);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 20);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 21);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon22.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 22);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 23);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon24.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 24);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon25.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 25);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon26.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 26);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon27.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 27);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon28.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 28);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon29.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 29);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 30);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 31);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 32);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 33);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 34);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon35.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 35);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon36.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 36);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon37.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 37);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon38.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 38);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon39.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 39);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon40.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 40);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 41);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon42.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 42);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 43);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon44.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 44);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon45.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 45);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon46.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 46);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon47.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 47);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon48.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 48);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon49.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 49);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon50.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 50);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon51.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 51);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon52.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 52);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon53.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 53);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        profile_create_icon54.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("icon_number", 54);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
