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
        Double metrosLinealesContratados,
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

    return ProyectoAPI.obtenerResumen();
}

public static ProyectoDetalle obtenerPorId(
        int idProyecto
) throws Exception {

    return ProyectoAPI.obtenerPorId(
            idProyecto
    );
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
        Double metrosLinealesContratados,
        Double precioUnitario,
        String estado,
        String observaciones
) throws Exception {

    ProyectoAPI.actualizarProyecto(
            idProyecto,
            codigoProyecto,
            descripcion,
            idEmpresa,
            idSector,
            idPiscina,
            ordenCompra,
            idTipoActividad,
            fechaInicio,
            fechaFinEstimada,
            fechaFinReal,
            diasEstimados,
            areaM2,
            espesor,
            factorCompactacion,
            cantidadContratada,
            metrosLinealesContratados,
            precioUnitario,
            estado,
            observaciones
    );
}

public static void eliminar(
        int idProyecto
) throws Exception {

    ProyectoAPI.eliminarProyecto(
            idProyecto
    );
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
        Double metrosLinealesContratados,
        Double precioUnitario,
        String estado,
        String observaciones
) throws Exception {

    return ProyectoAPI.crearProyecto(
            codigoProyecto,
            descripcion,
            idEmpresa,
            idSector,
            idPiscina,
            ordenCompra,
            idTipoActividad,
            fechaInicio,
            fechaFinEstimada,
            fechaFinReal,
            diasEstimados,
            areaM2,
            espesor,
            factorCompactacion,
            cantidadContratada,
            metrosLinealesContratados,
            precioUnitario,
            estado,
            observaciones
    );
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