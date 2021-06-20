package com.example.android_user_registration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toolbar;

public class ViewWorkoutActivity extends AppCompatActivity {

    public static final String EXTRA_WORKOUTID = "workoutId";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);
        setActionBar( (Toolbar) findViewById(R.id.toolbar));
    }
}