import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsignacionMaquinariaDAO {

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

    public record DatosHistoricosMaquinaria(
        String codigo,
        String descripcion,
        String proveedor,
        Double tarifa
) {
}

    public static List<ProyectoItem>
            obtenerProyectosActivos() throws Exception {

        String sql = """
                SELECT
                    id_proyecto,
                    codigo_proyecto,
                    descripcion
                FROM proyectos
                WHERE activo = 1
                  AND estado NOT IN (
                        'FINALIZADO',
                        'CANCELADO'
                  )
                ORDER BY codigo_proyecto
                """;

        List<ProyectoItem> proyectos =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                proyectos.add(
                        new ProyectoItem(
                                rs.getInt("id_proyecto"),
                                rs.getString("codigo_proyecto"),
                                rs.getString("descripcion")
                        )
                );
            }
        }

        return proyectos;
    }

    public static List<MaquinariaItem>
            obtenerMaquinariasDisponibles() throws Exception {

        String sql = """
                SELECT
                    m.id_maquinaria,

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
                        propietario.nombre,
                        'Sin propietario'
                    ) AS propietario,

                    NULL AS tarifa_referencia

                FROM maquinaria m

                LEFT JOIN entidades_maquinaria propietario
                    ON propietario.id_entidad =
                       m.id_propietario

                WHERE m.activo = 1
                  
                ORDER BY
                    codigo,
                    m.descripcion
                """;

        List<MaquinariaItem> maquinarias =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                maquinarias.add(
                        new MaquinariaItem(
                                rs.getInt("id_maquinaria"),
                                rs.getString("codigo"),
                                rs.getString("descripcion"),
                                rs.getString("propietario"),
                                obtenerDoubleNullable(
                                        rs,
                                        "tarifa_referencia"
                                )
                        )
                );
            }
        }

        return maquinarias;
    }

    public static List<AsignacionResumen>
            obtenerActivas() throws Exception {

        String sql = """
                SELECT
                    a.id_asignacion,

                    CONCAT(
                        p.codigo_proyecto,
                        ' - ',
                        p.descripcion
                    ) AS proyecto,

                    CONCAT(
                        COALESCE(
                            NULLIF(m.codigo_actual, ''),
                            NULLIF(m.codigo_placa, ''),
                            NULLIF(m.codigo_interno, ''),
                            'SIN CÓDIGO'
                        ),
                        ' - ',
                        COALESCE(m.descripcion, '')
                    ) AS maquinaria,

                    COALESCE(
                        propietario.nombre,
                        'Sin propietario'
                    ) AS propietario,

                    a.cantidad,
                    a.fecha_ingreso,
                    a.fecha_salida,
                    a.tarifa_hora_asignada,
                    a.estado

                FROM proyecto_maquinaria a

                INNER JOIN proyectos p
                    ON p.id_proyecto =
                       a.id_proyecto

                INNER JOIN maquinaria m
                    ON m.id_maquinaria =
                       a.id_maquinaria

                LEFT JOIN entidades_maquinaria propietario
                    ON propietario.id_entidad =
                       m.id_propietario

                WHERE a.activo = 1

                ORDER BY a.id_asignacion DESC
                """;

        List<AsignacionResumen> asignaciones =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Date fechaIngresoSql =
                        rs.getDate("fecha_ingreso");

                Date fechaSalidaSql =
                        rs.getDate("fecha_salida");

                asignaciones.add(
                        new AsignacionResumen(
                                rs.getInt("id_asignacion"),
                                rs.getString("proyecto"),
                                rs.getString("maquinaria"),
                                rs.getString("propietario"),
                                rs.getInt("cantidad"),
                                fechaIngresoSql == null
                                        ? null
                                        : fechaIngresoSql.toLocalDate(),
                                fechaSalidaSql == null
                                        ? null
                                        : fechaSalidaSql.toLocalDate(),
                                obtenerDoubleNullable(
                                        rs,
                                        "tarifa_hora_asignada"
                                ),
                                rs.getString("estado")
                        )
                );
            }
        }

        return asignaciones;
    }

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

        validarDatos(
                idProyecto,
                idMaquinaria,
                cantidad,
                fechaIngreso,
                fechaSalida
        );

        validarAsignacionDuplicada(
                idProyecto,
                idMaquinaria,
                fechaIngreso
        );
        DatosHistoricosMaquinaria datosHistoricos =
            obtenerDatosHistoricosMaquinaria(
                    idMaquinaria
            );

        Double tarifaGuardar =
                tarifaHoraAsignada == null
                        ? datosHistoricos.tarifa()
                        : tarifaHoraAsignada;

        String sql = """
            INSERT INTO proyecto_maquinaria (
                id_proyecto,
                id_maquinaria,
                codigo_historico,
                descripcion_historica,
                proveedor_historico,
                cantidad,
                fecha_ingreso,
                fecha_salida,
                tarifa_hora_asignada,
                estado,
                observaciones,
                activo
            )
            VALUES (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1
            )
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
                    idProyecto
            );

            ps.setInt(
                    2,
                    idMaquinaria
            );

            asignarTexto(
                    ps,
                    3,
                    datosHistoricos.codigo()
            );

            asignarTexto(
                    ps,
                    4,
                    datosHistoricos.descripcion()
            );

            asignarTexto(
                    ps,
                    5,
                    datosHistoricos.proveedor()
            );

            ps.setInt(
                    6,
                    cantidad
            );

            ps.setDate(
                    7,
                    Date.valueOf(fechaIngreso)
            );

            asignarFecha(
                    ps,
                    8,
                    fechaSalida
            );

            asignarDecimal(
                    ps,
                    9,
                    tarifaGuardar
            );

            ps.setString(
                    10,
                    estado == null
                            || estado.isBlank()
                            ? "ASIGNADA"
                :            estado
            );

            asignarTexto(
                    ps,
                    11,
                    observaciones
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar la asignación."
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
                "La asignación fue guardada, "
                        + "pero no se pudo obtener su ID."
        );
    }

    private static DatosHistoricosMaquinaria
        obtenerDatosHistoricosMaquinaria(
                int idMaquinaria
        ) throws Exception {

    String sql = """
            SELECT
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
                    proveedor.nombre,
                    'Sin proveedor'
                ) AS proveedor,

                NULL AS tarifa

            FROM maquinaria m

            LEFT JOIN entidades_maquinaria proveedor
                ON proveedor.id_entidad =
                   m.id_proveedor

            WHERE m.id_maquinaria = ?
              AND m.activo = 1

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

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró la maquinaria seleccionada."
                );
            }

            return new DatosHistoricosMaquinaria(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    obtenerDoubleNullable(
                            rs,
                            "tarifa"
                    )
            );
        }
    }
}



    private static void validarDatos(
            int idProyecto,
            int idMaquinaria,
            int cantidad,
            LocalDate fechaIngreso,
            LocalDate fechaSalida
    ) throws Exception {

        if (idProyecto <= 0) {

            throw new Exception(
                    "Debe seleccionar un proyecto."
            );
        }

        if (idMaquinaria <= 0) {

            throw new Exception(
                    "Debe seleccionar una maquinaria."
            );
        }

        if (cantidad <= 0) {

            throw new Exception(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (fechaIngreso == null) {

            throw new Exception(
                    "La fecha de ingreso es obligatoria."
            );
        }

        if (
            fechaSalida != null
            && fechaSalida.isBefore(fechaIngreso)
        ) {

            throw new Exception(
                    "La fecha de salida no puede ser "
                            + "anterior a la fecha de ingreso."
            );
        }
    }

    private static void validarAsignacionDuplicada(
            int idProyecto,
            int idMaquinaria,
            LocalDate fechaIngreso
    ) throws Exception {

        String sql = """
                SELECT id_asignacion
                FROM proyecto_maquinaria
                WHERE activo = 1
                  AND id_proyecto = ?
                  AND id_maquinaria = ?
                  AND fecha_ingreso = ?
                  AND estado <> 'CANCELADA'
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
                    idProyecto
            );

            ps.setInt(
                    2,
                    idMaquinaria
            );

            ps.setDate(
                    3,
                    Date.valueOf(fechaIngreso)
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "La maquinaria ya está asignada "
                                    + "a ese proyecto en la misma fecha."
                    );
                }
            }
        }
    }

    private static Double obtenerDoubleNullable(
            ResultSet rs,
            String columna
    ) throws Exception {

        Object valor =
                rs.getObject(columna);

        return valor == null
                ? null
                : rs.getDouble(columna);
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

    private static void asignarFecha(
            PreparedStatement ps,
            int parametro,
            LocalDate valor
    ) throws Exception {

        if (valor == null) {

            ps.setNull(
                    parametro,
                    Types.DATE
            );

        } else {

            ps.setDate(
                    parametro,
                    Date.valueOf(valor)
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
}