package modelo;
public class Libro {
    private final int codigo;
    private String titulo, autor;
    private int anio;
    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) throw new IllegalArgumentException("Código mayor que cero obligatorio");
        validar(titulo, autor, anio);
        this.codigo = codigo; this.titulo = titulo.trim(); this.autor = autor.trim(); this.anio = anio;
    }
    private static void validar(String titulo, String autor, int anio) {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank() || anio < 1900 || anio > 2026)
            throw new IllegalArgumentException("Título, autor y año (1900–2026) válidos son obligatorios");
    }
    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }
    public void actualizar(String titulo, String autor, int anio) {
        validar(titulo, autor, anio);
        this.titulo = titulo.trim(); this.autor = autor.trim(); this.anio = anio;
    }
    @Override public String toString() {
        return codigo + " | " + titulo + " | " + autor + " | " + anio;
    }
}
