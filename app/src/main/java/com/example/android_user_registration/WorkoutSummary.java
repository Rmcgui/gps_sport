package com.example.android_user_registration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class WorkoutSummary extends AppCompatActivity {

    public static final String EXTRA_WORKOUTID = "workoutId";
    ParseQuery query = ParseQuery.getQuery("TrainingSessions");
    public String distance;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);
        setActionBar( (Toolbar) findViewById(R.id.toolbar));

        //Initialise text views in workout summary
        TextView nameTextView = findViewById(R.id.name_left);
        TextView timeTextView = findViewById(R.id.time);
        TextView distanceTextView = findViewById(R.id.totalDistance);
        TextView durationTextView = findViewById(R.id.duration);
        TextView avgSpeedTextView = findViewById(R.id.avgSpeed);

        // start reading data from DB
        // set constraints to user and workout ID
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
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
}