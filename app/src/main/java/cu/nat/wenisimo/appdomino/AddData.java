package cu.nat.wenisimo.appdomino;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddData extends AppCompatActivity {
    String pareja1, pareja2, parejaGanadora, dataS, mesasS, rondasS;
    RadioButton pareja1R, pareja2R;
    EditText data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        pareja1R = findViewById(R.id.Pareja1);
        pareja2R = findViewById(R.id.Pareja2);
        data = findViewById(R.id.editText);
        mesasS = getIntent().getStringExtra("rondas");
        rondasS = getIntent().getStringExtra("mesas");
        pareja1 = getIntent().getStringExtra("pareja1");
        pareja2 = getIntent().getStringExtra("pareja2");
        pareja1R.setText(pareja1);
        pareja2R.setText(pareja2);
    }

    public void oK(View view) {
        if (pareja1R.isChecked()) {
            parejaGanadora = pareja1;
        } else if (pareja2R.isChecked()) {
            parejaGanadora = pareja2;
        }
        dataS = data.getText().toString();
        if (!dataS.equals("")) {
            Intent i = new Intent(this, RelojDomino.class);
            i.putExtra("mesas", mesasS);
            i.putExtra("rondas", rondasS);
            i.putExtra("pareja1", pareja1);
            i.putExtra("pareja2", pareja2);
            i.putExtra("parejaGanadora", parejaGanadora);
            i.putExtra("data", dataS);
            startActivity(i);
            finish();
        }
    }

    public void atras(View view) {
        Intent i = new Intent(this, RelojDomino.class);
        i.putExtra("mesas", mesasS);
        i.putExtra("rondas", rondasS);
        i.putExtra("pareja1", pareja1);
        i.putExtra("pareja2", pareja2);
        i.putExtra("parejaGanadora", "null");
        i.putExtra("data", "0");
        startActivity(i);
        finish();
    }
}
