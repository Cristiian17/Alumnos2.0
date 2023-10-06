package view;

import controller.AlumnosDAO;
import model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.*;

public class Ventana {
    private JPanel panel;
    private JButton btnCargarDatos;
    private JTable table1;
    private JButton btnGuardarDAT;
    private JButton anadirAlumnoButton;
    private JTextField txtNombreArchivo;
    private JComboBox cbConsultas2;
    private JComboBox cbConsultas;
    private JButton btnConsultar;
    private JButton btnBorrarAlumno;
    private JButton editarAlumnoButton;
    private JButton exportarAXMLButton;
    private JButton DATAJSONButton;
    private JButton DATACSVButton;
    private JButton DATAPDFButton;
    private DefaultComboBoxModel<String> dcbmConsultas;
    private final DefaultComboBoxModel<String> dcbm;
    private DefaultTableModel dtm;
    private ArrayList<Alumno> listaAlumnos;
    private ArrayList<Alumno> alumnosConsulta;
public Ventana() {
    listaAlumnos = new ArrayList<Alumno>();


    dcbm = new DefaultComboBoxModel();
    cbConsultas2.setModel(dcbm);

    dcbmConsultas = new DefaultComboBoxModel<>();
    cbConsultas.setModel(dcbmConsultas);

    dcbmConsultas.addElement("");
    dcbmConsultas.addElement("localidad");
    dcbmConsultas.addElement("carnet");
    dcbmConsultas.addElement("ordenador");
    cbConsultas.addActionListener(e -> generarFiltros());
    btnBorrarAlumno.addActionListener(e -> deleteAlumno(alumnosConsulta.get(table1.getSelectedRow())));
    btnCargarDatos.addActionListener(e -> cargarTabla());
    anadirAlumnoButton.addActionListener(e -> addAlumno());
    btnGuardarDAT.addActionListener(e -> save(alumnosConsulta));
    btnConsultar.addActionListener(e -> consult());

    editarAlumnoButton.addActionListener(e -> {
        Alumno a = alumnosConsulta.get(table1.getSelectedRow());
        try {
            editAlumno(a);
        } catch (ParseException ex) {
            System.out.println("fallo al convertir la fecha, formato incorrecto \n Formato correcto 'Wed Aug dd hh:mm:ss CEST YYYY'");
        }
    });
    exportarAXMLButton.addActionListener(e -> exportarDatAXml());
    DATAJSONButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            exportarDatAJson();
        }
    });
    DATACSVButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            exportarDatACsv();
        }
    });
    DATAPDFButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            exportarDatAPdf();
        }
    });
}

    private void exportarDatAPdf(){
        ArrayList<Alumno> alumnos = AlumnosDAO.leerArrayListDesdeArchivoBinario("alumnos.dat");
        AlumnosDAO.saveOnPDF(alumnos,"alumnos.pfd");

    }
    private void exportarDatACsv(){
        ArrayList<Alumno> alumnos = AlumnosDAO.leerArrayListDesdeArchivoBinario("alumnos.dat");
        AlumnosDAO.saveOnCSV(alumnos,"alumnos2.csv");

    }
    private void exportarDatAJson(){
        ArrayList<Alumno> alumnos = AlumnosDAO.leerArrayListDesdeArchivoBinario("alumnos.dat");
        AlumnosDAO.saveOnJSON(alumnos, "alumnos.json");

    }
    private void exportarDatAXml(){
        ArrayList<Alumno> alumnos = AlumnosDAO.leerArrayListDesdeArchivoBinario("alumnos.dat");
        AlumnosDAO.saveOnXML(alumnos,"alumnos.xml");

    }
    private void editAlumno(Alumno a) throws ParseException {
        AddDialog d = new AddDialog();
        d.setAlumno(a);
        d.setVisible(true);
        Alumno al = d.getAlumno();
        a.setFechaNacimiento(al.getFechaNacimiento());
        a.setNombre(al.getNombre());
        a.setApellidos(al.getApellidos());
        a.setHobbies(al.getHobbies());
        a.setEstudios(al.getEstudios());
        a.setOrdenador(al.getOrdenador());
        a.setCiclo(al.getCiclo());
        a.setPoblacion(al.getPoblacion());
        a.setSiCarnet(al.isSiCarnet());
        a.setTelefono(al.getTelefono());
        actualizarTabla();
    }
    private void actualizarTabla() {
        dtm.setRowCount(0);
        alumnosConsulta.forEach(a -> dtm.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));
    }

    private void generarFiltros() {
        dcbm.removeAllElements();
        if (dcbmConsultas.getSelectedItem().equals("localidad")){
            listaAlumnos.forEach( a -> {
                if (dcbm.getIndexOf(a.getPoblacion()) == -1) {
                    dcbm.addElement(a.getPoblacion());
                }
            });
        } else if (dcbmConsultas.getSelectedItem().equals("carnet")) {
            dcbm.addElement("si");
            dcbm.addElement("no");
        } else if (dcbmConsultas.getSelectedItem().equals("ordenador")) {
            dcbm.addElement("uno");
            dcbm.addElement("dos");
            dcbm.addElement("tres");
            dcbm.addElement("cuatro");
            dcbm.addElement("cinco");
        }
    }

    private void deleteAlumno(Alumno a) {
        listaAlumnos.remove(a);
        alumnosConsulta.remove(a);
        actualizarTabla();
    }

    private void consult(){
        String consulta = dcbmConsultas.getSelectedItem().toString();
        String consulta2 = dcbm.getSelectedItem().toString();
        alumnosConsulta = new ArrayList<>();
        switch (consulta) {
            case "localidad" -> listaAlumnos.forEach(alumno -> {
                if (alumno.getPoblacion().equals(consulta2)) {
                    alumnosConsulta.add(alumno);
                }
            });
            case "ordenador" -> listaAlumnos.forEach(alumno -> {
                if (alumno.getOrdenador().toString().equals(consulta2)) {
                    alumnosConsulta.add(alumno);
                }
            });
            case "carnet" -> {
                boolean carnet;
                if (consulta2.equals("si")) {
                    carnet = true;
                } else {
                    carnet = false;
                }
                listaAlumnos.forEach(alumno -> {
                    if (alumno.isSiCarnet() == carnet) {
                        alumnosConsulta.add(alumno);
                    }
                });
            }
        }
        dtm.setRowCount(0);
        alumnosConsulta.forEach(a -> dtm.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));

    }
    private void save(ArrayList<Alumno> listaAlumnos){
        AlumnosDAO dao = new AlumnosDAO();
        String nombreFichero = txtNombreArchivo.getText();
        if(!nombreFichero.isEmpty()){
            dao.saveAlumnosDAT(listaAlumnos,nombreFichero+".dat");
        }
    }
    private void cargarTabla() {
        dtm = new DefaultTableModel();
        table1.setModel(dtm);

        dtm.addColumn("apellidos");
        dtm.addColumn("Nombre");
        dtm.addColumn("Email");
        dtm.addColumn("Poblacion");
        dtm.addColumn("Telefono");
        dtm.addColumn("ciclo");
        dtm.addColumn("ordenador");
        dtm.addColumn("carnet");
        dtm.addColumn("Estudios");
        dtm.addColumn("Fecha nacimiento");
        dtm.addColumn("Motivacion");
        dtm.addColumn("Hobbies");

        AlumnosDAO dao = new AlumnosDAO();
        listaAlumnos = (ArrayList) dao.getAlumnosCSV();
        alumnosConsulta = listaAlumnos;
        listaAlumnos.forEach( a -> dtm.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
        a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));
    }
    private void addAlumno() {
        AddDialog d = new AddDialog();
        d.setVisible(true);
        if (d.getAlumno() != null){
            Alumno a = d.getAlumno();
            listaAlumnos.add(a);
            dtm.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                    a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()});
        }
        actualizarTabla();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
