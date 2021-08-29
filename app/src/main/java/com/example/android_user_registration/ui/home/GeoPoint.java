package com.example.android_user_registration.ui.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Class that creates GEO point objects and holds its data

public class GeoPoint {

    public String Lat = "";
    public String Lon = "";
    public String Time = "";
    public double Distance = 0.0;

    public float lat = 0.0f;
    public float lon = 0.0f;


    // default constructor
    public GeoPoint() {

    }

    public GeoPoint(String lat, String lon, String time) {
        this.Lat = lat;
        this.Lon = lon;
        this.Time = time;
    }


    // setters/getters
    public void setLAT(String lat) {
        this.Lat = lat;
    }
    public String getLAT() {
        return this.Lat;
    }

    public void setLON(String lat) {
        this.Lat = lat;
    }
    public String getLON() {
        return this.Lon;
    }

    public void setTime(String time) {
        this.Time = time;
    }
    public String getTime() {
        return this.Time;
    }

    public void setDistance(double distance) {
        this.Distance = distance;
    }

    public double getDistance() {
        return this.Distance;
    }


    // helper function to convert Latitude from string to decimal degrees
    public float Latitude2Decimal(String lat, String NS) {
        int minutesPosition = lat.indexOf('.') - 2;
        float minutes = Float.parseFloat(lat.substring(minutesPosition));
        float decimalDegrees = Float.parseFloat(lat.substring(minutesPosition))/60.0f;

        float degree = Float.parseFloat(lat) - minutes;
        float wholeDegrees = (int)degree/100;

        float latitudeDegrees = wholeDegrees + decimalDegrees;
        if(NS.startsWith("S")) {
            latitudeDegrees = -latitudeDegrees;
        }
        return latitudeDegrees;
    }

    // helper function to convert Longitude from string to decimal degrees
    public float Longitude2Decimal(String lon, String WE) {
        int minutesPosition = lon.indexOf('.') - 2;
        float minutes = Float.parseFloat(lon.substring(minutesPosition));
        float decimalDegrees = Float.parseFloat(lon.substring(minutesPosition))/60.0f;

        float degree = Float.parseFloat(lon) - minutes;
        float wholeDegrees = (int)degree/100;

        float longitudeDegrees = wholeDegrees + decimalDegrees;
        if(WE.startsWith("W")) {
            longitudeDegrees = -longitudeDegrees;
        }
        return longitudeDegrees;
    }

    // calculate distance between 2 points
    public double calcDistance(float prev_lat, float prev_lon, float lat, float lon) {
        // Haversine formula for calc distance
        double p = 0.017453292519943295;    // Math.PI / 180
        double a = 0.5 - Math.cos((lat - prev_lat) * p)/2 +
                Math.cos(prev_lat * p) * Math.cos(lat * p) *
                        (1 - Math.cos((lon - prev_lon) * p))/2;

        setDistance(this.Distance);

        return this.Distance = 12742 * Math.asin(Math.sqrt(a)) ; // 2 * R; R = 6371 km
    }


    // calculate the total duration of the workout in secodns
    public static double calcDuration(String startTime, String endTime) throws ParseException {

        double duration = 0; //seconds
        // Take time format as is from GPS trace: HHmmss
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        // parse to date
        Date start = format.parse(startTime);
        Date end = format.parse(endTime);
        // calculate duration
        duration = end.getTime() - start.getTime();
        return duration;
    }

}

