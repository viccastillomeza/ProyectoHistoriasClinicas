package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import Procesos.Mensajes;
import Vista.frmLogin;
import Vista.frmMenu;

public class ControlLogin implements ActionListener {

    frmLogin vista;

    public ControlLogin(frmLogin vista) {
        this.vista = vista;
        this.vista.btnIngresar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
    }//fin constructor

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnIngresar) {
            String user = vista.txtUsuario.getText().trim();
            String pass = vista.txtClave.getText().trim();

            // Credenciales de acceso a la Posta Médica 
            if (user.equalsIgnoreCase("admin") && pass.equals("1234")) {
                Mensajes.Mostrar("Bienvenido al Sistema de Historias Clínicas");
                vista.dispose(); // Cerramos el login

                // Abrimos el menú principal maximizado
                frmMenu fm = new frmMenu();
                fm.setTitle("SISTEMA DE REGISTRO DE HISTORIAS CLÍNICAS DE UNA POSTA");
                ControlMenu cm = new ControlMenu(fm);
                fm.setExtendedState(JFrame.MAXIMIZED_BOTH);
                fm.setVisible(true);
            } else {
                Mensajes.Mostrar("ERROR: Usuario o contraseña incorrectos");
                vista.txtClave.setText("");
                vista.txtUsuario.requestFocus();
            }
        }//fin btnIngresar

        if (e.getSource() == vista.btnSalir) {
            System.exit(0);
        }//fin btnSalir
    }//fin actionPerformed

}//fin de la clase