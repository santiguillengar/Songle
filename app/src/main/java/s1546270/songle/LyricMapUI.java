package s1546270.songle;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import java.util.List;

import s1546270.songle.Objects.Placemark;

public class LyricMapUI
        extends FragmentActivity
        implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private static final String TAG = "MapsActivity";

    // The broadcast receiver that tracks network connectivity changes.
    private NetworkReceiver receiver = new NetworkReceiver();

    // (!) Mine
    private Marker mCurrLocationMarker;

    // Store placemarks parsed
    List<Placemark> placemarks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_map_ui);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);

    }

    public void placemarksOnMap() {

        // HANDLE PLACEMARKS FOR MAP
        String placemarksUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map5.kml";
        //DownloadXmlTask downloadXmlTask(String;Void;String) = new DownloadXmlTask();
        DownloadXmlTask dt = new DownloadXmlTask();

        String[] strCoords = new String[3];

        try {
            dt.execute(placemarksUrl);
            placemarks = dt.get();
        } catch (Exception e) {

        }

        Placemark p1 = placemarks.get(0);
        strCoords = p1.getPoint().split(",");


        Log.d(TAG,"     |MAPMAPMAP| YOUR COORDS ARE THIS DUMB SHIT: "+strCoords[0]+" -- "+strCoords[1]);
        LatLng latLng2 = new LatLng(Double.parseDouble(strCoords[1]), Double.parseDouble(strCoords[0]));
        mMap.addMarker(new MarkerOptions().position(latLng2)
                .title("test"));
        Log.d(TAG, "--------------------------------------------------------------------");

        /*LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));*/


        try {
            dt.execute(placemarksUrl);
            placemarks = dt.get();

        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download placemarks for map");
        }


        for (Placemark p : placemarks) {
            // Add a marker for placemark

            Log.d(TAG, "     |MAP|     MAP: COORDINATES: "+p.getPoint());
            strCoords = p.getPoint().split(",");

            LatLng latLng = new LatLng(Double.parseDouble(strCoords[1]), Double.parseDouble(strCoords[0]));
            mMap.addMarker(new MarkerOptions().position(latLng).title(p.getName()));
        }
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

        try {

            // Visualise current position with a small blue circle
            mMap.setMyLocationEnabled(true);
            mMap.setMinZoomPreference(16.0f);
            mMap.setMaxZoomPreference(22.0f);
            LatLng at = new LatLng(-3.1899748612437024,55.94544964050534);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(at, 0));

        }
        catch (SecurityException se){
            System.out.println("Security exception thrown [onMapReady]");
        }

        //Add "My location" button to the UI
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        placemarksOnMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void createLocationRequest() {
        // Set the parameters for the location request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // preferably every 5 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Can we access the user's current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try { createLocationRequest(); }
        catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }

        // Can we access the user's current location?
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(Location current) {
        System.out.println(" [onLocationChanged] Lat/long now (" +
        String.valueOf(current.getLatitude()) + "," +
        String.valueOf(current.getLongitude()) +")");
    }

    @Override
    public void onConnectionSuspended(int flag) {
        System.out.println(" >>>> onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle the
        // failure silently
        System.out.println(" >>>> onConnectionFailed");
    }
}
