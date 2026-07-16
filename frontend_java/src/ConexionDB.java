import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/lpp_smart_erp"
            + "?useSSL=false"
            + "&serverTimezone=America/Guayaquil"
            + "&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "FERNANDO";
    private static final String CLAVE = "RVsoft2026@";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}