package Procesos;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Estructura.ListaEspecialidades;
import Modelo.Especialidad;
import Vista.frmEspecialidad;

public class ProcesosEspecialidades {

    // Muestra las especialidades del arreglo en el JTable
    public static void MostrarDatos(JTable tabla, Especialidad[] lista, int cantEsp) {
        String titulos[] = {"Nro", "Código", "Especialidad", "Consultorio", "Costo (S/)"};
        DefaultTableModel mt = new DefaultTableModel(null, titulos);
        tabla.setModel(mt);
        for (int i = 0; i < cantEsp; i++) {
            mt.addRow(lista[i].Registro(i + 1));
        }
    }

    // Lee los datos de las cajas de texto y devuelve un objeto Especialidad
    public static Especialidad LeerEspecialidad(frmEspecialidad f) {
        Especialidad esp = new Especialidad();
        esp.setCod(f.txtCodigo.getText().trim());
        esp.setNom(f.txtNombre.getText().trim());
        esp.setConsultorio(f.txtConsultorio.getText().trim());
        esp.setCosto(Double.parseDouble(f.txtCosto.getText().trim()));
        return esp;
    }

    // Limpia las cajas de texto
    public static void LimpiarEntradas(frmEspecialidad f) {
        f.txtCodigo.setText("");
        f.txtNombre.setText("");
        f.txtConsultorio.setText("");
        f.txtCosto.setText("");
        f.txtCodigo.requestFocus();
    }

    // Carga los datos de una especialidad encontrada en las cajas para actualizarla
    public static void MostrarUnaEspecialidad(Especialidad esp, frmEspecialidad f) {
        f.txtCodigo.setText(esp.getCod());
        f.txtNombre.setText(esp.getNom());
        f.txtConsultorio.setText(esp.getConsultorio());
        f.txtCosto.setText(String.valueOf(esp.getCosto()));
        f.txtCodigo.requestFocus();
    }
}//fin clase