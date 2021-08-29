package com.example.android_user_registration.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.android_user_registration.LoginActivity;
import com.example.android_user_registration.MainActivityLogin;
import com.example.android_user_registration.R;
import com.example.android_user_registration.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final int HOMEFRAGMENT_RESULT_CODE = 1;
    private static final int RESULT_OK = 1;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FloatingActionButton FAB;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // floating action button for prompting user for file from internal storage
        //FloatingActionButton FAB = fragment_home.findViewById(R.id.fab);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Assign FAB
        FAB = (FloatingActionButton) view.findViewById(R.id.fab);


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // what happens when the button is clicked??
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open File Picker
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, HOMEFRAGMENT_RESULT_CODE);
            }
        });

        return view;
        //return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                    String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    String fileLocation = (fileDir + filePath);

                    //String filePath = PathUtils.getPath(getContext(), uri);
                    Toast.makeText(getActivity(), "Processing file: " + filePath + ".Workout added to your workouts.",
                            Toast.LENGTH_LONG).show();

                    // Start processing File:
                    processFile(fileLocation);
                }
                break;
        }
    }

    // calculate the total duration of the workout
    public static double calcDuration(String startTime, String endTime) throws ParseException {

        double duration = 0; //seconds

        // set date format for calculating distance
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        Date start = format.parse(startTime);
        Date end = format.parse(endTime);

        duration = end.getTime() - start.getTime();
        return duration;
    }

    public void processFile(String fileDir) {
        // Empty data point object arraylist
        ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
        //String fileDir = "/root/storage/emulated/0/documents/document:27";
        String line = "";
        int i = 0;
        double distance = 0;
        double duration = 0;
        float prev_lat = 0;
        float prev_lon = 0;
        int count = 0;

        try {
            // Open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileDir));

            // Read until blank line is reached
            while((line = br.readLine()) != null) {

                // look for data if interest only: GPGLL
                if(line != null && line.contains("$GPGLL")) {

                    // Split after each comma
                    String [] tokens = line.split(",");

                    // add point to list of geopoints with LAT/LON/TIME
                    points.add(new GeoPoint(tokens[1], tokens[3], tokens[5]));
                    //Convert string to float, and then from degrees/mins to decimal degrees
                    points.get(count).lat = points.get(count).Latitude2Decimal(tokens[1], tokens[2]); //LAT
                    points.get(count).lon = points.get(count).Longitude2Decimal(tokens[3], tokens[4]);//LON


                    // factor for first set of coordinates where no previous lat/lon
                    //if(points.get(count).prev_lat == 0 && points.get(count).prev_lon == 0) {
                    if(prev_lat == 0 && prev_lon == 0) {
                        prev_lat = points.get(count).lat;
                        prev_lon = points.get(count).lon;
                        distance = 0;
                        System.out.println("\nLatitude: " + points.get(count).getLAT());
                        System.out.println("Longitude: " + points.get(count).getLON());
                        System.out.println("Distance to previous geopoint: " + points.get(count).getDistance());
                        System.out.println("Timestamp: " + points.get(count).getTime());

                    }
                    else {
//						points.get(count).prev_lat = points.get(count).lat;
//						points.get(count).prev_lon = points.get(count).lon;
//						distance += points.get(count).calcDistance(prev_lat, points.get(count).prev_lon ,points.get(count).lat, points.get(count).lon);
                        distance += points.get(count).calcDistance(prev_lat, prev_lon ,points.get(count).lat, points.get(count).lon);
                        prev_lat = points.get(count).lat;
                        prev_lon = points.get(count).lon;
                        System.out.println("\nLatitude: " + points.get(count).getLAT());
                        System.out.println("Longitude: " + points.get(count).getLON());
                        System.out.println("Distance to previous geopoint: " + points.get(count).getDistance());
                        System.out.println("Timestamp: " + points.get(count).getTime());
                    }

//					System.out.println("\nLatitude: " + points.get(count).getLAT());
//					System.out.println("Longitude: " + points.get(count).getLON());
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

//        // calculate total distance
//        try {
//            duration = calcDuration(points.get(0).getTime(), points.get(points.size() -1).getTime());// start and end time passed
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


        System.out.println("\nTotal distance:  " + distance + " Kilometres"); // in kilometres
        System.out.println("\nTotal duration:  " + duration/1000 + " Seconds"); // seconds
        System.out.println("\nAverage Speed: " + String.format("%.02f", (distance/duration)*10000) + " Metres/Second" ); // m/s



    }

    }
//        // Empty data point object arraylist
//        ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
//        String line = "";
//        int i, count = 0;
//        double distance, duration = 0;
//        float prev_lat, prev_lon = 0;
//
//        try {
//            // Open file for reading
//            BufferedReader br = new BufferedReader(new FileReader(fileDir));
//
//            // Read until blank line is reached
//            while ((line = br.readLine()) != null) {
//
//                // look for data if interest only: GPGLL
//                if (line != null && line.contains("$GPGLL")) {
//
//                    // Split after each comma
//                    String[] tokens = line.split(",");
//
//                    // add point to list of geopoints with LAT/LON
//                    points.add(new GeoPoint(tokens[1], tokens[3], tokens[5]));
//                    //Convert string to float, and then from degrees/mins to decimal degrees
//                    points.get(count).Lat = points.get(count).Latitude2Decimal(tokens[1], tokens[2]); //LAT
//                    points.get(count).Lon = points.get(count).Longitude2Decimal(tokens[3], tokens[4]);//LON
//
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

