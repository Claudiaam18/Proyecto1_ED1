import java.util.ArrayList;
import java.util.List;

public class ArbolAVL<T extends Comparable<T>> {
    private class Nodo {
        T clave;
        Nodo izquierdo;
        Nodo derecho;
        int altura;

        public Nodo(T clave) {
            this.clave = clave;
            this.altura = 1;
        }
    }

    private Nodo raiz;

    private int altura(Nodo nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private int factorBalance(Nodo nodo) {
        return (nodo == null) ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    private void actualizarAltura(Nodo nodo) {
        if (nodo != null) {
            nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        }
    }

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
        } else {
            return nodo;
        }

        actualizarAltura(nodo);
        return balancear(nodo, cmp);
    }

    private Nodo balancear(Nodo nodo, int cmp) {
        int balance = factorBalance(nodo);

        if (balance > 1 && cmp < 0) {
            return rotarDerecha(nodo);
        }
        if (balance < -1 && cmp > 0) {
            return rotarIzquierda(nodo);
        }
        if (balance > 1 && cmp > 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }
        if (balance < -1 && cmp < 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
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
            if (nodo.izquierdo == null || nodo.derecho == null) {
                return (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;
            } else {
                Nodo temp = minimoNodo(nodo.derecho);
                nodo.clave = temp.clave;
                nodo.derecho = eliminar(nodo.derecho, temp.clave);
            }
        }

        actualizarAltura(nodo);
        return balancear(nodo, cmp);
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