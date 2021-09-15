package com.example.android_user_registration.ui.workouts;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_user_registration.MainActivityLogin;
import com.example.android_user_registration.R;
import com.example.android_user_registration.WorkoutListAdapter;
import com.example.android_user_registration.WorkoutSummary;
import com.example.android_user_registration.interfaces.OnItemClickListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    // for displaying workouts
    private RecyclerView recyclerView;
    //Workouts data
    public ArrayList<TrainingSessions> sessions = new ArrayList<>();

    OnItemClickListener onItemClickListener;

    // When a workout is clicked
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void OnItemClick(String objectId) {
        startActivity((new Intent (getActivity(), WorkoutSummary.class)).putExtra("WORKOUT_ID", objectId));
        }
    };

    // adapter for accessing recycler view data
    public WorkoutListAdapter adapter = new WorkoutListAdapter(sessions, listener);


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workouts, container, false);

        // Generate Data
        sessions = getObjects();
        // Initialise  Recycler View
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(adapter);
        container.removeAllViews();
        // create recycler view for workouts
        return v;
    }

    //  retrieve all workout entries from Database
    public ArrayList getObjects(){
        // query object
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TrainingSessions");
        // set constraint to only look up the current user
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    // loop through objects retrieved from database
                    for(int i = 0; i < objects.size(); i++){
                        // add them to Training Sessions Object Array List
                        // for application to handle
                        sessions.add(new TrainingSessions(objects.get(i).get("userId").toString(),objects.get(i).get("dateTime").toString(),
                                objects.get(i).get("distance").toString(), objects.get(i).get("duration").toString(), objects.get(i).get("avgSpeed").toString(),
                                objects.get(i).get("avgPace").toString(), objects.get(i).get("calsBurned").toString(), objects.get(i).getObjectId()
                        ));
                        // update adapter
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });
        return sessions;
    }


}