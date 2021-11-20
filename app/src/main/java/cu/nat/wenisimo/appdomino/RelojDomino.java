package cu.nat.wenisimo.appdomino;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.util.ArrayList;

public class RelojDomino extends AppCompatActivity {

    Chronometer pareja1Cronometro, pareja2Cronometro;
    String mesasS, rondasS, pareja1, pareja2, dataS, parejaGanadora, pareja1BotonS, pareja2BotonS;
    Integer mesas, rondas, data = 0;
    ArrayList<Integer> datas = new ArrayList<>();
    ArrayList<String> parejasGsnadoras = new ArrayList<>();
    Boolean noEstanAndando1, noEstanAndando2;
    long tiempoPareja1 = SystemClock.elapsedRealtime();
    long tiempoPareja2 = SystemClock.elapsedRealtime();
    long pausaPareja1 = 0, pausaPareja2 = 0;
    Button pareja1Boton, pareja2Boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reloj_domino);

        pareja1Cronometro = (Chronometer) findViewById(R.id.pareja1CH);
        pareja2Cronometro = (Chronometer) findViewById(R.id.pareja2CH);
        pareja1Boton = (Button) findViewById(R.id.pareja1B);
        pareja2Boton = (Button) findViewById(R.id.pareja2B);
        pareja1BotonS = pareja1Boton.getText().toString();
        pareja2BotonS = pareja2Boton.getText().toString();
        mesasS = getIntent().getStringExtra("mesas");
        rondasS = getIntent().getStringExtra("rondas");
        pareja1 = getIntent().getStringExtra("pareja1");
        pareja2 = getIntent().getStringExtra("pareja2");
        dataS = getIntent().getStringExtra("data");
        parejaGanadora = getIntent().getStringExtra("parejaGanadora");
        mesas = Integer.parseInt(mesasS);
        data = Integer.parseInt(dataS);
        rondas = Integer.parseInt(rondasS);
        pareja1Cronometro.setFormat(pareja1BotonS + " %s");
        pareja2Cronometro.setFormat(pareja2BotonS + " %s");
        noEstanAndando1 = true;
        noEstanAndando2 = true;
        pareja1Cronometro.setBase(tiempoPareja1);
        pareja2Cronometro.setBase(tiempoPareja2);
        if (data != 0) {
            datas.add(data);
        }
        if (!parejaGanadora.equals("null")) {
            parejasGsnadoras.add(parejaGanadora);
        }
        pareja1Cronometro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (noEstanAndando1) {
                    pareja1Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja1);
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    pareja1Cronometro.start();
                    noEstanAndando1 = false;
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                } else {
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
                    pareja2Cronometro.setBase(SystemClock.elapsedRealtime() - pausaPareja2);
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    pareja2Cronometro.start();
                    noEstanAndando2 = false;
                    pareja1Cronometro.stop();
                    pausaPareja1 = SystemClock.elapsedRealtime() - pareja1Cronometro.getBase();
                    noEstanAndando1 = true;
                } else {
                    pareja2Cronometro.stop();
                    pausaPareja2 = SystemClock.elapsedRealtime() - pareja2Cronometro.getBase();
                    noEstanAndando2 = true;
                }
            }
        });

    }

    public void addData(View view) {
        Intent i = new Intent(this, AddData.class);
        i.putExtra("mesas", mesasS);
        i.putExtra("rondas", rondasS);
        i.putExtra("pareja1", pareja1);
        i.putExtra("pareja2", pareja2);
        startActivity(i);
        finish();
    }

    public void pause(View view) {
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

    public void resetear(View view) {

    }
}
