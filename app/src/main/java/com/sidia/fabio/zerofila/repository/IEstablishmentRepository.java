package com.sidia.fabio.zerofila.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofila.model.Establishment;

import java.util.List;

public interface IEstablishmentRepository {
    MutableLiveData<Establishment> findByUser(String key);
    MutableLiveData<List<Establishment>> findAllByQyery(String query);
}
