package negocio;
import modelo.UnidadBiblioteca;
import enums.TipoUnidad;
public class ArbolBiblioteca {
    private final NodoUnidad raiz;
    public ArbolBiblioteca(UnidadBiblioteca biblioteca) {
        if (biblioteca == null || biblioteca.getTipo() != TipoUnidad.BIBLIOTECA)
            throw new IllegalArgumentException("La raíz debe ser BIBLIOTECA");
        raiz = new NodoUnidad(biblioteca);
    }
    public NodoUnidad getRaiz() { return raiz; }
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (nueva == null || buscar(nueva.getCodigo()) != null) return false;
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) return false;
        padre.agregarHijo(new NodoUnidad(nueva)); return true;
    }
    public NodoUnidad buscar(String codigo) { return codigo == null ? null : buscar(raiz, codigo.trim()); }
    private NodoUnidad buscar(NodoUnidad actual, String codigo) {
        if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) return actual;
        for (NodoUnidad hijo : actual.getHijos()) {
            NodoUnidad hallado = buscar(hijo, codigo);
            if (hallado != null) return hallado;
        }
        return null;
    }
    public int altura() { return altura(raiz); }
    private int altura(NodoUnidad n) {
        int mayor = 0;
        for (NodoUnidad hijo : n.getHijos()) mayor = Math.max(mayor, 1 + altura(hijo));
        return mayor;
    }
    public int contarHojas() { return contarHojas(raiz); }
    private int contarHojas(NodoUnidad n) {
        if (n.getHijos().isEmpty()) return 1;
        int total = 0;
        for (NodoUnidad hijo : n.getHijos()) total += contarHojas(hijo);
        return total;
    }
    public int nivel(String codigo) { return codigo == null ? -1 : nivel(raiz, codigo.trim(), 0); }
    private int nivel(NodoUnidad n, String codigo, int profundidad) {
        if (n.getDato().getCodigo().equalsIgnoreCase(codigo)) return profundidad;
        for (NodoUnidad hijo : n.getHijos()) {
            int encontrado = nivel(hijo, codigo, profundidad + 1);
            if (encontrado >= 0) return encontrado;
        }
        return -1;
    }
    public String mostrarJerarquia() { StringBuilder sb = new StringBuilder(); mostrar(raiz, 0, sb); return sb.toString(); }
    private void mostrar(NodoUnidad n, int nivel, StringBuilder sb) {
        sb.append("  ".repeat(nivel)).append(n.getDato()).append('\n');
        for (NodoUnidad hijo : n.getHijos()) mostrar(hijo, nivel + 1, sb);
    }
    public int contarLibrosTotal() { return contarLibros(raiz); }
    private int contarLibros(NodoUnidad n) {
        int total = n.getCatalogo() == null ? 0 : n.getCatalogo().contar();
        for (NodoUnidad hijo : n.getHijos()) total += contarLibros(hijo);
        return total;
    }
}
