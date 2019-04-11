package com.example.alejandrotorresruiz.taller.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alejandrotorresruiz.taller.Entities.Taller;
import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.example.alejandrotorresruiz.taller.Utils.Utilidades;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private View rootView;
    private GoogleMap gMap;
    private MapView mapView;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final String LOGTAG = "android-localizacion";
    private static final int PHONE_CALL_CODE = 100;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 102;
    private SupportMapFragment mapFragment;
    private GoogleApiClient apiClient;
    private ServiceUsuarios serviceUsuarios;
    private ArrayList<Taller> listadoMarker;
    double latitude, longitude;
    private int telefonoTaller;
    public MapsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        listadoMarker = new ArrayList<Taller>();
        serviceUsuarios = new ServiceUsuarios();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = rootView.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            apiClient = new GoogleApiClient.Builder(mapView.getContext())
                    .enableAutoManage(getActivity(), this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

            mapView.getMapAsync(this);
            MapsFragment.RecogerDatosUsuarioWS gu = new MapsFragment.RecogerDatosUsuarioWS();
            gu.execute(Utilidades.rutaListaTalleres);
        }
    }


    public Bitmap changeSizeIcons(Bitmap iconName,int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(iconName, width, height, false);
        return resizedBitmap;
    }

    public void changeImgIcons(){
        final Bitmap[] bitmap2 = new Bitmap[1];
        for (int i = 0 ; i<listadoMarker.size();i++){
            LatLng marker = new LatLng(listadoMarker.get(i).getLatitud(), listadoMarker.get(i).getLongitud());
            try {
                Picasso.with(getActivity())
                        .load("http://andresterol.int.elcampico.org:8080/taller/"+listadoMarker.get(i).getFoto())
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                bitmap2[0] =  bitmap;
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {}
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {}
                        });
                gMap.addMarker(new MarkerOptions().position(marker).title(listadoMarker.get(i).getNombre())).setIcon(BitmapDescriptorFactory.fromBitmap(changeSizeIcons(bitmap2[0],100,100)));
            } catch(Exception e) {
                gMap.addMarker(new MarkerOptions().position(marker).title(listadoMarker.get(i).getNombre()));
            }
        }
    }

    public void rellenarMarker(List<Taller> listado){
        listadoMarker = new ArrayList<Taller>(listado);
        changeImgIcons();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMarkerClickListener(this);
        gMap.getUiSettings().isZoomGesturesEnabled();
    }


    @Override
    public void onPause() {
        super.onPause();
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AlertDialog.Builder builder;

        Toast.makeText(getActivity(), marker.getId()+"", Toast.LENGTH_SHORT).show();
        final double latitudeMarker = marker.getPosition().latitude;
        final double longitudeMarker = marker.getPosition().longitude;

        builder = new AlertDialog.Builder(getActivity());

         telefonoTaller = buscarTelefonoTaller(marker.getTitle());

        builder.setTitle("¿Que desea hacer?")
                .setPositiveButton("RUTA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitudeMarker + "," + longitudeMarker + "&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                })
                .setNegativeButton("TELEFONO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                        } else {
                            //Versiones anterioriores a CORE 6.
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse(""+telefonoTaller));
                            if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                                startActivity(i);
                            }
                        }
                    }
                })
                .show();
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
            gMap.setMyLocationEnabled(true);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            updateUI(lastLocation);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    public boolean CheckPermission(String permission){
        int result = getActivity().checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PETICION_PERMISO_LOCALIZACION:
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permiso concedido

                    @SuppressWarnings("MissingPermission")
                    Location lastLocation =
                            LocationServices.FusedLocationApi.getLastLocation(apiClient);

                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                    updateUI(lastLocation);

                }
                break;

            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:"+telefonoTaller));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(i);
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void updateUI(Location loc) {
        if (loc != null) {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();

            final LatLng miUbicacion = new LatLng(latitude, longitude);

            gMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicacion actual."));


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(miUbicacion)
                    .zoom(6)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            // mMap.setInfoWindowAdapter(new CustomInfoAdapter(LayoutInflater.from(this),"pepe","pepa"));

            gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(miUbicacion)
                            .zoom(8)
                            .build();
                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    return false;
                }
            });
        }
    }

    class RecogerDatosUsuarioWS extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            return serviceUsuarios.GetRequest(strings[0]);
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            //Toast.makeText(getActivity(), s+"", Toast.LENGTH_LONG).show();

            try {
                JSONArray jsArray = new JSONArray(s);
                ArrayList<Taller> talleresArray = new ArrayList();
                for (int i = 0;i<jsArray.length();i++){
                    Taller taller = new Taller();
                    JSONObject rec = jsArray.getJSONObject(i);
                    taller.setNombre(rec.getString("nombre"));
                    taller.setDireccion(rec.getString("direccion"));
                    taller.setTelefono(rec.getInt("telefono"));
                    taller.setFoto(rec.getString("foto"));
                    taller.setLatitud(rec.getDouble("latitud"));
                    taller.setLongitud(rec.getDouble("longitud"));
                    talleresArray.add(taller);
                }
                //Toast.makeText(getActivity(), talleresArray.toString()+"", Toast.LENGTH_SHORT).show();
               rellenarMarker(talleresArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public int buscarTelefonoTaller(String nombreTaller){
        for (int i = 0;i<listadoMarker.size();i++){
            if(listadoMarker.get(i).getNombre().equals(nombreTaller)) {
                return listadoMarker.get(i).getTelefono();
            }
        }
        return 666666666;
    }

}
