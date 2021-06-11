package com.example.kpopprofileandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
    This Activity acts as a listener for the bottom navigation, which uses the NavController to navigate between different fragments
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Bundle args = new Bundle();

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        args.putString("username", getIntent().getExtras().getString("username"));
//        initialise bottom navigation
//        create bottom navigation object and obtain controller
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


}
