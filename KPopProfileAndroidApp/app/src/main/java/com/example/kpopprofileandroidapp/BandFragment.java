package com.example.kpopprofileandroidapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BandFragment extends Fragment {

    RecyclerView bandRecyclerView;
    public BandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_band, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
//        bottomNavigationView.setSelectedItemId(R.id.bandFragment);

        //link viewmodel to this fragment
        BandViewModel viewModel = ViewModelProviders.of(this).get(BandViewModel.class);

        MainActivity activity = (MainActivity) getActivity();

        //make viewmodel do a GET request to fetch all bands and band favourites for this user
        viewModel.initialiseAllBands();
        viewModel.getFavouriteBands(activity.username);

        //assign recycler view to show band list
        bandRecyclerView = view.findViewById(R.id.bandRecyclerView);


        //dynamic adapter
        BandRecyclerViewAdapter adapter = new BandRecyclerViewAdapter(this.getContext(), viewModel.bandList, viewModel.favouriteBands);
        bandRecyclerView.setAdapter(adapter);
        bandRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}