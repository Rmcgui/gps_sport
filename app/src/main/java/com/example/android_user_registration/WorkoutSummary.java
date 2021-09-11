package com.example.android_user_registration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android_user_registration.interfaces.OnItemClickListener;
import com.example.android_user_registration.ui.workouts.TrainingSessions;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

// This class needs to implement the onclick interface
// When an item in the adapter is clicked
// pass the object Id to this activity.
public class WorkoutSummary extends AppCompatActivity implements OnItemClickListener{


    public static String WORKOUT_ID = ""; // unique workout to display

    // Query DB for data to display
    ParseQuery query = ParseQuery.getQuery("TrainingSessions");
    public ArrayList<TrainingSessions> sessions = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);
        setActionBar( (Toolbar) findViewById(R.id.toolbar));
        // GET ID of workout selected by user
        Intent i = getIntent();
        WORKOUT_ID = i.getStringExtra("WORKOUT_ID");
    }

    protected void onStart() {
        super.onStart();
        //Initialise text views in workout summary
        TextView nameTextView = findViewById(R.id.name_left);
        TextView timeTextView = findViewById(R.id.time);
        TextView distanceTextView = findViewById(R.id.totalDistance);
        TextView durationTextView = findViewById(R.id.duration);
        TextView avgSpeedTextView = findViewById(R.id.avgSpeed);

        // start reading data from DB
        // set constraints to user ID
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        // and workout ID
        query.whereEqualTo("objectId", WORKOUT_ID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    nameTextView.setText(ParseUser.getCurrentUser().getUsername());
                    durationTextView.setText(player.getString("duration") + "  Seconds");
                    timeTextView.setText(player.getString("dateTime"));
                    distanceTextView.setText(player.getString("distance") + "  Km");
                    avgSpeedTextView.setText(player.getString("avgSpeed" )+ "  Km/h");

                } else {
                    // something went wrong
                    System.out.println("Error " + e.getMessage());
                }
            }
        });

    }

    @Override
    public void OnItemClick(String workoutId) {
        this.WORKOUT_ID = workoutId;
    }

}

