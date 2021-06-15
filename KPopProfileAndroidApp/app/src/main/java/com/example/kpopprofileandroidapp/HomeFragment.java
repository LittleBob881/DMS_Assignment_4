package com.example.kpopprofileandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLOutput;

public class HomeFragment extends Fragment {

    TextView friendTextView;
    Button goToNFCFriendActivity;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = view.findViewById(R.id.welcomeText);

        MainActivity activity = (MainActivity) getActivity();

        textView.setText("Welcome, " + activity.username+ "!");

        goToNFCFriendActivity = view.findViewById(R.id.addFriendNFCButton);
        goToNFCFriendActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //set listener to start NFC activity if "Send Friend Request" button clicked
                System.out.println("Navigating to main activity..");
                Intent intent = new Intent(view.getContext(), NFCActivity.class);
                intent.putExtra("username",  activity.username);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //link viewmodel to this fragment
        FriendViewModel viewModel = ViewModelProviders.of(this).get(FriendViewModel.class);

        MainActivity activity = (MainActivity) getActivity();
        friendTextView = view.findViewById(R.id.friendsListTextView);
        //get user friends
        viewModel.getFriends(activity.username);

        if(viewModel.friends.isEmpty())
        {
            friendTextView.setText("No friends :(");
        }
        else
        {
            for(String name : viewModel.friends)
            {
                friendTextView.append("\n"+name);
            }
        }
    }
}
