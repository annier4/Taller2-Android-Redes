package com.example.estudiante.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private Comunicacion com;
    Button enemigo;
    Button comida;
    Spinner usuarios;
    ArrayList<Jugador> jugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enemigo = (Button) findViewById(R.id.enemigo);
        comida = (Button) findViewById(R.id.comida);
        usuarios = (Spinner) findViewById(R.id.usuarios);

        enemigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = "add:enemigo";
                com.getInstance().enviarMensaje(mensaje);
            }
        });

        comida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje= "add:comida";
                com.getInstance().enviarMensaje(mensaje);
            }
        });


    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.print("llego algo:"+arg);

    }
}
