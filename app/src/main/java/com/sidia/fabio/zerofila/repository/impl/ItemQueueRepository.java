package com.sidia.fabio.zerofila.repository.impl;

import android.arch.lifecycle.MutableLiveData;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofila.model.ItemQueue;
import com.sidia.fabio.zerofila.repository.IItemQueueRepository;
import com.sidia.fabio.zerofila.util.OnWidgetHandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemQueueRepository implements IItemQueueRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_ITEM = "item_queue";
    private MutableLiveData<Pair<Integer, Boolean>> mLiveData;

    public ItemQueueRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_ITEM);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Pair<Integer, Boolean>> getQueueLength(final String key, final String cpf) {
        reference.orderByChild("clerkKey").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer length = 0;
                Boolean onList = false, isAttendance = false;
                List<ItemQueue> itemQueues = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemQueue itemQueue = snapshot.getValue(ItemQueue.class);
                    itemQueues.add(itemQueue);
                }
                Collections.sort(itemQueues, new Comparator<ItemQueue>() {
                    @Override
                    public int compare(final ItemQueue object1, final ItemQueue object2) {
                        return object1.index < object2.index ? -1 : 1;
                    }
                });
                for (ItemQueue itemQueue : itemQueues) {
                    if (itemQueue.cpf.equals(cpf)) {
                        isAttendance = itemQueue.isAttendance;
                        onList = true;
                        break;
                    } else {
                        length++;
                    }
                }
                if (onList) {
                    mLiveData.setValue(new Pair<>(length, isAttendance));
                } else {
                    mLiveData.setValue(new Pair<>(-1, isAttendance));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public void getQueueLength(final String cpf, final OnWidgetHandle handle) {
        reference.orderByChild("clerkKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer length = 0;
                Boolean onList = false, isAttendance = false;
                List<ItemQueue> itemQueues = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemQueue itemQueue = snapshot.getValue(ItemQueue.class);
                    itemQueues.add(itemQueue);
                }
                Collections.sort(itemQueues, new Comparator<ItemQueue>() {
                    @Override
                    public int compare(final ItemQueue object1, final ItemQueue object2) {
                        return object1.index < object2.index ? -1 : 1;
                    }
                });
                String clerk = "";
                for (ItemQueue itemQueue : itemQueues) {
                    if (itemQueue.cpf.equals(cpf)) {
                        isAttendance = itemQueue.isAttendance;
                        onList = true;
                        break;
                    } else {
                        if(clerk.equals(itemQueue.clerkKey)) {
                            length++;
                        }else{
                            length = 1;
                        }
                    }
                    clerk = itemQueue.clerkKey;
                }
                if (onList) {
                    handle.setValue(new Pair<>(length, isAttendance));
                } else {
                    handle.setValue(new Pair<>(-1, isAttendance));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
