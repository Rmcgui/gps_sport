package com.example.android_user_registration.ui.workouts;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    // for displaying workouts
    private RecyclerView mRecyclerView;
    // for displaying only the items that will fit on screen
    private RecyclerView.Adapter mAdapter;
    // aligning items in list
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<TrainingSessions> sessions = new ArrayList<>();
    public WorkoutListAdapter adapter = new WorkoutListAdapter(sessions);

//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);

//        adapter = new WorkoutListAdapter(sessions);
//    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workouts, container, false);
        sessions = getObjects();
        //mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(adapter);
//        adapter = new WorkoutListAdapter(sessions);
//        mRecyclerView.setAdapter(adapter);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        container.removeAllViews();
        // create recycler view for workouts
        return v;
    }

//    WorkoutsFragment fragment = new WorkoutsFragment();
//    FragmentTransaction fragmentTransaction = getSupport().beginTransaction();
//        fragmentTransaction.add(R.id.recyclerView, fragment);
//        fragmentTransaction.commit();

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
                        sessions.add(new TrainingSessions(objects.get(i).get("userId").toString(),objects.get(i).get("dateTime").toString(),
                                objects.get(i).get("distance").toString(), objects.get(i).get("duration").toString(), objects.get(i).get("avgSpeed").toString(),
                                objects.get(i).get("avgPace").toString(), objects.get(i).get("calsBurned").toString()
                        ));
                        adapter.notifyDataSetChanged();
                        System.out.println("Distance: " +  objects.get(i).get("distance").toString());
                        System.out.println("Duration: " +  objects.get(i).get("duration").toString());
                        System.out.println("avgPace: " +  objects.get(i).get("avgPace").toString());
                        System.out.println("avgSpeed: " +  objects.get(i).get("avgSpeed").toString());
                    }

                }
            }
        });
        return sessions;
    }

    public ArrayList<TrainingSessions> getData(){
        return this.sessions;
    }
}