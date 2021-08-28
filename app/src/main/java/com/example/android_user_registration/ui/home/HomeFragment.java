package com.example.android_user_registration.ui.home;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.android_user_registration.LoginActivity;
import com.example.android_user_registration.MainActivityLogin;
import com.example.android_user_registration.R;
import com.example.android_user_registration.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int HOMEFRAGMENT_RESULT_CODE = 1;
    private static final int RESULT_OK = 1;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FloatingActionButton FAB;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // floating action button for prompting user for file from internal storage
        //FloatingActionButton FAB = fragment_home.findViewById(R.id.fab);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Assign FAB
        FAB = (FloatingActionButton) view.findViewById(R.id.fab);


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // what happens when the button is clicked??
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, HOMEFRAGMENT_RESULT_CODE);



                // Check condition
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // No permissions granted?
//                    // Request permissions
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.CAMERA}, 1);
//                } else {
//                    // when permission granted
//                    // Create file Picker method
//                    Intent intent = new Intent(getActivity(), FilePickerActivity.class);
//                    // put extra
//                    intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
//                            .setCheckPermission(true).
//                            setShowFiles(true).
//                            setShowImages(false)
//                            .setShowVideos(true)
//                            .setMaxSelection(1)
//                            .setSuffixes("txt", "xml", "doc", "docx")
//                            .setSkipZeroSizeFiles(true)
//                            .build());
//                    // start activity result
//                    //
//                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
//                }
                // prompt user to select a file. Store the directory once selected as fileName,
                // then open for reading and processing
                //Toast.makeText(getActivity(), "Would you like a coffee?", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
        //return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check condition

        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            //When permission granted, call file picker method
            //filePicker();
            Toast.makeText(getActivity().getApplicationContext(), "Permission Granted"
                    , Toast.LENGTH_SHORT).show();
        } else {
          // permission denied by user, display error
            Toast.makeText(getActivity().getApplicationContext(), "Permission Denied"
            , Toast.LENGTH_SHORT).show();

        }
    }

    // When a file is selected?
    // Call method to read the file
    // Process the data
    // Store in backend database
    // Add new workout to Workours fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case HOMEFRAGMENT_RESULT_CODE:
                if(resultCode==-1){
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    Toast.makeText(getActivity(), filePath,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    //
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Check condition
//        if (resultCode == RESULT_OK && data != null){
//
//            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(
//                    FilePickerActivity.MEDIA_FILES
//            );
//            // Get String Path
//            String path = mediaFiles.get(0).getPath();
//            Toast.makeText(getActivity().getApplicationContext(), path
//                    , Toast.LENGTH_SHORT).show();
//
//
//            // Open file for reading on this file path
//
//        }
//    }

    //    private void filePicker() {
//        //
//        Intent intent = new Intent(getActivity(), FilePickerActivity.CONFIGS,
//                new Configurations.Builder()
//                        .setCheckPermission(true).
//                        setShowFiles(true).
//                        setShowImages(false)
//                        .setShowVideos(true)
//                        .setMaxSelection(1)
//                        .setSuffixes("txt", "xml", "doc", "docx")
//                        .setSkipZeroSizeFiles(true)
//                        .build());
//    }

}