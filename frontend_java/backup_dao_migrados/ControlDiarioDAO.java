import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class ControlDiarioDAO {


    public record ProyectoItem(
        int idProyecto,
        String codigoProyecto,
        String descripcion
) {

    @Override
    public String toString() {

        return codigoProyecto
                + " - "
                + descripcion;
    }
}

    public record ControlDiarioResumen(
            int idControl,
            String proyecto,
            LocalDate fecha,
            double metrosLineales
    ) {
    }

    public record ControlDiarioDetalle(
            int idControl,
            int idProyecto,
            LocalDate fecha,
            double metrosLineales,
            Double ancho,
            Double espesor,
            Double volumenReal,
            String observaciones
    ) {
    }
    public static List<ControlDiarioResumen>
    obtenerControles() throws Exception {

        List<ControlDiarioResumen> controles =
                new ArrayList<>();

        for (
                ControlDiarioAPI.ControlDiarioResumen item
                : ControlDiarioAPI.obtenerResumen()
        ) {

            controles.add(
                    new ControlDiarioResumen(
                            item.idControl(),
                            item.proyecto(),
                            item.fecha(),
                            item.metrosLineales()
                    )
            );
        }

        return controles;
    }
public static List<ProyectoItem>
        obtenerProyectosActivos() throws Exception {

    String sql = """
            SELECT
                id_proyecto,
                codigo_proyecto,
                descripcion

            FROM proyectos

            WHERE activo = 1

            ORDER BY
                codigo_proyecto,
                descripcion
            """;

    List<ProyectoItem> proyectos =
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

            proyectos.add(
                    new ProyectoItem(
                            rs.getInt("id_proyecto"),
                            rs.getString("codigo_proyecto"),
                            rs.getString("descripcion")
                    )
            );
        }
    }

    return proyectos;
}
public static ControlDiarioDetalle obtenerPorId(
        int idControl
) throws Exception {

    ControlDiarioAPI.ControlDiarioDetalle detalle =
            ControlDiarioAPI.obtenerPorId(
                    idControl
            );

    return new ControlDiarioDetalle(
            detalle.idControl(),
            detalle.idProyecto(),
            detalle.fecha(),
            detalle.metrosLineales(),
            detalle.ancho(),
            detalle.espesor(),
            detalle.volumenReal(),
            detalle.observaciones()
    );
}
    public static int insertar(
        Integer idGuia,
        int idProyecto,
        LocalDate fechaControl,
        double metrosLineales,
        Double ancho,
        Double espesor,
        String observaciones
) throws Exception {

    return ControlDiarioAPI.crearControlDiario(
            idGuia,
            idProyecto,
            fechaControl,
            metrosLineales,
            ancho,
            espesor,
            observaciones
    );
}
public static void actualizar(
        int idControl,
        int idProyecto,
        LocalDate fechaControl,
        double metrosLineales,
        Double ancho,
        Double espesor,
        String observaciones
) throws Exception {

    ControlDiarioAPI.actualizarControlDiario(
            idControl,
            idProyecto,
            fechaControl,
            metrosLineales,
            ancho,
            espesor,
            observaciones
    );
}
public static void eliminar(
        int idControl
) throws Exception {

    ControlDiarioAPI.eliminarControlDiario(
            idControl
    );
}
private static void validarDatos(
        int idProyecto,
        LocalDate fechaControl,
        double metrosLineales,
        Double ancho,
        Double espesor
) throws Exception {

    if (idProyecto <= 0) {

        throw new Exception(
                "Debe seleccionar un proyecto."
        );
    }

    if (fechaControl == null) {

        throw new Exception(
                "La fecha del Control Diario es obligatoria."
        );
    }

    if (metrosLineales < 0) {

        throw new Exception(
                "Los metros lineales no pueden ser negativos."
        );
    }

    if (
            ancho != null
            && ancho <= 0
    ) {

        throw new Exception(
                "El ancho debe ser mayor que cero."
        );
    }

    if (
            espesor != null
            && espesor <= 0
    ) {

        throw new Exception(
                "El espesor debe ser mayor que cero."
        );
    }
}
private static void validarControlDuplicado(
        int idProyecto,
        LocalDate fechaControl,
        int idControlExcluir
) throws Exception {

    String sql = """
            SELECT id_control
            FROM control_diario
            WHERE id_proyecto = ?
              AND fecha_control = ?
              AND activo = 1
              AND id_control <> ?
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
                idProyecto
        );

        ps.setDate(
                2,
                Date.valueOf(fechaControl)
        );

        ps.setInt(
                3,
                idControlExcluir
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                throw new Exception(
                        "Ya existe un Control Diario para "
                                + "el proyecto y la fecha seleccionados.\n\n"
                                + "Debe editar el registro existente."
                );
            }
        }
    }
}
private static void validarMaquinariaEnOtroProyecto(
        int idProyecto,
        LocalDate fechaControl
) throws Exception {

    /*
     * Primero obtenemos el código y la descripción
     * del proyecto seleccionado.
     */
    String sqlProyecto = """
            SELECT
                codigo_proyecto,
                descripcion
            FROM proyectos
            WHERE id_proyecto = ?
              AND activo = 1
            LIMIT 1
            """;

    String codigoProyecto;
    String descripcionProyecto;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(
                            sqlProyecto
                    )
    ) {

        ps.setInt(
                1,
                idProyecto
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró el proyecto seleccionado."
                );
            }

            codigoProyecto =
                    rs.getString(
                            "codigo_proyecto"
                    );

            descripcionProyecto =
                    rs.getString(
                            "descripcion"
                    );
        }
    }

    String proyectoCompleto =
            codigoProyecto
                    + " - "
                    + descripcionProyecto;

    /*
     * Después verificamos si una misma maquinaria
     * aparece en más de un proyecto durante la fecha.
     */
    String sql = """
            SELECT
                COALESCE(
                    NULLIF(TRIM(g.numero_maquina), ''),
                    NULLIF(TRIM(g.placa), ''),
                    NULLIF(TRIM(g.equipo), ''),
                    'SIN IDENTIFICACIÓN'
                ) AS maquinaria,

                GROUP_CONCAT(
                    DISTINCT TRIM(d.proyecto)
                    ORDER BY TRIM(d.proyecto)
                    SEPARATOR ', '
                ) AS proyectos

            FROM guias g

            INNER JOIN guia_produccion_detalle d
                ON d.id_guia = g.id_guia

            WHERE g.fecha = ?
              AND g.estado <> 'CANCELADO'

              AND COALESCE(
                    NULLIF(TRIM(g.numero_maquina), ''),
                    NULLIF(TRIM(g.placa), ''),
                    NULLIF(TRIM(g.equipo), '')
                  ) IS NOT NULL

              AND d.proyecto IS NOT NULL
              AND TRIM(d.proyecto) <> ''

            GROUP BY
                COALESCE(
                    NULLIF(TRIM(g.numero_maquina), ''),
                    NULLIF(TRIM(g.placa), ''),
                    NULLIF(TRIM(g.equipo), ''),
                    'SIN IDENTIFICACIÓN'
                )

            HAVING
                COUNT(
                    DISTINCT UPPER(
                        TRIM(d.proyecto)
                    )
                ) > 1

                AND

                SUM(
                    CASE
                        WHEN UPPER(TRIM(d.proyecto))
                             = UPPER(TRIM(?))

                          OR UPPER(TRIM(d.proyecto))
                             = UPPER(TRIM(?))

                          OR UPPER(TRIM(d.proyecto))
                             = UPPER(TRIM(?))

                        THEN 1
                        ELSE 0
                    END
                ) > 0

            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setDate(
                1,
                Date.valueOf(fechaControl)
        );

        ps.setString(
                2,
                codigoProyecto
        );

        ps.setString(
                3,
                descripcionProyecto
        );

        ps.setString(
                4,
                proyectoCompleto
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                throw new Exception(
                        "No se puede grabar el Control Diario.\n\n"
                                + "La maquinaria "
                                + rs.getString("maquinaria")
                                + " tiene registros de trabajo "
                                + "en más de un proyecto "
                                + "para la misma fecha.\n\n"
                                + "Proyectos registrados: "
                                + rs.getString("proyectos")
                                + "\n\n"
                                + "Revise la tabla de Asignación "
                                + "de Maquinarias antes de continuar."
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
                java.sql.Types.VARCHAR
        );

    } else {

        ps.setString(
                parametro,
                valor.trim()
        );
    }
}
private static void asignarDecimal(
        PreparedStatement ps,
        int parametro,
        Double valor
) throws Exception {

    if (valor == null) {

        ps.setNull(
                parametro,
                java.sql.Types.DECIMAL
        );

    } else {

        ps.setDouble(
                parametro,
                valor
        );
    }
}

}