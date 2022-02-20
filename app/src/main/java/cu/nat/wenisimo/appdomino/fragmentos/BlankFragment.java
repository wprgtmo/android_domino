package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.dominoapi.DominoApiService;
import cu.nat.wenisimo.appdomino.models.Evento;
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
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Preference preferencesClass;
    TextView mesasNumero, tituloEvento, comentarioEvento, textViewPagar;
    ImageView imageViewPagar, imageViewEvento;
    ArrayList<Mesa> listMesas;
    ArrayList<Evento> listEvento;
    View vista;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        obtenerDatos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_blank, container, false);
        mesasNumero = (TextView) vista.findViewById(R.id.MesaNum);
        tituloEvento = (TextView) vista.findViewById(R.id.TituloEvento);
        comentarioEvento = (TextView) vista.findViewById(R.id.DesarolloEvento);
        textViewPagar = (TextView) vista.findViewById(R.id.TVPagar);
        imageViewPagar = (ImageView) vista.findViewById(R.id.imgPagar);
        imageViewEvento = (ImageView) vista.findViewById(R.id.imgEvento);
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

    private void llenarEventos(Evento e) {
        tituloEvento.setText(e.getNombre());
        descargarFoto(e.getImagen(), imageViewEvento);
        comentarioEvento.setText(e.getComentario());
    }

    private void llenarMesas(ArrayList<Mesa> listaMesasObtenidos) {
        List<String> listaMesas = new ArrayList<>();
        for (Mesa m : listaMesasObtenidos) {

        }
    }

    private void descargarFoto(String url, ImageView imageView) {
        Glide.with(getContext())
                .load(MainActivity.baseURL + url)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void obtenerDatos() {
        DominoApiService API_SERVICE = api();
        int ev = preferencesClass.datos.getInt("Evento_id", 1);
        Call<Evento> Evento = API_SERVICE.obtenerEvento(ev);
        try {
            Evento.enqueue(new BlankFragment.EventosCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
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
        API_SERVICE = api();
        Call<MesaRespuesta> Mesas = API_SERVICE.obtenerMesas(evento_id);
        try {
            Mesas.enqueue(new BlankFragment.MesasCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        Obj_preferences.putInt("Evento_id", evento_id);
        Obj_preferences.apply();
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

    private class EventosCallBack implements Callback<Evento> {

        @Override
        public void onResponse(Call<Evento> call, Response<Evento> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_LONG).show();
                Evento eventos = response.body();
                if (eventos != null) {
                    llenarEventos(eventos);
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Evento> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class MesasCallBack implements Callback<MesaRespuesta> {

        @Override
        public void onResponse(Call<MesaRespuesta> call, Response<MesaRespuesta> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_LONG).show();
                MesaRespuesta mesas = response.body();
                if (mesas != null) {
                    listMesas = mesas.getMesas();
                    llenarMesas(listMesas);
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<MesaRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
