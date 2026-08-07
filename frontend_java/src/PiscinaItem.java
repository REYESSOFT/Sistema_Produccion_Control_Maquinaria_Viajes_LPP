public record PiscinaItem(
        int idPiscina,
        int idSector,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}
