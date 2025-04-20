import java.util.InputMismatchException;
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
            try {
                System.out.println("\nMenú de Gestión de Contactos:");
                System.out.println("1. Agregar contacto");
                System.out.println("2. Eliminar contacto");
                System.out.println("3. Actualizar contacto");
                System.out.println("4. Visualizar contactos");
                System.out.println("5. Exportar contactos a CSV");
                System.out.println("6. Crear índice en un campo");
                System.out.println("7. Mostrar recorrido por niveles en índice");
                System.out.println("8. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
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
                            gestor.agregarContacto(nombre, apellido, apodo, telefono, email, direccion,
                                    fechaNacimiento);
                            System.out.println("Contacto agregado exitosamente.");
                        } catch (DateTimeParseException e) {
                            System.out.println("Error en el formato de la fecha: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.print("ID del contacto a eliminar: ");
                        int idEliminar = scanner.nextInt();
                        scanner.nextLine();
                        gestor.eliminarContacto(idEliminar);
                        System.out.println("Contacto eliminado.");
                        break;

                    case 3:
                        System.out.print("ID del contacto a actualizar: ");
                        int idActualizar = scanner.nextInt();
                        scanner.nextLine();

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
                            gestor.actualizarContacto(idActualizar, nuevoNombre, nuevoApellido,
                                    nuevoApodo, nuevoTelefono, nuevoEmail, nuevaDireccion, nuevaFechaNacimiento);
                            System.out.println("Contacto actualizado exitosamente.");
                        } catch (DateTimeParseException e) {
                            System.out.println("Error en el formato de la fecha: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.println("Lista de contactos:");
                        gestor.visualizarContactos();
                        break;

                    case 5:
                        System.out.print("Ruta del archivo CSV a exportar: ");
                        String rutaExportar = scanner.nextLine();
                        gestor.exportarContactos(rutaExportar);
                        System.out.println("Contactos exportados correctamente.");
                        break;

                    case 6:
                        System.out.print("Campo para indexar (nombre, apellido, etc.): ");
                        String campo = scanner.nextLine();
                        System.out.print("Tipo de índice (BST/AVL): ");
                        String tipo = scanner.nextLine();
                        if (tipo.equalsIgnoreCase("BST") || tipo.equalsIgnoreCase("AVL")) {
                            gestor.crearIndice(campo, tipo);
                            System.out.println("Índice creado exitosamente.");
                        } else {
                            System.out.println("Tipo de índice inválido. Use BST o AVL.");
                        }
                        break;

                    case 7:
                        System.out.print("Ingrese el campo del índice a mostrar: ");
                        String campoMostrar = scanner.nextLine();
                        gestor.mostrarRecorridoPorNivel(campoMostrar);
                        break;

                    case 8:
                        System.out.println("Saliendo...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }
}