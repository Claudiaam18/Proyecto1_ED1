import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Objects;

/**
 * Clase que representa un contacto en la agenda.
 *
 * Contiene toda la información personal de un contacto como nombre, apellido,
 * teléfono, email, etc., así como métodos para la conversión a/desde CSV.
 *
 */
public class Contacto {
    private int id;
    private String nombre;
    private String apellido;
    private String apodo;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaNacimiento;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor para crear un nuevo contacto.
     *
     * @param id              Identificador único del contacto
     * @param nombre          Nombre del contacto
     * @param apellido        Apellido del contacto
     * @param apodo           Apodo o alias del contacto (puede ser null)
     * @param telefono        Número de teléfono del contacto
     * @param email           Dirección de correo electrónico (debe tener un formato
     *                        válido)
     * @param direccion       Dirección física del contacto
     * @param fechaNacimiento Fecha de nacimiento del contacto
     */
    public Contacto(int id, String nombre, String apellido, String apodo, String telefono,
                    String email, String direccion, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apodo = apodo;
        this.telefono = telefono;
        setEmail(email); // Validación de email
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y Setters
    /**
     * @return El ID del contacto
     */
    public int getId() {
        return id;
    }

    /**
     * @param id El nuevo ID para el contacto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return El nombre del contacto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre El nuevo nombre del contacto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return El apellido del contacto
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido El nuevo apellido del contacto
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return El apodo del contacto
     */
    public String getApodo() {
        return apodo;
    }

    /**
     * @param apodo El nuevo apodo del contacto
     */
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    /**
     * @return El teléfono del contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono El nuevo teléfono del contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return El email del contacto
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece una nueva dirección de correo electrónico, pero solo si es válida.
     *
     * @param email La nueva dirección de correo electrónico
     * @throws IllegalArgumentException si el formato del email no es válido
     */
    public void setEmail(String email) {
        if (validarEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
    }

    /**
     * @return La dirección del contacto
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion La nueva dirección del contacto
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return La fecha de nacimiento del contacto
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento La nueva fecha de nacimiento del contacto
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Valida que una dirección de correo electrónico tenga un formato válido.
     *
     * @param email El email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean validarEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Devuelve una representación en cadena de texto del contacto.
     *
     * @return Una cadena con todos los datos del contacto
     */
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", apodo='" + apodo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento=" + (fechaNacimiento != null ? fechaNacimiento.format(FORMATO_FECHA) : "null") +
                '}';
    }

    /**
     * Convierte el contacto a una línea de texto en formato CSV.
     *
     * @return Una cadena con los datos del contacto en formato CSV
     */
    public String toCSV() {
        return id + "," +
                (nombre != null ? nombre : "") + "," +
                (apellido != null ? apellido : "") + "," +
                (apodo != null ? apodo : "") + "," +
                (telefono != null ? telefono : "") + "," +
                (email != null ? email : "") + "," +
                (direccion != null ? direccion : "") + "," +
                (fechaNacimiento != null ? fechaNacimiento.format(FORMATO_FECHA) : "");
    }

    /**
     * Crea un objeto Contacto a partir de una línea en formato CSV.
     *
     * @param linea La línea en formato CSV con los datos del contacto
     * @return Un nuevo objeto Contacto, o null si la línea es inválida
     */
    public static Contacto fromCSV(String linea) {
        try {
            String[] partes = linea.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if (partes.length < 8) {
                throw new IllegalArgumentException("Línea CSV incompleta: " + linea);
            }

            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            String apellido = partes[2].trim();
            String apodo = partes[3].trim().isEmpty() ? null : partes[3].trim();
            String telefono = partes[4].trim();
            String email = partes[5].trim();
            String direccion = partes[6].trim().isEmpty() ? null : partes[6].trim();

            LocalDate fechaNacimiento = null;
            String fechaStr = partes[7].trim();
            if (!fechaStr.isEmpty()) {
                // Intentar formatear la fecha según el formato esperado (yyyy-MM-dd)
                try {
                    fechaNacimiento = LocalDate.parse(fechaStr, FORMATO_FECHA);
                } catch (DateTimeParseException e) {
                    // Intentar formatear la fecha si está en formato YYYYMMDD
                    if (fechaStr.length() == 8 && fechaStr.matches("\\d{8}")) {
                        int year = Integer.parseInt(fechaStr.substring(0, 4));
                        int month = Integer.parseInt(fechaStr.substring(4, 6));
                        int day = Integer.parseInt(fechaStr.substring(6, 8));
                        fechaNacimiento = LocalDate.of(year, month, day);
                    } else {
                        throw e; // Relanzar la excepción si no se puede parsear
                    }
                }
            }

            return new Contacto(id, nombre, apellido, apodo, telefono, email, direccion,
                    fechaNacimiento);

        } catch (DateTimeParseException e) {
            System.out.println("Error de formato de fecha al importar: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Argumento ilegal al importar: " + e.getMessage());
            return null;
        }
    }

    /**
     * Comparador para ordenar contactos por apellido.
     */
    public static Comparator<Contacto> porApellido = Comparator.comparing(Contacto::getApellido,
            Comparator.nullsFirst(Comparator.naturalOrder()));

    /**
     * Compara este contacto con otro objeto para verificar si son iguales.
     * Dos contactos son considerados iguales si tienen el mismo ID.
     *
     * @param o El objeto a comparar con este contacto
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Contacto))
            return false;
        Contacto contacto = (Contacto) o;
        return id == contacto.id;
    }

    /**
     * Calcula el código hash para este contacto basado en su ID.
     *
     * @return El código hash calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Obtiene el valor de un campo específico del contacto utilizando el nombre del
     * campo.
     *
     * @param campo El nombre del campo a obtener (id, nombre, apellido, etc.)
     * @return El valor del campo solicitado, o null si el campo no existe
     */
    public Object getCampo(String campo) {
        return switch (campo.toLowerCase()) {
            case "id" -> id;
            case "nombre" -> nombre;
            case "apellido" -> apellido;
            case "apodo" -> apodo;
            case "telefono" -> telefono;
            case "email" -> email;
            case "direccion" -> direccion;
            case "fechanacimiento" -> fechaNacimiento;
            default -> null;
        };
    }
}