package com.example.android_user_registration.ui.workouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// use this class to interact with database
// create instance of database for reading
public class WorkoutsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public WorkoutsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is workouts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}