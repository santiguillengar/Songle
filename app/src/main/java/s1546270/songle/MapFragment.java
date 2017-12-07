package s1546270.songle;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.location.Location;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;

import android.icu.lang.UScript;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import s1546270.songle.Objects.Placemark;
import s1546270.songle.Objects.Song;
import s1546270.songle.Objects.Style;


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


    // LYRICMAPUI IMPORTS
    SupportMapFragment mapFrag;
    private Location mLastLocation;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // END OF LYRICMAPUI IMPORTS

    // Store placemarks parsed
    List<Placemark> placemarks = null;
    List<Style> styles = null;

    String mapDifficulty = null;

    private Song song = null;

    private String songNumber = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Start of LYRICMAPUI

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        // END OF LYRICMAPUI

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

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    // START OF LYRICMAPUI SECTION -----------------------------------------------------------------

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




    // END OF LYRICMAPUI SECTION -------------------------------------------------------------------

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

            //Marker is displayed on map, when clicked it shows user it's style (interesting, boring...)
            MarkerOptions marker = new MarkerOptions().position(latLng).title(p.getStyleUrl().substring(1));
            BitmapDescriptor icon = getPlacemarkIcon(p.getStyleUrl());
            marker.icon(icon);
            map.addMarker(marker);

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
