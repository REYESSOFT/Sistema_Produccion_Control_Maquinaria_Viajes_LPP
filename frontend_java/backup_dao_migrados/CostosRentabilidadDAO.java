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

        double cantidadContratada,
        double volumenAcumulado,
        double costoAcumulado,
        double porcentajeAvanceFisico,
        double porcentajeAvanceContractual,

        double ingreso,
        double costoMaterial,
        double costoTransporte,
        double costoMaquinaria,
        double costoTotal,
        double costoPorMetroLineal,
        double utilidad,
        double utilidadPorMetroLineal,
        double rentabilidad
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
                        p.precio_unitario,
                        0
                    ) AS precio_unitario,
                    COALESCE(
                        p.cantidad_contratada,
                        0
                    ) AS cantidad_contratada,

                    COALESCE(
                        avance.metros_ejecutados,
                        0
                    ) AS metros_ejecutados,
                    COALESCE(
                        costos_material.volumen_acumulado,
                        0
                    ) AS volumen_acumulado,

                    COALESCE(
                        costos_material.costo_material,
                        0
                    ) AS costo_material,
                    
                    COALESCE(
                        costos_material.costo_transporte,
                        0
                    ) AS costo_transporte,

                    COALESCE(
                        costos_maquinaria.costo_maquinaria,
                        0
                    ) AS costo_maquinaria

                FROM proyectos p

                INNER JOIN empresas e
                    ON e.id_empresa = p.id_empresa

                LEFT JOIN (
                    SELECT
                        cd.id_proyecto,
                        SUM(
                            COALESCE(
                                cd.metros_lineales,
                                0
                            )
                        ) AS metros_ejecutados
                    FROM control_diario cd
                    WHERE cd.activo = 1
                    GROUP BY cd.id_proyecto
                ) avance
                    ON avance.id_proyecto = p.id_proyecto

                LEFT JOIN (
    SELECT
        cd.id_proyecto,

        SUM(
            COALESCE(
                cdm.volumen_recibido,
                0
            )
        ) AS volumen_acumulado,

        SUM(
            COALESCE(
                cdm.costo_material,
                0
            )
        ) AS costo_material,

        SUM(
            COALESCE(
                cdm.costo_transporte,
                0
            )
        ) AS costo_transporte

    FROM control_diario_material cdm

    INNER JOIN control_diario cd
        ON cd.id_control = cdm.id_control
       AND cd.activo = 1

    WHERE cdm.activo = 1

    GROUP BY cd.id_proyecto
) costos_material
    ON costos_material.id_proyecto = p.id_proyecto

                LEFT JOIN (
                    SELECT
                        cd.id_proyecto,
                        SUM(
                            CASE
                                WHEN UPPER(
                                    TRIM(
                                        COALESCE(
                                            m.tipo_cobro,
                                            'POR_HORA'
                                        )
                                    )
                                ) = 'FIJO_DIARIO'
                                THEN COALESCE(
                                    m.costo_fijo_proveedor,
                                    0
                                )

                                WHEN UPPER(
                                    TRIM(
                                        COALESCE(
                                            m.tipo_cobro,
                                            'POR_HORA'
                                        )
                                    )
                                ) = 'FIJO_SERVICIO'
                                THEN
                                    CASE
                                        WHEN NOT EXISTS (
                                            SELECT 1
                                            FROM control_diario_maquinaria
                                                 cdm_anterior
                                            INNER JOIN control_diario
                                                 cd_anterior
                                                ON cd_anterior.id_control =
                                                   cdm_anterior.id_control
                                               AND cd_anterior.activo = 1
                                            WHERE
                                                cdm_anterior.id_maquinaria =
                                                cdm.id_maquinaria
                                                AND cdm_anterior.activo = 1
                                                AND cd_anterior.id_proyecto =
                                                cd.id_proyecto
                                                AND (
                                                    cd_anterior.fecha_control <
                                                    cd.fecha_control
                                                    OR (
                                                        cd_anterior.fecha_control =
                                                        cd.fecha_control
                                                        AND
                                                        cdm_anterior
                                                            .id_control_maquinaria <
                                                        cdm.id_control_maquinaria
                                                    )
                                                )
                                        )
                                        THEN COALESCE(
                                            m.costo_fijo_proveedor,
                                            0
                                        )
                                        ELSE 0
                                    END

                                ELSE
                                    COALESCE(
                                        cdm.horas_trabajadas,
                                        0
                                    )
                                    *
                                    COALESCE(
                                        m.costo_hora_proveedor,
                                        0
                                    )
                            END
                        ) AS costo_maquinaria
                    FROM control_diario_maquinaria cdm
                    INNER JOIN control_diario cd
                        ON cd.id_control = cdm.id_control
                       AND cd.activo = 1
                    INNER JOIN maquinaria m
                        ON m.id_maquinaria = cdm.id_maquinaria
                       AND m.activo = 1
                    WHERE cdm.activo = 1
                    GROUP BY cd.id_proyecto
                ) costos_maquinaria
                    ON costos_maquinaria.id_proyecto = p.id_proyecto

                WHERE p.activo = 1

                ORDER BY
                    p.codigo_proyecto,
                    p.descripcion
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                double metrosContratados =
                        rs.getDouble("metros_contratados");

                double metrosEjecutados =
                        rs.getDouble("metros_ejecutados");

                double precioUnitario =
                        rs.getDouble("precio_unitario");
                double cantidadContratada =
                        rs.getDouble("cantidad_contratada");

                double volumenAcumulado =
                        rs.getDouble("volumen_acumulado");

                double costoMaterial =
                        rs.getDouble("costo_material");

                double costoTransporte =
                        rs.getDouble("costo_transporte");

                double costoMaquinaria =
                        rs.getDouble("costo_maquinaria");

                double porcentajeAvance =
                        calcularPorcentaje(
                                metrosEjecutados,
                                metrosContratados
                        );

                double ingreso =
                        metrosEjecutados
                        * precioUnitario;
                double costoAcumulado =
        costoMaterial
        + costoTransporte;

double porcentajeAvanceFisico =
        calcularPorcentaje(
                volumenAcumulado,
                cantidadContratada
        );

double valorTotalContratado =
        metrosContratados
        * precioUnitario;

double porcentajeAvanceContractual =
        calcularPorcentaje(
                costoAcumulado,
                valorTotalContratado
        );

                double costoTotal =
                        costoMaterial
                        + costoTransporte
                        + costoMaquinaria;

                double costoPorMetroLineal =
                        calcularValorPorMetro(
                                costoTotal,
                                metrosEjecutados
                        );

                double utilidad =
                        ingreso
                        - costoTotal;

                double utilidadPorMetroLineal =
                        calcularValorPorMetro(
                                utilidad,
                                metrosEjecutados
                        );

                double rentabilidad =
                        calcularPorcentaje(
                                utilidad,
                                ingreso
                        );

                lista.add(
                        new CostosRentabilidadResumen(
        rs.getInt("id_proyecto"),
        rs.getString("codigo_proyecto"),
        rs.getString("descripcion"),
        rs.getString("empresa"),

        metrosContratados,
        metrosEjecutados,
        porcentajeAvance,

        cantidadContratada,
        volumenAcumulado,
        costoAcumulado,
        porcentajeAvanceFisico,
        porcentajeAvanceContractual,

        ingreso,
        costoMaterial,
        costoTransporte,
        costoMaquinaria,
        costoTotal,
        costoPorMetroLineal,
        utilidad,
        utilidadPorMetroLineal,
        rentabilidad
)
                );
            }
        }

        return lista;
    }

    private static double calcularPorcentaje(
            double valor,
            double base
    ) {

        if (base <= 0) {
            return 0.00;
        }

        return valor
                * 100.00
                / base;
    }

    private static double calcularValorPorMetro(
            double valor,
            double metrosEjecutados
    ) {

        if (metrosEjecutados <= 0) {
            return 0.00;
        }

        return valor
                / metrosEjecutados;
    }
}
