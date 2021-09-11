// Name: Ryan McGuire
// Class to register new users

package com.example.android_user_registration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private ImageView back;
    private Button signUp;
    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputEditText passwordagain;
    private TextInputEditText weight;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(SignUpActivity.this);

        back = findViewById(R.id.back);
        signUp = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        weight = findViewById(R.id.weight);
        password = findViewById(R.id.password);
        passwordagain = findViewById(R.id.passwordagain);

        // When user clicks sign on
        signUp.setOnClickListener(v -> {
            // Check for valid data entered by user
            if (password.getText().toString().equals(passwordagain.getText().toString()) && !TextUtils.isEmpty(username.getText().toString()))
                signUp(username.getText().toString(), password.getText().toString(), weight.getText().toString());
            else
                Toast.makeText(this, "Passwords don't match. Please check the values you entered are correct.", Toast.LENGTH_SHORT).show();
        });

        back.setOnClickListener(v -> finish());

    }

    // Sign users up to parse
    private void signUp(String username, String password, String weight) {
        progressDialog.show();

        // Create Parse Objects
        ParseUser user = new ParseUser();
        ParseObject userWeight = new ParseObject("UserWeight");
        // Set the user's username and password
        user.setUsername(username);
        user.setPassword(password);
        // Set the user's weight, add to UserWeight class
        userWeight.put("weight", weight);
        userWeight.put("username", user.getUsername());

        // save to db table
        userWeight.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //success
                } else {
                    //Error
                }
            }
        });

        // sign user up
        user.signUpInBackground(e -> {
            progressDialog.dismiss();
            // if no error, register user
            if (e == null) {
                showAlert("Successful Sign Up ! You logged in...\n", "Welcome " + username + " !");
            } else {
                // otherwise throw exception
                ParseUser.logOut();
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Begin main activity
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(SignUpActivity.this, MainActivityLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}