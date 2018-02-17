package com.dev.alex.weatherlite;

import android.util.Log;

/**
 * Created by Alex on 2/17/2018.
 */

public class CommonUtils {

    public String bearingConverter(double degree){
        Log.i("function","degree: " + degree);
        String bearing = "Bearing number invalid";
        if(degree >= 0 && degree <= 360){
            if(degree >= 22 && degree < 67) return"NE";
            if(degree >= 67 && degree < 112) return"E";
            if(degree >= 112 && degree < 157) return"SE";
            if(degree >= 157 && degree < 202) return"S";
            if(degree >= 202 && degree < 247) return"SW";
            if(degree >= 247 && degree < 292) return"W";
            if(degree >= 292 && degree < 337) return"NW";
            if((degree >= 337 && degree <= 360) || (degree >= 0 && degree <= 22) ) return"N";
        }
        return bearing;
    }
}
