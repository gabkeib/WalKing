package com.gryco.walking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnInfoWindowClickListener {

    private GoogleMap mMap;

    private static final String TAG = "com.gryco.walking";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private com.google.maps.model.LatLng latlng;
    private LatLng latLngg;
    private GeoApiContext mGeoApiContext = null;
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private BroadcastReceiver receiver;
    private FusedLocationProviderClient mFusedLocationClient;
    private Places places = new Places();

    private HashMap<ClusterMarker, Integer> mHashMap = new HashMap<ClusterMarker, Integer>();

    static final LatLng Baznycia = new LatLng(55.524478, 25.100715);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter("LOCATION_BROADCAST"));
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void getLastKnownLocation() {
        Log.d(TAG, "called");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /*mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                }
            }
        });*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location location = (Location) task.getResult();
                       // convertToLatLng(location);

                    }
                }
            });
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }

   /* private void convertToLatLng(Location location) {
        com.google.maps.model.LatLng latLng1 = new com.google.maps.model.LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLnggg = new LatLng(location.getLatitude(), location.getLongitude());
        latlng = latLng1;
        latLngg = latLnggg;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double latitude = intent.getDoubleExtra("LATITUDE",0);
                Log.i(TAG,"LATITUDE " + latitude);
                double longtitude = intent.getDoubleExtra("LONGITUDE",0);
                latlng = new  com.google.maps.model.LatLng(latitude,longtitude);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
       Intent serviceIntent = new Intent(this,LocationService.class);

        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            MapsActivity.this.startForegroundService(serviceIntent);
        }else{
            startService(serviceIntent);
        }
        getLastKnownLocation();
        // Add a marker in Sydney and move the camera
        LatLng home = new LatLng(55.520395, 25.116600);

        if (mMap!=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View row = getLayoutInflater().inflate(R.layout.custom_info,null);
                    ImageView imageView = row.findViewById(R.id.pic);
                    TextView name = row.findViewById(R.id.name);
                    TextView snip = row.findViewById(R.id.snippet);
                    String snippet = marker.getSnippet();
                    String snipp = snippet.substring(1);
                    name.setText(marker.getTitle());
                    snip.setText(snipp);
                    String idd = snippet.substring(0,1);
                    Log.i(TAG, "onInfoWindowClick: " + idd);
                    int num = Integer.parseInt(idd);
                    imageView.setImageResource(places.getImage(num));
                    //Log.i("Position of arraylist", pos+"");
                    return row;
                }
            });
        }
        addMapMarkers();
        //Marker baznycia = mMap.addMarker(new MarkerOptions().position(Baznycia).title("Anykščių Bažnyčia").snippet("Aukščiausia bažnyčia Lietuvoje"));
        float zoom = 15f;
        if (latlng!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngg, zoom));
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.525421, 25.103790), zoom));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
        enableLocation();
       // setPoiClick(mMap);

    }

    private void calculateDirections(Marker marker){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        Log.i(TAG,"LATITUDE " + latlng);
        directions.origin(latlng
        );
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for(DirectionsRoute route: result.routes){
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List <LatLng> newDecodedPath = new ArrayList<>();

                    for (com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(
                                latLng.lat, latLng.lng
                        ));
                    }

                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(MapsActivity.this,R.color.black));
                    polyline.setClickable(true);
                }
            }
        });
    }

    private void addMapMarkers(){

        if(mMap != null){

            if(mClusterManager == null){
                mClusterManager = new ClusterManager<ClusterMarker>(this.getApplicationContext(), mMap);
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        this,
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }
                   /* String snippet = "";
                    snippet = "Viewpoint of St. Matas church - the highest church in Lithuania. The height of this church reaches 79 metres. You can reach this viewpoint by climbing a lot of metal steps untill you reach the height of 33 metres. That is your goal! When you are finally there you will find yourself standing under all three bells of the church and the view of Anykščiai will be a pleasure for your eyes.";
                    String location = "Anykščių bažnyčia";

                    int avatar = R.drawable.untitled12;
                    int inside = R.drawable.untitled13;// set the default avatar
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            Baznycia,
                            location,
                            snippet,
                            avatar,
                            inside
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);*/
                   //TODO: change puntukas, laimes and horse photos
                    for (int i = 0; i < 5; i++){
                        String snippet = "";
                        snippet = String.valueOf(i) + places.getSnippet(i);
                        String location = places.getName(i);
                        com.google.android.gms.maps.model.LatLng coord = places.getLatlng(i);
                        Log.i(TAG, String.valueOf(coord));
                        int avatar = places.getImage(i);
                        int inside = places.getImage(i);// set the default avatar
                        ClusterMarker newClusterMarker = new ClusterMarker(
                                coord,
                                location,
                                snippet,
                                avatar,
                                inside,
                                String.valueOf(i)
                        );
                        mClusterManager.addItem(newClusterMarker);
                        mClusterMarkers.add(newClusterMarker);
                    }


            mClusterManager.cluster();
        }
    }

   /* private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();
            }
        });
    }*/

    private void enableLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        } else{
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                    break;
                }
        }
    }

    /*@Override
    public void onInfoWindowClick(final Marker marker) { //turned off until directions api will be allowed;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(marker.getSnippet()).setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                calculateDirections(marker);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }*/

    public void onInfoWindowClick(final Marker marker) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_info,null);
        ImageView iv = view.findViewById(R.id.pic);
        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        TextView name = view.findViewById(R.id.name);
        name.setText(marker.getSnippet());
        String id = marker.getId();
        Log.i(TAG, "onInfoWindowClick: " + id);
        String idd = id.substring(1);
        int num = Integer.parseInt(idd) - 1;
       iv.setImageResource(places.getImage(num));



    }

}
