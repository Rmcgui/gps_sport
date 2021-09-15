package com.example.android_user_registration;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_user_registration.interfaces.OnItemClickListener;
import com.example.android_user_registration.ui.workouts.TrainingSessions;

import java.util.ArrayList;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ListViewHolder> {
    // display data
    private ArrayList<TrainingSessions> sessions;
    // interface
    private OnItemClickListener clickListener;
    // unique workout identifier
    public String workoutId;

    public WorkoutListAdapter(ArrayList<TrainingSessions> list, OnItemClickListener listener){
        // set list objects
        this.sessions = list;
        // set click listener
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_task, parent, false);
        ListViewHolder vh = new ListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        ((ListViewHolder) viewHolder).bindView(position);
        // set the click action
        // when workout is selected by user.
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
    // For recycler view cards that display a summary of each workout
    public class ListViewHolder extends RecyclerView.ViewHolder {
        private  TextView dateView;
        private  TextView distanceView;
        // instance of interface
        public ListViewHolder(final View itemView) {
            super(itemView);
            dateView = (TextView)itemView.findViewById(R.id.text1);
            distanceView = (TextView)itemView.findViewById(R.id.text2);
        }
        // bind data
        public void bindView(int position){
            dateView.setText(sessions.get(position).getDateTime());
            distanceView.setText(sessions.get(position).getDistance());
        }
    }
}