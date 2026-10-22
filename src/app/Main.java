package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;
import negocio.NodoLibro;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola de la Biblioteca FISEI.
 * Esta clase solo lee opciones, muestra resultados y llama a las clases de negocio.
 * Los algoritmos de los arboles permanecen en ArbolBiblioteca y CatalogoABB.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static ArbolBiblioteca arbol = DatosPrueba.cargar();

    // ============================================================
    // 1. MENU PRINCIPAL
    // ============================================================
    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            try {
                int opcion = numero("Selecciona una opcion: ");
                switch (opcion) {
                    case 0 -> {
                        titulo("HASTA LUEGO");
                        System.out.println("Gracias por utilizar Biblioteca FISEI.");
                        return;
                    }
                    case 1 -> cargarBiblioteca();
                    case 2 -> mostrarJerarquia();
                    case 3 -> agregarUnidad();
                    case 4 -> buscarUnidad();
                    case 5 -> mostrarEstadisticas();
                    case 6 -> insertarLibro();
                    case 7 -> buscarLibro();
                    case 8 -> modificarLibro();
                    case 9 -> eliminarLibro();
                    case 10 -> mostrarRecorridos();
                    case 11 -> retos();
                    default -> System.out.println("[AVISO] Opcion no valida. Elige un numero del 0 al 11.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("[ERROR] " + ex.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===================================================");
        System.out.println("                  BIBLIOTECA FISEI");
        System.out.println("               SISTEMA DE GESTION");
        System.out.println("===================================================");
        System.out.println("  GESTION DE BIBLIOTECA");
        System.out.println("   1. Cargar biblioteca inicial");
        System.out.println("   2. Mostrar jerarquia");
        System.out.println("   3. Agregar unidad");
        System.out.println("   4. Buscar unidad");
        System.out.println("   5. Ver estadisticas");
        System.out.println("---------------------------------------------------");
        System.out.println("  GESTION DE LIBROS");
        System.out.println("   6. Insertar libro");
        System.out.println("   7. Buscar libro");
        System.out.println("   8. Modificar libro");
        System.out.println("   9. Eliminar libro");
        System.out.println("---------------------------------------------------");
        System.out.println("  RECORRIDOS Y ANALISIS");
        System.out.println("  10. Recorridos y trazas del ABB");
        System.out.println("  11. Retos del ABB");
        System.out.println("---------------------------------------------------");
        System.out.println("   0. Salir");
        System.out.println("===================================================");
    }

    // ============================================================
    // 2. UTILIDADES PARA MOSTRAR MENSAJES Y LEER DATOS
    // ============================================================
    private static void titulo(String nombre) {
        System.out.println("\n===================================================");
        System.out.println("  " + nombre);
        System.out.println("===================================================");
    }

    private static void subtitulo(String nombre) {
        System.out.println("\n--- " + nombre + " ---");
    }

    private static String texto(String pregunta) {
        System.out.print(pregunta);
        return sc.nextLine().trim();
    }

    private static int numero(String pregunta) {
        while (true) {
            try {
                return Integer.parseInt(texto(pregunta));
            } catch (NumberFormatException ex) {
                System.out.println("[AVISO] Escribe un numero entero valido.");
            }
        }
    }

    private static void resultado(boolean correcto, String exito, String fallo) {
        System.out.println(correcto ? "[OK] " + exito : "[AVISO] " + fallo);
    }

    private static void mostrarLibro(Libro libro) {
        System.out.println("  Codigo : " + libro.getCodigo());
        System.out.println("  Titulo : " + libro.getTitulo());
        System.out.println("  Autor  : " + libro.getAutor());
        System.out.println("  Anio   : " + libro.getAnio());
    }

    private static Libro leerLibro() {
        int codigo = numero("Codigo: ");
        String nombre = texto("Titulo: ");
        String autor = texto("Autor: ");
        int anio = numero("Anio: ");
        return new Libro(codigo, nombre, autor, anio);
    }

    // Devuelve el ABB del estante elegido o null cuando no existe.
    private static CatalogoABB seleccionarEstante() {
        subtitulo("SELECCIONAR ESTANTE");
        System.out.println("Elige uno de estos estantes (no escribas el numero del menu):");
        listarEstantes(arbol.getRaiz());
        NodoUnidad nodo = arbol.buscar(texto("Codigo de estante: "));
        if (nodo == null || nodo.getCatalogo() == null) {
            System.out.println("[AVISO] El estante no existe o el codigo no corresponde a un estante.");
            return null;
        }
        return nodo.getCatalogo();
    }

    // LISTADOS DE APOYO: muestran opciones reales antes de pedir codigos.
    private static void mostrarUnidades() {
        subtitulo("UNIDADES DISPONIBLES");
        System.out.print(arbol.mostrarJerarquia());
    }

    private static void listarEstantes(NodoUnidad actual) {
        if (actual.getCatalogo() != null) {
            System.out.printf("  %-10s %-28s (%d libros)%n",
                    actual.getDato().getCodigo(), actual.getDato().getNombre(),
                    actual.getCatalogo().contar());
        }
        for (NodoUnidad hijo : actual.getHijos()) listarEstantes(hijo);
    }

    private static void mostrarCodigos(CatalogoABB abb) {
        subtitulo("LIBROS DISPONIBLES");
        List<Libro> libros = abb.recorrer(TipoRecorrido.INORDEN);
        if (libros.isEmpty()) {
            System.out.println("  (Este estante no tiene libros)");
            return;
        }
        System.out.printf("  %-9s %-23s %-22s %s%n", "CODIGO", "TITULO", "AUTOR", "ANIO");
        System.out.println("  --------------------------------------------------------------");
        for (Libro libro : libros) {
            System.out.printf("  %-9d %-23.23s %-22.22s %d%n",
                    libro.getCodigo(), libro.getTitulo(), libro.getAutor(), libro.getAnio());
        }
    }

    // Clasifica segun los hijos ACTUALES; no supone que el arbol sigue intacto.
    private static void mostrarTiposEliminacion(NodoLibro nodo) {
        if (nodo == null) return;
        boolean izq = nodo.getIzquierdo() != null;
        boolean der = nodo.getDerecho() != null;
        String tipo = !izq && !der ? "hoja" : izq && der ? "dos hijos" : "un hijo";
        System.out.println("  " + nodo.getDato().getCodigo() + " -> " + tipo);
        mostrarTiposEliminacion(nodo.getIzquierdo());
        mostrarTiposEliminacion(nodo.getDerecho());
    }

    // ============================================================
    // 3. GESTION DE LA BIBLIOTECA (OPCIONES 1 A 5)
    // ============================================================
    private static void cargarBiblioteca() {
        titulo("CARGAR BIBLIOTECA INICIAL");
        arbol = DatosPrueba.cargar();
        System.out.println("[OK] Biblioteca inicial cargada correctamente.");
    }

    private static void mostrarJerarquia() {
        titulo("JERARQUIA DE LA BIBLIOTECA");
        System.out.println(arbol.mostrarJerarquia());
    }

    private static void agregarUnidad() {
        titulo("AGREGAR UNIDAD");
        mostrarUnidades();
        System.out.println("Para agregar AREA, el padre debe ser BIBLIOTECA; para ESTANTE, debe ser AREA.");
        String padre = texto("Codigo de la unidad padre: ");
        String codigo = texto("Codigo de la nueva unidad: ");
        String nombre = texto("Nombre de la nueva unidad: ");
        System.out.println("  1. AREA");
        System.out.println("  2. ESTANTE");
        TipoUnidad tipo = switch (numero("Tipo de unidad: ")) {
            case 1 -> TipoUnidad.AREA;
            case 2 -> TipoUnidad.ESTANTE;
            default -> throw new IllegalArgumentException("Tipo de unidad invalido.");
        };
        boolean ok = arbol.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        resultado(ok, "Unidad agregada correctamente.", "No se pudo agregar. Revisa el padre, el tipo y el codigo.");
    }

    private static void buscarUnidad() {
        titulo("BUSCAR UNIDAD");
        mostrarUnidades();
        NodoUnidad nodo = arbol.buscar(texto("Codigo de unidad: "));
        if (nodo == null) {
            System.out.println("[AVISO] No se encontro la unidad.");
        } else {
            subtitulo("UNIDAD ENCONTRADA");
            System.out.println("  " + nodo.getDato());
            System.out.println("  Tipo: " + nodo.getDato().getTipo());
        }
    }

    private static void mostrarEstadisticas() {
        titulo("ESTADISTICAS DE LA BIBLIOTECA");
        System.out.println("  Altura (aristas) : " + arbol.altura());
        System.out.println("  Numero de hojas  : " + arbol.contarHojas());
        System.out.println("  Total de libros  : " + arbol.contarLibrosTotal());
        subtitulo("CONSULTAR NIVEL DE UNA UNIDAD");
        mostrarUnidades();
        String codigo = texto("Codigo de unidad: ");
        int nivel = arbol.nivel(codigo);
        if (nivel == -1) {
            System.out.println("[AVISO] Esa unidad no existe.");
        } else {
            System.out.println("  Nivel de " + codigo.toUpperCase() + ": " + nivel + " (raiz = 0)");
        }
    }

    // ============================================================
    // 4. GESTION DE LIBROS (OPCIONES 6 A 9)
    // ============================================================
    private static void insertarLibro() {
        titulo("INSERTAR LIBRO");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        System.out.println("Introduce un codigo entero positivo que NO este en la tabla.");
        Libro libro = leerLibro();
        resultado(abb.insertar(libro), "Libro insertado correctamente.", "Ya existe un libro con ese codigo.");
    }

    private static void buscarLibro() {
        titulo("BUSCAR LIBRO");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        Libro libro = abb.buscar(numero("Codigo de libro: "));
        if (libro == null) {
            System.out.println("[AVISO] Libro no encontrado.");
        } else {
            subtitulo("DATOS DEL LIBRO");
            mostrarLibro(libro);
        }
    }

    private static void modificarLibro() {
        titulo("MODIFICAR LIBRO");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        int codigo = numero("Codigo del libro a modificar: ");
        if (abb.buscar(codigo) == null) {
            System.out.println("[AVISO] Ese libro no existe.");
            return;
        }
        System.out.println("El codigo del libro no se puede modificar.");
        String nombre = texto("Nuevo titulo: ");
        String autor = texto("Nuevo autor: ");
        int anio = numero("Nuevo anio: ");
        resultado(abb.modificar(codigo, nombre, autor, anio),
                "Datos del libro actualizados.", "No se pudo modificar el libro.");
    }

    private static void eliminarLibro() {
        titulo("ELIMINAR LIBRO");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        subtitulo("TIPO DE CADA NODO EN EL ARBOL ACTUAL");
        System.out.println("  Puedes elegir cualquier codigo existente.");
        mostrarTiposEliminacion(abb.getRaiz());
        System.out.println("  Nota: los tipos pueden cambiar despues de una eliminacion.");
        int codigo = numero("Codigo del libro a eliminar: ");
        resultado(abb.eliminar(codigo), "Libro eliminado correctamente.", "El codigo no existe en este estante.");
    }

    // ============================================================
    // 5. RECORRIDOS Y TRAZAS (OPCION 10)
    // ============================================================
    private static TipoRecorrido elegirRecorrido() {
        subtitulo("TIPO DE RECORRIDO");
        System.out.println("  1. PREORDEN  (raiz, izquierda, derecha)");
        System.out.println("  2. INORDEN   (izquierda, raiz, derecha)");
        System.out.println("  3. POSORDEN (izquierda, derecha, raiz)");
        return switch (numero("Elige un recorrido: ")) {
            case 1 -> TipoRecorrido.PREORDEN;
            case 2 -> TipoRecorrido.INORDEN;
            case 3 -> TipoRecorrido.POSORDEN;
            default -> throw new IllegalArgumentException("Recorrido invalido. Elige 1, 2 o 3.");
        };
    }

    private static void mostrarRecorridos() {
        titulo("RECORRIDOS Y TRAZAS DEL ABB");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        TipoRecorrido tipo = elegirRecorrido();
        List<Libro> libros = abb.recorrer(tipo);

        subtitulo("LIBROS EN EL ORDEN " + tipo);
        if (libros.isEmpty()) {
            System.out.println("  El estante no tiene libros.");
        } else {
            System.out.printf("  %-8s %-26s %-22s %s%n", "CODIGO", "TITULO", "AUTOR", "ANIO");
            System.out.println("  ------------------------------------------------------------------");
            for (Libro libro : libros) {
                System.out.printf("  %-8d %-26.26s %-22.22s %d%n",
                        libro.getCodigo(), libro.getTitulo(), libro.getAutor(), libro.getAnio());
            }
        }
        subtitulo("SECUENCIA DE CODIGOS - " + tipo);
        StringBuilder secuencia = new StringBuilder();
        for (Libro libro : libros) {
            if (secuencia.length() > 0) secuencia.append(" -> ");
            secuencia.append(libro.getCodigo());
        }
        System.out.println("  " + (secuencia.length() == 0 ? "(vacio)" : secuencia));

        subtitulo("TRAZA RECURSIVA - " + tipo);
        System.out.println("  entra = se llama al nodo; VISITA = se procesa; null = regreso.");
        // La traza original se imprime intacta para cumplir el formato del deber.
        System.out.println(abb.trazaRecorrido(tipo));
    }

    // ============================================================
    // 6. RETOS DEL ABB (OPCION 11)
    // ============================================================
    private static void retos() {
        titulo("RETOS DEL ABB");
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        mostrarCodigos(abb);
        subtitulo("SELECCIONA UN RETO");
        System.out.println("  1. Contar libros dentro de un rango");
        System.out.println("  2. Mostrar camino de busqueda");
        System.out.println("  3. Verificar si el arbol es un ABB valido");
        System.out.println("  4. Reconstruir ABB desde un preorden (extra)");
        switch (numero("Elige un reto: ")) {
            case 1 -> {
                subtitulo("CONTEO DE CODIGOS EN RANGO");
                System.out.println("Cuenta codigos existentes entre ambos limites, incluidos los extremos.");
                int a = numero("Codigo inicial: ");
                int b = numero("Codigo final: ");
                System.out.println("  Libros encontrados: " + abb.contarEnRango(a, b));
            }
            case 2 -> {
                subtitulo("CAMINO DE BUSQUEDA");
                System.out.println("Puedes buscar un codigo existente o inexistente para ver la ruta.");
                int codigo = numero("Codigo buscado: ");
                System.out.println("  Nodos recorridos: " + abb.caminoBusqueda(codigo));
            }
            case 3 -> {
                subtitulo("VALIDACION DEL ABB");
                resultado(abb.esABBValido(), "El arbol cumple las reglas del ABB.",
                        "El arbol NO cumple las reglas del ABB.");
            }
            case 4 -> {
                subtitulo("RECONSTRUCCION DESDE PREORDEN");
                System.out.println("Ejemplo valido: 60 35 20 45 80 70 90");
                System.out.println("Se construye un arbol NUEVO; el estante original no cambia.");
                String entrada = texto("Codigos separados por espacios: ");
                if (entrada.isBlank()) throw new IllegalArgumentException("Debes escribir al menos un codigo.");
                String[] partes = entrada.split("\\s+");
                int[] codigos = new int[partes.length];
                for (int i = 0; i < partes.length; i++) {
                    codigos[i] = Integer.parseInt(partes[i]);
                }
                CatalogoABB nuevo = CatalogoABB.desdePreorden(codigos);
                System.out.println("[OK] Arbol reconstruido.");
                subtitulo("RECORRIDO INORDEN DEL NUEVO ABB");
                List<Libro> libros = nuevo.recorrer(TipoRecorrido.INORDEN);
                for (Libro libro : libros) System.out.println("  Codigo: " + libro.getCodigo());
            }
            default -> System.out.println("[AVISO] Reto invalido. Elige 1, 2, 3 o 4.");
        }
    }
}
