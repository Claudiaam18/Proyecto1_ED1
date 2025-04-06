import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionIndices {
    private Map<String, ArbolAVL<String>> indices;

    public GestionIndices() {
        indices = new HashMap<>();
    }

    // Método para crear un índice para un campo específico
    public void crearIndice(String campo) {
        if (!indices.containsKey(campo)) {
            indices.put(campo, new ArbolAVL<>(String::compareTo));
            System.out.println("Índice creado para el campo: " + campo);
        } else {
            System.out.println("El índice ya existe para el campo: " + campo);
        }
    }

    // Método para agregar un contacto al índice (valor del campo + id)
    public void agregarAlIndice(String campo, String valor, int id) {
        if (!indices.containsKey(campo)) {
            crearIndice(campo);
        }
        indices.get(campo).insertar(valor); // Modificado para que coincida con la inserción en el ArbolAVL
        System.out.println("Contacto agregado exitosamente al índice: " + campo + " -> " + valor + " con ID: " + id);
    }

    // Método para buscar un contacto en un índice
    public boolean buscarEnIndice(String campo, String valor) {
        if (indices.containsKey(campo)) {
            List<String> valores = indices.get(campo).recorridoPorNivel(); // Modificado para usar recorrido por nivel
            return valores != null && valores.contains(valor); // Verifica si el valor está presente en el recorrido
        } else {
            System.out.println("No existe un índice para el campo: " + campo);
            return false;
        }
    }

    // Método para obtener el recorrido por niveles de un índice
    public List<String> recorridoPorNivel(String campo) {
        if (indices.containsKey(campo)) {
            return indices.get(campo).recorridoPorNivel(); // Modificado para usar recorrido por nivel
        } else {
            System.out.println("No existe un índice para el campo: " + campo);
            return null;
        }
    }

    public void eliminarDelIndice(String campo, String valor, int id) {
        if (indices.containsKey(campo)) {
            indices.get(campo).eliminar(valor); // Modificado para que coincida con la eliminación en el ArbolAVL
            System.out.println("Contacto eliminado del índice: " + campo + " -> " + valor + " con ID: " + id);
        }
    }

    public void actualizarIndice(String campo, String valorAnterior, String valorNuevo, int id) {
        eliminarDelIndice(campo, valorAnterior, id);
        agregarAlIndice(campo, valorNuevo, id);
    }
}
