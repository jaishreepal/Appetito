package com.foodapp.appetito.appetito;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.InputStream;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Register extends AppCompatActivity {
    private FusedLocationProviderClient client;

    EditText firstName;
    EditText lastName;
    EditText email, latitude, longitude, birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthday = (EditText) findViewById(R.id.dob);
        email = (EditText) findViewById(R.id.email);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);

        if (Build.VERSION.SDK_INT > 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        firstName.setText(getIntent().getStringExtra("first_name"));
        lastName.setText(getIntent().getStringExtra("last_name"));
        email.setText(getIntent().getStringExtra("email"));
//        birthday.setText(getIntent().getStringExtra("user_birthday"));

        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(Register.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null)
                        {
                            longitude.setText(location.toString());
                            latitude.setText(location.toString());
                        }
                    }
                });
            }
        });

    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(Register.this,new String[]{ACCESS_FINE_LOCATION},1);
    }

}

