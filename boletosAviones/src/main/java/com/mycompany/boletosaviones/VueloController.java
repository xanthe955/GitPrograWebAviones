package com.mycompany.boletosaviones;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class VueloController {
    private DBManager dbm = new DBManager();
    private List<Vuelo> vuelos = dbm.getVuelos();
    private Vuelo vuelo = new Vuelo();

    public String show() {
        return "showVuelos.xhtml";
    }

    public void saveVuelo() {
        System.out.println("RES: " + dbm.saveVuelo(vuelo));
        vuelos = dbm.getVuelos(); // Actualizar la lista de vuelos después de guardar
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void setVuelos(List<Vuelo> vuelos) {
        this.vuelos = vuelos;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
}
