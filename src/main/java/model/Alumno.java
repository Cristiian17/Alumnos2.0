package model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Alumno implements Serializable {
    private String apellidos;
    private String nombre;
    private String email;
    private String poblacion;
    private String telefono;
    private Ciclo ciclo;
    private Ordenador ordenador;
    private boolean siCarnet;
    private List<String> estudios;
    private Date fechaNacimiento;
    private Motivacion motivacion;
    private List<String> hobbies;

    public Alumno() {
    }

    public Alumno(String apellidos, String nombre, String email, String poblacion, String telefono, Ciclo ciclo, Ordenador ordenador, boolean siCarnet, List<String> estudios, Date fechaNacimiento, Motivacion motivacion, List<String> hobbies) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.poblacion = poblacion;
        this.telefono = telefono;
        this.ciclo = ciclo;
        this.ordenador = ordenador;
        this.siCarnet = siCarnet;
        this.estudios = estudios;
        this.fechaNacimiento = fechaNacimiento;
        this.motivacion = motivacion;
        this.hobbies = hobbies;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isSiCarnet() {
        return siCarnet;
    }

    public void setSiCarnet(boolean siCarnet) {
        this.siCarnet = siCarnet;
    }

    public List<String> getEstudios() {
        return estudios;
    }

    public void setEstudios(List<String> estudios) {
        this.estudios = estudios;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public Ordenador getOrdenador() {
        return ordenador;
    }

    public void setOrdenador(Ordenador ordenador) {
        this.ordenador = ordenador;
    }

    public Motivacion getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(Motivacion motivacion) {
        this.motivacion = motivacion;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "apellidos='" + apellidos + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", poblacion='" + poblacion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ciclo=" + ciclo +
                ", ordenador=" + ordenador +
                ", siCarnet=" + siCarnet +
                ", estudios=" + estudios +
                ", fechaNacimiento=" + fechaNacimiento +
                ", motivacion=" + motivacion +
                ", hobbies=" + hobbies +
                '}';
    }
}
