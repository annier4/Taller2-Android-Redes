package com.example.estudiante.myapplication;

import java.io.Serializable;

/**
 * Created by estudiante on 29/03/17.
 */

public class Jugador implements Serializable {
    private int id, r, g, b;

    public Jugador(int id, int r, int g, int b) {
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Jugador(int id) {
        this.id = id;
    }

    public Jugador() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
