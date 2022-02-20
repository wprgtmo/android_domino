package cu.nat.wenisimo.appdomino.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cu.nat.wenisimo.appdomino.R;
import cu.nat.wenisimo.appdomino.models.Data;
import cu.nat.wenisimo.appdomino.models.DataBoleta;


public class BoletaAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Data> datas;
    ArrayList<DataBoleta> dataBoletas;

    public BoletaAdapter(Context c, ArrayList<Data> data) {
        super(c, R.layout.row);
        this.context = c;
        this.datas.addAll(data);
        llenarDataBoleta(data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView myDatas = (TextView) row.findViewById(R.id.txtDatas);
        TextView myPuntos1 = (TextView) row.findViewById(R.id.txtPuntos1);
        TextView myTantos1 = (TextView) row.findViewById(R.id.txtTantos1);
        TextView myPuntos2 = (TextView) row.findViewById(R.id.txtPuntos2);
        TextView myTantos2 = (TextView) row.findViewById(R.id.txtTantos2);

//        if(position == 0){
//            myDatas.setPadding(5,5,5,5);
//            myPuntos1.setPadding(5,5,0,5);
//            myTantos1.setPadding(0,5,5,5);
//            myPuntos2.setPadding(5,5,0,5);
//            myTantos2.setPadding(0,5,5,5);
//            myPuntos1.setGravity(Gravity.RIGHT);
//            myTantos1.setGravity(Gravity.LEFT);
//            myPuntos2.setGravity(Gravity.RIGHT);
//            myTantos2.setGravity(Gravity.LEFT);
//            myPuntos1.setBackgroundResource(R.color.white1);
//            myPuntos2.setBackgroundResource(R.color.white1);
//        }
        myDatas.setText(this.dataBoletas.get(position).getDatas());
        myPuntos1.setText(this.dataBoletas.get(position).getPuntos1());
        myTantos1.setText(this.dataBoletas.get(position).getTantosAcumulados1());
        myPuntos2.setText(this.dataBoletas.get(position).getPuntos2());
        myTantos2.setText(this.dataBoletas.get(position).getTantosAcumulados2());
        return row;
    }

    private void llenarDataBoleta(ArrayList<Data> listaObtenidos) {
        Integer pareja1 = listaObtenidos.get(0).getPareja_ganadora();
        Integer Acumulado1 = 0, Acumulado2 = 0;
        DataBoleta dataBoleta;
        for (int i = 0; i <= listaObtenidos.size(); i++) {
//            if (i == 0) {
//                rDatas.add("Datas");
//                rPuntos1.add("Par");
//                rTantos1.add("eja1");
//                rTantos2.add("Par");
//                rPuntos2.add("eja2");
//            }
            dataBoleta = new DataBoleta();
            dataBoleta.setDatas("Datas " + listaObtenidos.get(i).getNumero());
            if (listaObtenidos.get(i).getPareja_ganadora().equals(pareja1)) {
                dataBoleta.setPuntos1(listaObtenidos.get(i).getPuntos());
                dataBoleta.setPuntos2(0);
                Acumulado1 = +listaObtenidos.get(i).getPuntos();
            } else {
                dataBoleta.setPuntos1(0);
                dataBoleta.setPuntos2(listaObtenidos.get(i).getPuntos());
                Acumulado2 = +listaObtenidos.get(i).getPuntos();
            }
            dataBoleta.setTantosAcumulados1(Acumulado1);
            dataBoleta.setTantosAcumulados2(Acumulado2);
        }
    }
}
