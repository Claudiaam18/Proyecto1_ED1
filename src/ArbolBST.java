import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de un Árbol Binario de Búsqueda (BST).
 *
 * Esta clase proporciona una estructura de datos jerárquica que mantiene
 * sus elementos ordenados para realizar búsquedas eficientes.
 *
 * @param <T> Tipo de datos que se almacenarán en el árbol, debe implementar
 *            Comparable
 */
public class ArbolBST<T extends Comparable<T>> {
    /**
     * Clase interna que representa un nodo en el árbol BST.
     */
    private class Nodo {
        T clave;
        //punteros a los hijos izquierdos y derechos
        Nodo izquierdo;
        Nodo derecho;

        /**
         * Constructor de un nodo.
         *
         * @param clave Valor a almacenar en el nodo
         */
        public Nodo(T clave) {
            this.clave = clave;
        }
    }

    private Nodo raiz;

    /**
     * Inserta un nuevo valor en el árbol BST.
     *
     * @param clave El valor a insertar
     */
    public void insertar(T clave) {
        raiz = insertar(raiz, clave);
    }

    /**
     * Método recursivo auxiliar para insertar un valor en el árbol.
     *
     * @param nodo  El nodo actual en la recursión
     * @param clave El valor a insertar
     * @return El nodo actualizado después de la inserción
     */
    private Nodo insertar(Nodo nodo, T clave) {
        if (nodo == null) {
            return new Nodo(clave);
        }

        int cmp = clave.compareTo(nodo.clave);

        if (cmp < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            nodo.derecho = insertar(nodo.derecho, clave);
        }

        return nodo;
    }

    /**
     * Busca un valor en el árbol BST.
     *
     * @param clave El valor a buscar
     * @return true si el valor existe en el árbol, false en caso contrario
     */
    public boolean buscar(T clave) {
        return buscar(raiz, clave);
    }

    /**
     * Método recursivo auxiliar para buscar un valor en el árbol.
     *
     * @param nodo  El nodo actual en la recursión
     * @param clave El valor a buscar
     * @return true si el valor existe en el subárbol, false en caso contrario
     */
    private boolean buscar(Nodo nodo, T clave) {
        if (nodo == null)
            return false;

        int cmp = clave.compareTo(nodo.clave);

        if (cmp < 0) {
            return buscar(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            return buscar(nodo.derecho, clave);
        } else {
            return true;
        }
    }

    /**
     * Elimina un valor del árbol BST.
     *
     * @param clave El valor a eliminar
     */
    public void eliminar(T clave) {
        raiz = eliminar(raiz, clave);
    }

    /**
     * Método recursivo auxiliar para eliminar un valor del árbol.
     *
     * @param nodo  El nodo actual en la recursión
     * @param clave El valor a eliminar
     * @return El nodo actualizado después de la eliminación
     */
    private Nodo eliminar(Nodo nodo, T clave) {
        if (nodo == null)
            return null;

        int cmp = clave.compareTo(nodo.clave);

        if (cmp < 0) {
            nodo.izquierdo = eliminar(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            nodo.derecho = eliminar(nodo.derecho, clave);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            } else {
                Nodo sucesor = minimoNodo(nodo.derecho);
                nodo.clave = sucesor.clave;
                nodo.derecho = eliminar(nodo.derecho, sucesor.clave);
            }
        }

        return nodo;
    }

    /**
     * Encuentra el nodo con el valor mínimo en un subárbol.
     *
     * @param nodo La raíz del subárbol
     * @return El nodo con el valor mínimo
     */
    private Nodo minimoNodo(Nodo nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    /**
     * Calcula la altura del árbol.
     *
     * @param nodo El nodo raíz para calcular la altura
     * @return La altura del árbol
     */
    private int altura(Nodo nodo) {
        if (nodo == null)
            return 0;

        int alturaIzq = altura(nodo.izquierdo);
        int alturaDer = altura(nodo.derecho);

        return Math.max(alturaIzq, alturaDer) + 1;
    }

    /**
     * Realiza un recorrido por niveles (BFS) del árbol y devuelve los valores en
     * orden, agregando un "null" después de cada nodo derecho para indicar el
     * cambio.
     *
     * @return Una lista con los valores del árbol en recorrido por niveles
     */
    public List<String> recorridoPorNivel() {
        List<String> resultado = new ArrayList<>();
        if (raiz == null)
            return resultado;

        // Para realizar un recorrido por niveles usando BFS
        java.util.Queue<Nodo> cola = new java.util.LinkedList<>();
        // Para marcar si un nodo es hijo derecho
        java.util.Queue<Boolean> esDerecho = new java.util.LinkedList<>();

        // Iniciar con la raíz (no es hijo derecho de nadie)
        cola.add(raiz);
        esDerecho.add(false);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            boolean esHijoDerecho = esDerecho.poll();

            // Añadir el valor del nodo actual
            if (actual != null) {
                resultado.add(actual.clave.toString());

                // Añadir los hijos a la cola
                if (actual.izquierdo != null) {
                    cola.add(actual.izquierdo);
                    esDerecho.add(false); // Hijo izquierdo
                }

                if (actual.derecho != null) {
                    cola.add(actual.derecho);
                    esDerecho.add(true); // Hijo derecho
                }

                // Añadir un "null" después de un hijo derecho para indicar cambio
                if (esHijoDerecho) {
                    resultado.add("null");
                }
            }
        }

        return resultado;
    }
}