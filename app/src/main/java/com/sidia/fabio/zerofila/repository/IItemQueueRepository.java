package com.sidia.fabio.zerofila.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Pair;

import com.sidia.fabio.zerofila.util.OnWidgetHandle;

public interface IItemQueueRepository {
    MutableLiveData<Pair<Integer, Boolean>> getQueueLength(final String key, final String cpf);

    void getQueueLength(final String cpf, final OnWidgetHandle handle);
}
