package cu.nat.wenisimo.appdomino.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.nat.wenisimo.appdomino.Adapters.BoletaAdapter;
import cu.nat.wenisimo.appdomino.MainActivity;
import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Data;
import cu.nat.wenisimo.appdomino.models.DatasRespuesta;
import cu.nat.wenisimo.appdomino.models.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoRondaFinal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoRondaFinal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoRondaFinal extends Fragment {
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View vista;
    ListView listView;
    ArrayList<Data> DDatas;
    String[] Numero;
    ArrayList<String> Datas = new ArrayList<>();
    ArrayList<String> Puntos1 = new ArrayList<>();
    ArrayList<String> Tantos1 = new ArrayList<>();
    ArrayList<String> Tantos2 = new ArrayList<>();
    ArrayList<String> Puntos2 = new ArrayList<>();
    Integer boleta_id;
    Preference preferencesClass;
    String ContadorTantos1;
    String ContadorTantos2;

    private OnFragmentInteractionListener mListener;

    public FragmentoRondaFinal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RondaFinal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoRondaFinal newInstance(String param1, String param2) {
        FragmentoRondaFinal fragment = new FragmentoRondaFinal();
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
            // Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        crearDataBoleta();
        preferencesClass = new Preference();
        preferencesClass.datos = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ronda_final, container, false);
        boleta_id = preferencesClass.datos.getInt("Boleta_id", 0);
        listView = vista.findViewById(R.id.ListView);
        BoletaAdapter adapter = new BoletaAdapter(this.getContext(), DDatas);
        listView.setAdapter(adapter);
        return vista;
    }


    public void crearDataBoleta() {
        Call<DatasRespuesta> data = MainActivity.api().obtenerDatasBoleta(boleta_id);
        try {
            data.enqueue(new FragmentoRondaFinal.CrearDataBoletaCallBack());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarDataBoleta(ArrayList<Data> listaObtenidos) {
        for (int i = 0; i <= listaObtenidos.size(); i++) {
            DDatas.add(listaObtenidos.get(i));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public class CrearDataBoletaCallBack implements Callback<DatasRespuesta> {
        @Override
        public void onResponse(Call<DatasRespuesta> call, Response<DatasRespuesta> response) {
            if (response.isSuccessful()) {
                DatasRespuesta data = response.body();
                if (data != null) {
                    llenarDataBoleta(data.getData());
                }
            } else {
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<DatasRespuesta> call, Throwable throwable) {
            Toast.makeText(getContext(), "Falló: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
