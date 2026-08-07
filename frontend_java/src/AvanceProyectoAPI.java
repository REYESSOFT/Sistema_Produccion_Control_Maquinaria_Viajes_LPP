import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AvanceProyectoAPI {

    private static final Gson GSON =
            new Gson();

    private AvanceProyectoAPI() {
    }

    public record AvanceProyectoResumen(
            int idProyecto,
            String codigoProyecto,
            String descripcion,
            String empresa,
            LocalDate fechaControl,
            LocalDate fechaInicio,
            Integer diasEstimados,
            double metrosLinealesContratados,
            double avanceMetrosLinealesDiario,
            double metrosLinealesAcumulados,
            double metrosLinealesRestantes,
            Double ancho,
            Double espesor,
            double volumenDiario,
            double volumenAcumulado,
            double horasTrabajadas,
            double metrosCubicosTransportados,
            int cantidadViajes,
            double porcentajeAvanceFisico
    ) {
    }

    public record DashboardResumen(
            int proyectosActivos,
            double metrosLinealesContratados,
            double metrosLinealesEjecutados,
            double volumenEjecutado,
            double porcentajeGeneral
    ) {
    }

    public static List<AvanceProyectoResumen>
            obtenerAvances() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/avance-proyecto"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<AvanceProyectoResumen> lista =
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
                    new AvanceProyectoResumen(
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
                                    "descripcion"
                            ),
                            obtenerTexto(
                                    item,
                                    "empresa"
                            ),
                            obtenerFecha(
                                    item,
                                    "fechaControl"
                            ),
                            obtenerFecha(
                                    item,
                                    "fechaInicio"
                            ),
                            obtenerEnteroNullable(
                                    item,
                                    "diasEstimados"
                            ),
                            obtenerDouble(
                                    item,
                                    "metrosLinealesContratados"
                            ),
                            obtenerDouble(
                                    item,
                                    "avanceMetrosLinealesDiario"
                            ),
                            obtenerDouble(
                                    item,
                                    "metrosLinealesAcumulados"
                            ),
                            obtenerDouble(
                                    item,
                                    "metrosLinealesRestantes"
                            ),
                            obtenerDoubleNullable(
                                    item,
                                    "ancho"
                            ),
                            obtenerDoubleNullable(
                                    item,
                                    "espesor"
                            ),
                            obtenerDouble(
                                    item,
                                    "volumenDiario"
                            ),
                            obtenerDouble(
                                    item,
                                    "volumenAcumulado"
                            ),
                            obtenerDouble(
                                    item,
                                    "horasTrabajadas"
                            ),
                            obtenerDouble(
                                    item,
                                    "metrosCubicosTransportados"
                            ),
                            obtenerEntero(
                                    item,
                                    "cantidadViajes"
                            ),
                            obtenerDouble(
                                    item,
                                    "porcentajeAvanceFisico"
                            )
                    )
            );
        }

        return lista;
    }

    public static DashboardResumen
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/avance-proyecto/resumen"
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

            return new DashboardResumen(
                    0,
                    0,
                    0,
                    0,
                    0
            );
        }

        return new DashboardResumen(
                obtenerEntero(
                        datos,
                        "proyectosActivos"
                ),
                obtenerDouble(
                        datos,
                        "metrosLinealesContratados"
                ),
                obtenerDouble(
                        datos,
                        "metrosLinealesEjecutados"
                ),
                obtenerDouble(
                        datos,
                        "volumenEjecutado"
                ),
                obtenerDouble(
                        datos,
                        "porcentajeGeneral"
                )
        );
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

    private static Integer obtenerEnteroNullable(
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

            return null;
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

    private static Double obtenerDoubleNullable(
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

            return null;
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

    private static LocalDate obtenerFecha(
            JsonObject objeto,
            String propiedad
    ) {

        String texto =
                obtenerTexto(
                        objeto,
                        propiedad
                );

        if (
                texto == null
                || texto.isBlank()
        ) {

            return null;
        }

        return LocalDate.parse(
                texto
        );
    }
}
