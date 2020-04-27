package com.project.bigdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Key {
    private String borough;
    private String date;

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Key(String borough, String date) {
        this.borough = borough;
        this.date = date;
    }

    public Key() {
    }

    @Override
    public String toString() {
        return this.getBorough() + " " + getDate() + " ";
    }


}
