package com.sidia.fabio.zerofila.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.sidia.fabio.zerofila.model.Client;
import com.sidia.fabio.zerofila.persistence.AppDatabase;
import com.sidia.fabio.zerofila.repository.IClientRepository;
import com.sidia.fabio.zerofila.repository.impl.ClientRepository;

public class ClientViewModel extends AndroidViewModel {

    private IClientRepository mUserRepository;
    private AppDatabase app;

    public ClientViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new ClientRepository();
        this.app = Room.databaseBuilder(application.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_ZERO).build();

    }

    public MutableLiveData<Boolean> getAuthenticatedLogin(Client user) {
        return mUserRepository.isExist(user);
    }

    public void setSession(Client session) {
        this.app.clientDAO().deleteAll();
        this.app.clientDAO().save(session);
    }

    public LiveData<Client> getClient() {
        return this.app.clientDAO().getClient();
    }

    public void deleteUser() {
        this.app.clientDAO().deleteAll();
    }

    public void logout() {
        this.app.clientDAO().deleteAll();
    }
}
