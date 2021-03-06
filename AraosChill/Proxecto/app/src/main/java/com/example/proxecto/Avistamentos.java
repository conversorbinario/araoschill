package com.example.proxecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.time.LocalTime;

public class Avistamentos extends AppCompatActivity implements Serializable {

    DatePicker dp;

    EditText concello;
    EditText lugar;
    EditText latitude;
    EditText lonxitue;
    Button seguinte;
    Button velloAvis;
    private long id_avistamento;
    Button abrirGps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avistamentos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lugar = findViewById(R.id.lugar);
        concello = findViewById(R.id.concello);
        dp = findViewById(R.id.dataAvistamento);
        latitude = findViewById(R.id.latitude);
        lonxitue=findViewById(R.id.lonxitude);


        if (savedInstanceState!=null){

            concello.setText(savedInstanceState.getString("Concello"));
            lugar.setText(savedInstanceState.getString("Lugar"));
        }
        abrirGps=findViewById(R.id.gpsMapa);
        abrirGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityMapa = new Intent(getApplicationContext(), Mapa.class);
                startActivityForResult(activityMapa, 2);
            }
        });
        seguinte = findViewById(R.id.continuar);
        seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conce = String.valueOf(concello.getText()).toLowerCase();
                String lug = String.valueOf(lugar.getText()).toLowerCase();
                if (conce.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.concelloBal, Toast.LENGTH_LONG).show();

                } else if (lug.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), R.string.lugarAvistamento, Toast.LENGTH_LONG).show();

                }else{
                    String lat="";
                    String lonx="";
                    if (latitude.getText()!=null && lonxitue.getText()!=null) {

                         lat = String.valueOf(latitude.getText());
                         lonx = String.valueOf(lonxitue.getText());
                         if (!lat.matches("\\-*[0-9]{1,3}(\\.[0-9]*)?") || !lonx.matches("\\-*[0-9]{1,3}(\\.[0-9]*)?")){
                             Toast.makeText(getApplicationContext(), R.string.formatoCoordenadas, Toast.LENGTH_LONG).show();
                             lat="";
                             lonx="";
                         }
                    }else{
                        lat="";
                        lonx="";
                        Toast.makeText(getApplicationContext(), R.string.formatoCoordenadas+" / " +R.string.coordenadasBaleiras, Toast.LENGTH_LONG).show();

                    }

                    int dia = dp.getDayOfMonth();
                    int mes = dp.getMonth();
                    int ano = dp.getYear();
                    String data = dia + "/" + mes + "/" + ano;
                    int hora = LocalTime.now().getHour();
                    int minutos = LocalTime.now().getMinute();

                    int segundos = LocalTime.now().getSecond();
                    String momento = hora + ":" + minutos + ":" + segundos;
                    try {
                       // id_avistamento = MainActivity.bb_dd.addAvistamento(new Avistamento(conce, lug, data, momento));
                        Intent individuo = new Intent(getApplicationContext(), Rexistro.class);
                     //   individuo.putExtra("idAvis", (Serializable) new Avistamento(conce, lug, data, momento));
                        //individuo.putExtra("idAvisFB", (Serializable) new AvistamentoFB(conce, lug, data, momento));
                        individuo.putExtra("idAvisFB", (Serializable) new AvistamentoFB(conce, lug, data, momento, lat, lonx));

                        individuo.putExtra("existente", false);



                        startActivity(individuo);
                    } catch (Exception e) {

                    }

                }
            }
        });

        velloAvis = findViewById(R.id.velloAvistamento);
        velloAvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent in = new Intent(getApplicationContext(), VellosAvistamento.class);
                    startActivity(in);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2){
            String []lonxlat=data.getStringArrayExtra("lonxlat");
            latitude.setText(String.valueOf(lonxlat[0]));
            lonxitue.setText(String.valueOf(lonxlat[1]));
            lugar.setText(String.valueOf(lonxlat[2]));
            concello.setText(String.valueOf(lonxlat[3]));
           // Toast.makeText(getApplicationContext(), lonxlat[2], Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        String conce = String.valueOf(concello.getText());
        String lug = String.valueOf(lugar.getText());
        outState.putString("Concello", conce);
        outState.putString("Lugar", lug);

        //dp.updateDate();
    }


}