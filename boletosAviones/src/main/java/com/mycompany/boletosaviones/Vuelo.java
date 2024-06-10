package com.mycompany.boletosaviones;

import java.sql.Timestamp;

public class Vuelo {
    private int id;
    private String destino;
    private Timestamp fechaSalida;
    private boolean vueloRedondo;
    private Timestamp fechaRegreso;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Timestamp getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Timestamp fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public boolean isVueloRedondo() {
        return vueloRedondo;
    }

    public void setVueloRedondo(boolean vueloRedondo) {
        this.vueloRedondo = vueloRedondo;
    }

    public Timestamp getFechaRegreso() {
        return fechaRegreso;
    }

    public void setFechaRegreso(Timestamp fechaRegreso) {
        this.fechaRegreso = fechaRegreso;
    }
}
