package Procesos;

import Estructura.*;
import Modelo.HistoriaClinica;
import Persistencia.AlmacenarDoctores;
import Persistencia.AlmacenarEspecialidades;
import Vista.frmHistoriaClinica;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ProcesosHistorias {

    // Carga las Especialidades y Doctores registrados en sus respectivos archivos .bin
    public static void CargarCombos(frmHistoriaClinica f) {
        // 1. Cargar Especialidades (desde el Arreglo de Objetos)
        f.cbxEspecialidad.removeAllItems();
        ListaEspecialidades listaEsp = AlmacenarEspecialidades.Recuperar();
        listaEsp.ActualizarCantidadEsp();
        if (ListaEspecialidades.cantEsp == 0) {
            f.cbxEspecialidad.addItem("MEDICINA GENERAL");
            f.cbxEspecialidad.addItem("PEDIATRIA");
            f.cbxEspecialidad.addItem("ODONTOLOGIA");
        } else {
            for (int i = 0; i < ListaEspecialidades.cantEsp; i++) {
                f.cbxEspecialidad.addItem(listaEsp.RecuperarEsp(i).getNom());
            }
        }

        // 2. Cargar Doctores (desde el ArrayList)
        f.cbxDoctor.removeAllItems();
        ListaDoctores listaDoc = AlmacenarDoctores.RecuperarListaDoc();
        if (listaDoc.CantidadDoc() == 0) {
            f.cbxDoctor.addItem("SIN DOCTORES REGISTRADOS");
        } else {
            for (int i = 0; i < listaDoc.CantidadDoc(); i++) {
                f.cbxDoctor.addItem(listaDoc.RecuperarDoc(i).getNombres());
            }
        }
    }

    // Devuelve la fecha actual del sistema en formato dd/MM/yyyy
    public static String FechaActual() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(fmt);
    }

    // Muestra todos los nodos de una ListaHistorias en el JTable
    public static void MostrarDatos(JTable tabla, ListaHistorias lista) {
        String titulos[] = {
            "Nro", "N° Historia", "DNI", "Paciente",
            "Especialidad", "Doctor", "Fecha", "Prioridad", "Diagnóstico", "Tratamiento"
        };
        DefaultTableModel mt = new DefaultTableModel(null, titulos);
        tabla.setModel(mt);

        NodoHistoria aux = lista.getIni();
        int cont = 0;
        while (aux != null) {
            cont++;
            mt.addRow(aux.getDato().Registro(cont));
            aux = aux.getSig();
        }
    }

    // Lee los controles del formulario y crea un objeto HistoriaClinica
    public static HistoriaClinica LeerHistoria(frmHistoriaClinica f) {
        HistoriaClinica hc = new HistoriaClinica();
        hc.setNumHistoria(f.txtNumHistoria.getText().trim());
        hc.setDniPaciente(f.txtDniPaciente.getText().trim());
        hc.setNombrePaciente(f.txtNombrePaciente.getText().trim());
        hc.setEspecialidad(f.cbxEspecialidad.getSelectedItem().toString());
        hc.setDoctor(f.cbxDoctor.getSelectedItem().toString());
        hc.setFecha(f.txtFecha.getText().trim());
        hc.setPrioridad(f.cbxPrioridad.getSelectedItem().toString());
        hc.setDiagnostico(f.txtDiagnostico.getText().trim());
        hc.setTratamiento(f.txtTratamiento.getText().trim());
        return hc;
    }

    // Limpia los campos y coloca la fecha actual automáticamente
    public static void LimpiarEntradas(frmHistoriaClinica f) {
        f.txtNumHistoria.setText("");
        f.txtNumHistoria.setEditable(true);
        f.txtDniPaciente.setText("");
        f.txtNombrePaciente.setText("");
        if (f.cbxEspecialidad.getItemCount() > 0) f.cbxEspecialidad.setSelectedIndex(0);
        if (f.cbxDoctor.getItemCount() > 0) f.cbxDoctor.setSelectedIndex(0);
        f.txtFecha.setText(FechaActual());
        f.cbxPrioridad.setSelectedIndex(2); // Por defecto: 3 - CONSULTA EXTERNA
        f.txtDiagnostico.setText("");
        f.txtTratamiento.setText("");
        f.txtNumHistoria.requestFocus();
    }

    // Carga los datos de una historia en el formulario para editarla
    public static void MostrarUnaHistoria(HistoriaClinica hc, frmHistoriaClinica f) {
        f.txtNumHistoria.setText(hc.getNumHistoria());
        f.txtNumHistoria.setEditable(false); // Bloqueamos el N° Historia al actualizar
        f.txtDniPaciente.setText(hc.getDniPaciente());
        f.txtNombrePaciente.setText(hc.getNombrePaciente());
        f.cbxEspecialidad.setSelectedItem(hc.getEspecialidad());
        f.cbxDoctor.setSelectedItem(hc.getDoctor());
        f.txtFecha.setText(hc.getFecha());
        f.cbxPrioridad.setSelectedItem(hc.getPrioridad());
        f.txtDiagnostico.setText(hc.getDiagnostico());
        f.txtTratamiento.setText(hc.getTratamiento());
    }
}//fin de la clase