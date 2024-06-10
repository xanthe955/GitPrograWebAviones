package com.mycompany.boletosaviones;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class LoginController {
    private UserData ud = new UserData();
    
    //tenemos que tener un intermediario que va a ir a la base de datos por nosotros
    
    @Inject
    private ClientesJpaController clientjpac;
    
    @Inject
    private UsuariosJpaController userjpac;

    public String attempt() {
        FacesContext fc = FacesContext.getCurrentInstance();
        DBManager dbm = new DBManager();

        if (!ud.getUsername().isEmpty() && !ud.getPassword().isEmpty()) {
//            System.out.println(dbm.verifyAuthentication(ud.getUsername(), ud.getPassword()));
            if (dbm.verifyAuthentication(ud.getUsername(), ud.getPassword())) {
                String verRol = dbm.getDatos().getRol();
                //System.out.println("El rol verificado en LoginAttempt es: "+verRol);
                if(verRol.equals("cliente")){
                    return "comienzo-cliente.xhtml";
                }else if(verRol.equals("administrador")){
                    return "comienzo-admin.xhtml";
                }
                //return "comienzo-cliente.xhtml";
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", "Nombre de usuario o contraseña incorrectos."));
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos vacíos", "El nombre de usuario y la contraseña no deben estar vacíos."));
        }
        return "index.xhtml";
    }

    public UserData getUd() {
        return ud;
    }

    public void setUd(UserData ud) {
        this.ud = ud;
    }
    
    
}