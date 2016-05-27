package com.helloworld.kenny.coupletones;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.registration.RegistrationInformation;
import com.helloworld.kenny.coupletones.registration.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.registration.exceptions.SelfAlreadyRegisteredException;

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
    private final RegistrationInformation registrationInformation = new RegistrationInformation();

    private Button buttonRemovePartner;
    private Button buttonAddPartner;
    private Button buttonGetFavorites;
    private Button buttonGetHistory;
    private ListView listFavorite;
    private ListView listHistory;

    private Favorites favorites;
    private FavoriteSwipeAdapter<FavoriteEntry> favoriteSwipeAdapter;

    private String PROJECT_NUMBER = "366742322722";
    private boolean autoRegistered = false;
    private boolean reached = false;
    //private TextView mTxtMsg;

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
        buttonGetFavorites = (Button)findViewById(R.id.get_favorites);
        buttonGetHistory = (Button)findViewById(R.id.get_history);
        listFavorite = (ListView)findViewById(R.id.left_listFavorite);
        listHistory = (ListView)findViewById(R.id.left_listHistory);
        listHistory.setVisibility(View.GONE);
        buttonGetFavorites.setVisibility(View.GONE);


        // setup data structures and variables
        favorites = new Favorites();
        favoriteSwipeAdapter = new FavoriteSwipeAdapter<FavoriteEntry>(me, R.layout.listview_item, R.id.listview_item_text, favorites.getAllEntries());
        rightDrawer.setAdapter(favoriteSwipeAdapter);

        //SETUPS
        //setupDeviceId();
        setupRightDrawer();
        setupLeftDrawer();
        setupLocationListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if(registrationInformation.isSelfRegistered() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.email_registration, null);
            builder.setView(dialogView);

            final EditText et = (EditText) dialogView.findViewById(R.id.email);

            builder.setTitle("Email Registration");
            builder.setMessage("Please enter a valid email address for registration: ");
            builder.setNeutralButton("Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int something) {
                            String emailAddress = et.getText().toString();

                            try {
                                registrationInformation.registerSelf(emailAddress);
                            } catch (SelfAlreadyRegisteredException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                        }
                    });

            AlertDialog emailRegistration = builder.create();
            emailRegistration.show();
            
        }
    }

    public void setupLeftDrawer()
    {
        TextView title = new TextView(me);
        title.setText("Partner Favorites");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        title.setTextColor(getResources().getColor(R.color.colorBlack));
        title.setHeight(120);
        title.setGravity(Gravity.CENTER);
        listFavorite.addHeaderView(title);
        TextView otherTitle = new TextView(me);
        otherTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        otherTitle.setTextColor(getResources().getColor(R.color.colorBlack));
        otherTitle.setHeight(120);
        otherTitle.setGravity(Gravity.CENTER);
        otherTitle.setText("History");
        listHistory.addHeaderView(otherTitle);
    }



    /**
     * Sets up the right drawer for the list of favorite locations
     */
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

    /**
     * Creates the Location Listener for acquiring current locations
     */
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
                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("update location"));
                double lat =  location.getLatitude();
                double lng = location.getLongitude();
                double alt = location.getAltitude();
                boolean inRange = false;
                FavoriteEntry favEntry = null;
                ArrayList<FavoriteEntry> favs = favorites.getAllEntries();
                for (int i = 0; i < favorites.size(); i++){
                    LatLng ref = favs.get(i).getLocation();
                    double refLat = ref.latitude;
                    double refLng = ref.longitude;
                    double refAlt = alt;
                    inRange = compDistance(lat, refLat, lng, refLng, alt, refAlt);
                    if(inRange){
                        favEntry = favs.get(i);
                        break;
                    }
                }

                if((reached==false)&&inRange && favEntry != null){
                    //System.out.print("In Range!!!\n");
                    onReachedFavoriteLocation(favEntry);
                    //inRange = false;
                    reached = true;
                }
                if(inRange == false)
                {
                    //System.out.print("Out of Range!!!\n");
                    reached  = false;
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

    /**
     * Sets up api services for retrieving device i.d.
     * DEPRECATED
     */
    /*public void setupDeviceId() {
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
    }*/

    /**
     * Button Method for retrieving i.d.
     * @param view
     */
    /*public void buttonGetDeviceId(View view) {
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

        //onReachedFavoriteLocation(new FavoriteEntry("Sixth College", new LatLng(0,0)));
    }*/

    /**
     * Button for adding partner
     * @param view
     */
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
                try {
                    registrationInformation.registerPartner(email.getText().toString());
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

    /**
     * Button for removing partner
     * @param view
     */
    public void buttonRemovePartner(View view) {

        AlertDialog.Builder remove = new AlertDialog.Builder(me);
        remove.setMessage("Are you sure you want to remove your partner?");
        remove.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                registrationInformation.clearPartner();
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

    /**
     * Button for deleting favorites
     * @param view
     */
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

    public void buttonHistory(View view)
    {
        listHistory.setVisibility(View.VISIBLE);
        listFavorite.setVisibility(View.GONE);
        buttonGetFavorites.setVisibility(View.VISIBLE);
        buttonGetHistory.setVisibility(View.GONE);
    }

    public void buttonFavorites(View view)
    {
        listHistory.setVisibility(View.GONE);
        listFavorite.setVisibility(View.VISIBLE);
        buttonGetFavorites.setVisibility(View.GONE);
        buttonGetHistory.setVisibility(View.VISIBLE);
    }

    /**
     * Button for searching locations
     * @param view
     */
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

    /**
     * Handling event-driven case where user clicks a location on the Map
     * @param point
     */
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

    /**
     * Handling event where user reaches a favorite location
     * @param entry
     */
    public void onReachedFavoriteLocation(FavoriteEntry entry) {
        //TODO: move this to the intent?
        // Message: Partner has reached location "(blah blah)"

        entry.visit();

        /*System.out.println("Sending a message to: " + partnerInformation.getRegId());

        try {
            String partnerId = partnerInformation.getRegId();
            Bundle data = new Bundle();
            data.putString("action", "com.helloworld.kenny.coupletones.MESSAGE");
            data.putString("toRegId", partnerInformation.getRegId());
            data.putString("title", "Partner reached favorite location!");
            data.putString("content", "Your partner reached: " + entry.getName());
            //String id = Integer.toString(getNextMsgId());
            gcm.send(PROJECT_NUMBER + "@gcm.googleapis.com","" + getNextMessageId(), data);
            //msgId++;
            //Log.v("grokkingandroid", "sent message: " + msg);
        } catch (IOException e) {
            System.out.println("uh oh");
        }*/
    }

    /*private int getNextMessageId() {
        SharedPreferences prefs = getPrefs();
        int id = prefs.getInt("keyMsgId", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("keyMsgId", ++id);
        editor.commit();
        return id;
    }*/

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
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

    /**
     * Application-api instantiation
     */
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

    /**
     * When API services stop
     */
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

    /**
     * Calculates the distance between two points via the distance formula
     */
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
