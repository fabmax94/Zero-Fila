package com.sidia.fabio.zerofila.model;

import com.google.firebase.database.Exclude;


public class ItemQueue {
    public int index;
    public String cpf;
    public String clerkKey;
    public String clerkName;
    public boolean isAttendance;
    @Exclude
    public String key;

    public ItemQueue(String cpf, String clerkKey, String clerkName, int index) {
        this.index = index;
        this.cpf = cpf;
        this.clerkKey = clerkKey;
        this.clerkName = clerkName;
        isAttendance = false;
    }

    public ItemQueue() {

    }
}
