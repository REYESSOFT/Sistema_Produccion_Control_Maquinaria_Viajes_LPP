import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class UsuarioAPI {

    private static final Gson GSON =
            new Gson();

    private UsuarioAPI() {
        // Evita crear objetos de esta clase.
    }

    public static Usuario autenticar(
            String nombreUsuario,
            String clave
    ) throws Exception {

        validarDatos(
                nombreUsuario,
                clave
        );

        JsonObject cuerpo =
                new JsonObject();

        cuerpo.addProperty(
                "nombreUsuario",
                nombreUsuario.trim()
        );

        cuerpo.addProperty(
                "clave",
                clave
        );

        String respuestaJson;

        try {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/usuarios/autenticar",
                            GSON.toJson(cuerpo)
                    );

        } catch (Exception ex) {

            /*
             * Algunas implementaciones de ConexionAPI lanzan una excepción
             * cuando el servidor responde HTTP 401.
             *
             * En ese caso se interpreta como credenciales incorrectas,
             * no como un fallo técnico del sistema.
             */
            String mensaje =
                    ex.getMessage() == null
                            ? ""
                            : ex.getMessage();

            if (
                    mensaje.contains("401")
                    || mensaje.toLowerCase()
                            .contains(
                                    "usuario o contraseña incorrectos"
                            )
            ) {

                return null;
            }

            throw ex;
        }

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        if (respuesta == null) {

            throw new Exception(
                    "La API no devolvió una respuesta válida."
            );
        }

        boolean exito =
                respuesta.has("exito")
                && !respuesta.get("exito").isJsonNull()
                && respuesta.get("exito").getAsBoolean();

        if (!exito) {

            String mensaje =
                    obtenerTexto(
                            respuesta,
                            "mensaje"
                    );

            if (
                    mensaje.equalsIgnoreCase(
                            "Usuario o contraseña incorrectos."
                    )
            ) {

                return null;
            }

            throw new Exception(
                    mensaje.isBlank()
                            ? "No fue posible iniciar sesión."
                            : mensaje
            );
        }

        JsonObject datos =
                respuesta.getAsJsonObject(
                        "datos"
                );

        if (datos == null) {

            throw new Exception(
                    "La API confirmó el inicio de sesión, "
                            + "pero no devolvió los datos del usuario."
            );
        }

        return new Usuario(
                obtenerEntero(
                        datos,
                        "idUsuario"
                ),
                obtenerTexto(
                        datos,
                        "nombreUsuario"
                ),
                obtenerTexto(
                        datos,
                        "nombreCompleto"
                ),
                obtenerTexto(
                        datos,
                        "rol"
                ),
                obtenerEnteroNullable(
                        datos,
                        "idEmpresa"
                ),
                obtenerTextoNullable(
                        datos,
                        "nombreEmpresa"
                )
        );
    }

    private static void validarDatos(
            String nombreUsuario,
            String clave
    ) {

        if (
                nombreUsuario == null
                || nombreUsuario.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio."
            );
        }

        if (
                clave == null
                || clave.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }
    }

    private static int obtenerEntero(
            JsonObject objeto,
            String propiedad
    ) throws Exception {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            throw new Exception(
                    "La API no devolvió el campo obligatorio: "
                            + propiedad
            );
        }

        return objeto
                .get(propiedad)
                .getAsInt();
    }

    private static Integer obtenerEnteroNullable(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return null;
        }

        return objeto
                .get(propiedad)
                .getAsInt();
    }

    private static String obtenerTexto(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return "";
        }

        return objeto
                .get(propiedad)
                .getAsString();
    }

    private static String obtenerTextoNullable(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return null;
        }

        return objeto
                .get(propiedad)
                .getAsString();
    }
}

