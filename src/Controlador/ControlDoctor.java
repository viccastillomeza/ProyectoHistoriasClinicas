package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Estructura.ListaDoctores;
import Modelo.Doctor;
import Persistencia.AlmacenarDoctores;
import Procesos.Mensajes;
import Procesos.ProcesosDoctores;
import Vista.frmDoctor;

public class ControlDoctor implements ActionListener {

    frmDoctor vista;
    ListaDoctores Lista;
    int pos = -1;

    public ControlDoctor(frmDoctor f) {
        this.vista = f;
        this.Lista = new ListaDoctores();
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnConsultar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);

        ProcesosDoctores.CargarEspecialidades(vista);
        ActualizarVista();
    }

    private void ActualizarVista() {
        Lista = AlmacenarDoctores.RecuperarListaDoc();
        ProcesosDoctores.MostrarDatos(vista.tblDatos, Lista);
        ProcesosDoctores.LimpiarEntradas(vista);
        pos = -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            Doctor doc = ProcesosDoctores.LeerDoctor(vista);
            Lista.AgregarDoc(doc);
            AlmacenarDoctores.GuardarListaDoc(Lista);
            Mensajes.Mostrar("Doctor registrado correctamente.");
            ActualizarVista();
        }

        if (e.getSource() == vista.btnConsultar) {
            String buscado = Mensajes.Leer("Ingrese Código CMP o DNI del doctor a buscar:");
            if (buscado != null) {
                pos = Lista.BuscarDoc(buscado);
                if (pos == -1) {
                    Mensajes.Mostrar("El doctor con dato " + buscado + " no existe en la lista.");
                } else {
                    ProcesosDoctores.MostrarUnDoctor(Lista.RecuperarDoc(pos), vista);
                }
            }
        }

        if (e.getSource() == vista.btnActualizar) {
            if (pos != -1) {
                Doctor nuevodoc = ProcesosDoctores.LeerDoctor(vista);
                Lista.ActualizarDoc(pos, nuevodoc);
                AlmacenarDoctores.GuardarListaDoc(Lista);
                Mensajes.Mostrar("Datos del doctor actualizados.");
                ActualizarVista();
            } else {
                Mensajes.Mostrar("Verifique la consulta primero antes de actualizar...");
            }
        }

        if (e.getSource() == vista.btnEliminar) {
            if (pos != -1) {
                int resp = Mensajes.Preguntar("Confirmar!!", "¿Desea eliminar el registro del doctor?");
                if (resp == 0) {
                    Lista.EliminarDoc(pos);
                    AlmacenarDoctores.GuardarListaDoc(Lista);
                    Mensajes.Mostrar("Registro de doctor eliminado!!");
                    ActualizarVista();
                }
            } else {
                Mensajes.Mostrar("Verifique la consulta primero antes de eliminar...");
            }
        }
    }
}//fin clase