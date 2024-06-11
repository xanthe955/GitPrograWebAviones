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

    @Inject
    private UsuariosJpaController userjpac;

    public String attempt() {
        FacesContext fc = FacesContext.getCurrentInstance();

        if (!ud.getUsername().isEmpty() && !ud.getPassword().isEmpty()) {
            try {
                Usuarios usuario = userjpac.findUsuariosByNombreAndPassword(ud.getUsername(), ud.getPassword());
                if (usuario != null) {
                    String verRol = usuario.getRol();
                    if (verRol.equals("cliente")) {
                        return "comienzo-cliente.xhtml";
                    } else if (verRol.equals("administrador")) {
                        return "comienzo-admin.xhtml";
                    }
                } else {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", "Nombre de usuario o contraseña incorrectos."));
                }
            } catch (Exception e) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", "Hubo un problema al intentar autenticar. Por favor, inténtelo de nuevo."));
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
