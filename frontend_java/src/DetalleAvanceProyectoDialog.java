import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetalleAvanceProyectoDialog extends JDialog {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    public DetalleAvanceProyectoDialog(
            Window parent,
           AvanceProyectoAPI.AvanceProyectoResumen avance
    ) {

        super(
                parent,
                "Detalle de Avance del Proyecto",
                ModalityType.APPLICATION_MODAL
        );

        setSize(
                760,
                650
        );

        setLocationRelativeTo(
                parent
        );

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        crearInterfaz(
                avance
        );
    }

    private void crearInterfaz(
            AvanceProyectoAPI.AvanceProyectoResumen avance
    ) {

        JPanel principal =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        principal.setBackground(
                new Color(
                        244,
                        246,
                        248
                )
        );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel titulo =
                new JLabel(
                        "Detalle de Avance del Proyecto"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        titulo.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel panelDatos =
                new JPanel(
                       new GridLayout(
        18,
        2,
        10,
        8
)
                );

        panelDatos.setBackground(
                Color.WHITE
        );

        panelDatos.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        209,
                                        213,
                                        219
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        agregarFila(
                panelDatos,
                "Código:",
                avance.codigoProyecto()
        );

        agregarFila(
                panelDatos,
                "Descripción:",
                avance.descripcion()
        );

        agregarFila(
                panelDatos,
                "Empresa:",
                avance.empresa()
        );

        agregarFila(
                panelDatos,
                "Fecha de control:",
                formatearFecha(
                        avance.fechaControl()
                )
        );

        agregarFila(
                panelDatos,
                "Fecha de inicio:",
                formatearFecha(
                        avance.fechaInicio()
                )
        );

        agregarFila(
                panelDatos,
                "Duración estimada:",
                avance.diasEstimados() == null
                        ? ""
                        : avance.diasEstimados()
                                + " días"
        );

        agregarFila(
                panelDatos,
                "Metros lineales contratados:",
                formatearDecimal(
                        avance.metrosLinealesContratados()
                )
        );

        agregarFila(
                panelDatos,
                "Avance diario:",
                formatearDecimal(
                        avance.avanceMetrosLinealesDiario()
                )
        );

        agregarFila(
                panelDatos,
                "Metros lineales acumulados:",
                formatearDecimal(
                        avance.metrosLinealesAcumulados()
                )
        );

        agregarFila(
                panelDatos,
                "Metros lineales restantes:",
                formatearDecimal(
                        avance.metrosLinealesRestantes()
                )
        );

        agregarFila(
                panelDatos,
                "Ancho:",
                formatearDecimalNullable(
                        avance.ancho()
                )
        );

        agregarFila(
                panelDatos,
                "Espesor:",
                formatearDecimalNullable(
                        avance.espesor()
                )
        );

        agregarFila(
                panelDatos,
                "Volumen diario:",
                formatearDecimal(
                        avance.volumenDiario()
                )
        );

        agregarFila(
                panelDatos,
                "Volumen acumulado:",
                formatearDecimal(
                        avance.volumenAcumulado()
                )
        );
        agregarFila(
        panelDatos,
        "Horas trabajadas:",
        formatearDecimal(
                avance.horasTrabajadas()
        )
);

agregarFila(
        panelDatos,
        "Metros cúbicos transportados:",
        formatearDecimal(
                avance.metrosCubicosTransportados()
        )
);

agregarFila(
        panelDatos,
        "Cantidad de viajes:",
        String.valueOf(
                avance.cantidadViajes()
        )
);

        agregarFila(
                panelDatos,
                "Porcentaje de avance físico:",
                String.format(
                        "%.2f %%",
                        avance.porcentajeAvanceFisico()
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        panelDatos
                );

        scroll.setBorder(
                null
        );

        principal.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBotones.setOpaque(
                false
        );

        JButton btnCerrar =
                new JButton(
                        "Cerrar"
                );

        btnCerrar.setFocusPainted(
                false
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        panelBotones.add(
                btnCerrar
        );

        principal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        setContentPane(
                principal
        );
    }

    private void agregarFila(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        JLabel lblEtiqueta =
                new JLabel(
                        etiqueta
                );

        lblEtiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblEtiqueta.setForeground(
                new Color(
                        55,
                        65,
                        81
                )
        );

        JTextField txtValor =
                new JTextField(
                        valor == null
                                ? ""
                                : valor
                );

        txtValor.setEditable(
                false
        );

        txtValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtValor.setBackground(
                new Color(
                        249,
                        250,
                        251
                )
        );

        panel.add(
                lblEtiqueta
        );

        panel.add(
                txtValor
        );
    }

    private String formatearFecha(
            LocalDate fecha
    ) {

        return fecha == null
                ? ""
                : fecha.format(
                        FORMATO_FECHA
                );
    }

    private String formatearDecimal(
            double valor
    ) {

        return String.format(
                "%,.2f",
                valor
        );
    }

    private String formatearDecimalNullable(
            Double valor
    ) {

        return valor == null
                ? ""
                : String.format(
                        "%,.2f",
                        valor
                );
    }
}
