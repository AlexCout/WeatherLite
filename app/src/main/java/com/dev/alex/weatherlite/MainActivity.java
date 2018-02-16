package com.dev.alex.weatherlite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.Console;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    String baseURL = "https://api.darksky.net/forecast/934e635759a894bd6806adb711aa951b/37.8267,-122.4233?exclude=minutely,hourly,daily,alerts,flags";
    String url;
    RequestQueue requestQueue;

    Forecast mainForecast;

    Button btn2;

    private TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.currentTemperature);
        btn2 = findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateForecast();
            }
        });
        requestQueue = Volley.newRequestQueue(this);

    }

    private void updateForecast(){
        this.url = this.baseURL;
        Log.i("track","1");
        JSONArray array;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Forecast f = gson.fromJson(response.toString(), Forecast.class);
                        updateListView(f);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("errorRequest","Error response: " + error.getMessage());
                    }
                });


        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsObjRequest);
    }

    private void updateListView(Forecast f) {
        Log.i("test","ici");
        temp.setText("Temperature: "+ f.currently.temperature);

    }
}
