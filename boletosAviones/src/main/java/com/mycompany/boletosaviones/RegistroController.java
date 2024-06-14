/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.boletosaviones;

/**
 *
 * @author darkx
 */
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegistroController {

    private UserData ud = new UserData();

    @Inject
    private UsuariosJpaController userjpac;

    public String insertUser() {
        FacesContext fc = FacesContext.getCurrentInstance();

        if (!ud.getUsername().isEmpty() && !ud.getPassword().isEmpty()) {
            try {
                userjpac.registerUser(ud.getUsername(), ud.getEmail(), ud.getPassword(), ud.getRol());
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", "Usuario registrado correctamente."));
                return "index.xhtml";
            } catch (Exception e) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro", "Hubo un problema al intentar registrar. Por favor, inténtelo de nuevo."));
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos vacíos", "El nombre de usuario y la contraseña no deben estar vacíos en el registro de usuario."));
        }

        return null; // Return null to stay on the same page in case of error
    }

    public UserData getUd() {
        return ud;
    }

    public void setUd(UserData ud) {
        this.ud = ud;
    }

}
