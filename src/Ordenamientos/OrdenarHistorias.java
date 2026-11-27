package Ordenamientos;

import Estructura.ListaHistorias;
import Estructura.NodoHistoria;
import Modelo.HistoriaClinica;

public class OrdenarHistorias {

    // Ordenamiento por Selección alfabéticamente por Nombre de Paciente
    public static void OrdenarPorNombrePaciente(ListaHistorias lista) {
        if (lista.getIni() == null) {
            return;
        }
        for (NodoHistoria i = lista.getIni(); i != null; i = i.getSig()) {
            for (NodoHistoria j = i.getSig(); j != null; j = j.getSig()) {
                if (i.getDato().getNombrePaciente()
                        .compareToIgnoreCase(j.getDato().getNombrePaciente()) > 0) {
                    // Intercambiamos los objetos HistoriaClinica entre los nodos
                    HistoriaClinica temp = i.getDato();
                    i.setDato(j.getDato());
                    j.setDato(temp);
                }
            }
        }
    }
}//fin de la clase