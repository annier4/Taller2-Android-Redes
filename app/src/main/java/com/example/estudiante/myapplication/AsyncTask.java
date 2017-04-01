package com.example.estudiante.myapplication;

import android.os.AsyncTask;


class Async extends AsyncTask<Void, Integer, String> {



    @Override
    protected String doInBackground(Void... params) { //nuevo
        Comunicacion com = new Comunicacion();
        Thread t = new Thread(com);
        return null;
    }
}
