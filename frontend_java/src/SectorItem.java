public record SectorItem(
        int idSector,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}

