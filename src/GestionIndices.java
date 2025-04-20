import java.util.*;
import java.io.*;

public class GestionIndices {
    private Map<String, ArbolAVL<String>> indicesAVL;
    private Map<String, ArbolBST<String>> indicesBST;
    private Map<String, String> tiposIndice;
    private static final String CSV_SEPARATOR = ",";

    public GestionIndices() {
        indicesAVL = new HashMap<>();
        indicesBST = new HashMap<>();
        tiposIndice = new HashMap<>();
    }

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

    public void agregarAlIndice(String campo, String valor) {
        if (valor == null || valor.isEmpty()) return;

        if (!tiposIndice.containsKey(campo)) {
            crearIndice(campo, "BST");
        }

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            indicesAVL.get(campo).insertar(valor);
        } else {
            indicesBST.get(campo).insertar(valor);
        }
    }

    public boolean buscarEnIndice(String campo, String valor) {
        if (!tiposIndice.containsKey(campo)) return false;

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            return indicesAVL.get(campo).buscar(valor);
        } else {
            return indicesBST.get(campo).buscar(valor);
        }
    }

    public List<String> recorridoPorNivel(String campo) {
        if (!tiposIndice.containsKey(campo)) return Collections.emptyList();

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            return indicesAVL.get(campo).recorridoPorNivel();
        } else {
            return indicesBST.get(campo).recorridoPorNivel();
        }
    }

    public void mostrarRecorridoPorNivel(String campo) {
        List<String> recorrido = recorridoPorNivel(campo);

        if (recorrido != null && !recorrido.isEmpty()) {
            System.out.println("Recorrido por niveles del índice '" + campo + "':");
            System.out.println(String.join(", ", recorrido));
        } else {
            System.out.println("No hay un índice para el campo '" + campo + "' o está vacío.");
        }
    }

    public void eliminarDelIndice(String campo, String valor) {
        if (!tiposIndice.containsKey(campo)) return;

        if ("AVL".equalsIgnoreCase(tiposIndice.get(campo))) {
            indicesAVL.get(campo).eliminar(valor);
        } else {
            indicesBST.get(campo).eliminar(valor);
        }
    }

    public void actualizarIndice(String campo, String valorAnterior, String valorNuevo) {
        if (!tiposIndice.containsKey(campo)) return;

        if (valorAnterior != null) {
            eliminarDelIndice(campo, valorAnterior);
        }
        if (valorNuevo != null) {
            agregarAlIndice(campo, valorNuevo);
        }
    }

    public Set<String> getCamposIndexados() {
        return new HashSet<>(tiposIndice.keySet());
    }

    public String getTipoIndice(String campo) {
        return tiposIndice.getOrDefault(campo, "N/A");
    }

    public void guardarIndices() throws IOException {
        for (String campo : tiposIndice.keySet()) {
            String tipo = tiposIndice.get(campo);
            String archivo = campo + "-" + tipo.toLowerCase() + ".txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                List<String> valores = recorridoPorNivel(campo);
                if (!valores.isEmpty()) {
                    writer.write(String.join(CSV_SEPARATOR, valores));
                }
            }
        }
    }

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
