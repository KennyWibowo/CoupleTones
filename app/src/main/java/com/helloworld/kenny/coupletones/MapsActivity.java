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
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.LayoutInflater;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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
import com.helloworld.kenny.coupletones.partner.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.partner.PartnerInformation;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Context context = this;
    private MapsActivity me = this;

    private GoogleApiClient client;
    private GoogleMap mMap;
    private GoogleCloudMessaging gcm;

    private LinearLayout searchLayout;
    private EditText searchBar;
    private ListView rightDrawer;
    private UiSettings myUiSetting;
    private DrawerLayout drawer;
    private final PartnerInformation partnerInformation = new PartnerInformation();

    private Button buttonRemovePartner;
    private Button buttonAddPartner;

    private Favorites favorites;
    private FavoriteSwipeAdapter<FavoriteEntry> favoriteSwipeAdapter;

    private String PROJECT_NUMBER = "366742322722";
    private boolean autoRegistered = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Reference setup
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        searchBar = (EditText) findViewById(R.id.search_bar);
        rightDrawer = (ListView) findViewById(R.id.right_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        buttonAddPartner = (Button) findViewById(R.id.add_partner);
        buttonRemovePartner = (Button) findViewById(R.id.remove_partner);

        // setup data structures and variables
        favorites = new Favorites();
        favoriteSwipeAdapter = new FavoriteSwipeAdapter<FavoriteEntry>(me, R.layout.listview_item, R.id.listview_item_text, favorites.getAllEntries());
        rightDrawer.setAdapter(favoriteSwipeAdapter);

        //SETUPS
        setupDeviceId();
        setupRightDrawer();
        setupLocationListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


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
                double lat =  location.getLatitude();
                double lng = location.getLongitude();
                double alt = location.getAltitude();
                boolean inRange = false;

                ArrayList<FavoriteEntry> favs = favorites.getAllEntries();
                for (int i = 0; i < favorites.size(); i++){
                    LatLng ref = favs.get(i).getLocation();
                    double refLat = ref.latitude;
                    double refLng = ref.longitude;
                    double refAlt = alt;
                    inRange = compDistance(lat, refLat, lng, refLng, alt, refAlt);
                    if(inRange) break;
                }

                if(inRange){

                }






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


    }

    public void setupDeviceId() {
        synchronized (partnerInformation) {

            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {
                    String msg = "";
                    String regid = "";
                    boolean autoRegistered = false;

                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        }

                        regid = gcm.register(PROJECT_NUMBER);
                        msg = "Device registered, registration ID=" + regid;
                        Log.i("GCM", "!!!!! " + regid);

                        autoRegistered = partnerInformation.registerOwnRegId(regid);

                    } catch (IOException ex) {
                        msg = "Error: " + ex.getMessage();
                    }

                    return autoRegistered;
                }

                @Override
                protected void onPostExecute(Boolean autoRegistered) {
                    me.autoRegistered = autoRegistered;

                    if(autoRegistered) {
                        buttonAddPartner.setVisibility(View.GONE);
                        buttonRemovePartner.setVisibility(View.VISIBLE);
                    }
                }
            }.execute(null, null, null);
        }
    }

    public void buttonGetDeviceId(View view) {
        String regId = partnerInformation.getOwnRegId();

        if(regId == null || regId.equals(""))
            setupDeviceId();

        AlertDialog.Builder show = new AlertDialog.Builder(me);

        synchronized (partnerInformation) {
            // Dialog settings
            show.setCancelable(true)
                    .setMessage("Reg Id: " + partnerInformation.getOwnRegId())
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog display = show.create();
            display.show();
        }
    }

    public void buttonAddPartner(View view) {
        final EditText username = new EditText(this);

        AlertDialog.Builder register = new AlertDialog.Builder(me);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View store = inflater.inflate(R.layout.alert_text_partner, null);

        register.setView(store);
        register.setCancelable(true);
        register.setMessage("Please input partner information");
        register.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText email = (EditText) store.findViewById(R.id.partner_email);
                EditText partner_id = (EditText) store.findViewById(R.id.partner_id);

                try {
                    partnerInformation.registerPartner(partner_id.getText().toString(), email.getText().toString());
                    buttonAddPartner.setVisibility(View.GONE);
                    buttonRemovePartner.setVisibility(View.VISIBLE);
                    Toast.makeText(me, "Partner successfully registered", Toast.LENGTH_SHORT).show();
                } catch (PartnerAlreadyRegisteredException e) {
                    Toast.makeText(me, "Oops! Partner already registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog displayReg = register.create();
        displayReg.show();

    }

    public void buttonRemovePartner(View view) {

        AlertDialog.Builder remove = new AlertDialog.Builder(me);
        remove.setMessage("Are you sure you want to remove your partner?");
        remove.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                partnerInformation.clear();
                buttonAddPartner.setVisibility(View.VISIBLE);
                buttonRemovePartner.setVisibility(View.GONE);
                Toast.makeText(me, "Partner successfully removed", Toast.LENGTH_SHORT).show();
            }
        });
        remove.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog displayWarn = remove.create();
        displayWarn.show();
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

        System.out.println(favorites.toString());
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
                Toast.makeText(me, "No results found by search", Toast.LENGTH_SHORT).show();
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

                            System.out.println(favorites.toString());
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

    public void onReachedFavoriteLocation() {
        //TODO: copy GcmDemoFragment.sendMessage
        // Message: Partner has reached location "(blah blah)"
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

    public boolean compDistance(double lat1, double lat2, double lon1, double lon2, double el1,
                            double el2){
        final int R = 6371;

        double latDistance = Math.toRadians(lat1 - lat2);
        double longDistance = Math.toRadians(lon1-lon2);
        double a = Math.sin(latDistance/2)*Math.sin(latDistance/2)
                   + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                   * Math.sin(longDistance/2) * Math.sin(longDistance/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R*c*1000;
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        distance = Math.sqrt(distance);

        return (distance/1609.344 < 0.1);
    }
}
