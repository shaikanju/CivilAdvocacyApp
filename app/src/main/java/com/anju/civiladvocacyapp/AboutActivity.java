package com.anju.civiladvocacyapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.graphics.Paint;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Civil Advocacy");


        TextView textView8 = findViewById(R.id.textView8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the URL when textView8 is clicked
                openWeb("https://developers.google.com/civic-information/");
            }
        });

// Underline the text
        textView8.setPaintFlags(textView8.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
// Set the text color to white
        textView8.setTextColor(Color.WHITE);

}
    private void openWeb(String url) {
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (websiteIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(websiteIntent);
        } else {
            Toast.makeText(AboutActivity.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }}

