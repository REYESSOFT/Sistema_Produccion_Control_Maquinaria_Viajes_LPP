import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CostosRentabilidadDAO {

    public record CostosRentabilidadResumen(
            int idProyecto,
            String codigoProyecto,
            String descripcionProyecto,
            String empresa,
            double metrosContratados,
            double metrosEjecutados,
            double porcentajeAvance,
            double ingreso,
            double costo,
            double utilidad
    ) {
    }

    public static List<CostosRentabilidadResumen>
            obtenerResumen() throws Exception {

        List<CostosRentabilidadResumen> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.id_proyecto,
                    p.codigo_proyecto,
                    p.descripcion,
                    e.nombre_empresa AS empresa,

                    COALESCE(
                        p.metros_lineales_contratados,
                        0
                    ) AS metros_contratados,

                    COALESCE(
                        SUM(cd.metros_lineales),
                        0
                    ) AS metros_ejecutados

                FROM proyectos p

                INNER JOIN empresas e
                    ON e.id_empresa =
                       p.id_empresa

                LEFT JOIN control_diario cd
                    ON cd.id_proyecto =
                       p.id_proyecto

                   AND cd.activo = 1

                WHERE p.activo = 1

                GROUP BY
                    p.id_proyecto,
                    p.codigo_proyecto,
                    p.descripcion,
                    e.nombre_empresa,
                    p.metros_lineales_contratados

                ORDER BY
                    p.codigo_proyecto,
                    p.descripcion
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                double metrosContratados =
                        rs.getDouble(
                                "metros_contratados"
                        );

                double metrosEjecutados =
                        rs.getDouble(
                                "metros_ejecutados"
                        );

                double porcentajeAvance =
                        calcularPorcentaje(
                                metrosEjecutados,
                                metrosContratados
                        );

                /*
                 * En esta primera etapa todavía no se
                 * calculan ingresos, costos ni utilidad.
                 *
                 * Estos valores permanecerán en cero
                 * hasta implementar las siguientes fases.
                 */
                double ingreso =
                        0.00;

                double costo =
                        0.00;

                double utilidad =
                        ingreso - costo;

                lista.add(
                        new CostosRentabilidadResumen(
                                rs.getInt(
                                        "id_proyecto"
                                ),

                                rs.getString(
                                        "codigo_proyecto"
                                ),

                                rs.getString(
                                        "descripcion"
                                ),

                                rs.getString(
                                        "empresa"
                                ),

                                metrosContratados,

                                metrosEjecutados,

                                porcentajeAvance,

                                ingreso,

                                costo,

                                utilidad
                        )
                );
            }
        }

        return lista;
    }

    private static double calcularPorcentaje(
            double ejecutado,
            double contratado
    ) {

        if (contratado <= 0) {

            return 0.00;
        }

        return ejecutado
                * 100.00
                / contratado;
    }
}
