import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetalleControlTrabajoDialog extends JDialog {

    private final String empresa;
    private final String numeroGuia;

    public DetalleControlTrabajoDialog(
            Window parent,
            String empresa,
            String numeroGuia
    ) {

        super(
                parent,
                "Detalle Control Trabajo Volquetas N° " + numeroGuia,
                ModalityType.APPLICATION_MODAL
        );

        this.empresa = empresa;
        this.numeroGuia = numeroGuia;

        setSize(1000, 700);
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
                    COALESCE(g.chofer_operador, '') AS chofer,
                    COALESCE(g.placa, '') AS placa,
                    COALESCE(g.sector, '') AS sector,
                    COALESCE(g.observaciones, '') AS observaciones,
                    g.estado
                FROM guias g
                INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa
                WHERE e.nombre_empresa = ?
                  AND g.tipo_guia = 'Control Trabajo Volquetas'
                  AND g.numero_guia = ?
                LIMIT 1
                """;

        String sqlTurnos = """
                SELECT
                    turno,
                    TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                    TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin,
                    total_horas
                FROM control_trabajo_turnos
                WHERE id_guia = ?
                ORDER BY FIELD(
                    turno,
                    'MAÑANA',
                    'TARDE',
                    'NOCHE'
                )
                """;

        String sqlParalizaciones = """
                SELECT
                    codigo,
                    TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                    TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin,
                    total_horas
                FROM control_trabajo_paralizaciones
                WHERE id_guia = ?
                ORDER BY numero_fila
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

                JPanel panelTablas =
                        new JPanel(
                                new GridLayout(
                                        2,
                                        1,
                                        10,
                                        10
                                )
                        );

                panelTablas.add(
                        crearPanelTurnos(
                                conexion,
                                idGuia,
                                sqlTurnos
                        )
                );

                panelTablas.add(
                        crearPanelParalizaciones(
                                conexion,
                                idGuia,
                                sqlParalizaciones
                        )
                );

                panelPrincipal.add(
                        panelTablas,
                        BorderLayout.CENTER
                );

                panelPrincipal.add(
                        crearPanelInferior(
                                cabecera.getString(
                                        "observaciones"
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
                                4,
                                4,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos generales"
                )
        );

        panel.add(new JLabel("Empresa:"));
        panel.add(
                new JLabel(
                        cabecera.getString(
                                "nombre_empresa"
                        )
                )
        );

        panel.add(new JLabel("N° Guía:"));
        panel.add(
                new JLabel(
                        cabecera.getString(
                                "numero_guia"
                        )
                )
        );

        panel.add(new JLabel("Fecha:"));
        panel.add(
                new JLabel(
                        cabecera.getString("fecha")
                )
        );

        panel.add(new JLabel("Estado:"));
        panel.add(
                new JLabel(
                        cabecera.getString("estado")
                )
        );

        panel.add(new JLabel("Cliente:"));
        panel.add(
                new JLabel(
                        cabecera.getString("cliente")
                )
        );

        panel.add(new JLabel("Sector:"));
        panel.add(
                new JLabel(
                        cabecera.getString("sector")
                )
        );

        panel.add(new JLabel("Chofer:"));
        panel.add(
                new JLabel(
                        cabecera.getString("chofer")
                )
        );

        panel.add(new JLabel("Placa:"));
        panel.add(
                new JLabel(
                        cabecera.getString("placa")
                )
        );

        return panel;
    }

    private JPanel crearPanelTurnos(
            Connection conexion,
            int idGuia,
            String sqlTurnos
    ) throws Exception {

        String[] columnas = {
                "Turno",
                "Hora Inicio",
                "Hora Fin",
                "Total Horas"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        try (
                PreparedStatement statement =
                        conexion.prepareStatement(
                                sqlTurnos
                        )
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
                                    valorHora(
                                            resultado,
                                            "hora_inicio"
                                    ),
                                    valorHora(
                                            resultado,
                                            "hora_fin"
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
                new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Turnos de trabajo"
                )
        );

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearPanelParalizaciones(
            Connection conexion,
            int idGuia,
            String sqlParalizaciones
    ) throws Exception {

        String[] columnas = {
                "Código",
                "Descripción",
                "Inicio",
                "Fin",
                "Total"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        try (
                PreparedStatement statement =
                        conexion.prepareStatement(
                                sqlParalizaciones
                        )
        ) {

            statement.setInt(1, idGuia);

            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                while (resultado.next()) {

                    int codigo =
                            resultado.getInt("codigo");

                    modelo.addRow(
                            new Object[]{
                                    codigo,
                                    obtenerDescripcionParalizacion(
                                            codigo
                                    ),
                                    valorHora(
                                            resultado,
                                            "hora_inicio"
                                    ),
                                    valorHora(
                                            resultado,
                                            "hora_fin"
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

        tabla.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(350);

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Paralizaciones"
                )
        );

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearPanelInferior(
            String observaciones
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBorder(
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

        panel.add(
                new JScrollPane(texto),
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

        panel.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return panel;
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

    private String valorHora(
            ResultSet resultado,
            String columna
    ) throws Exception {

        String valor =
                resultado.getString(columna);

        return valor == null
                ? ""
                : valor;
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

    private String obtenerDescripcionParalizacion(
            int codigo
    ) {

        return switch (codigo) {

            case 1 -> "Clima";
            case 2 -> "Parada por daño";
            case 3 -> "Falta de área";
            case 4 -> "Mantenimiento";
            case 5 -> "Alimentación";
            case 6 ->
                    "Abastecimiento de combustible";
            case 7 ->
                    "Mantenimiento o reparación (Eq. encendido)";
            case 8 -> "Otros";

            default -> "";
        };
    }
}
