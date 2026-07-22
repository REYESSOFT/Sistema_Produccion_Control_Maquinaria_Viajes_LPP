import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControlDiarioPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    public ControlDiarioPage(
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
        cargarControles();
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
                crearBotones(),
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

        panel.setOpaque(false);

        JButton btnVolver =
                new JButton(
                        "← Volver"
                );

        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Control Diario de Proyectos"
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

        panel.add(
                btnVolver,
                BorderLayout.WEST
        );

        panel.add(
                titulo,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                            "ID",
                            "Proyecto",
                            "Fecha",
                            "Metros lineales"
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

        tabla.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tabla.setRowHeight(26);

        tabla.setSelectionMode(
                ListSelectionModel
                        .SINGLE_SELECTION
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
                .setPreferredWidth(70);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(500);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(160);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        return scroll;
    }

    private JPanel crearBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnNuevo =
                new JButton(
                        "Nuevo Control Diario"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnDetalle =
        new JButton(
                "Detalle"
        );

JButton btnMaquinaria =
        new JButton(
                "Maquinaria utilizada"
        );

JButton btnEliminar =
        new JButton(
                "Eliminar"
        );

        btnNuevo.addActionListener(
                e -> abrirNuevoControl()
        );

        btnEditar.addActionListener(
                e -> editarControl()
        );

        btnDetalle.addActionListener(
        e -> verDetalleControl()
);

btnMaquinaria.addActionListener(
        e -> abrirMaquinariaUtilizada()
);

btnEliminar.addActionListener(
        e -> eliminarControl()
);

        panel.add(
                btnNuevo
        );

        panel.add(
                btnEditar
        );

        panel.add(
        btnDetalle
);

panel.add(
        btnMaquinaria
);

panel.add(
        btnEliminar
);

        return panel;
    }

    private void cargarControles() {

        try {

            modeloTabla.setRowCount(0);

            for (
                    ControlDiarioDAO.ControlDiarioResumen control
                    : ControlDiarioDAO.obtenerControles()
            ) {

                modeloTabla.addRow(
                        new Object[]{
                            control.idControl(),
                            control.proyecto(),
                            formatearFecha(
                                    control.fecha()
                            ),
                            control.metrosLineales()
                        }
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los controles diarios:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
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

    private void abrirNuevoControl() {

        FormControlDiario formulario =
                new FormControlDiario(
                        SwingUtilities
                                .getWindowAncestor(this)
                );

        formulario.setVisible(
                true
        );

        cargarControles();
    }


    private void editarControl() {

    int filaVista =
            tabla.getSelectedRow();

    if (filaVista == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un Control Diario.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaVista
            );

    int idControl =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    FormControlDiario formulario =
            new FormControlDiario(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idControl
            );

    formulario.setVisible(
            true
    );

    if (formulario.isGuardado()) {

        cargarControles();
    }
}

   private void verDetalleControl() {

    int filaVista =
            tabla.getSelectedRow();

    if (filaVista == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un Control Diario.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaVista
            );

    int idControl =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    FormControlDiario formulario =
            new FormControlDiario(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idControl,
                    true
            );

    formulario.setVisible(
            true
    );
}
private void abrirMaquinariaUtilizada() {

    int filaVista =
            tabla.getSelectedRow();

    if (filaVista == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un Control Diario.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaVista
            );

    int idControl =
            Integer.parseInt(
                    modeloTabla.getValueAt(
                            filaModelo,
                            0
                    ).toString()
            );

    DetalleControlDiarioMaquinariaDialog dialogo =
            new DetalleControlDiarioMaquinariaDialog(
                    SwingUtilities.getWindowAncestor(
                            this
                    ),
                    idControl
            );

    dialogo.setVisible(
            true
    );
}

    private void eliminarControl() {

        int filaVista =
                tabla.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un Control Diario.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        filaVista
                );

        int idControl =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar este Control Diario?\n\n"
                                + "El registro dejará de mostrarse,\n"
                                + "pero permanecerá almacenado "
                                + "en la base de datos.",
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

            ControlDiarioDAO.eliminar(
                    idControl
            );

            cargarControles();

            JOptionPane.showMessageDialog(
                    this,
                    "Control Diario eliminado correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}