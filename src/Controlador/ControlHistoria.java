package Controlador;

import Estructura.*;
import Modelo.HistoriaClinica;
import Ordenamientos.OrdenarHistorias;
import Persistencia.AlmacenarHistorias;
import Persistencia.AlmacenarPacientes;
import Procesos.Mensajes;
import Procesos.ProcesosHistorias;
import Vista.frmHistoriaClinica;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlHistoria implements ActionListener {

    frmHistoriaClinica vista;
    ListaHistorias lista;
    NodoHistoria nodoEditando = null; // Guarda el nodo seleccionado para actualizar

    public ControlHistoria(frmHistoriaClinica f) {
        this.vista = f;
        this.vista.btnValidarDni.addActionListener(this);
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnBuscarDni.addActionListener(this);
        this.vista.btnBuscarNombre.addActionListener(this);
        this.vista.btnCargarSeleccion.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnOrdenar.addActionListener(this);
        this.vista.btnMostrarTodos.addActionListener(this);

        ProcesosHistorias.CargarCombos(vista);
        ActualizarVista();
    }

    private void ActualizarVista() {
        lista = AlmacenarHistorias.Recuperar();
        ProcesosHistorias.MostrarDatos(vista.tblDatos, lista);
        ProcesosHistorias.LimpiarEntradas(vista);
        nodoEditando = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. Validar DNI en el Árbol Binario de Pacientes y autocompletar nombre
        if (e.getSource() == vista.btnValidarDni) {
            String dni = vista.txtDniPaciente.getText().trim();
            if (dni.isEmpty()) {
                Mensajes.Mostrar("Ingrese un DNI para validar en el registro de pacientes.");
                return;
            }
            ArbolPaciente arbolPac = AlmacenarPacientes.RecuperarDeArchivo();
            NodoPaciente encontrado = arbolPac.BuscarPorDni(arbolPac.getRaiz(), dni);
            if (encontrado != null) {
                vista.txtNombrePaciente.setText(encontrado.getElemento().NombreCompleto());
                Mensajes.Mostrar("Paciente encontrado: " + encontrado.getElemento().NombreCompleto());
            } else {
                Mensajes.Mostrar("El DNI " + dni + " no está registrado en el módulo de Pacientes.");
                vista.txtNombrePaciente.setText("");
            }
        }

        // 2. Registrar nueva Historia Clínica (RF-06)
        if (e.getSource() == vista.btnRegistrar) {
            String numHc = vista.txtNumHistoria.getText().trim();
            String dni = vista.txtDniPaciente.getText().trim();
            String nom = vista.txtNombrePaciente.getText().trim();

            if (numHc.isEmpty() || dni.isEmpty() || nom.isEmpty()) {
                Mensajes.Mostrar("Complete N° de Historia, DNI y Nombre del Paciente.");
                return;
            }
            if (lista.BuscarPorNumero(numHc) != null) {
                Mensajes.Mostrar("Ya existe una historia clínica con el código: " + numHc);
                return;
            }
            HistoriaClinica hc = ProcesosHistorias.LeerHistoria(vista);
            lista.AgregarAlFinal(hc);
            AlmacenarHistorias.Guardar(lista);
            Mensajes.Mostrar("Historia clínica registrada correctamente en la Lista Enlazada.");
            ActualizarVista();
        }

        // 3. Buscar Historias por DNI del paciente (RF-07)
        if (e.getSource() == vista.btnBuscarDni) {
            String dni = Mensajes.Leer("Ingrese el DNI del paciente a buscar:");
            if (dni != null && !dni.trim().isEmpty()) {
                ListaHistorias filtrada = lista.BuscarPorDni(dni.trim());
                if (filtrada.Cantidad() == 0) {
                    Mensajes.Mostrar("No se encontraron historias clínicas para el DNI: " + dni);
                } else {
                    ProcesosHistorias.MostrarDatos(vista.tblDatos, filtrada);
                }
            }
        }

        // 4. Buscar Historias por Nombres o Apellidos del paciente (RF-07)
        if (e.getSource() == vista.btnBuscarNombre) {
            String nombre = Mensajes.Leer("Ingrese Apellidos o Nombres del paciente a buscar:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                ListaHistorias filtrada = lista.BuscarPorNombres(nombre.trim());
                if (filtrada.Cantidad() == 0) {
                    Mensajes.Mostrar("No se encontraron historias clínicas para: " + nombre);
                } else {
                    ProcesosHistorias.MostrarDatos(vista.tblDatos, filtrada);
                }
            }
        }

        // 5. Cargar fila seleccionada de la tabla al formulario para editarla (RF-07)
        if (e.getSource() == vista.btnCargarSeleccion) {
            int fila = vista.tblDatos.getSelectedRow();
            if (fila == -1) {
                Mensajes.Mostrar("Seleccione una fila de la tabla para cargar sus datos.");
            } else {
                // Columna 1 corresponde a "N° Historia"
                String numHc = vista.tblDatos.getValueAt(fila, 1).toString();
                nodoEditando = lista.BuscarPorNumero(numHc);
                if (nodoEditando != null) {
                    ProcesosHistorias.MostrarUnaHistoria(nodoEditando.getDato(), vista);
                }
            }
        }

        // 6. Actualizar datos de la historia clínica cargada (RF-07)
        if (e.getSource() == vista.btnActualizar) {
            if (nodoEditando != null) {
                HistoriaClinica hcActualizada = ProcesosHistorias.LeerHistoria(vista);
                nodoEditando.setDato(hcActualizada);
                AlmacenarHistorias.Guardar(lista);
                Mensajes.Mostrar("Historia clínica actualizada correctamente.");
                ActualizarVista();
            } else {
                Mensajes.Mostrar("Primero seleccione una fila y presione CARGAR PARA EDITAR.");
            }
        }

        // 7. Eliminar una historia clínica seleccionada en la tabla (RF-07)
        if (e.getSource() == vista.btnEliminar) {
            int fila = vista.tblDatos.getSelectedRow();
            if (fila == -1) {
                Mensajes.Mostrar("Seleccione una fila de la tabla para eliminar.");
            } else {
                String numHc = vista.tblDatos.getValueAt(fila, 1).toString();
                int resp = Mensajes.Preguntar("Confirmar!!!", "¿Desea eliminar la historia clínica " + numHc + "?");
                if (resp == 0) {
                    lista.Eliminar(numHc);
                    AlmacenarHistorias.Guardar(lista);
                    Mensajes.Mostrar("Historia clínica eliminada.");
                    ActualizarVista();
                }
            }
        }

        // 8. Ordenar la lista enlazada alfabéticamente por Nombre del Paciente
        if (e.getSource() == vista.btnOrdenar) {
            OrdenarHistorias.OrdenarPorNombrePaciente(lista);
            AlmacenarHistorias.Guardar(lista);
            ProcesosHistorias.MostrarDatos(vista.tblDatos, lista);
            Mensajes.Mostrar("Historias clínicas ordenadas alfabéticamente por paciente.");
        }

        // 9. Restablecer la vista completa después de una búsqueda
        if (e.getSource() == vista.btnMostrarTodos) {
            ActualizarVista();
        }
    }//fin actionPerformed
}//fin de la clase