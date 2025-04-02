package com.anju.civiladvocacyapp;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    List<MainItem> mainItems = new ArrayList<>();
    MainItem mainItem;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private CustomAdapter adapter;

    private static String locationString = "Unspecified Location";
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.userinput);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);




            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new CustomAdapter(mainItems);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Civil Advocacy");
        if (!isNetworkConnected(this) || !isLocationEnabled()) {
            // No network connection or location services
            textView.setText("No Data For Location");
            showNoDataMessage();
            // or, if you want to make the RecyclerView invisible
            // recyclerView.setVisibility(View.INVISIBLE);
        } else {
            // Network connection and location services are available
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            determineLocation();
        }

    }
    private void showNoDataMessage() {

        // You can show a message here, for example:
        // nodataTextView.setVisibility(View.VISIBLE);
        // or you can display an AlertDialog with the message
        new AlertDialog.Builder(this)
                .setTitle("No Network Connection")
                .setMessage("Data cannot be accessed/loaded without an internet connection.")
                .setPositiveButton("OK", null)
                .show();
    }


    // In your MainActivity.java file
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            // Create an AlertDialog or custom dialog to prompt the user for input
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter ZIP code or City/State Name");

            // Add a text input field to the dialog
            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userInput = input.getText().toString();
                    // Validate the user input
                    if (isValidZipCode(userInput)) {
                        // Make the API call using the ZIP code
                        makeApiCallWithZipCode(userInput);
                        textView.setText(userInput);
                    } else if (isValidCityStateName(userInput)) {
                        // Make the API call using the city/state name
                        makeApiCallWithCityState(userInput);
                        textView.setText(userInput);
                    } else {
                        // Display an error message for invalid input
                        Toast.makeText(MainActivity.this, "Invalid input format", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }
        if (id == R.id.action_about) {
            // Handle the About menu item click
            openAboutActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    // Method to validate a ZIP code
    private boolean isValidZipCode(String userInput) {
        return userInput.matches("\\d{5}");
    }

    // Method to validate a city/state name
    private boolean isValidCityStateName(String userInput) {
        return userInput.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*,\\s*[A-Z]{2}$");
    }

    private String extractCityFromInput(String userInput) {
        String[] parts = userInput.split(",");
        if (parts.length >= 1) {
            return parts[0].trim(); // Extract the city name and remove any leading or trailing white spaces
        } else {
            return ""; // Return an empty string if the format is not as expected
        }
    }

    // Method to make the API call with the ZIP code
    private void makeApiCallWithZipCode(String zipCode) {
        // Implement your API call logic with the ZIP code here
        String apiKey = "AIzaSyBnHiw04IWz3u9PCfq5n_d6H3jtVe9OIh4"; // Replace with your actual API key
        String requestUrl = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + apiKey + "&address=" + zipCode;
        makeApiCall(requestUrl);
    }

    // Method to make the API call with the city/state name
    private void makeApiCallWithCityState(String cityState) {
        // Implement your API call logic with the city/state name here
        String cityName = extractCityFromInput(cityState);
        String apiKey = "AIzaSyBnHiw04IWz3u9PCfq5n_d6H3jtVe9OIh4"; // Replace with your actual API key
        String requestUrl = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + apiKey + "&address=" + cityName;
        makeApiCall(requestUrl);
    }

    private void makeApiCall(String requestUrl) {
        mainItems.clear();

        new Thread(() -> {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    handleResponse(stringBuilder.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleResponse(String response) {
        Log.d("API_RESPONSE", response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Extract normalizedInput details
            JSONObject normalizedInput = jsonObject.getJSONObject("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");

            String officetitle = "";

            // Extract and handle offices and officials JSON arrays
            JSONArray offices = jsonObject.getJSONArray("offices");
            JSONArray officials = jsonObject.getJSONArray("officials");

            for (int i = 0; i < offices.length(); i++) {
                JSONObject office = offices.getJSONObject(i);
                officetitle = office.getString("name");

                JSONArray officialIndices = office.getJSONArray("officialIndices");

                for (int j = 0; j < (officialIndices.length()); j++) {
                    int officialIndex = officialIndices.getInt(j);

                    // Extract official details using the officialIndex
                    JSONObject official = officials.getJSONObject(officialIndex);

                    String name = official.optString("name", "Unknown");
                    String party = official.optString("party", "Unknown");
                    String phoneNumber = "";
                    String officeWebsite = "";
                    String officeEmail = "";
                    String photoUrl = "";
                    String address = "";
                    JSONArray addressArray;
                    if (official.has("address")) {
                        addressArray = official.getJSONArray("address");
                        for (int k = 0; k < addressArray.length(); k++) {

                            JSONObject addressObj = addressArray.getJSONObject(k);
                            String line1 = addressObj.optString("line1", "");
                            String line2 = addressObj.optString("line2", "");
                            String line3 = addressObj.optString("line3", "");
                            // Concatenate the lines into one address string
                            address += line1 + " " + line2 + " " + line3 + "\n";
                            String c = addressObj.optString("city", "");
                            String s = addressObj.optString("state", "");
                            String z = addressObj.optString("zip", "");

                            // Concatenate city, state, and zip code to the address
                            address += c + ", " + s + " " + z + "\n";


                        }
                    }

                    // Extract phone number
                    if (official.has("phones")) {
                        JSONArray phonesArray = official.getJSONArray("phones");
                        phoneNumber = phonesArray.optString(0, "");
                    }

                    // Extract office website
                    if (official.has("urls")) {
                        JSONArray urlsArray = official.getJSONArray("urls");
                        officeWebsite = urlsArray.optString(0, "");
                    }

                    // Extract office email
                    if (official.has("emails")) {
                        JSONArray emailsArray = official.getJSONArray("emails");
                        officeEmail = emailsArray.optString(0, "");
                    }

                    // Extract photo URL
                    photoUrl = official.optString("photoUrl", "");

                    String facebookId = null;
                    String twitterId = null;
                    String youtubeId = null;

                    JSONArray channelsArray = official.optJSONArray("channels");
                    if (channelsArray != null) {
                        for (int m = 0; m < channelsArray.length(); m++) {
                            JSONObject channel = channelsArray.getJSONObject(m);
                            String type = channel.getString("type");
                            String id = channel.getString("id");
                            if (type.equals("Facebook")) {
                                facebookId = id;
                            } else if (type.equals("Twitter")) {
                                twitterId = id;
                            } else if (type.equals("YouTube")) {
                                youtubeId = id;
                            }
                        }
                    }
                    mainItem = new MainItem(name, address, party, phoneNumber, officeWebsite, officeEmail, photoUrl, officetitle, facebookId, twitterId, youtubeId, city, state, zip);
                    mainItems.add(mainItem);

                    // Create MainItem and add it to the list


                }

            }
            runOnUiThread(() -> adapter.notifyDataSetChanged());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void determineLocation() {
        Log.d("Location", "Entering determineLocation");
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    Log.d("Location", "Location retrieved successfully");
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                        Log.d("Location", "Location String: " + locationString);

                        textView.setText(locationString);

                        makeQuery(locationString);
                    } else {
                        Log.e("Location", "Last known location is null");
                        requestLocationUpdates();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e("Location", "Failed to retrieve location", e);
                    Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                });
        Log.d("Location", "After getLastLocation call");
    }

    private void makeQuery(String address) {
        // Use the address to make further queries or API calls
        Log.d("MakeQuery", "Address: " + locationString);
        getAddressDetails(address);
    }

    private void getAddressDetails(String address) {
        Log.d("GetAddressDetails", "Address: " + address);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && addresses.size() > 0) {
                Address resultAddress = addresses.get(0);

                // Extract ZIP code
                String zipCode = resultAddress.getPostalCode();

                // Make API call with the extracted ZIP code
                makeApiCallWithZipCode(zipCode);
            } else {
                Log.e("AddressDetails", "No address found for the given location");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AddressDetails", "Error during Geocoding: " + e.getMessage());
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds in milliseconds
                .setFastestInterval(5000); // 5 seconds in milliseconds

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Handle the updated location
                        locationString = getPlace(location);
                        textView.setText(locationString);

                        // You may also want to stop requesting updates if the desired accuracy is achieved
                        mFusedLocationClient.removeLocationUpdates(this);
                    }
                }
            }
        }, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    textView.setText("Location permission was denied - cannot determine address");
                }
            }
        }
    }



    private String getPlace(Location loc) {
        //Log.e("now entering get place"+ loc);

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String addr = addresses.get(0).getAddressLine(0);
                sb.append(String.format(
                        Locale.getDefault(),
                        "%s%n%nProvider: %s%n%n%.5f, %.5f",
                        addr, loc.getProvider(),
                        loc.getLatitude(), loc.getLongitude()));
            } else {
                sb.append("cannot_determine_location");
                Log.d("Location", "Geocoder returned no addresses");
            }
        } catch (IOException e) {

            e.printStackTrace();
            Log.e("Location", "Geocoding error: " + e.getMessage());
            sb.append("geocoding_error");
        }
        return sb.toString();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        return true;
    }
}