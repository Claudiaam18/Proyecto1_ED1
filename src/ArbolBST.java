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
     * Realiza un recorrido por niveles (BFS) del árbol y devuelve los valores en
     * orden.
     *
     * @return Una lista con los valores del árbol en recorrido por niveles
     */
    public List<T> recorridoPorNivel() {
        List<T> resultado = new ArrayList<>();
        if (raiz == null)
            return resultado;

        List<Nodo> nivel = new ArrayList<>();
        nivel.add(raiz);

        while (!nivel.isEmpty()) {
            List<Nodo> siguienteNivel = new ArrayList<>();
            for (Nodo actual : nivel) {
                resultado.add(actual.clave);
                if (actual.izquierdo != null)
                    siguienteNivel.add(actual.izquierdo);
                if (actual.derecho != null)
                    siguienteNivel.add(actual.derecho);
            }
            nivel = siguienteNivel;
        }

        return resultado;
    }
}