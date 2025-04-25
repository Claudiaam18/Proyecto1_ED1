import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Clase que gestiona la colección de contactos y sus operaciones.
 *
 * Esta clase implementa la funcionalidad CRUD (Crear, Leer, Actualizar,
 * Eliminar)
 * para la gestión de contactos, así como la indexación de campos mediante
 * estructuras de datos jerárquicas (árboles BST o AVL).
 *
 */
public class GestorContactos {
    private List<Contacto> contactos;
    private GestionIndices gestionIndices;
    private int siguienteId;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor de la clase GestorContactos.
     * Inicializa la lista de contactos, la gestión de índices y carga los contactos
     * desde el archivo CSV por defecto.
     */
    public GestorContactos() {
        contactos = new ArrayList<>();
        gestionIndices = new GestionIndices();
        siguienteId = 1;
        importarContactosDesdeCSV("contacts.csv");
    }

    /**
     * Crea un índice para un campo específico utilizando el tipo de árbol
     * especificado.
     *
     * @param campo     Campo sobre el que se creará el índice (nombre, apellido,
     *                  etc.)
     * @param tipoArbol Tipo de árbol a utilizar ("BST" o "AVL")
     */
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

    /**
     * Agrega un nuevo contacto a la lista y actualiza los índices correspondientes.
     *
     * @param nombre          Nombre del contacto
     * @param apellido        Apellido del contacto
     * @param apodo           Apodo del contacto
     * @param telefono        Número de teléfono del contacto
     * @param email           Correo electrónico del contacto (debe tener un formato
     *                        válido)
     * @param direccion       Dirección del contacto
     * @param fechaNacimiento Fecha de nacimiento del contacto
     */
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

    /**
     * Elimina un contacto de la lista por su ID y actualiza los índices
     * correspondientes.
     *
     * @param id ID del contacto a eliminar
     * @return true si el contacto fue eliminado exitosamente, false si no se
     *         encontró
     */
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

    /**
     * Actualiza los datos de un contacto existente por su ID y actualiza los
     * índices.
     *
     * @param id              ID del contacto a actualizar
     * @param nombre          Nuevo nombre del contacto
     * @param apellido        Nuevo apellido del contacto
     * @param apodo           Nuevo apodo del contacto
     * @param telefono        Nuevo teléfono del contacto
     * @param email           Nuevo email del contacto (debe tener formato válido)
     * @param direccion       Nueva dirección del contacto
     * @param fechaNacimiento Nueva fecha de nacimiento del contacto
     */
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

    /**
     * Muestra todos los contactos almacenados en la lista.
     */
    public void visualizarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos almacenados.");
        } else {
            System.out.println("Lista de contactos:");
            contactos.forEach(System.out::println);
        }
    }

    /**
     * Muestra el recorrido por niveles de un índice específico y lo guarda en un
     * archivo.
     *
     * @param campo Nombre del campo indexado a mostrar
     */
    public void mostrarRecorridoPorNivel(String campo) {
        List<String> recorrido = gestionIndices.recorridoPorNivel(campo);

        if (recorrido != null && !recorrido.isEmpty()) {
            System.out.println("Recorrido por niveles del índice '" + campo + "':");

            // Crear un mapa para relacionar los valores del campo con sus IDs
            Map<String, List<Integer>> valoresAIds = new HashMap<>();

            // Poblar el mapa con los valores del campo y sus IDs correspondientes
            for (Contacto contacto : contactos) {
                Object valorObj = contacto.getCampo(campo);
                if (valorObj != null) {
                    String valor = valorObj.toString();
                    if (!valoresAIds.containsKey(valor)) {
                        valoresAIds.put(valor, new ArrayList<>());
                    }
                    valoresAIds.get(valor).add(contacto.getId());
                }
            }

            // Construir la cadena de IDs para el recorrido por nivel
            StringBuilder resultado = new StringBuilder();
            boolean primero = true;

            for (String valor : recorrido) {
                if (!primero) {
                    resultado.append(",");
                }
                primero = false;

                if (valor != null && valoresAIds.containsKey(valor) && !valoresAIds.get(valor).isEmpty()) {
                    // Mostrar el ID del contacto correspondiente a este valor
                    resultado.append(valoresAIds.get(valor).get(0));
                } else {
                    resultado.append("null");
                }
            }

            System.out.println(resultado.toString());

            // Guardar el recorrido por niveles en archivos específicos
            // según el tipo de estructura (AVL o BST)
            String tipoIndice = gestionIndices.getTipoIndice(campo);
            if (tipoIndice != null && !tipoIndice.equals("N/A")) {
                String nombreArchivo = campo + "-" + tipoIndice.toLowerCase() + ".txt";
                // Cambiar la ruta para guardar en la carpeta 'src/reportes'
                String nombreArchivoCompleto = "reportes/" + nombreArchivo;

                // Asegurar que el directorio existe
                File directorioReportes = new File("reportes");
                if (!directorioReportes.exists()) {
                    directorioReportes.mkdirs();
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoCompleto))) {
                    writer.write(resultado.toString());
                    System.out.println("Recorrido por niveles (IDs) guardado en el archivo: " + nombreArchivoCompleto);
                } catch (IOException e) {
                    System.out.println("Error al guardar el recorrido por niveles: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No existe un índice para el campo '" + campo + "' o está vacío.");
        }
    }

    /**
     * Exporta todos los contactos a un archivo CSV en la ruta especificada.
     *
     * @param rutaExportar Ruta del archivo CSV donde se exportarán los contactos
     */
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

    /**
     * Guarda la lista de contactos en un archivo CSV.
     *
     * @param archivo Ruta del archivo CSV donde se guardarán los contactos
     */
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

    /**
     * Importa contactos desde un archivo CSV y los añade a la lista.
     *
     * @param archivoCSV Ruta del archivo CSV desde donde se importarán los
     *                   contactos
     */
    public void importarContactosDesdeCSV(String archivoCSV) {
        System.out.println("Importando contactos desde " + archivoCSV);

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            boolean primeraLinea = true; // Para identificar la línea de encabezados

            while ((linea = reader.readLine()) != null) {
                // Ignorar la primera línea que contiene los encabezados
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

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

    /**
     * Reconstruye los índices para todos los contactos existentes.
     * Se utiliza después de importar contactos desde un archivo.
     */
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

    /**
     * Valida que un correo electrónico tenga un formato válido.
     *
     * @param email El correo electrónico a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}