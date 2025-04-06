import java.io.*;
        import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorContactos {
    private List<Contacto> contactos;
    private GestionIndices gestionIndices;
    private int siguienteId;

    public GestorContactos() {
        contactos = new ArrayList<>();
        gestionIndices = new GestionIndices();
        siguienteId = 1; // Comenzar con el ID 1
    }

    // Método para agregar un nuevo contacto
    public void agregarContacto(String nombre, String apellido, String apodo, String telefono, String email, String direccion, Date fechaNacimiento) {
        Contacto nuevoContacto = new Contacto(siguienteId, nombre, apellido, apodo, telefono, email, direccion, fechaNacimiento);
        contactos.add(nuevoContacto);
        gestionIndices.agregarAlIndice("nombre", siguienteId);
        gestionIndices.agregarAlIndice("apellido", siguienteId);
        siguienteId++;
        guardarContactosEnCSV();
    }

    // Método para eliminar un contacto
    public void eliminarContacto(int id) {
        contactos.removeIf(contacto -> contacto.getId() == id);
        // Aquí se debería eliminar el ID del índice correspondiente
        // Implementar la lógica para eliminar del índice
        guardarContactosEnCSV();
    }

    // Método para actualizar un contacto
    public void actualizarContacto(int id, String nombre, String apellido, String apodo, String telefono, String email, String direccion, Date fechaNacimiento) {
        for (Contacto contacto : contactos) {
            if (contacto.getId() == id) {
                contacto.setNombre(nombre);
                contacto.setApellido(apellido);
                contacto.setApodo(apodo);
                contacto.setTelefono(telefono);
                contacto.setEmail(email);
                contacto.setDireccion(direccion);
                contacto.setFechaNacimiento(fechaNacimiento);
                // Aquí se debería actualizar el índice correspondiente
                // Implementar la lógica para actualizar el índice
                break;
            }
        }
        guardarContactosEnCSV();
    }

    // Método para guardar contactos en un archivo CSV
    private void guardarContactosEnCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.csv"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha
            for (Contacto contacto : contactos) {
                writer.write(contacto.getId() + "," + contacto.getNombre() + "," + contacto.getApellido() + "," +
                        contacto.getApodo() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "," +
                        contacto.getDireccion() + "," + sdf.format(contacto.getFechaNacimiento()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar contactos en CSV: " + e.getMessage());
        }
    }

    // Método para importar contactos desde un archivo CSV
    public void importarContactosDesdeCSV(String archivoCSV) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                Date fechaNacimiento = sdf.parse(datos[7]); // Convertir la fecha de String a Date
                agregarContacto(datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], fechaNacimiento);
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error al importar contactos desde CSV: " + e.getMessage());
        }
    }

    // Método para visualizar todos los contactos
    public void visualizarContactos() {
        for (Contacto contacto : contactos) {
            System.out.println(contacto);
        }
    }

    // Método para mostrar el recorrido por niveles (BFS) de un índice
    public void mostrarRecorridoPorNivel(String campo) {
        List<Integer> recorrido = gestionIndices.recorridoPorNivel(campo);
        if (recorrido != null) {
            System.out.println("Recorrido por niveles del índice \"" + campo + "\":");
            for (int id : recorrido) {
                System.out.print(id + " ");
            }
            System.out.println();
        }
    }

}