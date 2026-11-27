package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Estructura.ListaEspecialidades;
import Modelo.Especialidad;
import Persistencia.AlmacenarEspecialidades;
import Procesos.Mensajes;
import Procesos.ProcesosEspecialidades;
import Vista.frmEspecialidad;

public class ControlEspecialidad implements ActionListener {

    frmEspecialidad vista;
    ListaEspecialidades Lista;
    int pos = -1; // Guarda la posición buscada para actualizar

    public ControlEspecialidad(frmEspecialidad f) {
        this.vista = f;
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnConsultar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);

        Lista = new ListaEspecialidades(30);
        ActualizarVista();
    }

    private void ActualizarVista() {
        Lista = AlmacenarEspecialidades.Recuperar();
        Lista.ActualizarCantidadEsp();
        ProcesosEspecialidades.MostrarDatos(vista.tblDatos, Lista.getLista(), ListaEspecialidades.cantEsp);
        ProcesosEspecialidades.LimpiarEntradas(vista);
        pos = -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            Especialidad esp = ProcesosEspecialidades.LeerEspecialidad(vista);
            Lista.AgregarEsp(esp);
            AlmacenarEspecialidades.Guardar(Lista);
            Mensajes.Mostrar("Especialidad médica registrada correctamente.");
            ActualizarVista();
        }

        if (e.getSource() == vista.btnConsultar) {
            String codbuscado = Mensajes.Leer("Ingrese el código de especialidad a buscar:");
            if (codbuscado != null) {
                pos = Lista.BuscarEsp(codbuscado);
                if (pos == -1) {
                    Mensajes.Mostrar("El código " + codbuscado + " no existe.");
                } else {
                    ProcesosEspecialidades.MostrarUnaEspecialidad(Lista.RecuperarEsp(pos), vista);
                }
            }
        }

        if (e.getSource() == vista.btnActualizar) {
            if (pos != -1) {
                Especialidad nuevaEsp = ProcesosEspecialidades.LeerEspecialidad(vista);
                Lista.ActualizarEsp(pos, nuevaEsp);
                AlmacenarEspecialidades.Guardar(Lista);
                Mensajes.Mostrar("Datos de la especialidad actualizados.");
                ActualizarVista();
            } else {
                Mensajes.Mostrar("Primero consulte una especialidad para actualizarla.");
            }
        }

        if (e.getSource() == vista.btnEliminar) {
            String codbuscado = Mensajes.Leer("Ingrese el código de especialidad a eliminar:");
            if (codbuscado != null) {
                if (Lista.BuscarEsp(codbuscado) == -1) {
                    Mensajes.Mostrar("El código " + codbuscado + " no existe.");
                } else {
                    int resp = Mensajes.Preguntar("Confirmar!!", "¿Desea eliminar la especialidad " + codbuscado + "?");
                    if (resp == 0) {
                        Lista.EliminarEsp(codbuscado);
                        AlmacenarEspecialidades.Guardar(Lista);
                        Mensajes.Mostrar("Especialidad eliminada.");
                        ActualizarVista();
                    }
                }
            }
        }
    }
}//fin clase