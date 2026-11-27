package Modelo;

import java.io.Serializable;

public class HistoriaClinica implements Serializable {

    private String numHistoria;     // Ej: HC-001 (clave única de la atención)
    private String dniPaciente;
    private String nombrePaciente;
    private String especialidad;
    private String doctor;
    private String fecha;           // Formato dd/MM/yyyy (necesario para RF-08 y RF-09)
    private String prioridad;       // 1-EMERGENCIA, 2-URGENCIA, 3-CONSULTA EXTERNA
    private String diagnostico;     // Detalle de la atención médica
    private String tratamiento;     // Receta o indicaciones

    public HistoriaClinica() {
    }

    // Devuelve los datos formateados como fila para el JTable
    public Object[] Registro(int num) {
        Object[] fila = {
            num, numHistoria, dniPaciente, nombrePaciente,
            especialidad, doctor, fecha, prioridad, diagnostico, tratamiento
        };
        return fila;
    }

    // Getters y Setters
    public String getNumHistoria() { return numHistoria; }
    public void setNumHistoria(String numHistoria) { this.numHistoria = numHistoria; }

    public String getDniPaciente() { return dniPaciente; }
    public void setDniPaciente(String dniPaciente) { this.dniPaciente = dniPaciente; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
}//fin de la clase