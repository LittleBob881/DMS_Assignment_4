package com.example.kpopprofileandroidapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BandViewModel extends ViewModel {
    List<LiveData<Band>> bands;
}
