package com.sidia.fabio.zerofila.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofila.model.Client;

public interface IClientRepository {
    void add(Client user);
    MutableLiveData<Boolean> isExist(Client user);
}
