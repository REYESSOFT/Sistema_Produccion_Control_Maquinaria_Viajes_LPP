public record EmpresaItem(
        int idEmpresa,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}