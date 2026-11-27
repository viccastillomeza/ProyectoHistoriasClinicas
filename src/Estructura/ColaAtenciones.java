package Estructura;

import Modelo.HistoriaClinica;
import java.io.Serializable;
import java.util.PriorityQueue;

public class ColaAtenciones implements Serializable {

    private PriorityQueue<HistoriaClinica> cola;

    public ColaAtenciones() {
        // Inicializa la cola usando el comparador de prioridad de triaje
        cola = new PriorityQueue<>(new ComparadorAtenciones());
    }

    // Agrega una atención a la cola según su prioridad (OFFER / ADD)
    public void Encolar(HistoriaClinica hc) {
        cola.offer(hc);
    }

    // Retira y devuelve la atención con mayor prioridad al frente de la cola (POLL)
    public HistoriaClinica Desencolar() {
        return cola.poll();
    }

    // Consulta la atención con mayor prioridad sin retirarla (PEEK)
    public HistoriaClinica VerFrente() {
        return cola.peek();
    }

    public int Cantidad() {
        return cola.size();
    }

    public boolean EstaVacia() {
        return cola.isEmpty();
    }

    public PriorityQueue<HistoriaClinica> getCola() {
        return cola;
    }
}//fin de la clase