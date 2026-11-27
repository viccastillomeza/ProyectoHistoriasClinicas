package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import Vista.*;

public class ControlMenu implements ActionListener {

    frmMenu vista;

    public ControlMenu(frmMenu vista) {
        this.vista = vista;
        this.vista.itemPacientes.addActionListener(this);
        this.vista.itemDoctores.addActionListener(this);
        this.vista.itemEspecialidades.addActionListener(this);
        this.vista.itemHistorias.addActionListener(this);
        this.vista.itemReportes.addActionListener(this);
        this.vista.itemCerrarSesion.addActionListener(this);
    }//fin constructor

    // Método para limpiar el escritorio, agregar el JInternalFrame y centrarlo
    private void MostrarFrame(JInternalFrame iframe) {
        vista.dspEscritorio.removeAll();
        vista.dspEscritorio.add(iframe);
        iframe.setVisible(true);
        // Centra el JInternalFrame en medio de dspEscritorio
        int x = (vista.dspEscritorio.getWidth() - iframe.getWidth()) / 2;
        int y = (vista.dspEscritorio.getHeight() - iframe.getHeight()) / 2;
        iframe.setLocation(x, y);
        vista.dspEscritorio.repaint();
    }//fin metodo

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.itemEspecialidades) {
            frmEspecialidad fesp = new frmEspecialidad();
            fesp.setTitle("Acceso y registro de Especialidades Médicas");
            ControlEspecialidad cesp = new ControlEspecialidad(fesp);
            MostrarFrame(fesp);
        }
        if (e.getSource() == vista.itemDoctores) {
            frmDoctor fdoc = new frmDoctor();
            fdoc.setTitle("Acceso y registro de Doctores");
            ControlDoctor cdoc = new ControlDoctor(fdoc);
            MostrarFrame(fdoc);
        }
        if (e.getSource() == vista.itemPacientes) {
            frmPaciente fpac = new frmPaciente();
            fpac.setTitle("Registro y Mantenimiento de Pacientes");
            ControlPaciente cpac = new ControlPaciente(fpac);
            MostrarFrame(fpac);
        }
        if (e.getSource() == vista.itemHistorias) {
            frmHistoriaClinica fhc = new frmHistoriaClinica();
            fhc.setTitle("Registro y Mantenimiento de Historias Clínicas");
            ControlHistoria chc = new ControlHistoria(fhc);
            MostrarFrame(fhc);
        }
        if (e.getSource() == vista.itemReportes) {
            frmReportes frep = new frmReportes();
            frep.setTitle("Reportes y Consultas (Pilas, Colas y PDF)");
            ControlReportes crep = new ControlReportes(frep);
            MostrarFrame(frep);
        }
        if (e.getSource() == vista.itemCerrarSesion) {
            vista.dispose();
            frmLogin fl = new frmLogin();
            fl.setTitle("Acceso al Sistema - Posta Médica");
            fl.setLocationRelativeTo(null);
            fl.setVisible(true);
            ControlLogin cl = new ControlLogin(fl);
        }
    }//fin actionPerformed

}//fin de la clase