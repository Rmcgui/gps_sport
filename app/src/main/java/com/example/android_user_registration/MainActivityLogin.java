// Name: Ryan McGuire
// Class that handles main activity UI interactions
package com.example.android_user_registration;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.android_user_registration.interfaces.OnItemClickListener;
import com.example.android_user_registration.ui.workouts.TrainingSessions;
import com.example.android_user_registration.ui.workouts.WorkoutsFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_user_registration.databinding.ActivityMainLoginBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogin extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainLoginBinding binding;
    public ArrayList<TrainingSessions> sessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configure drawer
        setSupportActionBar(binding.appBarMainActivityLogin.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_workouts)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_activity_login);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_login, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_activity_login);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // control what happens when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        // TODO: Insert methods for accessing other menu items, such as 'Settings' etc.
        switch (item.getItemId()) {
            case R.id.action_bio:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                logout("So, you're going...", "Ok...Bye-bye then");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // sign out of parse and the application
    private void logout(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityLogin.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // what activity should app change to? - Login screen
                        Intent intent = new Intent(MainActivityLogin.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}