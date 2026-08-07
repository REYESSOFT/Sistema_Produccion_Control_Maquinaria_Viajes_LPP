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

                    COALESCE(
    m.costo_hora_proveedor,
    0
) AS tarifa_referencia

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

    public static AsignacionDetalle obtenerPorId(
        int idAsignacion
) throws Exception {

    String sql = """
            SELECT
                id_asignacion,
                id_proyecto,
                id_maquinaria,
                COALESCE(codigo_historico, '') AS codigo_historico,
                COALESCE(descripcion_historica, '') AS descripcion_historica,
                COALESCE(proveedor_historico, '') AS proveedor_historico,
                cantidad,
                fecha_ingreso,
                fecha_salida,
                tarifa_hora_asignada,
                estado,
                COALESCE(observaciones, '') AS observaciones
            FROM proyecto_maquinaria
            WHERE id_asignacion = ?
              AND activo = 1
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
                idAsignacion
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró la asignación."
                );
            }

            Date fechaIngresoSql =
                    rs.getDate("fecha_ingreso");

            Date fechaSalidaSql =
                    rs.getDate("fecha_salida");

            return new AsignacionDetalle(
                    rs.getInt("id_asignacion"),
                    rs.getInt("id_proyecto"),
                    rs.getInt("id_maquinaria"),
                    rs.getString("codigo_historico"),
                    rs.getString("descripcion_historica"),
                    rs.getString("proveedor_historico"),
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
                    rs.getString("estado"),
                    rs.getString("observaciones")
            );
        }
    }
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

validarDisponibilidadMaquinaria(
        0,
        idMaquinaria,
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
    public static void actualizar(
        int idAsignacion,
        int cantidad,
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        Double tarifaHoraAsignada,
        String estado,
        String observaciones
) throws Exception {

    validarDatosEdicion(
            cantidad,
            fechaIngreso,
            fechaSalida
    );
    AsignacionDetalle asignacionActual =
        obtenerPorId(idAsignacion);

    validarDisponibilidadMaquinaria(
        idAsignacion,
        asignacionActual.idMaquinaria(),
        fechaIngreso,
        fechaSalida
    );

    String sql = """
            UPDATE proyecto_maquinaria
            SET
                cantidad = ?,
                fecha_ingreso = ?,
                fecha_salida = ?,
                tarifa_hora_asignada = ?,
                estado = ?,
                observaciones = ?
            WHERE id_asignacion = ?
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
                cantidad
        );

        ps.setDate(
                2,
                Date.valueOf(fechaIngreso)
        );

        asignarFecha(
                ps,
                3,
                fechaSalida
        );

        asignarDecimal(
                ps,
                4,
                tarifaHoraAsignada
        );

        ps.setString(
                5,
                estado
        );

        asignarTexto(
                ps,
                6,
                observaciones
        );

        ps.setInt(
                7,
                idAsignacion
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar la asignación."
            );
        }
    }
}
public static void eliminar(
        int idAsignacion
) throws Exception {

    if (idAsignacion <= 0) {

        throw new Exception(
                "La asignación seleccionada no es válida."
        );
    }

    String sql = """
            UPDATE proyecto_maquinaria
            SET activo = 0
            WHERE id_asignacion = ?
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
                idAsignacion
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible eliminar la asignación. "
                            + "Puede que ya haya sido eliminada."
            );
        }
    }
}
private static void validarDatosEdicion(
        int cantidad,
        LocalDate fechaIngreso,
        LocalDate fechaSalida
) throws Exception {

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
    private static void validarDisponibilidadMaquinaria(
        int idAsignacionExcluir,
        int idMaquinaria,
        LocalDate fechaIngresoNueva,
        LocalDate fechaSalidaNueva
) throws Exception {

    if (fechaSalidaNueva == null) {
        fechaSalidaNueva = LocalDate.of(9999, 12, 31);
    }

    String sql = """
        SELECT
            CONCAT(
                p.codigo_proyecto,
                ' - ',
                p.descripcion
            ) AS proyecto,

            pm.fecha_ingreso,
            pm.fecha_salida

        FROM proyecto_maquinaria pm

        INNER JOIN proyectos p
            ON p.id_proyecto = pm.id_proyecto

        WHERE pm.activo = 1
          AND pm.estado <> 'CANCELADA'
          AND pm.id_maquinaria = ?
          AND pm.id_asignacion <> ?

          AND

          (
                ? <= COALESCE(
                        pm.fecha_salida,
                        '9999-12-31'
                    )

            AND

                ? >= pm.fecha_ingreso
          )

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
                idAsignacionExcluir
        );

        ps.setDate(
                3,
                Date.valueOf(fechaIngresoNueva)
        );

        ps.setDate(
                4,
                Date.valueOf(fechaSalidaNueva)
        );

        try (

                ResultSet rs =
                        ps.executeQuery()

        ) {

            if (rs.next()) {

                LocalDate fechaInicio =
                        rs.getDate(
                                "fecha_ingreso"
                        ).toLocalDate();

                Date fechaFinSql =
                        rs.getDate(
                                "fecha_salida"
                        );

                String fechaFin;

                if (fechaFinSql == null) {

                    fechaFin =
                            "SIN FECHA";

                } else {

                    fechaFin =
                            fechaFinSql
                                    .toLocalDate()
                                    .toString();

                }

                throw new Exception(

                        "No se puede realizar la asignación.\n\n"

                        + "La maquinaria ya se encuentra asignada.\n\n"

                        + "Proyecto: "
                        + rs.getString("proyecto")

                        + "\n\n"

                        + "Fecha inicio: "
                        + fechaInicio

                        + "\n"

                        + "Fecha final: "
                        + fechaFin

                        + "\n\n"

                        + "Revise las fechas antes de continuar."

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