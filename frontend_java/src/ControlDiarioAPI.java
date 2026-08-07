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



    public record ProyectoItem(
            int idProyecto,
            String codigoProyecto,
            String descripcion
    ) {
        @Override
        public String toString() {

            String codigo =
                    codigoProyecto == null
                            ? ""
                            : codigoProyecto;

            String detalle =
                    descripcion == null
                            ? ""
                            : descripcion;

            return codigo
                    + " - "
                    + detalle;
        }
    }

    public record GuiaAprobadaItem(
            int idGuia,
            LocalDate fecha,
            String empresa,
            String tipoGuia,
            String numeroGuia,
            String proyectoReferencia,
            String sector,
            String material,
            String choferOperador,
            String placa,
            double m3
    ) {
    }

    public record ControlDiarioResumen(
            int idControl,
            String proyecto,
            LocalDate fecha,
            double metrosLineales
    ) {
    }

    public record ControlDiarioDetalle(
            int idControl,
            int idProyecto,
            Integer idGuia,
            LocalDate fecha,
            double metrosLineales,
            Double ancho,
            Double espesor,
            Double volumenReal,
            String observaciones
    ) {
    }

    public record MaquinariaAsignadaItem(
            int idMaquinaria,
            String codigo,
            String descripcion,
            String tipoCobro,
            double costoHoraProveedor,
            double costoFijoProveedor
    ) {
        @Override
        public String toString() {

            String codigoMostrar =
                    codigo == null || codigo.isBlank()
                            ? "SIN CÓDIGO"
                            : codigo;

            String descripcionMostrar =
                    descripcion == null
                            ? ""
                            : descripcion;

            return codigoMostrar
                    + " - "
                    + descripcionMostrar;
        }
    }

    public record ControlMaquinariaDetalle(
            int idControlMaquinaria,
            int idControl,
            int idMaquinaria,
            String codigo,
            String descripcion,
            String tipoCobro,
            double horasTrabajadas,
            double costoHoraProveedor,
            double costoFijoProveedor,
            double costoCalculado,
            String observaciones,
            boolean activo
    ) {
    }

    public record ControlMaterialDetalle(
            int idControlMaterial,
            int idControl,
            Integer idTarifa,
            String materialRecibido,
            String cantera,
            String destinoSector,
            double cantidadViajes,
            double volumenRecibido,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            double costoMaterial,
            double costoTransporte,
            double costoTotal,
            int cantidadVolquetas,
            double horasVolqueta,
            String observaciones
    ) {
    }


    public static List<ControlDiarioResumen>
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

        List<ControlDiarioResumen> controles =
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
                    new ControlDiarioResumen(
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
    public static ControlDiarioDetalle
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

    return new ControlDiarioDetalle(

            obtenerEntero(
                    item,
                    "idControl"
            ),

            obtenerEntero(
                    item,
                    "idProyecto"
            ),

            obtenerEnteroNullable(
                    item,
                    "idGuia"
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

    public static List<ProyectoItem>
    obtenerProyectosActivos() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario/proyectos-activos"
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        List<ProyectoItem> proyectos =
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

        return proyectos;
    }

    public static List<GuiaAprobadaItem>
    obtenerGuiasAprobadas() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario/guias-aprobadas"
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        List<GuiaAprobadaItem> guias =
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
                    new GuiaAprobadaItem(
                            obtenerEntero(
                                    item,
                                    "idGuia"
                            ),
                            obtenerFecha(
                                    item,
                                    "fecha"
                            ),
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
                            obtenerTexto(
                                    item,
                                    "proyectoReferencia"
                            ),
                            obtenerTexto(
                                    item,
                                    "sector"
                            ),
                            obtenerTexto(
                                    item,
                                    "material"
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
                            )
                    )
            );
        }

        return guias;
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

    /*
     * Sobrecarga temporal para conservar compatibilidad con el DAO antiguo.
     * La Etapa Final B ya no utiliza esta firma.
     */
    public static void actualizarControlDiario(
            int idControl,
            int idProyecto,
            LocalDate fechaControl,
            double metrosLineales,
            Double ancho,
            Double espesor,
            String observaciones
    ) throws Exception {

        actualizarControlDiario(
                idControl,
                null,
                idProyecto,
                fechaControl,
                metrosLineales,
                ancho,
                espesor,
                observaciones
        );
    }

    public static void eliminarControlDiario(
        int idControl
) throws Exception {

    ConexionAPI.delete(
            "/api/v1/control-diario/" + idControl
    );
}



    public static List<MaquinariaAsignadaItem>
    obtenerMaquinariasAsignadas(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {
            throw new IllegalArgumentException(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario-maquinaria/asignadas/"
                                + idControl
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        List<MaquinariaAsignadaItem> lista =
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
                    new MaquinariaAsignadaItem(
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
                                    "tipoCobro"
                            ),
                            obtenerDecimal(
                                    item,
                                    "costoHoraProveedor"
                            ),
                            obtenerDecimal(
                                    item,
                                    "costoFijoProveedor"
                            )
                    )
            );
        }

        return lista;
    }

    public static List<ControlMaquinariaDetalle>
    obtenerMaquinariaPorControl(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {
            throw new IllegalArgumentException(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario-maquinaria/control/"
                                + idControl
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        List<ControlMaquinariaDetalle> lista =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            lista.add(
                    convertirMaquinariaDetalle(
                            elemento.getAsJsonObject()
                    )
            );
        }

        return lista;
    }

    public static ControlMaquinariaDetalle
    obtenerMaquinariaPorId(
            int idControlMaquinaria
    ) throws Exception {

        if (idControlMaquinaria <= 0) {
            throw new IllegalArgumentException(
                    "El registro de maquinaria seleccionado no es válido."
            );
        }

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario-maquinaria/"
                                + idControlMaquinaria
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        JsonObject datos =
                respuesta.getAsJsonObject("datos");

        if (datos == null) {
            throw new Exception(
                    "La API no devolvió el detalle de maquinaria."
            );
        }

        return convertirMaquinariaDetalle(datos);
    }

    public static ControlMaquinariaDetalle
    guardarMaquinaria(
            int idControl,
            Integer idControlMaquinaria,
            int idMaquinaria,
            double horasTrabajadas,
            String observaciones
    ) throws Exception {

        JsonObject cuerpo =
                new JsonObject();

        cuerpo.addProperty(
                "idControl",
                idControl
        );

        cuerpo.addProperty(
                "idMaquinaria",
                idMaquinaria
        );

        cuerpo.addProperty(
                "horasTrabajadas",
                horasTrabajadas
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

        String respuestaJson;

        if (
                idControlMaquinaria == null
                || idControlMaquinaria <= 0
        ) {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/control-diario-maquinaria",
                            GSON.toJson(cuerpo)
                    );

        } else {

            respuestaJson =
                    ConexionAPI.put(
                            "/api/v1/control-diario-maquinaria/"
                                    + idControlMaquinaria,
                            GSON.toJson(cuerpo)
                    );
        }

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        JsonObject datos =
                respuesta.getAsJsonObject("datos");

        if (datos == null) {
            throw new Exception(
                    "La API guardó la maquinaria, "
                            + "pero no devolvió sus datos."
            );
        }

        return convertirMaquinariaDetalle(datos);
    }

    public static void eliminarMaquinaria(
            int idControlMaquinaria
    ) throws Exception {

        if (idControlMaquinaria <= 0) {
            throw new IllegalArgumentException(
                    "El registro de maquinaria seleccionado no es válido."
            );
        }

        ConexionAPI.delete(
                "/api/v1/control-diario-maquinaria/"
                        + idControlMaquinaria
        );
    }

    private static ControlMaquinariaDetalle
    convertirMaquinariaDetalle(
            JsonObject item
    ) {

        return new ControlMaquinariaDetalle(
                obtenerEntero(
                        item,
                        "idControlMaquinaria"
                ),
                obtenerEntero(
                        item,
                        "idControl"
                ),
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
                        "tipoCobro"
                ),
                obtenerDecimal(
                        item,
                        "horasTrabajadas"
                ),
                obtenerDecimal(
                        item,
                        "costoHoraProveedor"
                ),
                obtenerDecimal(
                        item,
                        "costoFijoProveedor"
                ),
                obtenerDecimal(
                        item,
                        "costoCalculado"
                ),
                obtenerTexto(
                        item,
                        "observaciones"
                ),
                item != null
                        && item.has("activo")
                        && !item.get("activo").isJsonNull()
                        && item.get("activo").getAsBoolean()
        );
    }

    public static List<ControlMaterialDetalle> obtenerMateriales(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {
            throw new IllegalArgumentException(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario/"
                                + idControl
                                + "/materiales"
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        List<ControlMaterialDetalle> lista =
                new ArrayList<>();

        JsonArray datos =
                respuesta.getAsJsonArray("datos");

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            lista.add(
                    convertirMaterial(
                            elemento.getAsJsonObject()
                    )
            );
        }

        return lista;
    }

    public static ControlMaterialDetalle obtenerMaterialPorId(
            int idControlMaterial
    ) throws Exception {

        if (idControlMaterial <= 0) {
            throw new IllegalArgumentException(
                    "El registro de material seleccionado no es válido."
            );
        }

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/control-diario/materiales/"
                                + idControlMaterial
                );

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        JsonObject datos =
                respuesta.getAsJsonObject("datos");

        if (datos == null) {
            throw new Exception(
                    "La API no devolvió el detalle del material pétreo."
            );
        }

        return convertirMaterial(datos);
    }

    public static ControlMaterialDetalle guardarMaterial(
            int idControl,
            Integer idControlMaterial,
            Integer idTarifa,
            String materialRecibido,
            String cantera,
            String destinoSector,
            double cantidadViajes,
            double volumenRecibido,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            int cantidadVolquetas,
            double horasVolqueta,
            String observaciones
    ) throws Exception {

        if (idControl <= 0) {
            throw new IllegalArgumentException(
                    "El Control Diario seleccionado no es válido."
            );
        }

        JsonObject cuerpo =
                new JsonObject();

        agregarEnteroNullable(
                cuerpo,
                "idTarifa",
                idTarifa
        );

        agregarTexto(
                cuerpo,
                "materialRecibido",
                materialRecibido
        );

        agregarTexto(
                cuerpo,
                "cantera",
                cantera
        );

        agregarTexto(
                cuerpo,
                "destinoSector",
                destinoSector
        );

        cuerpo.addProperty(
                "cantidadViajes",
                cantidadViajes
        );

        cuerpo.addProperty(
                "volumenRecibido",
                volumenRecibido
        );

        cuerpo.addProperty(
                "costoUnitarioMaterial",
                costoUnitarioMaterial
        );

        cuerpo.addProperty(
                "costoUnitarioTransporte",
                costoUnitarioTransporte
        );

        cuerpo.addProperty(
                "cantidadVolquetas",
                cantidadVolquetas
        );

        cuerpo.addProperty(
                "horasVolqueta",
                horasVolqueta
        );

        agregarTexto(
                cuerpo,
                "observaciones",
                observaciones
        );

        String respuestaJson;

        if (
                idControlMaterial == null
                || idControlMaterial <= 0
        ) {

            respuestaJson =
                    ConexionAPI.post(
                            "/api/v1/control-diario/"
                                    + idControl
                                    + "/materiales",
                            GSON.toJson(cuerpo)
                    );

        } else {

            respuestaJson =
                    ConexionAPI.put(
                            "/api/v1/control-diario/"
                                    + idControl
                                    + "/materiales/"
                                    + idControlMaterial,
                            GSON.toJson(cuerpo)
                    );
        }

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        validarRespuestaExitosa(respuesta);

        JsonObject datos =
                respuesta.getAsJsonObject("datos");

        if (datos == null) {
            throw new Exception(
                    "La API guardó el material, "
                            + "pero no devolvió sus datos."
            );
        }

        return convertirMaterial(datos);
    }

    public static void eliminarMaterial(
            int idControlMaterial
    ) throws Exception {

        if (idControlMaterial <= 0) {
            throw new IllegalArgumentException(
                    "El registro de material seleccionado no es válido."
            );
        }

        ConexionAPI.delete(
                "/api/v1/control-diario/materiales/"
                        + idControlMaterial
        );
    }

    private static ControlMaterialDetalle convertirMaterial(
            JsonObject item
    ) {

        double costoMaterial =
                obtenerDecimal(
                        item,
                        "costoMaterial"
                );

        double costoTransporte =
                obtenerDecimal(
                        item,
                        "costoTransporte"
                );

        double costoTotal =
                item != null
                && item.has("costoTotal")
                && !item.get("costoTotal").isJsonNull()
                        ? item.get("costoTotal").getAsDouble()
                        : costoMaterial + costoTransporte;

        return new ControlMaterialDetalle(
                obtenerEntero(
                        item,
                        "idControlMaterial"
                ),
                obtenerEntero(
                        item,
                        "idControl"
                ),
                obtenerEnteroNullable(
                        item,
                        "idTarifa"
                ),
                obtenerTexto(
                        item,
                        "materialRecibido"
                ),
                obtenerTexto(
                        item,
                        "cantera"
                ),
                obtenerTexto(
                        item,
                        "destinoSector"
                ),
                obtenerDecimal(
                        item,
                        "cantidadViajes"
                ),
                obtenerDecimal(
                        item,
                        "volumenRecibido"
                ),
                obtenerDecimal(
                        item,
                        "costoUnitarioMaterial"
                ),
                obtenerDecimal(
                        item,
                        "costoUnitarioTransporte"
                ),
                costoMaterial,
                costoTransporte,
                costoTotal,
                obtenerEntero(
                        item,
                        "cantidadVolquetas"
                ),
                obtenerDecimal(
                        item,
                        "horasVolqueta"
                ),
                obtenerTexto(
                        item,
                        "observaciones"
                )
        );
    }

    private static void validarRespuestaExitosa(
            JsonObject respuesta
    ) throws Exception {

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
