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

    public static List<ProyectoDAO.ProyectoResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/proyectos/resumen"
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

        List<ProyectoDAO.ProyectoResumen> proyectos =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return proyectos;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            proyectos.add(
                    new ProyectoDAO.ProyectoResumen(
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

    public static ProyectoDAO.ProyectoDetalle
        obtenerPorId(
                int idProyecto
        ) throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/proyectos/"
                            + idProyecto
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
                "No se encontró el proyecto."
        );
    }

    return new ProyectoDAO.ProyectoDetalle(

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
            estado == null || estado.isBlank()
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

    String respuestaJson =
            ConexionAPI.post(
                    "/api/proyectos",
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
            || !datos.has("idProyecto")
            || datos.get("idProyecto").isJsonNull()
    ) {

        throw new Exception(
                "El proyecto fue guardado, pero la API no devolvió su ID."
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
            estado == null || estado.isBlank()
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

    String respuestaJson =
            ConexionAPI.put(
                    "/api/proyectos/" + idProyecto,
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
    public static void eliminarProyecto(
        int idProyecto
) throws Exception {

    ConexionAPI.delete(
            "/api/proyectos/" + idProyecto
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
