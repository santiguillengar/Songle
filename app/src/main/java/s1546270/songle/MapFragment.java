package s1546270.songle;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import s1546270.songle.Objects.Style;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = MapFragment.class.getSimpleName();

    GoogleMap map;



    //EVERYTHING NEW BELOW

    // Store placemarks parsed
    List<Placemark> placemarks = null;
    List<Style> styles = null;

    String mapDifficulty = null;

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


        try {
            mapDifficulty = getActivity().getIntent().getExtras().getString("difficulty");

        } catch (Exception e) {
            Log.e(TAG, "Exception raised retriving map difficulty");
        }
        Log.d(TAG, "LyricMapUI activity created. Map Difficulty: "+mapDifficulty);

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
        Log.d(TAG, "     |SANTI|     LyricMapUI - Placemarks being placed on map. ");


        // HANDLE PLACEMARKS FOR MAP
        String url = determineMapUrl();
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

            //Marker is displayed on map, when clicked it shows user it's style (interesting, boring...)
            MarkerOptions marker = new MarkerOptions().position(latLng).title(p.getStyleUrl().substring(1));
            BitmapDescriptor icon = getPlacemarkIcon(p.getStyleUrl());
            marker.icon(icon);
            map.addMarker(marker);

        }
    }

    private String determineMapUrl() {

        // Example: "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map1.kml"

        String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/";

        //choose song randomly from range (1 - numSongs)
        int random = (int) Math.random() * R.integer.numSongs + 1;
        if (random < 10) {
            songNumber = "0"+random;
        } else {
            songNumber = ""+random;
        }

        String url = baseUrl + songNumber + "/map" + mapDifficulty + ".kml";

        return url;
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
