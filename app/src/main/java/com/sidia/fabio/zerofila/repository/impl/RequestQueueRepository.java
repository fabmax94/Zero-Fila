package com.sidia.fabio.zerofila.repository.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sidia.fabio.zerofila.model.RequestQueue;
import com.sidia.fabio.zerofila.repository.IRequestQueueRepository;

public class RequestQueueRepository implements IRequestQueueRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_REQUEST = "request_queue";
    public RequestQueueRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_REQUEST);
    }

    @Override
    public void add(RequestQueue requestQueue) {
        reference.push().setValue(requestQueue);

    }
}
