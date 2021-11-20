package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Boleta;
import cu.nat.wenisimo.appdomino.models.BoletaRespuesta;
import cu.nat.wenisimo.appdomino.models.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoAddData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoAddData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoAddData extends Fragment implements
        FragmentoConfiguracion.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String pareja1, pareja2, parejaGanadora, dataS;
    EditText data;
    Button botonOK;
    ImageView iv1, iv2, iv3, iv4;
    TextView txtPareja1, txtPareja2, txtP1Jugador1, txtP1Jugador2, txtP2Jugador1, txtP2Jugador2;
    Integer mesa_id = 0;
    Preference preferencesClass;
    Integer iP = 0;
    String parejaS1;
    String parejaS2;
    View vista;
    Integer boleta_pareja_id;
    private OnFragmentInteractionListener mListener;

    public FragmentoAddData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoAddData.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoAddData newInstance(String param1, String param2) {
        FragmentoAddData fragment = new FragmentoAddData();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //}
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragmento_add_data, container, false);
        iv1 = (ImageView) vista.findViewById(R.id.ivP1Par1);
        iv2 = (ImageView) vista.findViewById(R.id.ivP1Par2);
        iv3 = (ImageView) vista.findViewById(R.id.ivP2Par1);
        iv4 = (ImageView) vista.findViewById(R.id.ivP2Par2);
        txtPareja1 = (TextView) vista.findViewById(R.id.txtPareja1);
        txtPareja2 = (TextView) vista.findViewById(R.id.txtPareja2);
        txtP1Jugador1 = (TextView) vista.findViewById(R.id.txtP1Par1);
        txtP1Jugador2 = (TextView) vista.findViewById(R.id.txtP1Par2);
        txtP2Jugador1 = (TextView) vista.findViewById(R.id.txtP2Par1);
        txtP2Jugador2 = (TextView) vista.findViewById(R.id.txtP2Par2);
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        mesa_id = preferencesClass.datos.getInt(MainActivity.MESA_ID, 0);
        obtenerBoleta(mesa_id);
        parejaS1 = preferencesClass.datos.getString("Pareja1", "");
        parejaS2 = preferencesClass.datos.getString("Pareja2", "");
        txtPareja1.setText(txtPareja1.getText().toString() + ": " + parejaS1);
        txtPareja2.setText(txtPareja2.getText().toString() + ": " + parejaS2);
        botonOK = (Button) vista.findViewById(R.id.ok);
        data = (EditText) vista.findViewById(R.id.editText);
        pareja1 = preferencesClass.datos.getString("Pareja1", "");
        pareja2 = preferencesClass.datos.getString("Pareja2", "");
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora("Pareja1");
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora("Pareja1");
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora("Pareja2");
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora("Pareja2");
            }
        });
        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataS = data.getText().toString();
                if (!dataS.equals("")) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmento, new FragmentoRelojDomino());
                    fragmentTransaction.commit();
                }
            }
        });
        return vista;
    }

    public void ParejaGanadora(String parejaGanadora) {
        this.parejaGanadora = parejaGanadora;
    }

    public void obtenerBoleta(Integer mesa_id) {
        int evento_id = preferencesClass.datos.getInt("Evento_id", 0);
        Call<BoletaRespuesta> parejasMesa = MainActivity.api().obtenerBoleta(evento_id, mesa_id);
        try {
            parejasMesa.enqueue(new FragmentoAddData.BoletaCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarBoleta(Boleta boleta) {
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        if (iP == 0) {
            descargarFoto(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getFoto(), iv1);
            descargarFoto(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getFoto(), iv2);
            txtP1Jugador1.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getNombre());
            txtP1Jugador2.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getNombre());
            iP++;
        } else if (iP == 1) {
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getFoto(), iv3);
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getFoto(), iv4);
            txtP2Jugador1.setText(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getNombre());
            txtP2Jugador2.setText(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getNombre());
        }
        boleta_pareja_id = Integer.parseInt(boleta.getId());
        Obj_preferences.apply();
    }

    private void descargarFoto(String url, ImageView imageView) {
        Glide.with(getContext())
                .load(MainActivity.baseURL + url)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private class BoletaCallBack implements Callback<BoletaRespuesta> {

        @Override
        public void onResponse(Call<BoletaRespuesta> call, Response<BoletaRespuesta> response) {
            if (response.isSuccessful()) {
                BoletaRespuesta boleta = response.body();
                if (boleta != null) {
                    llenarBoleta(boleta.getBoleta());
                    llenarBoleta(boleta.getBoleta());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<BoletaRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
