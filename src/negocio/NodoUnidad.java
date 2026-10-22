package negocio;
import modelo.UnidadBiblioteca;
import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos = new ArrayList<>();
    private final CatalogoABB catalogo;
    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) throw new IllegalArgumentException("Unidad null");
        this.dato = dato;
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }
    public UnidadBiblioteca getDato() { return dato; }
    public List<NodoUnidad> getHijos() { return Collections.unmodifiableList(hijos); }
    public void agregarHijo(NodoUnidad hijo) {
        if (hijo == null || !dato.getTipo().puedeTenerHijo(hijo.getDato().getTipo()))
            throw new IllegalArgumentException("Jerarquía no permitida");
        hijos.add(hijo);
    }
    public CatalogoABB getCatalogo() { return catalogo; }
}
