package Modelo;

import java.io.Serializable;

public class Doctor implements Serializable {

    private String codCmp;       // Código de Colegio Médico
    private String dni;
    private String nombres;
    private String especialidad;
    private String turno;        // MAÑANA o TARDE

    public Doctor() {
    }

    public Object[] Registro(int num) {
        Object[] fila = {num, codCmp, dni, nombres, especialidad, turno};
        return fila;
    }

    public String getCodCmp() {
        return codCmp;
    }

    public void setCodCmp(String codCmp) {
        this.codCmp = codCmp;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}//fin clase