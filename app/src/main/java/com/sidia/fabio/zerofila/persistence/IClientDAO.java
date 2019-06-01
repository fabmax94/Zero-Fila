package com.sidia.fabio.zerofila.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sidia.fabio.zerofila.model.Client;

@Dao
public interface IClientDAO {
    @Insert
    void save(Client user);

    @Query("DELETE FROM Client")
    void deleteAll();

    @Query("SELECT * FROM Client LIMIT 1")
    LiveData<Client> getClient();

    @Query("SELECT * FROM Client LIMIT 1")
    Client getClientSync();
}