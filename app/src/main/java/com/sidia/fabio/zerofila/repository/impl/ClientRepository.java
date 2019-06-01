package com.sidia.fabio.zerofila.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofila.model.Client;
import com.sidia.fabio.zerofila.repository.IClientRepository;

public class ClientRepository implements IClientRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_CLIENT = "client";

    private MutableLiveData<Boolean> mLiveData;

    public ClientRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_CLIENT);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public void add(Client user) {
        this.reference.child(user.getCpf()).setValue(user);
    }

    @Override
    public MutableLiveData<Boolean> isExist(final Client user) {
        reference.child(user.getCpf()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    add(user);
                }
                mLiveData.setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

}
