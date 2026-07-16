import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetalleGuiaTrabajoMaquinariaDialog extends JDialog {

    private final String empresa;
    private final String numeroGuia;

    public DetalleGuiaTrabajoMaquinariaDialog(
            Window parent,
            String empresa,
            String numeroGuia
    ) {

        super(
                parent,
                "Detalle Guía Trabajo Diario Maquinaria N° "
                        + numeroGuia,
                ModalityType.APPLICATION_MODAL
        );

        this.empresa = empresa;
        this.numeroGuia = numeroGuia;

        setSize(1050, 760);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarDetalle();
    }

    private void cargarDetalle() {

        String sqlCabecera = """
                SELECT
                    g.id_guia,
                    e.nombre_empresa,
                    g.numero_guia,
                    DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                    COALESCE(g.cliente, '') AS cliente,
                    COALESCE(g.equipo, '') AS tipo_maquina,
                    COALESCE(g.numero_maquina, '') AS numero_maquina,
                    COALESCE(g.chofer_operador, '') AS operador,
                    COALESCE(g.sector, '') AS sector,
                    COALESCE(g.trabajo_realizar, '') AS trabajo_realizar,
                    COALESCE(g.chequeo_engrase, 0) AS chequeo_engrase,
                    TIME_FORMAT(g.hora_inicio, '%H:%i') AS hora_inicio,
                    TIME_FORMAT(g.hora_fin, '%H:%i') AS hora_fin,
                    COALESCE(g.horas, 0) AS total_horas,
                    g.horometro_inicial,
                    g.horometro_final,
                    g.horometro_recorrido,
                    COALESCE(g.combustible, '') AS combustible,
                    COALESCE(g.recibi_conforme, '') AS recibi_conforme,
                    COALESCE(g.observaciones, '') AS observaciones,
                    g.estado
                FROM guias g
                INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa
                WHERE e.nombre_empresa = ?
                  AND g.tipo_guia =
                      'Guía Trabajo Diario Maquinaria'
                  AND g.numero_guia = ?
                LIMIT 1
                """;

        String sqlTurnos = """
                SELECT
                    turno,
                    TIME_FORMAT(hora_inicio, '%H:%i')
                        AS hora_inicio,
                    TIME_FORMAT(hora_fin, '%H:%i')
                        AS hora_fin,
                    total_horas
                FROM trabajo_maquinaria_turnos
                WHERE id_guia = ?
                ORDER BY FIELD(
                    turno,
                    'MAÑANA',
                    'TARDE',
                    'NOCHE'
                )
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement psCabecera =
                        conexion.prepareStatement(sqlCabecera)
        ) {

            psCabecera.setString(1, empresa);
            psCabecera.setString(2, numeroGuia);

            try (
                    ResultSet cabecera =
                            psCabecera.executeQuery()
            ) {

                if (!cabecera.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se encontró la guía seleccionada.",
                            "Información",
                            JOptionPane.WARNING_MESSAGE
                    );

                    dispose();
                    return;
                }

                int idGuia =
                        cabecera.getInt("id_guia");

                JPanel panelPrincipal =
                        new JPanel(
                                new BorderLayout(10, 10)
                        );

                panelPrincipal.setBorder(
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                );

                panelPrincipal.add(
                        crearPanelCabecera(cabecera),
                        BorderLayout.NORTH
                );

                JPanel panelCentro =
                        new JPanel(
                                new GridLayout(
                                        2,
                                        1,
                                        10,
                                        10
                                )
                        );

                panelCentro.add(
                        crearPanelTurnos(
                                conexion,
                                idGuia,
                                sqlTurnos,
                                cabecera.getDouble(
                                        "total_horas"
                                )
                        )
                );

                panelCentro.add(
                        crearPanelEquipo(cabecera)
                );

                panelPrincipal.add(
                        panelCentro,
                        BorderLayout.CENTER
                );

                panelPrincipal.add(
                        crearPanelInferior(
                                cabecera.getString(
                                        "observaciones"
                                ),
                                cabecera.getString(
                                        "recibi_conforme"
                                )
                        ),
                        BorderLayout.SOUTH
                );

                setContentPane(panelPrincipal);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al mostrar el detalle:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
            dispose();
        }
    }

    private JPanel crearPanelCabecera(
            ResultSet cabecera
    ) throws Exception {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                4,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del trabajo"
                )
        );

        agregarDato(
                panel,
                "Empresa:",
                cabecera.getString("nombre_empresa")
        );

        agregarDato(
                panel,
                "N° Guía:",
                cabecera.getString("numero_guia")
        );

        agregarDato(
                panel,
                "Fecha:",
                cabecera.getString("fecha")
        );

        agregarDato(
                panel,
                "Estado:",
                cabecera.getString("estado")
        );

        agregarDato(
                panel,
                "N° Máquina:",
                cabecera.getString("numero_maquina")
        );

        agregarDato(
                panel,
                "Tipo de máquina:",
                cabecera.getString("tipo_maquina")
        );

        agregarDato(
                panel,
                "Cliente:",
                cabecera.getString("cliente")
        );

        agregarDato(
                panel,
                "Sector:",
                cabecera.getString("sector")
        );

        agregarDato(
                panel,
                "Operador:",
                cabecera.getString("operador")
        );

        agregarDato(
                panel,
                "Trabajo a realizar:",
                cabecera.getString("trabajo_realizar")
        );

        agregarDato(
                panel,
                "Hora inicio:",
                valorTexto(
                        cabecera.getString("hora_inicio")
                )
        );

        agregarDato(
                panel,
                "Hora fin:",
                valorTexto(
                        cabecera.getString("hora_fin")
                )
        );

        agregarDato(
                panel,
                "Chequeo de engrase:",
                cabecera.getBoolean("chequeo_engrase")
                        ? "Realizado"
                        : "No realizado"
        );

        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel crearPanelTurnos(
            Connection conexion,
            int idGuia,
            String sqlTurnos,
            double totalHorasGeneral
    ) throws Exception {

        String[] columnas = {
                "Turno",
                "Inicio",
                "Terminación",
                "Total"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        try (
                PreparedStatement statement =
                        conexion.prepareStatement(sqlTurnos)
        ) {

            statement.setInt(1, idGuia);

            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                while (resultado.next()) {

                    modelo.addRow(
                            new Object[]{
                                    resultado.getString(
                                            "turno"
                                    ),
                                    valorTexto(
                                            resultado.getString(
                                                    "hora_inicio"
                                            )
                                    ),
                                    valorTexto(
                                            resultado.getString(
                                                    "hora_fin"
                                            )
                                    ),
                                    convertirDecimalAHoras(
                                            resultado.getDouble(
                                                    "total_horas"
                                            )
                                    )
                            }
                    );
                }
            }
        }

        JTable tabla =
                new JTable(modelo);

        tabla.setRowHeight(26);

        JPanel panel =
                new JPanel(
                        new BorderLayout(8, 8)
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de operación"
                )
        );

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        JPanel panelTotal =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelTotal.add(
                new JLabel(
                        "Total horas de trabajo: "
                                + convertirDecimalAHoras(
                                        totalHorasGeneral
                                )
                )
        );

        panel.add(
                panelTotal,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel crearPanelEquipo(
            ResultSet cabecera
    ) throws Exception {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del equipo"
                )
        );

        agregarDatoSimple(
                panel,
                "Horómetro inicio:",
                valorDecimal(
                        cabecera,
                        "horometro_inicial"
                )
        );

        agregarDatoSimple(
                panel,
                "Horómetro fin:",
                valorDecimal(
                        cabecera,
                        "horometro_final"
                )
        );

        agregarDatoSimple(
                panel,
                "Horómetro recorrido:",
                valorDecimal(
                        cabecera,
                        "horometro_recorrido"
                )
        );

        agregarDatoSimple(
                panel,
                "Combustible abastecido:",
                cabecera.getString("combustible")
        );

        return panel;
    }

    private JPanel crearPanelInferior(
            String observaciones,
            String recibiConforme
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel panelObservaciones =
                new JPanel(new BorderLayout());

        panelObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        JTextArea texto =
                new JTextArea(
                        observaciones == null
                                ? ""
                                : observaciones
                );

        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        panelObservaciones.add(
                new JScrollPane(texto),
                BorderLayout.CENTER
        );

        JPanel panelRecibe =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelRecibe.add(
                new JLabel("Recibí conforme:"),
                BorderLayout.WEST
        );

        panelRecibe.add(
                new JLabel(
                        recibiConforme == null
                                ? ""
                                : recibiConforme
                ),
                BorderLayout.CENTER
        );

        JButton btnCerrar =
                new JButton("Cerrar");

        btnCerrar.addActionListener(
                e -> dispose()
        );

        JPanel panelBoton =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBoton.add(btnCerrar);

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(8, 8)
                );

        panelCentro.add(
                panelObservaciones,
                BorderLayout.CENTER
        );

        panelCentro.add(
                panelRecibe,
                BorderLayout.SOUTH
        );

        panel.add(
                panelCentro,
                BorderLayout.CENTER
        );

        panel.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private void agregarDato(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        panel.add(new JLabel(etiqueta));
        panel.add(
                new JLabel(
                        valor == null ? "" : valor
                )
        );
    }

    private void agregarDatoSimple(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        panel.add(new JLabel(etiqueta));
        panel.add(
                new JLabel(
                        valor == null ? "" : valor
                )
        );
    }

    private DefaultTableModel crearModeloNoEditable(
            String[] columnas
    ) {

        return new DefaultTableModel(
                columnas,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };
    }

    private String valorTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor;
    }

    private String valorDecimal(
            ResultSet resultado,
            String columna
    ) throws Exception {

        Object valor =
                resultado.getObject(columna);

        return valor == null
                ? ""
                : valor.toString();
    }

    private String convertirDecimalAHoras(
            double horasDecimal
    ) {

        int totalMinutos =
                (int) Math.round(
                        horasDecimal * 60
                );

        int horas =
                totalMinutos / 60;

        int minutos =
                totalMinutos % 60;

        return String.format(
                "%02d:%02d",
                horas,
                minutos
        );
    }
}