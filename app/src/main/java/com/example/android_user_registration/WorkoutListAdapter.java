package com.example.android_user_registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_user_registration.interfaces.OnViewWorkout;
import com.example.android_user_registration.ui.workouts.TrainingSessions;
import com.example.android_user_registration.ui.workouts.WorkoutsFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ListViewHolder> {

  //  WorkoutListData[numberOfItems++] = newItem;
    // initlialise empty Data array

    ArrayList<TrainingSessions> sessions = WorkoutsFragment.getObjects();
    Context mcontext;
    // pass array list of our data
    public WorkoutListAdapter(ArrayList<TrainingSessions> list ){
        this.sessions = list;
    }

    // METHOD OK
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
 //       ((ListViewHolder) viewHolder).bindView(position);
        TrainingSessions currentItem = sessions.get(position);

        viewHolder.getDateView().setText(currentItem.getDateTime());
        viewHolder.getDistanceView().setText(currentItem.getDistance());

 //       final Context context = viewHolder.dateView.getContext();
        // set the click action
        // when workout is selected by user.
        // TODO: Load unique workout based on userId and workoutID
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ((OnViewWorkout) context).viewWorkout(position);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }



    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private  TextView dateView;
        private  TextView distanceView;

        public ListViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView)itemView.findViewById(R.id.text1);
            distanceView = (TextView)itemView.findViewById(R.id.text2);
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


        @Override
        public void onClick(View v) {

        }
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