package com.example.kpopprofileandroidapp;

public class Band {
    private String name;
    private int year;
    private int generation;
    private String fandomName;

    public Band(String name, int year, int generation, String fandomName) {
        this.name = name;
        this.year = year;
        this.generation = generation;
        this.fandomName = fandomName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFandomName() {
        return fandomName;
    }

    public void setFandomName(String fandomName) {
        this.fandomName = fandomName;
    }
}
