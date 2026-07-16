import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            if (conexion != null) {
                System.out.println("CONEXIÓN EXITOSA A MYSQL");
                System.out.println("Base de datos: lpp_smart_erp");
            }

        } catch (Exception e) {
            System.out.println("ERROR DE CONEXIÓN:");
            e.printStackTrace();
        }
    }
}