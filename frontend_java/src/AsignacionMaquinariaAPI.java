import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AsignacionMaquinariaAPI {

    private static final Gson GSON =
            new Gson();

    private AsignacionMaquinariaAPI() {
    }

    public record ProyectoItem(
            int idProyecto,
            String codigoProyecto,
            String descripcion
    ) {

        @Override
        public String toString() {

            return codigoProyecto
                    + " - "
                    + descripcion;
        }
    }

    public record MaquinariaItem(
            int idMaquinaria,
            String codigo,
            String descripcion,
            String propietario,
            Double tarifaReferencia
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

    public record AsignacionResumen(
            int idAsignacion,
            String proyecto,
            String maquinaria,
            String propietario,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHora,
            String estado
    ) {
    }

    public record AsignacionDetalle(
            int idAsignacion,
            int idProyecto,
            int idMaquinaria,
            String codigoHistorico,
            String descripcionHistorica,
            String proveedorHistorico,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHora,
            String estado,
            String observaciones
    ) {
    }

    /*
     * ============================================================
     * PROYECTOS ACTIVOS
     * ============================================================
     */
    public static List<ProyectoItem>
            obtenerProyectosActivos() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/asignaciones-maquinaria/proyectos"
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

        List<ProyectoItem> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new ProyectoItem(
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
                            )
                    )
            );
        }

        return lista;
    }

    /*
     * ============================================================
     * MAQUINARIAS DISPONIBLES
     * ============================================================
     */
    public static List<MaquinariaItem>
            obtenerMaquinariasDisponibles() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/asignaciones-maquinaria/maquinarias"
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
                            ),
                            obtenerTexto(
                                    item,
                                    "propietario"
                            ),
                            obtenerDoubleNullable(
                                    item,
                                    "tarifaReferencia"
                            )
                    )
            );
        }

        return lista;
    }

    /*
     * ============================================================
     * ASIGNACIONES ACTIVAS
     * ============================================================
     */
    public static List<AsignacionResumen>
            obtenerActivas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/asignaciones-maquinaria"
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

        List<AsignacionResumen> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new AsignacionResumen(
                            obtenerEntero(
                                    item,
                                    "idAsignacion"
                            ),
                            obtenerTexto(
                                    item,
                                    "proyecto"
                            ),
                            obtenerTexto(
                                    item,
                                    "maquinaria"
                            ),
                            obtenerTexto(
                                    item,
                                    "propietario"
                            ),
                            obtenerEntero(
                                    item,
                                    "cantidad"
                            ),
                            obtenerFecha(
                                    item,
                                    "fechaIngreso"
                            ),
                            obtenerFecha(
                                    item,
                                    "fechaSalida"
                            ),
                            obtenerDoubleNullable(
                                    item,
                                    "tarifaHora"
                            ),
                            obtenerTexto(
                                    item,
                                    "estado"
                            )
                    )
            );
        }

        return lista;
    }

    /*
     * ============================================================
     * DETALLE
     * ============================================================
     */
    public static AsignacionDetalle obtenerPorId(
            int idAsignacion
    ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/asignaciones-maquinaria/"
                                + idAsignacion
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
                    "No se encontró la asignación."
            );
        }

        return new AsignacionDetalle(
                obtenerEntero(
                        datos,
                        "idAsignacion"
                ),
                obtenerEntero(
                        datos,
                        "idProyecto"
                ),
                obtenerEntero(
                        datos,
                        "idMaquinaria"
                ),
                obtenerTexto(
                        datos,
                        "codigoHistorico"
                ),
                obtenerTexto(
                        datos,
                        "descripcionHistorica"
                ),
                obtenerTexto(
                        datos,
                        "proveedorHistorico"
                ),
                obtenerEntero(
                        datos,
                        "cantidad"
                ),
                obtenerFecha(
                        datos,
                        "fechaIngreso"
                ),
                obtenerFecha(
                        datos,
                        "fechaSalida"
                ),
                obtenerDoubleNullable(
                        datos,
                        "tarifaHora"
                ),
                obtenerTexto(
                        datos,
                        "estado"
                ),
                obtenerTexto(
                        datos,
                        "observaciones"
                )
        );
    }

    /*
     * ============================================================
     * INSERTAR
     * ============================================================
     */
    public static int insertar(
            int idProyecto,
            int idMaquinaria,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHoraAsignada,
            String estado,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                crearCuerpoInsercion(
                        idProyecto,
                        idMaquinaria,
                        cantidad,
                        fechaIngreso,
                        fechaSalida,
                        tarifaHoraAsignada,
                        estado,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/asignaciones-maquinaria",
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
                        "idAsignacion"
                )
                || datos
                        .get(
                                "idAsignacion"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "La asignación fue guardada, "
                            + "pero no se pudo obtener su ID."
            );
        }

        return datos
                .get(
                        "idAsignacion"
                )
                .getAsInt();
    }

    /*
     * ============================================================
     * ACTUALIZAR
     * ============================================================
     */
    public static void actualizar(
            int idAsignacion,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHoraAsignada,
            String estado,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                crearCuerpoEdicion(
                        cantidad,
                        fechaIngreso,
                        fechaSalida,
                        tarifaHoraAsignada,
                        estado,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/asignaciones-maquinaria/"
                                + idAsignacion,
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

    /*
     * ============================================================
     * ELIMINAR
     * ============================================================
     */
    public static void eliminar(
            int idAsignacion
    ) throws Exception {

        ConexionAPI.delete(
                "/api/v1/asignaciones-maquinaria/"
                        + idAsignacion
        );
    }

    /*
     * ============================================================
     * CONSTRUCCIÓN JSON - INSERTAR
     * ============================================================
     */
    private static JsonObject crearCuerpoInsercion(
            int idProyecto,
            int idMaquinaria,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHoraAsignada,
            String estado,
            String observaciones
    ) {

        JsonObject cuerpo =
                new JsonObject();

        cuerpo.addProperty(
                "idProyecto",
                idProyecto
        );

        cuerpo.addProperty(
                "idMaquinaria",
                idMaquinaria
        );

        cuerpo.addProperty(
                "cantidad",
                cantidad
        );

        agregarFecha(
                cuerpo,
                "fechaIngreso",
                fechaIngreso
        );

        agregarFecha(
                cuerpo,
                "fechaSalida",
                fechaSalida
        );

        agregarDouble(
                cuerpo,
                "tarifaHora",
                tarifaHoraAsignada
        );

        agregarTexto(
                cuerpo,
                "estado",
                estado
        );

        agregarTexto(
                cuerpo,
                "observaciones",
                observaciones
        );

        return cuerpo;
    }

    /*
     * ============================================================
     * CONSTRUCCIÓN JSON - EDICIÓN
     *
     * Proyecto y maquinaria NO se envían porque el Service
     * mantiene los originales.
     * ============================================================
     */
    private static JsonObject crearCuerpoEdicion(
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double tarifaHoraAsignada,
            String estado,
            String observaciones
    ) {

        JsonObject cuerpo =
                new JsonObject();

        cuerpo.addProperty(
                "cantidad",
                cantidad
        );

        agregarFecha(
                cuerpo,
                "fechaIngreso",
                fechaIngreso
        );

        agregarFecha(
                cuerpo,
                "fechaSalida",
                fechaSalida
        );

        agregarDouble(
                cuerpo,
                "tarifaHora",
                tarifaHoraAsignada
        );

        agregarTexto(
                cuerpo,
                "estado",
                estado
        );

        agregarTexto(
                cuerpo,
                "observaciones",
                observaciones
        );

        return cuerpo;
    }

    /*
     * ============================================================
     * RESPUESTAS
     * ============================================================
     */
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

    /*
     * ============================================================
     * LECTURA JSON
     * ============================================================
     */
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

        String valor =
                obtenerTexto(
                        objeto,
                        propiedad
                );

        if (
                valor == null
                || valor.isBlank()
        ) {

            return null;
        }

        return LocalDate.parse(
                valor
        );
    }

    /*
     * ============================================================
     * ESCRITURA JSON
     * ============================================================
     */
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

    private static void agregarFecha(
            JsonObject objeto,
            String propiedad,
            LocalDate valor
    ) {

        if (valor == null) {

            objeto.add(
                    propiedad,
                    JsonNull.INSTANCE
            );

        } else {

            objeto.addProperty(
                    propiedad,
                    valor.toString()
            );
        }
    }

    private static void agregarDouble(
            JsonObject objeto,
            String propiedad,
            Double valor
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
