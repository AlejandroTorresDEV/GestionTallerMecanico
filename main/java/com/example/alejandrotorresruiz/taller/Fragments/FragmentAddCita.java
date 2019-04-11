package com.example.alejandrotorresruiz.taller.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.example.alejandrotorresruiz.taller.Utils.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.alejandrotorresruiz.taller.R.layout.fragment_fragment_add_cita;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddCita extends Fragment implements View.OnClickListener {

    public ServiceUsuarios serviceUsuarios;
    private Map<String, String> params;
    private ArrayList<String> listadoTalleres;
    private ArrayList<String> listadoVehiculos;
    private ProgressBar progressBar;
    private Button button;
    private EditText editTextHora, editTextFecha;
    private Spinner spinnerTaller, spinnerVehiculo;
    private RelativeLayout relativeLayout;
    private ImageButton imageButton, buttonCalendar;
    final private String TAGListadoTALLER = "listadoTalleres";
    int mYear, mMonth, mDay, horas, minutos;
    private String date;
    private Calendar c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(fragment_fragment_add_cita, container, false);

        listadoTalleres = new ArrayList<>();
        listadoVehiculos = new ArrayList<>();

        serviceUsuarios = new ServiceUsuarios();

        habilitarElementosInterfaz(view);

        llamadaGetWS();
        return view;
    }


    public void llamadaPostWS() {
        FragmentAddCita.AddCitaUsuarioWS gu = new FragmentAddCita.AddCitaUsuarioWS();
        gu.execute(Utilidades.rutaAgregarCita);
    }

    public void llamadaGetWS() {

        FragmentAddCita.RecogerListadoWS gu = new FragmentAddCita.RecogerListadoWS();
        gu.execute(Utilidades.rutaListaTalleres, TAGListadoTALLER);
        FragmentAddCita.RecogerListadoWS gu2 = new FragmentAddCita.RecogerListadoWS();
        gu2.execute(Utilidades.rutaListaVehiculos, "");

    }

    public void recogerDatosFormulario() {

        String elementoSpinnerVehiculo = spinnerVehiculo.getSelectedItem().toString();
        String elementoSpinnerTaller = spinnerTaller.getSelectedItem().toString();

        params.put("fecha",mYear+"/"+mMonth+"/"+mDay);
        params.put("hora",horas+":"+minutos);
        params.put("matricula",elementoSpinnerVehiculo);
        params.put("taller",elementoSpinnerTaller);

    }

    public void rellenarSpinnerListadoTalleres() {
        spinnerTaller.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, listadoTalleres));
    }

    public void rellenarSpinnerListadoVehiculos() {
        spinnerVehiculo.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, listadoVehiculos));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonCrearCita:
                    crearCita();
                break;

            case R.id.buttonTimePicker:
                    mostrarTimePicker();
                break;

            case R.id.buttonDatePicker:
                    mostrarDatePicker();
                break;
        }
    }

    /**
     * Hilo donde llamaremos al WS para enviarle datos y recibir una respuesta.
     */
    class AddCitaUsuarioWS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animacionCargandoAnimacion();
                }
            });

            return serviceUsuarios.formPostRequest(strings[0], params);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObj = new JSONObject(s);
                Boolean estadoInserccion = jsonObj.getBoolean("estado");

                if (estadoInserccion){
                    volverFragmentPrincipal();
                }else{
                    editTextFecha.setError("Ese día ya esta ocupado.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    quitarAnimacionCargandoAnimacion();
                }
            });
        }
    }

    /**
     * Hilo donde llamaremos al WS para recibir una respuesta.
     */
    class RecogerListadoWS extends AsyncTask<String, Void, String> {

        String listado;

        @Override
        protected String doInBackground(String... strings) {
            listado = strings[1];
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animacionCargandoAnimacion();
                }
            });
            return serviceUsuarios.GetRequest(strings[0]);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final ArrayList<String> array = new ArrayList();
                JSONArray jsArray = new JSONArray(s);

                for (int i = 0; i < jsArray.length(); i++) {
                    JSONObject rec = jsArray.getJSONObject(i);
                    if (listado == "listadoTalleres") {
                        array.add(rec.getString("nombre"));
                    } else {
                        array.add(rec.getString("matricula"));
                    }
                }

                if (listado == "listadoTalleres") {
                    listadoTalleres = array;
                    rellenarSpinnerListadoTalleres();
                } else {
                    listadoVehiculos = array;
                    rellenarSpinnerListadoVehiculos();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        quitarAnimacionCargandoAnimacion();
                    }
                });

                Log.d("ARRAY ----------------", array.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void crearCita(){
        if (editTextHora.getText().toString().equals("")|| editTextHora.getText().toString().equals("")) {
            editTextFecha.setError("El formato de la fecha es : YYYY-MM-DD");
            editTextHora.setError("El formato de la hora es : YYYY-MM-DD");
        } else {
            recogerDatosFormulario();
            llamadaPostWS();
        }
    }

    public void volverFragmentPrincipal(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new CitasFragment()).commit();
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle("Mis citas");
    }
    public void mostrarTimePicker(){
        final Calendar c = Calendar.getInstance();
        horas = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int horaDelDia, int minutos) {
                editTextHora.setText(horaDelDia + ":" + minutos);
            }
        }, horas, minutos, false);

        timePickerDialog.show();
    }

    public void mostrarDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        calendar.set(year, month, day);
                        date = new SimpleDateFormat("yyyy/dd/MM").format(calendar.getTime());
                        mYear = calendar.get(Calendar.YEAR);
                        mMonth = calendar.get(Calendar.MONTH);
                        mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        editTextFecha.setText(date);
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());

        Calendar d = Calendar.getInstance();
        d.add(Calendar.MONTH, 1);

        dpd.getDatePicker().setMaxDate(d.getTimeInMillis());
        dpd.show();
    }

    public void animacionCargandoAnimacion() {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            View child = relativeLayout.getChildAt(i);
            if (child.getId() == R.id.progressBar) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public void quitarAnimacionCargandoAnimacion() {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            View child = relativeLayout.getChildAt(i);
            if (child.getId() == R.id.progressBar) {
                child.setVisibility(View.INVISIBLE);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public void habilitarElementosInterfaz(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        button = (Button) view.findViewById(R.id.buttonCrearCita);
        editTextHora = (EditText) view.findViewById(R.id.editTextHora);
        editTextFecha = (EditText) view.findViewById(R.id.editTextFecha);
        spinnerTaller = (Spinner) view.findViewById(R.id.spinnerTaller);
        spinnerVehiculo = (Spinner) view.findViewById(R.id.spinnerVehiculo);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        imageButton = (ImageButton) view.findViewById(R.id.buttonDatePicker);
        buttonCalendar = (ImageButton) view.findViewById(R.id.buttonTimePicker);
        params = new HashMap<String, String>();

        button.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        buttonCalendar.setOnClickListener(this);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    public FragmentAddCita() {
        // Required empty public constructor
    }
}
