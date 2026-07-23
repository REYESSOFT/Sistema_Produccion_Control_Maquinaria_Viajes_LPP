import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CatalogoCanteraMaterialDAO {

    public record TarifaResumen(
            int idTarifa,
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            boolean activo
    ) {
    }

    public record TarifaDetalle(
            int idTarifa,
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte,
            boolean activo
    ) {
    }

    public record CatalogoItem(
            int id,
            String nombre
    ) {
        @Override
        public String toString() {
            return nombre;
        }
    }

    public static List<TarifaResumen> buscar(
            String cantera,
            String material,
            String destinoSector,
            String estado
    ) throws Exception {

        StringBuilder sql = new StringBuilder(
                """
                SELECT
                    id_tarifa,
                    cantera,
                    material,
                    destino_sector,
                    COALESCE(
                        costo_unitario_material,
                        0
                    ) AS costo_unitario_material,
                    COALESCE(
                        costo_unitario_transporte,
                        0
                    ) AS costo_unitario_transporte,
                    activo

                FROM catalogo_cantera_material

                WHERE 1 = 1
                """
        );

        List<Object> parametros =
                new ArrayList<>();

        if (
                cantera != null
                && !cantera.isBlank()
        ) {

            sql.append(
                    """
                    
                    AND UPPER(TRIM(cantera))
                        LIKE UPPER(?)
                    """
            );

            parametros.add(
                    "%"
                    + cantera.trim()
                    + "%"
            );
        }

        if (
                material != null
                && !material.isBlank()
        ) {

            sql.append(
                    """
                    
                    AND UPPER(TRIM(material))
                        LIKE UPPER(?)
                    """
            );

            parametros.add(
                    "%"
                    + material.trim()
                    + "%"
            );
        }

        if (
                destinoSector != null
                && !destinoSector.isBlank()
        ) {

            sql.append(
                    """
                    
                    AND UPPER(TRIM(destino_sector))
                        LIKE UPPER(?)
                    """
            );

            parametros.add(
                    "%"
                    + destinoSector.trim()
                    + "%"
            );
        }

        if (
                estado != null
                && estado.equalsIgnoreCase("ACTIVO")
        ) {

            sql.append(
                    """
                    
                    AND activo = 1
                    """
            );

        } else if (
                estado != null
                && estado.equalsIgnoreCase("INACTIVO")
        ) {

            sql.append(
                    """
                    
                    AND activo = 0
                    """
            );
        }

        sql.append(
                """
                
                ORDER BY
                    cantera,
                    material,
                    destino_sector,
                    id_tarifa
                """
        );

        List<TarifaResumen> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql.toString()
                        )
        ) {

            for (
                    int i = 0;
                    i < parametros.size();
                    i++
            ) {

                ps.setObject(
                        i + 1,
                        parametros.get(i)
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            new TarifaResumen(
                                    rs.getInt(
                                            "id_tarifa"
                                    ),

                                    rs.getString(
                                            "cantera"
                                    ),

                                    rs.getString(
                                            "material"
                                    ),

                                    rs.getString(
                                            "destino_sector"
                                    ),

                                    rs.getDouble(
                                            "costo_unitario_material"
                                    ),

                                    rs.getDouble(
                                            "costo_unitario_transporte"
                                    ),

                                    rs.getBoolean(
                                            "activo"
                                    )
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static List<TarifaResumen> obtenerActivas()
            throws Exception {

        return buscar(
                "",
                "",
                "",
                "ACTIVO"
        );
    }

    public static TarifaDetalle obtenerPorId(
            int idTarifa
    ) throws Exception {

        validarIdTarifa(
                idTarifa
        );

        String sql =
                """
                SELECT
                    id_tarifa,
                    cantera,
                    material,
                    destino_sector,
                    COALESCE(
                        costo_unitario_material,
                        0
                    ) AS costo_unitario_material,
                    COALESCE(
                        costo_unitario_transporte,
                        0
                    ) AS costo_unitario_transporte,
                    activo

                FROM catalogo_cantera_material

                WHERE id_tarifa = ?

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
                    idTarifa
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró la tarifa seleccionada."
                    );
                }

                return new TarifaDetalle(
                        rs.getInt(
                                "id_tarifa"
                        ),

                        rs.getString(
                                "cantera"
                        ),

                        rs.getString(
                                "material"
                        ),

                        rs.getString(
                                "destino_sector"
                        ),

                        rs.getDouble(
                                "costo_unitario_material"
                        ),

                        rs.getDouble(
                                "costo_unitario_transporte"
                        ),

                        rs.getBoolean(
                                "activo"
                        )
                );
            }
        }
    }

    public static int insertar(
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte
    ) throws Exception {

        validarDatos(
                cantera,
                material,
                destinoSector,
                costoUnitarioMaterial,
                costoUnitarioTransporte
        );

        validarDuplicado(
                cantera,
                material,
                destinoSector,
                0
        );

        String sql =
                """
                INSERT INTO catalogo_cantera_material (
                    cantera,
                    material,
                    destino_sector,
                    costo_unitario_material,
                    costo_unitario_transporte,
                    activo
                )
                VALUES (?, ?, ?, ?, ?, 1)
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

            ps.setString(
                    1,
                    cantera.trim()
            );

            ps.setString(
                    2,
                    material.trim()
            );

            ps.setString(
                    3,
                    destinoSector.trim()
            );

            ps.setDouble(
                    4,
                    costoUnitarioMaterial
            );

            ps.setDouble(
                    5,
                    costoUnitarioTransporte
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible guardar la tarifa."
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
                "La tarifa fue guardada, "
                + "pero no se pudo obtener su ID."
        );
    }

    public static void actualizar(
            int idTarifa,
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte
    ) throws Exception {

        validarIdTarifa(
                idTarifa
        );

        validarDatos(
                cantera,
                material,
                destinoSector,
                costoUnitarioMaterial,
                costoUnitarioTransporte
        );

        validarDuplicado(
                cantera,
                material,
                destinoSector,
                idTarifa
        );

        String sql =
                """
                UPDATE catalogo_cantera_material
                SET
                    cantera = ?,
                    material = ?,
                    destino_sector = ?,
                    costo_unitario_material = ?,
                    costo_unitario_transporte = ?

                WHERE id_tarifa = ?
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setString(
                    1,
                    cantera.trim()
            );

            ps.setString(
                    2,
                    material.trim()
            );

            ps.setString(
                    3,
                    destinoSector.trim()
            );

            ps.setDouble(
                    4,
                    costoUnitarioMaterial
            );

            ps.setDouble(
                    5,
                    costoUnitarioTransporte
            );

            ps.setInt(
                    6,
                    idTarifa
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible actualizar la tarifa."
                );
            }
        }
    }

    public static void desactivar(
            int idTarifa
    ) throws Exception {

        validarIdTarifa(
                idTarifa
        );

        String sql =
                """
                UPDATE catalogo_cantera_material
                SET activo = 0
                WHERE id_tarifa = ?
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
                    idTarifa
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible eliminar la tarifa. "
                        + "Puede que ya esté inactiva."
                );
            }
        }
    }

    public static void reactivar(
            int idTarifa
    ) throws Exception {

        validarIdTarifa(
                idTarifa
        );

        String sql =
                """
                UPDATE catalogo_cantera_material
                SET activo = 1
                WHERE id_tarifa = ?
                  AND activo = 0
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
                    idTarifa
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new Exception(
                        "No fue posible reactivar la tarifa. "
                        + "Puede que ya esté activa."
                );
            }
        }
    }

    public static List<String> obtenerCanterasActivas()
            throws Exception {

        String sql =
                """
                SELECT DISTINCT
                    cantera

                FROM catalogo_cantera_material

                WHERE activo = 1

                ORDER BY cantera
                """;

        return obtenerListaTexto(
                sql
        );
    }

    public static List<String> obtenerMaterialesPorCantera(
            String cantera
    ) throws Exception {

        if (
                cantera == null
                || cantera.isBlank()
        ) {

            return new ArrayList<>();
        }

        String sql =
                """
                SELECT DISTINCT
                    material

                FROM catalogo_cantera_material

                WHERE activo = 1
                  AND UPPER(TRIM(cantera)) =
                      UPPER(TRIM(?))

                ORDER BY material
                """;

        List<String> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setString(
                    1,
                    cantera.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            rs.getString(
                                    "material"
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static List<String> obtenerDestinos(
            String cantera,
            String material
    ) throws Exception {

        if (
                cantera == null
                || cantera.isBlank()
                || material == null
                || material.isBlank()
        ) {

            return new ArrayList<>();
        }

        String sql =
                """
                SELECT DISTINCT
                    destino_sector

                FROM catalogo_cantera_material

                WHERE activo = 1
                  AND UPPER(TRIM(cantera)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(material)) =
                      UPPER(TRIM(?))

                ORDER BY destino_sector
                """;

        List<String> lista =
                new ArrayList<>();

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {

            ps.setString(
                    1,
                    cantera.trim()
            );

            ps.setString(
                    2,
                    material.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            rs.getString(
                                    "destino_sector"
                            )
                    );
                }
            }
        }

        return lista;
    }

    public static TarifaDetalle obtenerTarifaActiva(
            String cantera,
            String material,
            String destinoSector
    ) throws Exception {

        validarTexto(
                cantera,
                "la cantera"
        );

        validarTexto(
                material,
                "el material"
        );

        validarTexto(
                destinoSector,
                "el destino o sector"
        );

        String sql =
                """
                SELECT
                    id_tarifa,
                    cantera,
                    material,
                    destino_sector,
                    costo_unitario_material,
                    costo_unitario_transporte,
                    activo

                FROM catalogo_cantera_material

                WHERE activo = 1
                  AND UPPER(TRIM(cantera)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(material)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(destino_sector)) =
                      UPPER(TRIM(?))

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

            ps.setString(
                    1,
                    cantera.trim()
            );

            ps.setString(
                    2,
                    material.trim()
            );

            ps.setString(
                    3,
                    destinoSector.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (!rs.next()) {

                    throw new Exception(
                            "No se encontró una tarifa activa "
                            + "para la cantera, material y destino seleccionados."
                    );
                }

                return new TarifaDetalle(
                        rs.getInt(
                                "id_tarifa"
                        ),

                        rs.getString(
                                "cantera"
                        ),

                        rs.getString(
                                "material"
                        ),

                        rs.getString(
                                "destino_sector"
                        ),

                        rs.getDouble(
                                "costo_unitario_material"
                        ),

                        rs.getDouble(
                                "costo_unitario_transporte"
                        ),

                        rs.getBoolean(
                                "activo"
                        )
                );
            }
        }
    }

    private static List<String> obtenerListaTexto(
            String sql
    ) throws Exception {

        List<String> lista =
                new ArrayList<>();

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

                lista.add(
                        rs.getString(1)
                );
            }
        }

        return lista;
    }

    private static void validarDatos(
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte
    ) throws Exception {

        validarTexto(
                cantera,
                "la cantera"
        );

        validarTexto(
                material,
                "el material"
        );

        validarTexto(
                destinoSector,
                "el destino o sector"
        );

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
    }

    private static void validarDuplicado(
            String cantera,
            String material,
            String destinoSector,
            int idExcluir
    ) throws Exception {

        String sql =
                """
                SELECT id_tarifa

                FROM catalogo_cantera_material

                WHERE UPPER(TRIM(cantera)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(material)) =
                      UPPER(TRIM(?))
                  AND UPPER(TRIM(destino_sector)) =
                      UPPER(TRIM(?))
                  AND id_tarifa <> ?

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

            ps.setString(
                    1,
                    cantera.trim()
            );

            ps.setString(
                    2,
                    material.trim()
            );

            ps.setString(
                    3,
                    destinoSector.trim()
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
                            "Ya existe una tarifa registrada "
                            + "para esa cantera, material y destino."
                    );
                }
            }
        }
    }

    private static void validarTexto(
            String valor,
            String nombreCampo
    ) throws Exception {

        if (
                valor == null
                || valor.isBlank()
        ) {

            throw new Exception(
                    "Debe ingresar "
                    + nombreCampo
                    + "."
            );
        }
    }

    private static void validarIdTarifa(
            int idTarifa
    ) throws Exception {

        if (idTarifa <= 0) {

            throw new Exception(
                    "La tarifa seleccionada no es válida."
            );
        }
    }

    private static void asignarTexto(
            PreparedStatement ps,
            int posicion,
            String valor
    ) throws Exception {

        if (
                valor == null
                || valor.isBlank()
        ) {

            ps.setNull(
                    posicion,
                    Types.VARCHAR
            );

        } else {

            ps.setString(
                    posicion,
                    valor.trim()
            );
        }
    }
}
