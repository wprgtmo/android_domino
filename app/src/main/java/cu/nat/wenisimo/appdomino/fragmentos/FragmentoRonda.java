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
import cu.nat.wenisimo.appdomino.models.Ronda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoRonda extends Fragment {
    ImageView iv1, iv2, iv3, iv4;
    TextView txtRonda, txtP1Jugador1, txtP1Jugador2, txtP2Jugador1, txtP2Jugador2;
    Integer rondas = 1;
    Integer mesa_id = 0;
    Button actualizar;
    Preference preferencesClass;
    Integer iP = 0;
    String parejaS1;
    String parejaS2;
    View vista;
    Integer boleta_pareja_id;

    private OnFragmentInteractionListener mListener;

    public FragmentoRonda() {
        // Required empty public constructor
    }

    public static FragmentoRonda newInstance(String param1, String param2) {
        FragmentoRonda fragment = new FragmentoRonda();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ronda, container, false);
        iv1 = (ImageView) vista.findViewById(R.id.ivP1Par1);
        iv2 = (ImageView) vista.findViewById(R.id.ivP1Par2);
        iv3 = (ImageView) vista.findViewById(R.id.ivP2Par1);
        iv4 = (ImageView) vista.findViewById(R.id.ivP2Par2);
        txtRonda = (TextView) vista.findViewById(R.id.txtRonda);
        txtP1Jugador1 = (TextView) vista.findViewById(R.id.txtP1Par1);
        txtP1Jugador2 = (TextView) vista.findViewById(R.id.txtP1Par2);
        txtP2Jugador1 = (TextView) vista.findViewById(R.id.txtP2Par1);
        txtP2Jugador2 = (TextView) vista.findViewById(R.id.txtP2Par2);
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
//        actualizar = (Button)vista.findViewById(R.id.actualizar);
//        actualizar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                obtenerParejasMesa(preferencesClass.datos.getInt("Mesa_id",0));
//            }
//        });
        obtenerRondaActiva();
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
                obtenerSalidor("Pareja1", parejaS2);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSalidor("Pareja1", parejaS2);
            }
        });
        // Inflate the layout for this fragment
        return vista;
    }

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

    public void obtenerRondaActiva() {
        int evento_id = preferencesClass.datos.getInt("Evento_id", 0);
        Call<Ronda> rondaCall = MainActivity.api().obtenerRondaActiva(evento_id);
        try {
            rondaCall.enqueue(new FragmentoRonda.RondaCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarRonda(Ronda rondaObtenida) {
        txtRonda.setText(txtRonda.getText().toString() + ": " + rondaObtenida.getNumero());
        SharedPreferences.Editor Obj_preferences = preferencesClass.datos.edit();
        Obj_preferences.putInt("Ronda", Integer.parseInt(rondaObtenida.getNumero()));
        Obj_preferences.apply();
    }

    public void obtenerBoleta(Integer mesa_id) {
//        actualizar.setVisibility(View.GONE);
        int evento_id = preferencesClass.datos.getInt("Evento_id", 0);
//        DominoApiService API_SERVICE = api();
        Call<BoletaRespuesta> parejasMesa = MainActivity.api().obtenerBoleta(evento_id, mesa_id);
        try {
            parejasMesa.enqueue(new FragmentoRonda.BoletaCallBack());
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
            txtP1Jugador1.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador1().getNombre());
            txtP1Jugador2.setText(boleta.getBoleta_parejas().get(0).getPareja().getJugador2().getNombre());
            iP++;
        } else if (iP == 1) {
            Obj_preferences.putString("Pareja2", boleta.getBoleta_parejas().get(1).getPareja().getNombre());
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class RondaCallBack implements Callback<Ronda> {

        @Override
        public void onResponse(Call<Ronda> call, Response<Ronda> response) {
            if (response.isSuccessful()) {
//                Toast.makeText(getContext(),"Conexion exitosa",Toast.LENGTH_LONG).show();
//                actualizar.setVisibility(View.GONE);
                Ronda ronda = response.body();
                if (ronda != null) {
                    llenarRonda(ronda);
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
//                actualizar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<Ronda> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            actualizar.setVisibility(View.VISIBLE);
        }
    }

    private class BoletaCallBack implements Callback<BoletaRespuesta> {

        @Override
        public void onResponse(Call<BoletaRespuesta> call, Response<BoletaRespuesta> response) {
            if (response.isSuccessful()) {
//                Toast.makeText(getContext(),"Conexion exitosa",Toast.LENGTH_LONG).show();
//                actualizar.setVisibility(View.GONE);
                BoletaRespuesta boleta = response.body();
                if (boleta != null) {
                    llenarBoleta(boleta.getBoleta());
                    llenarBoleta(boleta.getBoleta());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
//                actualizar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<BoletaRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            actualizar.setVisibility(View.VISIBLE);
        }
    }
}
