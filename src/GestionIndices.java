import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionIndices {
    private Map<String, ArbolAVL> indices;

    public GestionIndices() {
        indices = new HashMap<>();
    }

    // Método para crear un índice para un campo específico
    public void crearIndice(String campo) {
        if (!indices.containsKey(campo)) {
            indices.put(campo, new ArbolAVL());
            System.out.println("Índice creado para el campo: " + campo);
        } else {
            System.out.println("El índice ya existe para el campo: " + campo);
        }
    }

    // Método para agregar un contacto al índice
    public void agregarAlIndice(String campo, int id) {
        if (indices.containsKey(campo)) {
            indices.get(campo).insertar(id);
        } else {
            System.out.println("No existe un índice para el campo: " + campo);
        }
    }

    // Método para buscar un contacto en un índice
    public boolean buscarEnIndice(String campo, int id) {
        if (indices.containsKey(campo)) {
            return indices.get(campo).buscar(id);
        } else {
            System.out.println("No existe un índice para el campo: " + campo);
            return false;
        }
    }

    // Método para obtener el recorrido por niveles de un índice
    public List<Integer> recorridoPorNivel(String campo) {
        if (indices.containsKey(campo)) {
            return indices.get(campo).recorridoPorNivel();
        } else {
            System.out.println("No existe un índice para el campo: " + campo);
            return null;
        }
    }
}