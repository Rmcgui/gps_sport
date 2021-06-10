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

import com.example.android_user_registration.R;
import com.example.android_user_registration.databinding.FragmentSlideshowBinding;
import com.example.android_user_registration.databinding.FragmentWorkoutsBinding;
import com.example.android_user_registration.ui.slideshow.SlideshowViewModel;

public class WorkoutsFragment extends Fragment {

    private WorkoutsViewModel workoutsViewModel;
    private FragmentWorkoutsBinding binding;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workoutsViewModel =
                new ViewModelProvider(this).get(WorkoutsViewModel.class);

        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textWorkouts;
        workoutsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}