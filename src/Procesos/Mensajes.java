package Procesos;

import javax.swing.JOptionPane;

public class Mensajes {

    // Muestra un mensaje emergente en pantalla
    public static void Mostrar(String msj) {
        JOptionPane.showMessageDialog(null, msj);
    }//fin metodo

    // Solicita un texto al usuario mediante un cuadro de entrada
    public static String Leer(String msj) {
        return JOptionPane.showInputDialog(msj);
    }//fin metodo

    // Muestra un cuadro de confirmación (Retorna 0 si el usuario presiona SI/OK)
    public static int Preguntar(String titulo, String msj) {
        return JOptionPane.showConfirmDialog(null,
                msj, titulo, JOptionPane.YES_NO_OPTION);
    }//fin metodo

}//fin de la clase