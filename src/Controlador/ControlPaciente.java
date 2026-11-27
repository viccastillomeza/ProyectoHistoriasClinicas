package Controlador;

import Estructura.*;
import Modelo.Paciente;
import Persistencia.AlmacenarPacientes;
import Procesos.Mensajes;
import Procesos.ProcesosPacientes;
import Vista.frmPaciente;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class ControlPaciente implements ActionListener {

    frmPaciente vista;
    ArbolPaciente objArbol;
    DefaultTableModel modTabla;
    NodoPaciente nodoEncontrado = null; // Guarda el nodo buscado para poder actualizarlo

    public ControlPaciente(frmPaciente f1) {
        vista = f1;
        vista.btnNuevo.addActionListener(this);
        vista.btnAgregar.addActionListener(this);
        vista.btnBuscar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnCancelar.addActionListener(this);
        vista.btnActualizar.addActionListener(this);

        // Definimos las columnas del JTable desde código para evitar errores de diseño
        String[] titulos = {"DNI", "APELLIDOS", "NOMBRES", "EDAD", "SEXO", "TELEFONO"};
        modTabla = new DefaultTableModel(null, titulos);
        vista.tblDatos.setModel(modTabla);

        ProcesosPacientes.Estado_Botones(false, vista);

        objArbol = new ArbolPaciente();
        ActualizarVista();
    }

    private void ActualizarVista() {
        objArbol = AlmacenarPacientes.RecuperarDeArchivo();
        ProcesosPacientes.LimpiarTabla(modTabla);
        objArbol.MostrarEnOrden(objArbol.getRaiz(), modTabla);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnNuevo) {
            ProcesosPacientes.LimpiarEntradas(vista);
            ProcesosPacientes.Estado_Botones(true, vista);
        }

        if (e.getSource() == vista.btnAgregar) {
            try {
                String dni = vista.txtDni.getText().trim();
                if (objArbol.BuscarPorDni(objArbol.getRaiz(), dni) != null) {
                    Mensajes.Mostrar("Ya existe un paciente registrado con el DNI: " + dni);
                    return;
                }
                Paciente elemento = ProcesosPacientes.LeerPaciente(vista);
                objArbol.setRaiz(objArbol.AgregarPaciente(objArbol.getRaiz(), elemento));
                AlmacenarPacientes.GuardarEnArchivo(objArbol);
                ActualizarVista();
                ProcesosPacientes.LimpiarEntradas(vista);
                ProcesosPacientes.Estado_Botones(false, vista);
                Mensajes.Mostrar("Paciente registrado correctamente en el Árbol Binario.");
            } catch (NumberFormatException ex) {
                Mensajes.Mostrar("La edad debe ser un número entero válido.");
                vista.txtEdad.requestFocus();
            }
        }

        if (e.getSource() == vista.btnCancelar) {
            nodoEncontrado = null;
            ProcesosPacientes.LimpiarEntradas(vista);
            ProcesosPacientes.Estado_Botones(false, vista);
        }

        if (e.getSource() == vista.btnBuscar) {
            String texto = Mensajes.Leer("Ingrese DNI o Apellidos del paciente a buscar:");
            if (texto != null && !texto.trim().isEmpty()) {
                // Primero busca por DNI; si no coincide, busca por Apellidos/Nombres
                nodoEncontrado = objArbol.BuscarPorDni(objArbol.getRaiz(), texto.trim());
                if (nodoEncontrado == null) {
                    nodoEncontrado = objArbol.BuscarNombres(texto.trim());
                }

                if (nodoEncontrado == null) {
                    Mensajes.Mostrar(texto + " no existe en el árbol de pacientes.");
                } else {
                    ProcesosPacientes.MostrarDatosNodo(nodoEncontrado, vista);
                    ProcesosPacientes.Estado_Actualizar(vista);
                }
            }
        }

        if (e.getSource() == vista.btnActualizar) {
            if (nodoEncontrado != null) {
                try {
                    // Guardamos la clave anterior antes de leer los datos nuevos
                    String claveAnterior = nodoEncontrado.getElemento().NombreCompleto();
                    Paciente pacActualizado = ProcesosPacientes.LeerPaciente(vista);

                    // Eliminamos el nodo anterior y reinsertamos para conservar el orden del ABB
                    objArbol.setRaiz(objArbol.Eliminar(objArbol.getRaiz(), claveAnterior));
                    objArbol.setRaiz(objArbol.AgregarPaciente(objArbol.getRaiz(), pacActualizado));

                    AlmacenarPacientes.GuardarEnArchivo(objArbol);
                    ActualizarVista();
                    nodoEncontrado = null;
                    ProcesosPacientes.LimpiarEntradas(vista);
                    ProcesosPacientes.Estado_Botones(false, vista);
                    Mensajes.Mostrar("Datos del paciente actualizados correctamente.");
                } catch (NumberFormatException ex) {
                    Mensajes.Mostrar("La edad debe ser un número entero válido.");
                    vista.txtEdad.requestFocus();
                }
            }
        }

        if (e.getSource() == vista.btnEliminar) {
            int fila = this.vista.tblDatos.getSelectedRow();
            if (fila == -1) {
                Mensajes.Mostrar("Debe seleccionar un registro de la tabla para eliminar.");
            } else {
                // Columna 1 = APELLIDOS, Columna 2 = NOMBRES
                String cadena = modTabla.getValueAt(fila, 1) + " " + modTabla.getValueAt(fila, 2);
                int resp = Mensajes.Preguntar("Confirmar!!!", "¿Desea eliminar a " + cadena + "?");
                if (resp == 0) {
                    objArbol.setRaiz(objArbol.Eliminar(objArbol.getRaiz(), cadena));
                    AlmacenarPacientes.GuardarEnArchivo(objArbol);
                    ActualizarVista();
                    Mensajes.Mostrar("Paciente eliminado del árbol.");
                }
            }
        }//fin eliminar
    }//fin action
}//fin de la clase