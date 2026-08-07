import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class CodigoHistoricoAPI {

    private static final Gson GSON =
            new Gson();

    private CodigoHistoricoAPI() {
    }

    public record ItemCatalogo(
            int id,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record MaquinariaItem(
            int idMaquinaria,
            String codigo,
            String descripcion
    ) {

        @Override
        public String toString() {

            String codigoMostrar =
                    codigo == null
                    || codigo.isBlank()
                            ? "SIN CÓDIGO"
                            : codigo;

            return codigoMostrar
                    + " - "
                    + descripcion;
        }
    }

    public record CodigoHistoricoResumen(
            int idCodigoHistorico,
            String proveedor,
            String codigoAnterior,
            String codigoActual,
            String descripcion,
            double costoHora,
            String estadoVinculacion,
            String observaciones
    ) {
    }

    public record CodigoHistoricoDetalle(
            int idCodigoHistorico,
            Integer idMaquinaria,
            String maquinariaActual,
            Integer idProveedorOriginal,
            String proveedorOriginal,
            String codigoAnterior,
            String codigoActualOrigen,
            String descripcionOriginal,
            double costoHoraOriginal,
            String estadoVinculacion,
            String observaciones,
            String fechaRegistro
    ) {
    }

    public static List<ItemCatalogo>
            obtenerProveedores() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/codigos-historicos/proveedores"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        List<ItemCatalogo> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new ItemCatalogo(
                            obtenerEntero(
                                    item,
                                    "id"
                            ),
                            obtenerTexto(
                                    item,
                                    "nombre"
                            )
                    )
            );
        }

        return lista;
    }

    public static List<MaquinariaItem>
            obtenerMaquinariasActivas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/codigos-historicos/maquinarias"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        List<MaquinariaItem> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new MaquinariaItem(
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
                            )
                    )
            );
        }

        return lista;
    }

    public static List<CodigoHistoricoResumen>
            obtenerActivos() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/codigos-historicos"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        List<CodigoHistoricoResumen> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new CodigoHistoricoResumen(
                            obtenerEntero(
                                    item,
                                    "idCodigoHistorico"
                            ),
                            obtenerTexto(
                                    item,
                                    "proveedor"
                            ),
                            obtenerTexto(
                                    item,
                                    "codigoAnterior"
                            ),
                            obtenerTexto(
                                    item,
                                    "codigoActual"
                            ),
                            obtenerTexto(
                                    item,
                                    "descripcion"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoHora"
                            ),
                            obtenerTexto(
                                    item,
                                    "estadoVinculacion"
                            ),
                            obtenerTexto(
                                    item,
                                    "observaciones"
                            )
                    )
            );
        }

        return lista;
    }

    public static CodigoHistoricoDetalle obtenerPorId(
            int idCodigoHistorico
    ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/codigos-historicos/"
                                + idCodigoHistorico
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
                    "No se encontró el código histórico."
            );
        }

        return new CodigoHistoricoDetalle(
                obtenerEntero(
                        datos,
                        "idCodigoHistorico"
                ),
                obtenerEnteroNullable(
                        datos,
                        "idMaquinaria"
                ),
                obtenerTexto(
                        datos,
                        "maquinariaActual"
                ),
                obtenerEnteroNullable(
                        datos,
                        "idProveedorOriginal"
                ),
                obtenerTexto(
                        datos,
                        "proveedorOriginal"
                ),
                obtenerTexto(
                        datos,
                        "codigoAnterior"
                ),
                obtenerTexto(
                        datos,
                        "codigoActualOrigen"
                ),
                obtenerTexto(
                        datos,
                        "descripcionOriginal"
                ),
                obtenerDouble(
                        datos,
                        "costoHoraOriginal"
                ),
                obtenerTexto(
                        datos,
                        "estadoVinculacion"
                ),
                obtenerTexto(
                        datos,
                        "observaciones"
                ),
                obtenerTexto(
                        datos,
                        "fechaRegistro"
                )
        );
    }

    public static int insertar(
            Integer idMaquinaria,
            String codigoAnterior,
            Integer idProveedorOriginal,
            String codigoActualOrigen,
            String descripcionOriginal,
            double costoHoraOriginal,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                crearCuerpo(
                        idMaquinaria,
                        codigoAnterior,
                        idProveedorOriginal,
                        codigoActualOrigen,
                        descripcionOriginal,
                        costoHoraOriginal,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/codigos-historicos",
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
                        "idCodigoHistorico"
                )
                || datos
                        .get(
                                "idCodigoHistorico"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "El código fue guardado, pero no se pudo "
                            + "obtener su identificador."
            );
        }

        return datos
                .get(
                        "idCodigoHistorico"
                )
                .getAsInt();
    }

    public static void actualizar(
            int idCodigoHistorico,
            Integer idMaquinaria,
            String codigoAnterior,
            Integer idProveedorOriginal,
            String codigoActualOrigen,
            String descripcionOriginal,
            double costoHoraOriginal,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                crearCuerpo(
                        idMaquinaria,
                        codigoAnterior,
                        idProveedorOriginal,
                        codigoActualOrigen,
                        descripcionOriginal,
                        costoHoraOriginal,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/codigos-historicos/"
                                + idCodigoHistorico,
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
            int idCodigoHistorico
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/codigos-historicos/"
                        + idCodigoHistorico
        );
    }

    private static JsonObject crearCuerpo(
            Integer idMaquinaria,
            String codigoAnterior,
            Integer idProveedorOriginal,
            String codigoActualOrigen,
            String descripcionOriginal,
            double costoHoraOriginal,
            String observaciones
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarEntero(
                cuerpo,
                "idMaquinaria",
                idMaquinaria
        );

        agregarTexto(
                cuerpo,
                "codigoAnterior",
                codigoAnterior
        );

        agregarEntero(
                cuerpo,
                "idProveedorOriginal",
                idProveedorOriginal
        );

        agregarTexto(
                cuerpo,
                "codigoActualOrigen",
                codigoActualOrigen
        );

        agregarTexto(
                cuerpo,
                "descripcionOriginal",
                descripcionOriginal
        );

        cuerpo.addProperty(
                "costoHoraOriginal",
                costoHoraOriginal
        );

        agregarTexto(
                cuerpo,
                "observaciones",
                observaciones
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
                || !respuesta.has("exito")
                || respuesta.get("exito").isJsonNull()
                || !respuesta
                        .get("exito")
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
                || !respuesta.has("datos")
                || respuesta.get("datos").isJsonNull()
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
                || !respuesta.has("datos")
                || respuesta.get("datos").isJsonNull()
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
                && respuesta.has("mensaje")
                && !respuesta.get("mensaje").isJsonNull()
        ) {

            return respuesta
                    .get("mensaje")
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
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return 0;
        }

        return objeto
                .get(propiedad)
                .getAsInt();
    }

    private static Integer obtenerEnteroNullable(
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
                .getAsInt();
    }

    private static double obtenerDouble(
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
                    JsonNull.INSTANCE
            );

        } else {

            objeto.addProperty(
                    propiedad,
                    valor.trim()
            );
        }
    }

    private static void agregarEntero(
            JsonObject objeto,
            String propiedad,
            Integer valor
    ) {

        if (valor == null) {

            objeto.add(
                    propiedad,
                    JsonNull.INSTANCE
            );

        } else {

            objeto.addProperty(
                    propiedad,
                    valor
            );
        }
    }
}
