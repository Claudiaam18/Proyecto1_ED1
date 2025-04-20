import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GestorContactos {
    private List<Contacto> contactos;
    private GestionIndices gestionIndices;
    private int siguienteId;
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GestorContactos() {
        contactos = new ArrayList<>();
        gestionIndices = new GestionIndices();
        siguienteId = 1;
        importarContactosDesdeCSV("contacts.csv");
    }

    public void crearIndice(String campo, String tipoArbol) {
        gestionIndices.crearIndice(campo, tipoArbol);

        // Reindexar contactos existentes
        for (Contacto contacto : contactos) {
            Object valorObj = contacto.getCampo(campo);
            if (valorObj != null) {
                gestionIndices.agregarAlIndice(campo, valorObj.toString());
            }
        }
    }

    public void agregarContacto(String nombre, String apellido, String apodo, String telefono,
                                String email, String direccion, LocalDate fechaNacimiento) {
        if (!validarEmail(email)) {
            System.out.println("Email inválido. No se agregó el contacto.");
            return;
        }

        Contacto nuevo = new Contacto(siguienteId, nombre, apellido, apodo, telefono,
                email, direccion, fechaNacimiento);
        contactos.add(nuevo);

        // Actualizar índices
        for (String campo : gestionIndices.getCamposIndexados()) {
            Object valorObj = nuevo.getCampo(campo);
            if (valorObj != null) {
                gestionIndices.agregarAlIndice(campo, valorObj.toString());
            }
        }

        siguienteId++;
        guardarContactosEnCSV("contacts.csv");
    }

    public boolean eliminarContacto(int id) {
        Contacto contactoAEliminar = contactos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (contactoAEliminar != null) {
            // Eliminar de los índices
            for (String campo : gestionIndices.getCamposIndexados()) {
                Object valorObj = contactoAEliminar.getCampo(campo);
                if (valorObj != null) {
                    gestionIndices.eliminarDelIndice(campo, valorObj.toString());
                }
            }

            contactos.remove(contactoAEliminar);
            guardarContactosEnCSV("contacts.csv");
            return true;
        }
        return false;
    }

    public void actualizarContacto(int id, String nombre, String apellido, String apodo,
                                   String telefono, String email, String direccion, LocalDate fechaNacimiento) {
        Contacto contacto = contactos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (contacto != null) {
            // Actualizar índices
            for (String campo : gestionIndices.getCamposIndexados()) {
                Object valorAnterior = contacto.getCampo(campo);
                Object valorNuevo = switch (campo.toLowerCase()) {
                    case "nombre" -> nombre;
                    case "apellido" -> apellido;
                    case "apodo" -> apodo;
                    case "telefono" -> telefono;
                    case "email" -> email;
                    case "direccion" -> direccion;
                    case "fechanacimiento" -> fechaNacimiento.format(FORMATO_FECHA);
                    default -> valorAnterior;
                };
                if (valorAnterior != null && valorNuevo != null) {
                    gestionIndices.actualizarIndice(campo, valorAnterior.toString(),
                            valorNuevo.toString());
                }
            }

            contacto.setNombre(nombre);
            contacto.setApellido(apellido);
            contacto.setApodo(apodo);
            contacto.setTelefono(telefono);
            if (validarEmail(email)) {
                contacto.setEmail(email);
            } else {
                System.out.println("Email inválido. Se mantuvo el anterior.");
            }
            contacto.setDireccion(direccion);
            contacto.setFechaNacimiento(fechaNacimiento);

            guardarContactosEnCSV("contacts.csv");
        }
    }

    public void visualizarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos almacenados.");
        } else {
            System.out.println("Lista de contactos:");
            contactos.forEach(System.out::println);
        }
    }

    public void mostrarRecorridoPorNivel(String campo) {
        List<String> recorrido = gestionIndices.recorridoPorNivel(campo);

        if (recorrido != null && !recorrido.isEmpty()) {
            System.out.println("Recorrido por niveles del índice '" + campo + "':");
            System.out.println(String.join(", ", recorrido));
        } else {
            System.out.println("No existe un índice para el campo '" + campo + "' o está vacío.");
        }
    }

    public void exportarContactos(String rutaExportar) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaExportar))) {
            for (Contacto contacto : contactos) {
                writer.write(contacto.toCSV());
                writer.newLine();
            }
            System.out.println("Contactos exportados correctamente a " + rutaExportar);
        } catch (IOException e) {
            System.out.println("Error al exportar contactos: " + e.getMessage());
        }
    }

    private void guardarContactosEnCSV(String archivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (Contacto contacto : contactos) {
                writer.write(contacto.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar contactos: " + e.getMessage());
        }
    }

    public void importarContactosDesdeCSV(String archivoCSV) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Contacto contacto = Contacto.fromCSV(linea);
                if (contacto != null) {
                    contactos.add(contacto);
                    siguienteId = Math.max(siguienteId, contacto.getId() + 1);
                }
            }
            reconstruirIndices();
        } catch (IOException e) {
            System.out.println("Error al importar contactos: " + e.getMessage());
        }
    }

    private void reconstruirIndices() {
        for (String campo : gestionIndices.getCamposIndexados()) {
            for (Contacto contacto : contactos) {
                Object valorObj = contacto.getCampo(campo);
                if (valorObj != null) {
                    gestionIndices.agregarAlIndice(campo, valorObj.toString());
                }
            }
        }
    }

    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}