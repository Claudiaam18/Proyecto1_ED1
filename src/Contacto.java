import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Objects;

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
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (validarEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    // Método para validar el formato de email
    public static boolean validarEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

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

    // Exportación a CSV
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

    // Importación desde CSV
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
            LocalDate fechaNacimiento = partes[7].trim().isEmpty() ? null : LocalDate.parse(partes[7].trim(), FORMATO_FECHA);

            return new Contacto(id, nombre, apellido, apodo, telefono, email, direccion, fechaNacimiento);

        } catch (DateTimeParseException e) {
            System.out.println("Error de formato de fecha al importar: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Argumento ilegal al importar: " + e.getMessage());
            return null;
        }
    }

    // Comparator de ejemplo (ordenar por apellido)
    public static Comparator<Contacto> porApellido = Comparator.comparing(Contacto::getApellido,
            Comparator.nullsFirst(Comparator.naturalOrder()));

    // Equals y hashCode (basado en ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;
        Contacto contacto = (Contacto) o;
        return id == contacto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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
