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
import cu.nat.wenisimo.appdomino.models.Data;
import cu.nat.wenisimo.appdomino.models.DataRespuesta;
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

    String pareja1, pareja2, puntosS;
    EditText puntosET;
    Button botonOK;
    ImageView iv1, iv2, iv3, iv4;
    TextView txtP1Jugador1, txtP1Jugador2, txtP2Jugador1, txtP2Jugador2;
    Integer mesa_id = 0;
    Preference preferencesClass;
    Integer iP = 0;
    String parejaS1;
    String parejaS2;
    View vista;
    Integer boleta_id, pareja1ID, pareja2ID, paregaGanadoraID = 0;
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
        botonOK = (Button) vista.findViewById(R.id.ok);
        puntosET = (EditText) vista.findViewById(R.id.editText);
        pareja1 = preferencesClass.datos.getString("Pareja1", "");
        pareja2 = preferencesClass.datos.getString("Pareja2", "");
        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puntosS = puntosET.getText().toString();
                if (!puntosS.equals("")) {
                    if (!paregaGanadoraID.equals(0)) {
                        Integer puntos = Integer.parseInt(puntosS);
                        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                        Integer numeroData = preferencesClass.datos.getInt("NumeroData", 0) + 1;
                        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
                        Obj_preferences.putInt("NumeroData", numeroData);
                        if (preferencesClass.datos.getString("ParejaSalidora", "").equals("Pareja1")) {
                            Obj_preferences.putString("ParejaSalidora", "Pareja2");
                        } else if (preferencesClass.datos.getString("ParejaSalidora", "").equals("Pareja2")) {
                            Obj_preferences.putString("ParejaSalidora", "Pareja1");
                        }
                        Obj_preferences.apply();

                        crearData(numeroData, boleta_id, puntos, 0, paregaGanadoraID);

                        Fragment fragmentS;
                        Bundle datosFragment;
                        fragmentS = new FragmentoRelojDomino();
                        datosFragment = new Bundle();
                        datosFragment.putInt("data", puntos);
                        datosFragment.putInt("parejaGanadora", paregaGanadoraID);
                        fragmentS.setArguments(datosFragment);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmento, fragmentS);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getContext(), "Debes escoger el ganador", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora(0, iv1);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora(0, iv2);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora(1, iv3);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParejaGanadora(1, iv4);
            }
        });
        return vista;
    }

    public void ParejaGanadora(Integer parejaGanadora, ImageView iv) {
        iv1.setPadding(0, 0, 0, 0);
        iv2.setPadding(0, 0, 0, 0);
        iv3.setPadding(0, 0, 0, 0);
        iv4.setPadding(0, 0, 0, 0);
        iv.setPadding(10, 10, 10, 10);
        if (parejaGanadora == 0) {
            this.paregaGanadoraID = pareja1ID;
        } else if (parejaGanadora == 1) {
            this.paregaGanadoraID = pareja2ID;
        }
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
            txtP1Jugador1.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getAlias());
            txtP1Jugador2.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getAlias());
            iP++;
        } else if (iP == 1) {
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getFoto(), iv3);
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getFoto(), iv4);
            txtP2Jugador1.setText(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getAlias());
            txtP2Jugador2.setText(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getAlias());
        }
        pareja1ID = Integer.parseInt(boleta.getBoleta_parejas().get(0).getId());
        pareja2ID = Integer.parseInt(boleta.getBoleta_parejas().get(1).getId());
        boleta_id = Integer.parseInt(boleta.getId());
        Obj_preferences.apply();
    }

    public void crearData(Integer numero, Integer boleta_id, Integer puntos, Integer duracion, Integer pareja_ganadora) {
        Call<DataRespuesta> data = MainActivity.api().crearData(numero, boleta_id, puntos, duracion, pareja_ganadora);
        try {
            data.enqueue(new FragmentoAddData.CrearDataCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarData(Data data) {

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

    private class CrearDataCallBack implements Callback<DataRespuesta> {

        @Override
        public void onResponse(Call<DataRespuesta> call, Response<DataRespuesta> response) {
            if (response.isSuccessful()) {
                DataRespuesta data = response.body();
                if (data != null) {
                    llenarData(data.getData());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onFailure(Call<DataRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
