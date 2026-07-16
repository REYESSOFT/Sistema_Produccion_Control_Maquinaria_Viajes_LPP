import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MigradorCatalogosMaquinaria {

    public record ResultadoCatalogos(
            int tiposCreados,
            int entidadesCreadas,
            int tiposEncontrados,
            int entidadesEncontradas
    ) {
    }

    public static ResultadoCatalogos importar(
            List<LectorTXTMaquinaria.FilaMaquinaria> filas
    ) throws Exception {

        if (filas == null || filas.isEmpty()) {

            throw new Exception(
                    "No existen filas válidas para procesar."
            );
        }

        Connection conexion = null;

        try {

            conexion =
                    ConexionDB.obtenerConexion();

            conexion.setAutoCommit(false);

            Set<String> tiposProcesados =
                    new HashSet<>();

            Set<String> entidadesProcesadas =
                    new HashSet<>();

            int tiposCreados = 0;
            int entidadesCreadas = 0;
            int tiposEncontrados = 0;
            int entidadesEncontradas = 0;

            for (
                    LectorTXTMaquinaria.FilaMaquinaria fila
                    : filas
            ) {

                String tipo =
                        limpiar(fila.tipoMaquinaria());

                if (
                    !tipo.isEmpty()
                    && tiposProcesados.add(
                            tipo.toUpperCase()
                    )
                ) {

                    if (
                        insertarTipoSiNoExiste(
                                conexion,
                                tipo
                        )
                    ) {

                        tiposCreados++;

                    } else {

                        tiposEncontrados++;
                    }
                }

                String proveedor =
                        limpiar(fila.proveedor());

                if (
                    esEntidadValida(proveedor)
                    && entidadesProcesadas.add(
                            proveedor.toUpperCase()
                    )
                ) {

                    if (
                        insertarEntidadSiNoExiste(
                                conexion,
                                proveedor
                        )
                    ) {

                        entidadesCreadas++;

                    } else {

                        entidadesEncontradas++;
                    }
                }

                String propietario =
                        limpiar(fila.propietario());

                if (
                    esEntidadValida(propietario)
                    && entidadesProcesadas.add(
                            propietario.toUpperCase()
                    )
                ) {

                    if (
                        insertarEntidadSiNoExiste(
                                conexion,
                                propietario
                        )
                    ) {

                        entidadesCreadas++;

                    } else {

                        entidadesEncontradas++;
                    }
                }
            }

            conexion.commit();

            return new ResultadoCatalogos(
                    tiposCreados,
                    entidadesCreadas,
                    tiposEncontrados,
                    entidadesEncontradas
            );

        } catch (Exception e) {

            if (conexion != null) {

                try {

                    conexion.rollback();

                } catch (Exception rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conexion != null) {

                try {

                    conexion.setAutoCommit(true);
                    conexion.close();

                } catch (Exception cierreError) {

                    cierreError.printStackTrace();
                }
            }
        }
    }

    private static boolean insertarTipoSiNoExiste(
            Connection conexion,
            String nombre
    ) throws Exception {

        String sql = """
                INSERT IGNORE INTO tipos_maquinaria (
                    nombre,
                    descripcion,
                    estado
                )
                VALUES (
                    ?,
                    'Creado mediante importación de maquinaria',
                    'ACTIVO'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );

            return ps.executeUpdate() > 0;
        }
    }

    private static boolean insertarEntidadSiNoExiste(
            Connection conexion,
            String nombre
    ) throws Exception {

        String sql = """
                INSERT IGNORE INTO entidades_maquinaria (
                    nombre,
                    tipo_entidad,
                    estado,
                    observaciones
                )
                VALUES (
                    ?,
                    'OTRO',
                    'ACTIVO',
                    'Creado mediante importación de maquinaria'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );

            return ps.executeUpdate() > 0;
        }
    }

    private static boolean esEntidadValida(
            String nombre
    ) {

        return nombre != null
                && !nombre.isBlank()
                && !nombre.equalsIgnoreCase(
                        "ALQUILADO"
                );
    }

    private static String limpiar(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        return texto.trim();
    }
}
