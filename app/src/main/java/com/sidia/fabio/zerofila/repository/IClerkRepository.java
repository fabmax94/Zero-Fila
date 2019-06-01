package com.sidia.fabio.zerofila.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofila.model.Clerk;
import com.sidia.fabio.zerofila.model.Establishment;

import java.util.List;

public interface IClerkRepository {
    MutableLiveData<List<Clerk>> findByEstablishment(String key);
    void add(Clerk clerk);
    MutableLiveData<Clerk> findByKey(String key);
}
