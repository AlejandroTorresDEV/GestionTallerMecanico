package com.example.alejandrotorresruiz.taller.Entities;

/**
 * Created by alejandrotorresruiz on 23/12/2018.
 */

public class Citas {

    private String fecha;
    private String hora;
    private String km;
    private String id_vehiculo;
    private String id_taller;
    private String matricula;
    private String marca;
    private String modelo;
    private String anno;
    private String color;
    private String foto;
    private int id_cliente;
    private String id_vehiculo_tip;
    private String nombre;
    private String direccion;
    private int telefono;
    private float latitud;
    private float longitud;
    private String foto_vehiculo;

    public Citas() {
    }

    public Citas(String fecha, String hora, String km, String id_vehiculo, String id_taller, String matricula,
                 String marca, String modelo, String anno, String color, String foto, int id_cliente, String id_vehiculo_tip,
                 String nombre, String direccion, int telefono, float latitud, float longitud, String foto_vehiculo) {

        this.fecha = fecha;
        this.hora = hora;
        this.km = km;
        this.id_vehiculo = id_vehiculo;
        this.id_taller = id_taller;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anno = anno;
        this.color = color;
        this.foto = foto;
        this.id_cliente = id_cliente;
        this.id_vehiculo_tip = id_vehiculo_tip;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto_vehiculo = foto_vehiculo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(String id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getId_taller() {
        return id_taller;
    }

    public void setId_taller(String id_taller) {
        this.id_taller = id_taller;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnno() {
        return this.anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getId_vehiculo_tip() {
        return id_vehiculo_tip;
    }

    public void setId_vehiculo_tip(String id_vehiculo_tip) {
        this.id_vehiculo_tip = id_vehiculo_tip;
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

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getFoto_vehiculo() {
        return foto_vehiculo;
    }

    public void setFoto_vehiculo(String foto_vehiculo) {
        this.foto_vehiculo = foto_vehiculo;
    }

    @Override
    public String toString() {
        return "Citas{" +
                "fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", km='" + km + '\'' +
                ", id_vehiculo='" + id_vehiculo + '\'' +
                ", id_taller='" + id_taller + '\'' +
                ", matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anno=" + anno +
                ", color='" + color + '\'' +
                ", foto='" + foto + '\'' +
                ", id_cliente=" + id_cliente +
                ", id_vehiculo_tip='" + id_vehiculo_tip + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", foto_vehiculo='" + foto_vehiculo + '\'' +
                '}';
    }
}
