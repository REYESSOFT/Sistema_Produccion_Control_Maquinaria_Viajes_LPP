import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MigradorMaquinaria {

    public record ResultadoMigracion(
            int procesadas,
            int insertadas,
            int actualizadas,
            int omitidas,
            List<String> errores
    ) {
    }

    public static ResultadoMigracion importar(
            List<LectorTXTMaquinaria.FilaMaquinaria> filas
    ) throws Exception {

        if (
            filas == null
            || filas.isEmpty()
        ) {

            throw new Exception(
                    "No existen filas válidas para importar."
            );
        }

        Connection conexion = null;

        int procesadas = 0;
        int insertadas = 0;
        int actualizadas = 0;
        int omitidas = 0;

        List<String> errores =
                new ArrayList<>();

        try {

            conexion =
                    ConexionDB.obtenerConexion();

            conexion.setAutoCommit(false);

            for (
                    LectorTXTMaquinaria.FilaMaquinaria fila
                    : filas
            ) {

                procesadas++;

                try {

                    int idTipoMaquinaria =
                            obtenerIdTipo(
                                    conexion,
                                    fila.tipoMaquinaria()
                            );

                    Integer idProveedor =
                            obtenerIdEntidad(
                                    conexion,
                                    fila.proveedor()
                            );

                    Integer idPropietario =
                            obtenerIdPropietario(
                                    conexion,
                                    fila.propietario()
                            );

                    String tipoPropiedad =
                            determinarTipoPropiedad(
                                    fila.proveedor(),
                                    fila.propietario()
                            );

                    Integer idExistente =
                            buscarMaquinariaExistente(
                                    conexion,
                                    fila
                            );

                    if (idExistente == null) {

                        insertarMaquinaria(
                                conexion,
                                fila,
                                idTipoMaquinaria,
                                idProveedor,
                                idPropietario,
                                tipoPropiedad
                        );

                        insertadas++;

                    } else {

                        actualizarMaquinaria(
                                conexion,
                                idExistente,
                                fila,
                                idTipoMaquinaria,
                                idProveedor,
                                idPropietario,
                                tipoPropiedad
                        );

                        actualizadas++;
                    }

                } catch (Exception errorFila) {

                    omitidas++;

                    errores.add(
                            "Línea "
                                    + fila.numeroLinea()
                                    + ": "
                                    + errorFila.getMessage()
                    );
                }
            }

            conexion.commit();

            return new ResultadoMigracion(
                    procesadas,
                    insertadas,
                    actualizadas,
                    omitidas,
                    errores
            );

        } catch (Exception e) {

            if (conexion != null) {

                try {

                    conexion.rollback();

                } catch (Exception rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conexion != null) {

                try {

                    conexion.setAutoCommit(true);
                    conexion.close();

                } catch (Exception cierreError) {

                    cierreError.printStackTrace();
                }
            }
        }
    }

    private static Integer buscarMaquinariaExistente(
            Connection conexion,
            LectorTXTMaquinaria.FilaMaquinaria fila
    ) throws Exception {

        Integer id;

        id = buscarPorCampo(
                conexion,
                "codigo_actual",
                fila.codigoActual()
        );

        if (id != null) {
            return id;
        }

        id = buscarPorCampo(
                conexion,
                "codigo_placa",
                fila.codigoPlaca()
        );

        if (id != null) {
            return id;
        }

        id = buscarPorCampo(
                conexion,
                "codigo_interno",
                fila.codigoInterno()
        );

        if (id != null) {
            return id;
        }

        id = buscarPorCampo(
                conexion,
                "serie_actual",
                fila.serieActual()
        );

        if (id != null) {
            return id;
        }

        return buscarPorCampo(
                conexion,
                "serie_maquina",
                fila.serieMaquina()
        );
    }

    private static Integer buscarPorCampo(
            Connection conexion,
            String campo,
            String valor
    ) throws Exception {

        if (
            valor == null
            || valor.isBlank()
        ) {
            return null;
        }

        /*
         * El nombre del campo no proviene del usuario.
         * Solo se llama con columnas controladas
         * dentro de esta clase.
         */
        String sql =
                "SELECT id_maquinaria "
                        + "FROM maquinaria "
                        + "WHERE UPPER(TRIM(" + campo + ")) "
                        + "= UPPER(TRIM(?)) "
                        + "LIMIT 1";

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    valor.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "id_maquinaria"
                    );
                }
            }
        }

        return null;
    }

    private static void insertarMaquinaria(
            Connection conexion,
            LectorTXTMaquinaria.FilaMaquinaria fila,
            int idTipoMaquinaria,
            Integer idProveedor,
            Integer idPropietario,
            String tipoPropiedad
    ) throws Exception {

        String sql = """
                INSERT INTO maquinaria (
                    codigo_interno,
                    codigo_actual,
                    codigo_placa,
                    descripcion,
                    id_tipo_maquinaria,
                    serie_maquina,
                    serie_actual,
                    horometro_actual,
                    horometro_confirmado,
                    id_proveedor,
                    id_propietario,
                    tipo_propiedad,
                    estado_operativo,
                    costo_hora_proveedor,
                    precio_hora_cliente,
                    observaciones,
                    activo
                )
                VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, 0.00, ?, 1
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            asignarTexto(
                    ps,
                    1,
                    fila.codigoInterno()
            );

            asignarTexto(
                    ps,
                    2,
                    fila.codigoActual()
            );

            asignarTexto(
                    ps,
                    3,
                    fila.codigoPlaca()
            );

            ps.setString(
                    4,
                    fila.descripcion()
            );

            ps.setInt(
                    5,
                    idTipoMaquinaria
            );

            asignarTexto(
                    ps,
                    6,
                    fila.serieMaquina()
            );

            asignarTexto(
                    ps,
                    7,
                    fila.serieActual()
            );

            asignarDecimal(
                    ps,
                    8,
                    fila.horometro()
            );

            ps.setBoolean(
                    9,
                    fila.horometroConfirmado()
            );

            asignarEntero(
                    ps,
                    10,
                    idProveedor
            );

            asignarEntero(
                    ps,
                    11,
                    idPropietario
            );

            ps.setString(
                    12,
                    tipoPropiedad
            );

            ps.setString(
                    13,
                    fila.estadoOperativo()
            );

            ps.setDouble(
                    14,
                    fila.precio()
            );

            ps.setString(
                    15,
                    "Importado desde listado de maquinaria. "
                            + "Línea original: "
                            + fila.numeroLinea()
            );

            int filasInsertadas =
                    ps.executeUpdate();

            if (filasInsertadas == 0) {

                throw new Exception(
                        "No fue posible insertar la maquinaria."
                );
            }
        }
    }

    private static void actualizarMaquinaria(
            Connection conexion,
            int idMaquinaria,
            LectorTXTMaquinaria.FilaMaquinaria fila,
            int idTipoMaquinaria,
            Integer idProveedor,
            Integer idPropietario,
            String tipoPropiedad
    ) throws Exception {

        String sql = """
                UPDATE maquinaria
                SET
                    codigo_interno = ?,
                    codigo_actual = ?,
                    codigo_placa = ?,
                    descripcion = ?,
                    id_tipo_maquinaria = ?,
                    serie_maquina = ?,
                    serie_actual = ?,
                    horometro_actual = ?,
                    horometro_confirmado = ?,
                    id_proveedor = ?,
                    id_propietario = ?,
                    tipo_propiedad = ?,
                    estado_operativo = ?,
                    costo_hora_proveedor = ?,
                    observaciones = ?
                WHERE id_maquinaria = ?
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            asignarTexto(
                    ps,
                    1,
                    fila.codigoInterno()
            );

            asignarTexto(
                    ps,
                    2,
                    fila.codigoActual()
            );

            asignarTexto(
                    ps,
                    3,
                    fila.codigoPlaca()
            );

            ps.setString(
                    4,
                    fila.descripcion()
            );

            ps.setInt(
                    5,
                    idTipoMaquinaria
            );

            asignarTexto(
                    ps,
                    6,
                    fila.serieMaquina()
            );

            asignarTexto(
                    ps,
                    7,
                    fila.serieActual()
            );

            asignarDecimal(
                    ps,
                    8,
                    fila.horometro()
            );

            ps.setBoolean(
                    9,
                    fila.horometroConfirmado()
            );

            asignarEntero(
                    ps,
                    10,
                    idProveedor
            );

            asignarEntero(
                    ps,
                    11,
                    idPropietario
            );

            ps.setString(
                    12,
                    tipoPropiedad
            );

            ps.setString(
                    13,
                    fila.estadoOperativo()
            );

            ps.setDouble(
                    14,
                    fila.precio()
            );

            ps.setString(
                    15,
                    "Actualizado mediante importación. "
                            + "Línea original: "
                            + fila.numeroLinea()
            );

            ps.setInt(
                    16,
                    idMaquinaria
            );

            int filasActualizadas =
                    ps.executeUpdate();

            if (filasActualizadas == 0) {

                throw new Exception(
                        "No fue posible actualizar la maquinaria."
                );
            }
        }
    }

    private static int obtenerIdTipo(
            Connection conexion,
            String nombre
    ) throws Exception {

        if (
            nombre == null
            || nombre.isBlank()
        ) {

            nombre = "Otro";
        }

        Integer id =
                consultarIdTipo(
                        conexion,
                        nombre
                );

        if (id != null) {
            return id;
        }

        String sql = """
                INSERT INTO tipos_maquinaria (
                    nombre,
                    descripcion,
                    estado
                )
                VALUES (
                    ?,
                    'Creado mediante importación',
                    'ACTIVO'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            ps.executeUpdate();

            try (
                    ResultSet claves =
                            ps.getGeneratedKeys()
            ) {

                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }

        throw new Exception(
                "No fue posible obtener el tipo de maquinaria."
        );
    }

    private static Integer consultarIdTipo(
            Connection conexion,
            String nombre
    ) throws Exception {

        String sql = """
                SELECT id_tipo_maquinaria
                FROM tipos_maquinaria
                WHERE UPPER(TRIM(nombre)) =
                      UPPER(TRIM(?))
                LIMIT 1
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "id_tipo_maquinaria"
                    );
                }
            }
        }

        return null;
    }

    private static Integer obtenerIdEntidad(
            Connection conexion,
            String nombre
    ) throws Exception {

        if (
            nombre == null
            || nombre.isBlank()
            || nombre.equalsIgnoreCase(
                    "ALQUILADO"
            )
        ) {

            return null;
        }

        Integer id =
                consultarIdEntidad(
                        conexion,
                        nombre
                );

        if (id != null) {
            return id;
        }

        String sql = """
                INSERT INTO entidades_maquinaria (
                    nombre,
                    tipo_entidad,
                    estado,
                    observaciones
                )
                VALUES (
                    ?,
                    'OTRO',
                    'ACTIVO',
                    'Creado mediante importación'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            ps.executeUpdate();

            try (
                    ResultSet claves =
                            ps.getGeneratedKeys()
            ) {

                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }

        throw new Exception(
                "No fue posible obtener la entidad "
                        + nombre
                        + "."
        );
    }

    private static Integer obtenerIdPropietario(
            Connection conexion,
            String propietario
    ) throws Exception {

        if (
            propietario == null
            || propietario.isBlank()
            || propietario.equalsIgnoreCase(
                    "ALQUILADO"
            )
        ) {

            return null;
        }

        return obtenerIdEntidad(
                conexion,
                propietario
        );
    }

    private static Integer consultarIdEntidad(
            Connection conexion,
            String nombre
    ) throws Exception {

        String sql = """
                SELECT id_entidad
                FROM entidades_maquinaria
                WHERE UPPER(TRIM(nombre)) =
                      UPPER(TRIM(?))
                LIMIT 1
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "id_entidad"
                    );
                }
            }
        }

        return null;
    }

    private static String determinarTipoPropiedad(
            String proveedor,
            String propietario
    ) {

        String proveedorNormalizado =
                proveedor == null
                        ? ""
                        : proveedor.trim()
                                .toUpperCase();

        String propietarioNormalizado =
                propietario == null
                        ? ""
                        : propietario.trim()
                                .toUpperCase();

        if (
            propietarioNormalizado.equals(
                    "ALQUILADO"
            )
        ) {

            return "ALQUILADA";
        }

        if (
            proveedorNormalizado.equals(
                    propietarioNormalizado
            )
            && (
                proveedorNormalizado.equals(
                        "EQUIPOSPRO"
                )
                || proveedorNormalizado.equals(
                        "DEVIALTRANSPORT"
                )
            )
        ) {

            return "PROPIA";
        }

        return "TERCERO";
    }

    private static void asignarTexto(
            PreparedStatement ps,
            int parametro,
            String texto
    ) throws Exception {

        if (
            texto == null
            || texto.isBlank()
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

    private static void asignarDecimal(
            PreparedStatement ps,
            int parametro,
            Double valor
    ) throws Exception {

        if (valor == null) {

            ps.setNull(
                    parametro,
                    Types.DECIMAL
            );

        } else {

            ps.setDouble(
                    parametro,
                    valor
            );
        }
    }

    private static void asignarEntero(
            PreparedStatement ps,
            int parametro,
            Integer valor
    ) throws Exception {

        if (valor == null) {

            ps.setNull(
                    parametro,
                    Types.INTEGER
            );

        } else {

            ps.setInt(
                    parametro,
                    valor
            );
        }
    }
}
