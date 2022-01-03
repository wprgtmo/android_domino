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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Boleta;
import cu.nat.wenisimo.appdomino.models.BoletaPareja;
import cu.nat.wenisimo.appdomino.models.BoletaRespuesta;
import cu.nat.wenisimo.appdomino.models.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoSalidor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoSalidor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoSalidor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView iv1, iv2, iv3, iv4;
    TextView txtP1Jugador1, txtP1Jugador2, txtP2Jugador1, txtP2Jugador2;
    Integer mesa_id = 0;
    Preference preferencesClass;
    Integer iP = 0;
    String parejaS1;
    String parejaS2;
    View vista;
    Integer boleta_pareja_id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentoSalidor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoSalidor.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoSalidor newInstance(String param1, String param2) {
        FragmentoSalidor fragment = new FragmentoSalidor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragmento_salidor, container, false);
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
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSalidor("Pareja1", parejaS1);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSalidor("Pareja1", parejaS1);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSalidor("Pareja2", parejaS2);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSalidor("Pareja2", parejaS2);
            }
        });
        // Inflate the layout for this fragment
        return vista;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    public void obtenerSalidor(String parejaSalidora, String pareja) {
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        Obj_preferences.putString("ParejaSalidora", parejaSalidora);
        Obj_preferences.apply();
        Toast.makeText(getContext(), "Escojiste a la pareja " + pareja + " como pareja salidora", Toast.LENGTH_LONG).show();
        Fragment fragmentS;
        Bundle datosFragment;
        fragmentS = new FragmentoRelojDomino();
        datosFragment = new Bundle();
        datosFragment.putInt("data", 0);
        datosFragment.putString("parejaGanadora", "null");
        fragmentS.setArguments(datosFragment);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmento, fragmentS);
        fragmentTransaction.commit();
        Call<BoletaPareja> boletaCall = MainActivity.api().salidor(boleta_pareja_id);
    }

    public void obtenerBoleta(Integer mesa_id) {
        int evento_id = preferencesClass.datos.getInt("Evento_id", 0);
        Call<BoletaRespuesta> parejasMesa = MainActivity.api().obtenerBoleta(evento_id, mesa_id);
        try {
            parejasMesa.enqueue(new FragmentoSalidor.BoletaCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarBoleta(Boleta boleta) {
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        if (iP == 0) {
            Obj_preferences.putString("Pareja1", boleta.getBoleta_parejas().get(0).getPareja().getNombre());
            descargarFoto(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getFoto(), iv1);
            descargarFoto(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getFoto(), iv2);
            txtP1Jugador1.setText("Pareja1 ".concat(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getAlias()));

            txtP1Jugador2.setText("Pareja1 ".concat(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getAlias()));
            iP++;
        } else if (iP == 1) {
            Obj_preferences.putString("Pareja2", boleta.getBoleta_parejas().get(1).getPareja().getNombre());
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getFoto(), iv3);
            descargarFoto(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getFoto(), iv4);
            txtP2Jugador1.setText("Pareja2 ".concat(boleta.getBoleta_parejas().get(1).getPareja().getJugador1().getAlias()));
            txtP2Jugador2.setText("Pareja2 ".concat(boleta.getBoleta_parejas().get(1).getPareja().getJugador2().getAlias()));
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
