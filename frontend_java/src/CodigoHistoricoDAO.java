import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CodigoHistoricoDAO {

    public record ItemCatalogo(
            int id,
            String nombre
    ) {
        @Override
        public String toString() {
            return nombre;
        }
    }

    public record MaquinariaItem(
            int idMaquinaria,
            String codigo,
            String descripcion
    ) {
        @Override
        public String toString() {

            String codigoMostrar =
                    codigo == null
                            || codigo.isBlank()
                            ? "SIN CÓDIGO"
                            : codigo;

            return codigoMostrar
                    + " - "
                    + descripcion;
        }
    }

    public record CodigoHistoricoResumen(
        int idCodigoHistorico,
        String proveedor,
        String codigoAnterior,
        String codigoActual,
        String descripcion,
        double costoHora,
        String estadoVinculacion,
        String observaciones
) {
}

    public record CodigoHistoricoDetalle(
        int idCodigoHistorico,
        Integer idMaquinaria,
        String maquinariaActual,
        Integer idProveedorOriginal,
        String proveedorOriginal,
        String codigoAnterior,
        String codigoActualOrigen,
        String descripcionOriginal,
        double costoHoraOriginal,
        String estadoVinculacion,
        String observaciones,
        String fechaRegistro
) {
}

    public static List<ItemCatalogo>
            obtenerProveedores() throws Exception {

        String sql = """
                SELECT
                    id_entidad,
                    nombre
                FROM entidades_maquinaria
                WHERE estado = 'ACTIVO'
                ORDER BY nombre
                """;

        List<ItemCatalogo> proveedores =
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

                proveedores.add(
                        new ItemCatalogo(
                                rs.getInt("id_entidad"),
                                rs.getString("nombre")
                        )
                );
            }
        }

        return proveedores;
    }

    public static List<MaquinariaItem>
            obtenerMaquinariasActivas() throws Exception {

        String sql = """
                SELECT
                    id_maquinaria,
                    COALESCE(
                        NULLIF(codigo_actual, ''),
                        NULLIF(codigo_placa, ''),
                        NULLIF(codigo_interno, ''),
                        'SIN CÓDIGO'
                    ) AS codigo_mostrar,
                    descripcion
                FROM maquinaria
                WHERE activo = 1
                ORDER BY codigo_mostrar, descripcion
                """;

        List<MaquinariaItem> maquinarias =
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

                maquinarias.add(
                        new MaquinariaItem(
                                rs.getInt("id_maquinaria"),
                                rs.getString("codigo_mostrar"),
                                rs.getString("descripcion")
                        )
                );
            }
        }

        return maquinarias;
    }

    public static int insertar(
            Integer idMaquinaria,
            String codigoAnterior,
            Integer idProveedorOriginal,
            String codigoActualOrigen,
            String descripcionOriginal,
            double costoHoraOriginal,
            String observaciones
    ) throws Exception {

        if (
            codigoAnterior == null
            || codigoAnterior.isBlank()
        ) {

            throw new Exception(
                    "El código anterior es obligatorio."
            );
        }

        String estadoVinculacion;

        if (idMaquinaria != null) {

            estadoVinculacion = "VINCULADO";

        } else if (
            codigoActualOrigen == null
            || codigoActualOrigen.isBlank()
        ) {

            estadoVinculacion = "PENDIENTE";

        } else {

            estadoVinculacion = "NO_ENCONTRADO";
        }

        validarDuplicado(
                codigoAnterior,
                idMaquinaria
        );

        String sql = """
                INSERT INTO maquinaria_codigos_historicos (
                    id_maquinaria,
                    codigo_anterior,
                    id_proveedor_original,
                    codigo_actual_origen,
                    descripcion_original,
                    costo_hora_original,
                    estado_vinculacion,
                    activo,
                    observaciones
                )
                VALUES (
                    ?, ?, ?, ?, ?, ?, ?, 1, ?
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

            asignarEntero(
                    ps,
                    1,
                    idMaquinaria
            );

            ps.setString(
                    2,
                    codigoAnterior.trim()
            );

            asignarEntero(
                    ps,
                    3,
                    idProveedorOriginal
            );

            asignarTexto(
                    ps,
                    4,
                    codigoActualOrigen
            );

            asignarTexto(
                    ps,
                    5,
                    descripcionOriginal
            );

            ps.setDouble(
                    6,
                    costoHoraOriginal
            );

            ps.setString(
                    7,
                    estadoVinculacion
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
                        "No fue posible guardar "
                                + "el código histórico."
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
                "El código fue guardado, pero no se pudo "
                        + "obtener su identificador."
        );
    }

    private static void validarDuplicado(
            String codigoAnterior,
            Integer idMaquinaria
    ) throws Exception {

        String sql = """
                SELECT id_codigo_historico
                FROM maquinaria_codigos_historicos
                WHERE activo = 1
                  AND UPPER(TRIM(codigo_anterior)) =
                      UPPER(TRIM(?))
                  AND (
                        (id_maquinaria = ?)
                        OR (
                            id_maquinaria IS NULL
                            AND ? IS NULL
                        )
                  )
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
                    codigoAnterior.trim()
            );

            asignarEntero(
                    ps,
                    2,
                    idMaquinaria
            );

            asignarEntero(
                    ps,
                    3,
                    idMaquinaria
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    throw new Exception(
                            "Ya existe un registro activo "
                                    + "con ese código anterior."
                    );
                }
            }
        }
    }


    public static List<CodigoHistoricoResumen>
        obtenerActivos() throws Exception {

    String sql = """
            SELECT
                h.id_codigo_historico,
                COALESCE(e.nombre, '') AS proveedor,
                h.codigo_anterior,
                COALESCE(h.codigo_actual_origen, '') AS codigo_actual,
                COALESCE(h.descripcion_original, '') AS descripcion,
                COALESCE(h.costo_hora_original, 0) AS costo_hora,
                h.estado_vinculacion,
                COALESCE(h.observaciones, '') AS observaciones
            FROM maquinaria_codigos_historicos h
            LEFT JOIN entidades_maquinaria e
                ON e.id_entidad = h.id_proveedor_original
            WHERE h.activo = 1
            ORDER BY h.id_codigo_historico DESC
            """;

    List<CodigoHistoricoResumen> registros =
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

            registros.add(
                    new CodigoHistoricoResumen(
                            rs.getInt(
                                    "id_codigo_historico"
                            ),
                            rs.getString("proveedor"),
                            rs.getString("codigo_anterior"),
                            rs.getString("codigo_actual"),
                            rs.getString("descripcion"),
                            rs.getDouble("costo_hora"),
                            rs.getString(
                                    "estado_vinculacion"
                            ),
                            rs.getString("observaciones")
                    )
            );
        }
    }

    return registros;
}


    public static CodigoHistoricoDetalle obtenerPorId(
        int idCodigoHistorico
) throws Exception {

    String sql = """
            SELECT
                h.id_codigo_historico,
                h.id_maquinaria,
                h.id_proveedor_original,

                COALESCE(
                    CONCAT(
                        COALESCE(
                            NULLIF(m.codigo_actual, ''),
                            NULLIF(m.codigo_placa, ''),
                            NULLIF(m.codigo_interno, ''),
                            'SIN CÓDIGO'
                        ),
                        ' - ',
                        m.descripcion
                    ),
                    'Sin maquinaria vinculada'
                ) AS maquinaria_actual,

                COALESCE(
                    proveedor.nombre,
                    'Sin asignar'
                ) AS proveedor_original,

                h.codigo_anterior,

                COALESCE(
                    h.codigo_actual_origen,
                    ''
                ) AS codigo_actual_origen,

                COALESCE(
                    h.descripcion_original,
                    ''
                ) AS descripcion_original,

                COALESCE(
                    h.costo_hora_original,
                    0
                ) AS costo_hora_original,

                h.estado_vinculacion,

                COALESCE(
                    h.observaciones,
                    ''
                ) AS observaciones,

                DATE_FORMAT(
                    h.fecha_registro,
                    '%d/%m/%Y %H:%i'
                ) AS fecha_registro

            FROM maquinaria_codigos_historicos h

            LEFT JOIN maquinaria m
                ON m.id_maquinaria =
                   h.id_maquinaria

            LEFT JOIN entidades_maquinaria proveedor
                ON proveedor.id_entidad =
                   h.id_proveedor_original

            WHERE h.id_codigo_historico = ?
              AND h.activo = 1

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
                idCodigoHistorico
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró el código histórico."
                );
            }

            Object maquinariaObjeto =
                    rs.getObject(
                            "id_maquinaria"
                    );

            Integer idMaquinaria =
                    maquinariaObjeto == null
                            ? null
                            : rs.getInt(
                                    "id_maquinaria"
                            );

            Object proveedorObjeto =
                    rs.getObject(
                            "id_proveedor_original"
                    );

            Integer idProveedor =
                    proveedorObjeto == null
                            ? null
                            : rs.getInt(
                                    "id_proveedor_original"
                            );

            return new CodigoHistoricoDetalle(
                    rs.getInt(
                            "id_codigo_historico"
                    ),
                    idMaquinaria,
                    rs.getString(
                            "maquinaria_actual"
                    ),
                    idProveedor,
                    rs.getString(
                            "proveedor_original"
                    ),
                    rs.getString(
                            "codigo_anterior"
                    ),
                    rs.getString(
                            "codigo_actual_origen"
                    ),
                    rs.getString(
                            "descripcion_original"
                    ),
                    rs.getDouble(
                            "costo_hora_original"
                    ),
                    rs.getString(
                            "estado_vinculacion"
                    ),
                    rs.getString(
                            "observaciones"
                    ),
                    rs.getString(
                            "fecha_registro"
                    )
            );
        }
    }
}

    public static void actualizar(
        int idCodigoHistorico,
        Integer idMaquinaria,
        String codigoAnterior,
        Integer idProveedorOriginal,
        String codigoActualOrigen,
        String descripcionOriginal,
        double costoHoraOriginal,
        String observaciones
) throws Exception {

    if (
        codigoAnterior == null
        || codigoAnterior.isBlank()
    ) {

        throw new Exception(
                "El código anterior es obligatorio."
        );
    }

    String estadoVinculacion;

    if (idMaquinaria != null) {

        estadoVinculacion =
                "VINCULADO";

    } else if (
        codigoActualOrigen == null
        || codigoActualOrigen.isBlank()
    ) {

        estadoVinculacion =
                "PENDIENTE";

    } else {

        estadoVinculacion =
                "NO_ENCONTRADO";
    }

    String sql = """
            UPDATE maquinaria_codigos_historicos
            SET
                id_maquinaria = ?,
                codigo_anterior = ?,
                id_proveedor_original = ?,
                codigo_actual_origen = ?,
                descripcion_original = ?,
                costo_hora_original = ?,
                estado_vinculacion = ?,
                observaciones = ?
            WHERE id_codigo_historico = ?
              AND activo = 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        asignarEntero(
                ps,
                1,
                idMaquinaria
        );

        ps.setString(
                2,
                codigoAnterior.trim()
        );

        asignarEntero(
                ps,
                3,
                idProveedorOriginal
        );

        asignarTexto(
                ps,
                4,
                codigoActualOrigen
        );

        asignarTexto(
                ps,
                5,
                descripcionOriginal
        );

        ps.setDouble(
                6,
                costoHoraOriginal
        );

        ps.setString(
                7,
                estadoVinculacion
        );

        asignarTexto(
                ps,
                8,
                observaciones
        );

        ps.setInt(
                9,
                idCodigoHistorico
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar "
                            + "el código histórico."
            );
        }
    }
}
    
    private static void validarDuplicadoEdicion(
        int idCodigoHistorico,
        String codigoAnterior,
        Integer idMaquinaria
) throws Exception {

    String sql = """
            SELECT id_codigo_historico
            FROM maquinaria_codigos_historicos
            WHERE activo = 1
              AND id_codigo_historico <> ?
              AND UPPER(TRIM(codigo_anterior)) =
                  UPPER(TRIM(?))
              AND (
                    id_maquinaria = ?
                    OR (
                        id_maquinaria IS NULL
                        AND ? IS NULL
                    )
              )
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
                idCodigoHistorico
        );

        ps.setString(
                2,
                codigoAnterior.trim()
        );

        asignarEntero(
                ps,
                3,
                idMaquinaria
        );

        asignarEntero(
                ps,
                4,
                idMaquinaria
        );

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                throw new Exception(
                        "Ya existe otro registro activo "
                                + "con ese código anterior."
                );
            }
        }
    }
}

    public static void eliminar(
        int idCodigoHistorico
) throws Exception {

    String sql = """
            UPDATE maquinaria_codigos_historicos
            SET activo = 0
            WHERE id_codigo_historico = ?
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
                idCodigoHistorico
        );

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "El código histórico no existe "
                            + "o ya fue eliminado."
            );
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
                    Types.VARCHAR
            );

        } else {

            ps.setString(
                    parametro,
                    valor.trim()
            );
        }
    }

    private static void asignarEntero(
            PreparedStatement ps,
            int parametro,
            Integer valor
    ) throws Exception {

        if (valor == null) {

            ps.setNull(
                    parametro,
                    Types.INTEGER
            );

        } else {

            ps.setInt(
                    parametro,
                    valor
            );
        }
    }
    
}