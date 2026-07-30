import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControlDiarioMaterialDAO {

    public record ControlMaterialResumen(
            int idControlMaterial,
            int idControl,
            String materialRecibido,
            String cantera,
            double cantidadViajes,
            double volumenRecibido,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            double costoMaterial,
            double costoTransporte,
            double costoTotal,
            int cantidadVolquetas,
            double horasVolqueta,
            String observaciones
    ) {
    }

    public record ControlMaterialDetalle(
        int idControlMaterial,
        int idControl,
        String materialRecibido,
        String cantera,
        String destinoSector,
        double cantidadViajes,
        double volumenRecibido,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte,
        double costoMaterial,
        double costoTransporte,
        int cantidadVolquetas,
        double horasVolqueta,
        String observaciones
) {
}

    public static List<ControlMaterialResumen> obtenerPorControl(
            int idControl
    ) throws Exception {

        validarIdControl(
                idControl
        );

        String sql = """
                SELECT
                    id_control_material,
                    id_control,
                    material_recibido,
                    cantera,
                    COALESCE(cantidad_viajes, 0)
                        AS cantidad_viajes,
                    COALESCE(volumen_recibido, 0)
                        AS volumen_recibido,
                    COALESCE(costo_unitario_material, 0)
                        AS costo_unitario_material,
                    COALESCE(costo_unitario_transporte, 0)
                        AS costo_unitario_transporte,
                    COALESCE(costo_material, 0)
                        AS costo_material,
                    COALESCE(costo_transporte, 0)
                        AS costo_transporte,
                    COALESCE(cantidad_volquetas, 0)
                        AS cantidad_volquetas,
                    COALESCE(horas_volqueta, 0)
                        AS horas_volqueta,
                    COALESCE(observaciones, '')
                        AS observaciones

                FROM control_diario_material

                WHERE id_control = ?
                  AND activo = 1

                ORDER BY
                    cantera,
                    material_recibido,
                    id_control_material
                """;

        List<ControlMaterialResumen> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    double costoMaterial =
                            rs.getDouble(
                                    "costo_material"
                            );

                    double costoTransporte =
                            rs.getDouble(
                                    "costo_transporte"
                            );

                    double costoTotal =
                            costoMaterial
                            + costoTransporte;

                    lista.add(
                            new ControlMaterialResumen(
                                    rs.getInt(
                                            "id_control_material"
                                    ),

                                    rs.getInt(
                                            "id_control"
                                    ),

                                    rs.getString(
                                            "material_recibido"
                                    ),

                                    rs.getString(
                                            "cantera"
                                    ),

                                    rs.getDouble(
                                            "cantidad_viajes"
                                    ),

                                    rs.getDouble(
                                            "volumen_recibido"
                                    ),

                                    rs.getDouble(
                                            "costo_unitario_material"
                                    ),

                                    rs.getDouble(
                                            "costo_unitario_transporte"
                                    ),

                                    costoMaterial,

                                    costoTransporte,

                                    costoTotal,

                                    rs.getInt(
                                            "cantidad_volquetas"
                                    ),

                                    rs.getDouble(
                                            "horas_volqueta"
                                    ),

                                    rs.getString(
                                            "observaciones"
                                    )
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static ControlMaterialDetalle obtenerPorId(
            int idControlMaterial
    ) throws Exception {

        validarIdRegistro(
                idControlMaterial
        );

        String sql = """
                SELECT
                    id_control_material,
                    id_control,
                    material_recibido,
                    cantera,
                    COALESCE(destino_sector, '')
    AS destino_sector,
                    COALESCE(cantidad_viajes, 0)
                        AS cantidad_viajes,
                    COALESCE(volumen_recibido, 0)
                        AS volumen_recibido,
                    COALESCE(costo_unitario_material, 0)
                        AS costo_unitario_material,
                    COALESCE(costo_unitario_transporte, 0)
                        AS costo_unitario_transporte,
                    COALESCE(costo_material, 0)
                        AS costo_material,
                    COALESCE(costo_transporte, 0)
                        AS costo_transporte,
                    COALESCE(cantidad_volquetas, 0)
                        AS cantidad_volquetas,
                    COALESCE(horas_volqueta, 0)
                        AS horas_volqueta,
                    COALESCE(observaciones, '')
                        AS observaciones

                FROM control_diario_material

                WHERE id_control_material = ?
                  AND activo = 1

                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    idControlMaterial
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró el registro de material pétreo."
                    );
                }

                return new ControlMaterialDetalle(
                        rs.getInt(
                                "id_control_material"
                        ),

                        rs.getInt(
                                "id_control"
                        ),

                        rs.getString(
                                "material_recibido"
                        ),

                        rs.getString(
                                "cantera"
                        ),
                        rs.getString("destino_sector"),

                        rs.getDouble(
                                "cantidad_viajes"
                        ),

                        rs.getDouble(
                                "volumen_recibido"
                        ),

                        rs.getDouble(
                                "costo_unitario_material"
                        ),

                        rs.getDouble(
                                "costo_unitario_transporte"
                        ),

                        rs.getDouble(
                                "costo_material"
                        ),

                        rs.getDouble(
                                "costo_transporte"
                        ),

                        rs.getInt(
                                "cantidad_volquetas"
                        ),

                        rs.getDouble(
                                "horas_volqueta"
                        ),

                        rs.getString(
                                "observaciones"
                        )
                );
            }
        }
    }

   public static int insertar(
        int idControl,
        String materialRecibido,
        String cantera,
        double cantidadViajes,
        double volumenRecibido,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte,
        int cantidadVolquetas,
        double horasVolqueta,
        String observaciones
) throws Exception {

    return insertar(
            idControl,
            null,
            materialRecibido,
            cantera,
            null,
            cantidadViajes,
            volumenRecibido,
            costoUnitarioMaterial,
            costoUnitarioTransporte,
            cantidadVolquetas,
            horasVolqueta,
            observaciones
    );
}

public static int insertar(
        int idControl,
        Integer idTarifa,
        String materialRecibido,
        String cantera,
        String destinoSector,
        double cantidadViajes,
        double volumenRecibido,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte,
        int cantidadVolquetas,
        double horasVolqueta,
        String observaciones
) throws Exception {

    validarDatos(
            idControl,
            materialRecibido,
            cantera,
            cantidadViajes,
            volumenRecibido,
            costoUnitarioMaterial,
            costoUnitarioTransporte,
            cantidadVolquetas,
            horasVolqueta
    );

    validarDuplicado(
            idControl,
            materialRecibido,
            cantera,
            0
    );

    double costoMaterial =
            calcularCostoMaterial(
                    cantidadViajes,
                    costoUnitarioMaterial
            );

    double costoTransporte =
            calcularCostoTransporte(
                    cantidadViajes,
                    costoUnitarioTransporte
            );

    String sql = """
            INSERT INTO control_diario_material (
                id_control,
                id_tarifa,
                material_recibido,
                cantera,
                destino_sector,
                cantidad_viajes,
                volumen_recibido,
                costo_unitario_material,
                costo_unitario_transporte,
                costo_material,
                costo_transporte,
                cantidad_volquetas,
                horas_volqueta,
                observaciones,
                activo
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
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

        ps.setInt(
                1,
                idControl
        );

        if (
                idTarifa == null
                || idTarifa <= 0
        ) {

            ps.setNull(
                    2,
                    Types.INTEGER
            );

        } else {

            ps.setInt(
                    2,
                    idTarifa
            );
        }

        ps.setString(
                3,
                materialRecibido.trim()
        );

        ps.setString(
                4,
                cantera.trim()
        );

        asignarTexto(
                ps,
                5,
                destinoSector
        );

        ps.setDouble(
                6,
                cantidadViajes
        );

        ps.setDouble(
                7,
                volumenRecibido
        );

        ps.setDouble(
                8,
                costoUnitarioMaterial
        );

        ps.setDouble(
                9,
                costoUnitarioTransporte
        );

        ps.setDouble(
                10,
                costoMaterial
        );

        ps.setDouble(
                11,
                costoTransporte
        );

        ps.setInt(
                12,
                cantidadVolquetas
        );

        ps.setDouble(
                13,
                horasVolqueta
        );

        asignarTexto(
                ps,
                14,
                observaciones
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible guardar el material pétreo."
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
            "El material pétreo fue guardado, "
                    + "pero no se pudo obtener su ID."
    );
}

    public static void actualizar(
        int idControlMaterial,
        int idControl,
        String materialRecibido,
        String cantera,
        double cantidadViajes,
        double volumenRecibido,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte,
        int cantidadVolquetas,
        double horasVolqueta,
        String observaciones
) throws Exception {

    actualizar(
            idControlMaterial,
            idControl,
            null,
            materialRecibido,
            cantera,
            null,
            cantidadViajes,
            volumenRecibido,
            costoUnitarioMaterial,
            costoUnitarioTransporte,
            cantidadVolquetas,
            horasVolqueta,
            observaciones
    );
}

public static void actualizar(
        int idControlMaterial,
        int idControl,
        Integer idTarifa,
        String materialRecibido,
        String cantera,
        String destinoSector,
        double cantidadViajes,
        double volumenRecibido,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte,
        int cantidadVolquetas,
        double horasVolqueta,
        String observaciones
) throws Exception {

    validarIdRegistro(
            idControlMaterial
    );

    validarDatos(
            idControl,
            materialRecibido,
            cantera,
            cantidadViajes,
            volumenRecibido,
            costoUnitarioMaterial,
            costoUnitarioTransporte,
            cantidadVolquetas,
            horasVolqueta
    );

    validarDuplicado(
            idControl,
            materialRecibido,
            cantera,
            idControlMaterial
    );

    double costoMaterial =
            calcularCostoMaterial(
                    cantidadViajes,
                    costoUnitarioMaterial
            );

    double costoTransporte =
            calcularCostoTransporte(
                    cantidadViajes,
                    costoUnitarioTransporte
            );

    String sql = """
            UPDATE control_diario_material
            SET
                id_tarifa = ?,
                material_recibido = ?,
                cantera = ?,
                destino_sector = ?,
                cantidad_viajes = ?,
                volumen_recibido = ?,
                costo_unitario_material = ?,
                costo_unitario_transporte = ?,
                costo_material = ?,
                costo_transporte = ?,
                cantidad_volquetas = ?,
                horas_volqueta = ?,
                observaciones = ?

            WHERE id_control_material = ?
              AND id_control = ?
              AND activo = 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(
                            sql
                    )
    ) {

        if (
                idTarifa == null
                || idTarifa <= 0
        ) {

            ps.setNull(
                    1,
                    Types.INTEGER
            );

        } else {

            ps.setInt(
                    1,
                    idTarifa
            );
        }

        ps.setString(
                2,
                materialRecibido.trim()
        );

        ps.setString(
                3,
                cantera.trim()
        );

        asignarTexto(
                ps,
                4,
                destinoSector
        );

        ps.setDouble(
                5,
                cantidadViajes
        );

        ps.setDouble(
                6,
                volumenRecibido
        );

        ps.setDouble(
                7,
                costoUnitarioMaterial
        );

        ps.setDouble(
                8,
                costoUnitarioTransporte
        );

        ps.setDouble(
                9,
                costoMaterial
        );

        ps.setDouble(
                10,
                costoTransporte
        );

        ps.setInt(
                11,
                cantidadVolquetas
        );

        ps.setDouble(
                12,
                horasVolqueta
        );

        asignarTexto(
                ps,
                13,
                observaciones
        );

        ps.setInt(
                14,
                idControlMaterial
        );

        ps.setInt(
                15,
                idControl
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar el material pétreo."
            );
        }
    }
}
    public static void eliminar(
            int idControlMaterial
    ) throws Exception {

        validarIdRegistro(
                idControlMaterial
        );

        String sql = """
                UPDATE control_diario_material
                SET activo = 0
                WHERE id_control_material = ?
                  AND activo = 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    idControlMaterial
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible eliminar el material pétreo. "
                                + "Puede que ya haya sido eliminado."
                );
            }
        }
    }

    public static double obtenerCostoMaterialControl(
            int idControl
    ) throws Exception {

        validarIdControl(
                idControl
        );

        String sql = """
                SELECT
                    COALESCE(
                        SUM(costo_material),
                        0
                    ) AS total

                FROM control_diario_material

                WHERE id_control = ?
                  AND activo = 1
                """;

        return obtenerTotal(
                idControl,
                sql
        );
    }

    public static double obtenerCostoTransporteControl(
            int idControl
    ) throws Exception {

        validarIdControl(
                idControl
        );

        String sql = """
                SELECT
                    COALESCE(
                        SUM(costo_transporte),
                        0
                    ) AS total

                FROM control_diario_material

                WHERE id_control = ?
                  AND activo = 1
                """;

        return obtenerTotal(
                idControl,
                sql
        );
    }

    public static double obtenerCostoTotalControl(
            int idControl
    ) throws Exception {

        validarIdControl(
                idControl
        );

        String sql = """
                SELECT
                    COALESCE(
                        SUM(
                            costo_material
                            + costo_transporte
                        ),
                        0
                    ) AS total

                FROM control_diario_material

                WHERE id_control = ?
                  AND activo = 1
                """;

        return obtenerTotal(
                idControl,
                sql
        );
    }

    private static double obtenerTotal(
            int idControl,
            String sql
    ) throws Exception {

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getDouble(
                            "total"
                    );
                }
            }
        }

        return 0.00;
    }

    private static double calcularCostoMaterial(
            double cantidadViajes,
            double costoUnitarioMaterial
    ) {

        return cantidadViajes
                * costoUnitarioMaterial;
    }

    private static double calcularCostoTransporte(
            double cantidadViajes,
            double costoUnitarioTransporte
    ) {

        return cantidadViajes
                * costoUnitarioTransporte;
    }

    private static void validarDatos(
            int idControl,
            String materialRecibido,
            String cantera,
            double cantidadViajes,
            double volumenRecibido,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            int cantidadVolquetas,
            double horasVolqueta
    ) throws Exception {

        validarIdControl(
                idControl
        );

        if (
                materialRecibido == null
                || materialRecibido.isBlank()
        ) {

            throw new Exception(
                    "Debe ingresar el material recibido."
            );
        }

        if (
                cantera == null
                || cantera.isBlank()
        ) {

            throw new Exception(
                    "Debe ingresar la cantera."
            );
        }

        if (cantidadViajes < 0) {

            throw new Exception(
                    "La cantidad de viajes no puede ser negativa."
            );
        }

        if (volumenRecibido < 0) {

            throw new Exception(
                    "El volumen recibido no puede ser negativo."
            );
        }

        if (costoUnitarioMaterial < 0) {

            throw new Exception(
                    "El costo unitario del material "
                            + "no puede ser negativo."
            );
        }

        if (costoUnitarioTransporte < 0) {

            throw new Exception(
                    "El costo unitario del transporte "
                            + "no puede ser negativo."
            );
        }

        if (cantidadVolquetas < 0) {

            throw new Exception(
                    "La cantidad de volquetas no puede ser negativa."
            );
        }

        if (horasVolqueta < 0) {

            throw new Exception(
                    "Las horas de volqueta no pueden ser negativas."
            );
        }

        if (horasVolqueta > 24) {

            throw new Exception(
                    "Las horas de volqueta no pueden superar 24 "
                            + "en un mismo Control Diario."
            );
        }
    }

    private static void validarDuplicado(
            int idControl,
            String materialRecibido,
            String cantera,
            int idExcluir
    ) throws Exception {

        String sql = """
                SELECT id_control_material

                FROM control_diario_material

                WHERE id_control = ?
                  AND UPPER(TRIM(material_recibido)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(cantera)) =
                      UPPER(TRIM(?))
                  AND activo = 1
                  AND id_control_material <> ?

                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    idControl
            );

            ps.setString(
                    2,
                    materialRecibido.trim()
            );

            ps.setString(
                    3,
                    cantera.trim()
            );

            ps.setInt(
                    4,
                    idExcluir
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "El material y la cantera ya están registrados "
                                    + "en este Control Diario."
                    );
                }
            }
        }
    }

    private static void validarIdControl(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {

            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }
    }

    private static void validarIdRegistro(
            int idControlMaterial
    ) throws Exception {

        if (idControlMaterial <= 0) {

            throw new Exception(
                    "El registro de material pétreo "
                            + "seleccionado no es válido."
            );
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