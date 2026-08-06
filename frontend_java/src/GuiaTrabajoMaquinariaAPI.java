import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuiaTrabajoMaquinariaAPI {

    private static final Gson GSON =
            new Gson();

    private GuiaTrabajoMaquinariaAPI() {
    }

    public record Turno(
            String turno,
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
            String tipoMaquina,
            String numeroMaquina,
            String operador,
            double totalHoras,
            String sector,
            String trabajoRealizar,
            boolean chequeoEngrase,
            String horaInicio,
            String horaFin,
            Double horometroInicial,
            Double horometroFinal,
            Double horometroRecorrido,
            String combustible,
            String recibiConforme,
            String observaciones,
            List<Turno> turnos
    ) {
    }

    public record GuiaDetalle(
            int idGuia,
            String empresa,
            String numeroGuia,
            LocalDate fecha,
            String cliente,
            String tipoMaquina,
            String numeroMaquina,
            String operador,
            double totalHoras,
            String sector,
            String trabajoRealizar,
            boolean chequeoEngrase,
            String horaInicio,
            String horaFin,
            Double horometroInicial,
            Double horometroFinal,
            Double horometroRecorrido,
            String combustible,
            String recibiConforme,
            String observaciones,
            String estado,
            List<Turno> turnos
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
                "cliente",
                guia.cliente()
        );

        cuerpo.addProperty(
                "tipoMaquina",
                guia.tipoMaquina()
        );

        cuerpo.addProperty(
                "numeroMaquina",
                guia.numeroMaquina()
        );

        cuerpo.addProperty(
                "operador",
                guia.operador()
        );

        cuerpo.addProperty(
                "totalHoras",
                guia.totalHoras()
        );

        cuerpo.addProperty(
                "sector",
                guia.sector()
        );

        cuerpo.addProperty(
                "trabajoRealizar",
                guia.trabajoRealizar()
        );

        cuerpo.addProperty(
                "chequeoEngrase",
                guia.chequeoEngrase()
        );

        cuerpo.addProperty(
                "horaInicio",
                guia.horaInicio()
        );

        cuerpo.addProperty(
                "horaFin",
                guia.horaFin()
        );

        agregarDecimalOpcional(
                cuerpo,
                "horometroInicial",
                guia.horometroInicial()
        );

        agregarDecimalOpcional(
                cuerpo,
                "horometroFinal",
                guia.horometroFinal()
        );

        agregarDecimalOpcional(
                cuerpo,
                "horometroRecorrido",
                guia.horometroRecorrido()
        );

        cuerpo.addProperty(
                "combustible",
                guia.combustible()
        );

        cuerpo.addProperty(
                "recibiConforme",
                guia.recibiConforme()
        );

        cuerpo.addProperty(
                "observaciones",
                guia.observaciones()
        );

        JsonArray turnosJson =
                new JsonArray();

        if (guia.turnos() != null) {

            for (Turno turno : guia.turnos()) {

                JsonObject item =
                        new JsonObject();

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

        String respuestaJson;

        if (guia.idGuia() == null) {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/guias/trabajo-maquinaria",
                            GSON.toJson(cuerpo)
                    );

        } else {

            respuestaJson =
                    ConexionAPI.put(
                            "/api/v1/guias/trabajo-maquinaria/"
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
            "/api/v1/guias/trabajo-maquinaria/detalle"
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

        List<Turno> turnos =
                new ArrayList<>();

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
                        "tipoMaquina"
                ),
                obtenerTexto(
                        datos,
                        "numeroMaquina"
                ),
                obtenerTexto(
                        datos,
                        "operador"
                ),
                obtenerDecimal(
                        datos,
                        "totalHoras"
                ),
                obtenerTexto(
                        datos,
                        "sector"
                ),
                obtenerTexto(
                        datos,
                        "trabajoRealizar"
                ),
                obtenerBooleano(
                        datos,
                        "chequeoEngrase"
                ),
                obtenerTexto(
                        datos,
                        "horaInicio"
                ),
                obtenerTexto(
                        datos,
                        "horaFin"
                ),
                obtenerDecimalOpcional(
                        datos,
                        "horometroInicial"
                ),
                obtenerDecimalOpcional(
                        datos,
                        "horometroFinal"
                ),
                obtenerDecimalOpcional(
                        datos,
                        "horometroRecorrido"
                ),
                obtenerTexto(
                        datos,
                        "combustible"
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
                turnos
        );
    }

    private static void agregarDecimalOpcional(
            JsonObject objeto,
            String propiedad,
            Double valor
    ) {

        if (valor == null) {
            objeto.add(
                    propiedad,
                    com.google.gson.JsonNull.INSTANCE
            );
        } else {
            objeto.addProperty(
                    propiedad,
                    valor
            );
        }
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

    private static Double obtenerDecimalOpcional(
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
                .getAsDouble();
    }

    private static boolean obtenerBooleano(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {
            return false;
        }

        return objeto
                .get(propiedad)
                .getAsBoolean();
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
