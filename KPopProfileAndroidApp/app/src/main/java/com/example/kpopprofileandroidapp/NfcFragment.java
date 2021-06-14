package com.example.kpopprofileandroidapp;

import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NfcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NfcFragment extends Fragment implements View.OnClickListener {

    Button sendFriendRequestButton;
    private NfcAdapter nfcAdapter;
    private TextView nfcStatusTextView;
    private TextView nfcResponseView;

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
        sendFriendRequestButton = view.findViewById(R.id.sendFriendNFCButton);
        nfcStatusTextView = view.findViewById(R.id.nfcStatusTextView);

        //check if NFC feature available on phone
        nfcAdapter = NfcAdapter.getDefaultAdapter(this.getContext());
        if(nfcAdapter == null)
        {
            sendFriendRequestButton.setEnabled(false);
            nfcStatusTextView.setTextColor(Color.RED);
            nfcStatusTextView.setText("NFC not available for this device!");
        }
        else
        {
            nfcStatusTextView.setTextColor(Color.RED);
            nfcStatusTextView.setText("NFC not available for this device!");
            sendFriendRequestButton.setOnClickListener(this);
        }



        return view;
    }

    @Override
    public void onClick(View v) {
        //TODO: add NFC handler for ANDROID BEAM

    }
}