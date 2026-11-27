package Estructura;

import java.io.Serializable;
import Modelo.Especialidad;

public class ListaEspecialidades implements Serializable {

    private Especialidad[] lista;
    public static int cantEsp = 0;

    public ListaEspecialidades(int TOPE) {
        lista = new Especialidad[TOPE];
    }

    // Encapsulamiento del arreglo
    public void setLista(Especialidad[] lista) {
        this.lista = lista;
    }

    public Especialidad[] getLista() {
        return this.lista;
    }

    // Actualiza el contador estático cuando se recupera el archivo binario
    public void ActualizarCantidadEsp() {
        cantEsp = 0;
        for (Especialidad esp : lista) {
            if (esp != null) {
                cantEsp++;
            }
        }
    }//fin metodo

    // Agrega una especialidad al arreglo
    public void AgregarEsp(Especialidad esp) {
        if (cantEsp < lista.length) {
            lista[cantEsp] = esp;
            cantEsp++;
        }
    }

    // Recupera una especialidad según su posición en el arreglo
    public Especialidad RecuperarEsp(int posicion) {
        return lista[posicion];
    }

    // Busca una especialidad por su código (Retorna la posición o -1 si no existe)
    public int BuscarEsp(String codbuscado) {
        for (int i = 0; i < cantEsp; i++) {
            if (codbuscado.equalsIgnoreCase(lista[i].getCod())) {
                return i;
            }
        }
        return -1;
    }

    // Actualiza una especialidad en una posición específica (Mantenimiento)
    public void ActualizarEsp(int posicion, Especialidad nuevaEsp) {
        lista[posicion] = nuevaEsp;
    }

    // Elimina una especialidad desplazando los elementos del arreglo
    public void EliminarEsp(String codaeliminar) {
        for (int i = 0; i < cantEsp; i++) {
            if (codaeliminar.equalsIgnoreCase(lista[i].getCod())) {
                for (int j = i; j < cantEsp - 1; j++) {
                    lista[j] = lista[j + 1];
                }
                lista[cantEsp - 1] = null;
                cantEsp--;
                break;
            }
        }
    }
}//fin clase