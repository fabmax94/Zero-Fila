package com.sidia.fabio.zerofila.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.sidia.fabio.zerofila.model.Clerk;
import com.sidia.fabio.zerofila.model.Establishment;
import com.sidia.fabio.zerofila.model.RequestQueue;
import com.sidia.fabio.zerofila.repository.IClerkRepository;
import com.sidia.fabio.zerofila.repository.IEstablishmentRepository;
import com.sidia.fabio.zerofila.repository.IItemQueueRepository;
import com.sidia.fabio.zerofila.repository.IRequestQueueRepository;
import com.sidia.fabio.zerofila.repository.impl.ClerkRepository;
import com.sidia.fabio.zerofila.repository.impl.EstablishmentRepository;
import com.sidia.fabio.zerofila.repository.impl.ItemQueueRepository;
import com.sidia.fabio.zerofila.repository.impl.RequestQueueRepository;

import java.util.List;

public class QueueViewModel extends AndroidViewModel {
    private IEstablishmentRepository establishmentRepository;
    private IClerkRepository clerkRepository;
    private IRequestQueueRepository requestQueueRepository;
    private IItemQueueRepository itemQueueRepository;

    public QueueViewModel(@NonNull Application application) {
        super(application);
        establishmentRepository = new EstablishmentRepository();
        clerkRepository = new ClerkRepository();
        requestQueueRepository = new RequestQueueRepository();
        itemQueueRepository = new ItemQueueRepository();
    }

    public LiveData<List<Establishment>> getEstablishment() {
        return establishmentRepository.findAllByQyery(null);
    }

    public MutableLiveData<Establishment> getEstablishment(String user) {
        return establishmentRepository.findByUser(user);
    }

    public MutableLiveData<Clerk> getClerk(String key) {
        return clerkRepository.findByKey(key);
    }

    public LiveData<List<Clerk>> getQueue(String key) {
        return clerkRepository.findByEstablishment(key);
    }

    public void addRequest(RequestQueue requestQueue) {
        requestQueueRepository.add(requestQueue);
    }

    public LiveData<Pair<Integer, Boolean>> getItemQueue(String key, String cpf) {
        return itemQueueRepository.getQueueLength(key, cpf);
    }
}
