public final class SesionUsuario {

    private static Usuario usuarioActual;

    private SesionUsuario() {
        // Evita crear objetos de esta clase.
    }

    public static void iniciarSesion(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario de la sesión no puede ser nulo."
            );
        }

        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
