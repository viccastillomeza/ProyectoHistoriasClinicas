package Controlador;

import Estructura.*;
import Modelo.HistoriaClinica;
import Persistencia.AlmacenarHistorias;
import Procesos.Mensajes;
import Procesos.ProcesosReportes;
import Vista.frmReportes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import Modelo.Paciente;
import Persistencia.AlmacenarPacientes;
import Procesos.GeneradorPDF;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlReportes implements ActionListener {

    frmReportes vista;
    ListaHistorias listaHistorias;
    PilaAtenciones pilaMedico;
    ColaAtenciones colaRango;
    Paciente pacientePdf = null;
    ListaHistorias historiasPdf = null;

    public ControlReportes(frmReportes f) {
        this.vista = f;
        this.pilaMedico = new PilaAtenciones();
        this.colaRango = new ColaAtenciones();

        // Escuchamos los botones 
        this.vista.btnConsultarMedico.addActionListener(this);
        this.vista.btnDesapilar.addActionListener(this);
        this.vista.btnConsultarRango.addActionListener(this);
        this.vista.btnDesencolar.addActionListener(this);
        // Escuchamos los botones de RF-10 (Historia Clínica en PDF)
        this.vista.btnBuscarPdf.addActionListener(this);
        this.vista.btnGenerarPdf.addActionListener(this);

        ProcesosReportes.CargarCombos(vista);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //ATENCIONES QUE HIZO UN MÉDICO DURANTE EL DÍA (PILA)
        
        if (e.getSource() == vista.btnConsultarMedico) {
            String doctorSel = vista.cbxDoctor.getSelectedItem().toString();
            String fechaDia = vista.txtFechaDia.getText().trim();

            if (fechaDia.isEmpty()) {
                Mensajes.Mostrar("Ingrese la fecha del día (formato dd/MM/yyyy).");
                return;
            }

            try {
                // Validamos que el formato de fecha sea correcto
                LocalDate fechaBuscada = ProcesosReportes.ConvertirFecha(fechaDia);

                listaHistorias = AlmacenarHistorias.Recuperar();
                pilaMedico = new PilaAtenciones();

                NodoHistoria aux = listaHistorias.getIni();
                while (aux != null) {
                    HistoriaClinica hc = aux.getDato();
                    if (hc.getDoctor().equalsIgnoreCase(doctorSel)) {
                        try {
                            LocalDate fechaHc = ProcesosReportes.ConvertirFecha(hc.getFecha());
                            if (fechaHc.isEqual(fechaBuscada)) {
                                pilaMedico.Apilar(hc); // PUSH a la pila
                            }
                        } catch (DateTimeParseException ex) {
                            // Si alguna historia antigua tiene formato inválido, se omite sin detener el proceso
                        }
                    }
                    aux = aux.getSig();
                }

                ProcesosReportes.MostrarPila(vista.tblPila, pilaMedico, vista);

            } catch (DateTimeParseException ex) {
                Mensajes.Mostrar("Formato de fecha incorrecto. Use dd/MM/yyyy (Ejemplo: 26/11/2026).");
                vista.txtFechaDia.requestFocus();
            }
        }

        // Demostración de operación POP sobre la Pila (RF-08)
        if (e.getSource() == vista.btnDesapilar) {
            if (pilaMedico.EstaVacia()) {
                Mensajes.Mostrar("La pila está vacía. Realice primero una consulta.");
            } else {
                HistoriaClinica retirada = pilaMedico.Desapilar();
                Mensajes.Mostrar("Se desapiló (POP) la última atención de la vista:\n"
                        + "Historia: " + retirada.getNumHistoria() + " | Paciente: " + retirada.getNombrePaciente());
                ProcesosReportes.MostrarPila(vista.tblPila, pilaMedico, vista);
            }
        }

        //ATENCIONES POR RANGO DE FECHAS Y ESPECIALIDAD (COLA DE PRIORIDAD)
        if (e.getSource() == vista.btnConsultarRango) {
            String espSel = vista.cbxEspecialidad.getSelectedItem().toString();
            String fInicioTxt = vista.txtFechaInicio.getText().trim();
            String fFinTxt = vista.txtFechaFin.getText().trim();

            if (fInicioTxt.isEmpty() || fFinTxt.isEmpty()) {
                Mensajes.Mostrar("Ingrese la Fecha Inicio y Fecha Fin (formato dd/MM/yyyy).");
                return;
            }

            try {
                LocalDate fechaIni = ProcesosReportes.ConvertirFecha(fInicioTxt);
                LocalDate fechaFin = ProcesosReportes.ConvertirFecha(fFinTxt);

                if (fechaFin.isBefore(fechaIni)) {
                    Mensajes.Mostrar("La Fecha Fin no puede ser anterior a la Fecha Inicio.");
                    return;
                }

                listaHistorias = AlmacenarHistorias.Recuperar();
                colaRango = new ColaAtenciones();

                NodoHistoria aux = listaHistorias.getIni();
                while (aux != null) {
                    HistoriaClinica hc = aux.getDato();
                    boolean coincideEsp = espSel.equalsIgnoreCase("TODAS")
                            || hc.getEspecialidad().equalsIgnoreCase(espSel);

                    if (coincideEsp) {
                        try {
                            LocalDate fechaHc = ProcesosReportes.ConvertirFecha(hc.getFecha());
                            // Verifica que fechaHc esté entre fechaIni y fechaFin (inclusive)
                            if (!fechaHc.isBefore(fechaIni) && !fechaHc.isAfter(fechaFin)) {
                                colaRango.Encolar(hc); // OFFER a la cola de prioridad
                            }
                        } catch (DateTimeParseException ex) {
                            // Omite fechas con formato inválido
                        }
                    }
                    aux = aux.getSig();
                }

                ProcesosReportes.MostrarCola(vista.tblCola, colaRango, vista);

            } catch (DateTimeParseException ex) {
                Mensajes.Mostrar("Verifique el formato de las fechas. Use dd/MM/yyyy.");
            }
        }

        // Demostración de operación POLL sobre la Cola de Prioridad
        if (e.getSource() == vista.btnDesencolar) {
            if (colaRango.EstaVacia()) {
                Mensajes.Mostrar("La cola está vacía. Realice primero una consulta.");
            } else {
                HistoriaClinica atendida = colaRango.Desencolar();
                Mensajes.Mostrar("Se desencoló (POLL) la atención más prioritaria de la vista:\n"
                        + "Historia: " + atendida.getNumHistoria() + " | Paciente: " + atendida.getNombrePaciente()
                        + "\nPrioridad: " + atendida.getPrioridad());
                ProcesosReportes.MostrarCola(vista.tblCola, colaRango, vista);
            }
        }
        // BUSCAR HISTORIA DEL PACIENTE Y GENERAR / VER EN PDF
        if (e.getSource() == vista.btnBuscarPdf) {
            String dni = vista.txtDniPdf.getText().trim();
            if (dni.isEmpty()) {
                Mensajes.Mostrar("Ingrese el DNI del paciente para buscar su historia clínica.");
                vista.txtDniPdf.requestFocus();
                return;
            }

            // 1. Buscamos al paciente en el Árbol Binario (DatosPacientes.bin)
            ArbolPaciente arbolPac = AlmacenarPacientes.RecuperarDeArchivo();
            NodoPaciente nodoPac = arbolPac.BuscarPorDni(arbolPac.getRaiz(), dni);

            // 2. Buscamos todas sus atenciones en la Lista Enlazada (DatosHistorias.bin)
            listaHistorias = AlmacenarHistorias.Recuperar();
            historiasPdf = listaHistorias.BuscarPorDni(dni);

            if (nodoPac != null) {
                pacientePdf = nodoPac.getElemento();
            } else if (historiasPdf.Cantidad() > 0) {
                // Respaldo: si tiene historias registradas pero no figura en el árbol de pacientes
                HistoriaClinica primera = historiasPdf.getIni().getDato();
                pacientePdf = new Paciente();
                pacientePdf.setDni(primera.getDniPaciente());
                pacientePdf.setApellidos(primera.getNombrePaciente());
                pacientePdf.setNombres("");
                pacientePdf.setEdad(0);
                pacientePdf.setSexo("No registrado");
                pacientePdf.setTelefono("No registrado");
            } else {
                pacientePdf = null;
                vista.txaVistaPrevia.setText("");
                Mensajes.Mostrar("No se encontró ningún paciente ni historia clínica con el DNI: " + dni);
                return;
            }

            ProcesosReportes.MostrarVistaPreviaPDF(pacientePdf, historiasPdf, vista);
        }

        if (e.getSource() == vista.btnGenerarPdf) {
            if (pacientePdf == null || historiasPdf == null) {
                Mensajes.Mostrar("Primero busque la historia clínica de un paciente por DNI.");
                return;
            }

            // Cuadro de diálogo para elegir dónde guardar el PDF
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Guardar Historia Clínica en PDF");
            selector.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));
            selector.setSelectedFile(new File("HistoriaClinica_" + pacientePdf.getDni() + ".pdf"));

            int opcion = selector.showSaveDialog(vista);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                // Aseguramos que el archivo termine siempre en .pdf
                if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                    archivo = new File(archivo.getAbsolutePath() + ".pdf");
                }

                try {
                    GeneradorPDF.CrearHistoriaClinicaPDF(pacientePdf, historiasPdf, archivo);
                    Mensajes.Mostrar("PDF generado correctamente en:\n" + archivo.getAbsolutePath());

                    // Abre automáticamente el PDF en el visor predeterminado de Windows
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(archivo);
                    }
                } catch (Exception ex) {
                    Mensajes.Mostrar("ERROR al generar o abrir el PDF: " + ex.getMessage());
                }
            }
        }
    }//fin actionPerformed
}//fin de la clase