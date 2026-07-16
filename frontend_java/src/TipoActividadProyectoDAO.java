import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TipoActividadProyectoDAO {

    public record TipoActividadResumen(
            int idTipoActividad,
            String nombreActividad,
            String descripcion
    ) {
    }

    public static List<TipoActividadResumen>
            obtenerActivos() throws Exception {

        String sql = """
                SELECT
                    id_tipo_actividad,
                    nombre_actividad,
                    COALESCE(descripcion, '') AS descripcion
                FROM tipos_actividad_proyecto
                WHERE activo = 1
                ORDER BY nombre_actividad
                """;

        List<TipoActividadResumen> actividades =
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
                        new TipoActividadResumen(
                                rs.getInt(
                                        "id_tipo_actividad"
                                ),
                                rs.getString(
                                        "nombre_actividad"
                                ),
                                rs.getString(
                                        "descripcion"
                                )
                        )
                );
            }
        }

        return actividades;
    }

    public static TipoActividadResumen obtenerPorId(
            int idTipoActividad
    ) throws Exception {

        String sql = """
                SELECT
                    id_tipo_actividad,
                    nombre_actividad,
                    COALESCE(descripcion, '') AS descripcion
                FROM tipos_actividad_proyecto
                WHERE id_tipo_actividad = ?
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
                    idTipoActividad
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró el tipo de actividad."
                    );
                }

                return new TipoActividadResumen(
                        rs.getInt(
                                "id_tipo_actividad"
                        ),
                        rs.getString(
                                "nombre_actividad"
                        ),
                        rs.getString(
                                "descripcion"
                        )
                );
            }
        }
    }

    public static int insertar(
            String nombreActividad,
            String descripcion
    ) throws Exception {

        validarNombre(
                nombreActividad
        );

        validarDuplicado(
                nombreActividad,
                null
        );

        String sql = """
                INSERT INTO tipos_actividad_proyecto (
                    nombre_actividad,
                    descripcion,
                    activo
                )
                VALUES (?, ?, 1)
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
                    nombreActividad.trim()
            );

            asignarTexto(
                    ps,
                    2,
                    descripcion
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar "
                                + "el tipo de actividad."
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
                "El tipo de actividad fue guardado, "
                        + "pero no se pudo obtener su ID."
        );
    }

    public static void actualizar(
            int idTipoActividad,
            String nombreActividad,
            String descripcion
    ) throws Exception {

        validarNombre(
                nombreActividad
        );

        validarDuplicado(
                nombreActividad,
                idTipoActividad
        );

        String sql = """
                UPDATE tipos_actividad_proyecto
                SET
                    nombre_actividad = ?,
                    descripcion = ?
                WHERE id_tipo_actividad = ?
                  AND activo = 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombreActividad.trim()
            );

            asignarTexto(
                    ps,
                    2,
                    descripcion
            );

            ps.setInt(
                    3,
                    idTipoActividad
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar "
                                + "el tipo de actividad."
                );
            }
        }
    }

    public static void eliminar(
        int idTipoActividad
) throws Exception {

    int proyectosRelacionados =
            contarProyectosRelacionados(
                    idTipoActividad
            );

    if (proyectosRelacionados > 0) {

        throw new Exception(
                "No se puede eliminar este tipo de actividad.\n\n"
                        + "Está asignado a "
                        + proyectosRelacionados
                        + (
                            proyectosRelacionados == 1
                                    ? " proyecto."
                                    : " proyectos."
                        )
                        + "\n\n"
                        + "Puede editar su descripción, "
                        + "pero debe conservarse para proteger el historial."
        );
    }

    String sql = """
            UPDATE tipos_actividad_proyecto
            SET activo = 0
            WHERE id_tipo_actividad = ?
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
                idTipoActividad
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "El tipo de actividad no existe "
                            + "o ya fue eliminado."
            );
        }
    }
}

public static int contarProyectosRelacionados(
        int idTipoActividad
) throws Exception {

    String sql = """
        SELECT COUNT(*) AS total
        FROM proyectos
        WHERE id_tipo_actividad = ?
        """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(
                1,
                idTipoActividad
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                return rs.getInt(
                        "total"
                );
            }
        }
    }

    return 0;
}

    private static void validarNombre(
            String nombreActividad
    ) throws Exception {

        if (
            nombreActividad == null
            || nombreActividad.isBlank()
        ) {

            throw new Exception(
                    "El nombre del tipo de actividad "
                            + "es obligatorio."
            );
        }
    }

    private static void validarDuplicado(
            String nombreActividad,
            Integer idTipoActividadExcluir
    ) throws Exception {

        String sql = """
                SELECT id_tipo_actividad
                FROM tipos_actividad_proyecto
                WHERE activo = 1
                  AND UPPER(TRIM(nombre_actividad)) =
                      UPPER(TRIM(?))
                  AND (
                        ? IS NULL
                        OR id_tipo_actividad <> ?
                  )
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
                    nombreActividad.trim()
            );

            if (idTipoActividadExcluir == null) {

                ps.setNull(
                        2,
                        Types.INTEGER
                );

                ps.setNull(
                        3,
                        Types.INTEGER
                );

            } else {

                ps.setInt(
                        2,
                        idTipoActividadExcluir
                );

                ps.setInt(
                        3,
                        idTipoActividadExcluir
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe un tipo de actividad "
                                    + "activo con ese nombre."
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
}
