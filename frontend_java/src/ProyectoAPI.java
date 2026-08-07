import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectoAPI {

    private static final Gson GSON =
            new Gson();

    private ProyectoAPI() {
    }

    /*
     * ============================================================
     * PROYECTOS
     * ============================================================
     */

     public static List<ProyectoResumen>
        obtenerResumen() throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/proyectos/resumen"
            );

    JsonObject respuesta =
            convertirRespuesta(
                    respuestaJson
            );

    validarRespuesta(respuesta);

    List<ProyectoResumen> proyectos =
            new ArrayList<>();

    JsonArray datos =
            obtenerArregloDatos(
                    respuesta
            );

    if (datos == null) {
        return proyectos;
    }

    for (JsonElement elemento : datos) {

        JsonObject item =
                elemento.getAsJsonObject();

        proyectos.add(
                new ProyectoResumen(
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
                                "empresa"
                        ),
                        obtenerTexto(
                                item,
                                "descripcion"
                        ),
                        obtenerTexto(
                                item,
                                "sector"
                        ),
                        obtenerTexto(
                                item,
                                "piscina"
                        ),
                        obtenerFecha(
                                item,
                                "fechaInicio"
                        ),
                        obtenerFecha(
                                item,
                                "fechaFinEstimada"
                        ),
                        obtenerTexto(
                                item,
                                "estado"
                        )
                )
        );
    }

    return proyectos;
}

    public static ProyectoDetalle
        obtenerPorId(
                int idProyecto
        ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/proyectos/"
                                + idProyecto
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(respuesta);

        JsonObject item =
                obtenerObjetoDatos(
                        respuesta
                );

        if (item == null) {

            throw new Exception(
                    "No se encontró el proyecto."
            );
        }

        return new ProyectoDetalle(
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
                obtenerEntero(
                        item,
                        "idEmpresa"
                ),
                obtenerEnteroNullable(
                        item,
                        "idSector"
                ),
                obtenerEnteroNullable(
                        item,
                        "idPiscina"
                ),
                obtenerTexto(
                        item,
                        "ordenCompra"
                ),
                obtenerEnteroNullable(
                        item,
                        "idTipoActividad"
                ),
                obtenerFecha(
                        item,
                        "fechaInicio"
                ),
                obtenerFecha(
                        item,
                        "fechaFinEstimada"
                ),
                obtenerFecha(
                        item,
                        "fechaFinReal"
                ),
                obtenerEnteroNullable(
                        item,
                        "diasEstimados"
                ),
                obtenerDoubleNullable(
                        item,
                        "areaM2"
                ),
                obtenerDoubleNullable(
                        item,
                        "espesor"
                ),
                obtenerDoubleNullable(
                        item,
                        "factorCompactacion"
                ),
                obtenerDoubleNullable(
                        item,
                        "cantidadContratada"
                ),
                obtenerDoubleNullable(
                        item,
                        "metrosLinealesContratados"
                ),
                obtenerDoubleNullable(
                        item,
                        "precioUnitario"
                ),
                obtenerTexto(
                        item,
                        "estado"
                ),
                obtenerTexto(
                        item,
                        "observaciones"
                )
        );
    }

    public static int crearProyecto(
            String codigoProyecto,
            String descripcion,
            int idEmpresa,
            Integer idSector,
            Integer idPiscina,
            String ordenCompra,
            Integer idTipoActividad,
            LocalDate fechaInicio,
            LocalDate fechaFinEstimada,
            LocalDate fechaFinReal,
            Integer diasEstimados,
            Double areaM2,
            Double espesor,
            Double factorCompactacion,
            Double cantidadContratada,
            Double metrosLinealesContratados,
            Double precioUnitario,
            String estado,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpoProyecto(
                        codigoProyecto,
                        descripcion,
                        idEmpresa,
                        idSector,
                        idPiscina,
                        ordenCompra,
                        idTipoActividad,
                        fechaInicio,
                        fechaFinEstimada,
                        fechaFinReal,
                        diasEstimados,
                        areaM2,
                        espesor,
                        factorCompactacion,
                        cantidadContratada,
                        metrosLinealesContratados,
                        precioUnitario,
                        estado,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/proyectos",
                        GSON.toJson(cuerpo)
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(respuesta);

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (
                datos == null
                || !datos.has("idProyecto")
                || datos.get("idProyecto").isJsonNull()
        ) {

            throw new Exception(
                    "El proyecto fue guardado, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get("idProyecto")
                .getAsInt();
    }

    public static void actualizarProyecto(
            int idProyecto,
            String codigoProyecto,
            String descripcion,
            int idEmpresa,
            Integer idSector,
            Integer idPiscina,
            String ordenCompra,
            Integer idTipoActividad,
            LocalDate fechaInicio,
            LocalDate fechaFinEstimada,
            LocalDate fechaFinReal,
            Integer diasEstimados,
            Double areaM2,
            Double espesor,
            Double factorCompactacion,
            Double cantidadContratada,
            Double metrosLinealesContratados,
            Double precioUnitario,
            String estado,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                construirCuerpoProyecto(
                        codigoProyecto,
                        descripcion,
                        idEmpresa,
                        idSector,
                        idPiscina,
                        ordenCompra,
                        idTipoActividad,
                        fechaInicio,
                        fechaFinEstimada,
                        fechaFinReal,
                        diasEstimados,
                        areaM2,
                        espesor,
                        factorCompactacion,
                        cantidadContratada,
                        metrosLinealesContratados,
                        precioUnitario,
                        estado,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/proyectos/"
                                + idProyecto,
                        GSON.toJson(cuerpo)
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(respuesta);
    }

    public static void eliminarProyecto(
            int idProyecto
    ) throws Exception {

        ConexionAPI.delete(
                "/api/proyectos/"
                        + idProyecto
        );
    }

    /*
     * ============================================================
     * CATÁLOGOS UTILIZADOS POR FORM PROYECTO
     * ============================================================
     */

    public static List<EmpresaItem>
        obtenerEmpresas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/proyectos/empresas"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(respuesta);

        List<EmpresaItem> empresas =
        new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return empresas;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            empresas.add(
                    new EmpresaItem(
                            obtenerEntero(
                                    item,
                                    "idEmpresa"
                            ),
                            obtenerTexto(
                                    item,
                                    "nombre"
                            )
                    )
            );
        }

        return empresas;
    }

    public static List<SectorItem>
        obtenerSectores() throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/v1/proyectos/sectores"
            );

    JsonObject respuesta =
            convertirRespuesta(
                    respuestaJson
            );

    validarRespuesta(respuesta);

    List<SectorItem> sectores =
            new ArrayList<>();

    JsonArray datos =
            obtenerArregloDatos(
                    respuesta
            );

    if (datos == null) {
        return sectores;
    }

    for (JsonElement elemento : datos) {

        JsonObject item =
                elemento.getAsJsonObject();

        sectores.add(
                new SectorItem(
                        obtenerEntero(
                                item,
                                "idSector"
                        ),
                        obtenerTexto(
                                item,
                                "nombreSector"
                        )
                )
        );
    }

    return sectores;
}

    public static List<PiscinaItem>
        obtenerPiscinasPorSector(
                Integer idSector
        ) throws Exception {

    String ruta =
            "/api/v1/proyectos/piscinas";

    if (idSector != null) {

        ruta +=
                "?idSector="
                        + idSector;
    }

    String respuestaJson =
            ConexionAPI.get(
                    ruta
            );

    JsonObject respuesta =
            convertirRespuesta(
                    respuestaJson
            );

    validarRespuesta(respuesta);

    List<PiscinaItem> piscinas =
            new ArrayList<>();

    JsonArray datos =
            obtenerArregloDatos(
                    respuesta
            );

    if (datos == null) {
        return piscinas;
    }

    for (JsonElement elemento : datos) {

        JsonObject item =
                elemento.getAsJsonObject();

        piscinas.add(
                new PiscinaItem(
                        obtenerEntero(
                                item,
                                "idPiscina"
                        ),
                        obtenerEntero(
                                item,
                                "idSector"
                        ),
                        obtenerTexto(
                                item,
                                "nombrePiscina"
                        )
                )
        );
    }

    return piscinas;
}

    public static List<TipoActividadItem>
        obtenerTiposActividad() throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/v1/proyectos/tipos-actividad"
            );

    JsonObject respuesta =
            convertirRespuesta(
                    respuestaJson
            );

    validarRespuesta(respuesta);

    List<TipoActividadItem> actividades =
            new ArrayList<>();

    JsonArray datos =
            obtenerArregloDatos(
                    respuesta
            );

    if (datos == null) {
        return actividades;
    }

    for (JsonElement elemento : datos) {

        JsonObject item =
                elemento.getAsJsonObject();

        actividades.add(
                new TipoActividadItem(
                        obtenerEntero(
                                item,
                                "idTipoActividad"
                        ),
                        obtenerTexto(
                                item,
                                "nombreActividad"
                        )
                )
        );
    }

    return actividades;
}
    /*
     * ============================================================
     * MÉTODOS AUXILIARES
     * ============================================================
     */

    private static JsonObject construirCuerpoProyecto(
            String codigoProyecto,
            String descripcion,
            int idEmpresa,
            Integer idSector,
            Integer idPiscina,
            String ordenCompra,
            Integer idTipoActividad,
            LocalDate fechaInicio,
            LocalDate fechaFinEstimada,
            LocalDate fechaFinReal,
            Integer diasEstimados,
            Double areaM2,
            Double espesor,
            Double factorCompactacion,
            Double cantidadContratada,
            Double metrosLinealesContratados,
            Double precioUnitario,
            String estado,
            String observaciones
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "codigoProyecto",
                codigoProyecto
        );

        agregarTexto(
                cuerpo,
                "descripcion",
                descripcion
        );

        cuerpo.addProperty(
                "idEmpresa",
                idEmpresa
        );

        agregarEnteroNullable(
                cuerpo,
                "idSector",
                idSector
        );

        agregarEnteroNullable(
                cuerpo,
                "idPiscina",
                idPiscina
        );

        agregarTexto(
                cuerpo,
                "ordenCompra",
                ordenCompra
        );

        agregarEnteroNullable(
                cuerpo,
                "idTipoActividad",
                idTipoActividad
        );

        agregarFecha(
                cuerpo,
                "fechaInicio",
                fechaInicio
        );

        agregarFecha(
                cuerpo,
                "fechaFinEstimada",
                fechaFinEstimada
        );

        agregarFecha(
                cuerpo,
                "fechaFinReal",
                fechaFinReal
        );

        agregarEnteroNullable(
                cuerpo,
                "diasEstimados",
                diasEstimados
        );

        agregarDoubleNullable(
                cuerpo,
                "areaM2",
                areaM2
        );

        agregarDoubleNullable(
                cuerpo,
                "espesor",
                espesor
        );

        agregarDoubleNullable(
                cuerpo,
                "factorCompactacion",
                factorCompactacion
        );

        agregarDoubleNullable(
                cuerpo,
                "cantidadContratada",
                cantidadContratada
        );

        agregarDoubleNullable(
                cuerpo,
                "metrosLinealesContratados",
                metrosLinealesContratados
        );

        agregarDoubleNullable(
                cuerpo,
                "precioUnitario",
                precioUnitario
        );

        cuerpo.addProperty(
                "estado",
                estado == null
                        || estado.isBlank()
                        ? "PLANIFICADO"
                        : estado
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

        return LocalDate.parse(
                valor
        );
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
