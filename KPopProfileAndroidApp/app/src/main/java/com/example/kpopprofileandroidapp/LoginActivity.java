package com.example.kpopprofileandroidapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    EditText usernameInput;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        nextButton = (Button) findViewById(R.id.login_next_button);
        usernameInput = (EditText) findViewById(R.id.usernameTextField);

        //do some input validation here..
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameInput.getText().length() == 0)
                {
                    usernameInput.setError("Please enter a username");
                }
                //TODO: else... do a network function? should it be triggered?
            }
        });
        }
    }