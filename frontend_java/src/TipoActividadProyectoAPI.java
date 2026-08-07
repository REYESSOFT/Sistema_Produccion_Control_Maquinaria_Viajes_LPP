import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class TipoActividadProyectoAPI {

    private static final Gson GSON =
            new Gson();

    private TipoActividadProyectoAPI() {
    }

    public static List<TipoActividadResumen>
            obtenerActivos() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/tipos-actividad-proyecto"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<TipoActividadResumen> actividades =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return actividades;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            actividades.add(
                    new TipoActividadResumen(
                            obtenerEntero(
                                    item,
                                    "idTipoActividad"
                            ),
                            obtenerTexto(
                                    item,
                                    "nombreActividad"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcion"
                            )
                    )
            );
        }

        return actividades;
    }

    public static TipoActividadResumen
            obtenerPorId(
                    int idTipoActividad
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/tipos-actividad-proyecto/"
                                + idTipoActividad
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
                    "No se encontró el tipo de actividad."
            );
        }

        return new TipoActividadResumen(
                obtenerEntero(
                        datos,
                        "idTipoActividad"
                ),
                obtenerTexto(
                        datos,
                        "nombreActividad"
                ),
                obtenerTexto(
                        datos,
                        "descripcion"
                )
        );
    }

    public static int insertar(
            String nombreActividad,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombreActividad,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/tipos-actividad-proyecto",
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
                        "idTipoActividad"
                )
                || datos
                        .get(
                                "idTipoActividad"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "El tipo de actividad fue guardado, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get(
                        "idTipoActividad"
                )
                .getAsInt();
    }

    public static void actualizar(
            int idTipoActividad,
            String nombreActividad,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombreActividad,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/tipos-actividad-proyecto/"
                                + idTipoActividad,
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

    public static void eliminar(
            int idTipoActividad
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/tipos-actividad-proyecto/"
                        + idTipoActividad
        );
    }

    /*
     * Se conserva porque TipoActividadProyectoPage
     * muestra primero cuántos proyectos utilizan
     * el tipo de actividad.
     *
     * La protección definitiva de eliminación
     * permanece también en Spring Boot.
     */
    public static int contarProyectosRelacionados(
            int idTipoActividad
    ) throws Exception {

        int total = 0;

        for (
                ProyectoResumen proyecto
                : ProyectoAPI.obtenerResumen()
        ) {

            ProyectoDetalle detalle =
                    ProyectoAPI.obtenerPorId(
                            proyecto.idProyecto()
                    );

            if (
                    detalle.idTipoActividad() != null
                    && detalle
                            .idTipoActividad()
                            .intValue()
                            == idTipoActividad
            ) {

                total++;
            }
        }

        return total;
    }

    private static JsonObject construirCuerpo(
            String nombreActividad,
            String descripcion
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "nombreActividad",
                nombreActividad
        );

        agregarTexto(
                cuerpo,
                "descripcion",
                descripcion
        );

        cuerpo.addProperty(
                "activo",
                true
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