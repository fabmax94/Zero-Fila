package com.sidia.fabio.zerofila.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofila.model.Clerk;
import com.sidia.fabio.zerofila.repository.IClerkRepository;

import java.util.ArrayList;
import java.util.List;

public class ClerkRepository implements IClerkRepository {

    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_CLERK = "clerk";

    private MutableLiveData<List<Clerk>> mLiveListData;
    private MutableLiveData<Clerk> mLiveData;

    public ClerkRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_CLERK);
        mLiveListData = new MutableLiveData<>();
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<List<Clerk>> findByEstablishment(final String key) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Clerk> clerks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clerk u = snapshot.getValue(Clerk.class);
                    u.key = snapshot.getKey();
                    assert u != null;
                    if (u.establishmentKey.equals(key)) {
                        clerks.add(u);
                    }
                }
                mLiveListData.setValue(clerks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveListData;
    }

    @Override
    public void add(Clerk clerk) {
        this.reference.child(clerk.key).setValue(clerk);
    }

    @Override
    public MutableLiveData<Clerk> findByKey(String key) {
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Clerk u = dataSnapshot.getValue(Clerk.class);
                u.key = dataSnapshot.getKey();
                mLiveData.setValue(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }
}
