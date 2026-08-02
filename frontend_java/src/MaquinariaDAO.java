import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MaquinariaDAO {

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

    public static List<MaquinariaResumen> buscar(
        String estado,
        String tipoMaquinaria,
        String proveedor,
        String codigo
) throws Exception {

    List<MaquinariaResumen> lista =
            MaquinariaAPI.obtenerResumen();

    List<MaquinariaResumen> filtrados =
            new ArrayList<>();

    for (MaquinariaResumen item : lista) {

        if (
                estado != null
                && !estado.isBlank()
                && !estado.equalsIgnoreCase("Todos")
                && !item.estadoOperativo()
                        .equalsIgnoreCase(estado)
        ) {
            continue;
        }

        if (
                tipoMaquinaria != null
                && !tipoMaquinaria.isBlank()
                && !tipoMaquinaria.equalsIgnoreCase("Todos")
                && !item.tipoMaquinaria()
                        .equalsIgnoreCase(tipoMaquinaria)
        ) {
            continue;
        }

        if (
                proveedor != null
                && !proveedor.isBlank()
                && !item.proveedor()
                        .toUpperCase()
                        .contains(
                                proveedor.trim().toUpperCase()
                        )
        ) {
            continue;
        }

        if (
                codigo != null
                && !codigo.isBlank()
        ) {

            String criterio =
                    codigo.trim().toUpperCase();

            boolean coincide =
                    item.codigo()
                            .toUpperCase()
                            .contains(criterio)
                    || item.descripcion()
                            .toUpperCase()
                            .contains(criterio);

            if (!coincide) {
                continue;
            }
        }

        filtrados.add(item);
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

    public static MaquinariaDetalle obtenerPorId(
        int idMaquinaria
) throws Exception {

    String respuestaJson =
            ConexionAPI.get(
                    "/api/maquinarias/"
                            + idMaquinaria
            );

    com.google.gson.JsonObject respuesta =
            com.google.gson.JsonParser
                    .parseString(respuestaJson)
                    .getAsJsonObject();

    if (
            respuesta == null
            || !respuesta.has("exito")
            || !respuesta.get("exito").getAsBoolean()
    ) {

        throw new Exception(
                respuesta != null
                        && respuesta.has("mensaje")
                        ? respuesta
                                .get("mensaje")
                                .getAsString()
                        : "No fue posible consultar la maquinaria."
        );
    }

    com.google.gson.JsonObject item =
            respuesta.getAsJsonObject("datos");

    if (item == null) {

        throw new Exception(
                "No se encontró la maquinaria."
        );
    }

    Double horometro =
            item.has("horometroActual")
            && !item.get("horometroActual").isJsonNull()
                    ? item.get("horometroActual").getAsDouble()
                    : null;

    Integer idProveedor =
            item.has("idProveedor")
            && !item.get("idProveedor").isJsonNull()
                    ? item.get("idProveedor").getAsInt()
                    : null;

    Integer idPropietario =
            item.has("idPropietario")
            && !item.get("idPropietario").isJsonNull()
                    ? item.get("idPropietario").getAsInt()
                    : null;

    return new MaquinariaDetalle(
            item.get("idMaquinaria").getAsInt(),

            obtenerTextoJson(
                    item,
                    "codigoInterno"
            ),

            obtenerTextoJson(
                    item,
                    "codigoActual"
            ),

            obtenerTextoJson(
                    item,
                    "codigoPlaca"
            ),

            obtenerTextoJson(
                    item,
                    "descripcion"
            ),

            item.get("idTipoMaquinaria").getAsInt(),

            "",

            obtenerTextoJson(
                    item,
                    "modelo"
            ),

            obtenerTextoJson(
                    item,
                    "serieMaquina"
            ),

            obtenerTextoJson(
                    item,
                    "serieActual"
            ),

            horometro,

            item.has("horometroConfirmado")
                    && !item.get("horometroConfirmado").isJsonNull()
                    && item.get("horometroConfirmado").getAsBoolean(),

            idProveedor,

            "",

            idPropietario,

            "",

            obtenerTextoJson(
                    item,
                    "tipoPropiedad"
            ),

            obtenerTextoJson(
                    item,
                    "estadoOperativo"
            ),

            obtenerTextoJson(
                    item,
                    "tipoCobro"
            ),

            obtenerDoubleJson(
                    item,
                    "costoHoraProveedor"
            ),

            obtenerDoubleJson(
                    item,
                    "costoFijoProveedor"
            ),

            obtenerDoubleJson(
                    item,
                    "precioHoraCliente"
            ),

            obtenerDoubleJson(
                    item,
                    "precioFijoCliente"
            ),

            obtenerTextoJson(
                    item,
                    "observaciones"
            ),

            item.has("activo")
                    && !item.get("activo").isJsonNull()
                    && item.get("activo").getAsBoolean()
    );
}
    public static List<CatalogoItem>
            obtenerTiposMaquinaria() throws Exception {

        List<CatalogoItem> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_tipo_maquinaria,
                    nombre

                FROM tipos_maquinaria

                WHERE estado = 'ACTIVO'

                ORDER BY nombre
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new CatalogoItem(
                                rs.getInt(
                                        "id_tipo_maquinaria"
                                ),

                                rs.getString(
                                        "nombre"
                                )
                        )
                );
            }
        }

        return lista;
    }

    public static List<CatalogoItem>
            obtenerEntidades() throws Exception {

        List<CatalogoItem> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_entidad,
                    nombre

                FROM entidades_maquinaria

                WHERE estado = 'ACTIVO'

                ORDER BY nombre
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new CatalogoItem(
                                rs.getInt(
                                        "id_entidad"
                                ),

                                rs.getString(
                                        "nombre"
                                )
                        )
                );
            }
        }

        return lista;
    }
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

    return MaquinariaAPI.crearMaquinaria(
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
            normalizarTipoCobro(tipoCobro),
            costoHoraProveedor,
            costoFijoProveedor,
            precioHoraCliente,
            precioFijoCliente,
            observaciones
    );
}

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

    MaquinariaAPI.actualizarMaquinaria(
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
            normalizarTipoCobro(tipoCobro),
            costoHoraProveedor,
            costoFijoProveedor,
            precioHoraCliente,
            precioFijoCliente,
            observaciones
    );
}
    public static void desactivar(
        int idMaquinaria
) throws Exception {

    MaquinariaAPI.desactivarMaquinaria(
            idMaquinaria
    );
}

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

    private static void asignarTexto(
            PreparedStatement ps,
            int parametro,
            String texto
    ) throws Exception {

        if (
                texto == null
                        || texto.trim().isEmpty()
        ) {

            ps.setNull(
                    parametro,
                    Types.VARCHAR
            );

        } else {

            ps.setString(
                    parametro,
                    texto.trim()
            );
        }
    }
    public static String obtenerDescripcionPorNumeroMaquina(String numeroMaquina) throws Exception {

    String sql = """
            SELECT descripcion
            FROM maquinaria
            WHERE activo = 1
              AND (
                    codigo_actual = ?
                 OR codigo_interno = ?
                 OR codigo_placa = ?
              )
            LIMIT 1
            """;

    try (
            Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)
    ) {

        ps.setString(1, numeroMaquina.trim());
        ps.setString(2, numeroMaquina.trim());
        ps.setString(3, numeroMaquina.trim());

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("descripcion");
            }
        }
    }

    return "";
}
private static String obtenerTextoJson(
        com.google.gson.JsonObject objeto,
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

private static double obtenerDoubleJson(
        com.google.gson.JsonObject objeto,
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
}