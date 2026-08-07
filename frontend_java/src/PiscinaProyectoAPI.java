import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class PiscinaProyectoAPI {

    private static final Gson GSON =
            new Gson();

    private PiscinaProyectoAPI() {
    }

    public static List<PiscinaResumen>
            obtenerActivas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/piscinas"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<PiscinaResumen> piscinas =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return piscinas;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            piscinas.add(
                    new PiscinaResumen(
                            obtenerEntero(
                                    item,
                                    "idPiscina"
                            ),
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
                                    "nombrePiscina"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcion"
                            )
                    )
            );
        }

        return piscinas;
    }

    public static PiscinaResumen
            obtenerPorId(
                    int idPiscina
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/piscinas/"
                                + idPiscina
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
                    "No se encontró la piscina."
            );
        }

        return new PiscinaResumen(
                obtenerEntero(
                        datos,
                        "idPiscina"
                ),
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
                        "nombrePiscina"
                ),
                obtenerTexto(
                        datos,
                        "descripcion"
                )
        );
    }

    public static int insertar(
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        idSector,
                        nombrePiscina,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/piscinas",
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
                        "idPiscina"
                )
                || datos
                        .get(
                                "idPiscina"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "La piscina fue guardada, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get(
                        "idPiscina"
                )
                .getAsInt();
    }

    public static void actualizar(
            int idPiscina,
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpo(
                        idSector,
                        nombrePiscina,
                        descripcion
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/piscinas/"
                                + idPiscina,
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
            int idPiscina
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/piscinas/"
                        + idPiscina
        );
    }

    private static JsonObject construirCuerpo(
            int idSector,
            String nombrePiscina,
            String descripcion
    ) {

        JsonObject cuerpo =
                new JsonObject();

        cuerpo.addProperty(
                "idSector",
                idSector
        );

        agregarTexto(
                cuerpo,
                "nombrePiscina",
                nombrePiscina
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