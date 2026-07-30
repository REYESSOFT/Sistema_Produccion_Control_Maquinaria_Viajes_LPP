import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SelectorGuiasAprobadasDAO {

    public record GuiaAprobadaItem(
            int idGuia,
            LocalDate fecha,
            String empresa,
            String tipoGuia,
            String numeroGuia,
            String proyectoReferencia,
            String sector,
            String material,
            String choferOperador,
            String placa,
            double m3
    ) {
    }

    public static List<GuiaAprobadaItem>
            obtenerGuiasAprobadas() throws Exception {

        String sql = """
                SELECT
                    g.id_guia,
                    g.fecha,
                    e.nombre_empresa,
                    g.tipo_guia,
                    g.numero_guia,

                    COALESCE(
    NULLIF(TRIM(detalles.proyecto), ''),
    NULLIF(TRIM(g.destino), ''),
    NULLIF(TRIM(g.sector), ''),
    'SIN PROYECTO DEFINIDO'
) AS proyecto_referencia,

COALESCE(
    NULLIF(TRIM(detalles.sector), ''),
    NULLIF(TRIM(g.sector), ''),
    ''
) AS sector,

COALESCE(
    NULLIF(TRIM(detalles.material), ''),
    NULLIF(TRIM(g.material), ''),
    ''
) AS material,
                    COALESCE(g.chofer_operador, '') AS chofer_operador,
                    COALESCE(g.placa, '') AS placa,
                    COALESCE(g.m3, 0) AS m3

                FROM guias g

                INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa

                LEFT JOIN (
    SELECT
        id_guia,

        GROUP_CONCAT(
            DISTINCT NULLIF(TRIM(proyecto), '')
            ORDER BY TRIM(proyecto)
            SEPARATOR ', '
        ) AS proyecto,

        GROUP_CONCAT(
            DISTINCT NULLIF(TRIM(sector), '')
            ORDER BY TRIM(sector)
            SEPARATOR ', '
        ) AS sector,

        GROUP_CONCAT(
            DISTINCT NULLIF(TRIM(material), '')
            ORDER BY TRIM(material)
            SEPARATOR ', '
        ) AS material

    FROM guia_produccion_detalle

    GROUP BY id_guia
) detalles
    ON detalles.id_guia = g.id_guia

                WHERE g.estado = 'APROBADO'

AND NOT EXISTS (

    SELECT 1

    FROM control_diario cd

    WHERE cd.id_guia = g.id_guia

      AND cd.activo = 1
)

ORDER BY
    g.fecha DESC,
    g.id_guia DESC
                """;

        List<GuiaAprobadaItem> guias =
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

                Date fechaSql =
                        rs.getDate("fecha");

                LocalDate fecha =
                        fechaSql == null
                                ? null
                                : fechaSql.toLocalDate();

                guias.add(
                        new GuiaAprobadaItem(
                                rs.getInt("id_guia"),
                                fecha,
                                rs.getString("nombre_empresa"),
                                rs.getString("tipo_guia"),
                                rs.getString("numero_guia"),
                                rs.getString("proyecto_referencia"),
                                rs.getString("sector"),
                                rs.getString("material"),
                                rs.getString("chofer_operador"),
                                rs.getString("placa"),
                                rs.getDouble("m3")
                        )
                );
            }
        }

        return guias;
    }
}
