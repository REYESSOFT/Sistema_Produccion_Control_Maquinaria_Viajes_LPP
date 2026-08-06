import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        try {

            ControlTrabajoVolquetasAPI.GuiaDetalle guia =
                    ControlTrabajoVolquetasAPI.obtenerDetalle(
                            empresa,
                            numeroGuia,
                            "Control Trabajo Volquetas"
                    );

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
                    crearPanelCabecera(guia),
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
                            guia.turnos()
                    )
            );

            panelTablas.add(
                    crearPanelParalizaciones(
                            guia.paralizaciones()
                    )
            );

            panelPrincipal.add(
                    panelTablas,
                    BorderLayout.CENTER
            );

            panelPrincipal.add(
                    crearPanelInferior(
                            guia.observaciones()
                    ),
                    BorderLayout.SOUTH
            );

            setContentPane(panelPrincipal);

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
            ControlTrabajoVolquetasAPI.GuiaDetalle guia
    ) {

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
        panel.add(new JLabel(guia.empresa()));

        panel.add(new JLabel("N° Guía:"));
        panel.add(new JLabel(guia.numeroGuia()));

        panel.add(new JLabel("Fecha:"));
        panel.add(
                new JLabel(
                        guia.fecha() == null
                                ? ""
                                : guia.fecha().format(
                                        java.time.format.DateTimeFormatter
                                                .ofPattern("dd/MM/yyyy")
                                )
                )
        );

        panel.add(new JLabel("Estado:"));
        panel.add(new JLabel(guia.estado()));

        panel.add(new JLabel("Cliente:"));
        panel.add(new JLabel(guia.cliente()));

        panel.add(new JLabel("Sector:"));
        panel.add(new JLabel(guia.sector()));

        panel.add(new JLabel("Chofer:"));
        panel.add(new JLabel(guia.choferOperador()));

        panel.add(new JLabel("Placa:"));
        panel.add(new JLabel(guia.placa()));

        return panel;
    }

    private JPanel crearPanelTurnos(
            java.util.List<ControlTrabajoVolquetasAPI.Turno> turnos
    ) {

        String[] columnas = {
                "Turno",
                "Hora Inicio",
                "Hora Fin",
                "Total Horas"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        if (turnos != null) {

            for (
                    ControlTrabajoVolquetasAPI.Turno turno
                            : turnos
            ) {

                modelo.addRow(
                        new Object[]{
                                turno.turno(),
                                turno.horaInicio(),
                                turno.horaFin(),
                                convertirDecimalAHoras(
                                        turno.totalHoras()
                                )
                        }
                );
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
            java.util.List<
                    ControlTrabajoVolquetasAPI.Paralizacion
            > paralizaciones
    ) {

        String[] columnas = {
                "Código",
                "Descripción",
                "Inicio",
                "Fin",
                "Total"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        if (paralizaciones != null) {

            for (
                    ControlTrabajoVolquetasAPI.Paralizacion paralizacion
                            : paralizaciones
            ) {

                modelo.addRow(
                        new Object[]{
                                paralizacion.codigo(),
                                paralizacion.descripcion(),
                                paralizacion.horaInicio(),
                                paralizacion.horaFin(),
                                convertirDecimalAHoras(
                                        paralizacion.totalHoras()
                                )
                        }
                );
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
