package com.mycompany.boletosaviones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager {
     private String driver = "org.postgresql.Driver";
    private Connection c;
    private String url = "jdbc:postgresql://localhost:5432/vuelosBoletos";
    private String username = "postgres";
    private String password = "123456";
    
    private UsuariosDatosBD Datos = new UsuariosDatosBD();

    public UsuariosDatosBD getDatos() {
        return Datos;
    }

    public void setDatos(UsuariosDatosBD Datos) {
        this.Datos = Datos;
    }
    
    
    
    public DBManager() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public List<Vuelo> getVuelos(){
        List<Vuelo> vuelos = new ArrayList<>();
        String query = "SELECT * FROM vuelos ORDER BY id";
        
        try{
            c = DriverManager.getConnection(url, username, password);
            try(PreparedStatement p = c.prepareStatement(query)){
                ResultSet r = p.executeQuery();
                while(r.next()){
                    Vuelo vuelo = new Vuelo();
                    vuelo.setId(r.getInt("id"));
                    vuelo.setDestino(r.getString("destino"));
                    vuelo.setFechaSalida(r.getTimestamp("fecha_salida"));
                    vuelo.setVueloRedondo(r.getBoolean("vuelo_redondo"));
                    vuelo.setFechaRegreso(r.getTimestamp("fecha_regreso"));
                    vuelos.add(vuelo);
                }
                r.close();
            }
            c.close();
        }catch(SQLException ex){
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vuelos;
    }
    
    public int saveVuelo(Vuelo vuelo){
        int rowsUpdated = 0;
        String query = "INSERT INTO vuelos (destino, fecha_salida, vuelo_redondo, fecha_regreso) VALUES (?, ?, ?, ?)";
        
        try{
            c = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, vuelo.getDestino());
            ps.setTimestamp(2, vuelo.getFechaSalida());
            ps.setBoolean(3, vuelo.isVueloRedondo());
            ps.setTimestamp(4, vuelo.getFechaRegreso());
            rowsUpdated = ps.executeUpdate();
            ps.close();
            c.close();
        }catch(SQLException ex){
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowsUpdated;
    }
    
//    public boolean verifyAuthentication(String username, String password) {
//        String query = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";
//        try {
//            c = DriverManager.getConnection(url, this.username, this.password);
//            try (PreparedStatement p = c.prepareStatement(query)) {
//                p.setString(1, username);
//                p.setString(2, password);
//                ResultSet r = p.executeQuery();
//                if (r.next()) {
//                    return true;
//                }
//                r.close();
//            }
//            c.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }

    public boolean verifyAuthentication (String username, String password){
        String query = "select * from usuarios";
        try {
            c = (Connection) DriverManager.getConnection(url, this.username, this.password);
            //System.out.println("conexion abierta");
            try(PreparedStatement p=c.prepareStatement(query)){
                //System.out.println("pasamos el try PreparedStatement");
                ResultSet r=p.executeQuery();
                //System.out.println("pasamos ResultSet = executeQuery");
//                if(r.next()){
//                    //No se mueve entonces solo revisa el primer registro
//                    //lp que sognifica que los demás resgistros nunca van a poder ingresar
//                    //System.out.println("entramos a if r,next");
//                    if(username.equals(r.getString("nombre")) && password.equals(r.getString("password"))){
//                        
//                        //System.out.println("username y pw son iguales");
//                        
//                        Datos.setRol(r.getString("rol"));
//                        //System.out.println("El rol ingresado para el usuario en DBMan es:"+Datos.getRol());
//                        //terminar de llenar los datos y hacer la lógica de ir a página 
//                        //respecto a rol en Login Controller
//                        //r.close();
//                        //System.out.println("resultSet cerrado dentro del if");
//                        
//                        //c.close();
//                        //System.out.println("cerramos conexion dentro del if");
//                        return true;
//                    }
//                }
                
                while(r.next()){
                    System.out.println(r.getString("nombre")+" y "+r.getString("password"));
                    if(username.equals(r.getString("nombre")) && password.equals(r.getString("password"))){
                        Datos.setRol(r.getString("rol"));
                        
                        return true;
                    }
                }
                
                r.close();
                System.out.println("resultSet cerrado fuera del if");
            }
            c.close();
            System.out.println("coneccion cerrada");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return false;
    }
    
//    public String verifyRole(String username){
//        String query = "select * from usuarios";
//        try {
//            c = (Connection) DriverManager.getConnection(url, this.username, this.password);
//            try(PreparedStatement p=c.prepareStatement(query)){
//                ResultSet r=p.executeQuery();
//                if(r.next()){
//                    if(username.equals(r.getString("nombre"))){
//                        r.getString("rol");
//                        if(r.equals("cliente")){
//                            return "cliente";
//                        }else{
//                            return "admin";
//                        }
//                    }
//                }
//                r.close();
//            }
//            c.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//            
//        }
//        return "";
//    }
}
