import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        try {

            GuiaTrabajoMaquinariaAPI.GuiaDetalle guia =
                    GuiaTrabajoMaquinariaAPI.obtenerDetalle(
                            empresa,
                            numeroGuia,
                            "Guía Trabajo Diario Maquinaria"
                    );

            if (guia == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la guía seleccionada.",
                        "Información",
                        JOptionPane.WARNING_MESSAGE
                );

                dispose();
                return;
            }

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
                    crearPanelTurnos(guia)
            );

            panelCentro.add(
                    crearPanelEquipo(guia)
            );

            panelPrincipal.add(
                    panelCentro,
                    BorderLayout.CENTER
            );

            panelPrincipal.add(
                    crearPanelInferior(
                            guia.observaciones(),
                            guia.recibiConforme()
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
            GuiaTrabajoMaquinariaAPI.GuiaDetalle guia
    ) {

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
                guia.empresa()
        );

        agregarDato(
                panel,
                "N° Guía:",
                guia.numeroGuia()
        );

        agregarDato(
                panel,
                "Fecha:",
                guia.fecha() == null
                        ? ""
                        : guia.fecha().format(
                                java.time.format.DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")
                        )
        );

        agregarDato(
                panel,
                "Estado:",
                guia.estado()
        );

        agregarDato(
                panel,
                "N° Máquina:",
                guia.numeroMaquina()
        );

        agregarDato(
                panel,
                "Tipo de máquina:",
                guia.tipoMaquina()
        );

        agregarDato(
                panel,
                "Cliente:",
                guia.cliente()
        );

        agregarDato(
                panel,
                "Sector:",
                guia.sector()
        );

        agregarDato(
                panel,
                "Operador:",
                guia.operador()
        );

        agregarDato(
                panel,
                "Trabajo a realizar:",
                guia.trabajoRealizar()
        );

        agregarDato(
                panel,
                "Hora inicio:",
                valorTexto(
                        guia.horaInicio()
                )
        );

        agregarDato(
                panel,
                "Hora fin:",
                valorTexto(
                        guia.horaFin()
                )
        );

        agregarDato(
                panel,
                "Chequeo de engrase:",
                guia.chequeoEngrase()
                        ? "Realizado"
                        : "No realizado"
        );

        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel crearPanelTurnos(
            GuiaTrabajoMaquinariaAPI.GuiaDetalle guia
    ) {

        String[] columnas = {
                "Turno",
                "Inicio",
                "Terminación",
                "Total"
        };

        DefaultTableModel modelo =
                crearModeloNoEditable(columnas);

        if (guia.turnos() != null) {

            for (
                    GuiaTrabajoMaquinariaAPI.Turno turno
                            : guia.turnos()
            ) {

                modelo.addRow(
                        new Object[]{
                                turno.turno(),
                                valorTexto(
                                        turno.horaInicio()
                                ),
                                valorTexto(
                                        turno.horaFin()
                                ),
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
                                        guia.totalHoras()
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
            GuiaTrabajoMaquinariaAPI.GuiaDetalle guia
    ) {

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
                        guia.horometroInicial()
                )
        );

        agregarDatoSimple(
                panel,
                "Horómetro fin:",
                valorDecimal(
                        guia.horometroFinal()
                )
        );

        agregarDatoSimple(
                panel,
                "Horómetro recorrido:",
                valorDecimal(
                        guia.horometroRecorrido()
                )
        );

        agregarDatoSimple(
                panel,
                "Combustible abastecido:",
                guia.combustible()
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
            Double valor
    ) {

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