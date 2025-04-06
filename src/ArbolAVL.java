import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Comparator;

// Clase Nodo genérica
class Nodo<T extends Comparable<T>> {
    T clave; // Cambia el tipo a T genérico
    Nodo<T> izquierdo;
    Nodo<T> derecho;
    int altura;

    public Nodo(T clave) {
        this.clave = clave;
        this.altura = 1;
    }
}

// Clase ArbolAVL genérica
public class ArbolAVL<T extends Comparable<T>> {
    private Nodo<T> raiz;
    private Comparator<T> comparador; // Comparador para personalizar el orden

    // Constructor que recibe un comparador
    public ArbolAVL(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    // Constructor por defecto
    public ArbolAVL() {

    }

    // Método para calcular la altura de un nodo
    private int altura(Nodo<T> nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    // Método para calcular el factor de balance de un nodo
    private int factorBalance(Nodo<T> nodo) {
        return (nodo == null) ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    // Rotación hacia la derecha
    private Nodo<T> rotarDerecha(Nodo<T> y) {
        Nodo<T> x = y.izquierdo;
        Nodo<T> T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    // Rotación hacia la izquierda
    private Nodo<T> rotarIzquierda(Nodo<T> x) {
        Nodo<T> y = x.derecho;
        Nodo<T> T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    // Método para insertar un nuevo nodo
    public void insertar(T clave) {
        raiz = insertar(raiz, clave);
    }

    private Nodo<T> insertar(Nodo<T> nodo, T clave) {
        if (nodo == null) {
            return new Nodo<>(clave);
        }

        // Comparar para decidir si ir a la izquierda o derecha
        if (comparador.compare(clave, nodo.clave) < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, clave);
        } else if (comparador.compare(clave, nodo.clave) > 0) {
            nodo.derecho = insertar(nodo.derecho, clave);
        } else {
            return nodo; // Clave duplicada no permitida
        }

        // Actualizar la altura del nodo
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));

        // Calcular el factor de balance
        int balance = factorBalance(nodo);

        // Rotaciones
        if (balance > 1 && comparador.compare(clave, nodo.izquierdo.clave) < 0) {
            return rotarDerecha(nodo);
        }
        if (balance < -1 && comparador.compare(clave, nodo.derecho.clave) > 0) {
            return rotarIzquierda(nodo);
        }
        if (balance > 1 && comparador.compare(clave, nodo.izquierdo.clave) > 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }
        if (balance < -1 && comparador.compare(clave, nodo.derecho.clave) < 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    // Método para buscar un nodo
    public boolean buscar(T clave) {
        return buscar(raiz, clave);
    }

    private boolean buscar(Nodo<T> nodo, T clave) {
        if (nodo == null) return false;
        if (comparador.compare(clave, nodo.clave) < 0) return buscar(nodo.izquierdo, clave);
        else if (comparador.compare(clave, nodo.clave) > 0) return buscar(nodo.derecho, clave);
        else return true;
    }

    // Método BFS (Recorrido por Nivel)
    public List<T> recorridoPorNivel() {
        List<T> resultado = new ArrayList<>();
        if (raiz == null) return resultado;

        Queue<Nodo<T>> cola = new LinkedList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();
            resultado.add(actual.clave);

            if (actual.izquierdo != null) cola.add(actual.izquierdo);
            if (actual.derecho != null) cola.add(actual.derecho);
        }

        return resultado;
    }

    public void eliminar(T valor) {
    }
}
