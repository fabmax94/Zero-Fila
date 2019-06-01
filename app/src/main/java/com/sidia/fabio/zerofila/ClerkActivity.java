package com.sidia.fabio.zerofila;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sidia.fabio.zerofila.model.Clerk;
import com.sidia.fabio.zerofila.model.Client;
import com.sidia.fabio.zerofila.model.RequestQueue;
import com.sidia.fabio.zerofila.viewModel.ClientViewModel;
import com.sidia.fabio.zerofila.viewModel.QueueViewModel;

public class ClerkActivity extends AppCompatActivity {
    public static String CLERK_KEY = "CLERK_KEY";
    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    private String key;
    private QueueViewModel queueViewModel;
    private ClientViewModel clientViewModel;
    private TextView tvName;
    private TextView tvType;
    private TextView tvDescription;
    private TextView tvAverage;
    private TextView tvAttendance;
    private Clerk clerk;
    private Client client;
    private double latitude;
    private double longitude;
    private long average;

    private double originLatitude;
    private double originLongitude;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        key = getIntent().getExtras().getString(CLERK_KEY);
        latitude = getIntent().getExtras().getDouble(LATITUDE);
        longitude = getIntent().getExtras().getDouble(LONGITUDE);
        bindViews();
        initViewModels();
        observers();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        getDeviceLocation();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    void observers() {
        clientViewModel.getClient().observe(this, new Observer<Client>() {
            @Override
            public void onChanged(@Nullable Client c) {
                client = c;
                checkQueue(client);
            }
        });
    }

    void initViewModels() {
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        clientViewModel = ViewModelProviders.of(this).get(ClientViewModel.class);
    }

    void bindViews() {
        tvName = findViewById(R.id.tv_name);
        tvAverage = findViewById(R.id.tv_average);
        tvAttendance = findViewById(R.id.tv_attendance);
        tvType = findViewById(R.id.tv_type);
        tvDescription = findViewById(R.id.tv_description);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void checkQueue(final Client client) {
        queueViewModel.getClerk(key).observe(this, new Observer<Clerk>() {
            @Override
            public void onChanged(@Nullable Clerk result) {
                tvName.setText(result.name);
                tvType.setText(result.type);
                tvDescription.setText(result.description);
                clerk = result;
                average = result.average;
                queueViewModel.getItemQueue(clerk.key, client.cpf).observe(ClerkActivity.this, new Observer<Pair<Integer, Boolean>>() {
                    @Override
                    public void onChanged(@Nullable Pair<Integer, Boolean> result) {
                        setTextViews(result.first, result.second);
                        findViewById(R.id.load).setVisibility(View.GONE);
                    }
                });
                queueViewModel.getClerk(key).removeObserver(this);
            }
        });
    }

    public void onRequest(View view) {
        queueViewModel.addRequest(new RequestQueue(client.cpf, client.name, clerk.establishmentKey, clerk.name, clerk.key));
        Toast.makeText(getApplicationContext(), getString(R.string.checkin_success), Toast.LENGTH_SHORT).show();
    }

    private void setTextViews(int length, boolean isAttendance) {
        tvAverage.setText(String.valueOf(average / (1000 * 60)) + getString(R.string.minutes));
        tvAttendance.setText(String.valueOf(average * length / (1000 * 60)) + getString(R.string.minutes));
        TextView queueuLength = findViewById(R.id.queue_lenght);
        TextView line = findViewById(R.id.people_in_line);
        if (length == -1) {
            tvAttendance.setText(getString(R.string.isnt_queue));
            line.setVisibility(View.GONE);
            queueuLength.setText(getString(R.string.isnt_queue));
            queueuLength.setBackgroundResource(android.R.color.transparent);
        } else if (length == 0) {
            if (isAttendance) {
                queueuLength.setText(getString(R.string.is_attendance));
            } else {
                queueuLength.setText(getString(R.string.is_next));
            }
            line.setVisibility(View.INVISIBLE);
            queueuLength.setBackgroundResource(android.R.color.transparent);
            findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
        } else {
            if (length == 1) {
                queueuLength.setText(getString(R.string.a_person));
                line.setText(getString(R.string.miss));
            } else {
                queueuLength.setText(length + getString(R.string.people));
                line.setText(getString(R.string.misss));
            }
            queueuLength.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                queueuLength.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            }
            findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            originLatitude = mLastKnownLocation.getLatitude();
                            originLongitude = mLastKnownLocation.getLongitude();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    void onRoute(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + originLatitude + "," + originLongitude + "&daddr=" + latitude + "," + longitude));
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


