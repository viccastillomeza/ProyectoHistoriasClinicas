package Estructura;

import Modelo.HistoriaClinica;
import java.io.Serializable;
import java.util.Comparator;

public class ComparadorAtenciones implements Comparator<HistoriaClinica>, Serializable {

    @Override
    public int compare(HistoriaClinica h1, HistoriaClinica h2) {
        // Compara por el texto de prioridad: "1 - EMERGENCIA" < "2 - URGENCIA" < "3 - CONSULTA EXTERNA"
        return h1.getPrioridad().compareToIgnoreCase(h2.getPrioridad());
    }
}//fin de la clase