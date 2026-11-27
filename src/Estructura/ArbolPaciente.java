package Estructura;

import Modelo.Paciente;
import java.io.Serializable;
import javax.swing.table.DefaultTableModel;

public class ArbolPaciente implements Serializable {

    private NodoPaciente Raiz;

    public ArbolPaciente() {
        Raiz = null;
    }

    // Método que agrega un nodo ordenado por Apellidos y Nombres
    public NodoPaciente AgregarPaciente(NodoPaciente Nodo, Paciente elemento) {
        if (Nodo == null) {
            NodoPaciente nuevo = new NodoPaciente(elemento);
            return nuevo;
        } else {
            if (elemento.NombreCompleto().compareToIgnoreCase(
                    Nodo.getElemento().NombreCompleto()) > 0) {
                Nodo.setDer(AgregarPaciente(Nodo.getDer(), elemento));
            } else {
                Nodo.setIzq(AgregarPaciente(Nodo.getIzq(), elemento));
            }
        }
        return Nodo;
    }

    // Método que muestra los datos ordenados alfabéticamente en el JTable (In-Orden)
    public void MostrarEnOrden(NodoPaciente Nodo, DefaultTableModel modelo) {
        if (Nodo != null) {
            MostrarEnOrden(Nodo.getIzq(), modelo);
            modelo.addRow(Nodo.getElemento().getRegistro());
            MostrarEnOrden(Nodo.getDer(), modelo);
        }
    }

    // Método que busca un nodo por Apellidos/Nombres 
    public NodoPaciente BuscarNombres(String dato) {
        NodoPaciente auxiliar = Raiz;
        while (auxiliar != null) {
            if (auxiliar.getElemento().NombreCompleto().toUpperCase()
                    .startsWith(dato.toUpperCase())) {
                return auxiliar;
            } else {
                if (dato.compareToIgnoreCase(auxiliar.getElemento().NombreCompleto()) > 0) {
                    auxiliar = auxiliar.getDer();
                } else {
                    auxiliar = auxiliar.getIzq();
                }
            }
        }//fin while
        return null;
    }

    // Búsqueda complementaria por DNI (recorre el árbol para localizar por documento)
    public NodoPaciente BuscarPorDni(NodoPaciente Nodo, String dniBuscado) {
        if (Nodo == null) {
            return null;
        }
        if (Nodo.getElemento().getDni().equalsIgnoreCase(dniBuscado)) {
            return Nodo;
        }
        NodoPaciente izq = BuscarPorDni(Nodo.getIzq(), dniBuscado);
        if (izq != null) {
            return izq;
        }
        return BuscarPorDni(Nodo.getDer(), dniBuscado);
    }

    // Getter y Setter
    public NodoPaciente getRaiz() { return Raiz; }
    public void setRaiz(NodoPaciente Raiz) { this.Raiz = Raiz; }

    // Método que busca el mayor elemento del subárbol izquierdo
    public NodoPaciente BuscarMayorIzquierda(NodoPaciente auxiliar) {
        if (auxiliar != null) {
            while (auxiliar.getDer() != null) {
                auxiliar = auxiliar.getDer();
            }
        }
        return auxiliar;
    }//fin metodo

    // Método que elimina el mayor paciente del lado izquierdo (corregido: == null)
    public NodoPaciente EliminarMayorIzquierda(NodoPaciente auxiliar) {
        if (auxiliar == null) {
            return null;
        } else if (auxiliar.getDer() != null) {
            auxiliar.setDer(EliminarMayorIzquierda(auxiliar.getDer()));
            return auxiliar;
        }
        return auxiliar.getIzq();
    }//fin metodo

    // Método que busca un nodo por NombreCompleto y lo elimina
    public NodoPaciente Eliminar(NodoPaciente auxiliar, String dato) {
        if (auxiliar == null) {
            return null;
        }
        if (dato.compareToIgnoreCase(auxiliar.getElemento().NombreCompleto()) < 0) {
            auxiliar.setIzq(Eliminar(auxiliar.getIzq(), dato));
        } else if (dato.compareToIgnoreCase(auxiliar.getElemento().NombreCompleto()) > 0) {
            auxiliar.setDer(Eliminar(auxiliar.getDer(), dato));
        } else if (auxiliar.getIzq() != null && auxiliar.getDer() != null) {
            auxiliar.setElemento(BuscarMayorIzquierda(auxiliar.getIzq()).getElemento());
            auxiliar.setIzq(EliminarMayorIzquierda(auxiliar.getIzq()));
        } else {
            auxiliar = (auxiliar.getIzq() != null) ? auxiliar.getIzq() : auxiliar.getDer();
        }
        return auxiliar;
    }//fin metodo
}//fin de la clase