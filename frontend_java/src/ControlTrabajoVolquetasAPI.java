import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlTrabajoVolquetasAPI {

    private static final Gson GSON = new Gson();

    private ControlTrabajoVolquetasAPI() {
    }

    public record Turno(
            String turno,
            String horaInicio,
            String horaFin,
            double totalHoras
    ) {
    }

    public record Paralizacion(
            int numeroFila,
            int codigo,
            String descripcion,
            String horaInicio,
            String horaFin,
            double totalHoras
    ) {
    }

    public record GuiaGuardar(
            Integer idGuia,
            String numeroGuia,
            LocalDate fecha,
            String cliente,
            String solicitante,
            String placa,
            String choferOperador,
            String sector,
            String observaciones,
            String encargadoObra,
            List<Turno> turnos,
            List<Paralizacion> paralizaciones
    ) {
    }

    public record GuiaDetalle(
            int idGuia,
            String empresa,
            String tipoGuia,
            String numeroGuia,
            LocalDate fecha,
            String cliente,
            String solicitante,
            String placa,
            String choferOperador,
            String sector,
            String observaciones,
            String encargadoObra,
            String estado,
            List<Turno> turnos,
            List<Paralizacion> paralizaciones
    ) {
    }

    public static GuiaDetalle guardar(
            GuiaGuardar guia
    ) throws Exception {

        if (guia == null) {
            throw new IllegalArgumentException(
                    "Los datos de la guía son obligatorios."
            );
        }

        JsonObject cuerpo = new JsonObject();

        cuerpo.addProperty(
                "numeroGuia",
                guia.numeroGuia()
        );

        cuerpo.addProperty(
                "fecha",
                guia.fecha() == null
                        ? null
                        : guia.fecha().toString()
        );

        cuerpo.addProperty(
                "cliente",
                guia.cliente()
        );

        cuerpo.addProperty(
                "solicitante",
                guia.solicitante()
        );

        cuerpo.addProperty(
                "placa",
                guia.placa()
        );

        cuerpo.addProperty(
                "choferOperador",
                guia.choferOperador()
        );

        cuerpo.addProperty(
                "sector",
                guia.sector()
        );

        cuerpo.addProperty(
                "observaciones",
                guia.observaciones()
        );

        cuerpo.addProperty(
                "encargadoObra",
                guia.encargadoObra()
        );

        JsonArray turnosJson = new JsonArray();

        if (guia.turnos() != null) {

            for (Turno turno : guia.turnos()) {

                JsonObject item = new JsonObject();

                item.addProperty(
                        "turno",
                        turno.turno()
                );

                item.addProperty(
                        "horaInicio",
                        turno.horaInicio()
                );

                item.addProperty(
                        "horaFin",
                        turno.horaFin()
                );

                item.addProperty(
                        "totalHoras",
                        turno.totalHoras()
                );

                turnosJson.add(item);
            }
        }

        cuerpo.add(
                "turnos",
                turnosJson
        );

        JsonArray paralizacionesJson = new JsonArray();

        if (guia.paralizaciones() != null) {

            for (Paralizacion paralizacion : guia.paralizaciones()) {

                JsonObject item = new JsonObject();

                item.addProperty(
                        "numeroFila",
                        paralizacion.numeroFila()
                );

                item.addProperty(
                        "codigo",
                        paralizacion.codigo()
                );

                item.addProperty(
                        "descripcion",
                        paralizacion.descripcion()
                );

                item.addProperty(
                        "horaInicio",
                        paralizacion.horaInicio()
                );

                item.addProperty(
                        "horaFin",
                        paralizacion.horaFin()
                );

                item.addProperty(
                        "totalHoras",
                        paralizacion.totalHoras()
                );

                paralizacionesJson.add(item);
            }
        }

        cuerpo.add(
                "paralizaciones",
                paralizacionesJson
        );

        String respuestaJson;

        if (guia.idGuia() == null) {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/guias/control-trabajo-volquetas",
                            GSON.toJson(cuerpo)
                    );

        } else {

            respuestaJson =
                    ConexionAPI.put(
                            "/api/v1/guias/control-trabajo-volquetas/"
                                    + guia.idGuia(),
                            GSON.toJson(cuerpo)
                    );
        }

        return convertirRespuesta(
                respuestaJson
        );
    }

    public static GuiaDetalle obtenerDetalle(
            String empresa,
            String numeroGuia,
            String tipoGuia
    ) throws Exception {

        String ruta =
                "/api/v1/guias/control-trabajo-volquetas/detalle"
                        + "?empresa="
                        + java.net.URLEncoder.encode(
                                empresa,
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                        + "&numeroGuia="
                        + java.net.URLEncoder.encode(
                                numeroGuia,
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                        + "&tipoGuia="
                        + java.net.URLEncoder.encode(
                                tipoGuia,
                                java.nio.charset.StandardCharsets.UTF_8
                        );

        String respuestaJson =
                ConexionAPI.get(
                        ruta
                );

        return convertirRespuesta(
                respuestaJson
        );
    }

    private static GuiaDetalle convertirRespuesta(
            String respuestaJson
    ) throws Exception {

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        if (
                respuesta == null
                        || !respuesta.has("exito")
                        || !respuesta.get("exito").getAsBoolean()
        ) {

            throw new Exception(
                    obtenerMensajeError(respuesta)
            );
        }

        JsonObject datos =
                respuesta.getAsJsonObject("datos");

        if (datos == null) {

            throw new Exception(
                    "La API no devolvió los datos de la guía."
            );
        }

        List<Turno> turnos = new ArrayList<>();

        JsonArray arregloTurnos =
                datos.getAsJsonArray("turnos");

        if (arregloTurnos != null) {

            for (JsonElement elemento : arregloTurnos) {

                JsonObject item =
                        elemento.getAsJsonObject();

                turnos.add(
                        new Turno(
                                obtenerTexto(
                                        item,
                                        "turno"
                                ),
                                obtenerTexto(
                                        item,
                                        "horaInicio"
                                ),
                                obtenerTexto(
                                        item,
                                        "horaFin"
                                ),
                                obtenerDecimal(
                                        item,
                                        "totalHoras"
                                )
                        )
                );
            }
        }

        List<Paralizacion> paralizaciones =
                new ArrayList<>();

        JsonArray arregloParalizaciones =
                datos.getAsJsonArray("paralizaciones");

        if (arregloParalizaciones != null) {

            for (JsonElement elemento : arregloParalizaciones) {

                JsonObject item =
                        elemento.getAsJsonObject();

                paralizaciones.add(
                        new Paralizacion(
                                obtenerEntero(
                                        item,
                                        "numeroFila"
                                ),
                                obtenerEntero(
                                        item,
                                        "codigo"
                                ),
                                obtenerTexto(
                                        item,
                                        "descripcion"
                                ),
                                obtenerTexto(
                                        item,
                                        "horaInicio"
                                ),
                                obtenerTexto(
                                        item,
                                        "horaFin"
                                ),
                                obtenerDecimal(
                                        item,
                                        "totalHoras"
                                )
                        )
                );
            }
        }

        return new GuiaDetalle(
                obtenerEntero(
                        datos,
                        "idGuia"
                ),
                obtenerTexto(
                        datos,
                        "empresa"
                ),
                obtenerTexto(
                        datos,
                        "tipoGuia"
                ),
                obtenerTexto(
                        datos,
                        "numeroGuia"
                ),
                obtenerFecha(
                        datos,
                        "fecha"
                ),
                obtenerTexto(
                        datos,
                        "cliente"
                ),
                obtenerTexto(
                        datos,
                        "solicitante"
                ),
                obtenerTexto(
                        datos,
                        "placa"
                ),
                obtenerTexto(
                        datos,
                        "choferOperador"
                ),
                obtenerTexto(
                        datos,
                        "sector"
                ),
                obtenerTexto(
                        datos,
                        "observaciones"
                ),
                obtenerTexto(
                        datos,
                        "encargadoObra"
                ),
                obtenerTexto(
                        datos,
                        "estado"
                ),
                turnos,
                paralizaciones
        );
    }

    private static String obtenerMensajeError(
            JsonObject respuesta
    ) {

        if (
                respuesta != null
                        && respuesta.has("mensaje")
                        && !respuesta.get("mensaje").isJsonNull()
        ) {

            return respuesta
                    .get("mensaje")
                    .getAsString();
        }

        return "La API no devolvió una respuesta válida.";
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

    private static int obtenerEntero(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                        || !objeto.has(propiedad)
                        || objeto.get(propiedad).isJsonNull()
        ) {

            return 0;
        }

        return objeto
                .get(propiedad)
                .getAsInt();
    }

    private static double obtenerDecimal(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                        || !objeto.has(propiedad)
                        || objeto.get(propiedad).isJsonNull()
        ) {

            return 0.0;
        }

        return objeto
                .get(propiedad)
                .getAsDouble();
    }

    private static LocalDate obtenerFecha(
            JsonObject objeto,
            String propiedad
    ) {

        String valor =
                obtenerTexto(
                        objeto,
                        propiedad
                );

        if (valor.isBlank()) {
            return null;
        }

        return LocalDate.parse(valor);
    }
}
