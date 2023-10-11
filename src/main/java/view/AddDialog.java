package view;

import model.Motivacion;
import model.Ordenador;
import model.Ciclo;
import model.Alumno;

import javax.swing.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JTextField txtPoblacion;
    private JTextField txtTelefono;
    private JRadioButton DAMRadioButton;
    private JRadioButton DAWRadioButton;
    private JRadioButton ASIRRadioButton;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JTextArea txtEstudios;
    private JTextField txtFecha;
    private JRadioButton muyMotivadoRadioButton;
    private JRadioButton moderadamenteMotivadoRadioButton;
    private JRadioButton pocoMotivadoRadioButton;
    private JTextArea txtHobbies;
    private JRadioButton a1RadioButton;
    private JRadioButton a2RadioButton;
    private JRadioButton a3RadioButton;
    private JRadioButton a4RadioButton;
    private JRadioButton a5RadioButton;
    private ButtonGroup carnetGroup;
    private ButtonGroup ciclosGroup;
    private ButtonGroup motivadoGroup;
    private ButtonGroup ordenadorGroup;
    private Alumno alumno;
    private boolean add;

    public AddDialog(boolean editable) {

        txtFecha.setEditable(editable);
        txtEstudios.setLineWrap(true);
        txtHobbies.setLineWrap(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(800,600);
        add = true;
        boolean edit = false;
        carnetGroup = new ButtonGroup();
        carnetGroup.add(siRadioButton);
        carnetGroup.add(noRadioButton);

        ciclosGroup = new ButtonGroup();
        ciclosGroup.add(ASIRRadioButton);
        ciclosGroup.add(DAMRadioButton);
        ciclosGroup.add(DAWRadioButton);

        motivadoGroup = new ButtonGroup();
        motivadoGroup.add(pocoMotivadoRadioButton);
        motivadoGroup.add(muyMotivadoRadioButton);
        motivadoGroup.add(moderadamenteMotivadoRadioButton);
        pocoMotivadoRadioButton = new JRadioButton();

        ordenadorGroup = new ButtonGroup();
        ordenadorGroup.add(a1RadioButton);
        ordenadorGroup.add(a2RadioButton);
        ordenadorGroup.add(a3RadioButton);
        ordenadorGroup.add(a4RadioButton);
        ordenadorGroup.add(a5RadioButton);

        buttonOK.addActionListener(e -> {
            if (add){
                addAlumno();
            }else {
                try {
                    editAlumno(alumno);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonCancel.addActionListener(e -> onCancel());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    private void addAlumno() {
        String apellido = txtApellidos.getText();
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String localidad = txtPoblacion.getText();
        Ciclo ciclo = null;
        if (ASIRRadioButton.isSelected()){
            ciclo = Ciclo.valueOf("ASIR");
        } else if (DAMRadioButton.isSelected()) {
            ciclo = Ciclo.valueOf("DAM");
        } else if (DAWRadioButton.isSelected()) {
            ciclo = Ciclo.valueOf("DAW");
        }
        Ordenador ordenador = null;
        if (a1RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("uno");
        } else if (a2RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("dos");
        }else if (a3RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("tres");
        }else if (a4RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("cuatro");
        }else if (a5RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("cinco");
        }
        boolean carnet = false;
        if(siRadioButton.isSelected()){
            carnet = true;
        } else if (noRadioButton.isSelected()) {
            carnet = false;
        }

        List<String> estudios = Collections.singletonList(txtEstudios.getText().replace("[","").replace("]",""));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNacimiento = null;
        try {
            if (!txtFecha.getText().isEmpty()){
                fechaNacimiento = formato.parse(txtFecha.getText());
            }
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Motivacion motivacion = null;
        if(muyMotivadoRadioButton.isSelected()) {
            motivacion = Motivacion.valueOf("Muy_motivado");
        } else if (moderadamenteMotivadoRadioButton.isSelected()) {
            motivacion = Motivacion.valueOf("Moderadamente_motivado");
        } else if (pocoMotivadoRadioButton.isSelected()) {
            motivacion = Motivacion.valueOf("Poco_motivado");
        }
        List<String> hobbies = Collections.singletonList(txtHobbies.getText().replace("[","").replace("]",""));
        this.alumno = new Alumno(apellido, nombre, email,localidad, telefono,ciclo,ordenador,carnet, estudios,fechaNacimiento,motivacion, hobbies);
        dispose();
    }

    private void editAlumno(Alumno a) throws ParseException {
        a.setApellidos(txtApellidos.getText());
        a.setNombre(txtNombre.getText());
        a.setEmail(txtEmail.getText());
        a.setEstudios(Collections.singletonList(txtEstudios.getText().replace("[","").replace("]","")));
        a.setTelefono(txtTelefono.getText());
        a.setHobbies(Collections.singletonList(txtHobbies.getText().replace("[", "").replace("]", "")));
        a.setPoblacion(txtPoblacion.getText());
        if (siRadioButton.isSelected()){
            a.setSiCarnet(true);
        }else{
            a.setSiCarnet(false);
        }
        Ciclo ciclo = null;
        if (ASIRRadioButton.isSelected()){
            ciclo = Ciclo.valueOf("ASIR");
        } else if (DAMRadioButton.isSelected()) {
            ciclo = Ciclo.valueOf("DAM");
        } else if (DAWRadioButton.isSelected()) {
            ciclo = Ciclo.valueOf("DAW");
        }
        a.setCiclo(ciclo);
        Ordenador ordenador = null;
        if (a1RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("uno");
        } else if (a2RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("dos");
        }else if (a3RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("tres");
        }else if (a4RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("cuatro");
        }else if (a5RadioButton.isSelected()) {
            ordenador = Ordenador.valueOf("cinco");
        }
        a.setOrdenador(ordenador);
        String fechaOriginal = txtFecha.getText();
        // Crear un SimpleDateFormat para analizar la fecha original
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        // Analizar la fecha original en un objeto Date
        Date fechaDate = formatoOriginal.parse(fechaOriginal);
        // Crear un nuevo SimpleDateFormat para formatear la fecha en dd/mm/yyyy
        SimpleDateFormat formatoNuevo = new SimpleDateFormat("dd/MM/yyyy");
        // Formatear la fecha en el nuevo formato
        String fechaFormateada = formatoNuevo.format(fechaDate);
        SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
        a.setFechaNacimiento(formato2.parse(fechaFormateada));
        Motivacion motivacion = null;
        if(muyMotivadoRadioButton.isSelected()) {
            motivacion = Motivacion.valueOf("Muy_motivado");
        } else if (moderadamenteMotivadoRadioButton.isSelected()) {
            motivacion = Motivacion.valueOf("Moderadamente_motivado");
        } else {
            motivacion = Motivacion.valueOf("Poco_motivado");
        }
        a.setMotivacion(motivacion);
        dispose();
    }

    public Alumno getAlumno() {
        return this.alumno;
    }
    public void setAlumno(Alumno a){
        this.alumno = a;
        txtFecha.setText(String.valueOf(a.getFechaNacimiento()));
        txtNombre.setText(a.getNombre());
        txtApellidos.setText(a.getApellidos());
        txtEmail.setText(a.getEmail());
        txtEstudios.setText(a.getEstudios().toString());
        txtPoblacion.setText(a.getPoblacion());
        txtTelefono.setText(a.getTelefono());
        txtHobbies.setText(a.getHobbies().toString());
        if (a.isSiCarnet()){
            siRadioButton.setSelected(true);
        }else {
            noRadioButton.setSelected(true);
        }
        switch (a.getOrdenador()){
            case uno -> a1RadioButton.setSelected(true);
            case dos -> a2RadioButton.setSelected(true);
            case tres -> a3RadioButton.setSelected(true);
            case cuatro -> a4RadioButton.setSelected(true);
            case cinco ->  a5RadioButton.setSelected(true);
        }
        switch (a.getMotivacion()){
            case Muy_motivado -> muyMotivadoRadioButton.setSelected(true);
            case Moderadamente_motivado -> moderadamenteMotivadoRadioButton.setSelected(true);
            case Poco_motivado -> pocoMotivadoRadioButton.setSelected(true);
        }
        switch (a.getCiclo()){
            case DAM -> DAMRadioButton.setSelected(true);
            case DAW -> DAWRadioButton.setSelected(true);
            case ASIR -> ASIRRadioButton.setSelected(true);
        }
        add = false;
    }

}
