package com.amirmaaref313.speedy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ir.adad.client.Adad;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_GPS = 0;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean receiving;
    Button button;
    TextView speedView;
    boolean per;
    ImageView subPanel, mainPanel;
    boolean nighMode;
    View view;
    View someView;
    boolean moreDetails;
    TextView latolong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Adad.initialize(getApplicationContext());
//        Adad.showInterstitialAd(this);

        latolong = (TextView) findViewById(R.id.textView3);
        button = (Button) findViewById(R.id.button);
        speedView = (TextView) findViewById(R.id.textView2);
        per = false;
        checkPermission();
        receiving = false;
        subPanel = (ImageView) findViewById(R.id.subPanel);
        mainPanel = (ImageView) findViewById(R.id.mainPanel);
        view = findViewById(R.id.button);
        someView = view.getRootView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        speedView.setTextColor(Color.BLUE);
        speedView.setText("");
        nighMode = false;
        someView.setBackgroundColor(Color.WHITE);
        moreDetails = false;
        latolong.setTextColor(Color.BLUE);
        latolong.setText("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_show_more) {
            if (moreDetails) {
                latolong.setText("");
                moreDetails = false;
            } else {
                moreDetails = true;
                latolong.setText("Waiting...");
            }

        } else if (id == R.id.nav_night_mode) {
            if (nighMode) {
                speedView.setTextColor(Color.BLUE);
                nighMode = false;
                someView.setBackgroundColor(Color.WHITE);
                latolong.setTextColor(Color.BLUE);

            } else {
                nighMode = true;
                speedView.setTextColor(Color.WHITE);
                someView.setBackgroundColor(Color.BLACK);
                latolong.setTextColor(Color.WHITE);

            }

        } else if (id == R.id.nav_rate) {
            String download_link = "https://cafebazaar.ir/app/com.amirmaaref313.speedy/?l=fa";
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_link));
            startActivity(myIntent);
        } else if (id == R.id.nav_contact) {
//            shareApplication();
//            sendAppItself();
            sentEmailBy("amirtt.gh@icloud.com", "Speedy feedback");
        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Speedy");
                String sAux = "\nLet me recommend you Speedy\n\n";
                sAux = sAux + "https://cafebazaar.ir/app/com.amirmaaref313.speedy/?l=fa \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void click(View view) {
        if (receiving) {
            receiving = false;
            locationManager.removeUpdates(locationListener);
            button.setText("CLICK TO CONNECT");
            speedView.setText("Welcome To Speedy!");
            mainPanel.setImageResource(R.drawable.panel1);
            subPanel.setRotation(0);
            latolong.setText("");
            Adad.showInterstitialAd(this);

        } else {
            startUpdate();
            Adad.showInterstitialAd(this);

        }
    }

    public void checkPermission() {
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , MY_PERMISSIONS_REQUEST_READ_GPS);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_GPS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else
            per = true;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();
                    per = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Permission denied...Enable it In Setting", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void startUpdate() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateViews(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
            receiving = true;
            speedView.setText("Waiting for Signal...");
            button.setText("CLICK TO DISCONNECT");
        } else
            Toast.makeText(this, "Please Enable Location Service", Toast.LENGTH_LONG).show();

    }

    public void updateViews(Location loc) {
        double speed = (loc.getSpeed() * 3.6);

        float f = (float) speed;
        int N = 2;
        double d = f * Math.pow(10, N);
        int i = (int) d;
        double f2 = i / Math.pow(10, N);

        speedView.setText("Speed: " + String.valueOf(f2) + " Km/h");

        if (speed <= 20) {
            mainPanel.setImageResource(R.drawable.panel1);

            subPanel.setRotation(((float) (speed * 9)));
        } else if (speed <= 150 && speed > 20) {
            mainPanel.setImageResource(R.drawable.panel2);

            subPanel.setRotation(((float) (speed * 1.125)));
        }
        if (moreDetails) {

            latolong.setText("Latitude: " + String.valueOf(loc.getLatitude()) + "\n" +
                    "Longitude: " + String.valueOf(loc.getLongitude()) + "\n" +
                    "Altitude: " + String.valueOf(loc.getAltitude()));
        } else
            latolong.setText("");
    }

    public void sentEmailBy(String mailAdress, String subject) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, mailAdress);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
