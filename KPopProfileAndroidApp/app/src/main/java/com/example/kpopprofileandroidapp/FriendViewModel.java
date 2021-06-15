package com.example.kpopprofileandroidapp;

import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
    ViewModel Class to handle a user's friends and make the connection to RESTful service. Used for main activity to show
     friend list and for NFC activity to add new friends for a user.
 */
public class FriendViewModel extends ViewModel {
    private static String KPopProfileServiceFriendResourceURL = "http://192.168.1.205:8080/KPopProfileService/kpopService/friends/";
    private static String POSTAddFriendsURLFragment = "addfriend";
    private static String GETFriendsURLFragment = "friend/";

    List<String> friends;
    public boolean addFriends(String username1, String username2) {
        String parameters[] = new String[3];
        parameters[0] = "POST";
        parameters[1] = username1;
        parameters[2] = username2;

        FriendTask friendTask = new FriendTask();
        String response = "";
        try {
            response = friendTask.execute(parameters).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.parseBoolean(response);
    }

    public void getFriends(String username)
    {
        friends = new ArrayList<>();
        FriendTask friendTask = new FriendTask();
        String parameters[] = new String[2];
        parameters[0] = "GET";
        parameters[1] = username;

        try {
            String response = friendTask.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray friendJsonArray = jsonObject.getJSONArray("friends");

            for(int i = 0; i < friendJsonArray.length(); i++)
            {
                //retrieve object in JSON array
                JSONObject friendObject = friendJsonArray.getJSONObject(i);

                //add name of  favourite bands
                String friendName = friendObject.getString("username");
                friends.add(friendName);
            }

            System.out.println("FRIENDS SIZE -> "+friends.size());
       } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class FriendTask extends AsyncTask<String, Void, String> {

        String stringResponse = "";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Request restRequest = null;

            //check if GET or POST
            if(strings[0].equalsIgnoreCase("GET")) {
                HttpUrl url = HttpUrl.parse(KPopProfileServiceFriendResourceURL+GETFriendsURLFragment+strings[1]);

                restRequest = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
            }

            else if(strings[0].equalsIgnoreCase("POST"))
            {
                //add both usernames as form params
                RequestBody body = new FormBody.Builder()
                        .addEncoded("username1", strings[1])
                        .addEncoded("username2", strings[2])
                        .build();

               HttpUrl url = HttpUrl.parse(KPopProfileServiceFriendResourceURL+POSTAddFriendsURLFragment);


                restRequest = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            }


            System.out.println(strings[0]+" call to friend resource made");

            try {
                Response response = client.newCall(restRequest).execute();
                ResponseBody responseBody = response.body();

                if(response.isSuccessful())
                {
                    stringResponse = response.body().string();

                }
                else {
                    System.err.println("Something went wrong with the RESTFUL method request."+response.code());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return stringResponse;
        }
    }
}

