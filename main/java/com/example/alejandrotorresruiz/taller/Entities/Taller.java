package com.example.alejandrotorresruiz.taller.Entities;

/**
 * Created by alejandrotorresruiz on 26/01/2019.
 */

public class Taller {

    private String nombre;
    private String direccion;
    private int telefono;
    private String foto;
    private double longitud;
    private double latitud;

    public Taller(){

    }

    public Taller(String nombre, String direccion, int telefono, String foto, double longitud, double latitud) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.foto = foto;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    @Override
    public String toString() {
        return "Taller{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", foto='" + foto + '\'' +
                ", longitud=" + longitud +
                ", latitud=" + latitud +
                '}';
    }
}
