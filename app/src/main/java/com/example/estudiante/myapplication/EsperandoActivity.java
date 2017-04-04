package com.example.estudiante.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import comun.AutoId;

public class EsperandoActivity extends AppCompatActivity implements Observer {
    Comunicacion com;
    TextView esperando;
    ArrayList<Jugador> jugadores;
    int numJugadores;
    EsperandoActivity e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esperando);
        //com.getInstance().addObserver(this);

        new Async().execute();
        esperando = (TextView) findViewById(R.id.esperando);
        jugadores = new ArrayList<>();
        e = this;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.print("llego algo:" + arg);
        if (arg instanceof AutoId) {
            System.out.println("Llego algo de Autoid:" + arg); //verificación
        }
        mensajeEsperando();
    }

    public void mensajeEsperando() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jugadores.add(new Jugador());
                if (jugadores.size() < 3) {
                    numJugadores = 3 - jugadores.size();
                    esperando.setText("Esperando  que " + numJugadores + " jugadores se conecten");

                    Context context = getApplicationContext();
                    CharSequence text = " " + numJugadores;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);

                    i.putExtra("resultado", "hola este es el resultado equisde");
                    startActivity(i);

                }
            }
        });
    }

    public class Async extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) { //nuevo
            com = new Comunicacion();
            com.addObserver(e);

            return null;
        }
    }

}

