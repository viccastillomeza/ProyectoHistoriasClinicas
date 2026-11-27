package Modelo;

import java.io.Serializable;

public class Especialidad implements Serializable {

    private String cod;
    private String nom;
    private String consultorio;
    private double costo;

    public Especialidad() {
    }

    // Método para agregar una fila al JTable (igual que en Proyecto04)
    public Object[] Registro(int num) {
        Object[] fila = {num, cod, nom, consultorio, costo};
        return fila;
    }

    // Getters y Setters
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}//fin clase