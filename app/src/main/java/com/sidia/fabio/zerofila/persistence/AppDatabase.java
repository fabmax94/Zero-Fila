package com.sidia.fabio.zerofila.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sidia.fabio.zerofila.model.Client;

@Database(entities = {Client.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_ZERO = "database-zero-client";
    public abstract IClientDAO clientDAO();
}
