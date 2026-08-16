package com.crud.Logica;

public class Telefono {
    private int id;
    private String numero;
    private int personaId;

    public Telefono() {

    }
    public Telefono(int id, String numero, int personId) {
        this.id = id;
        this.numero = numero;
        this.personaId = personId;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public int getPersonId() {
        return personaId;
    }
    public void setPersonId(int personId) {
        this.personaId = personId;
    }
}
