package com.example.kpopprofileandroidapp;

import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
    ViewModel Class to handle the Kpop Band Data and make the connection to RESTful service. Fragments can retrieve
    the data from this class's methods.
 */
public class BandViewModel extends ViewModel {      //my laptop ip address - 192.168.1.205
    private static String KPopProfileServiceBandResourceURL= "http://10.0.2.2:8080/KPopProfileService/kpopService/bands/";
    private static String GETFavouriteBandsURLFragment = "favourite/";
    private static String POSTFavouriteBandUrlFragment = "addfavourite";


    public List<Band> bandList = new ArrayList<>();
    public List<String> favouriteBands = new ArrayList<>();

    // is triggered when activity or framgnent is linked to an instance of this viewmodel class.

    public void initialiseAllBands()
    {
        BandTask bandTask = new BandTask();

        try {
            String response = bandTask.execute("GET").get();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray bands = jsonObject.getJSONArray("bands");

            for(int i = 0; i < bands.length(); i++)
            {
                //retrieve object in JSON array
                JSONObject bandObject = bands.getJSONObject(i);
                //map to local Band domain by retrieving values
                String bandName = bandObject.getString("name");
                int year = bandObject.getInt("year");
                int generation = bandObject.getInt("generation");
                String fandomName = bandObject.getString("fandomName");

                Band band = new Band(bandName, year, generation, fandomName);
                bandList.add(band);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getFavouriteBands(String username)
    {
        BandTask bandTask = new BandTask();
        String parameters[] = new String[2];
        parameters[0] = "GET";
        parameters[1] = username;

        try {
            String response = bandTask.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray favBands = jsonObject.getJSONArray("bands");

            for(int i = 0; i < favBands.length(); i++)
            {
                //retrieve object in JSON array
                JSONObject bandObject = favBands.getJSONObject(i);

                //add name of  favourite bands
                String bandName = bandObject.getString("name");
                favouriteBands.add(bandName);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean addFavouriteBand(String username, String bandName)
    {
        String parameters[] = new String[3];
        parameters[0] = "POST";
        parameters[1] = username;
        parameters[2] = bandName;

        BandTask bandTask = new BandTask();
        String response = "";
        try {
             response = bandTask.execute(parameters).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.parseBoolean(response);
    }

    public class BandTask extends AsyncTask<String, Void, String> {

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
            if(strings[0].equalsIgnoreCase("GET"))
            {
                //if GET has parameters (get list of favourite band)
                if(strings.length == 2) {
                    restRequest = new Request.Builder()
                            .url(KPopProfileServiceBandResourceURL + GETFavouriteBandsURLFragment + strings[1])
                            .get()
                            .build();

                }
                else
                {
                    restRequest = new Request.Builder()
                            .url(KPopProfileServiceBandResourceURL)
                            .get()
                            .build();
                }

            }
            //if POST request, i.e. add a new favourite band for the username.
            else if(strings[0].equalsIgnoreCase("POST"))
            {
                //add username and band name as a form parameters for  server to retrieve
                RequestBody body = new FormBody.Builder()
                        .addEncoded("username", strings[1])
                        .addEncoded("bandName", strings[2])
                        .build();

                restRequest = new Request.Builder()
                        .url(KPopProfileServiceBandResourceURL + POSTFavouriteBandUrlFragment)
                        .post(body)
                        .build();
            }

            System.out.println(strings[0]+" call to band resource made");

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
    //JSON VERSION
//    JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("username", strings[1]);
//                    jsonObject.put("bandName", strings[2]);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
//
//                //add json as input to POST request
//                RequestBody body = RequestBody.create(jsonType, jsonObject.toString());
