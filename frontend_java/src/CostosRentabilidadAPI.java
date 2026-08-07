import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class CostosRentabilidadAPI {

    private static final Gson GSON =
            new Gson();

    private CostosRentabilidadAPI() {
    }

    public record CostosRentabilidadResumen(
            int idProyecto,
            String codigoProyecto,
            String descripcionProyecto,
            String empresa,

            double metrosContratados,
            double metrosEjecutados,
            double porcentajeAvance,

            double cantidadContratada,
            double volumenAcumulado,
            double costoAcumulado,
            double porcentajeAvanceFisico,
            double porcentajeAvanceContractual,

            double ingreso,
            double costoMaterial,
            double costoTransporte,
            double costoMaquinaria,
            double costoTotal,
            double costoPorMetroLineal,
            double utilidad,
            double utilidadPorMetroLineal,
            double rentabilidad
    ) {
    }

    public static List<CostosRentabilidadResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/costos-rentabilidad"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<CostosRentabilidadResumen> lista =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new CostosRentabilidadResumen(
                            obtenerEntero(
                                    item,
                                    "idProyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "codigoProyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcionProyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "empresa"
                            ),

                            obtenerDouble(
                                    item,
                                    "metrosContratados"
                            ),
                            obtenerDouble(
                                    item,
                                    "metrosEjecutados"
                            ),
                            obtenerDouble(
                                    item,
                                    "porcentajeAvance"
                            ),

                            obtenerDouble(
                                    item,
                                    "cantidadContratada"
                            ),
                            obtenerDouble(
                                    item,
                                    "volumenAcumulado"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoAcumulado"
                            ),
                            obtenerDouble(
                                    item,
                                    "porcentajeAvanceFisico"
                            ),
                            obtenerDouble(
                                    item,
                                    "porcentajeAvanceContractual"
                            ),

                            obtenerDouble(
                                    item,
                                    "ingreso"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoMaterial"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoTransporte"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoMaquinaria"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoTotal"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoPorMetroLineal"
                            ),
                            obtenerDouble(
                                    item,
                                    "utilidad"
                            ),
                            obtenerDouble(
                                    item,
                                    "utilidadPorMetroLineal"
                            ),
                            obtenerDouble(
                                    item,
                                    "rentabilidad"
                            )
                    )
            );
        }

        return lista;
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

    private static double obtenerDouble(
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

            return 0.00;
        }

        return objeto
                .get(
                        propiedad
                )
                .getAsDouble();
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
}
