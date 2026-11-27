package Persistencia;

import Estructura.ListaHistorias;
import Procesos.Mensajes;
import java.io.*;

public class AlmacenarHistorias {

    public static String archivo = "DatosHistorias.bin";

    public static void Guardar(ListaHistorias lista) {
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lista);
            oos.close();
        } catch (Exception ex) {
            Mensajes.Mostrar("ERROR: No se pueden guardar las historias clínicas... " + ex);
        }
    }

    public static ListaHistorias Recuperar() {
        ListaHistorias lista = new ListaHistorias();
        File f = new File(archivo);
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lista = (ListaHistorias) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                Mensajes.Mostrar("ERROR: No se pueden recuperar las historias clínicas... " + ex);
            }
        }
        return lista;
    }
}//fin de la clase