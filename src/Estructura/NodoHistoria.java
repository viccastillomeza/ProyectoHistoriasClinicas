package Estructura;

import Modelo.HistoriaClinica;
import java.io.Serializable;

public class NodoHistoria implements Serializable {

    private HistoriaClinica dato;
    private NodoHistoria sig; // Puntero al siguiente nodo de la lista

    public NodoHistoria(HistoriaClinica dato) {
        this.dato = dato;
        this.sig = null;
    }

    public HistoriaClinica getDato() { return dato; }
    public void setDato(HistoriaClinica dato) { this.dato = dato; }

    public NodoHistoria getSig() { return sig; }
    public void setSig(NodoHistoria sig) { this.sig = sig; }
}//fin de la clase