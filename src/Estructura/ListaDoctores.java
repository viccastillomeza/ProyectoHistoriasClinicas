package Estructura;

import java.io.Serializable;
import java.util.ArrayList;
import Modelo.Doctor;

public class ListaDoctores implements Serializable {

    private ArrayList<Doctor> Lista;

    public ListaDoctores() {
        Lista = new ArrayList<>();
    }

    public void AgregarDoc(Doctor doc) {
        Lista.add(doc);
    }

    public Doctor RecuperarDoc(int pos) {
        return Lista.get(pos);
    }

    public void ActualizarDoc(int pos, Doctor nuevodoc) {
        Lista.set(pos, nuevodoc);
    }

    public void EliminarDoc(int pos) {
        Lista.remove(pos);
    }

    public int CantidadDoc() {
        return Lista.size();
    }

    // Busca un doctor por su Código CMP o por DNI
    public int BuscarDoc(String datoBuscado) {
        for (int i = 0; i < Lista.size(); i++) {
            if (datoBuscado.equalsIgnoreCase(Lista.get(i).getCodCmp())
                    || datoBuscado.equalsIgnoreCase(Lista.get(i).getDni())) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Doctor> getLista() {
        return Lista;
    }

    public void setLista(ArrayList<Doctor> Lista) {
        this.Lista = Lista;
    }
}//fin clase