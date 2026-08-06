import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuiaAPI {

    private static final Gson GSON =
            new Gson();

    private GuiaAPI() {
    }

    public record GuiaResumen(
        String empresa,
        String tipoGuia,
        String numeroGuia,
        LocalDate fecha,
        String choferOperador,
        String placa,
        double m3,
        String estado
) {
}
    public record GuiaProduccionDetalleFila(
        int numeroFila,
        String proyecto,
        String sector,
        String cantera,
        String material,
        String horaOrigen,
        String horaDestino
) {
}

public record GuiaProduccionDetalle(
        int idGuia,
        String empresa,
        String numeroGuia,
        LocalDate fecha,
        String choferOperador,
        String placa,
        double m3,
        String recibiConforme,
        String observaciones,
        String estado,
        List<GuiaProduccionDetalleFila> detalle
) {
}
    public record GuiaProduccionGuardarFila(
        int numeroFila,
        String proyecto,
        String sector,
        String cantera,
        String material,
        String horaOrigen,
        String horaDestino
) {
}

public record GuiaProduccionGuardar(
        Integer idGuia,
        String numeroGuia,
        LocalDate fecha,
        String placa,
        String choferOperador,
        double m3,
        String recibiConforme,
        String observaciones,
        List<GuiaProduccionGuardarFila> detalle
) {
}

    public static List<GuiaResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/guias/resumen"
                );

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

        List<GuiaResumen> guias =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return guias;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            guias.add(
                    new 
                    GuiaResumen(
                            
                            obtenerTexto(
                                    item,
                                    "empresa"
                            ),
                            obtenerTexto(
                                    item,
                                    "tipoGuia"
                            ),
                            obtenerTexto(
                                    item,
                                    "numeroGuia"
                            ),
                            obtenerFecha(
                                    item,
                                    "fecha"
                            ),
                            obtenerTexto(
                                    item,
                                    "choferOperador"
                            ),
                            obtenerTexto(
                                    item,
                                    "placa"
                            ),
                            obtenerDecimal(
                                    item,
                                    "m3"
                            ),
                            obtenerTexto(
                                    item,
                                    "estado"
                            )
                    )
            );
        }

        return guias;
    }
    public static void aprobarGuia(
        String empresa,
        String tipoGuia,
        String numeroGuia
) throws Exception {

    String ruta =
            "/api/v1/guias/aprobar"
            + "?empresa="
            + java.net.URLEncoder.encode(
                    empresa,
                    java.nio.charset.StandardCharsets.UTF_8
            )
            + "&tipoGuia="
            + java.net.URLEncoder.encode(
                    tipoGuia,
                    java.nio.charset.StandardCharsets.UTF_8
            )
            + "&numeroGuia="
            + java.net.URLEncoder.encode(
                    numeroGuia,
                    java.nio.charset.StandardCharsets.UTF_8
            );

    String respuestaJson =
            ConexionAPI.put(
                    ruta,
                    ""
            );

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
}
    public static void eliminarGuia(
        String empresa,
        String tipoGuia,
        String numeroGuia
) throws Exception {

    String ruta =
            "/api/v1/guias"
            + "?empresa="
            + java.net.URLEncoder.encode(
                    empresa,
                    java.nio.charset.StandardCharsets.UTF_8
            )
            + "&tipoGuia="
            + java.net.URLEncoder.encode(
                    tipoGuia,
                    java.nio.charset.StandardCharsets.UTF_8
            )
            + "&numeroGuia="
            + java.net.URLEncoder.encode(
                    numeroGuia,
                    java.nio.charset.StandardCharsets.UTF_8
            );

    ConexionAPI.delete(ruta);
}
    public static GuiaProduccionDetalle obtenerDetalleProduccion(
        String empresa,
        String numeroGuia,
        String tipoGuia
) throws Exception {

    String ruta =
            "/api/v1/guias/produccion/detalle"
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
                "No se encontró la guía seleccionada."
        );
    }

    List<GuiaProduccionDetalleFila> filas =
            new ArrayList<>();

    JsonArray detalle =
            datos.getAsJsonArray("detalle");

    if (detalle != null) {

        for (JsonElement elemento : detalle) {

            JsonObject item =
                    elemento.getAsJsonObject();

            filas.add(
                    new GuiaProduccionDetalleFila(
                            obtenerEntero(
                                    item,
                                    "numeroFila"
                            ),
                            obtenerTexto(
                                    item,
                                    "proyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "sector"
                            ),
                            obtenerTexto(
                                    item,
                                    "cantera"
                            ),
                            obtenerTexto(
                                    item,
                                    "material"
                            ),
                            obtenerTexto(
                                    item,
                                    "horaOrigen"
                            ),
                            obtenerTexto(
                                    item,
                                    "horaDestino"
                            )
                    )
            );
        }
    }

    return new GuiaProduccionDetalle(
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
                    "numeroGuia"
            ),
            obtenerFecha(
                    datos,
                    "fecha"
            ),
            obtenerTexto(
                    datos,
                    "choferOperador"
            ),
            obtenerTexto(
                    datos,
                    "placa"
            ),
            obtenerDecimal(
                    datos,
                    "m3"
            ),
            obtenerTexto(
                    datos,
                    "recibiConforme"
            ),
            obtenerTexto(
                    datos,
                    "observaciones"
            ),
            obtenerTexto(
                    datos,
                    "estado"
            ),
            filas
    );
}
    public static GuiaProduccionDetalle guardarGuiaProduccion(
        GuiaProduccionGuardar guia
) throws Exception {

    if (guia == null) {
        throw new IllegalArgumentException(
                "Los datos de la guía son obligatorios."
        );
    }

    JsonObject cuerpo =
            new JsonObject();

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
            "placa",
            guia.placa()
    );

    cuerpo.addProperty(
            "choferOperador",
            guia.choferOperador()
    );

    cuerpo.addProperty(
            "m3",
            guia.m3()
    );

    cuerpo.addProperty(
            "recibiConforme",
            guia.recibiConforme()
    );

    cuerpo.addProperty(
            "observaciones",
            guia.observaciones()
    );

    JsonArray detalleJson =
            new JsonArray();

    if (guia.detalle() != null) {

        for (
                GuiaProduccionGuardarFila fila
                        : guia.detalle()
        ) {

            JsonObject item =
                    new JsonObject();

            item.addProperty(
                    "numeroFila",
                    fila.numeroFila()
            );

            item.addProperty(
                    "proyecto",
                    fila.proyecto()
            );

            item.addProperty(
                    "sector",
                    fila.sector()
            );

            item.addProperty(
                    "cantera",
                    fila.cantera()
            );

            item.addProperty(
                    "material",
                    fila.material()
            );

            item.addProperty(
                    "horaOrigen",
                    fila.horaOrigen()
            );

            item.addProperty(
                    "horaDestino",
                    fila.horaDestino()
            );

            detalleJson.add(item);
        }
    }

    cuerpo.add(
            "detalle",
            detalleJson
    );

    String respuestaJson;

    if (guia.idGuia() == null) {

        respuestaJson =
                ConexionAPI.post(
                        "/api/v1/guias/produccion",
                        GSON.toJson(cuerpo)
                );

    } else {

        respuestaJson =
                ConexionAPI.put(
                        "/api/v1/guias/produccion/"
                                + guia.idGuia(),
                        GSON.toJson(cuerpo)
                );
    }

    return convertirDetalleRespuesta(
            respuestaJson
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
    private static GuiaProduccionDetalle convertirDetalleRespuesta(
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

    List<GuiaProduccionDetalleFila> filas =
            new ArrayList<>();

    JsonArray detalle =
            datos.getAsJsonArray("detalle");

    if (detalle != null) {

        for (JsonElement elemento : detalle) {

            JsonObject item =
                    elemento.getAsJsonObject();

            filas.add(
                    new GuiaProduccionDetalleFila(
                            obtenerEntero(
                                    item,
                                    "numeroFila"
                            ),
                            obtenerTexto(
                                    item,
                                    "proyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "sector"
                            ),
                            obtenerTexto(
                                    item,
                                    "cantera"
                            ),
                            obtenerTexto(
                                    item,
                                    "material"
                            ),
                            obtenerTexto(
                                    item,
                                    "horaOrigen"
                            ),
                            obtenerTexto(
                                    item,
                                    "horaDestino"
                            )
                    )
            );
        }
    }

    return new GuiaProduccionDetalle(
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
                    "numeroGuia"
            ),
            obtenerFecha(
                    datos,
                    "fecha"
            ),
            obtenerTexto(
                    datos,
                    "choferOperador"
            ),
            obtenerTexto(
                    datos,
                    "placa"
            ),
            obtenerDecimal(
                    datos,
                    "m3"
            ),
            obtenerTexto(
                    datos,
                    "recibiConforme"
            ),
            obtenerTexto(
                    datos,
                    "observaciones"
            ),
            obtenerTexto(
                    datos,
                    "estado"
            ),
            filas
    );
}
}