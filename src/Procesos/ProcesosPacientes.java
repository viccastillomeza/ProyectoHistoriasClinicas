package Procesos;

import Estructura.NodoPaciente;
import Modelo.Paciente;
import Vista.frmPaciente;
import javax.swing.table.DefaultTableModel;

public class ProcesosPacientes {

    // Activa o desactiva campos y botones según el modo (igual que ProcesosForma01)
    public static void Estado_Botones(boolean Estado, frmPaciente f) {
        f.txtDni.setEnabled(Estado);
        f.txtApellidos.setEnabled(Estado);
        f.txtNombres.setEnabled(Estado);
        f.txtEdad.setEnabled(Estado);
        f.cbxSexo.setEnabled(Estado);
        f.txtTelefono.setEnabled(Estado);

        f.btnNuevo.setEnabled(!Estado);
        f.btnAgregar.setEnabled(Estado);
        f.btnCancelar.setEnabled(Estado);
        f.btnBuscar.setEnabled(!Estado);
        f.btnActualizar.setEnabled(false);
        f.btnEliminar.setEnabled(!Estado);
        f.Scroll.setEnabled(!Estado);
    }

    // Modo especial cuando se encuentra un paciente con BUSCAR para poder actualizarlo
    public static void Estado_Actualizar(frmPaciente f) {
        f.txtDni.setEnabled(true);
        f.txtApellidos.setEnabled(true);
        f.txtNombres.setEnabled(true);
        f.txtEdad.setEnabled(true);
        f.cbxSexo.setEnabled(true);
        f.txtTelefono.setEnabled(true);

        f.btnNuevo.setEnabled(false);
        f.btnAgregar.setEnabled(false);
        f.btnCancelar.setEnabled(true);
        f.btnBuscar.setEnabled(false);
        f.btnActualizar.setEnabled(true);
        f.btnEliminar.setEnabled(false);
        f.Scroll.setEnabled(false);
    }

    public static void LimpiarEntradas(frmPaciente f) {
        f.txtDni.setText("");
        f.txtApellidos.setText("");
        f.txtNombres.setText("");
        f.txtEdad.setText("");
        f.cbxSexo.setSelectedIndex(0);
        f.txtTelefono.setText("");
        f.txtDni.requestFocus();
    }

    public static void LimpiarTabla(DefaultTableModel modtabla) {
        modtabla.setRowCount(0);
    }

    // Lee los datos del formulario usando el constructor Object[] de Paciente
    public static Paciente LeerPaciente(frmPaciente f) {
        Object[] Registro = {
            f.txtDni.getText().trim(),
            f.txtApellidos.getText().trim(),
            f.txtNombres.getText().trim(),
            Integer.parseInt(f.txtEdad.getText().trim()),
            f.cbxSexo.getSelectedItem().toString(),
            f.txtTelefono.getText().trim()
        };
        return new Paciente(Registro);
    }

    // Carga los datos de un nodo encontrado en las cajas de texto
    public static void MostrarDatosNodo(NodoPaciente actual, frmPaciente f1) {
        f1.txtDni.setText(actual.getElemento().getDni());
        f1.txtApellidos.setText(actual.getElemento().getApellidos());
        f1.txtNombres.setText(actual.getElemento().getNombres());
        f1.txtEdad.setText(String.valueOf(actual.getElemento().getEdad()));
        f1.cbxSexo.setSelectedItem(actual.getElemento().getSexo());
        f1.txtTelefono.setText(actual.getElemento().getTelefono());
    }
}//fin de la clase