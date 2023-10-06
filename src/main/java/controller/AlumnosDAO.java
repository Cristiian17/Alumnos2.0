package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.Alumno;
import model.Ciclo;
import model.Motivacion;
import model.Ordenador;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AlumnosDAO {

    public List<Alumno> getAlumnosCSV() {
        BufferedReader bufferLectura = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List listaAlumno = new ArrayList();
        try {
            // Abrir el .csv en buffer de lectura
            bufferLectura = new BufferedReader(new FileReader("alumnos.csv"));

            // Leer una linea del archivo
            String linea = bufferLectura.readLine();
            int cont = 0;
            while (linea != null) {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split(";");
                    if (cont != 0) {
                        String apellido = campos[1];
                        String nombre = campos[2];
                        String email = campos[3];
                        String telefono = campos[4];
                        String localidad = campos[5];
                        Ciclo ciclo = Ciclo.valueOf(campos[6]);
                        int numeroOrdenador = Integer.parseInt(campos[7]);
                        Ordenador ordenador;
                        switch (numeroOrdenador) {
                            case 1:
                                ordenador = Ordenador.valueOf("uno");
                                break;
                            case 2:
                                ordenador = Ordenador.valueOf("dos");
                                break;
                            case 3:
                                ordenador = Ordenador.valueOf("tres");
                                break;
                            case 4:
                                ordenador = Ordenador.valueOf("cuatro");
                                break;
                            default:
                                ordenador = Ordenador.valueOf("cinco");
                        }
                        boolean carnet = campos[8].equalsIgnoreCase("si");
                        List<String> estudios = Collections.singletonList(campos[9]);
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaNacimiento = formato.parse(campos[10]);
                        String motivado = campos[11];
                        Motivacion motivacion;
                        switch (motivado) {
                            case "Muy motivado":
                                motivacion = Motivacion.valueOf("Muy_motivado");
                                break;
                            case "Moderadamente motivado":
                                motivacion = Motivacion.valueOf("Moderadamente_motivado");
                                break;
                            default:
                                motivacion = Motivacion.valueOf("Poco_motivado");
                        }
                        List<String> hobbies = Collections.singletonList(campos[12]);
                        Alumno a = new Alumno(apellido,nombre,email,localidad,telefono,ciclo,ordenador,carnet,estudios,fechaNacimiento,motivacion,hobbies);
                        listaAlumno.add(a);
                    }
                    // Volver a leer otra línea del fichero
                    linea = bufferLectura.readLine();
                    cont++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cierro el buffer de lectura
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return listaAlumno;
        }
    }

    public void saveAlumnosDAT(ArrayList<Alumno> lista, String nombreArchivo) {
        try {
            // Crea un flujo de salida de objetos FileOutputStream y ObjectOutputStream
            FileOutputStream archivoSalida = new FileOutputStream(nombreArchivo);
            ObjectOutputStream objetoSalida = new ObjectOutputStream(archivoSalida);
            // Escribe la lista en el archivo
            objetoSalida.writeObject(lista);
            // Cierra los flujos
            objetoSalida.close();
            archivoSalida.close();
            System.out.println("ArrayList guardado en " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveJsonAlumnos(List<Alumno> listaAlumnos){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Serializar el ArrayList a JSON y guardar en un archivo
            objectMapper.writeValue(new File("alumnos.json"), listaAlumnos);

            System.out.println("Alumnos guardados en alumnos.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Alumno> leerArrayListDesdeArchivoBinario(String nombreArchivo) {
        try {
            // Crear un objeto FileInputStream para leer el archivo binario
            FileInputStream archivoEntrada = new FileInputStream(nombreArchivo);

            // Crear un ObjectInputStream para deserializar el ArrayList
            ObjectInputStream objetoEntrada = new ObjectInputStream(archivoEntrada);

            // Leer el ArrayList desde el archivo binario
            ArrayList<Alumno> listaAlumnos = (ArrayList<Alumno>) objetoEntrada.readObject();

            // Cerrar el ObjectInputStream y el FileInputStream
            objetoEntrada.close();
            archivoEntrada.close();

            return listaAlumnos;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveOnXML(ArrayList<Alumno> listaAlumnos, String nombreArchivo) {
        try {
            // Crear un objeto XmlMapper para la conversión a XML
            XmlMapper xmlMapper = new XmlMapper();

            // Convertir la lista de alumnos a formato XML
            String xml = xmlMapper.writeValueAsString(listaAlumnos);

            // Escribir el XML en un archivo
            File archivoXML = new File(nombreArchivo);
            xmlMapper.writeValue(archivoXML, listaAlumnos);

            System.out.println("ArrayList guardado en XML correctamente en el archivo " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveOnJSON(ArrayList<Alumno> listaAlumnos, String nombreArchivo) {
        try {
            // Crear un objeto ObjectMapper para la conversión a JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Convertir la lista de alumnos a formato JSON y escribirlo en un archivo
            File archivoJSON = new File(nombreArchivo);
            objectMapper.writeValue(archivoJSON, listaAlumnos);

            System.out.println("ArrayList exportado a JSON correctamente en el archivo " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveOnCSV(List<Alumno> listaAlumnos, String nombreArchivo) {
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)) {
            // Escribir la cabecera (nombres de las columnas) en el archivo CSV
            fileWriter.append("Marca temporal;Apellidos;Nombre;Email;Poblacion;Telefono;Ciclo;Ordenador;SiCarnet;Estudios;FechaNacimiento;Motivacion;Hobbies");
            fileWriter.append("\n");

            // Formateador de fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Escribir los datos de la lista en el archivo CSV
            for (Alumno alumno : listaAlumnos) {
                fileWriter.append("9/12/2023 23:44:32");
                fileWriter.append(";");
                fileWriter.append(alumno.getApellidos());
                fileWriter.append(";");
                fileWriter.append(alumno.getNombre());
                fileWriter.append(";");
                fileWriter.append(alumno.getEmail());
                fileWriter.append(";");
                fileWriter.append(alumno.getPoblacion());
                fileWriter.append(";");
                fileWriter.append(alumno.getTelefono());
                fileWriter.append(";");
                fileWriter.append(alumno.getCiclo().toString());
                fileWriter.append(";");
                fileWriter.append(alumno.getOrdenador().toString());
                fileWriter.append(";");
                fileWriter.append(String.valueOf(alumno.isSiCarnet()));
                fileWriter.append(";");
                fileWriter.append(String.join(", ", alumno.getEstudios()));
                fileWriter.append(";");
                fileWriter.append(dateFormat.format(alumno.getFechaNacimiento()));
                fileWriter.append(";");
                fileWriter.append(alumno.getMotivacion().toString());
                fileWriter.append(";");
                fileWriter.append(String.join(", ", alumno.getHobbies()));
                fileWriter.append("\n");
            }

            System.out.println("ArrayList exportado a CSV correctamente en el archivo " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveOnPDF(ArrayList<Alumno> listaAlumnos, String nombreArchivo) {
        try {
            // Crear un documento PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Crear un objeto PDPageContentStream para escribir en la página
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Configurar la fuente y el tamaño de la fuente
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            // Escribir los datos de la lista en el archivo PDF
            float marginX = 50;
            float yPosition = page.getMediaBox().getHeight() - 50;
            for (Alumno alumno : listaAlumnos) {
                contentStream.beginText();
                contentStream.newLineAtOffset(marginX, yPosition);
                contentStream.showText("Apellidos: " + alumno.getApellidos());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Nombre: " + alumno.getNombre());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Email: " + alumno.getEmail());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Población: " + alumno.getPoblacion());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Teléfono: " + alumno.getTelefono());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Ciclo: " + alumno.getCiclo());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Ordenador: " + alumno.getOrdenador());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Carnet: " + (alumno.isSiCarnet() ? "Sí" : "No"));
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Estudios: " + String.join(", ", alumno.getEstudios()));
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Fecha de Nacimiento: " + alumno.getFechaNacimiento());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Motivación: " + alumno.getMotivacion());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Hobbies: " + String.join(", ", alumno.getHobbies()));
                contentStream.newLineAtOffset(0, -20);

                contentStream.endText();
                yPosition -= 260; // Espaciado entre estudiantes
                // Si el espacio disponible en la página actual es insuficiente, crea una nueva página
                if (yPosition < 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    yPosition = page.getMediaBox().getHeight() - 50;
                }
            }

            // Cerrar el objeto PDPageContentStream
            contentStream.close();

            // Guardar el documento PDF en el archivo especificado
            document.save(new File(nombreArchivo));

            // Cerrar el documento
            document.close();

            System.out.println("ArrayList exportado a PDF correctamente en el archivo " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

