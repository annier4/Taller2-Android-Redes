package com.example.estudiante.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Esperando extends AppCompatActivity implements Observer {
    Comunicacion com;
    TextView esperando;
    ArrayList<Jugador> jugadores;
    int numJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esperando);
        //com.getInstance().addObserver(this);

        new Async().execute();
        esperando = (TextView) findViewById(R.id.esperando);
        jugadores = new ArrayList<>();
        mensajeEsperando();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.print("llego algo:"+arg);
        if(arg instanceof AutoId){
            System.out.println("Llego algo de Autoid");
        }
    }

    public void mensajeEsperando(){
        if (jugadores.size()<3){
            numJugadores = 3 - jugadores.size();
            esperando.setText("Esperando que "+numJugadores+" jugadores se conecten");

            Context context = getApplicationContext();
            CharSequence text = ""+ numJugadores;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

        }
    }
}
