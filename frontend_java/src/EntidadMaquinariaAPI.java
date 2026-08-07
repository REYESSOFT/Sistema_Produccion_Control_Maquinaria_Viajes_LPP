import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class EntidadMaquinariaAPI {

    private static final Gson GSON =
            new Gson();

    private EntidadMaquinariaAPI() {
    }

    public record EntidadResumen(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String estado
    ) {
    }

    public record EntidadDetalle(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String direccion,
            String observaciones,
            String estado
    ) {
    }

    public static List<EntidadResumen>
            obtenerTodas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/entidades-maquinaria"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<EntidadResumen> entidades =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return entidades;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            entidades.add(
                    new EntidadResumen(
                            obtenerEntero(
                                    item,
                                    "idEntidad"
                            ),
                            obtenerTexto(
                                    item,
                                    "nombre"
                            ),
                            obtenerTexto(
                                    item,
                                    "tipoEntidad"
                            ),
                            obtenerTexto(
                                    item,
                                    "identificacion"
                            ),
                            obtenerTexto(
                                    item,
                                    "telefono"
                            ),
                            obtenerTexto(
                                    item,
                                    "correo"
                            ),
                            obtenerTexto(
                                    item,
                                    "estado"
                            )
                    )
            );
        }

        return entidades;
    }

    public static EntidadDetalle
            obtenerPorId(
                    int idEntidad
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/entidades-maquinaria/"
                                + idEntidad
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (datos == null) {

            throw new Exception(
                    "No se encontró la entidad seleccionada."
            );
        }

        return new EntidadDetalle(
                obtenerEntero(
                        datos,
                        "idEntidad"
                ),
                obtenerTexto(
                        datos,
                        "nombre"
                ),
                obtenerTexto(
                        datos,
                        "tipoEntidad"
                ),
                obtenerTexto(
                        datos,
                        "identificacion"
                ),
                obtenerTexto(
                        datos,
                        "telefono"
                ),
                obtenerTexto(
                        datos,
                        "correo"
                ),
                obtenerTexto(
                        datos,
                        "direccion"
                ),
                obtenerTexto(
                        datos,
                        "observaciones"
                ),
                obtenerTexto(
                        datos,
                        "estado"
                )
        );
    }
    public static int insertar(
        String nombre,
        String tipoEntidad,
        String identificacion,
        String telefono,
        String correo,
        String observaciones
) throws Exception {

    return insertar(
            nombre,
            tipoEntidad,
            identificacion,
            telefono,
            correo,
            null,
            observaciones
    );
}

    public static int insertar(
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String direccion,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombre,
                        tipoEntidad,
                        identificacion,
                        telefono,
                        correo,
                        direccion,
                        observaciones,
                        "ACTIVO"
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/entidades-maquinaria",
                        GSON.toJson(
                                cuerpo
                        )
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (
                datos == null
                || !datos.has(
                        "idEntidad"
                )
                || datos
                        .get(
                                "idEntidad"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "La entidad fue guardada, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get(
                        "idEntidad"
                )
                .getAsInt();
    }
    public static void actualizar(
        int idEntidad,
        String nombre,
        String tipoEntidad,
        String identificacion,
        String telefono,
        String correo,
        String observaciones,
        String estado
) throws Exception {

    actualizar(
            idEntidad,
            nombre,
            tipoEntidad,
            identificacion,
            telefono,
            correo,
            null,
            observaciones,
            estado
    );
}

    public static void actualizar(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String direccion,
            String observaciones,
            String estado
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombre,
                        tipoEntidad,
                        identificacion,
                        telefono,
                        correo,
                        direccion,
                        observaciones,
                        estado
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/entidades-maquinaria/"
                                + idEntidad,
                        GSON.toJson(
                                cuerpo
                        )
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );
    }

    public static void eliminarLogico(
            int idEntidad
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/entidades-maquinaria/"
                        + idEntidad
        );
    }

    private static JsonObject construirCuerpo(
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String direccion,
            String observaciones,
            String estado
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "nombre",
                nombre
        );

        agregarTexto(
                cuerpo,
                "tipoEntidad",
                tipoEntidad
        );

        agregarTexto(
                cuerpo,
                "identificacion",
                identificacion
        );

        agregarTexto(
                cuerpo,
                "telefono",
                telefono
        );

        agregarTexto(
                cuerpo,
                "correo",
                correo
        );

        agregarTexto(
                cuerpo,
                "direccion",
                direccion
        );

        agregarTexto(
                cuerpo,
                "observaciones",
                observaciones
        );

        agregarTexto(
                cuerpo,
                "estado",
                estado
        );

        return cuerpo;
    }

    private static JsonObject convertirRespuesta(
            String respuestaJson
    ) throws Exception {

        if (
                respuestaJson == null
                || respuestaJson.isBlank()
        ) {

            throw new Exception(
                    "La API no devolvió una respuesta."
            );
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

        return respuesta;
    }

    private static void validarRespuesta(
            JsonObject respuesta
    ) throws Exception {

        if (
                respuesta == null
                || !respuesta.has(
                        "exito"
                )
                || respuesta
                        .get(
                                "exito"
                        )
                        .isJsonNull()
                || !respuesta
                        .get(
                                "exito"
                        )
                        .getAsBoolean()
        ) {

            throw new Exception(
                    obtenerMensajeError(
                            respuesta
                    )
            );
        }
    }

    private static JsonArray obtenerArregloDatos(
            JsonObject respuesta
    ) {

        if (
                respuesta == null
                || !respuesta.has(
                        "datos"
                )
                || respuesta
                        .get(
                                "datos"
                        )
                        .isJsonNull()
        ) {

            return null;
        }

        return respuesta
                .getAsJsonArray(
                        "datos"
                );
    }

    private static JsonObject obtenerObjetoDatos(
            JsonObject respuesta
    ) {

        if (
                respuesta == null
                || !respuesta.has(
                        "datos"
                )
                || respuesta
                        .get(
                                "datos"
                        )
                        .isJsonNull()
        ) {

            return null;
        }

        return respuesta
                .getAsJsonObject(
                        "datos"
                );
    }

    private static String obtenerMensajeError(
            JsonObject respuesta
    ) {

        if (
                respuesta != null
                && respuesta.has(
                        "mensaje"
                )
                && !respuesta
                        .get(
                                "mensaje"
                        )
                        .isJsonNull()
        ) {

            return respuesta
                    .get(
                            "mensaje"
                    )
                    .getAsString();
        }

        return "La API no devolvió una respuesta válida.";
    }

    private static int obtenerEntero(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(
                        propiedad
                )
                || objeto
                        .get(
                                propiedad
                        )
                        .isJsonNull()
        ) {

            return 0;
        }

        return objeto
                .get(
                        propiedad
                )
                .getAsInt();
    }

    private static String obtenerTexto(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(
                        propiedad
                )
                || objeto
                        .get(
                                propiedad
                        )
                        .isJsonNull()
        ) {

            return "";
        }

        return objeto
                .get(
                        propiedad
                )
                .getAsString();
    }

    private static void agregarTexto(
            JsonObject objeto,
            String propiedad,
            String valor
    ) {

        if (
                valor == null
                || valor.isBlank()
        ) {

            objeto.add(
                    propiedad,
                    com.google.gson.JsonNull.INSTANCE
            );

        } else {

            objeto.addProperty(
                    propiedad,
                    valor.trim()
            );
        }
    }
}
