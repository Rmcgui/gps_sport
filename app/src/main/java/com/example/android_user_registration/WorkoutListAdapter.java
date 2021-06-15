package com.example.android_user_registration;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ViewHolder> {

    static String [] fakeData = new String[]{
            "Workout One",
            "Workout Two",
            "Workout Three",
            "Workout Four",
            "Workout Five",
            "Workout Six",
            "Workout Seven",
            "Workout Eight",
            "Workout Nine",
            "Workout Ten"
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_task, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ViewHolder.titleView.setText(fakeData[i]);

    }

    @Override
    public int getItemCount() {
        return fakeData.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        static TextView titleView;

        public ViewHolder(CardView card) {
            super(card);
            cardView = card;
            titleView = (TextView)card.findViewById(R.id.text1);
        }
    }
}
