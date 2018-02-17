package com.dev.alex.weatherlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    String baseURL = "https://api.darksky.net/forecast/";
    String darkSkyKey = "934e635759a894bd6806adb711aa951b";
    String darkSkyExcludes = "?exclude=minutely,hourly,daily,alerts,flags&units=auto";
    RequestQueue requestQueue;

    GoogleApiClient mGoogleApiClient;
    Location location;

    private FusedLocationProviderClient mFusedLocationClient;


    private TextView lblTemperature;
    private TextView lblFeelsLike;
    private ListView mainListView;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());


        requestQueue = Volley.newRequestQueue(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        lblTemperature = findViewById(R.id.lblTemperature);
        lblFeelsLike = findViewById(R.id.lblFeelsLike);
        mainListView = findViewById(R.id.mainListView);



    }


    private void updateForecast() {
        String urlDarkSky = getDarkSkyURL();

        Log.i("track", urlDarkSky);
        JSONArray array;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlDarkSky, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Forecast f = gson.fromJson(response.toString(), Forecast.class);
                        updateListView(f);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("errorRequest", "Error response: " + error.getMessage());
                    }
                });


        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsObjRequest);
    }

    public void updateListView(Forecast f) {
        lblTemperature.setText("Temperature: " + f.currently.temperature);
        lblFeelsLike.setText("Feels like: " + f.currently.apparentTemperature);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, f.currently.getListValues());
        mainListView.setAdapter(adapter);
    }

    private String getDarkSkyURL() {
        String latitude = "58.5664518";
        String longitude = "-76.411153";
        if (location != null){
            latitude = ""+location.getLatitude();
            longitude = ""+location.getLongitude();
        }

        return baseURL + darkSkyKey + "/" + latitude + "," + longitude + darkSkyExcludes;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            Log.d("location","location: "+location.getLatitude()+","+location.getLongitude());
            updateForecast();
        }
        else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Please activate location")
                    .setMessage("Click ok to goto settings else exit.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GoogleAPI", "Connection failed: ConnectionResult.getErrorCode() = "+ connectionResult.getErrorCode());
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
