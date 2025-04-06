import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Nodo {
    int id;
    Nodo izquierdo;
    Nodo derecho;
    int altura;

    public Nodo(int id) {
        this.id = id;
        this.altura = 1;
    }
}

public class ArbolAVL {
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

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    public void insertar(int id) {
        raiz = insertar(raiz, id);
    }

    private Nodo insertar(Nodo nodo, int id) {
        if (nodo == null) {
            return new Nodo(id);
        }

        if (id < nodo.id) {
            nodo.izquierdo = insertar(nodo.izquierdo, id);
        } else if (id > nodo.id) {
            nodo.derecho = insertar(nodo.derecho, id);
        } else {
            return nodo;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int balance = factorBalance(nodo);

        if (balance > 1 && id < nodo.izquierdo.id) {
            return rotarDerecha(nodo);
        }

        if (balance < -1 && id > nodo.derecho.id) {
            return rotarIzquierda(nodo);
        }

        if (balance > 1 && id > nodo.izquierdo.id) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        if (balance < -1 && id < nodo.derecho.id) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public boolean buscar(int id) {
        return buscar(raiz, id);
    }

    private boolean buscar(Nodo nodo, int id) {
        if (nodo == null) return false;
        if (id < nodo.id) return buscar(nodo.izquierdo, id);
        else if (id > nodo.id) return buscar(nodo.derecho, id);
        else return true;
    }

    //método BFS
    public List<Integer> recorridoPorNivel() {
        List<Integer> resultado = new ArrayList<>();
        if (raiz == null) return resultado;

        Queue<Nodo> cola = new LinkedList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            resultado.add(actual.id);

            if (actual.izquierdo != null) cola.add(actual.izquierdo);
            if (actual.derecho != null) cola.add(actual.derecho);
        }

        return resultado;
    }
}
