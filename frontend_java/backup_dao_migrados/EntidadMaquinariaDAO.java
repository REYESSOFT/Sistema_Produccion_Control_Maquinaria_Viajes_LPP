import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EntidadMaquinariaDAO {

    public record EntidadResumen(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String estado
    ) {
    }

    public record EntidadDetalle(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String observaciones,
            String estado
    ) {
    }

    public static List<EntidadResumen>
            obtenerTodas() throws Exception {

        String sql = """
                SELECT
                    id_entidad,
                    nombre,
                    tipo_entidad,
                    COALESCE(identificacion, '') AS identificacion,
                    COALESCE(telefono, '') AS telefono,
                    COALESCE(correo, '') AS correo,
                    estado
                FROM entidades_maquinaria
                ORDER BY
                    estado,
                    nombre
                """;

        List<EntidadResumen> entidades =
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

                entidades.add(
                        new EntidadResumen(
                                rs.getInt("id_entidad"),
                                rs.getString("nombre"),
                                rs.getString("tipo_entidad"),
                                rs.getString("identificacion"),
                                rs.getString("telefono"),
                                rs.getString("correo"),
                                rs.getString("estado")
                        )
                );
            }
        }

        return entidades;
    }

    public static EntidadDetalle obtenerPorId(
            int idEntidad
    ) throws Exception {

        String sql = """
                SELECT
                    id_entidad,
                    nombre,
                    tipo_entidad,
                    COALESCE(identificacion, '') AS identificacion,
                    COALESCE(telefono, '') AS telefono,
                    COALESCE(correo, '') AS correo,
                    COALESCE(observaciones, '') AS observaciones,
                    estado
                FROM entidades_maquinaria
                WHERE id_entidad = ?
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
                    idEntidad
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró la entidad seleccionada."
                    );
                }

                return new EntidadDetalle(
                        rs.getInt("id_entidad"),
                        rs.getString("nombre"),
                        rs.getString("tipo_entidad"),
                        rs.getString("identificacion"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("observaciones"),
                        rs.getString("estado")
                );
            }
        }
    }

    public static int insertar(
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String observaciones
    ) throws Exception {

        validarDatos(
                nombre,
                tipoEntidad,
                correo
        );

        validarNombreDuplicado(
                nombre,
                null
        );

        String sql = """
                INSERT INTO entidades_maquinaria (
                    nombre,
                    tipo_entidad,
                    identificacion,
                    telefono,
                    correo,
                    observaciones,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?, 'ACTIVO')
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            ps.setString(
                    2,
                    tipoEntidad
            );

            asignarTexto(
                    ps,
                    3,
                    identificacion
            );

            asignarTexto(
                    ps,
                    4,
                    telefono
            );

            asignarTexto(
                    ps,
                    5,
                    correo
            );

            asignarTexto(
                    ps,
                    6,
                    observaciones
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar la entidad."
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
                "La entidad fue guardada, "
                        + "pero no se pudo obtener su ID."
        );
    }

    public static void actualizar(
            int idEntidad,
            String nombre,
            String tipoEntidad,
            String identificacion,
            String telefono,
            String correo,
            String observaciones,
            String estado
    ) throws Exception {

        if (idEntidad <= 0) {

            throw new Exception(
                    "La entidad seleccionada no es válida."
            );
        }

        validarDatos(
                nombre,
                tipoEntidad,
                correo
        );

        validarEstado(
                estado
        );

        validarNombreDuplicado(
                nombre,
                idEntidad
        );

        String sql = """
                UPDATE entidades_maquinaria
                SET
                    nombre = ?,
                    tipo_entidad = ?,
                    identificacion = ?,
                    telefono = ?,
                    correo = ?,
                    observaciones = ?,
                    estado = ?
                WHERE id_entidad = ?
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre.trim()
            );

            ps.setString(
                    2,
                    tipoEntidad
            );

            asignarTexto(
                    ps,
                    3,
                    identificacion
            );

            asignarTexto(
                    ps,
                    4,
                    telefono
            );

            asignarTexto(
                    ps,
                    5,
                    correo
            );

            asignarTexto(
                    ps,
                    6,
                    observaciones
            );

            ps.setString(
                    7,
                    estado
            );

            ps.setInt(
                    8,
                    idEntidad
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar la entidad."
                );
            }
        }
    }

    public static void eliminarLogico(
            int idEntidad
    ) throws Exception {

        if (idEntidad <= 0) {

            throw new Exception(
                    "La entidad seleccionada no es válida."
            );
        }

        validarEntidadSinUso(
                idEntidad
        );

        String sql = """
                UPDATE entidades_maquinaria
                SET estado = 'INACTIVO'
                WHERE id_entidad = ?
                  AND estado = 'ACTIVO'
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idEntidad
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible eliminar la entidad. "
                                + "Puede que ya esté inactiva."
                );
            }
        }
    }

    private static void validarDatos(
            String nombre,
            String tipoEntidad,
            String correo
    ) throws Exception {

        if (
            nombre == null
            || nombre.isBlank()
        ) {

            throw new Exception(
                    "El nombre es obligatorio."
            );
        }

        if (
            nombre.trim().length() > 150
        ) {

            throw new Exception(
                    "El nombre no puede superar "
                            + "los 150 caracteres."
            );
        }

        if (
            tipoEntidad == null
            || (
                !tipoEntidad.equals("EMPRESA")
                && !tipoEntidad.equals("PERSONA")
                && !tipoEntidad.equals("OTRO")
            )
        ) {

            throw new Exception(
                    "El tipo de entidad no es válido."
            );
        }

        if (
            correo != null
            && !correo.isBlank()
            && !correo.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            )
        ) {

            throw new Exception(
                    "El correo electrónico no tiene "
                            + "un formato válido."
            );
        }
    }

    private static void validarEstado(
            String estado
    ) throws Exception {

        if (
            estado == null
            || (
                !estado.equals("ACTIVO")
                && !estado.equals("INACTIVO")
            )
        ) {

            throw new Exception(
                    "El estado seleccionado no es válido."
            );
        }
    }

    private static void validarNombreDuplicado(
            String nombre,
            Integer idExcluir
    ) throws Exception {

        String sql = """
                SELECT id_entidad
                FROM entidades_maquinaria
                WHERE UPPER(TRIM(nombre)) =
                      UPPER(TRIM(?))
                """;

        if (idExcluir != null) {

            sql += """
                    
                    AND id_entidad <> ?
                    """;
        }

        sql += """
                
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
                    nombre.trim()
            );

            if (idExcluir != null) {

                ps.setInt(
                        2,
                        idExcluir
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe una entidad registrada "
                                    + "con ese nombre."
                    );
                }
            }
        }
    }

    private static void validarEntidadSinUso(
            int idEntidad
    ) throws Exception {

        String sql = """
                SELECT id_maquinaria
                FROM maquinaria
                WHERE activo = 1
                  AND (
                      id_propietario = ?
                      OR id_proveedor = ?
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
                    idEntidad
            );

            ps.setInt(
                    2,
                    idEntidad
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "No se puede eliminar la entidad porque "
                                    + "está relacionada con una maquinaria activa."
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
