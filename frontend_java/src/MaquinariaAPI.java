import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class MaquinariaAPI {

    private static final Gson GSON =
            new Gson();

    private MaquinariaAPI() {
    }

    /*
     * ============================================================
     * RECORDS PROPIOS
     * ============================================================
     */

    public record MaquinariaResumen(
            int idMaquinaria,
            String codigo,
            String descripcion,
            String tipoMaquinaria,
            String proveedor,
            String propietario,
            String estadoOperativo,
            double costoHoraProveedor
    ) {
    }

    public record CatalogoItem(
            int id,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record MaquinariaDetalle(
            int idMaquinaria,
            String codigoInterno,
            String codigoActual,
            String codigoPlaca,
            String descripcion,
            int idTipoMaquinaria,
            String tipoMaquinaria,
            String modelo,
            String serieMaquina,
            String serieActual,
            Double horometroActual,
            boolean horometroConfirmado,
            Integer idProveedor,
            String proveedor,
            Integer idPropietario,
            String propietario,
            String tipoPropiedad,
            String estadoOperativo,
            String tipoCobro,
            double costoHoraProveedor,
            double costoFijoProveedor,
            double precioHoraCliente,
            double precioFijoCliente,
            String observaciones,
            boolean activo
    ) {
    }

    /*
     * ============================================================
     * RESUMEN DE MAQUINARIAS
     * GET /api/maquinarias/resumen
     * ============================================================
     */

    public static List<MaquinariaResumen>
            obtenerResumen() throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/maquinarias/resumen"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<MaquinariaResumen> lista =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new MaquinariaResumen(
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

    /*
     * ============================================================
     * BÚSQUEDA LOCAL SOBRE RESUMEN
     * ============================================================
     */

    public static List<MaquinariaResumen> buscar(
            String estado,
            String tipoMaquinaria,
            String proveedor,
            String codigo
    ) throws Exception {

        List<MaquinariaResumen> lista =
                obtenerResumen();

        List<MaquinariaResumen> filtrados =
                new ArrayList<>();

        for (MaquinariaResumen item : lista) {

            if (
                    estado != null
                    && !estado.isBlank()
                    && !estado.equalsIgnoreCase(
                            "Todos"
                    )
                    && !valorTexto(
                            item.estadoOperativo()
                    ).equalsIgnoreCase(
                            estado
                    )
            ) {
                continue;
            }

            if (
                    tipoMaquinaria != null
                    && !tipoMaquinaria.isBlank()
                    && !tipoMaquinaria.equalsIgnoreCase(
                            "Todos"
                    )
                    && !valorTexto(
                            item.tipoMaquinaria()
                    ).equalsIgnoreCase(
                            tipoMaquinaria
                    )
            ) {
                continue;
            }

            if (
                    proveedor != null
                    && !proveedor.isBlank()
            ) {

                String criterioProveedor =
                        proveedor
                                .trim()
                                .toUpperCase();

                if (
                        !valorTexto(
                                item.proveedor()
                        )
                                .toUpperCase()
                                .contains(
                                        criterioProveedor
                                )
                ) {
                    continue;
                }
            }

            if (
                    codigo != null
                    && !codigo.isBlank()
            ) {

                String criterio =
                        codigo
                                .trim()
                                .toUpperCase();

                boolean coincide =
                        valorTexto(
                                item.codigo()
                        )
                                .toUpperCase()
                                .contains(
                                        criterio
                                )
                        ||
                        valorTexto(
                                item.descripcion()
                        )
                                .toUpperCase()
                                .contains(
                                        criterio
                                );

                if (!coincide) {
                    continue;
                }
            }

            filtrados.add(
                    item
            );
        }

        return filtrados;
    }

    public static List<MaquinariaResumen>
            obtenerTodas() throws Exception {

        return buscar(
                "Todos",
                "Todos",
                "",
                ""
        );
    }

    /*
     * ============================================================
     * DETALLE POR ID
     * GET /api/maquinarias/{id}
     * ============================================================
     */

    public static MaquinariaDetalle obtenerPorId(
            int idMaquinaria
    ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/maquinarias/"
                                + idMaquinaria
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject item =
                obtenerObjetoDatos(
                        respuesta
                );

        if (item == null) {

            throw new Exception(
                    "No se encontró la maquinaria."
            );
        }

        return new MaquinariaDetalle(
                obtenerEntero(
                        item,
                        "idMaquinaria"
                ),

                obtenerTexto(
                        item,
                        "codigoInterno"
                ),

                obtenerTexto(
                        item,
                        "codigoActual"
                ),

                obtenerTexto(
                        item,
                        "codigoPlaca"
                ),

                obtenerTexto(
                        item,
                        "descripcion"
                ),

                obtenerEntero(
                        item,
                        "idTipoMaquinaria"
                ),

                obtenerTexto(
                        item,
                        "tipoMaquinaria"
                ),

                obtenerTexto(
                        item,
                        "modelo"
                ),

                obtenerTexto(
                        item,
                        "serieMaquina"
                ),

                obtenerTexto(
                        item,
                        "serieActual"
                ),

                obtenerDoubleNullable(
                        item,
                        "horometroActual"
                ),

                obtenerBoolean(
                        item,
                        "horometroConfirmado"
                ),

                obtenerEnteroNullable(
                        item,
                        "idProveedor"
                ),

                obtenerTexto(
                        item,
                        "proveedor"
                ),

                obtenerEnteroNullable(
                        item,
                        "idPropietario"
                ),

                obtenerTexto(
                        item,
                        "propietario"
                ),

                obtenerTexto(
                        item,
                        "tipoPropiedad"
                ),

                obtenerTexto(
                        item,
                        "estadoOperativo"
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
                ),

                obtenerDecimal(
                        item,
                        "precioHoraCliente"
                ),

                obtenerDecimal(
                        item,
                        "precioFijoCliente"
                ),

                obtenerTexto(
                        item,
                        "observaciones"
                ),

                obtenerBoolean(
                        item,
                        "activo"
                )
        );
    }

    /*
     * ============================================================
     * TIPOS DE MAQUINARIA
     * GET /api/maquinarias/tipos
     * ============================================================
     */

    public static List<CatalogoItem>
            obtenerTiposMaquinaria() throws Exception {

        return obtenerCatalogo(
                "/api/maquinarias/tipos"
        );
    }

    /*
     * ============================================================
     * ENTIDADES ACTIVAS
     * GET /api/maquinarias/entidades
     * ============================================================
     */

    public static List<CatalogoItem>
            obtenerEntidades() throws Exception {

        return obtenerCatalogo(
                "/api/maquinarias/entidades"
        );
    }

    private static List<CatalogoItem>
            obtenerCatalogo(
                    String ruta
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        ruta
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        List<CatalogoItem> lista =
                new ArrayList<>();

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new CatalogoItem(
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

    /*
     * ============================================================
     * DESCRIPCIÓN POR NÚMERO / CÓDIGO
     * GET /api/maquinarias/descripcion
     * ============================================================
     */

    public static String
            obtenerDescripcionPorNumeroMaquina(
                    String numeroMaquina
            ) throws Exception {

        if (
                numeroMaquina == null
                || numeroMaquina.isBlank()
        ) {
            return "";
        }

        String ruta =
                "/api/maquinarias/descripcion"
                        + "?numeroMaquina="
                        + URLEncoder.encode(
                                numeroMaquina.trim(),
                                StandardCharsets.UTF_8
                        );

        String respuestaJson =
                ConexionAPI.get(
                        ruta
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        if (
                !respuesta.has(
                        "datos"
                )
                || respuesta
                        .get(
                                "datos"
                        )
                        .isJsonNull()
        ) {
            return "";
        }

        return respuesta
                .get(
                        "datos"
                )
                .getAsString();
    }

    /*
     * ============================================================
     * CREAR MAQUINARIA
     * POST /api/maquinarias
     * ============================================================
     */

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
                crearCuerpoMaquinaria(
                        codigoInterno,
                        codigoActual,
                        codigoPlaca,
                        descripcion,
                        idTipoMaquinaria,
                        modelo,
                        serieMaquina,
                        serieActual,
                        horometroActual,
                        horometroConfirmado,
                        idProveedor,
                        idPropietario,
                        tipoPropiedad,
                        estadoOperativo,
                        tipoCobro,
                        costoHoraProveedor,
                        costoFijoProveedor,
                        precioHoraCliente,
                        precioFijoCliente,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/maquinarias",
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
                        "idMaquinaria"
                )
                || datos
                        .get(
                                "idMaquinaria"
                        )
                        .isJsonNull()
        ) {

            throw new Exception(
                    "La maquinaria fue guardada, "
                            + "pero la API no devolvió su ID."
            );
        }

        return datos
                .get(
                        "idMaquinaria"
                )
                .getAsInt();
    }

    /*
     * Alias compatible con el nombre anterior del DAO.
     */

    public static int insertar(
            String codigoInterno,
            String codigoActual,
            String placa,
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

        return crearMaquinaria(
                codigoInterno,
                codigoActual,
                placa,
                descripcion,
                idTipoMaquinaria,
                modelo,
                serieMaquina,
                serieActual,
                horometroActual,
                horometroConfirmado,
                idProveedor,
                idPropietario,
                tipoPropiedad,
                estadoOperativo,
                normalizarTipoCobro(
                        tipoCobro
                ),
                costoHoraProveedor,
                costoFijoProveedor,
                precioHoraCliente,
                precioFijoCliente,
                observaciones
        );
    }

    /*
     * ============================================================
     * ACTUALIZAR MAQUINARIA
     * PUT /api/maquinarias/{id}
     * ============================================================
     */

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

        JsonObject cuerpo =
                crearCuerpoMaquinaria(
                        codigoInterno,
                        codigoActual,
                        codigoPlaca,
                        descripcion,
                        idTipoMaquinaria,
                        modelo,
                        serieMaquina,
                        serieActual,
                        horometroActual,
                        horometroConfirmado,
                        idProveedor,
                        idPropietario,
                        tipoPropiedad,
                        estadoOperativo,
                        tipoCobro,
                        costoHoraProveedor,
                        costoFijoProveedor,
                        precioHoraCliente,
                        precioFijoCliente,
                        observaciones
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/maquinarias/"
                                + idMaquinaria,
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
     * Alias compatible con el nombre anterior del DAO.
     */

    public static void actualizar(
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

        actualizarMaquinaria(
                idMaquinaria,
                codigoInterno,
                codigoActual,
                codigoPlaca,
                descripcion,
                idTipoMaquinaria,
                modelo,
                serieMaquina,
                serieActual,
                horometroActual,
                horometroConfirmado,
                idProveedor,
                idPropietario,
                tipoPropiedad,
                estadoOperativo,
                normalizarTipoCobro(
                        tipoCobro
                ),
                costoHoraProveedor,
                costoFijoProveedor,
                precioHoraCliente,
                precioFijoCliente,
                observaciones
        );
    }

    /*
     * ============================================================
     * DESACTIVAR MAQUINARIA
     * DELETE /api/maquinarias/{id}
     * ============================================================
     */

    public static void desactivarMaquinaria(
            int idMaquinaria
    ) throws Exception {

        ConexionAPI.delete(
                "/api/maquinarias/"
                        + idMaquinaria
        );
    }

    /*
     * Alias compatible con el nombre anterior del DAO.
     */

    public static void desactivar(
            int idMaquinaria
    ) throws Exception {

        desactivarMaquinaria(
                idMaquinaria
        );
    }

    /*
     * ============================================================
     * CONSTRUCCIÓN DEL JSON
     * ============================================================
     */

    private static JsonObject crearCuerpoMaquinaria(
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
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "codigoInterno",
                codigoInterno
        );

        agregarTexto(
                cuerpo,
                "codigoActual",
                codigoActual
        );

        agregarTexto(
                cuerpo,
                "codigoPlaca",
                codigoPlaca
        );

        agregarTexto(
                cuerpo,
                "descripcion",
                descripcion
        );

        cuerpo.addProperty(
                "idTipoMaquinaria",
                idTipoMaquinaria
        );

        agregarTexto(
                cuerpo,
                "modelo",
                modelo
        );

        agregarTexto(
                cuerpo,
                "serieMaquina",
                serieMaquina
        );

        agregarTexto(
                cuerpo,
                "serieActual",
                serieActual
        );

        agregarDoubleNullable(
                cuerpo,
                "horometroActual",
                horometroActual
        );

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

        agregarTexto(
                cuerpo,
                "tipoPropiedad",
                tipoPropiedad
        );

        agregarTexto(
                cuerpo,
                "estadoOperativo",
                estadoOperativo
        );

        agregarTexto(
                cuerpo,
                "tipoCobro",
                normalizarTipoCobro(
                        tipoCobro
                )
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

        return cuerpo;
    }

    /*
     * ============================================================
     * NORMALIZACIÓN
     * ============================================================
     */

    private static String normalizarTipoCobro(
            String tipoCobro
    ) {

        if (
                tipoCobro == null
                || tipoCobro.isBlank()
        ) {

            return "POR_HORA";
        }

        return tipoCobro
                .trim()
                .toUpperCase();
    }

    /*
     * ============================================================
     * RESPUESTA API
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

    private static Integer obtenerEnteroNullable(
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
                .getAsInt();
    }

    private static double obtenerDecimal(
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

            return 0.00;
        }

        return objeto
                .get(
                        propiedad
                )
                .getAsDouble();
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

    private static boolean obtenerBoolean(
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

            return false;
        }

        return objeto
                .get(
                        propiedad
                )
                .getAsBoolean();
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
                || valor.trim().isEmpty()
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

    private static void agregarEnteroNullable(
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

    private static void agregarDoubleNullable(
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

    private static String valorTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor;
    }
}