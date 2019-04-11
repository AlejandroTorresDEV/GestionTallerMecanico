package com.example.alejandrotorresruiz.taller.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.example.alejandrotorresruiz.taller.Utils.Utilidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alejandrotorresruiz on 01/12/2018.
 */

public class MainActivity extends AppCompatActivity{

    private Switch idRecordar;
    private EditText etEmail,etPassword;
    private Button btnAcceder;
    private ServiceUsuarios serviceUsuarios;
    private Boolean accesoHabilitado = false;
    private ProgressBar progresBar;
    private SharedPreferences prefs;
    private Boolean[] accesoAcceptado;
    private String emailUsuario,foto;
    private String passwordUsuario;
    private Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Inicializamos los elementos de IU.
         */
        inicializarIUActivity();

        iniciarBotonesActicity();
    }

    /**
     * Metodo para validar el acceso del usuario.
     */
    private void validarAcceso() {
        emailUsuario = String.valueOf(etEmail.getText());
        passwordUsuario = String.valueOf(etPassword.getText());
        accesoAcceptado = new Boolean[1];

        if (emailUsuario.isEmpty() || passwordUsuario.isEmpty()){
            etEmail.setError("El email no puede ser vacío.");
            etPassword.setError("La contraseña no puede ser vacío.");
        }else{
            /**
             * Preparo los parametros para enviarselos al servidor
             * */
            params.put("usuario",emailUsuario);
            params.put("clave",passwordUsuario);
            ValidarAccesoWS gu = new ValidarAccesoWS();
            gu.execute(Utilidades.rutaAccesoUsuario);
        }
    }

    /**
     * Hilo donde llamaremos al WS para enviarle datos y recibir una respuesta.
     */
    class ValidarAccesoWS extends AsyncTask<String,Boolean,String> {
        @Override
        protected String doInBackground(String... strings) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    habilidarProgresBar();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            return serviceUsuarios.formPostRequest(strings[0],params);
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(!s.isEmpty()){
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    accesoAcceptado[0] = jsonObj.getBoolean("estado");
                    //foto = jsonObj.getString("foto");
                    accesoHabilitado = accesoAcceptado[0];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            desabilitarProgresBar();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);/////
                        }
                    });
                    accederIndexAPP();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     *
     */
    private void accederIndexAPP(){
        if(accesoHabilitado){
            guardarShared(emailUsuario, passwordUsuario);
            Intent intent = new Intent(this, UserLoggerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.putExtra("foto",foto);
            startActivity(intent);
        }else{
            etEmail.setError("El email es erroneo");
            etPassword.setError("Contraseña incorrecta");
        }
    }

    public void inicializarIUActivity(){
        prefs = getSharedPreferences("DatosAcceso", Context.MODE_PRIVATE);
        idRecordar = (Switch) findViewById(R.id.idRecordar);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        progresBar = (ProgressBar) findViewById(R.id.progressBar);
        params = new HashMap<String, String>();
        serviceUsuarios = new ServiceUsuarios();
        progresBar.setVisibility(View.GONE);

        /**
         * Al iniciar el activity compruebo si los datos de las SharedPreferences continuan siendo correctos y si asi es accedo a la aplicacion los datos previamente guardados.
         */
        recogerDatosShared();
    }

    public void iniciarBotonesActicity(){
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                validarAcceso();
            }
        });
    }

    public void habilidarProgresBar(){
        progresBar.setVisibility(View.VISIBLE);
        idRecordar.setVisibility(View.INVISIBLE);
        etEmail.setVisibility(View.INVISIBLE);
        etPassword.setVisibility(View.INVISIBLE);
        btnAcceder.setVisibility(View.INVISIBLE);
    }

    public void desabilitarProgresBar(){
        progresBar.setVisibility(View.GONE);
        idRecordar.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        btnAcceder.setVisibility(View.VISIBLE);
    }

    public void guardarShared(String usuario,String password){
        if(idRecordar.isChecked()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("correo",usuario);
            editor.putString("password",password);
            editor.commit();
        }
    }

    /**
     * Creo unas nuevas SharedPreferences para guardar los datos del acceso del usuario.
     */
    public void recogerDatosShared(){
        String usuario = prefs.getString("correo","");
        String password = prefs.getString("password","");
        etEmail.setText(usuario);
        etPassword.setText(password);
        if(!usuario.isEmpty() && !password.isEmpty()){
            //checkBoxPrefs.setChecked(true);
            validarAcceso();
            if(accesoHabilitado) {
                accederIndexAPP();
            }
        }
    }
}

