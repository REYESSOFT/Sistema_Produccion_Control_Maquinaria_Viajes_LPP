import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AvanceProyectoPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modelo;
    private List<AvanceProyectoDAO.AvanceProyectoResumen>
        listaAvances;

    private JLabel lblProyectosActivos;
    private JLabel lblMlContratados;
    private JLabel lblMlEjecutados;
    private JLabel lblAvanceGeneral;
    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern(
                "dd/MM/yyyy"
        );

    public AvanceProyectoPage(
            Runnable accionVolver
    ) {

        this.accionVolver = accionVolver;

        setLayout(
                new BorderLayout(
                        15,
                        15
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

        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        panelSuperior.setOpaque(false);

        JPanel encabezado =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        encabezado.setOpaque(false);

        JButton btnVolver =
                new JButton(
                        "← Volver"
                );

        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(
                e -> {

                    if (accionVolver != null) {
                        accionVolver.run();
                    }
                }
        );

        JLabel titulo =
                new JLabel(
                        "Dashboard de Proyectos"
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

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );
        JButton btnDetalle =
                new JButton(
                        "Detalle"
                );

        btnDetalle.setFocusPainted(
                false
        );

        btnDetalle.addActionListener(
                e -> mostrarDetalleSeleccionado()
        );

        btnActualizar.setFocusPainted(false);

        btnActualizar.addActionListener(
                e -> cargarDatos()
        );

        encabezado.add(
                btnVolver,
                BorderLayout.WEST
        );

        encabezado.add(
                titulo,
                BorderLayout.CENTER
        );

        JPanel panelAcciones =
        new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        8,
                        0
                )
        );

panelAcciones.setOpaque(
        false
);

panelAcciones.add(
        btnDetalle
);

panelAcciones.add(
        btnActualizar
);

encabezado.add(
        panelAcciones,
        BorderLayout.EAST
);

        JPanel panelIndicadores =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        panelIndicadores.setOpaque(false);

        lblProyectosActivos =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        lblMlContratados =
                new JLabel(
                        "0.00",
                        SwingConstants.CENTER
                );

        lblMlEjecutados =
                new JLabel(
                        "0.00",
                        SwingConstants.CENTER
                );

        lblAvanceGeneral =
                new JLabel(
                        "0.00 %",
                        SwingConstants.CENTER
                );

        panelIndicadores.add(
                crearTarjetaIndicador(
                        "Proyectos activos",
                        lblProyectosActivos,
                        new Color(
                                219,
                                234,
                                254
                        )
                )
        );

        panelIndicadores.add(
                crearTarjetaIndicador(
                        "Metros lineales contratados",
                        lblMlContratados,
                        new Color(
                                220,
                                252,
                                231
                        )
                )
        );

        panelIndicadores.add(
                crearTarjetaIndicador(
                        "Metros lineales ejecutados",
                        lblMlEjecutados,
                        new Color(
                                254,
                                243,
                                199
                        )
                )
        );

        panelIndicadores.add(
                crearTarjetaIndicador(
                        "Avance general",
                        lblAvanceGeneral,
                        new Color(
                                254,
                                226,
                                226
                        )
                )
        );

        panelSuperior.add(
                encabezado,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                panelIndicadores,
                BorderLayout.CENTER
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        modelo =
                new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {

                        return false;
                    }
                };

        modelo.addColumn("Proyecto/Codigo");
        modelo.addColumn("Descripción");
        modelo.addColumn("Empresa");
        modelo.addColumn("Fecha Control");
        modelo.addColumn("Fecha Inicio");
        modelo.addColumn("Duración");
        modelo.addColumn("Metros Lineales Contratados");
        modelo.addColumn("Avance Diario");
        modelo.addColumn("Metros Lineales Acumulados");
        modelo.addColumn("Metros Lineales Restantes");
        modelo.addColumn("Ancho");
        modelo.addColumn("Espesor");
        modelo.addColumn("Volumen Diario");
        modelo.addColumn("Volumen Acumulado");
        modelo.addColumn("Horas Trabajadas");
        modelo.addColumn("Metros Cúbicos Transportados");
        modelo.addColumn("Cantidad de Viajes");
        modelo.addColumn("% Avance Físico");
        tabla =
                new JTable(
                        modelo
                );
        tabla.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tabla.setRowHeight(28);
        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        tabla.getSelectionModel().addListSelectionListener(
        e -> {

            if (!e.getValueIsAdjusting()) {

                actualizarIndicadoresSeleccion();
            }
        }
);
        tabla.addMouseListener(
        new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e
            ) {

                if (e.getClickCount() == 2) {

                    mostrarDetalleSeleccionado();
                }
            }
        }
);

        tabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabla.getTableHeader().setReorderingAllowed(
                false
        );

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (
                int columna = 2;
                columna < tabla.getColumnCount();
                columna++
        ) {

            tabla.getColumnModel()
                    .getColumn(columna)
                    .setCellRenderer(centrado);
        }

        tabla.getColumnModel()
        .getColumn(0)
        .setPreferredWidth(140);
// Proyecto

tabla.getColumnModel()
        .getColumn(1)
        .setPreferredWidth(280);
// Descripción

tabla.getColumnModel()
        .getColumn(2)
        .setPreferredWidth(220);
// Empresa

tabla.getColumnModel()
        .getColumn(3)
        .setPreferredWidth(140);
// Fecha Control

tabla.getColumnModel()
        .getColumn(4)
        .setPreferredWidth(130);
// Fecha Inicio

tabla.getColumnModel()
        .getColumn(5)
        .setPreferredWidth(110);
// Duración

tabla.getColumnModel()
        .getColumn(6)
        .setPreferredWidth(230);
// Metros Lineales Contratados

tabla.getColumnModel()
        .getColumn(7)
        .setPreferredWidth(160);
// Avance Diario

tabla.getColumnModel()
        .getColumn(8)
        .setPreferredWidth(230);
// Metros Lineales Acumulados

tabla.getColumnModel()
        .getColumn(9)
        .setPreferredWidth(220);
// Metros Lineales Restantes

tabla.getColumnModel()
        .getColumn(10)
        .setPreferredWidth(90);
// Ancho

tabla.getColumnModel()
        .getColumn(11)
        .setPreferredWidth(90);
// Espesor

tabla.getColumnModel()
        .getColumn(12)
        .setPreferredWidth(170);
// Volumen Diario

tabla.getColumnModel()
        .getColumn(13)
        .setPreferredWidth(190);
// Volumen Acumulado

tabla.getColumnModel()
        .getColumn(14)
        .setPreferredWidth(170);
// Horas Trabajadas

tabla.getColumnModel()
        .getColumn(15)
        .setPreferredWidth(240);
// Metros Cúbicos Transportados

tabla.getColumnModel()
        .getColumn(16)
        .setPreferredWidth(150);
// Cantidad de Viajes

tabla.getColumnModel()
        .getColumn(17)
        .setPreferredWidth(150);
// % Avance Físico
        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
);

scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                209,
                                213,
                                219
                        )
                )
        );

        add(
                scroll,
                BorderLayout.CENTER
        );
    }

    private JPanel crearTarjetaIndicador(
            String titulo,
            JLabel etiquetaValor,
            Color colorFondo
    ) {

        JPanel tarjeta =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        tarjeta.setBackground(
                colorFondo
        );

        tarjeta.setBorder(
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

        JLabel etiquetaTitulo =
                new JLabel(
                        titulo,
                        SwingConstants.CENTER
                );

        etiquetaTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        etiquetaTitulo.setForeground(
                new Color(
                        55,
                        65,
                        81
                )
        );

        etiquetaValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        etiquetaValor.setForeground(
                new Color(
                        17,
                        24,
                        39
                )
        );

        tarjeta.add(
                etiquetaTitulo,
                BorderLayout.NORTH
        );

        tarjeta.add(
                etiquetaValor,
                BorderLayout.CENTER
        );

        return tarjeta;
    }

    private void cargarDatos() {

        try {

            cargarResumen();
            cargarTabla();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el Dashboard:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarResumen()
            throws Exception {

        AvanceProyectoDAO.DashboardResumen resumen =
                AvanceProyectoDAO.obtenerResumen();

        lblProyectosActivos.setText(
                String.valueOf(
                        resumen.proyectosActivos()
                )
        );

        lblMlContratados.setText(
                String.format(
                        "%,.2f",
                        resumen.metrosLinealesContratados()
                )
        );

        lblMlEjecutados.setText(
                String.format(
                        "%,.2f",
                        resumen.metrosLinealesEjecutados()
                )
        );

        lblAvanceGeneral.setText(
                String.format(
                        "%.2f %%",
                        resumen.porcentajeGeneral()
                )
        );
    }

    private void cargarTabla()
        throws Exception {

    modelo.setRowCount(0);
    tabla.clearSelection();

    listaAvances =
        AvanceProyectoDAO.obtenerAvances();

    for (
        AvanceProyectoDAO.AvanceProyectoResumen item
                : listaAvances
) {

        modelo.addRow(

                new Object[]{

                        item.codigoProyecto(),

                        item.descripcion(),

                        item.empresa(),

                        formatearFecha(
                            item.fechaControl()
                        ),

                        formatearFecha(
                            item.fechaInicio()
                        ),

                        item.diasEstimados(),

                        String.format(
                                "%,.2f",
                                item.metrosLinealesContratados()
                        ),

                        String.format(
                                "%,.2f",
                                item.avanceMetrosLinealesDiario()
                        ),

                        String.format(
                                "%,.2f",
                                item.metrosLinealesAcumulados()
                        ),

                        String.format(
                                "%,.2f",
                                item.metrosLinealesRestantes()
                        ),

                        item.ancho(),

                        item.espesor(),

                        String.format(
        "%,.2f",
        item.volumenDiario()
),

String.format(
        "%,.2f",
        item.volumenAcumulado()
),

String.format(
        "%,.2f",
        item.horasTrabajadas()
),

String.format(
        "%,.2f",
        item.metrosCubicosTransportados()
),

item.cantidadViajes(),

String.format(
        "%.2f %%",
        item.porcentajeAvanceFisico()
)

                }

        );

    }

}

private void actualizarIndicadoresSeleccion() {

    int filaVista =
            tabla.getSelectedRow();

    /*
     * Si no existe una fila seleccionada,
     * volvemos a mostrar el resumen global.
     */
    if (filaVista < 0) {

        try {

            cargarResumen();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el resumen general:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaVista
            );

    if (
            listaAvances == null
            || filaModelo < 0
            || filaModelo >= listaAvances.size()
    ) {

        return;
    }

    AvanceProyectoDAO.AvanceProyectoResumen proyecto =
            listaAvances.get(
                    filaModelo
            );

    double metrosContratados =
            proyecto.metrosLinealesContratados();

    double metrosEjecutados =
            proyecto.metrosLinealesAcumulados();

    double porcentajeAvance =
            metrosContratados > 0
                    ? (
                            metrosEjecutados
                            / metrosContratados
                      ) * 100.00
                    : 0.00;

    /*
     * Al seleccionar una fila, el resumen representa
     * solamente un proyecto.
     */
    lblProyectosActivos.setText(
            "1"
    );

    lblMlContratados.setText(
            String.format(
                    "%,.2f",
                    metrosContratados
            )
    );

    lblMlEjecutados.setText(
            String.format(
                    "%,.2f",
                    metrosEjecutados
            )
    );

    lblAvanceGeneral.setText(
            String.format(
                    "%.2f %%",
                    porcentajeAvance
            )
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
private void mostrarDetalleSeleccionado() {

    int filaVista =
            tabla.getSelectedRow();

    if (filaVista < 0) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un proyecto para ver el detalle.",
                "Detalle de avance",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaVista
            );

    if (
            listaAvances == null
            || filaModelo < 0
            || filaModelo >= listaAvances.size()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "No fue posible obtener el proyecto seleccionado.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    AvanceProyectoDAO.AvanceProyectoResumen avance =
            listaAvances.get(
                    filaModelo
            );

    Window ventana =
            SwingUtilities.getWindowAncestor(
                    this
            );

    DetalleAvanceProyectoDialog dialogo =
            new DetalleAvanceProyectoDialog(
                    ventana,
                    avance
            );

    dialogo.setVisible(
            true
    );
}
}