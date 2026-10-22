package app;
import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;
import java.util.List;
import java.util.stream.Collectors;
public class PruebasBiblioteca {
    private static int total = 0, correctas = 0;
    private static void comprobar(String nombre, boolean ok) {
        total++; if (ok) correctas++;
        System.out.println((ok ? "OK   " : "FALLA ") + nombre);
    }
    private static String codigos(CatalogoABB abb, TipoRecorrido tipo) {
        return abb.recorrer(tipo).stream().map(l -> String.valueOf(l.getCodigo())).collect(Collectors.joining(" "));
    }
    public static void main(String[] args) {
        ArbolBiblioteca sola = new ArbolBiblioteca(new UnidadBiblioteca("BIB", "Biblioteca", TipoUnidad.BIBLIOTECA));
        comprobar("Raíz: altura 0", sola.altura() == 0);
        comprobar("Raíz: 1 hoja", sola.contarHojas() == 1);
        ArbolBiblioteca b = DatosPrueba.cargar();
        comprobar("Inicial: altura 2", b.altura() == 2);
        comprobar("Inicial: 4 hojas", b.contarHojas() == 4);
        comprobar("Inicial: 13 libros", b.contarLibrosTotal() == 13);
        comprobar("Niveles 0,1,2,-1", b.nivel("BIB")==0 && b.nivel("sw")==1 && b.nivel("SW-E2")==2 && b.nivel("XYZ")==-1);
        comprobar("Estante directo en biblioteca rechazado", !b.agregar("BIB", new UnidadBiblioteca("ERROR", "Error", TipoUnidad.ESTANTE)));
        comprobar("Código unidad duplicado rechazado", !b.agregar("BIB", new UnidadBiblioteca("sw", "Repetido", TipoUnidad.AREA)));
        CatalogoABB abb = b.buscar("SW-E1").getCatalogo();
        comprobar("Preorden inicial", codigos(abb, TipoRecorrido.PREORDEN).equals("60 35 20 45 40 80 70 75 90"));
        comprobar("Inorden inicial", codigos(abb, TipoRecorrido.INORDEN).equals("20 35 40 45 60 70 75 80 90"));
        comprobar("Posorden inicial", codigos(abb, TipoRecorrido.POSORDEN).equals("20 40 45 35 75 70 90 80 60"));
        comprobar("Código libro duplicado rechazado", !abb.insertar(new Libro(60, "Duplicado", "Autor", 2026)));
        comprobar("Camino a 42 ausente", abb.caminoBusqueda(42).equals(List.of(60,35,45,40)));
        comprobar("Rango inclusivo 40..75", abb.contarEnRango(40,75)==5);
        comprobar("Buscar inexistente", abb.buscar(999)==null);
        comprobar("Modificar inexistente", !abb.modificar(999, "X", "Y", 2026));
        comprobar("Eliminar inexistente", !abb.eliminar(999));
        comprobar("Modificar libro existente", abb.modificar(35, "Nuevo", "Autor", 2020) && abb.buscar(35).getTitulo().equals("Nuevo") && abb.buscar(35).getCodigo()==35);
        boolean excepcion=false;
        try { new Libro(0, "", "", 1899); } catch (IllegalArgumentException ex) { excepcion=true; }
        comprobar("Libro inválido lanza excepción", excepcion);
        comprobar("Inserción de 42", abb.insertar(new Libro(42,"Libro 42","Autor",2026)));
        comprobar("Eliminar hoja 20", abb.eliminar(20));
        comprobar("Eliminar un hijo 70", abb.eliminar(70));
        comprobar("Eliminar dos hijos 60", abb.eliminar(60));
        comprobar("Preorden tras eliminaciones", codigos(abb, TipoRecorrido.PREORDEN).equals("75 35 45 40 42 80 90"));
        comprobar("ABB válido", abb.esABBValido());
        CatalogoABB defectuoso = new CatalogoABB();
        NodoLibro r = new NodoLibro(new Libro(50,"A","B",2026));
        r.setIzquierdo(new NodoLibro(new Libro(30,"A","B",2026)));
        r.getIzquierdo().setDerecho(new NodoLibro(new Libro(60,"A","B",2026)));
        defectuoso.setRaiz(r);
        comprobar("Nieto fuera de rango invalida ABB", !defectuoso.esABBValido());
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(new int[]{60,35,20,45,40,80,70,75,90});
        comprobar("Extra: reconstrucción de preorden", codigos(reconstruido, TipoRecorrido.PREORDEN).equals("60 35 20 45 40 80 70 75 90"));
        boolean malo=false;
        try { CatalogoABB.desdePreorden(new int[]{50,60,40}); } catch (IllegalArgumentException ex) { malo=true; }
        comprobar("Extra: preorden inválido rechazado", malo);
        comprobar("Traza tiene secuencia y retornos nulos", abb.trazaRecorrido(TipoRecorrido.PREORDEN).contains("null -> regresa") && abb.trazaRecorrido(TipoRecorrido.PREORDEN).contains("Secuencia PREORDEN: 75 35 45 40 42 80 90"));
        System.out.println("\n" + correctas + "/" + total + " pruebas OK");
        if (correctas != total) System.exit(1);
    }
}
