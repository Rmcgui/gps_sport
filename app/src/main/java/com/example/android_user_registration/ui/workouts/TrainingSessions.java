package com.example.android_user_registration.ui.workouts;

// MODEL
// use this class to interact with database
// create instance of database for reading
public class TrainingSessions {

    private String mUserId;
    private String mDateTime;
    private String mDistance;
    private String mDuration;
    private String mAvgSpeed;
    private String mAvgPace;
    private String mCalsBurned;

    public TrainingSessions (){

    }

    public TrainingSessions(String userId, String dateTime, String distance,
                            String duration, String avgSpeed,String avgPace,String calsBurned){
        mUserId = userId;
        mDateTime = dateTime;
        mDistance = distance;
        mDuration = duration;
        mAvgSpeed = avgSpeed;
        mAvgPace = avgPace;
        mCalsBurned = calsBurned;
    }


    public String getUserId(){
        return mUserId;
    }
    public String getDateTime(){
        return mDateTime;
    }

    public String getDistance(){
        return mDistance;
    }

    public String getDuration(){
        return mDuration;
    }

    public String getAvgSpeed(){
        return mAvgSpeed;
    }

}