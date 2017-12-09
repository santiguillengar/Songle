package s1546270.songle.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.location.Location;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import s1546270.songle.Activities.DrawerActivity;
import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Song;
import s1546270.songle.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = MapFragment.class.getSimpleName();

    GoogleMap map;


    SupportMapFragment mapFrag;
    private Location mLastLocation;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // Store placemarks parsed
    List<Placemark> placemarks = null;

    private Map<Marker,String> markersMap = new HashMap<>();

    String mapDifficulty = null;

    private Song song = null;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        try {
            SharedPreferences pref = this.getActivity().getSharedPreferences("SonglePref", 0);
            SharedPreferences.Editor editor = pref.edit();
            mapDifficulty = pref.getString("mapDifficulty", null);
            Log.d(TAG, "     |SANTI|     Shared Pref Retrieved");

            song  = (Song) getActivity().getIntent().getSerializableExtra("song");
            Log.d(TAG, "SONG passed through to map fragment: "+song.getNumber()+" "+song.getTitle());

        } catch (Exception e) {
            Log.e(TAG, "Exception raised retriving map difficulty");
        }
        Log.d(TAG, "Map created. Map Difficulty: "+mapDifficulty);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    WordCollectedListener mCallBack;

    public interface WordCollectedListener {
         public void newWordFound(String wordIndex);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (WordCollectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement WordCollected");
        }
    }

    @Override
    public void onDetach() {
        mCallBack = null;
        super.onDetach();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng pp = new LatLng(11.5448729, 194.8921668);

        MarkerOptions option = new MarkerOptions();
        option.position(pp).title("Tutorial");

        map.addMarker(option);
        map.moveCamera(CameraUpdateFactory.newLatLng(pp));


        try {

            // Visualise current position with a small blue circle
            map.setMyLocationEnabled(true);
            map.setMinZoomPreference(16.0f);
            map.setMaxZoomPreference(22.0f);

            LatLng mapMidpoint = new LatLng(55.944425, -3.188396);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapMidpoint, 0));

        }
        catch (SecurityException se){
            System.out.println("Security exception thrown [onMapReady]");
        }

        //Add "My location" button to the UI
        map.getUiSettings().setMyLocationButtonEnabled(true);
        //Hide map buttons that were previously appearing under FAB.
        map.getUiSettings().setMapToolbarEnabled(false);


        // make sure placemarks are only downloaded and parsed once
        if (placemarks == null) {
            placemarksOnMap();
        }

    }



    private GoogleApiClient mGoogleApiClient;

    // Should be protected not public
    @Override
    public void onStart() {

        try {
            super.onStart();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            Log.d(TAG, "ERROR: GPSU: "+GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext()));
        }
    }

    // Should be protected not public
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void createLocationRequest() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // preferably every 5 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Can we access the user's current location?
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        try { createLocationRequest(); }
        catch (java.lang.IllegalStateException ise) {
            Log.e(TAG, "IllegalStateException thrown [onConnected]");
        }

        // Can we access the user's current location?
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onLocationChanged(Location current) {
        Log.d(TAG,"     |SANTI|     Location Changed");

        for (Marker marker: markersMap.keySet()) {
            if (marker.isVisible()) {


                LatLng markerLatLng = marker.getPosition();
                double distance = getDistance(markerLatLng.latitude, markerLatLng.longitude,current.getLatitude(),current.getLongitude());

                if (distance <= getMinimiumDistance()) {
                    marker.setVisible(false);
                    mCallBack.newWordFound(markersMap.get(marker));

                }
            }
        }
    }

    public double getMinimiumDistance() {
        double distance = 20.0;
        switch(mapDifficulty) {
            case "1":
                distance = 25.0;
                break;
            case "2":
                distance = 25.0;
                break;
            case "3":
                distance = 20.0;
                break;
            case "4":
                distance = 20.0;
                break;
            case "5":
                distance = 15.0;
                break;
        }
        return distance;
    }

    // Return distance between two points in meters
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float distance = (float) (earthRadius * c);

        return distance;
    }



    @Override
    public void onConnectionSuspended(int flag) {
        Log.e(TAG, "onConnectionSuspended");
        System.out.println("");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public void placemarksOnMap() {
        Log.d(TAG, "     |SANTI|     Map - Placemarks being placed on map. ");

        placemarks = ((DrawerActivity) getActivity()).getPlacemarks();

        String[] strCoords;

        for (Placemark p : placemarks) {

            // Add a marker for placemark
            strCoords = p.getPoint().split(",");

            LatLng latLng = new LatLng(Double.parseDouble(strCoords[1]), Double.parseDouble(strCoords[0]));

            Marker marker = map.addMarker(new MarkerOptions().position(latLng).icon(getPlacemarkIcon(p.getStyleUrl())).title(p.getStyleUrl().substring(1)));
            markersMap.put(marker, p.getName());

        }
    }


    //method moved
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


}
