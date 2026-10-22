package app;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
public final class DatosPrueba {
    private DatosPrueba() {}
    public static ArbolBiblioteca cargar() {
        ArbolBiblioteca b = new ArbolBiblioteca(new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        b.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        b.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        b.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        b.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        b.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        b.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        insertarCodigos(b.buscar("SW-E1").getCatalogo(), new int[]{60,35,80,20,45,70,90,40,75});
        insertarCodigos(b.buscar("SW-E2").getCatalogo(), new int[]{15,10,25});
        insertarCodigos(b.buscar("TI-E1").getCatalogo(), new int[]{50});
        return b;
    }
    public static void insertarCodigos(CatalogoABB abb, int[] codigos) { insertarCodigos(abb, codigos, 0); }
    private static void insertarCodigos(CatalogoABB abb, int[] codigos, int i) {
        if (i >= codigos.length) return;
        int k = codigos[i]; abb.insertar(new Libro(k, "Libro " + k, "Autor de ejemplo", 2026));
        insertarCodigos(abb, codigos, i + 1);
    }
}
