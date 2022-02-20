package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.dominoapi.DominoApiService;
import cu.nat.wenisimo.appdomino.models.Evento;
import cu.nat.wenisimo.appdomino.models.EventoRespuesta;
import cu.nat.wenisimo.appdomino.models.Mesa;
import cu.nat.wenisimo.appdomino.models.MesaRespuesta;
import cu.nat.wenisimo.appdomino.models.Preference;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoConfiguracion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoConfiguracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoConfiguracion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText servidorET;
    Spinner mesas, evento;
    String parejaGanadora = "null", pareja1, pareja2;
    Integer rondas, data = 0;
    Button actualizar, guardar;
    ArrayAdapter<String> mesasAdapter;
    ArrayAdapter<String> eventoAdapter;
    Preference preferencesClass;
    ArrayList<Mesa> listMesas;
    ArrayList<Evento> listEvento;
    Integer iP = 0;
    View vista;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentoConfiguracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoConfiguracion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoConfiguracion newInstance(String param1, String param2) {
        FragmentoConfiguracion fragment = new FragmentoConfiguracion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        obtenerDatos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_configuracion, container, false);
        servidorET = (EditText) vista.findViewById(R.id.servidor);
        evento = (Spinner) vista.findViewById(R.id.evento);
        mesas = (Spinner) vista.findViewById(R.id.mesas);
        actualizar = (Button) vista.findViewById(R.id.actualizar);
        guardar = (Button) vista.findViewById(R.id.guardar);
        actualizar.setVisibility(View.GONE);
        String servidor = preferencesClass.datos.getString("Servidor", "");
        Toast.makeText(getContext(), servidor, Toast.LENGTH_LONG).show();
        servidorET.setText(servidor);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!servidorET.getText().toString().equals("")) {
                    SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
                    Obj_preferences.putString("Servidor", servidorET.getText().toString());
                    Obj_preferences.apply();
                    Toast.makeText(getContext(), "Se guardaron los datos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        evento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer evento = Integer.parseInt(listEvento.get(position).getId());
                obtenerMesas(evento, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mesas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer mesa = Integer.parseInt(listMesas.get(position).getId());
                preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
                Obj_preferences.putInt(MainActivity.MESA_ID, mesa);
                Obj_preferences.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void llenarEventos(ArrayList<Evento> listaEventosObtenidos) {
        List<String> listaEventos = new ArrayList<>();
        for (Evento e : listaEventosObtenidos) {
            listaEventos.add(e.getNombre());
        }
        eventoAdapter = new ArrayAdapter<>(getContext(), R.layout.stylos_spiner, listaEventos);
        eventoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        evento.setAdapter(eventoAdapter);
    }

    public void obtenerDatos() {
        DominoApiService API_SERVICE = api();
        Call<EventoRespuesta> Eventos = API_SERVICE.obtenerEventosIniciados();
        try {
            Eventos.enqueue(new FragmentoConfiguracion.EventosCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public DominoApiService api1() {
        DominoApiService API_SERVICE;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(loggingInterceptor);
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String baseURL = servidorET.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        API_SERVICE = retrofit.create(DominoApiService.class);
        return API_SERVICE;
    }


    public DominoApiService api() {
        DominoApiService API_SERVICE;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(loggingInterceptor);
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String baseURL = preferencesClass.datos.getString("Servidor", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        API_SERVICE = retrofit.create(DominoApiService.class);
        return API_SERVICE;
    }

    public void obtenerMesas(Integer evento_id, int num) {
        DominoApiService API_SERVICE;
        if (num == 1) {
            API_SERVICE = api1();
        } else {
            API_SERVICE = api();
        }
        Call<MesaRespuesta> Mesas = API_SERVICE.obtenerMesas(evento_id);
        try {
            Mesas.enqueue(new FragmentoConfiguracion.MesasCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        Obj_preferences.putInt("Evento_id", evento_id);
        Obj_preferences.apply();
    }

    private void llenarMesas(ArrayList<Mesa> listaMesasObtenidos) {
        List<String> listaMesas = new ArrayList<>();
        for (Mesa m : listaMesasObtenidos) {
            listaMesas.add("Mesa " + m.getNumero());
        }
        mesasAdapter = new ArrayAdapter<>(getContext(), R.layout.stylos_spiner, listaMesas);
        mesasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mesas.setAdapter(mesasAdapter);
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

    private class EventosCallBack implements Callback<EventoRespuesta> {

        @Override
        public void onResponse(Call<EventoRespuesta> call, Response<EventoRespuesta> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_LONG).show();
                actualizar.setVisibility(View.GONE);
                EventoRespuesta eventos = response.body();
                if (eventos != null) {
                    listEvento = eventos.getEventos();
                    llenarEventos(listEvento);
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
                actualizar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<EventoRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            actualizar.setVisibility(View.VISIBLE);
        }
    }

    private class MesasCallBack implements Callback<MesaRespuesta> {

        @Override
        public void onResponse(Call<MesaRespuesta> call, Response<MesaRespuesta> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_LONG).show();
                actualizar.setVisibility(View.GONE);
                MesaRespuesta mesas = response.body();
                if (mesas != null) {
                    listMesas = mesas.getMesas();
                    llenarMesas(listMesas);
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
                actualizar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<MesaRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            actualizar.setVisibility(View.VISIBLE);
        }
    }
}
