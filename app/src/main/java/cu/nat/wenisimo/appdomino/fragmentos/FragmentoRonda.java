package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Preference;

public class FragmentoRonda extends Fragment {
    ImageView iv1, iv2, iv3, iv4;
    TextView txtRonda, txtP1Jugador1, txtP1Jugador2, txtP2Jugador1, txtP2Jugador2;
    Integer mesa_id = 0;
    Preference preferencesClass;
    Integer iP = 0;
    String parejaS1;
    String parejaS2;
    View vista;
    Integer boleta_pareja_id;
    Button guardar;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context
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
        guardar = vista.findViewById(R.id.guardar);
        guardar.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmento, new FragmentoSalidor());
            fragmentTransaction.commit();
        });
        return vista;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
