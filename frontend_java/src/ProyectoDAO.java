import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    public record EmpresaItem(
            int idEmpresa,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record SectorItem(
        int idSector,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}

public record PiscinaItem(
        int idPiscina,
        int idSector,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}

public record TipoActividadItem(
        int idTipoActividad,
        String nombre
) {

    @Override
    public String toString() {
        return nombre;
    }
}

    public record ProyectoResumen(
            int idProyecto,
            String codigoProyecto,
            String empresa,
            String descripcion,
            String sector,
            String piscina,
            LocalDate fechaInicio,
            LocalDate fechaFinEstimada,
            String estado
    ) {
    }
    public record ProyectoDetalle(
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
        Double precioUnitario,
        String estado,
        String observaciones
) {
}

    public static List<EmpresaItem>
            obtenerEmpresas() throws Exception {

        String sql = """
                SELECT
                    id_empresa,
                    nombre_empresa
                FROM empresas
                ORDER BY nombre_empresa
                """;

        List<EmpresaItem> empresas =
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

                empresas.add(
                        new EmpresaItem(
                                rs.getInt("id_empresa"),
                                rs.getString("nombre_empresa")
                        )
                );
            }
        }

        return empresas;
    }

    public static List<SectorItem>
        obtenerSectores() throws Exception {

    String sql = """
            SELECT
                id_sector,
                nombre_sector
            FROM sectores_proyecto
            WHERE activo = 1
            ORDER BY nombre_sector
            """;

    List<SectorItem> sectores =
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

            sectores.add(
                    new SectorItem(
                            rs.getInt("id_sector"),
                            rs.getString("nombre_sector")
                    )
            );
        }
    }

    return sectores;
}

public static List<PiscinaItem>
        obtenerPiscinasPorSector(
                Integer idSector
        ) throws Exception {

    String sql = """
            SELECT
                id_piscina,
                id_sector,
                nombre_piscina
            FROM piscinas
            WHERE activo = 1
              AND (
                    id_sector = ?
                    OR ? IS NULL
              )
            ORDER BY nombre_piscina
            """;

    List<PiscinaItem> piscinas =
            new ArrayList<>();

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        if (idSector == null) {

            ps.setNull(1, Types.INTEGER);
            ps.setNull(2, Types.INTEGER);

        } else {

            ps.setInt(1, idSector);
            ps.setInt(2, idSector);
        }

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                piscinas.add(
                        new PiscinaItem(
                                rs.getInt("id_piscina"),
                                rs.getInt("id_sector"),
                                rs.getString("nombre_piscina")
                        )
                );
            }
        }
    }

    return piscinas;
}

public static List<TipoActividadItem>
        obtenerTiposActividad() throws Exception {

    String sql = """
            SELECT
                id_tipo_actividad,
                nombre_actividad
            FROM tipos_actividad_proyecto
            WHERE activo = 1
            ORDER BY nombre_actividad
            """;

    List<TipoActividadItem> actividades =
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

            actividades.add(
                    new TipoActividadItem(
                            rs.getInt("id_tipo_actividad"),
                            rs.getString("nombre_actividad")
                    )
            );
        }
    }

    return actividades;
}

    public static List<ProyectoResumen>
        obtenerActivos() throws Exception {

    String sql = """
            SELECT
                p.id_proyecto,
                p.codigo_proyecto,
                e.nombre_empresa AS empresa,
                p.descripcion,
                COALESCE(s.nombre_sector, '') AS sector,
                COALESCE(pi.nombre_piscina, '') AS piscina,
                p.fecha_inicio,
                p.fecha_fin_estimada,
                p.estado

            FROM proyectos p

            INNER JOIN empresas e
                ON e.id_empresa = p.id_empresa

            LEFT JOIN sectores_proyecto s
                ON s.id_sector = p.id_sector

            LEFT JOIN piscinas pi
                ON pi.id_piscina = p.id_piscina

            WHERE p.activo = 1

            ORDER BY p.id_proyecto DESC
            """;

    List<ProyectoResumen> proyectos =
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

            Date fechaInicioSql =
                    rs.getDate("fecha_inicio");

            Date fechaFinSql =
                    rs.getDate("fecha_fin_estimada");

            proyectos.add(
                    new ProyectoResumen(
                            rs.getInt("id_proyecto"),
                            rs.getString("codigo_proyecto"),
                            rs.getString("empresa"),
                            rs.getString("descripcion"),
                            rs.getString("sector"),
                            rs.getString("piscina"),
                            fechaInicioSql == null
                                    ? null
                                    : fechaInicioSql.toLocalDate(),
                            fechaFinSql == null
                                    ? null
                                    : fechaFinSql.toLocalDate(),
                            rs.getString("estado")
                    )
            );
        }
    }

    return proyectos;
}

public static ProyectoDetalle obtenerPorId(
        int idProyecto
) throws Exception {

    String sql = """
            SELECT
                id_proyecto,
                codigo_proyecto,
                descripcion,
                id_empresa,
                id_sector,
                id_piscina,
                orden_compra,
                id_tipo_actividad,
                fecha_inicio,
                fecha_fin_estimada,
                fecha_fin_real,
                dias_estimados,
                area_m2,
                espesor,
                factor_compactacion,
                cantidad_contratada,
                precio_unitario,
                estado,
                observaciones
            FROM proyectos
            WHERE id_proyecto = ?
              AND activo = 1
            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(1, idProyecto);

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró el proyecto."
                );
            }

            return new ProyectoDetalle(
                    rs.getInt("id_proyecto"),
                    rs.getString("codigo_proyecto"),
                    rs.getString("descripcion"),
                    rs.getInt("id_empresa"),
                    obtenerEnteroNullable(
                            rs,
                            "id_sector"
                    ),
                    obtenerEnteroNullable(
                            rs,
                            "id_piscina"
                    ),
                    rs.getString("orden_compra"),
                    obtenerEnteroNullable(
                            rs,
                            "id_tipo_actividad"
                    ),
                    obtenerFecha(
                            rs,
                            "fecha_inicio"
                    ),
                    obtenerFecha(
                            rs,
                            "fecha_fin_estimada"
                    ),
                    obtenerFecha(
                            rs,
                            "fecha_fin_real"
                    ),
                    obtenerEnteroNullable(
                            rs,
                            "dias_estimados"
                    ),
                    obtenerDoubleNullable(
                            rs,
                            "area_m2"
                    ),
                    obtenerDoubleNullable(
                            rs,
                            "espesor"
                    ),
                    obtenerDoubleNullable(
                            rs,
                            "factor_compactacion"
                    ),
                    obtenerDoubleNullable(
                            rs,
                            "cantidad_contratada"
                    ),
                    obtenerDoubleNullable(
                            rs,
                            "precio_unitario"
                    ),
                    rs.getString("estado"),
                    rs.getString("observaciones")
            );
        }
    }
}

public static void actualizar(
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
        Double precioUnitario,
        String estado,
        String observaciones
) throws Exception {

    validarCodigoDuplicadoEdicion(
            idProyecto,
            codigoProyecto
    );

    String sql = """
            UPDATE proyectos
            SET
                codigo_proyecto = ?,
                descripcion = ?,
                id_empresa = ?,
                id_sector = ?,
                id_piscina = ?,
                orden_compra = ?,
                id_tipo_actividad = ?,
                fecha_inicio = ?,
                fecha_fin_estimada = ?,
                fecha_fin_real = ?,
                dias_estimados = ?,
                area_m2 = ?,
                espesor = ?,
                factor_compactacion = ?,
                cantidad_contratada = ?,
                precio_unitario = ?,
                estado = ?,
                observaciones = ?
            WHERE id_proyecto = ?
              AND activo = 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setString(1, codigoProyecto.trim());
        ps.setString(2, descripcion.trim());
        ps.setInt(3, idEmpresa);

        asignarEntero(ps, 4, idSector);
        asignarEntero(ps, 5, idPiscina);
        asignarTexto(ps, 6, ordenCompra);
        asignarEntero(ps, 7, idTipoActividad);
        asignarFecha(ps, 8, fechaInicio);
        asignarFecha(ps, 9, fechaFinEstimada);
        asignarFecha(ps, 10, fechaFinReal);
        asignarEntero(ps, 11, diasEstimados);
        asignarDecimal(ps, 12, areaM2);
        asignarDecimal(ps, 13, espesor);
        asignarDecimal(ps, 14, factorCompactacion);
        asignarDecimal(ps, 15, cantidadContratada);
        asignarDecimal(ps, 16, precioUnitario);

        ps.setString(17, estado);
        asignarTexto(ps, 18, observaciones);

        ps.setInt(19, idProyecto);

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar el proyecto."
            );
        }
    }
}

public static void eliminar(
        int idProyecto
) throws Exception {

    String sql = """
            UPDATE proyectos
            SET activo = 0
            WHERE id_proyecto = ?
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
                idProyecto
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "El proyecto no existe "
                            + "o ya fue eliminado."
            );
        }
    }
}

private static void validarCodigoDuplicadoEdicion(
        int idProyecto,
        String codigoProyecto
) throws Exception {

    String sql = """
            SELECT id_proyecto
            FROM proyectos
            WHERE activo = 1
              AND id_proyecto <> ?
              AND UPPER(TRIM(codigo_proyecto)) =
                  UPPER(TRIM(?))
            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(1, idProyecto);
        ps.setString(
                2,
                codigoProyecto.trim()
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                throw new Exception(
                        "Ya existe otro proyecto con ese código."
                );
            }
        }
    }
}

private static Integer obtenerEnteroNullable(
        ResultSet rs,
        String columna
) throws Exception {

    Object valor =
            rs.getObject(columna);

    return valor == null
            ? null
            : rs.getInt(columna);
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
private static LocalDate obtenerFecha(
        ResultSet rs,
        String columna
) throws Exception {

    Date fecha =
            rs.getDate(columna);

    return fecha == null
            ? null
            : fecha.toLocalDate();
}

    public static int insertar(
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
            Double precioUnitario,
            String estado,
            String observaciones
    ) throws Exception {

        if (
            codigoProyecto == null
            || codigoProyecto.isBlank()
        ) {

            throw new Exception(
                    "El código del proyecto es obligatorio."
            );
        }

        if (
            descripcion == null
            || descripcion.isBlank()
        ) {

            throw new Exception(
                    "La descripción del proyecto es obligatoria."
            );
        }

        validarCodigoDuplicado(
                codigoProyecto
        );

        String sql = """
                INSERT INTO proyectos (
                    codigo_proyecto,
                    descripcion,
                    id_empresa,
                    id_sector,
                    id_piscina,
                    orden_compra,
                    id_tipo_actividad,
                    fecha_inicio,
                    fecha_fin_estimada,
                    fecha_fin_real,
                    dias_estimados,
                    area_m2,
                    espesor,
                    factor_compactacion,
                    cantidad_contratada,
                    precio_unitario,
                    estado,
                    observaciones,
                    activo
                )
                VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?, 1
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

            ps.setString(
                    1,
                    codigoProyecto.trim()
            );

            ps.setString(
                    2,
                    descripcion.trim()
            );

            ps.setInt(
                    3,
                    idEmpresa
            );

            asignarEntero(
        ps,
        4,
        idSector
);

asignarEntero(
        ps,
        5,
        idPiscina
);

asignarTexto(
        ps,
        6,
        ordenCompra
);

asignarEntero(
        ps,
        7,
        idTipoActividad
);

            asignarFecha(
                    ps,
                    8,
                    fechaInicio
            );

            asignarFecha(
                    ps,
                    9,
                    fechaFinEstimada
            );

            asignarFecha(
                    ps,
                    10,
                    fechaFinReal
            );

            asignarEntero(
                    ps,
                    11,
                    diasEstimados
            );

            asignarDecimal(
                    ps,
                    12,
                    areaM2
            );

            asignarDecimal(
                    ps,
                    13,
                    espesor
            );

            asignarDecimal(
                    ps,
                    14,
                    factorCompactacion
            );

            asignarDecimal(
                    ps,
                    15,
                    cantidadContratada
            );

            asignarDecimal(
                    ps,
                    16,
                    precioUnitario
            );

            ps.setString(
                    17,
                    estado == null
                            || estado.isBlank()
                            ? "PLANIFICADO"
                            : estado
            );

            asignarTexto(
                    ps,
                    18,
                    observaciones
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar el proyecto."
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
                "El proyecto fue guardado, pero no se pudo obtener su ID."
        );
    }

    private static void validarCodigoDuplicado(
            String codigoProyecto
    ) throws Exception {

        String sql = """
                SELECT id_proyecto
                FROM proyectos
                WHERE activo = 1
                  AND UPPER(TRIM(codigo_proyecto)) =
                      UPPER(TRIM(?))
                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    codigoProyecto.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe un proyecto activo con ese código."
                    );
                }
            }
        }
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