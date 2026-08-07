public record TipoActividadItem(
        int idTipoActividad,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}
