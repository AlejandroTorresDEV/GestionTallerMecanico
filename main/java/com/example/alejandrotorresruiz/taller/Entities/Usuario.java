package com.example.alejandrotorresruiz.taller.Entities;

/**
 * Created by alejandrotorresruiz on 23/12/2018.
 */

public class Usuario {

    private String usuario;
    private String clave;
    private String foto;
    private String token;
    private String fecha_inicio;
    private String fecha_fin;

    public Usuario(){

    }

    public Usuario(String usuario, String clave, String foto, String token, String fecha_inicio, String fecha_fin) {
        this.usuario = usuario;
        this.clave = clave;
        this.foto = foto;
        this.token = token;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", foto='" + foto + '\'' +
                ", token='" + token + '\'' +
                ", fecha_inicio='" + fecha_inicio + '\'' +
                ", fecha_fin='" + fecha_fin + '\'' +
                '}';
    }
}

