package com.example.alejandrotorresruiz.taller.Entities;

/**
 * Created by alejandrotorresruiz on 13/01/2019.
 */

public class Vehiculos {

    String matricula;
    String marca;
    String modelo;
    String anno;
    String color;
    String foto;
    String id_cliente;
    String id_vehiculo_tipo;

    public Vehiculos(){

    }

    public Vehiculos(String matricula, String marca, String modelo,
                     String anno, String color, String foto,
                     String id_cliente, String id_vehiculo_tipo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anno = anno;
        this.color = color;
        this.foto = foto;
        this.id_cliente = id_cliente;
        this.id_vehiculo_tipo = id_vehiculo_tipo;
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
        return anno;
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

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getId_vehiculo_tipo() {
        return id_vehiculo_tipo;
    }

    public void setId_vehiculo_tipo(String id_vehiculo_tipo) {
        this.id_vehiculo_tipo = id_vehiculo_tipo;
    }
}
