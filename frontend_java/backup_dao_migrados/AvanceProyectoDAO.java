import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvanceProyectoDAO {

    public record AvanceProyectoResumen(
            int idProyecto,
            String codigoProyecto,
            String descripcion,
            String empresa,
            LocalDate fechaControl,
            LocalDate fechaInicio,
            Integer diasEstimados,
            double metrosLinealesContratados,
            double avanceMetrosLinealesDiario,
            double metrosLinealesAcumulados,
            double metrosLinealesRestantes,
            Double ancho,
            Double espesor,
            double volumenDiario,
            double volumenAcumulado,
            double horasTrabajadas,
            double metrosCubicosTransportados,
            int cantidadViajes,
            double porcentajeAvanceFisico
    ) {
    }

    public record DashboardResumen(
            int proyectosActivos,
            double metrosLinealesContratados,
            double metrosLinealesEjecutados,
            double volumenEjecutado,
            double porcentajeGeneral
    ) {
    }

    /*
     * Clase interna para almacenar la información
     * acumulada de Control Diario por proyecto.
     */
    private static class ControlDatos {

        private LocalDate fechaControl;
        private int idUltimoControl;

        private double avanceDiario;
        private double metrosAcumulados;

        private Double ancho;
        private Double espesor;

        private double volumenDiario;
        private double volumenAcumulado;
    }

    /*
     * Clase interna para almacenar la información
     * acumulada de las Guías por proyecto.
     */
    private static class GuiaDatos {

        private double horasTrabajadas;
        private double metrosCubicos;
        private int cantidadViajes;
    }

    public static List<AvanceProyectoResumen>
            obtenerAvances() throws Exception {

        List<AvanceProyectoResumen> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion()
        ) {

            /*
             * Primero obtenemos toda la información
             * de Control Diario agrupada por proyecto.
             */
            Map<Integer, ControlDatos> controles =
                    obtenerDatosControlDiario(
                            conexion
                    );

            /*
             * Después obtenemos las horas, m³ y viajes
             * registrados en las Guías.
             */
            Map<String, GuiaDatos> guias =
                    obtenerDatosGuias(
                            conexion
                    );

            /*
             * Finalmente consultamos los proyectos
             * y unimos la información en Java.
             */
            String sqlProyectos = """
                    SELECT
                        p.id_proyecto,
                        p.codigo_proyecto,
                        p.descripcion,
                        e.nombre_empresa AS empresa,
                        p.fecha_inicio,
                        p.dias_estimados,

                        COALESCE(
                            p.metros_lineales_contratados,
                            0
                        ) AS metros_lineales_contratados

                    FROM proyectos p

                    INNER JOIN empresas e
                        ON e.id_empresa =
                           p.id_empresa

                    WHERE p.activo = 1

                    ORDER BY
                        p.codigo_proyecto
                    """;

            try (
                    PreparedStatement ps =
                            conexion.prepareStatement(
                                    sqlProyectos
                            );

                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    int idProyecto =
                            rs.getInt(
                                    "id_proyecto"
                            );

                    String codigo =
                            rs.getString(
                                    "codigo_proyecto"
                            );

                    String descripcion =
                            rs.getString(
                                    "descripcion"
                            );

                    double contratados =
                            rs.getDouble(
                                    "metros_lineales_contratados"
                            );

                    /*
                     * Buscamos la información de Control Diario.
                     */
                    ControlDatos control =
                            controles.get(
                                    idProyecto
                            );

                    if (control == null) {

                        control =
                                new ControlDatos();
                    }

                    /*
                     * Buscamos las Guías usando:
                     *
                     * - código;
                     * - descripción;
                     * - código + descripción.
                     */
                    GuiaDatos operacion =
                            buscarDatosGuia(
                                    guias,
                                    codigo,
                                    descripcion
                            );

                    double restantes =
                            contratados
                                    - control.metrosAcumulados;

                    if (restantes < 0) {

                        restantes = 0;
                    }

                    double porcentaje = 0;

                    if (contratados > 0) {

                        porcentaje =
                                control.metrosAcumulados
                                        * 100
                                        / contratados;
                    }

                    lista.add(
                            new AvanceProyectoResumen(
                                    idProyecto,
                                    codigo,
                                    descripcion,

                                    rs.getString(
                                            "empresa"
                                    ),

                                    control.fechaControl,

                                    obtenerFecha(
                                            rs,
                                            "fecha_inicio"
                                    ),

                                    obtenerEnteroNullable(
                                            rs,
                                            "dias_estimados"
                                    ),

                                    contratados,

                                    control.avanceDiario,

                                    control.metrosAcumulados,

                                    restantes,

                                    control.ancho,

                                    control.espesor,

                                    control.volumenDiario,

                                    control.volumenAcumulado,

                                    operacion.horasTrabajadas,

                                    operacion.metrosCubicos,

                                    operacion.cantidadViajes,

                                    porcentaje
                            )
                    );
                }
            }
        }

        return lista;
    }

    /*
     * Consulta todos los controles diarios activos.
     *
     * Por cada proyecto calcula:
     *
     * - metros lineales acumulados;
     * - volumen acumulado;
     * - último control registrado;
     * - avance del último día;
     * - último ancho;
     * - último espesor;
     * - último volumen diario.
     */
    private static Map<Integer, ControlDatos>
            obtenerDatosControlDiario(
                    Connection conexion
            ) throws Exception {

        Map<Integer, ControlDatos> resultado =
                new HashMap<>();

        String sql = """
                SELECT
                    id_control,
                    id_proyecto,
                    fecha_control,
                    metros_lineales,
                    ancho,
                    espesor,

                    COALESCE(
                        volumen_real,
                        0
                    ) AS volumen_real

                FROM control_diario

                WHERE activo = 1

                ORDER BY
                    id_proyecto,
                    fecha_control,
                    id_control
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                int idProyecto =
                        rs.getInt(
                                "id_proyecto"
                        );

                int idControl =
                        rs.getInt(
                                "id_control"
                        );

                LocalDate fecha =
                        obtenerFecha(
                                rs,
                                "fecha_control"
                        );

                double metrosLineales =
                        rs.getDouble(
                                "metros_lineales"
                        );

                double volumen =
                        rs.getDouble(
                                "volumen_real"
                        );

                ControlDatos datos =
                        resultado.computeIfAbsent(
                                idProyecto,
                                clave ->
                                        new ControlDatos()
                        );

                /*
                 * Acumulados del proyecto.
                 */
                datos.metrosAcumulados +=
                        metrosLineales;

                datos.volumenAcumulado +=
                        volumen;

                /*
                 * Determinamos si este registro es
                 * el control más reciente del proyecto.
                 */
                boolean esMasReciente =
                        datos.fechaControl == null
                                || fecha.isAfter(
                                        datos.fechaControl
                                )
                                || (
                                fecha.equals(
                                        datos.fechaControl
                                )
                                        && idControl
                                        > datos.idUltimoControl
                        );

                if (esMasReciente) {

                    datos.fechaControl =
                            fecha;

                    datos.idUltimoControl =
                            idControl;

                    datos.avanceDiario =
                            metrosLineales;

                    datos.ancho =
                            obtenerDoubleNullable(
                                    rs,
                                    "ancho"
                            );

                    datos.espesor =
                            obtenerDoubleNullable(
                                    rs,
                                    "espesor"
                            );

                    datos.volumenDiario =
                            volumen;
                }
            }
        }

        return resultado;
    }

    /*
     * Consulta las Guías.
     *
     * Obtiene por proyecto:
     *
     * - horas trabajadas;
     * - metros cúbicos;
     * - cantidad de viajes.
     *
     * DISTINCT evita contar varias veces la misma guía
     * cuando tiene varias filas en guia_produccion_detalle.
     */
    private static Map<String, GuiaDatos>
            obtenerDatosGuias(
                    Connection conexion
            ) throws Exception {

        Map<String, GuiaDatos> resultado =
                new HashMap<>();

        String sql = """
                SELECT DISTINCT
                    g.id_guia,

                    COALESCE(
                        NULLIF(
                            TRIM(d.proyecto),
                            ''
                        ),
                        NULLIF(
                            TRIM(g.proyecto),
                            ''
                        )
                    ) AS proyecto,

                    COALESCE(
                        g.horas,
                        0
                    ) AS horas,

                    COALESCE(
                        g.m3,
                        0
                    ) AS metros_cubicos

                FROM guias g

                LEFT JOIN guia_produccion_detalle d
                    ON d.id_guia =
                       g.id_guia

                WHERE COALESCE(
                          g.estado,
                          ''
                      ) <> 'CANCELADO'

                  AND COALESCE(
                        NULLIF(
                            TRIM(d.proyecto),
                            ''
                        ),
                        NULLIF(
                            TRIM(g.proyecto),
                            ''
                        )
                      ) IS NOT NULL

                ORDER BY
                    g.id_guia
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                String proyectoNormalizado =
                        normalizar(
                                rs.getString(
                                        "proyecto"
                                )
                        );

                if (proyectoNormalizado.isBlank()) {

                    continue;
                }

                GuiaDatos datos =
                        resultado.computeIfAbsent(
                                proyectoNormalizado,
                                clave ->
                                        new GuiaDatos()
                        );

                datos.horasTrabajadas +=
                        rs.getDouble(
                                "horas"
                        );

                datos.metrosCubicos +=
                        rs.getDouble(
                                "metros_cubicos"
                        );

                datos.cantidadViajes++;
            }
        }

        return resultado;
    }

    /*
     * Busca la información de las Guías utilizando
     * tres formatos posibles:
     *
     * 1. Código del proyecto.
     * 2. Descripción del proyecto.
     * 3. Código - Descripción.
     */
    private static GuiaDatos buscarDatosGuia(
            Map<String, GuiaDatos> guias,
            String codigo,
            String descripcion
    ) {

        String claveCodigo =
                normalizar(
                        codigo
                );

        String claveDescripcion =
                normalizar(
                        descripcion
                );

        String claveCompleta =
                normalizar(
                        codigo
                                + " - "
                                + descripcion
                );

        GuiaDatos datos =
                guias.get(
                        claveCodigo
                );

        if (datos == null) {

            datos =
                    guias.get(
                            claveDescripcion
                    );
        }

        if (datos == null) {

            datos =
                    guias.get(
                            claveCompleta
                    );
        }

        return datos == null
                ? new GuiaDatos()
                : datos;
    }

    /*
     * Normaliza los nombres de los proyectos para
     * facilitar la comparación entre Proyectos y Guías.
     */
    private static String normalizar(
            String texto
    ) {

        if (texto == null) {

            return "";
        }

        return texto
                .trim()
                .replaceAll(
                        "\\s+",
                        " "
                )
                .toUpperCase();
    }

    /*
     * Obtiene los indicadores superiores del Dashboard.
     */
    public static DashboardResumen
            obtenerResumen() throws Exception {

        String sql = """
                SELECT
                    COUNT(*) AS proyectos_activos,

                    COALESCE(
                        SUM(resumen.contratados),
                        0
                    ) AS metros_lineales_contratados,

                    COALESCE(
                        SUM(resumen.ejecutados),
                        0
                    ) AS metros_lineales_ejecutados,

                    COALESCE(
                        SUM(resumen.volumen),
                        0
                    ) AS volumen_ejecutado

                FROM (
                    SELECT
                        p.id_proyecto,

                        COALESCE(
                            p.metros_lineales_contratados,
                            0
                        ) AS contratados,

                        COALESCE(
                            SUM(
                                cd.metros_lineales
                            ),
                            0
                        ) AS ejecutados,

                        COALESCE(
                            SUM(
                                cd.volumen_real
                            ),
                            0
                        ) AS volumen

                    FROM proyectos p

                    LEFT JOIN control_diario cd
                        ON cd.id_proyecto =
                           p.id_proyecto

                       AND cd.activo = 1

                    WHERE p.activo = 1

                    GROUP BY
                        p.id_proyecto,
                        p.metros_lineales_contratados
                ) resumen
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

            if (!rs.next()) {

                return new DashboardResumen(
                        0,
                        0,
                        0,
                        0,
                        0
                );
            }

            double contratados =
                    rs.getDouble(
                            "metros_lineales_contratados"
                    );

            double ejecutados =
                    rs.getDouble(
                            "metros_lineales_ejecutados"
                    );

            double porcentaje = 0;

            if (contratados > 0) {

                porcentaje =
                        ejecutados
                                * 100
                                / contratados;
            }

            return new DashboardResumen(
                    rs.getInt(
                            "proyectos_activos"
                    ),

                    contratados,

                    ejecutados,

                    rs.getDouble(
                            "volumen_ejecutado"
                    ),

                    porcentaje
            );
        }
    }

    private static LocalDate obtenerFecha(
            ResultSet rs,
            String columna
    ) throws Exception {

        Date fecha =
                rs.getDate(
                        columna
                );

        return fecha == null
                ? null
                : fecha.toLocalDate();
    }

    private static Integer obtenerEnteroNullable(
            ResultSet rs,
            String columna
    ) throws Exception {

        Object valor =
                rs.getObject(
                        columna
                );

        return valor == null
                ? null
                : rs.getInt(
                        columna
                );
    }

    private static Double obtenerDoubleNullable(
            ResultSet rs,
            String columna
    ) throws Exception {

        Object valor =
                rs.getObject(
                        columna
                );

        return valor == null
                ? null
                : rs.getDouble(
                        columna
                );
    }
}