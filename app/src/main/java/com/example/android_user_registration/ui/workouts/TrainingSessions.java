package com.example.android_user_registration.ui.workouts;
// Ryan McGuire
// MODEL
// use this class to interact with database
public class TrainingSessions {

    private String mUserId;
    private String mObjectId;
    private String mDateTime;
    private String mDistance;
    private String mDuration;
    private String mAvgSpeed;
    private String mAvgPace;
    private String mCalsBurned;

    public TrainingSessions (){

    }

    public TrainingSessions(String userId, String dateTime, String distance, String duration,
                            String avgSpeed,String avgPace,String calsBurned, String objectId )
    {
        mUserId = userId;
        mDateTime = dateTime;
        mDistance = distance;
        mDuration = duration;
        mAvgSpeed = avgSpeed;
        mAvgPace = avgPace;
        mCalsBurned = calsBurned;
        mObjectId = objectId;
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

    public String getObjectId(){
        return mObjectId;
    }


    public String getDuration(){
        return mDuration;
    }

    public String getAvgSpeed(){
        return mAvgSpeed;
    }

}