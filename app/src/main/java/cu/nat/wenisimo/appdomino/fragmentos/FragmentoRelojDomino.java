package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.ArrayList;

import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Preference;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoRelojDomino.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoRelojDomino#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoRelojDomino extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    View vista;
    Chronometer pareja1Cronometro, pareja2Cronometro, tiempoRondaCronometro;
    String pareja1, pareja2, parejaGanadora = "null";
    Integer mesas, rondas, data = 0;
    ArrayList<Integer> datas = new ArrayList<>();
    ArrayList<String> parejasGsnadoras = new ArrayList<>();
    Boolean noEstanAndando1, noEstanAndando2;
    long tiempoPareja1 = SystemClock.elapsedRealtime();
    long tiempoPareja2 = SystemClock.elapsedRealtime();
    long tiempoRonda = SystemClock.elapsedRealtime();
    long pausaPareja1 = 0, pausaPareja2 = 0, pausaRonda = 0;
    Button pausar, resetear, bData;
    Preference preferencesClass;
    private OnFragmentInteractionListener mListener;

    public FragmentoRelojDomino() {
        // Required empty public constructor
    }

    public static FragmentoRelojDomino newInstance(String param1, String param2) {
        FragmentoRelojDomino fragment = new FragmentoRelojDomino();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        vista = inflater.inflate(R.layout.fragmento_reloj_domino, container, false);

        pareja1Cronometro = (Chronometer) vista.findViewById(R.id.pareja1CH);
        pareja2Cronometro = (Chronometer) vista.findViewById(R.id.pareja2CH);
        tiempoRondaCronometro = (Chronometer) vista.findViewById(R.id.Tiempo);
        pausar = (Button) vista.findViewById(R.id.pauseB);
        resetear = (Button) vista.findViewById(R.id.resetearB);
        bData = (Button) vista.findViewById(R.id.addDataB);
        mesas = preferencesClass.datos.getInt(MainActivity.MESA_ID, 0);
        rondas = preferencesClass.datos.getInt("Ronda", 0);
        pareja1 = preferencesClass.datos.getString("Pareja1", "");
        pareja2 = preferencesClass.datos.getString("Pareja2", "");
        data = getArguments().getInt("data");
        parejaGanadora = getArguments().getString("parejaGanadora");
        pareja1Cronometro.setFormat(" %s");
        pareja2Cronometro.setFormat(" %s");
        tiempoRondaCronometro.setFormat(" %s");
        noEstanAndando1 = true;
        noEstanAndando2 = true;
        pareja1Cronometro.setBase(tiempoPareja1);
        pareja2Cronometro.setBase(tiempoPareja2);
        tiempoRondaCronometro.setBase(tiempoRonda);
        tiempoRondaCronometro.start();
        tiempoRondaCronometro.setBase(SystemClock.elapsedRealtime() - pausaRonda);
        if (preferencesClass.datos.getString("ParejaSalidora", "").equals("Pareja1")) {
            pareja1Cronometro.start();
            pareja1Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja1);
            noEstanAndando1 = false;
            pareja2Cronometro.stop();
            pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
            noEstanAndando2 = true;
        } else if (preferencesClass.datos.getString("ParejaSalidora", "").equals("Pareja2")) {
            pareja2Cronometro.start();
            pareja2Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja2);
            noEstanAndando2 = false;
            pareja1Cronometro.stop();
            pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
            noEstanAndando1 = true;
        }
        if (data != 0) {
            datas.add(data);
        }
        if (!parejaGanadora.equals("null")) {
            parejasGsnadoras.add(parejaGanadora);
        }
        pausar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!noEstanAndando2) {
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                } else if (!noEstanAndando1) {
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    noEstanAndando1 = true;
                }
            }
        });

        resetear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tiempo de juego" + tiempoRondaCronometro.getText(), Toast.LENGTH_LONG).show();
            }
        });

        bData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!noEstanAndando2) {
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                } else if (!noEstanAndando1) {
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    noEstanAndando1 = true;
                }
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmento, new FragmentoAddData());
                fragmentTransaction.commit();
            }
        });
        pareja1Cronometro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (noEstanAndando1) {
                    pareja1Cronometro.start();
                    pareja1Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja1);
                    noEstanAndando1 = false;
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                } else {
                    pareja2Cronometro.start();
                    pareja2Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja2);
                    noEstanAndando2 = false;
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    noEstanAndando1 = true;
                }
            }
        });

        pareja2Cronometro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (noEstanAndando2) {
                    pareja2Cronometro.start();
                    pareja2Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja2);
                    noEstanAndando2 = false;
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    noEstanAndando1 = true;
                } else {
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                    pareja1Cronometro.start();
                    pareja1Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja1);
                    noEstanAndando1 = false;
                }
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
}
