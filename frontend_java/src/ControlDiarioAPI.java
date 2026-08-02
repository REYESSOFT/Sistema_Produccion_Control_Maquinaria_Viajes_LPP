import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlDiarioAPI {

    private static final Gson GSON =
            new Gson();

    private ControlDiarioAPI() {
    }

    public static List<ControlDiarioDAO.ControlDiarioResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario/resumen"
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

        List<ControlDiarioDAO.ControlDiarioResumen> controles =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return controles;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            controles.add(
                    new ControlDiarioDAO.ControlDiarioResumen(
                            obtenerEntero(
                                    item,
                                    "idControl"
                            ),
                            obtenerTexto(
                                    item,
                                    "proyecto"
                            ),
                            obtenerFecha(
                                    item,
                                    "fecha"
                            ),
                            obtenerDecimal(
                                    item,
                                    "metrosLineales"
                            )
                    )
            );
        }

        return controles;
    }
    public static ControlDiarioDAO.ControlDiarioDetalle
        obtenerPorId(
                int idControl
        ) throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/v1/control-diario/"
                            + idControl
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

    JsonObject item =
            respuesta.getAsJsonObject(
                    "datos"
            );

    if (item == null) {

        throw new Exception(
                "No se encontró el Control Diario."
        );
    }

    return new ControlDiarioDAO.ControlDiarioDetalle(

            obtenerEntero(
                    item,
                    "idControl"
            ),

            obtenerEntero(
                    item,
                    "idProyecto"
            ),

            obtenerFecha(
                    item,
                    "fechaControl"
            ),

            obtenerDecimal(
                    item,
                    "metrosLineales"
            ),

            obtenerDoubleNullable(
                    item,
                    "ancho"
            ),

            obtenerDoubleNullable(
                    item,
                    "espesor"
            ),

            obtenerDoubleNullable(
                    item,
                    "volumenReal"
            ),

            obtenerTexto(
                    item,
                    "observaciones"
            )
    );
}
    public static int crearControlDiario(
        Integer idGuia,
        int idProyecto,
        LocalDate fechaControl,
        double metrosLineales,
        Double ancho,
        Double espesor,
        String observaciones
) throws Exception {

    double volumenReal = 0.00;

    if (
            ancho != null
            && espesor != null
    ) {

        volumenReal =
                metrosLineales
                * ancho
                * espesor;
    }

    JsonObject cuerpo =
            new JsonObject();

    agregarEnteroNullable(
            cuerpo,
            "idGuia",
            idGuia
    );

    cuerpo.addProperty(
            "idProyecto",
            idProyecto
    );

    agregarFecha(
            cuerpo,
            "fechaControl",
            fechaControl
    );

    cuerpo.addProperty(
            "metrosLineales",
            metrosLineales
    );

    agregarDoubleNullable(
            cuerpo,
            "ancho",
            ancho
    );

    agregarDoubleNullable(
            cuerpo,
            "espesor",
            espesor
    );

    cuerpo.addProperty(
            "volumenReal",
            volumenReal
    );

    agregarTexto(
            cuerpo,
            "observaciones",
            observaciones
    );

    cuerpo.addProperty(
            "activo",
            true
    );

    String respuestaJson =
            ConexionAPI.post(
                    "/api/v1/control-diario",
                    GSON.toJson(cuerpo)
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

    JsonObject datos =
            respuesta.getAsJsonObject(
                    "datos"
            );

    if (
            datos == null
            || !datos.has("idControl")
            || datos.get("idControl").isJsonNull()
    ) {

        throw new Exception(
                "El Control Diario fue guardado, "
                        + "pero la API no devolvió su ID."
        );
    }

    return datos
            .get("idControl")
            .getAsInt();
}
    public static void actualizarControlDiario(
        int idControl,
        int idProyecto,
        LocalDate fechaControl,
        double metrosLineales,
        Double ancho,
        Double espesor,
        String observaciones
) throws Exception {

    double volumenReal = 0.00;

    if (
            ancho != null
            && espesor != null
    ) {

        volumenReal =
                metrosLineales
                * ancho
                * espesor;
    }

    JsonObject cuerpo =
            new JsonObject();

    cuerpo.addProperty(
            "idProyecto",
            idProyecto
    );

    agregarFecha(
            cuerpo,
            "fechaControl",
            fechaControl
    );

    cuerpo.addProperty(
            "metrosLineales",
            metrosLineales
    );

    agregarDoubleNullable(
            cuerpo,
            "ancho",
            ancho
    );

    agregarDoubleNullable(
            cuerpo,
            "espesor",
            espesor
    );

    cuerpo.addProperty(
            "volumenReal",
            volumenReal
    );

    agregarTexto(
            cuerpo,
            "observaciones",
            observaciones
    );

    cuerpo.addProperty(
            "activo",
            true
    );

    String respuestaJson =
            ConexionAPI.put(
                    "/api/v1/control-diario/"
                            + idControl,
                    GSON.toJson(cuerpo)
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
    public static void eliminarControlDiario(
        int idControl
) throws Exception {

    ConexionAPI.delete(
            "/api/v1/control-diario/" + idControl
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

            return 0.00;
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
        private static Double obtenerDoubleNullable(
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

private static void agregarEnteroNullable(
        JsonObject objeto,
        String propiedad,
        Integer valor
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

private static void agregarDoubleNullable(
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

private static void agregarFecha(
        JsonObject objeto,
        String propiedad,
        LocalDate valor
) {

    if (valor == null) {

        objeto.add(
                propiedad,
                com.google.gson.JsonNull.INSTANCE
        );

    } else {

        objeto.addProperty(
                propiedad,
                valor.toString()
        );
    }
}
}
