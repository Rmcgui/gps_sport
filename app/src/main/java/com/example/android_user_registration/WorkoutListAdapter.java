package com.example.android_user_registration;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_user_registration.interfaces.OnItemClickListener;
import com.example.android_user_registration.ui.workouts.TrainingSessions;

import java.util.ArrayList;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ListViewHolder> {

  //  WorkoutListData[numberOfItems++] = newItem;
//    // initlialise empty Data array
//    WorkoutsFragment fragment = new WorkoutsFragment();
    private ArrayList<TrainingSessions> sessions;
    private OnItemClickListener clickListener;
    View.OnClickListener listener;
    // pass array list of our data
    public String workoutId;

    public WorkoutListAdapter(ArrayList<TrainingSessions> list, OnItemClickListener listener){
        //this.context = context;
        this.sessions = list;
        this.clickListener = listener;
    }

    public WorkoutListAdapter() {

    }

    // METHOD OK
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_task, parent, false);
        ListViewHolder vh = new ListViewHolder(v);
//        v.setOnClickListener(listener);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        ((ListViewHolder) viewHolder).bindView(position);



//        viewHolder.getDateView().setText(currentItem.getDateTime());
//        viewHolder.getDistanceView().setText(currentItem.getDistance());
        // set the click action
        // when workout is selected by user.
        // TODO: Load unique workout based on userId and workoutID
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // get the workoutId
                        workoutId = sessions.get(position).getObjectId();
                        // Send to Fragment.
                        clickListener.OnItemClick(workoutId);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }



    // BEGIN OTHER CLASS
    // For recycler view cards.
    public class ListViewHolder extends RecyclerView.ViewHolder {
        private  TextView dateView;
        private  TextView distanceView;
        // instance of interface


        public ListViewHolder(final View itemView) {
            super(itemView);
            dateView = (TextView)itemView.findViewById(R.id.text1);
            distanceView = (TextView)itemView.findViewById(R.id.text2);
            //itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            dateView.setText(sessions.get(position).getDateTime());
            distanceView.setText(sessions.get(position).getDistance());
        }

        public TextView getDateView(){
            return dateView;
        }

        public TextView getDistanceView(){
            return distanceView;
        }


//        @Override
//        public void onClick(View v) {
//            clickListener.OnItemClick(workoutId);
//        }
    }
}

//
//    // initlialise Data array
//    //String [] WorkoutListData = new String[]{};
//    int numberOfItems = 0;
//    ParseQuery query = ParseQuery.getQuery("TrainingSessions");
//
//        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
//                try{
//                ParseObject dates = query.getFirst();
//                ParseQuery<ParseObject> dateQuery = new ParseQuery<ParseObject>("dateTime");
//
//        } catch (ParseException e) {
//        e.printStackTrace();
//        }
//        query.whereContains("userId",ParseUser.getCurrentUser().getObjectId() );
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//public void done(ParseObject player, ParseException e) {
//        if (e == null) {
//        // TODO: Save dates and workout ID as Key value pairs
//        // Display both on the recycler view workout tab
//        //WorkoutListData[numberOfItems++] = player.getString("dateTime");
//
//        } else {
//        // something went wrong
//        System.out.println("Error " + e.getMessage());
//        }
//        }
//        });