package Procesos;

import Estructura.*;
import Modelo.HistoriaClinica;
import Modelo.Paciente;
import Persistencia.AlmacenarDoctores;
import Persistencia.AlmacenarEspecialidades;
import Vista.frmReportes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Stack;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ProcesosReportes {

    // Carga los médicos y especialidades registrados en los combos de ambas pestañas
    public static void CargarCombos(frmReportes f) {
        // 1. Combo de Doctores (para RF-08)
        f.cbxDoctor.removeAllItems();
        ListaDoctores listaDoc = AlmacenarDoctores.RecuperarListaDoc();
        if (listaDoc.CantidadDoc() == 0) {
            f.cbxDoctor.addItem("SIN DOCTORES REGISTRADOS");
        } else {
            for (int i = 0; i < listaDoc.CantidadDoc(); i++) {
                f.cbxDoctor.addItem(listaDoc.RecuperarDoc(i).getNombres());
            }
        }

        // 2. Combo de Especialidades (para RF-09)
        f.cbxEspecialidad.removeAllItems();
        f.cbxEspecialidad.addItem("TODAS"); // Opción para ver todas las especialidades
        ListaEspecialidades listaEsp = AlmacenarEspecialidades.Recuperar();
        listaEsp.ActualizarCantidadEsp();
        for (int i = 0; i < ListaEspecialidades.cantEsp; i++) {
            f.cbxEspecialidad.addItem(listaEsp.RecuperarEsp(i).getNom());
        }

        // 3. Autocompletar fechas con el día actual
        String hoy = ProcesosHistorias.FechaActual();
        f.txtFechaDia.setText(hoy);
        f.txtFechaInicio.setText(hoy);
        f.txtFechaFin.setText(hoy);
    }

    // Convierte un texto dd/MM/yyyy (o d/M/yyyy) a LocalDate para comparar rangos de fechas
    public static LocalDate ConvertirFecha(String textoFecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(textoFecha.trim(), fmt);
    }

    // Muestra el contenido de la Pila en orden LIFO (de la cima hacia la base) y su resumen
    public static void MostrarPila(JTable tabla, PilaAtenciones pilaAt, frmReportes f) {
        String titulos[] = {
            "Nro", "N° Historia", "DNI", "Paciente",
            "Especialidad", "Doctor", "Fecha", "Prioridad", "Diagnóstico", "Tratamiento"
        };
        DefaultTableModel mt = new DefaultTableModel(null, titulos);
        tabla.setModel(mt);

        Stack<HistoriaClinica> st = pilaAt.getPila();
        int cont = 0;
        // Recorremos desde el último índice hasta 0 para visualizar el orden LIFO (cima arriba)
        for (int i = st.size() - 1; i >= 0; i--) {
            cont++;
            mt.addRow(st.get(i).Registro(cont));
        }

        // Resumen estadístico en el JTextArea
        if (pilaAt.EstaVacia()) {
            f.txaResumenPila.setText("No existen atenciones en la pila para el médico y fecha indicados.");
        } else {
            HistoriaClinica primera = pilaAt.VerPrimera();
            HistoriaClinica ultima = pilaAt.VerUltima();
            f.txaResumenPila.setText(
                "Total de atenciones del médico en el día : " + pilaAt.Cantidad() + "\n" +
                "Primera atención del día (Base)          : " + primera.getNumHistoria() + " - " + primera.getNombrePaciente() + "\n" +
                "Última atención registrada (Cima / Peek) : " + ultima.getNumHistoria() + " - " + ultima.getNombrePaciente()
            );
        }
    }

    // Muestra la Cola de Prioridad respetando el orden del Comparador y genera su resumen
    public static void MostrarCola(JTable tabla, ColaAtenciones colaAt, frmReportes f) {
        String titulos[] = {
            "Nro", "N° Historia", "DNI", "Paciente",
            "Especialidad", "Doctor", "Fecha", "Prioridad", "Diagnóstico", "Tratamiento"
        };
        DefaultTableModel mt = new DefaultTableModel(null, titulos);
        tabla.setModel(mt);

        // Creamos una copia temporal para extraer con poll() en orden estricto de prioridad sin vaciar la original
        PriorityQueue<HistoriaClinica> copia = new PriorityQueue<>(new ComparadorAtenciones());
        copia.addAll(colaAt.getCola());

        int cont = 0, cEmerg = 0, cUrg = 0, cExt = 0;
        while (!copia.isEmpty()) {
            HistoriaClinica hc = copia.poll();
            cont++;
            mt.addRow(hc.Registro(cont));

            if (hc.getPrioridad().startsWith("1")) cEmerg++;
            else if (hc.getPrioridad().startsWith("2")) cUrg++;
            else cExt++;
        }

        if (colaAt.EstaVacia()) {
            f.txaResumenCola.setText("No existen atenciones en la cola para el rango de fechas y especialidad seleccionados.");
        } else {
            HistoriaClinica frente = colaAt.VerFrente();
            f.txaResumenCola.setText(
                "Total de atenciones en el rango          : " + colaAt.Cantidad() + "\n" +
                "   - Emergencias (Prioridad 1)           : " + cEmerg + "\n" +
                "   - Urgencias (Prioridad 2)             : " + cUrg + "\n" +
                "   - Consulta Externa (Prioridad 3)      : " + cExt + "\n" +
                "Atención prioritaria al frente (Peek)    : " + frente.getNumHistoria() + " - " + frente.getNombrePaciente() + " [" + frente.getPrioridad() + "]"
            );
        }
    }
    // Construye y muestra la vista previa de la Historia Clínica antes de exportar a PDF
    public static void MostrarVistaPreviaPDF(Paciente pac, ListaHistorias historial, frmReportes f) {
        StringBuilder sb = new StringBuilder();
        sb.append("==========================================================================\n");
        sb.append("                POSTA MÉDICA - HISTORIA CLÍNICA DEL PACIENTE              \n");
        sb.append("==========================================================================\n");
        sb.append("DNI          : ").append(pac.getDni()).append("\n");
        sb.append("Paciente     : ").append(pac.NombreCompleto()).append("\n");
        sb.append("Edad         : ").append(pac.getEdad() > 0 ? pac.getEdad() + " años" : "No registrado").append("\n");
        sb.append("Sexo         : ").append(pac.getSexo()).append("\n");
        sb.append("Teléfono     : ").append(pac.getTelefono()).append("\n");
        sb.append("Total visitas: ").append(historial.Cantidad()).append("\n");
        sb.append("--------------------------------------------------------------------------\n");

        if (historial.Cantidad() == 0) {
            sb.append("El paciente aún no registra atenciones médicas.\n");
        } else {
            NodoHistoria aux = historial.getIni();
            int nro = 0;
            while (aux != null) {
                nro++;
                HistoriaClinica hc = aux.getDato();
                sb.append("ATENCIÓN #").append(nro)
                        .append("  |  N° Historia: ").append(hc.getNumHistoria())
                        .append("  |  Fecha: ").append(hc.getFecha()).append("\n");
                sb.append("  Especialidad : ").append(hc.getEspecialidad()).append("\n");
                sb.append("  Médico       : ").append(hc.getDoctor()).append("\n");
                sb.append("  Prioridad    : ").append(hc.getPrioridad()).append("\n");
                sb.append("  Diagnóstico  : ").append(hc.getDiagnostico()).append("\n");
                sb.append("  Tratamiento  : ").append(hc.getTratamiento()).append("\n");
                sb.append("--------------------------------------------------------------------------\n");
                aux = aux.getSig();
            }
        }
        f.txaVistaPrevia.setText(sb.toString());
        f.txaVistaPrevia.setCaretPosition(0); // Ubica el scroll al inicio del texto
    }
}//fin de la clase