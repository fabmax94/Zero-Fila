package com.sidia.fabio.zerofila.repository.impl;

import android.arch.lifecycle.MutableLiveData;
import android.support.constraint.solver.widgets.Snapshot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofila.model.Establishment;
import com.sidia.fabio.zerofila.repository.IEstablishmentRepository;

import java.util.ArrayList;
import java.util.List;

public class EstablishmentRepository implements IEstablishmentRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_ESTABLISHMENT = "establishment";

    private MutableLiveData<Establishment> mLiveData;
    private MutableLiveData<List<Establishment>> mLiveListData;

    public EstablishmentRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_ESTABLISHMENT);
        mLiveData = new MutableLiveData<>();
        mLiveListData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Establishment> findByUser(String key) {
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Establishment u = dataSnapshot.getValue(Establishment.class);
                    u.key = dataSnapshot.getKey();
                    mLiveData.setValue(u);
                } else {
                    mLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public MutableLiveData<List<Establishment>> findAllByQyery(String query) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Establishment> establishments = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Establishment u = snapshot.getValue(Establishment.class);
                    u.key = snapshot.getKey();
                    if(!u.name.isEmpty()) {
                        establishments.add(u);
                    }
                }
                mLiveListData.setValue(establishments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveListData;
    }
}
