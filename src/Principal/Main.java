package Principal;

import Controlador.ControlLogin;
import Vista.frmLogin;

public class Main {

    public static void main(String[] args) {
        frmLogin fl = new frmLogin();
        fl.setTitle("ACCESO AL SISTEMA - POSTA MÉDICA");
        fl.setLocationRelativeTo(null);
        fl.setVisible(true);
        ControlLogin cl = new ControlLogin(fl);
    }//fin main

}//fin class