import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MigradorCodigosHistoricos {

    public record ResultadoMigracion(
            int procesadas,
            int insertadas,
            int actualizadas,
            int vinculadas,
            int pendientes,
            int noEncontradas,
            int omitidas,
            List<String> errores
    ) {
    }

    public static ResultadoMigracion importar(
            List<LectorTXTCodigosHistoricos
                    .FilaCodigoHistorico> filas
    ) throws Exception {

        if (
            filas == null
            || filas.isEmpty()
        ) {

            throw new Exception(
                    "No existen códigos históricos para importar."
            );
        }

        Connection conexion = null;

        int procesadas = 0;
        int insertadas = 0;
        int actualizadas = 0;
        int vinculadas = 0;
        int pendientes = 0;
        int noEncontradas = 0;
        int omitidas = 0;

        List<String> errores =
                new ArrayList<>();

        try {

            conexion =
                    ConexionDB.obtenerConexion();

            conexion.setAutoCommit(false);

            for (
                    LectorTXTCodigosHistoricos
                            .FilaCodigoHistorico fila
                    : filas
            ) {

                procesadas++;

                try {

                    Integer idProveedor =
                            obtenerIdEntidad(
                                    conexion,
                                    fila.razonSocial()
                            );

                    Integer idMaquinaria = null;

                    String estadoVinculacion;

                    if (
                        fila.codigoActual() == null
                        || fila.codigoActual().isBlank()
                    ) {

                        estadoVinculacion =
                                "PENDIENTE";

                        pendientes++;

                    } else {

                        idMaquinaria =
                                buscarMaquinariaPorCodigo(
                                        conexion,
                                        fila.codigoActual()
                                );

                        if (idMaquinaria == null) {

                            estadoVinculacion =
                                    "NO_ENCONTRADO";

                            noEncontradas++;

                        } else {

                            estadoVinculacion =
                                    "VINCULADO";

                            vinculadas++;
                        }
                    }

                    Integer idHistoricoExistente =
                            buscarHistoricoExistente(
                                    conexion,
                                    fila.codigoAnterior(),
                                    idMaquinaria,
                                    fila.codigoActual()
                            );

                    if (idHistoricoExistente == null) {

                        insertarHistorico(
                                conexion,
                                idMaquinaria,
                                idProveedor,
                                fila,
                                estadoVinculacion
                        );

                        insertadas++;

                    } else {

                        actualizarHistorico(
                                conexion,
                                idHistoricoExistente,
                                idMaquinaria,
                                idProveedor,
                                fila,
                                estadoVinculacion
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
                    vinculadas,
                    pendientes,
                    noEncontradas,
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

    private static Integer buscarMaquinariaPorCodigo(
            Connection conexion,
            String codigoActual
    ) throws Exception {

        if (
            codigoActual == null
            || codigoActual.isBlank()
        ) {

            return null;
        }

        String sql = """
                SELECT id_maquinaria
                FROM maquinaria
                WHERE activo = 1
                  AND (
                        UPPER(TRIM(codigo_actual)) =
                        UPPER(TRIM(?))

                     OR UPPER(TRIM(codigo_placa)) =
                        UPPER(TRIM(?))
                  )
                LIMIT 1
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    codigoActual.trim()
            );

            ps.setString(
                    2,
                    codigoActual.trim()
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

    private static Integer buscarHistoricoExistente(
            Connection conexion,
            String codigoAnterior,
            Integer idMaquinaria,
            String codigoActualOrigen
    ) throws Exception {

        String sql;

        if (idMaquinaria != null) {

            sql = """
                    SELECT id_codigo_historico
                    FROM maquinaria_codigos_historicos
                    WHERE activo = 1
                      AND id_maquinaria = ?
                      AND UPPER(TRIM(codigo_anterior)) =
                          UPPER(TRIM(?))
                    LIMIT 1
                    """;

        } else {

            sql = """
                    SELECT id_codigo_historico
                    FROM maquinaria_codigos_historicos
                    WHERE activo = 1
                      AND id_maquinaria IS NULL
                      AND UPPER(TRIM(codigo_anterior)) =
                          UPPER(TRIM(?))
                      AND (
                            (
                                codigo_actual_origen IS NULL
                                AND (
                                    ? IS NULL
                                    OR TRIM(?) = ''
                                )
                            )
                            OR
                            UPPER(TRIM(
                                COALESCE(
                                    codigo_actual_origen,
                                    ''
                                )
                            )) =
                            UPPER(TRIM(
                                COALESCE(
                                    ?,
                                    ''
                                )
                            ))
                      )
                    LIMIT 1
                    """;
        }

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            if (idMaquinaria != null) {

                ps.setInt(
                        1,
                        idMaquinaria
                );

                ps.setString(
                        2,
                        codigoAnterior.trim()
                );

            } else {

                ps.setString(
                        1,
                        codigoAnterior.trim()
                );

                ps.setString(
                        2,
                        codigoActualOrigen
                );

                ps.setString(
                        3,
                        codigoActualOrigen
                );

                ps.setString(
                        4,
                        codigoActualOrigen
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "id_codigo_historico"
                    );
                }
            }
        }

        return null;
    }

    private static void insertarHistorico(
            Connection conexion,
            Integer idMaquinaria,
            Integer idProveedor,
            LectorTXTCodigosHistoricos
                    .FilaCodigoHistorico fila,
            String estadoVinculacion
    ) throws Exception {

        String sql = """
                INSERT INTO maquinaria_codigos_historicos (
                    id_maquinaria,
                    codigo_anterior,
                    id_proveedor_original,
                    codigo_actual_origen,
                    descripcion_original,
                    costo_hora_original,
                    estado_vinculacion,
                    activo,
                    observaciones
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?)
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            asignarEntero(
                    ps,
                    1,
                    idMaquinaria
            );

            ps.setString(
                    2,
                    fila.codigoAnterior().trim()
            );

            asignarEntero(
                    ps,
                    3,
                    idProveedor
            );

            asignarTexto(
                    ps,
                    4,
                    fila.codigoActual()
            );

            asignarTexto(
                    ps,
                    5,
                    fila.descripcion()
            );

            ps.setDouble(
                    6,
                    fila.costoHora()
            );

            ps.setString(
                    7,
                    estadoVinculacion
            );

            ps.setString(
                    8,
                    "Importado desde códigos alquilados. "
                            + "Línea original: "
                            + fila.numeroLinea()
            );

            int filasInsertadas =
                    ps.executeUpdate();

            if (filasInsertadas == 0) {

                throw new Exception(
                        "No fue posible insertar "
                                + "el código histórico."
                );
            }
        }
    }

    private static void actualizarHistorico(
            Connection conexion,
            int idCodigoHistorico,
            Integer idMaquinaria,
            Integer idProveedor,
            LectorTXTCodigosHistoricos
                    .FilaCodigoHistorico fila,
            String estadoVinculacion
    ) throws Exception {

        String sql = """
                UPDATE maquinaria_codigos_historicos
                SET
                    id_maquinaria = ?,
                    codigo_anterior = ?,
                    id_proveedor_original = ?,
                    codigo_actual_origen = ?,
                    descripcion_original = ?,
                    costo_hora_original = ?,
                    estado_vinculacion = ?,
                    activo = 1,
                    observaciones = ?
                WHERE id_codigo_historico = ?
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            asignarEntero(
                    ps,
                    1,
                    idMaquinaria
            );

            ps.setString(
                    2,
                    fila.codigoAnterior().trim()
            );

            asignarEntero(
                    ps,
                    3,
                    idProveedor
            );

            asignarTexto(
                    ps,
                    4,
                    fila.codigoActual()
            );

            asignarTexto(
                    ps,
                    5,
                    fila.descripcion()
            );

            ps.setDouble(
                    6,
                    fila.costoHora()
            );

            ps.setString(
                    7,
                    estadoVinculacion
            );

            ps.setString(
                    8,
                    "Actualizado desde códigos alquilados. "
                            + "Línea original: "
                            + fila.numeroLinea()
            );

            ps.setInt(
                    9,
                    idCodigoHistorico
            );

            int filasActualizadas =
                    ps.executeUpdate();

            if (filasActualizadas == 0) {

                throw new Exception(
                        "No fue posible actualizar "
                                + "el código histórico."
                );
            }
        }
    }

    private static Integer obtenerIdEntidad(
            Connection conexion,
            String nombre
    ) throws Exception {

        if (
            nombre == null
            || nombre.isBlank()
        ) {

            return null;
        }

        String sqlConsulta = """
                SELECT id_entidad
                FROM entidades_maquinaria
                WHERE UPPER(TRIM(nombre)) =
                      UPPER(TRIM(?))
                LIMIT 1
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sqlConsulta
                        )
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

        String sqlInsertar = """
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
                    'Creado desde códigos históricos'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sqlInsertar,
                                java.sql.Statement.RETURN_GENERATED_KEYS
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
