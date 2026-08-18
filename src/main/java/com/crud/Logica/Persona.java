package com.crud.Logica;

import java.util.ArrayList;
import java.util.List;

public class Persona {
    private String nombre;
    private int id;
    private String direccion;
    private List<Telefono> telefonos;

    public Persona(String nombre, int id, String direccion) {
        this.nombre = nombre;
        this.id = id;
        this.direccion = direccion;
        telefonos = new ArrayList<>();
    }

    public Persona() {
        telefonos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public List<Telefono> getTelefonos() {
        return telefonos;
    }
    public void addTelefono(Telefono telefono) {
        this.telefonos.add(telefono);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefonos=" + telefonos +
                '}';
    }
}
