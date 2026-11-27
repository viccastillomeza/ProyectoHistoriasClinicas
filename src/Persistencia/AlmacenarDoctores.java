package Persistencia;

import java.io.*;
import Estructura.ListaDoctores;
import Procesos.Mensajes;

public class AlmacenarDoctores {

    public static String archivo = "DatosDoctores.bin";

    public static void GuardarListaDoc(ListaDoctores ld) {
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ld);
            oos.close();
        } catch (Exception ex) {
            Mensajes.Mostrar("ERROR: No se puede guardar doctores... " + ex);
        }
    }

    public static ListaDoctores RecuperarListaDoc() {
        ListaDoctores lista = new ListaDoctores();
        File f = new File(archivo);
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lista = (ListaDoctores) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                Mensajes.Mostrar("ERROR: No se puede recuperar doctores... " + ex);
            }
        }
        return lista;
    }
}//fin clase