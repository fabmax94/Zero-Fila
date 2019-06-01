package com.sidia.fabio.zerofila.model;

import com.google.firebase.database.Exclude;

public class Establishment {
    public String name;
    public String local;
    public double latitude;
    public double longitude;
    public String type;

    @Exclude
    public String key;

    public Establishment() {
    }
}
