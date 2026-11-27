package Persistencia;

import java.io.*;
import Estructura.ListaEspecialidades;
import Procesos.Mensajes;

public class AlmacenarEspecialidades {

    public static String nombrearchivo = "DatosEspecialidades.bin";

    // Guarda el arreglo de objetos en el archivo binario
    public static void Guardar(ListaEspecialidades lista) {
        try {
            FileOutputStream fos = new FileOutputStream(nombrearchivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lista);
            oos.close();
        } catch (Exception ex) {
            Mensajes.Mostrar("ERROR: No se puede guardar especialidades... " + ex);
        }
    }//fin metodo

    // Recupera el arreglo de objetos desde el archivo binario
    public static ListaEspecialidades Recuperar() {
        ListaEspecialidades lista = new ListaEspecialidades(30);
        File f = new File(nombrearchivo);
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(nombrearchivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lista = (ListaEspecialidades) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                Mensajes.Mostrar("ERROR: No se puede recuperar especialidades... " + ex);
            }
        }
        return lista;
    }//fin metodo
}//fin clase