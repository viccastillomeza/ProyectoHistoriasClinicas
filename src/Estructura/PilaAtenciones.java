package Estructura;

import Modelo.HistoriaClinica;
import java.io.Serializable;
import java.util.Stack;

public class PilaAtenciones implements Serializable {

    private Stack<HistoriaClinica> pila;

    public PilaAtenciones() {
        pila = new Stack<>();
    }

    // Agrega una atención médica en la cima de la pila (PUSH)
    public void Apilar(HistoriaClinica hc) {
        pila.push(hc);
    }

    // Retira y devuelve la última atención registrada en la cima (POP)
    public HistoriaClinica Desapilar() {
        if (!pila.isEmpty()) {
            return pila.pop();
        }
        return null;
    }

    // Devuelve la última atención sin retirarla de la pila (PEEK)
    public HistoriaClinica VerUltima() {
        if (!pila.isEmpty()) {
            return pila.peek();
        }
        return null;
    }

    // Devuelve la primera atención del día del médico (fondo de la pila)
    public HistoriaClinica VerPrimera() {
        if (!pila.isEmpty()) {
            return pila.firstElement();
        }
        return null;
    }

    // Devuelve la cantidad total de atenciones en la pila
    public int Cantidad() {
        return pila.size();
    }

    // Verifica si la pila está vacía
    public boolean EstaVacia() {
        return pila.isEmpty();
    }

    // Getter de la pila interna para recorrerla al llenar el JTable
    public Stack<HistoriaClinica> getPila() {
        return pila;
    }
}//fin de la clase