package app;
import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;
import java.util.Scanner;
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ArbolBiblioteca arbol = DatosPrueba.cargar();
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== BIBLIOTECA FISEI ===");
            System.out.println("1 Cargar estructura inicial | 2 Jerarquía | 3 Agregar unidad | 4 Buscar unidad");
            System.out.println("5 Estadísticas | 6 Insertar libro | 7 Buscar libro | 8 Modificar libro");
            System.out.println("9 Eliminar libro | 10 Recorridos y trazas | 11 Retos ABB | 0 Salir");
            try {
                int opcion = numero("Opción: ");
                switch (opcion) {
                    case 0 -> { System.out.println("Hasta luego"); return; }
                    case 1 -> { arbol = DatosPrueba.cargar(); System.out.println("Estructura reiniciada"); }
                    case 2 -> System.out.println(arbol.mostrarJerarquia());
                    case 3 -> agregarUnidad();
                    case 4 -> { NodoUnidad n = arbol.buscar(texto("Código de unidad: "));
                                System.out.println(n == null ? "No existe" : n.getDato() + " (" + n.getDato().getTipo() + ")"); }
                    case 5 -> { System.out.printf("Altura: %d | Hojas: %d | Libros: %d%n", arbol.altura(), arbol.contarHojas(), arbol.contarLibrosTotal());
                                System.out.println("Nivel: " + arbol.nivel(texto("Código de unidad: "))); }
                    case 6 -> { CatalogoABB abb = estante(); if (abb != null)
                                System.out.println("Insertado: " + abb.insertar(leerLibro())); }
                    case 7 -> { CatalogoABB abb = estante(); if (abb != null)
                                System.out.println(abb.buscar(numero("Código libro: "))); }
                    case 8 -> { CatalogoABB abb = estante(); if (abb != null) {
                                int k=numero("Código libro: "); String titulo=texto("Título nuevo: "); String autor=texto("Autor nuevo: "); int anio=numero("Año nuevo: ");
                                System.out.println("Modificado: " + abb.modificar(k, titulo, autor, anio)); } }
                    case 9 -> { CatalogoABB abb = estante(); if (abb != null)
                                System.out.println("Eliminado: " + abb.eliminar(numero("Código libro: "))); }
                    case 10 -> { CatalogoABB abb = estante(); if (abb != null) {
                                 TipoRecorrido t = recorrido();
                                 System.out.println("Libros: " + abb.recorrer(t));
                                 System.out.println(abb.trazaRecorrido(t)); } }
                    case 11 -> retos();
                    default -> System.out.println("Opción inválida");
                }
            } catch (IllegalArgumentException ex) { System.out.println("Error: " + ex.getMessage()); }
        }
    }
    private static String texto(String pregunta) { System.out.print(pregunta); return sc.nextLine().trim(); }
    private static int numero(String pregunta) {
        while (true) {
            try { return Integer.parseInt(texto(pregunta)); }
            catch (NumberFormatException e) { System.out.println("Ingresa un entero válido"); }
        }
    }
    private static Libro leerLibro() {
        int codigo=numero("Código: "); String titulo=texto("Título: "); String autor=texto("Autor: "); int anio=numero("Año: ");
        return new Libro(codigo, titulo, autor, anio);
    }
    private static CatalogoABB estante() {
        NodoUnidad n=arbol.buscar(texto("Código de estante (ej. SW-E1): "));
        if (n == null || n.getCatalogo() == null) { System.out.println("No existe ese estante"); return null; }
        return n.getCatalogo();
    }
    private static TipoRecorrido recorrido() {
        System.out.println("1 PREORDEN | 2 INORDEN | 3 POSORDEN");
        return switch(numero("Elige: ")) {
            case 1 -> TipoRecorrido.PREORDEN;
            case 2 -> TipoRecorrido.INORDEN;
            case 3 -> TipoRecorrido.POSORDEN;
            default -> throw new IllegalArgumentException("Recorrido inválido");
        };
    }
    private static void agregarUnidad() {
        String padre=texto("Código padre: "); String codigo=texto("Nuevo código: "); String nombre=texto("Nombre: ");
        System.out.println("1 AREA | 2 ESTANTE");
        TipoUnidad tipo=switch(numero("Tipo: ")) {
            case 1 -> TipoUnidad.AREA;
            case 2 -> TipoUnidad.ESTANTE;
            default -> throw new IllegalArgumentException("Tipo inválido");
        };
        System.out.println("Agregado: " + arbol.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo)));
    }
    private static void retos() {
        CatalogoABB abb = estante(); if (abb == null) return;
        System.out.println("1 Contar rango | 2 Camino búsqueda | 3 Validar ABB | 4 Reconstruir desde preorden");
        switch (numero("Reto: ")) {
            case 1 -> System.out.println("Total: " + abb.contarEnRango(numero("Inicio: "), numero("Fin: ")));
            case 2 -> System.out.println("Camino: " + abb.caminoBusqueda(numero("Código: ")));
            case 3 -> System.out.println("ABB válido: " + abb.esABBValido());
            case 4 -> { String[] entrada = texto("Preorden (enteros separados por espacios): ").trim().split("\\s+");
                        int[] codigos=new int[entrada.length];
                        for(int i=0;i<entrada.length;i++) codigos[i]=Integer.parseInt(entrada[i]);
                        CatalogoABB nuevo=CatalogoABB.desdePreorden(codigos);
                        System.out.println("Reconstruido, inorden: " + nuevo.recorrer(TipoRecorrido.INORDEN)); }
            default -> System.out.println("Reto inválido");
        }
    }
}
