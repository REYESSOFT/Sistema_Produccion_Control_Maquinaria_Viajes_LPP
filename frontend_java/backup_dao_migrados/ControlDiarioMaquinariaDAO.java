import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControlDiarioMaquinariaDAO {

    public record MaquinariaItem(
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

            return codigoMostrar
                    + " - "
                    + descripcion;
        }
    }

    public record ControlMaquinariaResumen(
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
            String observaciones
    ) {
    }

    public static List<MaquinariaItem> obtenerMaquinariasAsignadas(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {

            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String sql = """
                SELECT DISTINCT
                    m.id_maquinaria,
                    COALESCE(
                        NULLIF(m.codigo_actual, ''),
                        NULLIF(m.codigo_placa, ''),
                        NULLIF(m.codigo_interno, ''),
                        'SIN CÓDIGO'
                    ) AS codigo,
                    COALESCE(m.descripcion, '') AS descripcion,
                    COALESCE(m.tipo_cobro, 'POR_HORA') AS tipo_cobro,
                    COALESCE(m.costo_hora_proveedor, 0)
                        AS costo_hora_proveedor,
                    COALESCE(m.costo_fijo_proveedor, 0)
                        AS costo_fijo_proveedor
                FROM control_diario cd
                INNER JOIN proyecto_maquinaria pm
                    ON pm.id_proyecto = cd.id_proyecto
                   AND pm.activo = 1
                   AND pm.estado <> 'CANCELADA'
                   AND cd.fecha_control >= pm.fecha_ingreso
                   AND cd.fecha_control <= COALESCE(
                        pm.fecha_salida,
                        '9999-12-31'
                   )
                INNER JOIN maquinaria m
                    ON m.id_maquinaria = pm.id_maquinaria
                   AND m.activo = 1
                WHERE cd.id_control = ?
                  AND cd.activo = 1
                ORDER BY codigo, descripcion
                """;

        List<MaquinariaItem> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            new MaquinariaItem(
                                    rs.getInt(
                                            "id_maquinaria"
                                    ),
                                    rs.getString(
                                            "codigo"
                                    ),
                                    rs.getString(
                                            "descripcion"
                                    ),
                                    rs.getString(
                                            "tipo_cobro"
                                    ),
                                    rs.getDouble(
                                            "costo_hora_proveedor"
                                    ),
                                    rs.getDouble(
                                            "costo_fijo_proveedor"
                                    )
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static List<ControlMaquinariaResumen> obtenerPorControl(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {

            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String sql = """
                SELECT
                    cdm.id_control_maquinaria,
                    cdm.id_control,
                    cdm.id_maquinaria,

                    COALESCE(
                        NULLIF(m.codigo_actual, ''),
                        NULLIF(m.codigo_placa, ''),
                        NULLIF(m.codigo_interno, ''),
                        'SIN CÓDIGO'
                    ) AS codigo,

                    COALESCE(
                        m.descripcion,
                        ''
                    ) AS descripcion,

                    COALESCE(
                        m.tipo_cobro,
                        'POR_HORA'
                    ) AS tipo_cobro,

                    COALESCE(
                        cdm.horas_trabajadas,
                        0
                    ) AS horas_trabajadas,

                    COALESCE(
                        m.costo_hora_proveedor,
                        0
                    ) AS costo_hora_proveedor,

                    COALESCE(
                        m.costo_fijo_proveedor,
                        0
                    ) AS costo_fijo_proveedor,

                    COALESCE(
                        cdm.observaciones,
                        ''
                    ) AS observaciones,

                    CASE
                        WHEN EXISTS (
                            SELECT 1
                            FROM control_diario_maquinaria cdm_anterior
                            INNER JOIN control_diario cd_anterior
                                ON cd_anterior.id_control =
                                   cdm_anterior.id_control
                               AND cd_anterior.activo = 1
                            WHERE cdm_anterior.id_maquinaria =
                                  cdm.id_maquinaria
                              AND cdm_anterior.activo = 1
                              AND cd_anterior.id_proyecto =
                                  cd.id_proyecto
                              AND (
                                    cd_anterior.fecha_control <
                                    cd.fecha_control

                                    OR (
                                        cd_anterior.fecha_control =
                                        cd.fecha_control

                                        AND
                                        cdm_anterior.id_control_maquinaria <
                                        cdm.id_control_maquinaria
                                    )
                              )
                        )
                        THEN 1
                        ELSE 0
                    END AS servicio_cobrado_antes

                FROM control_diario_maquinaria cdm

                INNER JOIN control_diario cd
                    ON cd.id_control = cdm.id_control
                   AND cd.activo = 1

                INNER JOIN maquinaria m
                    ON m.id_maquinaria = cdm.id_maquinaria

                WHERE cdm.id_control = ?
                  AND cdm.activo = 1

                ORDER BY
                    codigo,
                    descripcion
                """;

        List<ControlMaquinariaResumen> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    String tipoCobro =
                            normalizarTipoCobro(
                                    rs.getString(
                                            "tipo_cobro"
                                    )
                            );

                    double horasTrabajadas =
                            rs.getDouble(
                                    "horas_trabajadas"
                            );

                    double costoHora =
                            rs.getDouble(
                                    "costo_hora_proveedor"
                            );

                    double costoFijo =
                            rs.getDouble(
                                    "costo_fijo_proveedor"
                            );

                    boolean servicioCobradoAntes =
                            rs.getInt(
                                    "servicio_cobrado_antes"
                            ) == 1;

                    double costoCalculado =
                            calcularCostoDiario(
                                    tipoCobro,
                                    horasTrabajadas,
                                    costoHora,
                                    costoFijo,
                                    servicioCobradoAntes
                            );

                    lista.add(
                            new ControlMaquinariaResumen(
                                    rs.getInt(
                                            "id_control_maquinaria"
                                    ),
                                    rs.getInt(
                                            "id_control"
                                    ),
                                    rs.getInt(
                                            "id_maquinaria"
                                    ),
                                    rs.getString(
                                            "codigo"
                                    ),
                                    rs.getString(
                                            "descripcion"
                                    ),
                                    tipoCobro,
                                    horasTrabajadas,
                                    costoHora,
                                    costoFijo,
                                    costoCalculado,
                                    rs.getString(
                                            "observaciones"
                                    )
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static int insertar(
            int idControl,
            int idMaquinaria,
            double horasTrabajadas,
            String observaciones
    ) throws Exception {

        validarDatos(
                idControl,
                idMaquinaria,
                horasTrabajadas
        );

        validarMaquinariaAsignada(
                idControl,
                idMaquinaria
        );

        validarDuplicado(
                idControl,
                idMaquinaria,
                0
        );

        String sql = """
                INSERT INTO control_diario_maquinaria (
                    id_control,
                    id_maquinaria,
                    horas_trabajadas,
                    observaciones,
                    activo
                )
                VALUES (?, ?, ?, ?, 1)
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            ps.setInt(
                    2,
                    idMaquinaria
            );

            ps.setDouble(
                    3,
                    horasTrabajadas
            );

            asignarTexto(
                    ps,
                    4,
                    observaciones
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar la maquinaria "
                                + "del Control Diario."
                );
            }

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
                "La maquinaria fue guardada, "
                        + "pero no se pudo obtener su ID."
        );
    }

    public static void actualizar(
            int idControlMaquinaria,
            int idControl,
            int idMaquinaria,
            double horasTrabajadas,
            String observaciones
    ) throws Exception {

        if (idControlMaquinaria <= 0) {

            throw new Exception(
                    "El registro seleccionado no es válido."
            );
        }

        validarDatos(
                idControl,
                idMaquinaria,
                horasTrabajadas
        );

        validarMaquinariaAsignada(
                idControl,
                idMaquinaria
        );

        validarDuplicado(
                idControl,
                idMaquinaria,
                idControlMaquinaria
        );

        String sql = """
                UPDATE control_diario_maquinaria
                SET
                    id_maquinaria = ?,
                    horas_trabajadas = ?,
                    observaciones = ?
                WHERE id_control_maquinaria = ?
                  AND id_control = ?
                  AND activo = 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idMaquinaria
            );

            ps.setDouble(
                    2,
                    horasTrabajadas
            );

            asignarTexto(
                    ps,
                    3,
                    observaciones
            );

            ps.setInt(
                    4,
                    idControlMaquinaria
            );

            ps.setInt(
                    5,
                    idControl
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar la maquinaria "
                                + "del Control Diario."
                );
            }
        }
    }

    public static void eliminar(
            int idControlMaquinaria
    ) throws Exception {

        if (idControlMaquinaria <= 0) {

            throw new Exception(
                    "El registro seleccionado no es válido."
            );
        }

        String sql = """
                UPDATE control_diario_maquinaria
                SET activo = 0
                WHERE id_control_maquinaria = ?
                  AND activo = 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idControlMaquinaria
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible eliminar el registro. "
                                + "Puede que ya haya sido eliminado."
                );
            }
        }
    }

    public static double obtenerCostoTotalControl(
            int idControl
    ) throws Exception {

        double total =
                0.00;

        List<ControlMaquinariaResumen> lista =
                obtenerPorControl(
                        idControl
                );

        for (
                ControlMaquinariaResumen item
                : lista
        ) {

            total +=
                    item.costoCalculado();
        }

        return total;
    }

    private static double calcularCostoDiario(
            String tipoCobro,
            double horasTrabajadas,
            double costoHora,
            double costoFijo,
            boolean servicioCobradoAntes
    ) {

        return switch (
                normalizarTipoCobro(
                        tipoCobro
                )
        ) {

            case "FIJO_DIARIO" ->
                    costoFijo;

            case "FIJO_SERVICIO" ->
                    servicioCobradoAntes
                            ? 0.00
                            : costoFijo;

            default ->
                    horasTrabajadas
                            * costoHora;
        };
    }

    private static void validarDatos(
            int idControl,
            int idMaquinaria,
            double horasTrabajadas
    ) throws Exception {

        if (idControl <= 0) {

            throw new Exception(
                    "Debe seleccionar un Control Diario válido."
            );
        }

        if (idMaquinaria <= 0) {

            throw new Exception(
                    "Debe seleccionar una maquinaria."
            );
        }

        if (horasTrabajadas < 0) {

            throw new Exception(
                    "Las horas trabajadas no pueden ser negativas."
            );
        }

        if (horasTrabajadas > 24) {

            throw new Exception(
                    "Las horas trabajadas no pueden superar 24 "
                            + "en un mismo Control Diario."
            );
        }
    }

    private static void validarMaquinariaAsignada(
            int idControl,
            int idMaquinaria
    ) throws Exception {

        String sql = """
                SELECT pm.id_asignacion
                FROM control_diario cd
                INNER JOIN proyecto_maquinaria pm
                    ON pm.id_proyecto = cd.id_proyecto
                   AND pm.id_maquinaria = ?
                   AND pm.activo = 1
                   AND pm.estado <> 'CANCELADA'
                   AND cd.fecha_control >= pm.fecha_ingreso
                   AND cd.fecha_control <= COALESCE(
                        pm.fecha_salida,
                        '9999-12-31'
                   )
                WHERE cd.id_control = ?
                  AND cd.activo = 1
                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idMaquinaria
            );

            ps.setInt(
                    2,
                    idControl
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "La maquinaria seleccionada no está "
                                    + "asignada al proyecto en la fecha "
                                    + "del Control Diario."
                    );
                }
            }
        }
    }

    private static void validarDuplicado(
            int idControl,
            int idMaquinaria,
            int idExcluir
    ) throws Exception {

        String sql = """
                SELECT id_control_maquinaria
                FROM control_diario_maquinaria
                WHERE id_control = ?
                  AND id_maquinaria = ?
                  AND activo = 1
                  AND id_control_maquinaria <> ?
                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            ps.setInt(
                    2,
                    idMaquinaria
            );

            ps.setInt(
                    3,
                    idExcluir
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "La maquinaria ya está registrada "
                                    + "en este Control Diario."
                    );
                }
            }
        }
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
            String valor
    ) throws Exception {

        if (
                valor == null
                || valor.isBlank()
        ) {

            ps.setNull(
                    parametro,
                    Types.VARCHAR
            );

        } else {

            ps.setString(
                    parametro,
                    valor.trim()
            );
        }
    }
}