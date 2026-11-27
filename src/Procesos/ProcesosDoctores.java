package Procesos;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Estructura.ListaDoctores;
import Estructura.ListaEspecialidades;
import Modelo.Doctor;
import Persistencia.AlmacenarEspecialidades;
import Vista.frmDoctor;

public class ProcesosDoctores {

    // Carga en el ComboBox las especialidades guardadas en DatosEspecialidades.bin
    public static void CargarEspecialidades(frmDoctor f) {
        f.cbxEspecialidad.removeAllItems();
        ListaEspecialidades listaEsp = AlmacenarEspecialidades.Recuperar();
        listaEsp.ActualizarCantidadEsp();
        if (ListaEspecialidades.cantEsp == 0) {
            // Especialidades por defecto si aún no se han registrado en el módulo
            f.cbxEspecialidad.addItem("MEDICINA GENERAL");
            f.cbxEspecialidad.addItem("PEDIATRIA");
            f.cbxEspecialidad.addItem("ODONTOLOGIA");
            f.cbxEspecialidad.addItem("OBSTETRICIA");
        } else {
            for (int i = 0; i < ListaEspecialidades.cantEsp; i++) {
                f.cbxEspecialidad.addItem(listaEsp.RecuperarEsp(i).getNom());
            }
        }
    }

    public static void MostrarDatos(JTable tabla, ListaDoctores lista) {
        String titulos[] = {"Nro", "Cód. CMP", "DNI", "Doctor(a)", "Especialidad", "Turno"};
        DefaultTableModel mt = new DefaultTableModel(null, titulos);
        tabla.setModel(mt);
        for (int i = 0; i < lista.CantidadDoc(); i++) {
            mt.addRow(lista.RecuperarDoc(i).Registro(i + 1));
        }
    }

    public static Doctor LeerDoctor(frmDoctor f) {
        Doctor doc = new Doctor();
        doc.setCodCmp(f.txtCodigoCmp.getText().trim());
        doc.setDni(f.txtDni.getText().trim());
        doc.setNombres(f.txtNombres.getText().trim());
        doc.setEspecialidad(f.cbxEspecialidad.getSelectedItem().toString());
        doc.setTurno(f.cbxTurno.getSelectedItem().toString());
        return doc;
    }

    public static void LimpiarEntradas(frmDoctor f) {
        f.txtCodigoCmp.setText("");
        f.txtDni.setText("");
        f.txtNombres.setText("");
        if (f.cbxEspecialidad.getItemCount() > 0) {
            f.cbxEspecialidad.setSelectedIndex(0);
        }
        f.cbxTurno.setSelectedIndex(0);
        f.txtCodigoCmp.requestFocus();
    }

    public static void MostrarUnDoctor(Doctor doc, frmDoctor f) {
        f.txtCodigoCmp.setText(doc.getCodCmp());
        f.txtDni.setText(doc.getDni());
        f.txtNombres.setText(doc.getNombres());
        f.cbxEspecialidad.setSelectedItem(doc.getEspecialidad());
        f.cbxTurno.setSelectedItem(doc.getTurno());
        f.txtCodigoCmp.requestFocus();
    }
}//fin clase