import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorContactos {
    private List<Contacto> contactos;
    private GestionIndices gestionIndices;
    private int siguienteId;

    public GestorContactos() {
        contactos = new ArrayList<>();
        gestionIndices = new GestionIndices();
        siguienteId = 1;
    }

    public void agregarContacto(String nombre, String apellido, String apodo, String telefono, String email, String direccion, LocalDate fechaNacimiento) {
        Contacto nuevoContacto = new Contacto(siguienteId, nombre, apellido, apodo, telefono, email, direccion, fechaNacimiento);
        contactos.add(nuevoContacto);

        gestionIndices.agregarAlIndice("nombre", nombre, siguienteId);
        gestionIndices.agregarAlIndice("apellido", apellido, siguienteId);

        siguienteId++;
        guardarContactosEnCSV();
    }

    public void eliminarContacto(int id) {
        contactos.removeIf(contacto -> {
            if (contacto.getId() == id) {
                gestionIndices.eliminarDelIndice("nombre", contacto.getNombre(), id);
                gestionIndices.eliminarDelIndice("apellido", contacto.getApellido(), id);
                return true;
            }
            return false;
        });
        guardarContactosEnCSV();
    }

    public void actualizarContacto(int id, String nombre, String apellido, String apodo, String telefono, String email, String direccion, LocalDate fechaNacimiento) {
        for (Contacto contacto : contactos) {
            if (contacto.getId() == id) {
                // Actualizar índices
                gestionIndices.actualizarIndice("nombre", contacto.getNombre(), nombre, id);
                gestionIndices.actualizarIndice("apellido", contacto.getApellido(), apellido, id);

                contacto.setNombre(nombre);
                contacto.setApellido(apellido);
                contacto.setApodo(apodo);
                contacto.setTelefono(telefono);
                contacto.setEmail(email);
                contacto.setDireccion(direccion);
                contacto.setFechaNacimiento(fechaNacimiento);
                break;
            }
        }
        guardarContactosEnCSV();
    }

    private void guardarContactosEnCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.csv"))) {
            for (Contacto contacto : contactos) {
                writer.write(contacto.getId() + "," + contacto.getNombre() + "," + contacto.getApellido() + "," +
                        contacto.getApodo() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "," +
                        contacto.getDireccion() + "," + contacto.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar contactos en CSV: " + e.getMessage());
        }
    }

    public void importarContactosDesdeCSV(String archivoCSV) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                LocalDate fechaNacimiento = LocalDate.parse(datos[7], dtf);
                agregarContacto(datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], fechaNacimiento);
            }

            int maxId = contactos.stream().mapToInt(Contacto::getId).max().orElse(0);
            siguienteId = maxId + 1;
        } catch (IOException e) {
            System.out.println("Error al importar contactos desde CSV: " + e.getMessage());
        }
    }

    public void visualizarContactos() {
        for (Contacto contacto : contactos) {
            System.out.println(contacto);
        }
    }

    public void mostrarRecorridoPorNivel(String campo) {
        List<String> recorrido = gestionIndices.recorridoPorNivel(campo);  // Actualizado para usar String como valor en el índice
        if (recorrido != null) {
            System.out.println("Recorrido por niveles del índice \"" + campo + "\":");
            for (String valor : recorrido) {  // Se cambió de id a valor
                System.out.print(valor + " ");
            }
            System.out.println();
        }
    }
}
