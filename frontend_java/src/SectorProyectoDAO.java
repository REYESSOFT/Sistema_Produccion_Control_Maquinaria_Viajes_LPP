import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SectorProyectoDAO {

    public record SectorResumen(
            int idSector,
            String nombreSector,
            String descripcion
    ) {
    }

    public static List<SectorResumen>
            obtenerActivos() throws Exception {

        String sql = """
                SELECT
                    id_sector,
                    nombre_sector,
                    COALESCE(descripcion, '') AS descripcion
                FROM sectores_proyecto
                WHERE activo = 1
                ORDER BY nombre_sector
                """;

        List<SectorResumen> sectores =
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
                        new SectorResumen(
                                rs.getInt("id_sector"),
                                rs.getString("nombre_sector"),
                                rs.getString("descripcion")
                        )
                );
            }
        }

        return sectores;
    }

    public static int insertar(
            String nombreSector,
            String descripcion
    ) throws Exception {

        if (
            nombreSector == null
            || nombreSector.isBlank()
        ) {

            throw new Exception(
                    "El nombre del sector es obligatorio."
            );
        }

        validarDuplicado(
                nombreSector,
                null
        );

        String sql = """
                INSERT INTO sectores_proyecto (
                    nombre_sector,
                    descripcion,
                    activo
                )
                VALUES (
                    ?, ?, 1
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
                    nombreSector.trim()
            );

            if (
                descripcion == null
                || descripcion.isBlank()
            ) {

                ps.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                ps.setString(
                        2,
                        descripcion.trim()
                );
            }

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar el sector."
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
                "El sector fue guardado, "
                        + "pero no se pudo obtener su ID."
        );
    }

    public static SectorResumen obtenerPorId(
            int idSector
    ) throws Exception {

        String sql = """
                SELECT
                    id_sector,
                    nombre_sector,
                    COALESCE(descripcion, '') AS descripcion
                FROM sectores_proyecto
                WHERE id_sector = ?
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
                    idSector
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró el sector."
                    );
                }

                return new SectorResumen(
                        rs.getInt("id_sector"),
                        rs.getString("nombre_sector"),
                        rs.getString("descripcion")
                );
            }
        }
    }

    public static void actualizar(
            int idSector,
            String nombreSector,
            String descripcion
    ) throws Exception {

        if (
            nombreSector == null
            || nombreSector.isBlank()
        ) {

            throw new Exception(
                    "El nombre del sector es obligatorio."
            );
        }

        validarDuplicado(
                nombreSector,
                idSector
        );

        String sql = """
                UPDATE sectores_proyecto
                SET
                    nombre_sector = ?,
                    descripcion = ?
                WHERE id_sector = ?
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
                    nombreSector.trim()
            );

            if (
                descripcion == null
                || descripcion.isBlank()
            ) {

                ps.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                ps.setString(
                        2,
                        descripcion.trim()
                );
            }

            ps.setInt(
                    3,
                    idSector
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar el sector."
                );
            }
        }
    }

    public static void eliminar(
            int idSector
    ) throws Exception {

        String sql = """
                UPDATE sectores_proyecto
                SET activo = 0
                WHERE id_sector = ?
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
                    idSector
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "El sector no existe "
                                + "o ya fue eliminado."
                );
            }
        }
    }

    private static void validarDuplicado(
            String nombreSector,
            Integer idSectorExcluir
    ) throws Exception {

        String sql = """
                SELECT id_sector
                FROM sectores_proyecto
                WHERE activo = 1
                  AND UPPER(TRIM(nombre_sector)) =
                      UPPER(TRIM(?))
                  AND (
                        ? IS NULL
                        OR id_sector <> ?
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
                    nombreSector.trim()
            );

            if (idSectorExcluir == null) {

                ps.setNull(
                        2,
                        java.sql.Types.INTEGER
                );

                ps.setNull(
                        3,
                        java.sql.Types.INTEGER
                );

            } else {

                ps.setInt(
                        2,
                        idSectorExcluir
                );

                ps.setInt(
                        3,
                        idSectorExcluir
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe un sector activo "
                                    + "con ese nombre."
                    );
                }
            }
        }
    }
}
