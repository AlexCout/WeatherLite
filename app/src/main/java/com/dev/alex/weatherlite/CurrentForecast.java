package com.dev.alex.weatherlite;

import java.lang.reflect.Field;
import com.dev.alex.weatherlite.CommonUtils;

/**
 * Created by Alex on 2/16/2018.
 */

public class CurrentForecast {
    public int time;
    public String summary;
    public String icon;
    public int nearestStormDistance;
    public int nearestStormBearing;
    public int precipIntensity;
    public int precipProbability;
    public double temperature;
    public double apparentTemperature;
    public double dewPoint;
    public double humidity;
    public double pressure;
    public double windSpeed;
    public double windGust;
    public double windBearing;
    public double cloudCover;
    public double uvIndex;
    public double visibility;
    public double ozone;


    public String[] getListValues(){
        CommonUtils c = new CommonUtils();

        String[] list = new String[]{
                "time: " + time
                ,"summary: " + summary
                ,"icon: " + icon
                ,"precipIntensity: " + precipIntensity
                ,"precipProbability: " + precipProbability
                ,"humidity: " + humidity
                ,"windSpeed: " + windSpeed
                ,"windGust: " + windGust
                ,"windBearing: " + windBearing + " (" + c.bearingConverter(windBearing) + ")"
                ,"cloudCover: " + cloudCover
        };

        return list;

    }

}
