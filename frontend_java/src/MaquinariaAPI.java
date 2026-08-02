import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MaquinariaAPI {

    private static final Gson GSON =
            new Gson();

    private MaquinariaAPI() {
    }

    public static List<MaquinariaDAO.MaquinariaResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/maquinarias/resumen"
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

        List<MaquinariaDAO.MaquinariaResumen> lista =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new MaquinariaDAO.MaquinariaResumen(
                            obtenerEntero(
                                    item,
                                    "idMaquinaria"
                            ),
                            obtenerTexto(
                                    item,
                                    "codigo"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcion"
                            ),
                            obtenerTexto(
                                    item,
                                    "tipoMaquinaria"
                            ),
                            obtenerTexto(
                                    item,
                                    "proveedor"
                            ),
                            obtenerTexto(
                                    item,
                                    "propietario"
                            ),
                            obtenerTexto(
                                    item,
                                    "estadoOperativo"
                            ),
                            obtenerDecimal(
                                    item,
                                    "costoHoraProveedor"
                            )
                    )
            );
        }

        return lista;
    }
    public static int crearMaquinaria(
        String codigoInterno,
        String codigoActual,
        String codigoPlaca,
        String descripcion,
        int idTipoMaquinaria,
        String modelo,
        String serieMaquina,
        String serieActual,
        Double horometroActual,
        boolean horometroConfirmado,
        Integer idProveedor,
        Integer idPropietario,
        String tipoPropiedad,
        String estadoOperativo,
        String tipoCobro,
        double costoHoraProveedor,
        double costoFijoProveedor,
        double precioHoraCliente,
        double precioFijoCliente,
        String observaciones
) throws Exception {

    JsonObject cuerpo =
            new JsonObject();

    agregarTexto(cuerpo, "codigoInterno", codigoInterno);
    agregarTexto(cuerpo, "codigoActual", codigoActual);
    agregarTexto(cuerpo, "codigoPlaca", codigoPlaca);

    cuerpo.addProperty(
            "descripcion",
            descripcion
    );

    cuerpo.addProperty(
            "idTipoMaquinaria",
            idTipoMaquinaria
    );

    agregarTexto(cuerpo, "modelo", modelo);
    agregarTexto(cuerpo, "serieMaquina", serieMaquina);
    agregarTexto(cuerpo, "serieActual", serieActual);

    if (horometroActual == null) {
        cuerpo.add(
                "horometroActual",
                com.google.gson.JsonNull.INSTANCE
        );
    } else {
        cuerpo.addProperty(
                "horometroActual",
                horometroActual
        );
    }

    cuerpo.addProperty(
            "horometroConfirmado",
            horometroConfirmado
    );

    agregarEnteroNullable(
            cuerpo,
            "idProveedor",
            idProveedor
    );

    agregarEnteroNullable(
            cuerpo,
            "idPropietario",
            idPropietario
    );

    cuerpo.addProperty(
            "tipoPropiedad",
            tipoPropiedad
    );

    cuerpo.addProperty(
            "estadoOperativo",
            estadoOperativo
    );

    cuerpo.addProperty(
            "tipoCobro",
            tipoCobro
    );

    cuerpo.addProperty(
            "costoHoraProveedor",
            costoHoraProveedor
    );

    cuerpo.addProperty(
            "costoFijoProveedor",
            costoFijoProveedor
    );

    cuerpo.addProperty(
            "precioHoraCliente",
            precioHoraCliente
    );

    cuerpo.addProperty(
            "precioFijoCliente",
            precioFijoCliente
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
                    "/api/maquinarias",
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
            respuesta.getAsJsonObject("datos");

    if (
            datos == null
            || !datos.has("idMaquinaria")
            || datos.get("idMaquinaria").isJsonNull()
    ) {

        throw new Exception(
                "La maquinaria fue guardada, pero la API no devolvió su ID."
        );
    }

    return datos
            .get("idMaquinaria")
            .getAsInt();
}
public static void actualizarMaquinaria(
        int idMaquinaria,
        String codigoInterno,
        String codigoActual,
        String codigoPlaca,
        String descripcion,
        int idTipoMaquinaria,
        String modelo,
        String serieMaquina,
        String serieActual,
        Double horometroActual,
        boolean horometroConfirmado,
        Integer idProveedor,
        Integer idPropietario,
        String tipoPropiedad,
        String estadoOperativo,
        String tipoCobro,
        double costoHoraProveedor,
        double costoFijoProveedor,
        double precioHoraCliente,
        double precioFijoCliente,
        String observaciones
) throws Exception {

    JsonObject cuerpo = new JsonObject();

    agregarTexto(cuerpo, "codigoInterno", codigoInterno);
    agregarTexto(cuerpo, "codigoActual", codigoActual);
    agregarTexto(cuerpo, "codigoPlaca", codigoPlaca);

    cuerpo.addProperty("descripcion", descripcion);
    cuerpo.addProperty("idTipoMaquinaria", idTipoMaquinaria);

    agregarTexto(cuerpo, "modelo", modelo);
    agregarTexto(cuerpo, "serieMaquina", serieMaquina);
    agregarTexto(cuerpo, "serieActual", serieActual);

    if (horometroActual == null) {
        cuerpo.add(
                "horometroActual",
                com.google.gson.JsonNull.INSTANCE
        );
    } else {
        cuerpo.addProperty(
                "horometroActual",
                horometroActual
        );
    }

    cuerpo.addProperty(
            "horometroConfirmado",
            horometroConfirmado
    );

    agregarEnteroNullable(
            cuerpo,
            "idProveedor",
            idProveedor
    );

    agregarEnteroNullable(
            cuerpo,
            "idPropietario",
            idPropietario
    );

    cuerpo.addProperty(
            "tipoPropiedad",
            tipoPropiedad
    );

    cuerpo.addProperty(
            "estadoOperativo",
            estadoOperativo
    );

    cuerpo.addProperty(
            "tipoCobro",
            tipoCobro
    );

    cuerpo.addProperty(
            "costoHoraProveedor",
            costoHoraProveedor
    );

    cuerpo.addProperty(
            "costoFijoProveedor",
            costoFijoProveedor
    );

    cuerpo.addProperty(
            "precioHoraCliente",
            precioHoraCliente
    );

    cuerpo.addProperty(
            "precioFijoCliente",
            precioFijoCliente
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
                    "/api/maquinarias/" + idMaquinaria,
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
public static void desactivarMaquinaria(
        int idMaquinaria
) throws Exception {

    ConexionAPI.delete(
            "/api/maquinarias/" + idMaquinaria
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
    private static void agregarTexto(
        JsonObject objeto,
        String propiedad,
        String valor
) {

    if (
            valor == null
            || valor.trim().isEmpty()
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
}