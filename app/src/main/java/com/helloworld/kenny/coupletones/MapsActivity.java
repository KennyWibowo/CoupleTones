package com.helloworld.kenny.coupletones;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helloworld.kenny.coupletones.Favorites.Exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.Favorites.Exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.Favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.Favorites.Favorites;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Context context = this;
    private MapsActivity me = this;
    private GoogleMap mMap;
    private LinearLayout searchLayout;
    private EditText searchBar;
    private ListView rightDrawer;
    private UiSettings myUiSetting;
    private DrawerLayout drawer;

    private Favorites favorites;
    private FavoriteSwipeAdapter<FavoriteEntry> favoriteSwipeAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Reference setup
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        searchBar = (EditText) findViewById(R.id.search_bar);
        rightDrawer = (ListView) findViewById(R.id.right_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        searchLayout.bringToFront();

        favorites = new Favorites();
        favoriteSwipeAdapter = new FavoriteSwipeAdapter<FavoriteEntry>(me, R.layout.listview_item, R.id.listview_item_text, favorites.getAllEntries());
        rightDrawer.setAdapter(favoriteSwipeAdapter);

        //SETUPS
        setupRightDrawer();
        setupLocationListener();


    }

    public void setupRightDrawer() {

        TextView title = new TextView(me);
        title.setText("Favorites");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        title.setTextColor(getResources().getColor(R.color.colorBlack));
        title.setHeight(120);
        title.setGravity(Gravity.CENTER);

        rightDrawer.addHeaderView(title);

        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                favoriteSwipeAdapter.closeAllItems();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };

        drawer.addDrawerListener(drawerListener);

    }

    public void setupLocationListener() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("update location"));

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void buttonDeleteFavorite(View view) {
        SwipeLayout swipeLayout = (SwipeLayout) view.getParent().getParent();
        TextView nameView = (TextView) swipeLayout.findViewById(R.id.listview_item_text);
        String name = nameView.getText().toString();
        int pos = favorites.lookupPosition(name);

        // Delete the marker from the map, then the actual entry
        favorites.getEntry(pos).getMarker().remove();
        favorites.deleteEntry(pos);

        // Notify the swipeAdapter and close everything
        favoriteSwipeAdapter.notifyDataSetChanged();
        favoriteSwipeAdapter.closeAllItems();
        Toast.makeText(me, "Favorite deleted successfully", Toast.LENGTH_SHORT).show();
    }

    public void buttonSearch(View view) {
        String location = searchBar.getText().toString();
        List<Address> addressList = null;

        if (!location.equals("")) {
            Geocoder geocoder = new Geocoder(me);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 14.7));
            } else {
            }
        }
    }

    @Override
    public void onMapClick(final LatLng point) {

        //Source: http://developer.android.com/guide/topics/ui/dialogs.html

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final LatLng pointFinal = point;
        final EditText et = new EditText(this);

        et.setGravity(Gravity.CENTER);
        builder.setView(et);

        builder.setCancelable(true)
                .setMessage("Input name for new favorite location")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            favorites.addEntry(et.getText().toString(), pointFinal);
                            favoriteSwipeAdapter.notifyDataSetChanged();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(point).title(et.getText().toString()));
                            favorites.getEntry(favorites.size() - 1).setMarker(marker);
                            Toast.makeText(me, "Favorite location added successfully", Toast.LENGTH_SHORT).show();
                        } catch (NameInUseException e) {
                            Toast.makeText(me, "Name already in use", Toast.LENGTH_SHORT).show();
                        } catch (InvalidNameException e) {
                            Toast.makeText(me, "Invalid name for favorite", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog namePrompt = builder.create();
        namePrompt.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);

        myUiSetting = mMap.getUiSettings();
        myUiSetting.setZoomControlsEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.helloworld.kenny.coupletones/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.helloworld.kenny.coupletones/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
