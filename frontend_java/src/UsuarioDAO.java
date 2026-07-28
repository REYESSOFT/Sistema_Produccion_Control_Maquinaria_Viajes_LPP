import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario autenticar(String usuario, String clave) {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombre_usuario,
                    u.nombre_completo,
                    u.rol,
                    u.id_empresa,
                    e.nombre_empresa
                FROM usuarios u
                LEFT JOIN empresas e
                    ON e.id_empresa = u.id_empresa
                WHERE u.nombre_usuario = ?
                  AND u.clave = ?
                  AND u.activo = 1
                LIMIT 1
                """;

        try (
                Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Usuario(

                            rs.getInt("id_usuario"),

                            rs.getString("nombre_usuario"),

                            rs.getString("nombre_completo"),

                            rs.getString("rol"),

                            (Integer) rs.getObject("id_empresa"),

                            rs.getString("nombre_empresa")
                    );
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

}