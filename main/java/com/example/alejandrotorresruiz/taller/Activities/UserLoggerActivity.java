package com.example.alejandrotorresruiz.taller.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alejandrotorresruiz.taller.Entities.Usuario;
import com.example.alejandrotorresruiz.taller.Fragments.CitasFragment;
import com.example.alejandrotorresruiz.taller.Fragments.FragmentAddCita;
import com.example.alejandrotorresruiz.taller.Fragments.InfoFragment;
import com.example.alejandrotorresruiz.taller.Fragments.MapsFragment;
import com.example.alejandrotorresruiz.taller.Fragments.VehiculosFragment;
import com.example.alejandrotorresruiz.taller.R;
import com.example.alejandrotorresruiz.taller.ServiceAPP.ServiceUsuarios;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserLoggerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String correo,foto;
    private ServiceUsuarios serviceUsuarios;
    private SharedPreferences prefs;
    private View header;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    boolean fragmentTransaction;
    private Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logger);

        recogerSharedPreferences();

        habilitarElementosInterfaz();

        llamadaGetWS();

        setFragmentByDefault();

        habilitarBotonesInterfaz();
    }

    /**
     * Marcamos la opcion seleccionada y cargamos su titulo al actionBar
     */
    private void changeFragmentNavigate(Fragment fragment,MenuItem item){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("Correo", correo);
        fragment.setArguments(bundle);
        getSupportActionBar().setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("Correo", correo);
        fragment.setArguments(bundle);
        getSupportActionBar().setTitle("Nueva cita");
        drawerLayout.closeDrawers();
    }

    /**
     * Establecemos el fragment inicial al cargar el activity
     */
    private void setFragmentByDefault() {
        changeFragmentNavigate(new CitasFragment(), navigationView.getMenu().getItem(0));
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void guardarUsuario(Usuario usuario){
        TextView text = (TextView) header.findViewById(R.id.textoUsuario);
        ImageView imageView = (ImageView) header.findViewById(R.id.imageViewUser);
        Picasso.with(navigationView.getContext()).load("http://andresterol.int.elcampico.org:8080/taller/"+usuario.getFoto()).resize(250,250).centerCrop().error(R.mipmap.ic_launcher).into(imageView);
        text.setText(usuario.getUsuario());
    }

    public void eliminarSession(){
        prefs.edit().clear().apply();//ELIMINAR TODAS LAS CLAVES
        //pref.edit().remove("email").commit(); ELIMINAR CLAVE ESPECIFICA
        Intent intent = new Intent(UserLoggerActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void recogerSharedPreferences(){
        prefs = getSharedPreferences("DatosAcceso", Context.MODE_PRIVATE);
        correo = prefs.getString("correo","");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                /*Abrir menu*/
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_SignOut:
                eliminarSession();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void habilitarElementosInterfaz(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navView);

        serviceUsuarios = new ServiceUsuarios();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        header = navigationView.getHeaderView(0);
        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
    }

    public void habilitarBotonesInterfaz(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                fragmentTransaction = false;

                switch (item.getItemId()) {
                    case R.id.menu_citas:
                        fragment = new CitasFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_coches:
                        fragment = new VehiculosFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_talleres:
                        fragment = new InfoFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_usuarios:
                        fragment = new MapsFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_SignOut:
                        eliminarSession();
                        break;
                }

                /**
                 * Si hemos seleccionado una opción cargamos su fragment.
                 */
                if (fragmentTransaction) {
                    changeFragmentNavigate(fragment, item);
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new FragmentAddCita());
                drawerLayout.closeDrawers();
            }
        });
    }

    public void llamadaGetWS(){
        UserLoggerActivity.RecogerDatosUsuarioWS gu = new UserLoggerActivity.RecogerDatosUsuarioWS();
        gu.execute("http://andresterol.int.elcampico.org:8080/taller/usuarios/"+correo);
    }
    /**
     * Hilo donde llamaremos al WS para enviarle datos y recibir una respuesta.
     */
    class RecogerDatosUsuarioWS extends AsyncTask<String,Boolean,String> {

        @Override
        protected String doInBackground(String... strings) {
            return serviceUsuarios.GetRequest(strings[0]);
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                Usuario usuario = new Usuario();
                JSONArray jsArray = new JSONArray(s);
                JSONObject rec = jsArray.getJSONObject(0);
                usuario.setUsuario(rec.getString("usuario"));
                usuario.setClave(rec.getString("clave"));
                usuario.setFoto(rec.getString("foto"));
                usuario.setToken(rec.getString("token"));
                usuario.setFecha_inicio(rec.getString("fecha_inicio"));
                usuario.setFecha_fin(rec.getString("fecha_fin"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                guardarUsuario(usuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
