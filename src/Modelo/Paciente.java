package Modelo;

import java.io.Serializable;

public class Paciente implements Serializable {

    private String dni;
    private String apellidos;
    private String nombres;
    private int edad;
    private String sexo;
    private String telefono;

    public Paciente() {
    }

    // Constructor con arreglo de objetos
    public Paciente(Object[] registro) {
        this.dni = registro[0].toString();
        this.apellidos = registro[1].toString();
        this.nombres = registro[2].toString();
        this.edad = Integer.parseInt(registro[3].toString());
        this.sexo = registro[4].toString();
        this.telefono = registro[5].toString();
    }

    // Devuelve la fila lista para el JTable
    public Object[] getRegistro() {
        Object[] fila = {dni, apellidos, nombres, edad, sexo, telefono};
        return fila;
    }

    // Clave de ordenamiento del Árbol Binario
    public String NombreCompleto() {
        return apellidos + " " + nombres;
    }

    // Getters y Setters
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}//fin de la clase