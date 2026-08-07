import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.List;

public class DetalleControlDiarioMaterialDialog extends JDialog {

    private final int idControl;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JLabel lblTotalMaterial;
    private JLabel lblTotalTransporte;
    private JLabel lblCostoTotal;

    public DetalleControlDiarioMaterialDialog(
            Window propietario,
            int idControl
    ) {

        super(
                propietario,
                "Material pétreo",
                ModalityType.APPLICATION_MODAL
        );

        this.idControl =
                idControl;

        setSize(
                1320,
                680
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
                        "Material pétreo del Control Diario"
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
                                "Material",
                                "Cantera",
                                "Viajes",
                                "Volumen",
                                "Costo unit. material",
                                "Costo unit. transporte",
                                "Costo material",
                                "Costo transporte",
                                "Costo total",
                                "Volquetas",
                                "Horas volqueta",
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
                .setPreferredWidth(180);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(170);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(90);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        tabla.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(150);

        tabla.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        tabla.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(130);

        tabla.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(120);

        tabla.getColumnModel()
                .getColumn(9)
                .setPreferredWidth(90);

        tabla.getColumnModel()
                .getColumn(10)
                .setPreferredWidth(110);

        tabla.getColumnModel()
                .getColumn(11)
                .setPreferredWidth(260);

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
                        "Detalle de material pétreo"
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

        JPanel panelTotales =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panelTotales.setOpaque(
                false
        );

        JLabel lblTituloMaterial =
                new JLabel(
                        "Material:"
                );

        JLabel lblTituloTransporte =
                new JLabel(
                        "Transporte:"
                );

        JLabel lblTituloTotal =
                new JLabel(
                        "Total:"
                );

        Font fuenteTitulo =
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                );

        lblTituloMaterial.setFont(
                fuenteTitulo
        );

        lblTituloTransporte.setFont(
                fuenteTitulo
        );

        lblTituloTotal.setFont(
                fuenteTitulo
        );

        lblTotalMaterial =
                new JLabel(
                        "$0.00"
                );

        lblTotalTransporte =
                new JLabel(
                        "$0.00"
                );

        lblCostoTotal =
                new JLabel(
                        "$0.00"
                );

        Font fuenteValor =
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                );

        lblTotalMaterial.setFont(
                fuenteValor
        );

        lblTotalTransporte.setFont(
                fuenteValor
        );

        lblCostoTotal.setFont(
                fuenteValor
        );

        lblCostoTotal.setForeground(
                new Color(
                        22,
                        101,
                        52
                )
        );

        panelTotales.add(
                lblTituloMaterial
        );

        panelTotales.add(
                lblTotalMaterial
        );

        panelTotales.add(
                Box.createHorizontalStrut(
                        18
                )
        );

        panelTotales.add(
                lblTituloTransporte
        );

        panelTotales.add(
                lblTotalTransporte
        );

        panelTotales.add(
                Box.createHorizontalStrut(
                        18
                )
        );

        panelTotales.add(
                lblTituloTotal
        );

        panelTotales.add(
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
                panelTotales,
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

            List<ControlDiarioAPI.ControlMaterialDetalle> lista =
                    ControlDiarioAPI.obtenerMateriales(
                            idControl
                    );

            modeloTabla.setRowCount(
                    0
            );

            double totalMaterial =
                    0.00;

            double totalTransporte =
                    0.00;

            double totalGeneral =
                    0.00;

            for (
                    ControlDiarioAPI.ControlMaterialDetalle item
                    : lista
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                item.idControlMaterial(),

                                item.materialRecibido(),

                                item.cantera(),

                                formatearNumero(
                                        item.cantidadViajes()
                                ),

                                formatearNumero(
                                        item.volumenRecibido()
                                ),

                                formatearMoneda(
                                        item.costoUnitarioMaterial()
                                ),

                                formatearMoneda(
                                        item.costoUnitarioTransporte()
                                ),

                                formatearMoneda(
                                        item.costoMaterial()
                                ),

                                formatearMoneda(
                                        item.costoTransporte()
                                ),

                                formatearMoneda(
                                        item.costoTotal()
                                ),

                                item.cantidadVolquetas(),

                                formatearNumero(
                                        item.horasVolqueta()
                                ),

                                item.observaciones()
                        }
                );

                totalMaterial +=
                        item.costoMaterial();

                totalTransporte +=
                        item.costoTransporte();

                totalGeneral +=
                        item.costoTotal();
            }

            lblTotalMaterial.setText(
                    formatearMoneda(
                            totalMaterial
                    )
            );

            lblTotalTransporte.setText(
                    formatearMoneda(
                            totalTransporte
                    )
            );

            lblCostoTotal.setText(
                    formatearMoneda(
                            totalGeneral
                    )
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el material pétreo:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void agregarRegistro() {

        FormControlDiarioMaterial formulario =
                new FormControlDiarioMaterial(
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
                    "Seleccione un material en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        FormControlDiarioMaterial formulario =
                new FormControlDiarioMaterial(
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
                    "Seleccione un material en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el material seleccionado "
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

            ControlDiarioAPI.eliminarMaterial(
                    idRegistro
            );

            cargarDatos();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar el material:\n"
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