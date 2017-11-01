package s1546270.songle;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Style;

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
    List<Style> styles = null;

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
        Log.d(TAG, "     |SANTI|     LyricMapUI - Placemarks being placed on map. ");

        // HANDLE PLACEMARKS FOR MAP
        String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map1.kml";
        DownloadXmlTask dtPlacemarks = new DownloadXmlTask();
        DownloadStylesTask dtStyles = new DownloadStylesTask();

        String[] strCoords;

        try {
            dtPlacemarks.execute(url);
            placemarks = dtPlacemarks.get();
            dtStyles.execute(url);
            styles = dtStyles.get();


        } catch (Exception e) {
            Log.e(TAG, "ERROR: Couldn't download placemarks for map");
        }

        for (Style s : styles) {
            Log.d(TAG, "STYLE: ID: "+s.getId()+" ICONSTYLE: "+s.getIconStyle());
        }


        for (Placemark p : placemarks) {
            // Add a marker for placemark

            strCoords = p.getPoint().split(",");

            LatLng latLng = new LatLng(Double.parseDouble(strCoords[1]), Double.parseDouble(strCoords[0]));

            MarkerOptions marker = new MarkerOptions().position(latLng).title(p.getName());
            BitmapDescriptor icon = getPlacemarkIcon(p.getStyleUrl());
            marker.icon(icon);
            mMap.addMarker(marker);



        }
    }

    private BitmapDescriptor getPlacemarkIcon(String styleUrl) {
        BitmapDescriptor icon = null;

        switch (styleUrl) {
            case "#boring":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ylw_blank);
                break;
            case "#notboring":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ylw_circle);
                break;
            case "#interesting":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.orange_diamond);
                break;
            case "#veryinteresting":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_stars);
                break;
            case "#unclassified":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.wht_blank);
                break;
            default:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.error_marker);
                break;
        }


        return icon;
    }

    /*public Bitmap getIconFromURL(String imageUrl) {
        Log.d(TAG, "Accessed getIconFromURL");
        InputStream input = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            Bitmap icon = BitmapFactory.decodeStream(input);
            return icon;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            input.close();
        }
    }*/

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
            LatLng at = new LatLng(-3.18701,55.9444);
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
