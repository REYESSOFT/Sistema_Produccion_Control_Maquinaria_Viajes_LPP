import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetalleControlDiarioMaquinariaDialog extends JDialog {

    private final int idControl;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblCostoTotal;

    public DetalleControlDiarioMaquinariaDialog(
            Window propietario,
            int idControl
    ) {

        super(
                propietario,
                "Maquinaria utilizada",
                ModalityType.APPLICATION_MODAL
        );

        this.idControl =
                idControl;

        setSize(
                1050,
                620
        );

        setLocationRelativeTo(
                propietario
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        crearInterfaz();
        cargarDatos();
    }

    private void crearInterfaz() {

        setLayout(
                new BorderLayout(
                        12,
                        12
                )
        );

        getContentPane().setBackground(
                new Color(
                        244,
                        246,
                        248
                )
        );

        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        add(
                crearTabla(),
                BorderLayout.CENTER
        );

        add(
                crearPanelInferior(),
                BorderLayout.SOUTH
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(
                false
        );

        JLabel titulo =
                new JLabel(
                        "Maquinaria utilizada en el Control Diario"
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

        JLabel subtitulo =
                new JLabel(
                        "Control Diario ID: "
                                + idControl
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

        panel.add(
                titulo
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Código",
                                "Maquinaria",
                                "Tipo de cobro",
                                "Horas",
                                "Costo hora",
                                "Costo fijo",
                                "Costo calculado",
                                "Observaciones"
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
                .setPreferredWidth(230);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(90);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);

        tabla.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        tabla.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(130);

        tabla.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(280);

        tabla.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (
                                e.getClickCount() == 2
                        ) {

                            editarRegistro();
                        }
                    }
                }
        );

        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Detalle de maquinaria"
                )
        );

        return scroll;
    }

    private JPanel crearPanelInferior() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelPrincipal.setOpaque(
                false
        );

        JPanel panelTotal =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panelTotal.setOpaque(
                false
        );

        JLabel lblTituloTotal =
                new JLabel(
                        "Costo total maquinaria:"
                );

        lblTituloTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblCostoTotal =
                new JLabel(
                        "$0.00"
                );

        lblCostoTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblCostoTotal.setForeground(
                new Color(
                        22,
                        101,
                        52
                )
        );

        panelTotal.add(
                lblTituloTotal
        );

        panelTotal.add(
                Box.createHorizontalStrut(
                        8
                )
        );

        panelTotal.add(
                lblCostoTotal
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

        JButton btnAgregar =
                new JButton(
                        "Agregar"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        JButton btnCerrar =
                new JButton(
                        "Cerrar"
                );

        btnAgregar.addActionListener(
                e -> agregarRegistro()
        );

        btnEditar.addActionListener(
                e -> editarRegistro()
        );

        btnEliminar.addActionListener(
                e -> eliminarRegistro()
        );

        btnActualizar.addActionListener(
                e -> cargarDatos()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        panelBotones.add(
                btnAgregar
        );

        panelBotones.add(
                btnEditar
        );

        panelBotones.add(
                btnEliminar
        );

        panelBotones.add(
                btnActualizar
        );

        panelBotones.add(
                btnCerrar
        );

        panelPrincipal.add(
                panelTotal,
                BorderLayout.WEST
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.EAST
        );

        return panelPrincipal;
    }

    private void cargarDatos() {

        try {

            List<ControlDiarioAPI.ControlMaquinariaDetalle> lista =
                    ControlDiarioAPI.obtenerMaquinariaPorControl(
                            idControl
                    );

            modeloTabla.setRowCount(
                    0
            );

            double total =
                    0.00;

            for (
                    ControlDiarioAPI.ControlMaquinariaDetalle item
                    : lista
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                item.idControlMaquinaria(),

                                item.codigo(),

                                item.descripcion(),

                                formatearTipoCobro(
                                        item.tipoCobro()
                                ),

                                formatearNumero(
                                        item.horasTrabajadas()
                                ),

                                formatearMoneda(
                                        item.costoHoraProveedor()
                                ),

                                formatearMoneda(
                                        item.costoFijoProveedor()
                                ),

                                formatearMoneda(
                                        item.costoCalculado()
                                ),

                                item.observaciones()
                        }
                );

                total +=
                        item.costoCalculado();
            }

            lblCostoTotal.setText(
                    formatearMoneda(
                            total
                    )
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la maquinaria utilizada:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void agregarRegistro() {

        FormControlDiarioMaquinaria formulario =
                new FormControlDiarioMaquinaria(
                        this,
                        idControl,
                        null
                );

        formulario.setVisible(
                true
        );

        if (
                formulario.isGuardado()
        ) {

            cargarDatos();
        }
    }

    private void editarRegistro() {

        Integer idRegistro =
                obtenerIdSeleccionado();

        if (
                idRegistro == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una maquinaria en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        FormControlDiarioMaquinaria formulario =
                new FormControlDiarioMaquinaria(
                        this,
                        idControl,
                        idRegistro
                );

        formulario.setVisible(
                true
        );

        if (
                formulario.isGuardado()
        ) {

            cargarDatos();
        }
    }

    private void eliminarRegistro() {

        Integer idRegistro =
                obtenerIdSeleccionado();

        if (
                idRegistro == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una maquinaria en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar la maquinaria seleccionada "
                                + "del Control Diario?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                respuesta
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        try {

            ControlDiarioAPI.eliminarMaquinaria(
                    idRegistro
            );

            cargarDatos();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar el registro:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private Integer obtenerIdSeleccionado() {

        int filaVista =
                tabla.getSelectedRow();

        if (
                filaVista == -1
        ) {

            return null;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        filaVista
                );

        Object valor =
                modeloTabla.getValueAt(
                        filaModelo,
                        0
                );

        if (
                valor == null
        ) {

            return null;
        }

        return Integer.valueOf(
                valor.toString()
        );
    }

    private String formatearTipoCobro(
            String tipoCobro
    ) {

        if (
                tipoCobro == null
        ) {

            return "POR HORA";
        }

        return switch (
                tipoCobro
        ) {

            case "FIJO_DIARIO" ->
                    "FIJO DIARIO";

            case "FIJO_SERVICIO" ->
                    "FIJO SERVICIO";

            default ->
                    "POR HORA";
        };
    }

    private String formatearNumero(
            double valor
    ) {

        return String.format(
                "%,.2f",
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
