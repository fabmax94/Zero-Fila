package com.sidia.fabio.zerofila;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sidia.fabio.zerofila.adapter.QueueArrayAdapter;
import com.sidia.fabio.zerofila.model.Clerk;
import com.sidia.fabio.zerofila.model.Establishment;
import com.sidia.fabio.zerofila.viewModel.QueueViewModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements QueueArrayAdapter.ListItemClickListener {

    public static String DETAIL_KEY = "DETAIL_KEY";
    private QueueViewModel queueViewModel;
    private RecyclerView recyclerView;
    private String key;
    private TextView tvName;
    private TextView tvType;
    private TextView tvLocation;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViewModels();
        key = getIntent().getExtras().getString(DETAIL_KEY);
        bindViews();

        if (key == null) {
            finish();
        }

        observers();
    }

    void observers() {
        queueViewModel.getEstablishment(key).observe(this, new Observer<Establishment>() {
            @Override
            public void onChanged(@Nullable Establishment result) {
                tvName.setText(result.name);
                tvType.setText(result.type);
                tvLocation.setText(result.local);
                latitude = result.latitude;
                longitude = result.longitude;
            }
        });
        queueViewModel.getQueue(key).observe(this, new Observer<List<Clerk>>() {
            @Override
            public void onChanged(@Nullable final List<Clerk> result) {
                QueueArrayAdapter adapter = new QueueArrayAdapter(result, DetailActivity.this);
                recyclerView.setAdapter(adapter);
                findViewById(R.id.load).setVisibility(View.GONE);
            }
        });
    }

    void initViewModels() {
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
    }

    void bindViews() {
        tvName = findViewById(R.id.tv_name);
        tvType = findViewById(R.id.tv_type);
        tvLocation = findViewById(R.id.tv_location);
        recyclerView = findViewById(R.id.queue);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onListItemClick(Clerk clerk) {
        Intent intent = new Intent(DetailActivity.this, ClerkActivity.class);
        intent.putExtra(ClerkActivity.CLERK_KEY, clerk.key);
        intent.putExtra(ClerkActivity.LATITUDE, latitude);
        intent.putExtra(ClerkActivity.LONGITUDE, longitude);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
