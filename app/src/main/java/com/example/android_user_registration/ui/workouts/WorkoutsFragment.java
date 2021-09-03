package com.example.android_user_registration.ui.workouts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_user_registration.R;
import com.example.android_user_registration.WorkoutListAdapter;
import com.example.android_user_registration.databinding.FragmentSlideshowBinding;
import com.example.android_user_registration.databinding.FragmentWorkoutsBinding;
import com.example.android_user_registration.ui.slideshow.SlideshowViewModel;

public class WorkoutsFragment extends Fragment {

    private WorkoutsViewModel workoutsViewModel;
    private FragmentWorkoutsBinding binding;
    // for displaying workouts
    RecyclerView recyclerView;
    WorkoutListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        adapter = new WorkoutListAdapter();
    }

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        // initialise view model
//        workoutsViewModel =
//                new ViewModelProvider(this).get(WorkoutsViewModel.class);

        container.removeAllViews();

        // create recycler view for workouts
        final View v = inflater.inflate(R.layout.fragment_workouts, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((
                new LinearLayoutManager(getActivity())
                ));

        return v;
    }

}