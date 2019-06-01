package com.sidia.fabio.zerofila.model;

import com.google.firebase.database.Exclude;

public class Clerk {
    public String name;
    public String description;
    public String type;
    public String establishmentKey;
    public long lastAttendance;
    public long average;
    @Exclude
    public String key;

    public Clerk() {

    }
}
