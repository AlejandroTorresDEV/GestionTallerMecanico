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

import com.example.alejandrotorresruiz.taller.AdapterReciclerView.MyAdapterCitas;
import com.example.alejandrotorresruiz.taller.Entities.Citas;
import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.example.alejandrotorresruiz.taller.Utils.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.alejandrotorresruiz.taller.R.layout.fragment_citas;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitasFragment extends Fragment {

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

        final View view = inflater.inflate(fragment_citas, container, false);

        serviceUsuarios = new ServiceUsuarios();

        recogerSharedPreferences();

        habilitarElementosInterfaz(view);

        llamadaGetWS();

        return view;
    }

    public void rellenarReciclerView(List<Citas> citas){
        //recyclerView.setVisibility(View.GONE);
        recyclerViewAdapter = new MyAdapterCitas(getContext() ,citas, R.layout.recycler_view_citas, new MyAdapterCitas.OnItemClickListener() {
            @Override
            public void onItemClick(Citas citas, int position) {
                Fragment fragment = new  DetalleCitaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                Bundle bundle = new Bundle();
                bundle.putString("nombreTaller", citas.getNombre());
                bundle.putString("fotoTaller", citas.getFoto());
                bundle.putString("fotoCoche", citas.getFoto_vehiculo());
                bundle.putString("diaCita", citas.getFecha());
                bundle.putString("horaCita", citas.getHora());
                fragment.setArguments(bundle);
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


    public void llamadaGetWS(){
        CitasFragment.RecogerDatosUsuarioWS gu = new CitasFragment.RecogerDatosUsuarioWS();
        gu.execute(Utilidades.rutaCitasUsuario+correo);
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
                ArrayList<Citas> array = new ArrayList();
                for (int i = 0;i<jsArray.length();i++){
                    Citas cita = new Citas();
                    JSONObject rec = jsArray.getJSONObject(i);
                    cita.setFecha(rec.getString("fecha"));
                    cita.setHora(rec.getString("hora"));
                    cita.setKm(rec.getString("km"));
                    cita.setId_vehiculo(rec.getString("id_vehiculo"));
                    cita.setAnno(rec.getString("anno"));
                    cita.setNombre(rec.getString("nombre"));
                    cita.setFoto(rec.getString("foto"));
                    cita.setFoto_vehiculo(rec.getString("foto_vehiculo"));
                    array.add(cita);
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

    public CitasFragment() {
        // Required empty public constructor
    }
}