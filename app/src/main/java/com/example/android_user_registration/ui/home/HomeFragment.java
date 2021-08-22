package com.example.android_user_registration.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.android_user_registration.LoginActivity;
import com.example.android_user_registration.MainActivityLogin;
import com.example.android_user_registration.R;
import com.example.android_user_registration.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    FloatingActionButton FAB;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // floating action button for prompting user for file from internal storage
        //FloatingActionButton FAB = fragment_home.findViewById(R.id.fab);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FAB = (FloatingActionButton) view.findViewById(R.id.fab);


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // what happens when the button is clicked
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opening file for reading data
                Toast.makeText(getActivity(), "Would you like a coffee?", Toast.LENGTH_SHORT).show();
            }
        });

        //return view;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}