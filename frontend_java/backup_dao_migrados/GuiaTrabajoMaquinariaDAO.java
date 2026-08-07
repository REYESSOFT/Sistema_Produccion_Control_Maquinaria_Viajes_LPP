import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.List;

public class GuiaTrabajoMaquinariaDAO {

    public record Turno(
            String nombre,
            String horaInicio,
            String horaFin,
            double totalHoras
    ) {
    }

    public static int guardarNuevaGuia(
            String numeroGuia,
            String fecha,
            String cliente,
            String tipoMaquina,
            String numeroMaquina,
            String operador,
            double totalHoras,
            String sector,
            String trabajoRealizar,
            boolean chequeoEngrase,
            String horaInicioGeneral,
            String horaFinGeneral,
            Double horometroInicial,
            Double horometroFinal,
            Double horometroRecorrido,
            String combustible,
            String recibiConforme,
            String observaciones,
            List<Turno> turnos
    ) throws Exception {

        Connection conexion = null;

        try {

            conexion = ConexionDB.obtenerConexion();
            conexion.setAutoCommit(false);

            int idGuia = insertarCabecera(
                    conexion,
                    numeroGuia,
                    fecha,
                    cliente,
                    tipoMaquina,
                    numeroMaquina,
                    operador,
                    totalHoras,
                    sector,
                    trabajoRealizar,
                    chequeoEngrase,
                    horaInicioGeneral,
                    horaFinGeneral,
                    horometroInicial,
                    horometroFinal,
                    horometroRecorrido,
                    combustible,
                    recibiConforme,
                    observaciones
            );

            insertarTurnos(
                    conexion,
                    idGuia,
                    turnos
            );

            conexion.commit();

            return idGuia;

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
                    conexion.close();
                } catch (Exception cierreError) {
                    cierreError.printStackTrace();
                }
            }
        }
    }


    public static void actualizarGuia(
        int idGuia,
        String numeroGuia,
        String fecha,
        String cliente,
        String tipoMaquina,
        String numeroMaquina,
        String operador,
        double totalHoras,
        String sector,
        String trabajoRealizar,
        boolean chequeoEngrase,
        String horaInicioGeneral,
        String horaFinGeneral,
        Double horometroInicial,
        Double horometroFinal,
        Double horometroRecorrido,
        String combustible,
        String recibiConforme,
        String observaciones,
        List<Turno> turnos
) throws Exception {

    Connection conexion = null;

    try {

        conexion = ConexionDB.obtenerConexion();
        conexion.setAutoCommit(false);

        actualizarCabecera(
                conexion,
                idGuia,
                numeroGuia,
                fecha,
                cliente,
                tipoMaquina,
                numeroMaquina,
                operador,
                totalHoras,
                sector,
                trabajoRealizar,
                chequeoEngrase,
                horaInicioGeneral,
                horaFinGeneral,
                horometroInicial,
                horometroFinal,
                horometroRecorrido,
                combustible,
                recibiConforme,
                observaciones
        );

        eliminarTurnos(
                conexion,
                idGuia
        );

        insertarTurnos(
                conexion,
                idGuia,
                turnos
        );

        conexion.commit();

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
                conexion.close();
            } catch (Exception cierreError) {
                cierreError.printStackTrace();
            }
        }
    }
}

    private static int insertarCabecera(
            Connection conexion,
            String numeroGuia,
            String fecha,
            String cliente,
            String tipoMaquina,
            String numeroMaquina,
            String operador,
            double totalHoras,
            String sector,
            String trabajoRealizar,
            boolean chequeoEngrase,
            String horaInicioGeneral,
            String horaFinGeneral,
            Double horometroInicial,
            Double horometroFinal,
            Double horometroRecorrido,
            String combustible,
            String recibiConforme,
            String observaciones
    ) throws Exception {

        String sql = """
                INSERT INTO guias (
                    id_empresa,
                    tipo_guia,
                    numero_guia,
                    fecha,
                    cliente,
                    equipo,
                    numero_maquina,
                    chofer_operador,
                    horas,
                    sector,
                    trabajo_realizar,
                    chequeo_engrase,
                    hora_inicio,
                    hora_fin,
                    horometro_inicial,
                    horometro_final,
                    horometro_recorrido,
                    combustible,
                    recibi_conforme,
                    observaciones,
                    estado
                )
                VALUES (
                    (
                        SELECT id_empresa
                        FROM empresas
                        WHERE nombre_empresa = 'EQUIPOS PRO'
                        LIMIT 1
                    ),
                    'Guía Trabajo Diario Maquinaria',
                    ?,
                    STR_TO_DATE(?, '%d/%m/%Y'),
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    'PENDIENTE'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(1, numeroGuia);
            ps.setString(2, fecha);
            ps.setString(3, cliente);
            ps.setString(4, tipoMaquina);
            ps.setString(5, numeroMaquina);
            ps.setString(6, operador);
            ps.setDouble(7, totalHoras);
            ps.setString(8, sector);
            ps.setString(9, trabajoRealizar);
            ps.setBoolean(10, chequeoEngrase);

            asignarHora(
                    ps,
                    11,
                    horaInicioGeneral
            );

            asignarHora(
                    ps,
                    12,
                    horaFinGeneral
            );

            asignarDecimal(
                    ps,
                    13,
                    horometroInicial
            );

            asignarDecimal(
                    ps,
                    14,
                    horometroFinal
            );

            asignarDecimal(
                    ps,
                    15,
                    horometroRecorrido
            );

            ps.setString(16, combustible);
            ps.setString(17, recibiConforme);
            ps.setString(18, observaciones);

            int filasInsertadas =
                    ps.executeUpdate();

            if (filasInsertadas == 0) {

                throw new Exception(
                        "No fue posible guardar la cabecera de la guía."
                );
            }

            try (
                    ResultSet claves =
                            ps.getGeneratedKeys()
            ) {

                if (!claves.next()) {

                    throw new Exception(
                            "No fue posible obtener el ID de la guía."
                    );
                }

                return claves.getInt(1);
            }
        }
    }



    private static void actualizarCabecera(
        Connection conexion,
        int idGuia,
        String numeroGuia,
        String fecha,
        String cliente,
        String tipoMaquina,
        String numeroMaquina,
        String operador,
        double totalHoras,
        String sector,
        String trabajoRealizar,
        boolean chequeoEngrase,
        String horaInicioGeneral,
        String horaFinGeneral,
        Double horometroInicial,
        Double horometroFinal,
        Double horometroRecorrido,
        String combustible,
        String recibiConforme,
        String observaciones
) throws Exception {

    String sql = """
            UPDATE guias
            SET
                numero_guia = ?,
                fecha = STR_TO_DATE(?, '%d/%m/%Y'),
                cliente = ?,
                equipo = ?,
                numero_maquina = ?,
                chofer_operador = ?,
                horas = ?,
                sector = ?,
                trabajo_realizar = ?,
                chequeo_engrase = ?,
                hora_inicio = ?,
                hora_fin = ?,
                horometro_inicial = ?,
                horometro_final = ?,
                horometro_recorrido = ?,
                combustible = ?,
                recibi_conforme = ?,
                observaciones = ?
            WHERE id_guia = ?
              AND tipo_guia = 'Guía Trabajo Diario Maquinaria'
              AND estado = 'PENDIENTE'
            """;

    try (
            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setString(1, numeroGuia);
        ps.setString(2, fecha);
        ps.setString(3, cliente);
        ps.setString(4, tipoMaquina);
        ps.setString(5, numeroMaquina);
        ps.setString(6, operador);
        ps.setDouble(7, totalHoras);
        ps.setString(8, sector);
        ps.setString(9, trabajoRealizar);
        ps.setBoolean(10, chequeoEngrase);

        asignarHora(
                ps,
                11,
                horaInicioGeneral
        );

        asignarHora(
                ps,
                12,
                horaFinGeneral
        );

        asignarDecimal(
                ps,
                13,
                horometroInicial
        );

        asignarDecimal(
                ps,
                14,
                horometroFinal
        );

        asignarDecimal(
                ps,
                15,
                horometroRecorrido
        );

        ps.setString(16, combustible);
        ps.setString(17, recibiConforme);
        ps.setString(18, observaciones);
        ps.setInt(19, idGuia);

        int filasActualizadas =
                ps.executeUpdate();

        if (filasActualizadas == 0) {

            throw new Exception(
                    "La guía no pudo actualizarse. "
                            + "Puede estar aprobada o ya no existir."
            );
        }
    }
}



private static void eliminarTurnos(
        Connection conexion,
        int idGuia
) throws Exception {

    String sql = """
            DELETE FROM trabajo_maquinaria_turnos
            WHERE id_guia = ?
            """;

    try (
            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setInt(
                1,
                idGuia
        );

        ps.executeUpdate();
    }
}


    private static void insertarTurnos(
            Connection conexion,
            int idGuia,
            List<Turno> turnos
    ) throws Exception {

        String sql = """
                INSERT INTO trabajo_maquinaria_turnos (
                    id_guia,
                    turno,
                    hora_inicio,
                    hora_fin,
                    total_horas
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            for (Turno turno : turnos) {

                ps.setInt(
                        1,
                        idGuia
                );

                ps.setString(
                        2,
                        turno.nombre()
                );

                asignarHora(
                        ps,
                        3,
                        turno.horaInicio()
                );

                asignarHora(
                        ps,
                        4,
                        turno.horaFin()
                );

                ps.setDouble(
                        5,
                        turno.totalHoras()
                );

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private static void asignarHora(
            PreparedStatement ps,
            int parametro,
            String hora
    ) throws Exception {

        if (
            hora == null
            || hora.isBlank()
        ) {

            ps.setNull(
                    parametro,
                    Types.TIME
            );

        } else {

            ps.setTime(
                    parametro,
                    Time.valueOf(
                            hora + ":00"
                    )
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
                    Types.DECIMAL
            );

        } else {

            ps.setDouble(
                    parametro,
                    valor
            );
        }
    }
}