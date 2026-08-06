import javax.swing.*;
import java.awt.*;

public class DetalleGuiaDespachoMaterialDialog extends JDialog {

    private final String empresa;
    private final String numeroGuia;

    public DetalleGuiaDespachoMaterialDialog(
            Window parent,
            String empresa,
            String numeroGuia
    ) {

        super(
                parent,
                "Detalle Guía Despacho de Material N° "
                        + numeroGuia,
                ModalityType.APPLICATION_MODAL
        );

        this.empresa = empresa;
        this.numeroGuia = numeroGuia;

        setSize(950, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarDetalle();
    }

    private void cargarDetalle() {

        try {

            GuiaDespachoMaterialAPI.GuiaDetalle guia =
                    GuiaDespachoMaterialAPI.obtenerDetalle(
                            empresa,
                            numeroGuia,
                            "Guía Despacho de Material"
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
                    crearPanelDatosGenerales(guia),
                    BorderLayout.NORTH
            );

            panelPrincipal.add(
                    crearPanelDespacho(guia),
                    BorderLayout.CENTER
            );

            panelPrincipal.add(
                    crearPanelInferior(guia),
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

    private JPanel crearPanelDatosGenerales(
            GuiaDespachoMaterialAPI.GuiaDetalle guia
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
                "Chofer:",
                guia.choferOperador()
        );

        agregarDato(
                panel,
                "Solicitante:",
                guia.solicitante()
        );

        agregarDato(
                panel,
                "Sector:",
                guia.sector()
        );

        agregarDato(
                panel,
                "Placa:",
                guia.placa()
        );

        return panel;
    }

    private JPanel crearPanelDespacho(
            GuiaDespachoMaterialAPI.GuiaDetalle guia
    ) {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Información del despacho"
                )
        );

        agregarDatoSimple(
                panel,
                "Cubicaje:",
                formatearDecimal(
                        guia.m3()
                )
        );

        agregarDatoSimple(
                panel,
                "Lugar(es) de origen:",
                guia.origen()
        );

        agregarDatoSimple(
                panel,
                "Lugar de destino:",
                guia.destino()
        );

        agregarDatoSimple(
                panel,
                "Hora de entrada:",
                valorTexto(
                        guia.horaEntrada()
                )
        );

        agregarDatoSimple(
                panel,
                "Hora de salida:",
                valorTexto(
                        guia.horaSalida()
                )
        );

        agregarDatoSimple(
                panel,
                "Tipo(s) de material:",
                guia.material()
        );

        return panel;
    }

    private JPanel crearPanelInferior(
            GuiaDespachoMaterialAPI.GuiaDetalle guia
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

        JTextArea textoObservaciones =
                new JTextArea(
                        valorTexto(
                                guia.observaciones()
                        )
                );

        textoObservaciones.setEditable(false);
        textoObservaciones.setLineWrap(true);
        textoObservaciones.setWrapStyleWord(true);

        panelObservaciones.add(
                new JScrollPane(textoObservaciones),
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
                        valorTexto(
                                guia.recibiConforme()
                        )
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
                        valorTexto(valor)
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
                        valorTexto(valor)
                )
        );
    }

    private String valorTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor;
    }

    private String formatearDecimal(
            double valor
    ) {

        if (valor == Math.rint(valor)) {
            return String.format(
                    java.util.Locale.US,
                    "%.0f",
                    valor
            );
        }

        return String.format(
                java.util.Locale.US,
                "%.2f",
                valor
        );
    }
}
