public class Usuario {

    private final int idUsuario;
    private final String nombreUsuario;
    private final String nombreCompleto;
    private final String rol;
    private final Integer idEmpresa;
    private final String nombreEmpresa;

    public Usuario(
            int idUsuario,
            String nombreUsuario,
            String nombreCompleto,
            String rol,
            Integer idEmpresa,
            String nombreEmpresa
    ) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }

    public boolean esDigitadorGuias() {
        return "DIGITADOR_GUIAS".equalsIgnoreCase(rol);
    }

    public boolean esControlProyeccion() {
        return "CONTROL_PROYECCION".equalsIgnoreCase(rol);
    }

    public boolean perteneceAEmpresa(int empresa) {
        return idEmpresa != null && idEmpresa == empresa;
    }

    public boolean tieneAccesoGlobal() {
        return idEmpresa == null;
    }
}
