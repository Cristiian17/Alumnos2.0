package view;

import controller.AlumnosDAO;
import model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Ventana {
    private JPanel panel;
    private JComboBox cbConsultas2, cbConsultas;
    private JTextField txtNombreArchivo;
    private JButton btnCargarDatos;

    private JButton btnGuardarDAT;
    private JButton anadirAlumnoButton;
    private JButton btnConsultar;
    private JButton btnBorrarAlumno;
    private JButton editarAlumnoButton;
    private JButton exportarAXMLButton;
    private JButton DATAJSONButton;
    private JButton DATACSVButton;
    private JButton DATAPDFButton;
    private JButton persistirButton;
    private JButton cargarTablaButton;
    private JTable tablaAlumnos;
    private JTable tablaFicheros;
    private JButton DATAHTMLButton;
    private final DefaultTableModel dtmFicheros, dtmAlumnos;
    private final DefaultComboBoxModel<String> dcbmConsultas, dcbm;
    private ArrayList<Alumno> listaAlumnos, alumnosConsulta;
public Ventana() {
    listaAlumnos = new ArrayList<>();

    dtmAlumnos = new DefaultTableModel();
    tablaAlumnos.setModel(dtmAlumnos);

    dtmFicheros = new DefaultTableModel();
    tablaFicheros.setModel(dtmFicheros);

    dcbm = new DefaultComboBoxModel<>();
    cbConsultas2.setModel(dcbm);
    dcbmConsultas = new DefaultComboBoxModel<>();
    cbConsultas.setModel(dcbmConsultas);
    dcbmConsultas.addElement("");
    dcbmConsultas.addElement("localidad");
    dcbmConsultas.addElement("carnet");
    dcbmConsultas.addElement("ordenador");

    cbConsultas.addActionListener(e -> generateFilters());
    btnBorrarAlumno.addActionListener(e -> deleteAlumno(tablaAlumnos.getSelectedRow()));
    btnCargarDatos.addActionListener(e -> chargeTable());
    anadirAlumnoButton.addActionListener(e -> addAlumno());

    btnGuardarDAT.addActionListener(e -> saveConsult(alumnosConsulta));
    btnConsultar.addActionListener(e -> consult());
    exportarAXMLButton.addActionListener(e -> exportarDatAXml());
    DATAJSONButton.addActionListener(e -> exportarDatAJson());
    DATACSVButton.addActionListener(e -> exportarDatACsv());
    DATAPDFButton.addActionListener(e -> exportarDatAPdf());
    persistirButton.addActionListener(e -> save(listaAlumnos));
    cargarTablaButton.addActionListener(e -> recargarTabla());
    DATAHTMLButton.addActionListener(e -> exportarDatAHtml());
    editarAlumnoButton.addActionListener(e -> editAlumno(tablaAlumnos.getSelectedRow()));
    tablaFicheros.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            showContent();
        }
    });
    chargeFiles();
    cargar();

}

    private void showContent(){
        int row = tablaFicheros.getSelectedRow();
        String nombreArchivo = (String) dtmFicheros.getValueAt(row,0);
        int lastIndex = nombreArchivo.lastIndexOf(".");

        if (lastIndex != -1) {
            // Extraer la extensión a partir del último punto
            String extension = nombreArchivo.substring(lastIndex + 1);
            // Imprimir la extensión
            ArrayList<Alumno> list = new ArrayList<>();
            switch (extension){
                case "csv" -> list = (ArrayList<Alumno>) AlumnosDAO.readCsv("./files/" + nombreArchivo);
                case "dat" -> list = AlumnosDAO.readDat("./files/" + nombreArchivo);
                case "json" -> list = (ArrayList<Alumno>) AlumnosDAO.readJson("./files/" + nombreArchivo);
                case "xml" -> list = (ArrayList<Alumno>) AlumnosDAO.readXml("./files/" + nombreArchivo) ;
                case "pdf" -> list = (ArrayList<Alumno>) AlumnosDAO.readPdf("./files/" + nombreArchivo);
                case "html" -> openHTMLInChrome("./files/" + nombreArchivo);
            }
            if (!extension.equals("html")){
                FileDialog d = new FileDialog(list);
                d.setSize(1400,500);
                d.setTitle(nombreArchivo);
                d.setLocationRelativeTo(null);
                d.setVisible(true);
            }

        }
    }
    public static void openHTMLInChrome(String nombreArchivo) {
        try {
            File htmlFile = new File(nombreArchivo);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (htmlFile.exists()) {
                    desktop.open(htmlFile);
                } else {
                    System.out.println("El archivo HTML no existe.");
                }
            } else {
                System.out.println("El sistema no admite la clase Desktop.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargeFiles(){
        ArrayList listFiles = (ArrayList) AlumnosDAO.getFileNames("./files/");
        dtmFicheros.setRowCount(0);
        dtmFicheros.setColumnCount(0);
        dtmFicheros.addColumn("Ficheros");
        listFiles.forEach(a -> dtmFicheros.addRow(new Object[]{a.toString()}));

    }
    private void cargar(){
        dtmAlumnos.addColumn("Apellidos");
        dtmAlumnos.addColumn("Nombre");
        dtmAlumnos.addColumn("Email");
        dtmAlumnos.addColumn("Poblacion");
        dtmAlumnos.addColumn("Telefono");
        dtmAlumnos.addColumn("Ciclo");
        dtmAlumnos.addColumn("Ordenador");
        dtmAlumnos.addColumn("Carnet");
        dtmAlumnos.addColumn("Estudios");
        dtmAlumnos.addColumn("Fecha nacimiento");
        dtmAlumnos.addColumn("Motivacion");
        dtmAlumnos.addColumn("Hobbies");
        listaAlumnos.addAll(AlumnosDAO.readDat("./files/alumnos.dat"));
        alumnosConsulta = listaAlumnos;
        updateTable();
    }

    private void recargarTabla(){
        alumnosConsulta = listaAlumnos;
        updateTable();
    }
    private void exportarDatAPdf(){
        ArrayList<Alumno> alumnos = AlumnosDAO.readDat("./files/alumnos.dat");
        try {
            AlumnosDAO.saveOnPDF(alumnos,"./files/alumnos.pdf");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void exportarDatACsv(){
        ArrayList<Alumno> alumnos = AlumnosDAO.readDat("./files/alumnos.dat");
        try {
            AlumnosDAO.saveOnCSV(alumnos,"./files/alumnos2.csv");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void exportarDatAJson(){
        ArrayList<Alumno> alumnos = AlumnosDAO.readDat("./files/alumnos.dat");
        AlumnosDAO.saveOnJSON(alumnos, "./files/alumnos.json");
    }
    private void exportarDatAHtml(){
        ArrayList<Alumno> alumnos = AlumnosDAO.readDat("./files/alumnos.dat");
        AlumnosDAO.saveOnHTML(alumnos);
    }
    private void exportarDatAXml(){
        ArrayList<Alumno> alumnos = AlumnosDAO.readDat("./files/alumnos.dat");
        AlumnosDAO.saveOnXML(alumnos,"./files/alumnos.xml");
    }
    private void editAlumno(int alu){
        if(alu < 0){
            return;
        }
        Alumno a = alumnosConsulta.get(alu);
        AddDialog d = new AddDialog(false);
        d.setLocationRelativeTo(null);
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
        updateTable();
    }
    private void updateTable() {
        dtmAlumnos.setRowCount(0);
        alumnosConsulta.forEach(a -> dtmAlumnos.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));
    }

    private void generateFilters() {
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

    private void deleteAlumno(int num) {
        if(num < 0){
            return;
        }
        Alumno a = alumnosConsulta.get(num);
        listaAlumnos.remove(a);
        alumnosConsulta.remove(a);
        updateTable();
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
                carnet = consulta2.equals("si");
                listaAlumnos.forEach(alumno -> {
                    if (alumno.isSiCarnet() == carnet) {
                        alumnosConsulta.add(alumno);
                    }
                });
            }
        }
        dtmAlumnos.setRowCount(0);
        alumnosConsulta.forEach(a -> dtmAlumnos.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));

    }
    private void saveConsult(ArrayList<Alumno> listaAlumnos){
        String nombreFichero = txtNombreArchivo.getText();
        if(!nombreFichero.isEmpty()){
            AlumnosDAO.saveAlumnosDAT(listaAlumnos,"./files/"+nombreFichero+".dat");
        }
        chargeFiles();
    }
    private void save(ArrayList<Alumno> listaAlumnos){
        if(!listaAlumnos.isEmpty()){
            AlumnosDAO.saveAlumnosDAT(listaAlumnos,"./files/alumnos.dat");
        }

    }
    private void chargeTable() {
        dtmAlumnos.setRowCount(0);
        listaAlumnos = (ArrayList) AlumnosDAO.getAlumnosCSV();
        alumnosConsulta = listaAlumnos;
        listaAlumnos.forEach( a -> dtmAlumnos.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
        a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()}));
    }
    private void addAlumno() {
        AddDialog d = new AddDialog(true);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
        if (d.getAlumno() != null){
            Alumno a = d.getAlumno();
            listaAlumnos.add(a);
            dtmAlumnos.addRow(new Object[]{a.getApellidos(),a.getNombre(), a.getEmail(), a.getPoblacion(), a.getTelefono(),
                    a.getCiclo(),a.getOrdenador() ,a.isSiCarnet(), a.getEstudios(), a.getFechaNacimiento(), a.getMotivacion(), a.getHobbies()});
        }
        updateTable();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
