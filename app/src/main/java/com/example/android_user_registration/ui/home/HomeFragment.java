package com.example.android_user_registration.ui.home;
import java.text.DateFormat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.android_user_registration.R;
import com.example.android_user_registration.ui.workouts.WorkoutsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private static final int HOMEFRAGMENT_RESULT_CODE = 1;
    private static final int RESULT_OK = 1;
    private FloatingActionButton FAB;
    // Empty data point object arraylist
    ArrayList<GeoPoint> points = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        // floating action button for prompting user for file from internal storage
        //FloatingActionButton FAB = fragment_home.findViewById(R.id.fab);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Assign FAB
        FAB = (FloatingActionButton) view.findViewById(R.id.fab);

//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        // what happens when the button is clicked??
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open File Picker
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, HOMEFRAGMENT_RESULT_CODE);
                String yourFilePath = getContext().getFilesDir() + "/" + "Data.txt";
                System.out.println(yourFilePath);
            }
        });

        return view;
        //return root;
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
    // Process the data
    // Store in backend database
    // Add new workout to Workouts fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case HOMEFRAGMENT_RESULT_CODE:
                if(resultCode==-1){
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    // Get directory and file path
                    String fileDir = Environment.getExternalStorageDirectory() + filePath;

                    //String filePath = PathUtils.getPath(getContext(), uri);
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

        double duration = 0; //seconds

        // set date format for calculating distance
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        Date start = format.parse(startTime);
        Date end = format.parse(endTime);

        duration = end.getTime() - start.getTime();
        return duration;
    }

    // method to format date/time into one string
    private String formatTimeDate(String time, String date) throws ParseException {

        String timeDate = "";

        SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyy");
        formatTime.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date _time = formatTime.parse(time);
            Date _date = formatDate.parse(date);
            SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yy");

            return timeDate = formattedDate.format(_time) + formattedTime.format(_date);
    }


    // File Handler
    public void processFile() throws ParseException {

//        String fileDir = "/storage/emulated/0/Documents/Data.txt";
        String fileDir = "/data/data/com.example.android_user_registration/files/Sample NMEA Data.txt";
        String line = "";
        double distance = 0;
        double duration = 0;
        float prev_lat = 0;
        float prev_lon = 0;
        int count = 0; //for num of lines read GPGLL
        int counter = 0; // for num of lines read GPRMC

        String yourFilePath = getContext().getFilesDir() + "/" + "Data.txt";
        //File yourFile = new File( yourFilePath );

        try {
            // Open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileDir));

            // Read until blank line is reached
            while((line = br.readLine()) != null) {


                // look for data if interest only: GPGLL
                if(line != null && line.contains("$GPRMC")) {

                    // Split after each comma
                    String[] tokens = line.split(",");

                    // add point to list of geopoints with LAT/LON/TIME/DATE
                    points.add(new GeoPoint(tokens[3], tokens[5], tokens[1], tokens[9]));
                    //Convert string to float, and then from degrees/mins to decimal degrees
                    points.get(count).lat = points.get(count).Latitude2Decimal(tokens[3], tokens[4]); //LAT
                    points.get(count).lon = points.get(count).Longitude2Decimal(tokens[5], tokens[6]);//LON

                    // CALCULATE DISTANCE
                    // factor for first set of coordinates where no previous lat/lon
                    //if(points.get(count).prev_lat == 0 && points.get(count).prev_lon == 0) {
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

        System.out.println("\nFile Location: " + yourFilePath );

        // Upload data to backend
        uploadData(distance, duration);

    }

    // Method for uploading data to back4app server
    public void uploadData(Double distance, Double duration) throws ParseException {
        ParseObject workout = new ParseObject("TrainingSessions");
        double avgSpeed = distance/duration;
        duration = duration / 1000; // duration in seconds
        // store data in DB
        workout.put("userId", ParseUser.getCurrentUser().getObjectId()); //user id
        workout.put("distance", distance.toString());       //distance
        workout.put("duration", duration.toString());       //duration
        workout.put("avgSpeed", Double.toString(avgSpeed)); //average speed
        workout.put("time", getTime(points));               //time
        workout.put("date", points.get(0).getDate());       //date
        workout.put("dateTime", getDateTime(points));       // date and time formatted
        // calsBurned
        // avgPace
        workout.saveInBackground(e->{
            if (e == null) {
                //Data saved
                System.out.println("Data saved to database successfully");
                try {
                    System.out.println("dateTime:" + getDateTime(points));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            } else {
                //Display what went wrong
                System.out.println("Error " + e.getMessage());
            }
        });
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("File Uploaded Successfully!").setCancelable(false);
        builder.setPositiveButton("View workouts", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button_positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button button_negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);


                button_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                button_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create new fragment and transaction
                        Fragment newFragment = new WorkoutsFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                        // Replace whatever is in the home_fragment view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(((ViewGroup)getView().getParent()).getId(), newFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                        alertDialog.dismiss();}
                });

            }
        });
        alertDialog.show();


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

}
