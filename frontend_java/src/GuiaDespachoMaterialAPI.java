import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.LocalDate;

public class GuiaDespachoMaterialAPI {

    private static final Gson GSON = new Gson();

    private GuiaDespachoMaterialAPI() {
    }

    public record GuiaGuardar(
            Integer idGuia,
            String numeroGuia,
            LocalDate fecha,
            String choferOperador,
            String solicitante,
            String sector,
            String placa,
            double m3,
            String origen,
            String destino,
            String horaEntrada,
            String horaSalida,
            String material,
            String observaciones,
            String recibiConforme
    ) {
    }

    public record GuiaDetalle(
            int idGuia,
            String empresa,
            String tipoGuia,
            String numeroGuia,
            LocalDate fecha,
            String choferOperador,
            String solicitante,
            String sector,
            String placa,
            double m3,
            String origen,
            String destino,
            String horaEntrada,
            String horaSalida,
            String material,
            String observaciones,
            String recibiConforme,
            String estado
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
                "choferOperador",
                guia.choferOperador()
        );

        cuerpo.addProperty(
                "solicitante",
                guia.solicitante()
        );

        cuerpo.addProperty(
                "sector",
                guia.sector()
        );

        cuerpo.addProperty(
                "placa",
                guia.placa()
        );

        cuerpo.addProperty(
                "m3",
                guia.m3()
        );

        cuerpo.addProperty(
                "origen",
                guia.origen()
        );

        cuerpo.addProperty(
                "destino",
                guia.destino()
        );

        cuerpo.addProperty(
                "horaEntrada",
                guia.horaEntrada()
        );

        cuerpo.addProperty(
                "horaSalida",
                guia.horaSalida()
        );

        cuerpo.addProperty(
                "material",
                guia.material()
        );

        cuerpo.addProperty(
                "observaciones",
                guia.observaciones()
        );

        cuerpo.addProperty(
                "recibiConforme",
                guia.recibiConforme()
        );

        String respuestaJson;

        if (guia.idGuia() == null) {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/guias/despacho-material",
                            GSON.toJson(cuerpo)
                    );

        } else {

            respuestaJson =
                    ConexionAPI.put(
                            "/api/v1/guias/despacho-material/"
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
                "/api/v1/guias/despacho-material/detalle"
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
                ConexionAPI.get(ruta);

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

        return new GuiaDetalle(
                obtenerEntero(datos, "idGuia"),
                obtenerTexto(datos, "empresa"),
                obtenerTexto(datos, "tipoGuia"),
                obtenerTexto(datos, "numeroGuia"),
                obtenerFecha(datos, "fecha"),
                obtenerTexto(datos, "choferOperador"),
                obtenerTexto(datos, "solicitante"),
                obtenerTexto(datos, "sector"),
                obtenerTexto(datos, "placa"),
                obtenerDecimal(datos, "m3"),
                obtenerTexto(datos, "origen"),
                obtenerTexto(datos, "destino"),
                obtenerTexto(datos, "horaEntrada"),
                obtenerTexto(datos, "horaSalida"),
                obtenerTexto(datos, "material"),
                obtenerTexto(datos, "observaciones"),
                obtenerTexto(datos, "recibiConforme"),
                obtenerTexto(datos, "estado")
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

