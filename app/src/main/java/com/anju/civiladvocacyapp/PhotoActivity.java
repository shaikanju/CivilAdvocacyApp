package com.anju.civiladvocacyapp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class PhotoActivity extends AppCompatActivity {

    private TextView textView11;
    private TextView textView12;
    private ImageView imageView3;
    private ImageView imageView4;
    private TextView userinput;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Civil Advocacy");
        constraintLayout = findViewById(R.id.conview);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        userinput = findViewById(R.id.userinput);

        Intent intent = getIntent();
        if (intent != null) {
            String partyWithBrackets = intent.getStringExtra("party");
            String party = partyWithBrackets.replace("(", "").replace(")", "");
            String name = intent.getStringExtra("name");
            String office = intent.getStringExtra("office");
            String photoUrl = intent.getStringExtra("photoUrl");
            String input = intent.getStringExtra("elf");

            userinput.setText(input);
            Picasso.get().load(photoUrl).error(R.drawable.brokenimage).into(imageView3);

            if ("Democratic Party".equals(party)) {
                imageView4.setImageResource(R.drawable.dem_logo);
                imageView4.setOnClickListener(v -> openPartyWebsite("https://democrats.org"));
            } else if ("Republican Party".equals(party)) {
                imageView4.setImageResource(R.drawable.rep_logo);
                imageView4.setOnClickListener(v -> openPartyWebsite("https://www.gop.com"));
            } else {
                // Do nothing or set a default image if the party is neither Democratic nor Republican
                // logoImageView.setImageResource(R.drawable.default_logo); // Uncomment and provide a default logo resource
                imageView4.setVisibility(View.GONE); // Hide the ImageView if no logo should be displayed
            }
            if (party.equals("Democratic Party")) {
                // constraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                // constraintLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
                constraintLayout.setBackgroundColor(Color.rgb(20, 20, 150));
            } else if (party.equals("Republican Party")) {
                constraintLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                constraintLayout.setBackgroundColor(Color.BLACK);
            }

            textView11.setText(name);
            textView12.setText(office);
        }




    }
    private void openPartyWebsite(String websiteUrl) {
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        if (websiteIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(websiteIntent);
        } else {
            Toast.makeText(this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }


    }


