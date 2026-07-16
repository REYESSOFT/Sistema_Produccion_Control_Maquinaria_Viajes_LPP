import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PiscinaProyectoDAO {

    public record PiscinaResumen(
            int idPiscina,
            int idSector,
            String nombreSector,
            String nombrePiscina,
            String descripcion
    ) {
    }

    public static List<PiscinaResumen>
            obtenerActivas() throws Exception {

        String sql = """
                SELECT
                    p.id_piscina,
                    p.id_sector,
                    s.nombre_sector,
                    p.nombre_piscina,
                    COALESCE(p.descripcion, '') AS descripcion
                FROM piscinas p
                INNER JOIN sectores_proyecto s
                    ON s.id_sector = p.id_sector
                WHERE p.activo = 1
                  AND s.activo = 1
                ORDER BY
                    s.nombre_sector,
                    p.nombre_piscina
                """;

        List<PiscinaResumen> piscinas =
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

                piscinas.add(
                        new PiscinaResumen(
                                rs.getInt("id_piscina"),
                                rs.getInt("id_sector"),
                                rs.getString("nombre_sector"),
                                rs.getString("nombre_piscina"),
                                rs.getString("descripcion")
                        )
                );
            }
        }

        return piscinas;
    }

    public static PiscinaResumen obtenerPorId(
            int idPiscina
    ) throws Exception {

        String sql = """
                SELECT
                    p.id_piscina,
                    p.id_sector,
                    s.nombre_sector,
                    p.nombre_piscina,
                    COALESCE(p.descripcion, '') AS descripcion
                FROM piscinas p
                INNER JOIN sectores_proyecto s
                    ON s.id_sector = p.id_sector
                WHERE p.id_piscina = ?
                  AND p.activo = 1
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
                    idPiscina
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró la piscina."
                    );
                }

                return new PiscinaResumen(
                        rs.getInt("id_piscina"),
                        rs.getInt("id_sector"),
                        rs.getString("nombre_sector"),
                        rs.getString("nombre_piscina"),
                        rs.getString("descripcion")
                );
            }
        }
    }

    public static int insertar(
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        validarDatos(
                idSector,
                nombrePiscina
        );

        validarDuplicado(
                idSector,
                nombrePiscina,
                null
        );

        String sql = """
                INSERT INTO piscinas (
                    id_sector,
                    nombre_piscina,
                    descripcion,
                    activo
                )
                VALUES (?, ?, ?, 1)
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
                    idSector
            );

            ps.setString(
                    2,
                    nombrePiscina.trim()
            );

            asignarTexto(
                    ps,
                    3,
                    descripcion
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar la piscina."
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
                "La piscina fue guardada, "
                        + "pero no se pudo obtener su ID."
        );
    }

    public static void actualizar(
            int idPiscina,
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        validarDatos(
                idSector,
                nombrePiscina
        );

        validarDuplicado(
                idSector,
                nombrePiscina,
                idPiscina
        );

        String sql = """
                UPDATE piscinas
                SET
                    id_sector = ?,
                    nombre_piscina = ?,
                    descripcion = ?
                WHERE id_piscina = ?
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

            ps.setString(
                    2,
                    nombrePiscina.trim()
            );

            asignarTexto(
                    ps,
                    3,
                    descripcion
            );

            ps.setInt(
                    4,
                    idPiscina
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar la piscina."
                );
            }
        }
    }

    public static void eliminar(
            int idPiscina
    ) throws Exception {

        String sql = """
                UPDATE piscinas
                SET activo = 0
                WHERE id_piscina = ?
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
                    idPiscina
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "La piscina no existe "
                                + "o ya fue eliminada."
                );
            }
        }
    }

    private static void validarDatos(
            int idSector,
            String nombrePiscina
    ) throws Exception {

        if (idSector <= 0) {

            throw new Exception(
                    "Debe seleccionar un sector."
            );
        }

        if (
            nombrePiscina == null
            || nombrePiscina.isBlank()
        ) {

            throw new Exception(
                    "El nombre de la piscina es obligatorio."
            );
        }
    }

    private static void validarDuplicado(
            int idSector,
            String nombrePiscina,
            Integer idPiscinaExcluir
    ) throws Exception {

        String sql = """
                SELECT id_piscina
                FROM piscinas
                WHERE activo = 1
                  AND id_sector = ?
                  AND UPPER(TRIM(nombre_piscina)) =
                      UPPER(TRIM(?))
                  AND (
                        ? IS NULL
                        OR id_piscina <> ?
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
                    idSector
            );

            ps.setString(
                    2,
                    nombrePiscina.trim()
            );

            if (idPiscinaExcluir == null) {

                ps.setNull(
                        3,
                        Types.INTEGER
                );

                ps.setNull(
                        4,
                        Types.INTEGER
                );

            } else {

                ps.setInt(
                        3,
                        idPiscinaExcluir
                );

                ps.setInt(
                        4,
                        idPiscinaExcluir
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe una piscina activa "
                                    + "con ese nombre en el sector seleccionado."
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
