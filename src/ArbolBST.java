import java.util.ArrayList;
import java.util.List;

public class ArbolBST<T extends Comparable<T>> {
    private class Nodo {
        T clave;
        Nodo izquierdo;
        Nodo derecho;

        public Nodo(T clave) {
            this.clave = clave;
        }
    }

    private Nodo raiz;

    public void insertar(T clave) {
        raiz = insertar(raiz, clave);
    }

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

    public boolean buscar(T clave) {
        return buscar(raiz, clave);
    }

    private boolean buscar(Nodo nodo, T clave) {
        if (nodo == null) return false;

        int cmp = clave.compareTo(nodo.clave);

        if (cmp < 0) {
            return buscar(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            return buscar(nodo.derecho, clave);
        } else {
            return true;
        }
    }

    public void eliminar(T clave) {
        raiz = eliminar(raiz, clave);
    }

    private Nodo eliminar(Nodo nodo, T clave) {
        if (nodo == null) return null;

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

    private Nodo minimoNodo(Nodo nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    public List<T> recorridoPorNivel() {
        List<T> resultado = new ArrayList<>();
        if (raiz == null) return resultado;

        List<Nodo> nivel = new ArrayList<>();
        nivel.add(raiz);

        while (!nivel.isEmpty()) {
            List<Nodo> siguienteNivel = new ArrayList<>();
            for (Nodo actual : nivel) {
                resultado.add(actual.clave);
                if (actual.izquierdo != null) siguienteNivel.add(actual.izquierdo);
                if (actual.derecho != null) siguienteNivel.add(actual.derecho);
            }
            nivel = siguienteNivel;
        }

        return resultado;
    }
}