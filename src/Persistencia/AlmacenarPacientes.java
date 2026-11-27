package Persistencia;

import java.io.*;
import Estructura.ArbolPaciente;
import Procesos.Mensajes;

public class AlmacenarPacientes {

    public static String archivo = "DatosPacientes.bin";

    public static void GuardarEnArchivo(ArbolPaciente arbol) {
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arbol);
            oos.close();
        } catch (Exception ex) {
            Mensajes.Mostrar("ERROR no se puede guardar en archivo " + ex);
        }
    }//fin guardar

    public static ArbolPaciente RecuperarDeArchivo() {
        ArbolPaciente arbol = new ArbolPaciente();
        File f = new File(archivo);
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                arbol = (ArbolPaciente) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                Mensajes.Mostrar("ERROR no se puede recuperar de archivo " + ex);
            }
        }
        return arbol;
    }//fin recuperar
}//fin class