import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de un Árbol AVL (Adelson-Velsky y Landis).
 *
 * Esta clase proporciona una estructura de datos jerárquica autobalanceada
 * que mantiene sus elementos ordenados para realizar búsquedas eficientes,
 * garantizando operaciones en O(log n).
 *
 */
public class ArbolAVL<T extends Comparable<T>> {
    /**
     * Clase interna que representa un nodo en el árbol AVL.
     */
    private class Nodo {
        T clave;
        Nodo izquierdo;
        Nodo derecho;
        int altura;

        /**
         * Constructor de un nodo.
         *
         * @param clave Valor a almacenar en el nodo
         */
        public Nodo(T clave) {
            this.clave = clave;
            this.altura = 1;
        }
    }

    private Nodo raiz;

    /**
     * Calcula la altura de un nodo.
     *
     * @param nodo El nodo del cual se calculará la altura
     * @return La altura del nodo, o 0 si el nodo es nulo
     */
    private int altura(Nodo nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    /**
     * Calcula el factor de balance de un nodo.
     * Factor de balance = altura(subárbol izquierdo) - altura(subárbol derecho)
     *
     * @param nodo El nodo del cual se calculará el factor de balance
     * @return El factor de balance del nodo
     */
    private int factorBalance(Nodo nodo) {
        return (nodo == null) ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    /**
     * Realiza una rotación derecha en el árbol AVL.
     *
     * @param y El nodo sobre el cual se realiza la rotación
     * @return El nuevo nodo raíz después de la rotación
     */
    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    /**
     * Realiza una rotación izquierda en el árbol AVL.
     *
     * @param x El nodo sobre el cual se realiza la rotación
     * @return El nuevo nodo raíz después de la rotación
     */
    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    /**
     * Actualiza la altura de un nodo basándose en la altura de sus hijos.
     *
     * @param nodo El nodo cuya altura será actualizada
     */
    private void actualizarAltura(Nodo nodo) {
        if (nodo != null) {
            nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        }
    }

    /**
     * Inserta un nuevo valor en el árbol AVL.
     *
     * @param clave El valor a insertar
     */
    public void insertar(T clave) {
        raiz = insertar(raiz, clave);
    }

    /**
     * Método recursivo auxiliar para insertar un valor en el árbol
     * y mantener el balance AVL.
     *
     * @param nodo  El nodo actual en la recursión
     * @param clave El valor a insertar
     * @return El nodo actualizado después de la inserción y el balanceo
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
        } else {
            return nodo;
        }

        actualizarAltura(nodo);
        return balancear(nodo);
    }

    /**
     * Balancea un nodo si su factor de balance indica un desequilibrio.
     *
     * @param nodo El nodo a balancear
     * @return El nodo balanceado
     */
    private Nodo balancear(Nodo nodo) {
        if (nodo == null) {
            return null;
        }

        int balance = factorBalance(nodo);

        // Caso Izquierda-Izquierda
        if (balance > 1 && factorBalance(nodo.izquierdo) >= 0) {
            return rotarDerecha(nodo);
        }

        // Caso Izquierda-Derecha
        if (balance > 1 && factorBalance(nodo.izquierdo) < 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // Caso Derecha-Derecha
        if (balance < -1 && factorBalance(nodo.derecho) <= 0) {
            return rotarIzquierda(nodo);
        }

        // Caso Derecha-Izquierda
        if (balance < -1 && factorBalance(nodo.derecho) > 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    /**
     * Busca un valor en el árbol AVL.
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
     * Elimina un valor del árbol AVL.
     *
     * @param clave El valor a eliminar
     */
    public void eliminar(T clave) {
        raiz = eliminar(raiz, clave);
    }

    /**
     * Método recursivo auxiliar para eliminar un valor del árbol
     * y mantener el balance AVL.
     *
     * @param nodo  El nodo actual en la recursión
     * @param clave El valor a eliminar
     * @return El nodo actualizado después de la eliminación y el balanceo
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
            // Caso 1: Nodo hoja o con un solo hijo
            if (nodo.izquierdo == null)
                return nodo.derecho;
            else if (nodo.derecho == null)
                return nodo.izquierdo;

            // Caso 2: Nodo con dos hijos
            // Encontrar el sucesor inorden (el mínimo valor en el subárbol derecho)
            Nodo temp = minimoNodo(nodo.derecho);

            // Copiar el valor del sucesor a este nodo
            nodo.clave = temp.clave;

            // Eliminar el sucesor
            nodo.derecho = eliminar(nodo.derecho, temp.clave);
        }

        // Si el árbol solo tenía un nodo, retornamos
        if (nodo == null)
            return null;

        // Actualizar la altura del nodo actual
        actualizarAltura(nodo);

        // Balancear el árbol
        return balancear(nodo);
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

        java.util.Queue<Nodo> cola = new java.util.LinkedList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            resultado.add(actual.clave);

            if (actual.izquierdo != null) {
                cola.add(actual.izquierdo);
            }

            if (actual.derecho != null) {
                cola.add(actual.derecho);
            }
        }

        return resultado;
    }
}