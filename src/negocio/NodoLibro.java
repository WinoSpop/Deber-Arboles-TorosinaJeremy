package negocio;
import modelo.Libro;
public class NodoLibro {
    private Libro dato;
    private NodoLibro izquierdo, derecho;
    public NodoLibro(Libro dato) { if (dato == null) throw new IllegalArgumentException("Libro null"); this.dato = dato; }
    public Libro getDato() { return dato; }
    public void setDato(Libro dato) { if (dato == null) throw new IllegalArgumentException("Libro null"); this.dato = dato; }
    public NodoLibro getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoLibro n) { izquierdo = n; }
    public NodoLibro getDerecho() { return derecho; }
    public void setDerecho(NodoLibro n) { derecho = n; }
}
