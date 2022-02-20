package cu.nat.wenisimo.appdomino;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import cu.nat.wenisimo.appdomino.dominoapi.DominoApiService;
import cu.nat.wenisimo.appdomino.fragmentos.BlankFragment;
import cu.nat.wenisimo.appdomino.fragmentos.FragmentoAddData;
import cu.nat.wenisimo.appdomino.fragmentos.FragmentoConfiguracion;
import cu.nat.wenisimo.appdomino.fragmentos.FragmentoRelojDomino;
import cu.nat.wenisimo.appdomino.fragmentos.FragmentoRonda;
import cu.nat.wenisimo.appdomino.fragmentos.FragmentoRondaFinal;
import cu.nat.wenisimo.appdomino.models.Preference;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentoRonda.OnFragmentInteractionListener,
        FragmentoConfiguracion.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener,
        FragmentoAddData.OnFragmentInteractionListener,
        FragmentoRelojDomino.OnFragmentInteractionListener,
        FragmentoRondaFinal.OnFragmentInteractionListener {

    public static final String EMPTY_STR = "";
    public static final String DATOS = "datos";
    public static final String MESA_ID = "Mesa_id";
    public static final String NUMERO_DATA = "NumeroData";
    public static final String SERVIDOR = "Servidor";
    public static final String URL_INICIAL = "http://192.168.1.103/domino_api/";
    public static final String EVENTO_ID = "Evento_id";
    public static final String RONDA = "Ronda";
    public static final String PAREJA1 = "Pareja1";
    public static final String PAREJA2 = "Pareja2";
    public static final String PAREJA_SALIDORA = "ParejaSalidora";
    public static String baseURL;
    String parejaGanadora = "null", pareja1, pareja2;
    Integer rondas, data = 0;
    Preference preferencesClass;
    BlankFragment FraInicio;
    Bundle datosFragment;

    public static DominoApiService api() {
        DominoApiService API_SERVICE;
        Interceptor interceptorError = new ErrorInterceptor();
        //HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //okHttpClient.addInterceptor(loggingInterceptor);
        //okHttpClient.addInterceptor(interceptorError);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        API_SERVICE = retrofit.create(DominoApiService.class);
        return API_SERVICE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FraInicio = new BlankFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmento, FraInicio).commit();
        preferencesClass = new Preference();
        preferencesClass.datos = getSharedPreferences(DATOS, Context.MODE_PRIVATE);
        if (preferencesClass.datos.getString(SERVIDOR, EMPTY_STR).equals(EMPTY_STR)) {
            guardarDatos(SERVIDOR, URL_INICIAL);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmento, new FragmentoConfiguracion());
            fragmentTransaction.commit();
        }
        baseURL = preferencesClass.datos.getString(SERVIDOR, EMPTY_STR);
        pareja1 = preferencesClass.datos.getString(PAREJA1, EMPTY_STR);
        pareja2 = preferencesClass.datos.getString(PAREJA2, EMPTY_STR);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragmentS = new BlankFragment();
        if (id == R.id.nav_configuracion) {
            fragmentS = new FragmentoConfiguracion();
            datosFragment = new Bundle();
            datosFragment.putString(SERVIDOR, preferencesClass.datos.getString(SERVIDOR, EMPTY_STR));
            fragmentS.setArguments(datosFragment);
        } else if (id == R.id.nav_ronda) {
            fragmentS = new FragmentoRonda();
        } else if (id == R.id.nav_inicio) {
            fragmentS = new BlankFragment();
        } else if (id == R.id.nav_resultados) {
            fragmentS = new FragmentoRondaFinal();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmento, fragmentS);
        fragmentTransaction.commit();
        return true;
    }

    public void guardarDatos(String key, String valor) {
        preferencesClass.datos = getSharedPreferences(DATOS, Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        Obj_preferences.putString(key, valor);
        Obj_preferences.apply();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class ErrorInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int code = response.code();

            if ((code > 299) && (code < 600)) {
            }
            return response;
        }
    }
}
