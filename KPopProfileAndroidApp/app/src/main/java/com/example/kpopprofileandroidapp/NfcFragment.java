package com.example.kpopprofileandroidapp;

import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.security.auth.callback.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NfcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NfcFragment extends Fragment implements View.OnClickListener {

    Button sendFriendRequestButton;

    public NfcFragment() {
        // Required empty public constructor
    }

    public static NfcFragment newInstance(String param1, String param2) {
        NfcFragment fragment = new NfcFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nfc, container, false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onClick(View v) {
        //TODO: add NFC handler for ANDROID BEAM


    }
}