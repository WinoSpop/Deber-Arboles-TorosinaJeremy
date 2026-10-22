package modelo;
import enums.TipoUnidad;
import java.util.Locale;
public class UnidadBiblioteca {
    private final String codigo, nombre;
    private final TipoUnidad tipo;
    public UnidadBiblioteca(String codigo, String nombre, TipoUnidad tipo) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank() || tipo == null)
            throw new IllegalArgumentException("Unidad inválida");
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim(); this.tipo = tipo;
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public TipoUnidad getTipo() { return tipo; }
    @Override public String toString() { return codigo + " " + nombre; }
}
