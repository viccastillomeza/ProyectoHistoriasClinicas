package Procesos;

import Estructura.ListaHistorias;
import Estructura.NodoHistoria;
import Modelo.HistoriaClinica;
import Modelo.Paciente;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GeneradorPDF {

    private final List<StringBuilder> paginas;
    private StringBuilder paginaActual;
    private int y; // Posición vertical actual en la hoja A4 (842 pt de alto)

    private GeneradorPDF() {
        this.paginas = new ArrayList<>();
        this.paginaActual = new StringBuilder();
        this.paginas.add(paginaActual);
        this.y = 790;
    }

    // Método principal estático que invoca el Controlador para crear el archivo PDF
    public static void CrearHistoriaClinicaPDF(Paciente pac, ListaHistorias historial, File archivoDestino) throws IOException {
        GeneradorPDF pdf = new GeneradorPDF();
        pdf.construirContenido(pac, historial);
        pdf.guardarArchivo(archivoDestino);
    }

    // Diseña el contenido visual del documento (encabezado, datos del paciente y atenciones)
    private void construirContenido(Paciente pac, ListaHistorias historial) {
        // 1. Franja azul de encabezado institucional
        paginaActual.append("0.08 0.28 0.55 rg 50 752 495 50 re f\n");
        paginaActual.append("1 1 1 rg\n"); // Texto blanco
        escribirTexto("F2", 15, 65, 780, "POSTA MEDICA - HISTORIA CLINICA DEL PACIENTE");
        escribirTexto("F1", 10, 65, 762, "Fecha de emision: " + ProcesosHistorias.FechaActual()
                + "   |   Total de atenciones registradas: " + historial.Cantidad());
        paginaActual.append("0 0 0 rg\n"); // Volvemos a texto negro
        y = 730;

        // 2. Sección de Datos del Paciente
        dibujarBannerSeccion("1. DATOS GENERALES DEL PACIENTE");
        escribirTexto("F2", 10, 60, y, "DNI:");
        escribirTexto("F1", 10, 145, y, pac.getDni());
        escribirTexto("F2", 10, 310, y, "Edad:");
        escribirTexto("F1", 10, 365, y, (pac.getEdad() > 0 ? pac.getEdad() + " años" : "No registrado"));
        y -= 18;

        escribirTexto("F2", 10, 60, y, "Paciente:");
        escribirTexto("F1", 10, 145, y, pac.NombreCompleto());
        y -= 18;

        escribirTexto("F2", 10, 60, y, "Sexo:");
        escribirTexto("F1", 10, 145, y, pac.getSexo());
        escribirTexto("F2", 10, 310, y, "Teléfono:");
        escribirTexto("F1", 10, 365, y, pac.getTelefono());
        y -= 25;

        // 3. Sección de Atenciones Médicas (recorriendo la Lista Enlazada)
        dibujarBannerSeccion("2. DETALLE DE ATENCIONES MEDICAS REGISTRADAS");

        if (historial.Cantidad() == 0) {
            escribirTexto("F1", 10, 60, y, "El paciente no cuenta con atenciones médicas registradas hasta la fecha.");
        } else {
            NodoHistoria aux = historial.getIni();
            int nro = 0;
            while (aux != null) {
                nro++;
                HistoriaClinica hc = aux.getDato();

                // Verificamos si queda espacio suficiente en la hoja antes de imprimir el bloque
                verificarEspacio(110);

                // Cabecera de cada atención
                paginaActual.append("0.94 0.96 0.98 rg 50 ").append(y - 5).append(" 495 18 re f\n");
                paginaActual.append("0 0 0 rg\n");
                escribirTexto("F2", 10, 58, y, "ATENCIÓN #" + nro + "  |  N° Historia: " + hc.getNumHistoria()
                        + "  |  Fecha: " + hc.getFecha());
                y -= 20;

                escribirTexto("F2", 10, 60, y, "Especialidad:");
                escribirTexto("F1", 10, 145, y, hc.getEspecialidad());
                escribirTexto("F2", 10, 310, y, "Prioridad:");
                escribirTexto("F1", 10, 370, y, hc.getPrioridad());
                y -= 16;

                escribirTexto("F2", 10, 60, y, "Médico:");
                escribirTexto("F1", 10, 145, y, hc.getDoctor());
                y -= 16;

                // Diagnóstico con salto de línea automático para textos largos
                escribirCampoLargo("Diagnóstico:", hc.getDiagnostico());
                // Tratamiento con salto de línea automático
                escribirCampoLargo("Tratamiento:", hc.getTratamiento());

                y -= 6;
                dibujarLinea(50, y, 545, y);
                y -= 18;

                aux = aux.getSig();
            }
        }
    }

    // Dibuja un subtítulo sombreado para separar secciones
    private void dibujarBannerSeccion(String titulo) {
        verificarEspacio(45);
        paginaActual.append("0.85 0.90 0.96 rg 50 ").append(y - 6).append(" 495 20 re f\n");
        paginaActual.append("0.08 0.28 0.55 rg\n");
        escribirTexto("F2", 11, 58, y, titulo);
        paginaActual.append("0 0 0 rg\n");
        y -= 24;
    }

    // Divide textos largos en varias líneas para que no se salgan del margen derecho
    private void escribirCampoLargo(String etiqueta, String valor) {
        List<String> lineas = dividirLineas(valor, 68);
        for (int i = 0; i < lineas.size(); i++) {
            verificarEspacio(18);
            if (i == 0) {
                escribirTexto("F2", 10, 60, y, etiqueta);
            }
            escribirTexto("F1", 10, 145, y, lineas.get(i));
            y -= 15;
        }
    }

    // Crea una nueva hoja A4 si el contenido llega cerca del borde inferior
    private void verificarEspacio(int espacioNecesario) {
        if (y - espacioNecesario < 60) {
            paginaActual = new StringBuilder();
            paginas.add(paginaActual);
            y = 790;
            escribirTexto("F2", 10, 50, y, "POSTA MÉDICA - HISTORIA CLÍNICA (CONTINUACIÓN)");
            y -= 10;
            dibujarLinea(50, y, 545, y);
            y -= 24;
        }
    }

    // Escribe una línea de texto en coordenadas (x, y) usando F1 (Normal) o F2 (Negrita)
    private void escribirTexto(String fuente, int tamano, int x, int posY, String texto) {
        paginaActual.append("BT /").append(fuente).append(" ").append(tamano)
                .append(" Tf ").append(x).append(" ").append(posY)
                .append(" Td (").append(escaparTexto(texto)).append(") Tj ET\n");
    }

    // Dibuja una línea horizontal gris divisoria
    private void dibujarLinea(int x1, int y1, int x2, int y2) {
        paginaActual.append("0.75 0.75 0.75 RG 0.8 w ")
                .append(x1).append(" ").append(y1).append(" m ")
                .append(x2).append(" ").append(y2).append(" l S\n");
    }

    // Escapa paréntesis y barras invertidas para no dañar la sintaxis interna del PDF
    private String escaparTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("\r", " ")
                .replace("\n", " ");
    }

    // Parte una cadena larga respetando las palabras
    private List<String> dividirLineas(String texto, int maxChars) {
        List<String> resultado = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            resultado.add("-");
            return resultado;
        }
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder linea = new StringBuilder();
        for (String p : palabras) {
            if (linea.length() + p.length() + 1 > maxChars) {
                if (linea.length() > 0) {
                    resultado.add(linea.toString());
                    linea = new StringBuilder();
                }
            }
            if (linea.length() > 0) {
                linea.append(" ");
            }
            linea.append(p);
        }
        if (linea.length() > 0) {
            resultado.add(linea.toString());
        }
        return resultado;
    }

    // Ensambla la estructura binaria oficial PDF 1.4 (objetos, fuentes, tabla xref y trailer)
    private void guardarArchivo(File archivoDestino) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();

        escribirBytes(baos, "%PDF-1.4\n");

        int totalPaginas = paginas.size();
        int totalObjetos = 4 + (totalPaginas * 2);

        // Objeto 1: Catálogo raíz del documento
        offsets.add(baos.size());
        escribirBytes(baos, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        // Objeto 2: Árbol de páginas
        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < totalPaginas; i++) {
            int idPagina = 5 + (i * 2);
            kids.append(idPagina).append(" 0 R ");
        }
        offsets.add(baos.size());
        escribirBytes(baos, "2 0 obj\n<< /Type /Pages /Kids [ " + kids.toString()
                + "] /Count " + totalPaginas + " >>\nendobj\n");

        // Objeto 3: Fuente F1 Helvetica Normal con codificación WinAnsi (soporta tildes y ñ)
        offsets.add(baos.size());
        escribirBytes(baos, "3 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj\n");

        // Objeto 4: Fuente F2 Helvetica Negrita
        offsets.add(baos.size());
        escribirBytes(baos, "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>\nendobj\n");

        // Objetos de cada hoja (Página A4 + Flujo de contenido)
        for (int i = 0; i < totalPaginas; i++) {
            int idPagina = 5 + (i * 2);
            int idContenido = 6 + (i * 2);
            byte[] streamBytes = paginas.get(i).toString().getBytes(StandardCharsets.ISO_8859_1);

            offsets.add(baos.size());
            escribirBytes(baos, idPagina + " 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                    + "/Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents " + idContenido + " 0 R >>\nendobj\n");

            offsets.add(baos.size());
            escribirBytes(baos, idContenido + " 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n");
            baos.write(streamBytes);
            escribirBytes(baos, "\nendstream\nendobj\n");
        }

        // Tabla de referencias cruzadas (xref) con el byte exacto de cada objeto
        int inicioXref = baos.size();
        escribirBytes(baos, "xref\n0 " + (totalObjetos + 1) + "\n");
        escribirBytes(baos, "0000000000 65535 f \n");
        for (int off : offsets) {
            escribirBytes(baos, String.format("%010d 00000 n \n", off));
        }

        // Cierre del documento PDF
        escribirBytes(baos, "trailer\n<< /Size " + (totalObjetos + 1) + " /Root 1 0 R >>\n");
        escribirBytes(baos, "startxref\n" + inicioXref + "\n%%EOF\n");

        try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
            baos.writeTo(fos);
        }
    }

    private void escribirBytes(ByteArrayOutputStream baos, String texto) throws IOException {
        baos.write(texto.getBytes(StandardCharsets.ISO_8859_1));
    }
}//fin de la clase