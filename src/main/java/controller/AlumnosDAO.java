package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AlumnosDAO {

    public static List<Alumno> getAlumnosCSV() {
        BufferedReader bufferLectura = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List listaAlumno = new ArrayList();
        try {
            JFileChooser fileChooser = new JFileChooser();

            // Obtener el directorio actual de trabajo del programa
            FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv");
            fileChooser.setFileFilter(csvFilter);
            String directorioActual = System.getProperty("user.dir");
            File directorioInicial = new File(directorioActual + "/files");

            fileChooser.setCurrentDirectory(directorioInicial);

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();


                // Abrir el .csv en buffer de lectura
                bufferLectura = new BufferedReader(new FileReader(rutaArchivo));

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
                        Alumno a = new Alumno(apellido, nombre, email, localidad, telefono, ciclo, ordenador, carnet, estudios, fechaNacimiento, motivacion, hobbies);
                        listaAlumno.add(a);
                    }
                    // Volver a leer otra línea del fichero
                    linea = bufferLectura.readLine();
                    cont++;
                }
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

    public static List<Alumno> readPdf(String archivo){
        //Loading an existing document
        PDDocument document = null;
        ArrayList listaAlumno = new ArrayList();
        try {
            File file = new File(archivo);
            document = PDDocument.load(file);

            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();

            //Retrieving text from PDF document
            String text = pdfStripper.getText(document);
            String lines[] = text.split("#");
            int cont = 0;
            for (String line : lines) {
                if (cont > 0) {


                    String[] campos = line.split("\\r?\\n");
                    String apellido = campos[1].split(":")[1];
                    String nombre = campos[2].split(":")[1];
                    String email = campos[3].split(":")[1];
                    String telefono = campos[5].split(":")[1];
                    String localidad = campos[4].split(":")[1];
                    Ciclo ciclo = Ciclo.valueOf(campos[6].split(":")[1].trim());
                    Ordenador ordenador = Ordenador.valueOf(campos[7].split(":")[1].trim());
                    boolean carnet = campos[8].equalsIgnoreCase("Sí");
                    List<String> estudios = Collections.singletonList(campos[9].split(":")[1].trim());

                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaNacimiento = formato.parse(campos[10].split(":")[1].trim());
                    String motivacion1 = campos[11].split(":")[1].trim();
                    Motivacion motivacion;
                    switch (motivacion1) {
                        case ("Muy_motivado") -> motivacion = Motivacion.valueOf("Muy_motivado");
                        case ("Moderadamente_motivado") -> motivacion = Motivacion.valueOf("Moderadamente_motivado");
                        default -> motivacion = Motivacion.valueOf("Poco_motivado");
                    }


                    List<String> hobbies = Collections.singletonList(campos[12].split(":")[1]);
                    Alumno a = new Alumno(apellido, nombre, email, localidad, telefono, ciclo, ordenador, carnet, estudios, fechaNacimiento, motivacion, hobbies);
                    listaAlumno.add(a);
                }
                cont++;
            }
        }catch (IOException e){

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                document.close();
                return listaAlumno;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static List<Alumno> readCsv(String archivo) {
        BufferedReader bufferLectura = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List listaAlumno = new ArrayList();
        try {
                // Abrir el .csv en buffer de lectura
                bufferLectura = new BufferedReader(new FileReader(archivo));

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
                        Alumno a = new Alumno(apellido, nombre, email, localidad, telefono, ciclo, ordenador, carnet, estudios, fechaNacimiento, motivacion, hobbies);
                        listaAlumno.add(a);
                    }
                    linea = bufferLectura.readLine();
                        cont++;

                }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

    public static List<Alumno> readJson(String archivoJSON) {
        List<Alumno> listaDeObjetos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File archivo = new File(archivoJSON);
            JsonNode rootNode = objectMapper.readTree(archivo);

            // Iterar a través de los elementos del JSON y agregarlos a la lista
            for (JsonNode jsonNode : rootNode) {
                Alumno objeto = objectMapper.treeToValue(jsonNode, Alumno.class);
                listaDeObjetos.add(objeto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaDeObjetos;
    }

        public static List<Alumno> readXml(String nombreArchivo) {
            XmlMapper xmlMapper = new XmlMapper();
            List<Alumno> listaDeDatos = null;

            try {
                File archivoXML = new File(nombreArchivo);

                // Utiliza un TypeReference para obtener una lista de objetos Datos
                listaDeDatos = xmlMapper.readValue(archivoXML, new TypeReference<List<Alumno>>() {});

            } catch (IOException e) {
                e.printStackTrace();
            }

            return listaDeDatos;
        }

    public static void saveAlumnosDAT(ArrayList<Alumno> lista, String nombreArchivo) {
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

    public static ArrayList<Alumno> readDat(String nombreArchivo) {
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

    public static void saveOnCSV(List<Alumno> listaAlumnos, String nombreArchivo) throws ParseException {
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
                switch (alumno.getOrdenador()) {
                    case uno -> fileWriter.append("1");
                    case dos -> fileWriter.append("2");
                    case tres -> fileWriter.append("3");
                    case cuatro -> fileWriter.append("4");
                    default -> fileWriter.append("5");
                }
                fileWriter.append(";");

                if (alumno.isSiCarnet()) {
                    fileWriter.append("Si");
                } else {
                    fileWriter.append("No");
                }
                fileWriter.append(";");
                fileWriter.append(String.join(", ", alumno.getEstudios()));
                fileWriter.append(";");

                SimpleDateFormat formatoOriginal = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                SimpleDateFormat formatoNuevo = new SimpleDateFormat("dd/MM/yyyy");
                // Parsear la fecha en el formato original
                Date date = formatoOriginal.parse(alumno.getFechaNacimiento().toString());

                // Formatear la fecha en el nuevo formato
                String fechaFormateada = formatoNuevo.format(date);
                fileWriter.append(fechaFormateada);
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

    public static void saveOnPDF(ArrayList<Alumno> listaAlumnos, String nombreArchivo) throws ParseException {
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

                contentStream.showText("#");
                contentStream.newLineAtOffset(0, -20);

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


                SimpleDateFormat formatoOriginal = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                SimpleDateFormat formatoNuevo = new SimpleDateFormat("dd/MM/yyyy");
                // Parsear la fecha en el formato original
                Date date = formatoOriginal.parse(alumno.getFechaNacimiento().toString());

                // Formatear la fecha en el nuevo formato
                String fechaFormateada = formatoNuevo.format(date);
                contentStream.showText("Fecha de Nacimiento: " + fechaFormateada);
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

    public static List<String> getFileNames(String directorio) {
        List<String> nombresArchivos = new ArrayList<>();

        File carpeta = new File(directorio);

        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();

            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile()) {
                        nombresArchivos.add(archivo.getName());
                    }
                }
            }
        }
        return nombresArchivos;
    }
}

