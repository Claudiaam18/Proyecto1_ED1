import java.util.*;
import java.io.*;

/**
 * Clase que gestiona los índices para búsquedas rápidas en los contactos.
 *
 * Permite crear y gestionar índices utilizando árboles BST o AVL para
 * campos específicos de los contactos, facilitando búsquedas eficientes.
 *
 */
public class GestionIndices {
    private Map<String, ArbolAVL<String>> indicesAVL;
    private Map<String, ArbolBST<String>> indicesBST;
    private Map<String, String> tiposIndice;
    private static final String CSV_SEPARATOR = ",";

    /**
     * Constructor de la clase GestionIndices.
     * Inicializa los mapas para almacenar los diferentes tipos de índices.
     */
    public GestionIndices() {
        indicesAVL = new HashMap<>();
        indicesBST = new HashMap<>();
        tiposIndice = new HashMap<>();
    }

    /**
     * Crea un nuevo índice para un campo específico utilizando el tipo de árbol
     * indicado.
     *
     * @param campo     El nombre del campo a indexar (nombre, apellido, etc.)
     * @param tipoArbol El tipo de árbol a utilizar ("AVL" o "BST")
     */
    public void crearIndice(String campo, String tipoArbol) {
        if (!tiposIndice.containsKey(campo)) {
            if ("AVL".equalsIgnoreCase(tipoArbol)) {
                indicesAVL.put(campo, new ArbolAVL<>());
                tiposIndice.put(campo, "AVL");
            } else {
                indicesBST.put(campo, new ArbolBST<>());
                tiposIndice.put(campo, "BST");
            }
        }
    }

    /**
     * Agrega un valor al índice correspondiente al campo especificado.
     *
     * @param campo El nombre del campo indexado
     * @param valor El valor a agregar al índice
     */
    public void agregarAlIndice(String campo, String valor) {
        if (valor == null || valor.isEmpty())
            return;

        if (!tiposIndice.containsKey(campo)) {
            crearIndice(campo, "BST");
        }

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            indicesAVL.get(campo).insertar(valor);
        } else {
            indicesBST.get(campo).insertar(valor);
        }
    }

    /**
     * Busca un valor en el índice correspondiente al campo especificado.
     *
     * @param campo El nombre del campo indexado
     * @param valor El valor a buscar
     * @return true si el valor existe en el índice, false en caso contrario
     */
    public boolean buscarEnIndice(String campo, String valor) {
        if (!tiposIndice.containsKey(campo))
            return false;

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            return indicesAVL.get(campo).buscar(valor);
        } else {
            return indicesBST.get(campo).buscar(valor);
        }
    }

    /**
     * Obtiene una lista con el recorrido por niveles del índice correspondiente al
     * campo.
     *
     * @param campo El nombre del campo indexado
     * @return Una lista de strings con los valores del recorrido por niveles
     */
    public List<String> recorridoPorNivel(String campo) {
        if (!tiposIndice.containsKey(campo))
            return Collections.emptyList();

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            return indicesAVL.get(campo).recorridoPorNivel();
        } else {
            return indicesBST.get(campo).recorridoPorNivel();
        }
    }

    /**
     * Muestra en pantalla el recorrido por niveles del índice correspondiente al
     * campo.
     *
     * @param campo El nombre del campo indexado
     */
    public void mostrarRecorridoPorNivel(String campo) {
        List<String> recorrido = recorridoPorNivel(campo);

        if (recorrido != null && !recorrido.isEmpty()) {
            System.out.println("Recorrido por niveles del índice '" + campo + "':");
            System.out.println(String.join(",", recorrido));
        } else {
            System.out.println("No existe un índice para el campo '" + campo + "' o está vacío.");
        }
    }

    /**
     * Elimina un valor del índice correspondiente al campo especificado.
     *
     * @param campo El nombre del campo indexado
     * @param valor El valor a eliminar
     */
    public void eliminarDelIndice(String campo, String valor) {
        if (!tiposIndice.containsKey(campo))
            return;

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            indicesAVL.get(campo).eliminar(valor);
        } else {
            indicesBST.get(campo).eliminar(valor);
        }
    }

    /**
     * Actualiza un valor en el índice correspondiente al campo especificado.
     * Elimina el valor anterior y agrega el nuevo valor.
     *
     * @param campo         El nombre del campo indexado
     * @param valorAnterior El valor a eliminar
     * @param valorNuevo    El valor a agregar
     */
    public void actualizarIndice(String campo, String valorAnterior, String valorNuevo) {
        if (!tiposIndice.containsKey(campo))
            return;

        if (valorAnterior != null) {
            eliminarDelIndice(campo, valorAnterior);
        }
        if (valorNuevo != null) {
            agregarAlIndice(campo, valorNuevo);
        }
    }

    /**
     * Obtiene el conjunto de campos que tienen índices creados.
     *
     * @return Un conjunto de strings con los nombres de los campos indexados
     */
    public Set<String> getCamposIndexados() {
        return new HashSet<>(tiposIndice.keySet());
    }

    /**
     * Obtiene el tipo de índice utilizado para un campo específico.
     *
     * @param campo El nombre del campo
     * @return El tipo de índice ("AVL" o "BST"), o "N/A" si no existe
     */
    public String getTipoIndice(String campo) {
        return tiposIndice.getOrDefault(campo, "N/A");
    }

    /**
     * Guarda los índices en archivos de texto.
     * Cada índice se guarda en un archivo con formato "campo-tipo.txt".
     *
     * @throws IOException Si ocurre un error al escribir los archivos
     */
    public void guardarIndices() throws IOException {
        for (String campo : tiposIndice.keySet()) {
            String tipo = tiposIndice.get(campo);
            String archivo = campo + "-" + tipo.toLowerCase() + ".txt";
            // Cambiar la ruta para guardar en la carpeta 'src/reportes'
            String archivoCompleto = "reportes/" + archivo;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCompleto))) {
                List<String> valores = recorridoPorNivel(campo);
                if (!valores.isEmpty()) {
                    writer.write(String.join(CSV_SEPARATOR, valores));
                }
            }
        }
    }

    /**
     * Carga índices desde archivos de texto según la configuración proporcionada.
     *
     * @param configIndices Mapa con la configuración de índices (campo -> tipo)
     * @throws IOException siocurre un error al leer los archivos
     */
    public void cargarIndices(Map<String, String> configIndices) throws IOException {
        for (Map.Entry<String, String> entry : configIndices.entrySet()) {
            String campo = entry.getKey();
            String tipo = entry.getValue();
            String archivo = campo + "-" + tipo.toLowerCase() + ".txt";

            crearIndice(campo, tipo);

            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea = reader.readLine();
                if (linea != null && !linea.isEmpty()) {
                    String[] valores = linea.split(CSV_SEPARATOR);
                    for (String valor : valores) {
                        agregarAlIndice(campo, valor);
                    }
                }
            }
        }
    }
}