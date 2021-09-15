package com.example.android_user_registration.ui.home;
// Ryan McGuire
// Class that handles the home fragment.
// Opens a file from internal storage
// Processes the file, extracts data
// Performs calculations on the data
//

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.android_user_registration.R;
import com.example.android_user_registration.ui.workouts.WorkoutsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private static final int HOMEFRAGMENT_RESULT_CODE = 1;
    private FloatingActionButton FAB;
    // Empty data point object arraylist
    ArrayList<GeoPoint> points = new ArrayList<>();

    private double weight;
    private String _weight = "";
    private double metScore = 0;
    private double avgSpeed = 0;
    private double avgPace = 0;
    private double distance = 0;
    private String caloriesBurned = "";
    private String timeDate;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Assign FAB
        FAB = (FloatingActionButton) view.findViewById(R.id.fab);

        // Fetch user weight for later use
        ParseQuery query = new ParseQuery("UserWeight");
        // set constraints
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, com.parse.ParseException e) {
                if (e == null) {
                    //parse to Double from String
                    _weight = (player.getString("weight"));
                    weight = Double.valueOf(_weight);
                    System.out.println("Weight: " + weight);
                } else {
                    // something went wrong
                    System.out.println("Error " + e.getMessage());
                }
            }
        });

        // what happens when the button is clicked??
        // file access
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open File Picker
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, HOMEFRAGMENT_RESULT_CODE);
                // file path here does not return a valid
                // file path that our fileprocessor can find
                // Therefore, filepath is hardcoded into processFile();
                String yourFilePath = getContext().getFilesDir() + "/" + "Data.txt";
                System.out.println(yourFilePath);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            //When permission granted, call file picker method
            //filePicker();
            Toast.makeText(getActivity().getApplicationContext(), "Permission Granted"
                    , Toast.LENGTH_SHORT).show();
        } else {
          // permission denied by user, display error
            Toast.makeText(getActivity().getApplicationContext(), "Permission Denied"
            , Toast.LENGTH_SHORT).show();

        }
    }

    // When a file is selected?
    // Call method to read the file
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case HOMEFRAGMENT_RESULT_CODE:
                if(resultCode==-1){
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    // Get directory and file path
                    Toast.makeText(getActivity(), "Processing file: " + filePath + ".Workout added to your workouts.",
                            Toast.LENGTH_LONG).show();

                    // Start processing File:
                    try {
                        processFile();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Inform user success
                    showProgress();
                }
                break;
        }
    }

    // calculate the total duration of the workout
    public double calcDuration(String startTime, String endTime) throws ParseException {

        double duration = 0; //milli seconds
        // get rid of extra zero at the end
        // set date format for calculating distance
        SimpleDateFormat format = new SimpleDateFormat("HHmmss"); // GPS trace format. Eg: '210122' = 9:01:22 pm
        Date start = format.parse(startTime);
        Date end = format.parse(endTime);

        // Calculate how many milli seconds between each.
        duration = end.getTime() - start.getTime();

        return duration/1000; //seconds;
    }

    // method to format date/time into one string
    private String formatTimeDate(String time, String date) throws ParseException {

        time = time.substring(0, time.length()-3);
        System.out.println("Time: " + time + "Date: " + date);
        SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyy");
        formatTime.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date _time = formatTime.parse(time);
            Date _date = formatDate.parse(date);
            SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yy");

            formattedTime.setTimeZone(TimeZone.getTimeZone("GMT"));

            timeDate = formattedDate.format(_time) + " " + formattedTime.format(_date);
            System.out.println("Time Date: " + timeDate);
            return timeDate;
    }


    // File Handler
    public void processFile() throws ParseException {
        // file directory for files on
        // emulator internal storage
        String fileDir = "/data/data/com.example.android_user_registration/files/Sample NMEA Data.txt";

        String line = "";

        double duration = 0;
        float prev_lat = 0;
        float prev_lon = 0;
        int count = 0; //for num of lines read GPGLL

//        String yourFilePath = getContext().getFilesDir() + "/" + "Data.txt";
        //File yourFile = new File( yourFilePath );

        try {
            // Open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileDir));
            // Read until blank line is reached
            while((line = br.readLine()) != null) {
                // look for data if interest only: $GPRMC
                if(line != null && line.contains("$GPRMC")) {
                    // Split after each comma
                    String[] tokens = line.split(",");
                    try{
                        // add point to list of geopoints with LAT/LON/TIME/DATE
                        points.add(new GeoPoint(tokens[3], tokens[5], tokens[1], tokens[9]));
                        //Convert string to float, and then from degrees/mins to decimal degrees
                        points.get(count).lat = points.get(count).Latitude2Decimal(tokens[3], tokens[4]); //LAT
                        points.get(count).lon = points.get(count).Longitude2Decimal(tokens[5], tokens[6]);//LON

                    } catch (StringIndexOutOfBoundsException e){
                        System.out.println("Data too short, not a valid reading");
                        continue;
                    }

                    // CALCULATE DISTANCE
                    // factor for first set of coordinates where no previous lat/lon
                    if (prev_lat == 0 && prev_lon == 0) {
                        prev_lat = points.get(count).lat;
                        prev_lon = points.get(count).lon;
                        distance = 0;
                    } else {
                        distance += points.get(count).calcDistance(prev_lat, prev_lon, points.get(count).lat, points.get(count).lon);
                        prev_lat = points.get(count).lat;
                        prev_lon = points.get(count).lon;
                    }
                    count++;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // calculate the duration of workout
        try {
            duration = calcDuration(points.get(0).getTime(), points.get(points.size() -1).getTime() );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //DUMMY DATA FOR TESTING
//        avgSpeed = calcSpeed(33.4, 6000.0);
//        avgPace = calcPace(33.4, 6000.0);
//        caloriesBurned = calcCaloriesBurned(6000.0, 33.4);
//        timeDate = getDateTime(points);

        avgSpeed = calcSpeed(distance, duration);
        avgPace = calcPace(distance, duration);
        caloriesBurned = calcCaloriesBurned(duration, distance);
        timeDate = getDateTime(points);

        // Upload data to backend
        uploadData(distance, duration, caloriesBurned, timeDate, avgSpeed, avgPace);

    }// end processFile()

    public String calcCaloriesBurned(Double duration, Double distance){
        duration = duration / 60; //minutes
        double avgSpeed = calcSpeed(distance, duration);
        // determine MET score
        if(avgSpeed < 6){
            metScore = 5;
        }
        else if(avgSpeed > 6 && avgSpeed <= 8){
            metScore = 8.3;
        }
        else if(avgSpeed > 8 && avgSpeed <= 9.5){
            metScore = 9.8;
        }
        else if(avgSpeed > 9.5 && avgSpeed <= 10.7){
            metScore = 10.5;
        }
        else if(avgSpeed > 10.7 && avgSpeed <= 11.2){
            metScore = 11;
        }
        else if(avgSpeed > 11.2 && avgSpeed <= 14){
            metScore = 11.8;
        }
        else{
            metScore = 12.3;
        }
        return String.valueOf(((weight * metScore)/60)*duration); // total kcal burned in workout duration
    }


    // Method for uploading data to back4app server
    public void uploadData(Double distance, Double duration, String calsBurned, String timeDate, Double speed, Double pace) throws ParseException {
        ParseObject workout = new ParseObject("TrainingSessions");
        duration = duration / 60; //minutes
        //String avgSpeed = Double.toString(duration*60*60); // Km/hr
        String avgPace = Double.toString(duration/distance); // mins/mile
       // duration = duration / 1000; // duration in seconds
        // store data in DB
        workout.put("userId", ParseUser.getCurrentUser().getObjectId());                              //user id
        workout.put("distance", distance.toString().substring(0, distance.toString().length()-13));   //distance
        workout.put("duration", duration.toString());                                                 //duration
        workout.put("avgSpeed", speed.toString());                                                    //average speed
        workout.put("avgPace", pace.toString());                                                      //  average speed
        workout.put("calsBurned", calsBurned);                                                        // calories burned
        workout.put("time", getTime(points));                                                         //time
        workout.put("date", points.get(0).getDate());                                                 //date
        workout.put("dateTime", timeDate);                                                  // date and time formatted
        workout.saveInBackground(e->{
            if (e == null) {
                //Data saved
                System.out.println("Data saved to database successfully");
            } else {
                //Display what went wrong
                System.out.println("Error " + e.getMessage());
            }
        });
    }

    private void showProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Inform user file processed
        builder.setMessage("File Uploaded Successfully!")
                .setCancelable(false)
                // Give user option to go to workouts
                .setPositiveButton("View workouts", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // switch to workouts fragment
                        WorkoutsFragment fragment = new WorkoutsFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.home_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                })
                // or to add another workout
                .setNegativeButton("Add another workout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing, stay on home fragment
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    // Retrieve starting time
    public String getTime(ArrayList<GeoPoint> points){
        return points.get(0).getTime();
    }

    public String getDateTime(ArrayList<GeoPoint> points) throws ParseException {
        String dateTime = formatTimeDate(points.get(0).getTime(), points.get(0).getDate());
        return dateTime;
    }

    // helper method for calculating speed in km/h
    public Double calcSpeed(Double distance, Double duration){
    //    distance = distance*100; //distance in metres
        duration = duration/60; // duration in hours
        avgSpeed = (distance/duration); // km/h
        return avgSpeed;
    }

    // helper method for calulating pace in mins/km
    public Double calcPace(Double distance, Double duration){
        duration = duration/60; // duration in hours
        avgPace = (duration/distance); // mins per km
        return avgPace;
    }





}
