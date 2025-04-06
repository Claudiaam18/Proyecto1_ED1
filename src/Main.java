import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorContactos gestor = new GestorContactos();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.println("Menú de Gestión de Contactos:");
            System.out.println("1. Agregar contacto");
            System.out.println("2. Eliminar contacto");
            System.out.println("3. Actualizar contacto");
            System.out.println("4. Visualizar contactos");
            System.out.println("5. Importar contactos desde CSV");
            System.out.println("6. Exportar contactos a CSV");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    // Agregar contacto
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = scanner.nextLine();
                    System.out.print("Apodo: ");
                    String apodo = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Dirección: ");
                    String direccion = scanner.nextLine();
                    System.out.print("Fecha de nacimiento (yyyy-MM-dd): ");
                    String fechaStr = scanner.nextLine();
                    try {
                        LocalDate fechaNacimiento = LocalDate.parse(fechaStr, dtf);
                        gestor.agregarContacto(nombre, apellido, apodo, telefono, email, direccion, fechaNacimiento);
                        System.out.println("Contacto agregado exitosamente.");
                    } catch (DateTimeParseException e) {
                        System.out.println("Error en el formato de la fecha: " + e.getMessage());
                    }
                    break;
                case 2:
                    // Eliminar contacto
                    System.out.print("ID del contacto a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    gestor.eliminarContacto(idEliminar);
                    System.out.println("Contacto eliminado.");
                    break;
                case 3:
                    // Actualizar contacto
                    System.out.print("ID del contacto a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Nuevo apellido: ");
                    String nuevoApellido = scanner.nextLine();
                    System.out.print("Nuevo apodo: ");
                    String nuevoApodo = scanner.nextLine();
                    System.out.print("Nuevo teléfono: ");
                    String nuevoTelefono = scanner.nextLine();
                    System.out.print("Nuevo email: ");
                    String nuevoEmail = scanner.nextLine();
                    System.out.print("Nueva dirección: ");
                    String nuevaDireccion = scanner.nextLine();
                    System.out.print("Nueva fecha de nacimiento (yyyy-MM-dd): ");
                    String nuevaFechaStr = scanner.nextLine();
                    try {
                        LocalDate nuevaFechaNacimiento = LocalDate.parse(nuevaFechaStr, dtf);
                        gestor.actualizarContacto(idActualizar, nuevoNombre, nuevoApellido, nuevoApodo, nuevoTelefono, nuevoEmail, nuevaDireccion, nuevaFechaNacimiento);
                        System.out.println("Contacto actualizado exitosamente.");
                    } catch (DateTimeParseException e) {
                        System.out.println("Error en el formato de la fecha: " + e.getMessage());
                    }
                    break;
                case 4:
                    // Visualizar contactos
                    System.out.println("Lista de contactos:");
                    gestor.visualizarContactos();
                    break;
                case 5:
                    // Importar contactos
                    System.out.print("Ruta del archivo CSV: ");
                    String rutaImportar = scanner.nextLine();
                    gestor.importarContactosDesdeCSV(rutaImportar);
                    System.out.println("Contactos importados exitosamente.");
                    break;
                case 6:
                    // Exportar contactos
                    System.out.print("Ruta del archivo CSV a exportar: ");
                    String rutaExportar = scanner.nextLine();
                    // Implementar la lógica de exportación
                    // Por ejemplo, puedes llamar a un método en GestorContactos para exportar
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
