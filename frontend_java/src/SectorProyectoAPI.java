import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class SectorProyectoAPI {

    private static final Gson GSON =
            new Gson();

    private SectorProyectoAPI() {
    }

    /*
     * ============================================================
     * LISTAR SECTORES ACTIVOS
     * ============================================================
     */
    public static List<SectorResumen>
            obtenerActivos() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/sectores-proyecto"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<SectorResumen> sectores =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return sectores;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            sectores.add(
                    new SectorResumen(
                            obtenerEntero(
                                    item,
                                    "idSector"
                            ),
                            obtenerTexto(
                                    item,
                                    "nombreSector"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcion"
                            )
                    )
            );
        }

        return sectores;
    }

    /*
     * ============================================================
     * OBTENER SECTOR POR ID
     * ============================================================
     */
    public static SectorResumen
            obtenerPorId(
                    int idSector
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/sectores-proyecto/"
                                + idSector
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
                    "No se encontró el sector."
            );
        }

        return new SectorResumen(
                obtenerEntero(
                        datos,
                        "idSector"
                ),
                obtenerTexto(
                        datos,
                        "nombreSector"
                ),
                obtenerTexto(
                        datos,
                        "descripcion"
                )
        );
    }

    /*
     * ============================================================
     * INSERTAR SECTOR
     * ============================================================
     */
    public static int insertar(
            String nombreSector,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombreSector,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/sectores-proyecto",
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
                        "idSector"
                )
                || datos
                        .get(
                                "idSector"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "El sector fue guardado, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get(
                        "idSector"
                )
                .getAsInt();
    }

    /*
     * ============================================================
     * ACTUALIZAR SECTOR
     * ============================================================
     */
    public static void actualizar(
            int idSector,
            String nombreSector,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        nombreSector,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/sectores-proyecto/"
                                + idSector,
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

    /*
     * ============================================================
     * ELIMINAR SECTOR
     * ============================================================
     */
    public static void eliminar(
            int idSector
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/sectores-proyecto/"
                        + idSector
        );
    }

    /*
     * ============================================================
     * CONSTRUIR JSON
     * ============================================================
     */
    private static JsonObject construirCuerpo(
            String nombreSector,
            String descripcion
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "nombreSector",
                nombreSector
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

    /*
     * ============================================================
     * CONVERTIR RESPUESTA JSON
     * ============================================================
     */
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

    /*
     * ============================================================
     * VALIDAR RESPUESTA API
     * ============================================================
     */
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

    /*
     * ============================================================
     * OBTENER ARRAY DE DATOS
     * ============================================================
     */
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

    /*
     * ============================================================
     * OBTENER OBJETO DATOS
     * ============================================================
     */
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

    /*
     * ============================================================
     * MENSAJE DE ERROR
     * ============================================================
     */
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

    /*
     * ============================================================
     * OBTENER ENTERO
     * ============================================================
     */
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

    /*
     * ============================================================
     * OBTENER TEXTO
     * ============================================================
     */
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

    /*
     * ============================================================
     * AGREGAR TEXTO AL JSON
     * ============================================================
     */
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