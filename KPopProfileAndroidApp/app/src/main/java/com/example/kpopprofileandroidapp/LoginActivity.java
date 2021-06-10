package com.example.kpopprofileandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends Activity {

    EditText usernameInput;
    Button nextButton;
    private static String KPopProfileServiceLoginURL= "http://10.0.2.2:8080/KPopProfileService/kpopService/userprofile/login";

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
                //TODO: else... connect to RESTFul KPopProfileService
                else
                {
                    //connect to RESTful service using OkHttpClient wrapped in "LoginTask"
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(usernameInput.getText().toString());
                }
            }
        });
        }

        public class LoginTask extends AsyncTask<String, Void, String>{

            OkHttpClient client = new OkHttpClient();

            public boolean runPostRequest(String... inputs)
            {
                boolean POSTsuccess = false;
                //add username input as a form parameter for server to retrieve
                RequestBody body = new FormBody.Builder()
                        .addEncoded("username", inputs[0])
                        .build();

                System.out.println("request body built "+inputs[0]);

                Request postRequest = new Request.Builder()
                        .url(KPopProfileServiceLoginURL)
                        .post(body)
                        .build();

                //execute request to RESTful service
                Call call = client.newCall(postRequest);
                System.out.println("call made");
                try {
                    Response response = client.newCall(postRequest).execute();
                    ResponseBody responseBody = response.body();

                    if(response.isSuccessful())
                    {
                        Headers responseHeaders = response.headers();
                        POSTsuccess = true;
                    }
                    else {
                        System.err.println("Something went wrong with the POST request."+response.code());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return POSTsuccess;
            }


            @Override
            protected String doInBackground(String... strings) {
                return Boolean.toString(runPostRequest(strings));
            }

            @Override
            protected void onPostExecute(String result)
            {
                boolean success = Boolean.parseBoolean(result);

                //take to main page, pass username through
                if(success)
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", usernameInput.toString());

                }
                else
                {
                    usernameInput.setError("Something went wrong with logging in. Please try again.");
                }
            }
        }

}
