package negocio;
import modelo.Libro;
import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
public class CatalogoABB {
    private NodoLibro raiz;
    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }
    public boolean insertar(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("Libro null");
        if (buscar(libro.getCodigo()) != null) return false;
        raiz = insertar(raiz, libro); return true;
    }
    private NodoLibro insertar(NodoLibro n, Libro libro) {
        if (n == null) return new NodoLibro(libro);
        if (libro.getCodigo() < n.getDato().getCodigo()) n.setIzquierdo(insertar(n.getIzquierdo(), libro));
        else if (libro.getCodigo() > n.getDato().getCodigo()) n.setDerecho(insertar(n.getDerecho(), libro));
        return n;
    }
    public Libro buscar(int codigo) { NodoLibro n = buscar(raiz, codigo); return n == null ? null : n.getDato(); }
    private NodoLibro buscar(NodoLibro n, int codigo) {
        if (n == null || n.getDato().getCodigo() == codigo) return n;
        return codigo < n.getDato().getCodigo() ? buscar(n.getIzquierdo(), codigo) : buscar(n.getDerecho(), codigo);
    }
    public int contar() { return contar(raiz); }
    private int contar(NodoLibro n) { return n == null ? 0 : 1 + contar(n.getIzquierdo()) + contar(n.getDerecho()); }
    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) throw new IllegalArgumentException("Recorrido null");
        List<Libro> lista = new ArrayList<>(); recorrer(raiz, tipo, lista); return lista;
    }
    private void recorrer(NodoLibro n, TipoRecorrido tipo, List<Libro> lista) {
        if (n == null) return;
        if (tipo == TipoRecorrido.PREORDEN) lista.add(n.getDato());
        recorrer(n.getIzquierdo(), tipo, lista);
        if (tipo == TipoRecorrido.INORDEN) lista.add(n.getDato());
        recorrer(n.getDerecho(), tipo, lista);
        if (tipo == TipoRecorrido.POSORDEN) lista.add(n.getDato());
    }
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) throw new IllegalArgumentException("Recorrido null");
        StringBuilder sb = new StringBuilder(); StringBuilder seq = new StringBuilder();
        traza(raiz, tipo, 0, sb, seq);
        sb.append("Secuencia ").append(tipo).append(": ").append(seq.toString().trim());
        return sb.toString();
    }
    private void traza(NodoLibro n, TipoRecorrido tipo, int nivel, StringBuilder sb, StringBuilder seq) {
        String sangria = "  ".repeat(nivel);
        if (n == null) { sb.append(sangria).append("null -> regresa\n"); return; }
        int codigo = n.getDato().getCodigo();
        sb.append(sangria).append("entra ").append(codigo).append('\n');
        if (tipo == TipoRecorrido.PREORDEN) visita(codigo, sangria, sb, seq);
        traza(n.getIzquierdo(), tipo, nivel+1, sb, seq);
        if (tipo == TipoRecorrido.INORDEN) visita(codigo, sangria, sb, seq);
        traza(n.getDerecho(), tipo, nivel+1, sb, seq);
        if (tipo == TipoRecorrido.POSORDEN) visita(codigo, sangria, sb, seq);
    }
    private void visita(int codigo, String sangria, StringBuilder sb, StringBuilder seq) {
        sb.append(sangria).append("VISITA ").append(codigo).append('\n'); seq.append(codigo).append(' ');
    }
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro encontrado = buscar(codigo);
        if (encontrado == null) return false;
        encontrado.actualizar(titulo, autor, anio); return true;
    }
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) return false;
        raiz = eliminar(raiz, codigo); return true;
    }
    private NodoLibro eliminar(NodoLibro n, int codigo) {
        if (n == null) return null;
        int actual = n.getDato().getCodigo();
        if (codigo < actual) n.setIzquierdo(eliminar(n.getIzquierdo(), codigo));
        else if (codigo > actual) n.setDerecho(eliminar(n.getDerecho(), codigo));
        else {
            if (n.getIzquierdo() == null) return n.getDerecho();
            if (n.getDerecho() == null) return n.getIzquierdo();
            NodoLibro sucesor = minimo(n.getDerecho());
            n.setDato(sucesor.getDato());
            n.setDerecho(eliminar(n.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return n;
    }
    private NodoLibro minimo(NodoLibro n) { return n.getIzquierdo() == null ? n : minimo(n.getIzquierdo()); }
    public int contarEnRango(int a, int b) { return a > b ? 0 : contarEnRango(raiz, a, b); }
    private int contarEnRango(NodoLibro n, int a, int b) {
        if (n == null) return 0;
        int k = n.getDato().getCodigo();
        if (k < a) return contarEnRango(n.getDerecho(), a, b);
        if (k > b) return contarEnRango(n.getIzquierdo(), a, b);
        return 1 + contarEnRango(n.getIzquierdo(), a, b) + contarEnRango(n.getDerecho(), a, b);
    }
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>(); camino(raiz, codigo, camino); return camino;
    }
    private void camino(NodoLibro n, int codigo, List<Integer> salida) {
        if (n == null) return;
        int k = n.getDato().getCodigo(); salida.add(k);
        if (codigo < k) camino(n.getIzquierdo(), codigo, salida);
        else if (codigo > k) camino(n.getDerecho(), codigo, salida);
    }
    public boolean esABBValido() { return valido(raiz, Long.MIN_VALUE, Long.MAX_VALUE); }
    private boolean valido(NodoLibro n, long min, long max) {
        if (n == null) return true;
        long k = n.getDato().getCodigo();
        return k > min && k < max && valido(n.getIzquierdo(), min, k) && valido(n.getDerecho(), k, max);
    }
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) throw new IllegalArgumentException("Preorden null");
        CatalogoABB resultado = new CatalogoABB();
        int[] indice = {0};
        resultado.raiz = construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) throw new IllegalArgumentException("Preorden inválido para ABB");
        return resultado;
    }
    private static NodoLibro construir(int[] arr, int[] indice, long min, long max) {
        if (indice[0] == arr.length) return null;
        int k = arr[indice[0]];
        if (k <= min || k >= max) return null;
        if (k <= 0) throw new IllegalArgumentException("Códigos positivos obligatorios");
        indice[0]++;
        NodoLibro n = new NodoLibro(new Libro(k, "Libro " + k, "Por completar", 2026));
        n.setIzquierdo(construir(arr, indice, min, k));
        n.setDerecho(construir(arr, indice, k, max));
        return n;
    }
}
