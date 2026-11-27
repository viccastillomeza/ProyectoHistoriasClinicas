package Estructura;

import Modelo.HistoriaClinica;
import java.io.Serializable;

public class ListaHistorias implements Serializable {

    private NodoHistoria ini; // Primer nodo de la lista
    private NodoHistoria fin; // Último nodo de la lista

    public ListaHistorias() {
        ini = null;
        fin = null;
    }

    // 1. Insertar una nueva historia clínica al final de la lista enlazada
    public void AgregarAlFinal(HistoriaClinica hc) {
        NodoHistoria nuevo = new NodoHistoria(hc);
        if (ini == null) {
            ini = nuevo;
            fin = nuevo;
        } else {
            fin.setSig(nuevo);
            fin = nuevo;
        }
    }

    // 2. Buscar un nodo exacto por su Número de Historia (para mantenimiento)
    public NodoHistoria BuscarPorNumero(String numBuscado) {
        NodoHistoria aux = ini;
        while (aux != null) {
            if (aux.getDato().getNumHistoria().equalsIgnoreCase(numBuscado)) {
                return aux;
            }
            aux = aux.getSig();
        }
        return null;
    }

    // 3. Buscar y devolver una sublista de historias por DNI del paciente (RF-07)
    public ListaHistorias BuscarPorDni(String dniBuscado) {
        ListaHistorias resultados = new ListaHistorias();
        NodoHistoria aux = ini;
        while (aux != null) {
            if (aux.getDato().getDniPaciente().equalsIgnoreCase(dniBuscado)) {
                resultados.AgregarAlFinal(aux.getDato());
            }
            aux = aux.getSig();
        }
        return resultados;
    }

    // 4. Buscar y devolver una sublista de historias por Nombres del paciente (RF-07)
    public ListaHistorias BuscarPorNombres(String nombreBuscado) {
        ListaHistorias resultados = new ListaHistorias();
        NodoHistoria aux = ini;
        while (aux != null) {
            if (aux.getDato().getNombrePaciente().toUpperCase()
                    .contains(nombreBuscado.toUpperCase())) {
                resultados.AgregarAlFinal(aux.getDato());
            }
            aux = aux.getSig();
        }
        return resultados;
    }

    // 5. Eliminar un nodo por Número de Historia (mantenimiento RF-07)
    public boolean Eliminar(String numEliminar) {
        if (ini == null) {
            return false;
        }
        // Caso 1: El nodo a eliminar es el primero (ini)
        if (ini.getDato().getNumHistoria().equalsIgnoreCase(numEliminar)) {
            ini = ini.getSig();
            if (ini == null) {
                fin = null; // La lista quedó vacía
            }
            return true;
        }
        // Caso 2: El nodo está en el medio o al final
        NodoHistoria ant = ini;
        NodoHistoria aux = ini.getSig();
        while (aux != null) {
            if (aux.getDato().getNumHistoria().equalsIgnoreCase(numEliminar)) {
                ant.setSig(aux.getSig());
                if (aux == fin) {
                    fin = ant; // Actualizamos fin si borramos el último nodo
                }
                return true;
            }
            ant = aux;
            aux = aux.getSig();
        }
        return false;
    }

    // Cuenta el total de nodos en la lista
    public int Cantidad() {
        int cont = 0;
        NodoHistoria aux = ini;
        while (aux != null) {
            cont++;
            aux = aux.getSig();
        }
        return cont;
    }

    public NodoHistoria getIni() { return ini; }
    public void setIni(NodoHistoria ini) { this.ini = ini; }

    public NodoHistoria getFin() { return fin; }
    public void setFin(NodoHistoria fin) { this.fin = fin; }
}//fin de la clase