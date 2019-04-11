package com.example.alejandrotorresruiz.taller.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alejandrotorresruiz.taller.AdapterReciclerView.MyAdapterVehiculos;
import com.example.alejandrotorresruiz.taller.Entities.Vehiculos;
import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.example.alejandrotorresruiz.taller.Utils.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehiculosFragment extends Fragment {

    private String correo;
    private ServiceUsuarios serviceUsuarios;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ProgressBar progressBar;
    private SharedPreferences prefs;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_vehiculos, container, false);

        serviceUsuarios = new ServiceUsuarios();

        recogerSharedPreferences();

        habilitarElementosInterfaz(view);

        llamadaGetWS();

        return view;
    }

    public void rellenarReciclerView(List<Vehiculos> vehiculos){
        //recyclerView.setVisibility(View.GONE);
        recyclerViewAdapter = new MyAdapterVehiculos(getContext() ,vehiculos, R.layout.recycler_view_vehiculos, new MyAdapterVehiculos.OnItemClickListener() {
            @Override
            public void onItemClick(Vehiculos vehiculos, int position) {
                /*Fragment fragment = new  DetalleCitaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                Bundle bundle = new Bundle();
                bundle.putString("nombreTaller", vehiculos.getNombre());
                bundle.putString("fotoTaller", vehiculos.getFoto());
                bundle.putString("fotoCoche", vehiculos.getFoto_vehiculo());
                bundle.putString("diaCita", vehiculos.getFecha());
                bundle.putString("horaCita", vehiculos.getHora());
                fragment.setArguments(bundle);*/
            }
        });
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void recogerSharedPreferences(){
        prefs = this.getActivity().getSharedPreferences("DatosAcceso", Context.MODE_PRIVATE);
        correo = prefs.getString("correo","");
    }

    public void habilitarElementosInterfaz(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setVisibility(View.INVISIBLE);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
    }

    /**
     * Hilo donde llamaremos al WS para enviarle datos y recibir una respuesta.
     */
    class RecogerDatosUsuarioWS extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            return serviceUsuarios.GetRequest(strings[0]);
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            try {
                JSONArray jsArray = new JSONArray(s);
                ArrayList<Vehiculos> array = new ArrayList();
                for (int i = 0;i<jsArray.length();i++){
                    Vehiculos vehiculos = new Vehiculos();
                    JSONObject rec = jsArray.getJSONObject(i);
                    vehiculos.setMatricula(rec.getString("matricula"));
                    vehiculos.setMarca(rec.getString("marca"));
                    vehiculos.setModelo(rec.getString("modelo"));
                    vehiculos.setAnno(rec.getString("anno"));
                    vehiculos.setColor(rec.getString("color"));
                    vehiculos.setFoto(rec.getString("foto"));
                    vehiculos.setId_cliente(rec.getString("id_cliente"));
                    vehiculos.setId_vehiculo_tipo(rec.getString("id_vehiculo_tipo"));
                    array.add(vehiculos);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                if(jsArray.length()==0){
                    textView.setVisibility(View.VISIBLE);
                }else{
                    rellenarReciclerView(array);
                }
                Log.d("ARRAY ----------------",array.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void llamadaGetWS(){
        VehiculosFragment.RecogerDatosUsuarioWS gu = new VehiculosFragment.RecogerDatosUsuarioWS();
        gu.execute(Utilidades.rutaListarVehiculos+correo);
    }

    public VehiculosFragment() {}

}
