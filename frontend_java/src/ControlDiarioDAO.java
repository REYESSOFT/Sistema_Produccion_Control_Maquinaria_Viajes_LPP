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

    String sql = """
            SELECT
                c.id_control,

                CONCAT(
                    p.codigo_proyecto,
                    ' - ',
                    p.descripcion
                ) AS proyecto,

                c.fecha_control,
                c.metros_lineales

            FROM control_diario c

            INNER JOIN proyectos p
                ON p.id_proyecto = c.id_proyecto

            WHERE c.activo = 1

            ORDER BY
                c.fecha_control DESC,
                c.id_control DESC
            """;

    List<ControlDiarioResumen> controles =
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
                    rs.getDate("fecha_control");

            LocalDate fecha =
                    fechaSql == null
                            ? null
                            : fechaSql.toLocalDate();

            controles.add(
                    new ControlDiarioResumen(
                            rs.getInt("id_control"),
                            rs.getString("proyecto"),
                            fecha,
                            rs.getDouble("metros_lineales")
                    )
            );
        }
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

    String sql = """
            SELECT
                id_control,
                id_proyecto,
                fecha_control,
                metros_lineales,
                ancho,
                espesor,
                volumen_real,
                COALESCE(
                    observaciones,
                    ''
                ) AS observaciones

            FROM control_diario

            WHERE id_control = ?

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
                idControl
        );

        try (

                ResultSet rs =
                        ps.executeQuery()

        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró el Control Diario."
                );

            }

            Date fechaSql =
                    rs.getDate(
                            "fecha_control"
                    );

            return new ControlDiarioDetalle(

                    rs.getInt(
                            "id_control"
                    ),

                    rs.getInt(
                            "id_proyecto"
                    ),

                    fechaSql == null
                            ? null
                            : fechaSql.toLocalDate(),

                    rs.getDouble(
                            "metros_lineales"
                    ),

                    rs.getObject("ancho") == null
                            ? null
                            : rs.getDouble("ancho"),

                    rs.getObject("espesor") == null
        ? null
        : rs.getDouble("espesor"),

rs.getObject("volumen_real") == null
        ? null
        : rs.getDouble("volumen_real"),

rs.getString(
        "observaciones"
)

            );

        }

    }

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

    validarDatos(
            idProyecto,
            fechaControl,
            metrosLineales,
            ancho,
            espesor
    );

    validarControlDuplicado(
            idProyecto,
            fechaControl,
            0
    );

    validarMaquinariaEnOtroProyecto(
            idProyecto,
            fechaControl
    );
    double volumenReal = 0;

if (
        ancho != null
        && espesor != null
) {

    volumenReal =
            metrosLineales
            * ancho
            * espesor;

}

    String sql = """
            INSERT INTO control_diario (
    id_guia,
    id_proyecto,
    fecha_control,
    metros_lineales,
    ancho,
    espesor,
    volumen_real,
    observaciones,
    activo
)
VALUES (
    ?, ?, ?, ?, ?, ?, ?, ?, 1
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

        if (idGuia == null) {

    ps.setNull(
            1,
            java.sql.Types.INTEGER
    );

} else {

    ps.setInt(
            1,
            idGuia
    );
}

ps.setInt(
        2,
        idProyecto
);

ps.setDate(
        3,
        Date.valueOf(fechaControl)
);

ps.setDouble(
        4,
        metrosLineales
);

asignarDecimal(
        ps,
        5,
        ancho
);

asignarDecimal(
        ps,
        6,
        espesor
);

ps.setDouble(
        7,
        volumenReal
);

asignarTexto(
        ps,
        8,
        observaciones
);

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible guardar el Control Diario."
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
            "El Control Diario fue guardado, "
                    + "pero no se pudo obtener su ID."
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

    validarDatos(
            idProyecto,
            fechaControl,
            metrosLineales,
            ancho,
            espesor
    );

    validarControlDuplicado(
            idProyecto,
            fechaControl,
            idControl
    );

    validarMaquinariaEnOtroProyecto(
            idProyecto,
            fechaControl
    );
    double volumenReal = 0;

if (
        ancho != null
        && espesor != null
) {

    volumenReal =
            metrosLineales
            * ancho
            * espesor;

}

    String sql = """
            UPDATE control_diario
            SET

                id_proyecto = ?,

                fecha_control = ?,

                metros_lineales = ?,

                ancho = ?,

                espesor = ?,
                
                volumen_real = ?,

                observaciones = ?

            WHERE id_control = ?

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
                idProyecto
        );

        ps.setDate(
                2,
                Date.valueOf(fechaControl)
        );

        ps.setDouble(
                3,
                metrosLineales
        );

        asignarDecimal(
                ps,
                4,
                ancho
        );

       asignarDecimal(
        ps,
        5,
        espesor
);

ps.setDouble(
        6,
        volumenReal
);

asignarTexto(
        ps,
        7,
        observaciones
);

        ps.setInt(
                8,
                idControl
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar el Control Diario."
            );

        }

    }

}
public static void eliminar(
        int idControl
) throws Exception {

    if (idControl <= 0) {

        throw new Exception(
                "El Control Diario seleccionado no es válido."
        );
    }

    String sql = """
            UPDATE control_diario
            SET activo = 0
            WHERE id_control = ?
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
                idControl
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible eliminar el Control Diario. "
                            + "Puede que ya haya sido eliminado."
            );
        }
    }
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