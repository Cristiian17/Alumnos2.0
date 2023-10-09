package view;

import controller.AlumnosDAO;
import model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class FileDialog extends JDialog {
    private JPanel contentPane;
    private DefaultTableModel dtmAlumnos;
    private JTable tabla;
    private ArrayList listaAlumnos;

    public FileDialog(List list) {
        listaAlumnos = (ArrayList) list;
        setContentPane(contentPane);
        setModal(true);
        dtmAlumnos = new DefaultTableModel();
        tabla.setModel(dtmAlumnos);
        cargarTabla(listaAlumnos);
    }

    private void cargarTabla(List<Alumno> list) {
        dtmAlumnos.addColumn("Apellidos");
        dtmAlumnos.addColumn("Nombre");
        dtmAlumnos.addColumn("Email");
        dtmAlumnos.addColumn("Poblacion");
        dtmAlumnos.addColumn("Telefono");
        dtmAlumnos.addColumn("ciclo");
        dtmAlumnos.addColumn("ordenador");
        dtmAlumnos.addColumn("carnet");
        dtmAlumnos.addColumn("Estudios");
        dtmAlumnos.addColumn("Fecha nacimiento");
        dtmAlumnos.addColumn("Motivacion");
        dtmAlumnos.addColumn("Hobbies");
        list.forEach( a -> dtmAlumnos.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
