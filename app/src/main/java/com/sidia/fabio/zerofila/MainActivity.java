package com.sidia.fabio.zerofila;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sidia.fabio.zerofila.adapter.EstablishmentArrayAdapter;
import com.sidia.fabio.zerofila.model.Client;
import com.sidia.fabio.zerofila.model.Establishment;
import com.sidia.fabio.zerofila.viewModel.ClientViewModel;
import com.sidia.fabio.zerofila.viewModel.QueueViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EstablishmentArrayAdapter.ListItemClickListener {
    private QueueViewModel queueViewModel;
    private ClientViewModel clientViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModels();
        initViews();
        observers();
    }

    void observers() {
        clientViewModel.getClient().observe(this, new Observer<Client>() {
            @Override
            public void onChanged(@Nullable Client client) {
                if (client == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        queueViewModel.getEstablishment().observe(this, new Observer<List<Establishment>>() {
            @Override
            public void onChanged(@Nullable List<Establishment> establishments) {
                EstablishmentArrayAdapter mAdapter = new EstablishmentArrayAdapter(establishments, MainActivity.this);
                recyclerView.setAdapter(mAdapter);
                findViewById(R.id.load).setVisibility(View.GONE);
            }
        });
    }

    void initViewModels() {
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        clientViewModel = ViewModelProviders.of(this).get(ClientViewModel.class);
    }

    void initViews() {
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
    }

    @Override
    public void onListItemClick(Establishment establishment) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.DETAIL_KEY, establishment.key);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new LogoutTask().execute();
                break;
        }

        return true;
    }

    class LogoutTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                clientViewModel.deleteUser();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

}
