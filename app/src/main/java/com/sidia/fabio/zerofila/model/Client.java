package com.sidia.fabio.zerofila.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

@Entity(tableName = "client")
public class Client {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @Exclude
    public String cpf;
    public String name;

    public Client() {
    }

    public Client(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf.replace(".", "").replace("-", "");
    }
}
