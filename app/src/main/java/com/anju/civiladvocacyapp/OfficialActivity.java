package com.anju.civiladvocacyapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textViewAddress;
    private  ImageView imageView;
    private  ImageView logoImageView;
    private String fullAddress;
    private String emailAddress;
    private TextView textViewEmail;
    private TextView textViewEmailHeading;
    private TextView AddressHeading;
    private TextView phonenumberheading;
    private TextView websiteheading;
    private ConstraintLayout constraintLayout;
    private  TextView addressTextView;
    private TextView addressheading;
    private String PhoneNumber;
    private String imageUrl;
    private String party;

    private TextView textViewPhone;
    private TextView textViewWebsite;
    private String web;
    private String address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Civil Advocacy");

        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textViewWebsite = findViewById(R.id.website);
         imageView = findViewById(R.id.imageView2);
        addressTextView = findViewById(R.id.userinput);
addressheading = findViewById(R.id.AddressHeading);
        textViewPhone = findViewById(R.id.phone);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewEmail = findViewById(R.id.emailAdress);
        logoImageView = findViewById(R.id.logo);
        textViewEmailHeading= findViewById(R.id.EmailHeading);
        phonenumberheading = findViewById(R.id.phonenumberheading);
        websiteheading  = findViewById(R.id.websiteheading);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the PhotoActivity when the ImageView is clicked
                Intent photoIntent = new Intent(OfficialActivity.this, PhotoActivity.class);
                String party = textView6.getText().toString(); // Assuming textView6 contains party information
                String name = textView4.getText().toString(); // Assuming textView4 contains name information
                String office = textView5.getText().toString(); // Assuming textView5 contains office information
                Log.d("Address", "Address value: " + addressTextView.getText().toString());
                String elf = addressTextView.getText().toString();
                Drawable placeholderDrawable = ContextCompat.getDrawable(OfficialActivity.this, R.drawable.placeholder_image);
                Drawable currentDrawable = imageView.getDrawable();

                if (currentDrawable != null && currentDrawable.getConstantState() != null &&
                        placeholderDrawable != null && placeholderDrawable.getConstantState() != null &&
                        currentDrawable.getConstantState().equals(placeholderDrawable.getConstantState())) {
                    // Image is the placeholder image, do not open PhotoActivity
                    Toast.makeText(OfficialActivity.this, "Cannot open PhotoActivity with a placeholder image", Toast.LENGTH_SHORT).show();
                }
                else{
                    String photoUrl = imageUrl; // Assuming imageUrl is the variable holding the photo URL
                    if (logoImageView.getVisibility() == View.VISIBLE) {
                        // If logo is present, send its resource ID to PhotoActivity
                        photoIntent.putExtra("logo", R.id.logo); // Replace "your_logo" with the actual logo resource ID
                    } else {
                        // If no logo is present, indicate that in PhotoActivity
                        photoIntent.putExtra("no_logo", true);
                    }
                    photoIntent.putExtra("party", party);
                    photoIntent.putExtra("name", name);
                    photoIntent.putExtra("office", office);
                    photoIntent.putExtra("photoUrl", photoUrl);
                    photoIntent.putExtra("elf", elf);
                    startActivity(photoIntent);
                }}
        });



        constraintLayout = findViewById(R.id.consview);
        Intent intent = getIntent();
        if (intent.hasExtra("mainItem")) {
            MainItem mainItem = (MainItem) intent.getSerializableExtra("mainItem",MainItem.class);
            if (mainItem != null) {

                 address = mainItem.getCity() + ", " + mainItem.getState();
                if (!mainItem.getZip().isEmpty()) {
                    address += " " + mainItem.getZip();
                }
                addressTextView.setText(address);

                PhoneNumber=mainItem.getPhoneNumber();
                fullAddress = mainItem.getAddress();
                emailAddress = mainItem.getEmail();
                web = mainItem.getWebsite();
                 imageUrl = mainItem.getPhotoUrl();
                 party=mainItem.getParty();
                textViewAddress.setText(fullAddress);
                if(fullAddress!=null && !fullAddress.isEmpty()){
                    addressheading.setVisibility(View.VISIBLE);
                    textViewAddress.setVisibility(View.VISIBLE);
                }
                else{
                    addressheading.setVisibility(View.GONE);
                            textViewAddress.setVisibility(View.GONE);
                }

                Linkify.addLinks(textViewAddress, Linkify.ALL);
                textViewEmail.setText(emailAddress);
                textViewWebsite.setText(web);
                textViewPhone.setText(PhoneNumber);
                if (emailAddress != null && !emailAddress.isEmpty()) {
                    textViewEmailHeading.setVisibility(View.VISIBLE);
                    textViewEmail.setVisibility(View.VISIBLE);
                    Linkify.addLinks(textViewEmail, Linkify.EMAIL_ADDRESSES);

                } else {
                    textViewEmailHeading.setVisibility(View.GONE);
                    textViewEmail.setVisibility(View.GONE);
                }
                if (PhoneNumber != null && !PhoneNumber.isEmpty()) {
                    textViewPhone.setVisibility(View.VISIBLE);
                    phonenumberheading.setVisibility(View.VISIBLE);
                    textViewPhone.setOnClickListener(v -> callNumber(PhoneNumber));
                    Linkify.addLinks(textViewPhone,Linkify.PHONE_NUMBERS);
                } else {

                    textViewPhone.setVisibility(View.GONE);
                    phonenumberheading.setVisibility(View.GONE);
                }
                if (web != null && !web.isEmpty()) {
                    websiteheading.setVisibility(View.VISIBLE);
                    textViewWebsite.setVisibility(View.VISIBLE);
                    Linkify.addLinks(textViewWebsite, Linkify.WEB_URLS);



                    textViewWebsite.setOnClickListener(v -> openWeb(web));
                } else {
                    websiteheading.setVisibility(View.GONE);
                    textViewWebsite.setVisibility(View.GONE);
                }

                textView4.setText(mainItem.getName());
                textView5.setText(mainItem.getOffice());
                String facebookChannel = mainItem.getFacebookId(); // Assuming you have a method like getFacebookChannel() in MainItem
                String youtubeChannel = mainItem.getYoutubeId(); // Assuming you have a method like getYoutubeChannel() in MainItem
                String twitterChannel = mainItem.getTwitterId(); // Assuming you have a method like getTwitterChannel() in MainItem
                ImageView facebookImageView = findViewById(R.id.facebook_icon);
                ImageView youtubeImageView = findViewById(R.id.youtube_icon);
                ImageView twitterImageView = findViewById(R.id.twitter_icon);

                textView6.setText("(" + party + ")");


                textView4.setTextColor(Color.WHITE);
                textView5.setTextColor(Color.WHITE);
                textView6.setTextColor(Color.WHITE);
                textViewAddress.setTextColor(Color.WHITE);
                textViewEmail.setTextColor(Color.WHITE);
                textViewPhone.setTextColor(Color.WHITE);
                textViewWebsite.setTextColor(Color.WHITE);
                if (twitterChannel != null && !twitterChannel.isEmpty()) {
                    twitterImageView.setImageResource(R.drawable.twitter);
                    twitterImageView.setOnClickListener(v -> clickTwitter(v, twitterChannel));
                } else {
                    // If no Twitter channel, hide or disable the Twitter icon
                    twitterImageView.setVisibility(View.GONE);
                }

                if (youtubeChannel != null && !youtubeChannel.isEmpty()) {
                    youtubeImageView.setImageResource(R.drawable.youtube);
                    youtubeImageView.setOnClickListener(v -> youTubeClicked(v, youtubeChannel));
                } else {
                    // If no YouTube channel, hide or disable the YouTube icon
                    youtubeImageView.setVisibility(View.GONE);
                }
                if (facebookChannel != null && !facebookChannel.isEmpty()) {
                    facebookImageView.setImageResource(R.drawable.facebook);
                    facebookImageView.setOnClickListener(v -> clickFacebook(v, facebookChannel));
                } else {
                    // If no Facebook channel, hide or disable the Facebook icon
                    facebookImageView.setVisibility(View.GONE);
                }
                if ("Democratic Party".equals(party)) {

                    logoImageView.setImageResource(R.drawable.dem_logo);
                    logoImageView.setOnClickListener(v -> openPartyWebsite("https://democrats.org"));
                } else if ("Republican Party".equals(party)) {

                    logoImageView.setImageResource(R.drawable.rep_logo);
                    logoImageView.setOnClickListener(v -> openPartyWebsite("https://www.gop.com"));
                } else {
                    // Do nothing or set a default image if the party is neither Democratic nor Republican
                    // logoImageView.setImageResource(R.drawable.default_logo); // Uncomment and provide a default logo resource
                    logoImageView.setVisibility(View.GONE); // Hide the ImageView if no logo should be displayed
                }
                if (imageUrl!= null && !imageUrl.isEmpty()) {
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.drawable.brokenimage) // Set the placeholder drawable resource here
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.placeholder_image);
                }

                if (mainItem.getParty().equals("Democratic Party")) {
                   // constraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                   // constraintLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
                    constraintLayout.setBackgroundColor(Color.rgb(20, 20, 150));
                } else if (mainItem.getParty().equals("Republican Party")) {
                    constraintLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                } else {
                    constraintLayout.setBackgroundColor(Color.BLACK);
                }
            }
        }
        textViewAddress.setOnClickListener(v -> openInMaps(fullAddress));
        textViewEmail.setOnClickListener(v -> composeEmail(emailAddress));

        textViewWebsite.setOnClickListener(v ->openWeb(web));
    }
    private void openPartyWebsite(String websiteUrl) {
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        if (websiteIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(websiteIntent);
        } else {
            Toast.makeText(this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickTwitter(View v, String channel) {
        String user = channel;
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

        Intent intent;

        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }
    public void youTubeClicked(View v, String channel) {
        String name = channel;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }


    public void clickFacebook(View v, String channel) {
        String FACEBOOK_URL = "https://www.facebook.com/" + channel;

        Intent intent;

        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
        }
    }
    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void makeErrorAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Do nothing or handle the error
                });
        builder.create().show();
    }
    private void openWeb(String website){
        Toast.makeText(OfficialActivity.this, "Website: " + website, Toast.LENGTH_SHORT).show();

        // Create an implicit intent to open the website
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));

        // Check if there's an app available to handle the intent
        if (websiteIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity with the intent
            startActivity(websiteIntent);
        } else {
            // Display a message if no app can handle the intent
            Toast.makeText(OfficialActivity.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }

}

    private void  callNumber(String PhNum){
        Toast.makeText(OfficialActivity.this, "Phone Number: " + PhNum, Toast.LENGTH_SHORT).show();

        // Create an implicit intent to dial the phone number
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + PhNum));

        // Check if there's an app available to handle the intent
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity with the intent
            startActivity(dialIntent);
        } else {
            // Display a message if no app can handle the intent
            Toast.makeText(OfficialActivity.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }
    private void openInMaps(String address) {
        // Create an implicit intent with the action VIEW and the geo URI for the address
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);

        // Check if there's an app available to handle the intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity with the intent
            startActivity(mapIntent);
        } else {
            // Display a message if no app can handle the intent
            Toast.makeText(this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }



    private void composeEmail(String email) {
        Toast.makeText(OfficialActivity.this, "Email Address: " + emailAddress, Toast.LENGTH_SHORT).show();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
        }
    }

}
