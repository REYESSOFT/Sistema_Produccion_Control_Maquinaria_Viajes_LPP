import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Types;

public class MaquinariaDAO {

    public record MaquinariaResumen(
            int idMaquinaria,
            String codigo,
            String descripcion,
            String tipoMaquinaria,
            String proveedor,
            String propietario,
            String estadoOperativo,
            double costoHoraProveedor
    ) {
    }



    public record CatalogoItem(
            int id,
            String nombre
    ) {
    }

    public record MaquinariaDetalle(
        int idMaquinaria,
        String codigoInterno,
        String codigoActual,
        String codigoPlaca,
        String descripcion,
        int idTipoMaquinaria,
        String tipoMaquinaria,
        String modelo,
        String serieMaquina,
        String serieActual,
        Double horometroActual,
        boolean horometroConfirmado,
        Integer idProveedor,
        String proveedor,
        Integer idPropietario,
        String propietario,
        String tipoPropiedad,
        String estadoOperativo,
        double costoHoraProveedor,
        double precioHoraCliente,
        String observaciones,
        boolean activo
) {
}

    public static List<MaquinariaResumen> buscar(
            String estado,
            String tipoMaquinaria,
            String proveedor,
            String codigo
    ) throws Exception {

        List<MaquinariaResumen> resultados =
                new ArrayList<>();

        StringBuilder sql =
                new StringBuilder(
                        """
                        SELECT
                            m.id_maquinaria,

                            COALESCE(
                                NULLIF(m.codigo_actual, ''),
                                NULLIF(m.codigo_interno, ''),
                                NULLIF(m.codigo_placa, ''),
                                ''
                            ) AS codigo_mostrar,

                            m.descripcion,

                            tm.nombre AS tipo_maquinaria,

                            COALESCE(
                                proveedor.nombre,
                                ''
                            ) AS proveedor,

                            COALESCE(
                                propietario.nombre,
                                ''
                            ) AS propietario,

                            m.estado_operativo,

                            COALESCE(
                                m.costo_hora_proveedor,
                                0
                            ) AS costo_hora_proveedor

                        FROM maquinaria m

                        INNER JOIN tipos_maquinaria tm
                            ON tm.id_tipo_maquinaria =
                               m.id_tipo_maquinaria

                        LEFT JOIN entidades_maquinaria proveedor
                            ON proveedor.id_entidad =
                               m.id_proveedor

                        LEFT JOIN entidades_maquinaria propietario
                            ON propietario.id_entidad =
                               m.id_propietario

                        WHERE m.activo = 1
                        """
                );

        List<Object> parametros =
                new ArrayList<>();

        if (
            estado != null
            && !estado.isBlank()
            && !estado.equalsIgnoreCase("Todos")
        ) {

            sql.append(
                    " AND m.estado_operativo = ? "
            );

            parametros.add(estado);
        }

        if (
            tipoMaquinaria != null
            && !tipoMaquinaria.isBlank()
            && !tipoMaquinaria.equalsIgnoreCase("Todos")
        ) {

            sql.append(
                    " AND tm.nombre = ? "
            );

            parametros.add(tipoMaquinaria);
        }

        if (
            proveedor != null
            && !proveedor.isBlank()
        ) {

            sql.append(
                    """
                     AND proveedor.nombre
                         LIKE ?
                    """
            );

            parametros.add(
                    "%" + proveedor.trim() + "%"
            );
        }

        if (
            codigo != null
            && !codigo.isBlank()
        ) {

            sql.append(
                    """
                     AND (
                            m.codigo_actual LIKE ?
                         OR m.codigo_interno LIKE ?
                         OR m.codigo_placa LIKE ?
                         OR m.descripcion LIKE ?
                     )
                    """
            );

            String criterio =
                    "%" + codigo.trim() + "%";

            parametros.add(criterio);
            parametros.add(criterio);
            parametros.add(criterio);
            parametros.add(criterio);
        }

        sql.append(
                """
                 ORDER BY
                    tm.nombre,
                    codigo_mostrar,
                    m.descripcion
                """
        );

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

                    resultados.add(
                            new MaquinariaResumen(
                                    rs.getInt(
                                            "id_maquinaria"
                                    ),
                                    rs.getString(
                                            "codigo_mostrar"
                                    ),
                                    rs.getString(
                                            "descripcion"
                                    ),
                                    rs.getString(
                                            "tipo_maquinaria"
                                    ),
                                    rs.getString(
                                            "proveedor"
                                    ),
                                    rs.getString(
                                            "propietario"
                                    ),
                                    rs.getString(
                                            "estado_operativo"
                                    ),
                                    rs.getDouble(
                                            "costo_hora_proveedor"
                                    )
                            )
                    );
                }
            }
        }

        return resultados;
    }

    public static List<MaquinariaResumen>
            obtenerTodas() throws Exception {

        return buscar(
                "Todos",
                "Todos",
                "",
                ""
        );
    }

    public static MaquinariaDetalle obtenerPorId(
        int idMaquinaria
) throws Exception {

    String sql = """
            SELECT
                m.id_maquinaria,
                COALESCE(m.codigo_interno, '') AS codigo_interno,
                COALESCE(m.codigo_actual, '') AS codigo_actual,
                COALESCE(m.codigo_placa, '') AS codigo_placa,
                m.descripcion,
                m.id_tipo_maquinaria,
                tm.nombre AS tipo_maquinaria,
                COALESCE(m.modelo, '') AS modelo,
                COALESCE(m.serie_maquina, '') AS serie_maquina,
                COALESCE(m.serie_actual, '') AS serie_actual,
                m.horometro_actual,
                m.horometro_confirmado,
                m.id_proveedor,
                COALESCE(proveedor.nombre, '') AS proveedor,
                m.id_propietario,
                COALESCE(propietario.nombre, '') AS propietario,
                m.tipo_propiedad,
                m.estado_operativo,
                COALESCE(m.costo_hora_proveedor, 0)
                    AS costo_hora_proveedor,
                COALESCE(m.precio_hora_cliente, 0)
                    AS precio_hora_cliente,
                COALESCE(m.observaciones, '') AS observaciones,
                m.activo
            FROM maquinaria m
            INNER JOIN tipos_maquinaria tm
                ON tm.id_tipo_maquinaria =
                   m.id_tipo_maquinaria
            LEFT JOIN entidades_maquinaria proveedor
                ON proveedor.id_entidad =
                   m.id_proveedor
            LEFT JOIN entidades_maquinaria propietario
                ON propietario.id_entidad =
                   m.id_propietario
            WHERE m.id_maquinaria = ?
            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(1, idMaquinaria);

        try (
                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (!rs.next()) {

                throw new Exception(
                        "No se encontró la maquinaria."
                );
            }

            Object horometroObjeto =
                    rs.getObject("horometro_actual");

            Double horometro =
                    horometroObjeto == null
                            ? null
                            : rs.getDouble(
                                    "horometro_actual"
                            );

            Object proveedorObjeto =
                    rs.getObject("id_proveedor");

            Integer idProveedor =
                    proveedorObjeto == null
                            ? null
                            : rs.getInt("id_proveedor");

            Object propietarioObjeto =
                    rs.getObject("id_propietario");

            Integer idPropietario =
                    propietarioObjeto == null
                            ? null
                            : rs.getInt("id_propietario");

            return new MaquinariaDetalle(
                    rs.getInt("id_maquinaria"),
                    rs.getString("codigo_interno"),
                    rs.getString("codigo_actual"),
                    rs.getString("codigo_placa"),
                    rs.getString("descripcion"),
                    rs.getInt("id_tipo_maquinaria"),
                    rs.getString("tipo_maquinaria"),
                    rs.getString("modelo"),
                    rs.getString("serie_maquina"),
                    rs.getString("serie_actual"),
                    horometro,
                    rs.getBoolean(
                            "horometro_confirmado"
                    ),
                    idProveedor,
                    rs.getString("proveedor"),
                    idPropietario,
                    rs.getString("propietario"),
                    rs.getString("tipo_propiedad"),
                    rs.getString("estado_operativo"),
                    rs.getDouble(
                            "costo_hora_proveedor"
                    ),
                    rs.getDouble(
                            "precio_hora_cliente"
                    ),
                    rs.getString("observaciones"),
                    rs.getBoolean("activo")
            );
        }
    }
}



    public static List<CatalogoItem>
        obtenerTiposMaquinaria() throws Exception {

    List<CatalogoItem> lista =
            new ArrayList<>();

    String sql = """
            SELECT id_tipo_maquinaria, nombre
            FROM tipos_maquinaria
            WHERE estado = 'ACTIVO'
            ORDER BY nombre
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

            lista.add(
                    new CatalogoItem(
                            rs.getInt("id_tipo_maquinaria"),
                            rs.getString("nombre")
                    )
            );
        }
    }

    return lista;
}

public static List<CatalogoItem>
        obtenerEntidades() throws Exception {

    List<CatalogoItem> lista =
            new ArrayList<>();

    String sql = """
            SELECT id_entidad, nombre
            FROM entidades_maquinaria
            WHERE estado = 'ACTIVO'
            ORDER BY nombre
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

            lista.add(
                    new CatalogoItem(
                            rs.getInt("id_entidad"),
                            rs.getString("nombre")
                    )
            );
        }
    }

    return lista;
}

public static int insertar(
        String codigoInterno,
        String codigoActual,
        String placa,
        String descripcion,
        int idTipoMaquinaria,
        String modelo,
        String serieMaquina,
        String serieActual,
        Double horometroActual,
        boolean horometroConfirmado,
        Integer idProveedor,
        Integer idPropietario,
        String tipoPropiedad,
        String estadoOperativo,
        double costoHoraProveedor,
        double precioHoraCliente,
        String observaciones
) throws Exception {

    String sql = """
            INSERT INTO maquinaria (
                codigo_interno,
                codigo_actual,
                codigo_placa,
                descripcion,
                id_tipo_maquinaria,
                modelo,
                serie_maquina,
                serie_actual,
                horometro_actual,
                horometro_confirmado,
                id_proveedor,
                id_propietario,
                tipo_propiedad,
                estado_operativo,
                costo_hora_proveedor,
                precio_hora_cliente,
                observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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

        asignarTexto(ps, 1, codigoInterno);
        asignarTexto(ps, 2, codigoActual);
        asignarTexto(ps, 3, placa);

        ps.setString(4, descripcion);
        ps.setInt(5, idTipoMaquinaria);

        asignarTexto(ps, 6, modelo);
        asignarTexto(ps, 7, serieMaquina);
        asignarTexto(ps, 8, serieActual);

        if (horometroActual == null) {
            ps.setNull(9, Types.DECIMAL);
        } else {
            ps.setDouble(9, horometroActual);
        }

        ps.setBoolean(10, horometroConfirmado);

        if (idProveedor == null) {
            ps.setNull(11, Types.INTEGER);
        } else {
            ps.setInt(11, idProveedor);
        }

        if (idPropietario == null) {
            ps.setNull(12, Types.INTEGER);
        } else {
            ps.setInt(12, idPropietario);
        }

        ps.setString(13, tipoPropiedad);
        ps.setString(14, estadoOperativo);
        ps.setDouble(15, costoHoraProveedor);
        ps.setDouble(16, precioHoraCliente);

        asignarTexto(ps, 17, observaciones);

        ps.executeUpdate();

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
            "No fue posible obtener el ID de la maquinaria."
    );
}



public static void actualizar(
        int idMaquinaria,
        String codigoInterno,
        String codigoActual,
        String codigoPlaca,
        String descripcion,
        int idTipoMaquinaria,
        String modelo,
        String serieMaquina,
        String serieActual,
        Double horometroActual,
        boolean horometroConfirmado,
        Integer idProveedor,
        Integer idPropietario,
        String tipoPropiedad,
        String estadoOperativo,
        double costoHoraProveedor,
        double precioHoraCliente,
        String observaciones
) throws Exception {

    String sql = """
            UPDATE maquinaria
            SET
                codigo_interno = ?,
                codigo_actual = ?,
                codigo_placa = ?,
                descripcion = ?,
                id_tipo_maquinaria = ?,
                modelo = ?,
                serie_maquina = ?,
                serie_actual = ?,
                horometro_actual = ?,
                horometro_confirmado = ?,
                id_proveedor = ?,
                id_propietario = ?,
                tipo_propiedad = ?,
                estado_operativo = ?,
                costo_hora_proveedor = ?,
                precio_hora_cliente = ?,
                observaciones = ?
            WHERE id_maquinaria = ?
              AND activo = 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        asignarTexto(ps, 1, codigoInterno);
        asignarTexto(ps, 2, codigoActual);
        asignarTexto(ps, 3, codigoPlaca);

        ps.setString(4, descripcion);
        ps.setInt(5, idTipoMaquinaria);

        asignarTexto(ps, 6, modelo);
        asignarTexto(ps, 7, serieMaquina);
        asignarTexto(ps, 8, serieActual);

        if (horometroActual == null) {
            ps.setNull(9, Types.DECIMAL);
        } else {
            ps.setDouble(9, horometroActual);
        }

        ps.setBoolean(10, horometroConfirmado);

        if (idProveedor == null) {
            ps.setNull(11, Types.INTEGER);
        } else {
            ps.setInt(11, idProveedor);
        }

        if (idPropietario == null) {
            ps.setNull(12, Types.INTEGER);
        } else {
            ps.setInt(12, idPropietario);
        }

        ps.setString(13, tipoPropiedad);
        ps.setString(14, estadoOperativo);
        ps.setDouble(15, costoHoraProveedor);
        ps.setDouble(16, precioHoraCliente);

        asignarTexto(ps, 17, observaciones);

        ps.setInt(18, idMaquinaria);

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "No fue posible actualizar la maquinaria."
            );
        }
    }
}


public static void desactivar(
        int idMaquinaria
) throws Exception {

    String sql = """
            UPDATE maquinaria
            SET
                activo = 0,
                estado_operativo = 'INACTIVA'
            WHERE id_maquinaria = ?
              AND activo = 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(1, idMaquinaria);

        int filas =
                ps.executeUpdate();

        if (filas == 0) {

            throw new Exception(
                    "La maquinaria no existe "
                            + "o ya está desactivada."
            );
        }
    }
}


private static void asignarTexto(
        PreparedStatement ps,
        int parametro,
        String texto
) throws Exception {

    if (
        texto == null
        || texto.trim().isEmpty()
    ) {
        ps.setNull(parametro, Types.VARCHAR);
    } else {
        ps.setString(parametro, texto.trim());
    }
}

}