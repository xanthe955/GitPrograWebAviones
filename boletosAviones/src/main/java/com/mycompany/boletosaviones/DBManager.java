package com.mycompany.boletosaviones;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBManager {
    private EntityManagerFactory emf;
    private UsuariosJpaController usuariosJpaController;

    public DBManager() {
        emf = Persistence.createEntityManagerFactory("Vuelos_y_Boletos_Persistentes");
        usuariosJpaController = new UsuariosJpaController(null, emf);
    }

    public UsuariosDatosBD getUsuariosData(String nom, String pw) {
        Usuarios usuario = usuariosJpaController.findUsuariosByNombreAndPassword(nom, pw);
        if (usuario != null) {
            UsuariosDatosBD datosBD = new UsuariosDatosBD();
            datosBD.setNombre(usuario.getNombre());
            datosBD.setEmail(usuario.getEmail());
            datosBD.setPassword(usuario.getPassword());
            datosBD.setRol(usuario.getRol());
            return datosBD;
        }
        return null;
    }
}
