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
        double costoUnitarioMaterial,
        boolean activo
) {
}

public record TarifaDetalle(
        int idTarifa,
        String cantera,
        String material,
        double costoUnitarioMaterial,
        boolean activo
) {
}
public record TarifaOperacion(
        int idTarifaMaterial,
        int idTarifaTransporte,
        String cantera,
        String material,
        String destinoSector,
        double costoUnitarioMaterial,
        double costoUnitarioTransporte
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
        String estado
) throws Exception {

        StringBuilder sql = new StringBuilder(
                """
                SELECT
    id_tarifa,
    cantera,
    material,
    COALESCE(
        costo_unitario_material,
        0
    ) AS costo_unitario_material,
    activo

FROM catalogo_cantera_material

                WHERE 1 = 1
                """
        );

        List<Object> parametros =
                new ArrayList<>();
        Usuario usuarioActual =
        SesionUsuario.getUsuarioActual();

Integer idEmpresaSesion =
        usuarioActual == null
                ? null
                : usuarioActual.getIdEmpresa();

if (idEmpresaSesion != null) {

    sql.append(
            """

            AND id_empresa = ?
            """
    );

    parametros.add(idEmpresaSesion);
}

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
    rs.getInt("id_tarifa"),
    rs.getString("cantera"),
    rs.getString("material"),
    rs.getDouble("costo_unitario_material"),
    rs.getBoolean("activo")
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
    COALESCE(
        costo_unitario_material,
        0
    ) AS costo_unitario_material,
    activo

FROM catalogo_cantera_material

                WHERE id_tarifa = ?
  AND id_empresa = ?

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

            Usuario usuarioActual =
        SesionUsuario.getUsuarioActual();

if (
        usuarioActual == null
        || usuarioActual.getIdEmpresa() == null
) {

    throw new Exception(
            "No se pudo determinar la empresa del usuario."
    );
}

ps.setInt(
        1,
        idTarifa
);

ps.setInt(
        2,
        usuarioActual.getIdEmpresa()
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

        rs.getDouble(
                "costo_unitario_material"
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
        double costoUnitarioMaterial
) throws Exception {

        validarDatos(
        cantera,
        material,
        costoUnitarioMaterial
);
        Usuario usuarioActual =
        SesionUsuario.getUsuarioActual();

if (
        usuarioActual == null
        || usuarioActual.getIdEmpresa() == null
) {

    throw new Exception(
            "No se pudo determinar la empresa del usuario."
    );
}

int idEmpresa =
        usuarioActual.getIdEmpresa();

        validarDuplicado(
        cantera,
        material,
        0
);

        String sql =
                """
                INSERT INTO catalogo_cantera_material (
    id_empresa,
    cantera,
    material,
    costo_unitario_material,
    activo
)
VALUES (?, ?, ?, ?, 1)
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

           ps.setInt(1, idEmpresa);
           ps.setString(2, cantera.trim());
           ps.setString(3, material.trim());
           ps.setDouble(4, costoUnitarioMaterial);

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
        double costoUnitarioMaterial
) throws Exception {

        validarIdTarifa(
                idTarifa
        );

        validarDatos(
        cantera,
        material,
        costoUnitarioMaterial
);

        validarDuplicado(
        cantera,
        material,
        idTarifa
);

        String sql =
                """
               UPDATE catalogo_cantera_material
SET
    cantera = ?,
    material = ?,
    costo_unitario_material = ?

WHERE id_tarifa = ?
  AND id_empresa = ?
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql
                        )
        ) {
        Usuario usuarioActual =
        SesionUsuario.getUsuarioActual();

if (
        usuarioActual == null
        || usuarioActual.getIdEmpresa() == null
) {

    throw new Exception(
            "No se pudo determinar la empresa del usuario."
    );
}

          ps.setString(
        1,
        cantera.trim()
);

ps.setString(
        2,
        material.trim()
);

ps.setDouble(
        3,
        costoUnitarioMaterial
);

ps.setInt(
        4,
        idTarifa
);

ps.setInt(
        5,
        usuarioActual.getIdEmpresa()
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
  AND id_empresa = ?
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
                Usuario usuarioActual =
        SesionUsuario.getUsuarioActual();

if (
        usuarioActual == null
        || usuarioActual.getIdEmpresa() == null
) {

    throw new Exception(
            "No se pudo determinar la empresa del usuario."
    );
}

           ps.setInt(
        1,
        idTarifa
);

ps.setInt(
        2,
        usuarioActual.getIdEmpresa()
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
              AND id_empresa = ?
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

        Usuario usuarioActual =
                SesionUsuario.getUsuarioActual();

        if (
                usuarioActual == null
                || usuarioActual.getIdEmpresa() == null
        ) {

            throw new Exception(
                    "No se pudo determinar la empresa del usuario."
            );
        }

        ps.setInt(
                1,
                idTarifa
        );

        ps.setInt(
                2,
                usuarioActual.getIdEmpresa()
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

    public static List<String> obtenerCanterasActivas(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {
            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String sql =
                """
                SELECT DISTINCT
                    cm.cantera

                FROM control_diario cd

                INNER JOIN proyectos p
                    ON p.id_proyecto = cd.id_proyecto

                INNER JOIN catalogo_cantera_material cm
                    ON cm.id_empresa = p.id_empresa
                   AND cm.activo = 1

                WHERE cd.id_control = ?
                  AND cd.activo = 1

                ORDER BY cm.cantera
                """;

        List<String> lista =
                new ArrayList<>();

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

                while (rs.next()) {

                    lista.add(
                            rs.getString("cantera")
                    );
                }
            }
        }

        return lista;
    }

    public static List<String> obtenerMaterialesPorCantera(
            int idControl,
            String cantera
    ) throws Exception {

        if (idControl <= 0) {
            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }

        if (
                cantera == null
                || cantera.isBlank()
        ) {
            return new ArrayList<>();
        }

        String sql =
                """
                SELECT DISTINCT
                    cm.material

                FROM control_diario cd

                INNER JOIN proyectos p
                    ON p.id_proyecto = cd.id_proyecto

                INNER JOIN catalogo_cantera_material cm
                    ON cm.id_empresa = p.id_empresa
                   AND cm.activo = 1

                WHERE cd.id_control = ?
                  AND cd.activo = 1
                  AND UPPER(TRIM(cm.cantera)) =
                      UPPER(TRIM(?))

                ORDER BY cm.material
                """;

        List<String> lista =
                new ArrayList<>();

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

            ps.setString(
                    2,
                    cantera.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            rs.getString("material")
                    );
                }
            }
        }

        return lista;
    }

    public static List<String> obtenerDestinos(
            int idControl
    ) throws Exception {

        if (idControl <= 0) {
            throw new Exception(
                    "El Control Diario seleccionado no es válido."
            );
        }

        String sql =
                """
                SELECT DISTINCT
                    dt.destino_sector

                FROM control_diario cd

                INNER JOIN proyectos p
                    ON p.id_proyecto = cd.id_proyecto

                INNER JOIN catalogo_destino_tarifa dt
                    ON dt.id_empresa = p.id_empresa
                   AND dt.activo = 1

                WHERE cd.id_control = ?
                  AND cd.activo = 1

                ORDER BY dt.destino_sector
                """;

        List<String> lista =
                new ArrayList<>();

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

                while (rs.next()) {

                    lista.add(
                            rs.getString("destino_sector")
                    );
                }
            }
        }

        return lista;
    }

    public static TarifaOperacion obtenerTarifaActiva(
        int idControl,
        String cantera,
        String material,
        String destinoSector
) throws Exception {

    validarTexto(cantera, "la cantera");
    validarTexto(material, "el material");
    validarTexto(destinoSector, "el destino o sector");

    if (idControl <= 0) {
        throw new Exception(
                "El Control Diario seleccionado no es válido."
        );
    }

    String sql =
            """
            SELECT
                cm.id_tarifa AS id_tarifa_material,
                dt.id_destino_tarifa AS id_tarifa_transporte,
                cm.cantera,
                cm.material,
                dt.destino_sector,
                cm.costo_unitario_material,
                dt.costo_unitario_transporte

            FROM control_diario cd

            INNER JOIN proyectos p
                ON p.id_proyecto = cd.id_proyecto

            INNER JOIN catalogo_cantera_material cm
                ON cm.id_empresa = p.id_empresa
               AND cm.activo = 1
               AND UPPER(TRIM(cm.cantera)) =
                   UPPER(TRIM(?))
               AND UPPER(TRIM(cm.material)) =
                   UPPER(TRIM(?))

            INNER JOIN catalogo_destino_tarifa dt
                ON dt.id_empresa = p.id_empresa
               AND dt.activo = 1
               AND UPPER(TRIM(dt.destino_sector)) =
                   UPPER(TRIM(?))

            WHERE cd.id_control = ?
              AND cd.activo = 1

            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
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
                idControl
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró una tarifa activa para la empresa "
                                + "del proyecto, cantera, material y destino "
                                + "seleccionados."
                );
            }

            return new TarifaOperacion(
                    rs.getInt(
                            "id_tarifa_material"
                    ),
                    rs.getInt(
                            "id_tarifa_transporte"
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
        double costoUnitarioMaterial
) throws Exception {

    validarTexto(
            cantera,
            "la cantera"
    );

    validarTexto(
            material,
            "el material"
    );

    if (costoUnitarioMaterial < 0) {

        throw new Exception(
                "El costo unitario del material no puede ser negativo."
        );
    }
}

    private static void validarDuplicado(
        String cantera,
        String material,
        int idExcluir
) throws Exception {

    String sql =
            """
            SELECT id_tarifa

            FROM catalogo_cantera_material

            WHERE id_empresa = ?
              AND UPPER(TRIM(cantera)) =
                  UPPER(TRIM(?))
              AND UPPER(TRIM(material)) =
                  UPPER(TRIM(?))
              AND id_tarifa <> ?

            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        Usuario usuarioActual =
                SesionUsuario.getUsuarioActual();

        if (
                usuarioActual == null
                || usuarioActual.getIdEmpresa() == null
        ) {

            throw new Exception(
                    "No se pudo determinar la empresa del usuario."
            );
        }

        ps.setInt(
                1,
                usuarioActual.getIdEmpresa()
        );

        ps.setString(
                2,
                cantera.trim()
        );

        ps.setString(
                3,
                material.trim()
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
                        "Ya existe una tarifa registrada para esa cantera y material."
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