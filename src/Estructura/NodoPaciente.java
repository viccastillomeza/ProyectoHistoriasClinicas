package Estructura;

import Modelo.Paciente;
import java.io.Serializable;

public class NodoPaciente implements Serializable {

    private Paciente elemento;
    private NodoPaciente Izq;
    private NodoPaciente Der;

    public NodoPaciente(Paciente elemento) {
        this.elemento = elemento;
        Izq = Der = null;
    }

    // Getters y Setters
    public Paciente getElemento() { return elemento; }
    public void setElemento(Paciente elemento) { this.elemento = elemento; }
    public NodoPaciente getIzq() { return Izq; }
    public void setIzq(NodoPaciente Izq) { this.Izq = Izq; }
    public NodoPaciente getDer() { return Der; }
    public void setDer(NodoPaciente Der) { this.Der = Der; }
}//fin de la clase