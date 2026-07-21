import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CostosRentabilidadPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CostosRentabilidadPage(
            Runnable accionVolver
    ) {

        this.accionVolver =
                accionVolver;

        setLayout(
                new BorderLayout(
                        12,
                        12
                )
        );

        setBackground(
                new Color(
                        244,
                        246,
                        248
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        crearInterfaz();
        cargarDatos();
    }

    private void crearInterfaz() {

        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        add(
                crearTabla(),
                BorderLayout.CENTER
        );

        add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setOpaque(
                false
        );

        JButton btnVolver =
                new JButton(
                        "← Volver"
                );

        btnVolver.setFocusPainted(
                false
        );

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Costos y Rentabilidad"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titulo.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        JLabel subtitulo =
                new JLabel(
                        "Resumen económico y avance por proyecto"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                new Color(
                        75,
                        85,
                        99
                )
        );

        JPanel panelTitulos =
                new JPanel();

        panelTitulos.setLayout(
                new BoxLayout(
                        panelTitulos,
                        BoxLayout.Y_AXIS
                )
        );

        panelTitulos.setOpaque(
                false
        );

        panelTitulos.add(
                titulo
        );

        panelTitulos.add(
                Box.createVerticalStrut(
                        4
                )
        );

        panelTitulos.add(
                subtitulo
        );

        panel.add(
                btnVolver,
                BorderLayout.WEST
        );

        panel.add(
                panelTitulos,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Código",
                                "Proyecto",
                                "Empresa",
                                "ML contratados",
                                "ML ejecutados",
                                "% avance",
                                "Ingreso",
                                "Costo",
                                "Utilidad"
                        },
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

        tabla =
                new JTable(
                        modeloTabla
                );

        tabla.removeColumn(
                tabla.getColumnModel()
                        .getColumn(0)
        );

        tabla.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tabla.setRowHeight(
                28
        );

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabla.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(110);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(300);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(200);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(140);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        tabla.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        tabla.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(130);

        tabla.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(130);

        tabla.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(130);

        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Resumen por proyecto"
                )
        );

        return scroll;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setOpaque(
                false
        );

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        btnActualizar.addActionListener(
                e -> cargarDatos()
        );

        panel.add(
                btnActualizar
        );

        return panel;
    }

    private void cargarDatos() {

        try {

            List<CostosRentabilidadDAO.CostosRentabilidadResumen> lista =
                    CostosRentabilidadDAO.obtenerResumen();

            modeloTabla.setRowCount(
                    0
            );

            for (
                    CostosRentabilidadDAO.CostosRentabilidadResumen item
                    : lista
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                item.idProyecto(),

                                item.codigoProyecto(),

                                item.descripcionProyecto(),

                                item.empresa(),

                                formatearNumero(
                                        item.metrosContratados()
                                ),

                                formatearNumero(
                                        item.metrosEjecutados()
                                ),

                                formatearPorcentaje(
                                        item.porcentajeAvance()
                                ),

                                formatearMoneda(
                                        item.ingreso()
                                ),

                                formatearMoneda(
                                        item.costo()
                                ),

                                formatearMoneda(
                                        item.utilidad()
                                )
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar Costos y Rentabilidad:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private String formatearNumero(
            double valor
    ) {

        return String.format(
                "%,.2f",
                valor
        );
    }

    private String formatearPorcentaje(
            double valor
    ) {

        return String.format(
                "%.2f %%",
                valor
        );
    }

    private String formatearMoneda(
            double valor
    ) {

        return String.format(
                "$%,.2f",
                valor
        );
    }
}
