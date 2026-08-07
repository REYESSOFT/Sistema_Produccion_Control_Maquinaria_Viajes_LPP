import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProyectoPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmbEmpresaFiltro;
    private JComboBox<String> cmbEstadoFiltro;

    private JTextField txtCodigoFiltro;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    public ProyectoPage(
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
        cargarFiltros();
        cargarProyectos();
    }

    private void crearInterfaz() {

        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelSuperior.setOpaque(false);

        panelSuperior.add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        panelSuperior.add(
                crearPanelFiltros(),
                BorderLayout.CENTER
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        add(
                crearPanelTabla(),
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
                        "Catálogo de Proyectos"
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

    private JPanel crearPanelFiltros() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
                                12,
                                5
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Filtros de búsqueda"
                )
        );

        cmbEmpresaFiltro =
                new JComboBox<>();

        cmbEstadoFiltro =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "PLANIFICADO",
                                "EN_EJECUCION",
                                "SUSPENDIDO",
                                "FINALIZADO",
                                "CANCELADO"
                        }
                );

        txtCodigoFiltro =
                new JTextField();

        JButton btnBuscar =
                new JButton(
                        "Buscar"
                );

        btnBuscar.addActionListener(
                e -> cargarProyectos()
        );

        panel.add(
                new JLabel(
                        "Empresa:"
                )
        );

        panel.add(
                new JLabel(
                        "Estado:"
                )
        );

        panel.add(
                new JLabel(
                        "Código:"
                )
        );

        panel.add(
                new JLabel("")
        );

        panel.add(
                cmbEmpresaFiltro
        );

        panel.add(
                cmbEstadoFiltro
        );

        panel.add(
                txtCodigoFiltro
        );

        panel.add(
                btnBuscar
        );

        return panel;
    }

    private JScrollPane crearPanelTabla() {

    modeloTabla =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Código",
                            "Empresa",
                            "Descripción",
                            "Sector",
                            "Piscina",
                            "Inicio",
                            "Fin estimado",
                            "Estado"
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
            .setPreferredWidth(60);

    tabla.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(140);

    tabla.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(180);

    tabla.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(300);

    tabla.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(180);

    tabla.getColumnModel()
            .getColumn(5)
            .setPreferredWidth(180);

    tabla.getColumnModel()
            .getColumn(6)
            .setPreferredWidth(120);

    tabla.getColumnModel()
            .getColumn(7)
            .setPreferredWidth(140);

    tabla.getColumnModel()
            .getColumn(8)
            .setPreferredWidth(140);

    JScrollPane scroll =
            new JScrollPane(
                    tabla
            );

    scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    return scroll;
}

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnNuevo =
                new JButton(
                        "Nuevo proyecto"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnDetalle =
                new JButton(
                        "Detalle"
                );

        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );

        btnNuevo.addActionListener(
                e -> abrirNuevoProyecto()
        );

        btnEditar.addActionListener(
                e -> editarProyecto()
        );

        btnDetalle.addActionListener(
                e -> mostrarDetalleProyecto()
        );

        btnEliminar.addActionListener(
                e -> eliminarProyecto()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnDetalle);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarFiltros() {

        try {

            cmbEmpresaFiltro.removeAllItems();

            cmbEmpresaFiltro.addItem(
                    "Todas"
            );

            for (
        EmpresaItem empresa
        : ProyectoAPI.obtenerEmpresas()
) {

    cmbEmpresaFiltro.addItem(
            empresa.nombre()
    );
}

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las empresas:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void cargarProyectos() {

        try {

            modeloTabla.setRowCount(0);

            String empresaFiltro =
                    obtenerTextoSeleccionado(
                            cmbEmpresaFiltro
                    );

            String estadoFiltro =
                    obtenerTextoSeleccionado(
                            cmbEstadoFiltro
                    );

            String codigoFiltro =
                    txtCodigoFiltro
                            .getText()
                            .trim()
                            .toUpperCase();

            for (
        ProyectoResumen proyecto
        : ProyectoAPI.obtenerResumen()
) {

                if (
                    !empresaFiltro.equalsIgnoreCase(
                            "Todas"
                    )
                    && !proyecto.empresa()
                            .equalsIgnoreCase(
                                    empresaFiltro
                            )
                ) {

                    continue;
                }

                if (
                    !estadoFiltro.equalsIgnoreCase(
                            "Todos"
                    )
                    && !proyecto.estado()
                            .equalsIgnoreCase(
                                    estadoFiltro
                            )
                ) {

                    continue;
                }

                if (
                    !codigoFiltro.isEmpty()
                    && !proyecto.codigoProyecto()
                            .toUpperCase()
                            .contains(
                                    codigoFiltro
                            )
                ) {

                    continue;
                }

                modeloTabla.addRow(
                        new Object[]{
                                proyecto.idProyecto(),
                                proyecto.codigoProyecto(),
                                proyecto.empresa(),
                                proyecto.descripcion(),
                                proyecto.sector(),
                                proyecto.piscina(),
                                formatearFecha(
                                        proyecto.fechaInicio()
                                ),
                                formatearFecha(
                                        proyecto.fechaFinEstimada()
                                ),
                                proyecto.estado()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los proyectos:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private String obtenerTextoSeleccionado(
            JComboBox<String> combo
    ) {

        Object seleccionado =
                combo.getSelectedItem();

        return seleccionado == null
                ? ""
                : seleccionado.toString();
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

    private void validarSeleccionTemporal(
            String accion
    ) {

        if (
            tabla.getSelectedRow() == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un proyecto "
                            + "en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        mostrarPendiente(
                accion
        );
    }

    private void mostrarPendiente(
            String accion
    ) {

        JOptionPane.showMessageDialog(
                this,
                "La opción \""
                        + accion
                        + "\" se conectará "
                        + "en el siguiente paso.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    private void abrirNuevoProyecto() {

    FormProyecto formulario =
            new FormProyecto(
                    SwingUtilities
                            .getWindowAncestor(this)
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarProyectos();
    }
}

private void editarProyecto() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un proyecto.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    fila
            );

    int idProyecto =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    FormProyecto formulario =
            new FormProyecto(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idProyecto
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarProyectos();
    }
}

private void mostrarDetalleProyecto() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un proyecto.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    fila
            );

    int idProyecto =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    DetalleProyectoDialog detalle =
            new DetalleProyectoDialog(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idProyecto
            );

    detalle.setVisible(true);
}
private void eliminarProyecto() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un proyecto.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    fila
            );

    int idProyecto =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    String codigoProyecto =
            modeloTabla
                    .getValueAt(
                            filaModelo,
                            1
                    )
                    .toString();

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar el proyecto "
                            + codigoProyecto
                            + "?\n\n"
                            + "El proyecto dejará de aparecer "
                            + "en la pantalla,\n"
                            + "pero permanecerá almacenado "
                            + "en MySQL para conservar el historial.",
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

        ProyectoAPI.eliminarProyecto(
        idProyecto
);

        cargarProyectos();

        JOptionPane.showMessageDialog(
                this,
                "Proyecto eliminado correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al eliminar el proyecto:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
}