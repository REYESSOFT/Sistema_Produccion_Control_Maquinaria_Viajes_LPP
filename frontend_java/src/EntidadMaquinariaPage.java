import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EntidadMaquinariaPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombreFiltro;
    private JComboBox<String> cmbTipoFiltro;
    private JComboBox<String> cmbEstadoFiltro;

    public EntidadMaquinariaPage(
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
        cargarEntidades();
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
                crearFiltros(),
                BorderLayout.CENTER
        );

        add(
                panelSuperior,
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
                        "Proveedores y Propietarios"
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

    private JPanel crearFiltros() {

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

        txtNombreFiltro =
                new JTextField();

        cmbTipoFiltro =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "EMPRESA",
                                "PERSONA",
                                "OTRO"
                        }
                );

        cmbEstadoFiltro =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "ACTIVO",
                                "INACTIVO"
                        }
                );

        JButton btnBuscar =
                new JButton(
                        "Buscar"
                );

        btnBuscar.addActionListener(
                e -> cargarEntidades()
        );

        panel.add(
                new JLabel(
                        "Nombre:"
                )
        );

        panel.add(
                new JLabel(
                        "Tipo:"
                )
        );

        panel.add(
                new JLabel(
                        "Estado:"
                )
        );

        panel.add(
                new JLabel("")
        );

        panel.add(
                txtNombreFiltro
        );

        panel.add(
                cmbTipoFiltro
        );

        panel.add(
                cmbEstadoFiltro
        );

        panel.add(
                btnBuscar
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Nombre",
                                "Tipo",
                                "Identificación",
                                "Teléfono",
                                "Correo",
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
                .setPreferredWidth(280);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(120);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(150);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(150);

        tabla.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(260);

        tabla.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
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
                        "Nueva entidad"
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
                e -> abrirNuevaEntidad()
        );

        btnEditar.addActionListener(
                e -> editarEntidad()
        );

        btnDetalle.addActionListener(
                e -> verDetalleEntidad()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntidad()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnDetalle);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarEntidades() {

        try {

            modeloTabla.setRowCount(0);

            String nombreFiltro =
                    txtNombreFiltro
                            .getText()
                            .trim()
                            .toUpperCase();

            String tipoFiltro =
                    obtenerSeleccion(
                            cmbTipoFiltro
                    );

            String estadoFiltro =
                    obtenerSeleccion(
                            cmbEstadoFiltro
                    );

            for (
                    EntidadMaquinariaAPI.EntidadResumen entidad
                    : EntidadMaquinariaAPI.obtenerTodas()
            ) {

                if (
                    !nombreFiltro.isEmpty()
                    && !entidad.nombre()
                            .toUpperCase()
                            .contains(
                                    nombreFiltro
                            )
                ) {

                    continue;
                }

                if (
                    !tipoFiltro.equalsIgnoreCase(
                            "Todos"
                    )
                    && !entidad.tipoEntidad()
                            .equalsIgnoreCase(
                                    tipoFiltro
                            )
                ) {

                    continue;
                }

                if (
                    !estadoFiltro.equalsIgnoreCase(
                            "Todos"
                    )
                    && !entidad.estado()
                            .equalsIgnoreCase(
                                    estadoFiltro
                            )
                ) {

                    continue;
                }

                modeloTabla.addRow(
                        new Object[]{
                                entidad.idEntidad(),
                                entidad.nombre(),
                                entidad.tipoEntidad(),
                                entidad.identificacion(),
                                entidad.telefono(),
                                entidad.correo(),
                                entidad.estado()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar proveedores y propietarios:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private String obtenerSeleccion(
            JComboBox<String> combo
    ) {

        Object seleccionado =
                combo.getSelectedItem();

        return seleccionado == null
                ? ""
                : seleccionado.toString();
    }

    private void validarSeleccionTemporal(
            String accion
    ) {

        if (
            tabla.getSelectedRow() == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entidad en la tabla.",
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
    private void abrirNuevaEntidad() {

    FormEntidadMaquinaria formulario =
            new FormEntidadMaquinaria(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarEntidades();
    }
}
private void editarEntidad() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una entidad.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int idEntidad =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    fila,
                                    0
                            )
                            .toString()
            );

    FormEntidadMaquinaria formulario =
            new FormEntidadMaquinaria(
                    SwingUtilities.getWindowAncestor(this),
                    idEntidad
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarEntidades();
    }
}
private void verDetalleEntidad() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una entidad.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int idEntidad =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    fila,
                                    0
                            )
                            .toString()
            );

    FormEntidadMaquinaria formulario =
            new FormEntidadMaquinaria(
                    SwingUtilities.getWindowAncestor(this),
                    idEntidad,
                    true
            );

    formulario.setVisible(true);
}
private void eliminarEntidad() {

    int fila =
            tabla.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una entidad.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    fila
            );

    int idEntidad =
            Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    filaModelo,
                                    0
                            )
                            .toString()
            );

    String nombreEntidad =
            modeloTabla
                    .getValueAt(
                            filaModelo,
                            1
                    )
                    .toString();

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar la entidad \""
                            + nombreEntidad
                            + "\"?\n\n"
                            + "La entidad quedará INACTIVA y "
                            + "permanecerá almacenada en MySQL.",
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

        EntidadMaquinariaAPI.eliminarLogico(
                idEntidad
        );

        cargarEntidades();

        JOptionPane.showMessageDialog(
                this,
                "Entidad eliminada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "No fue posible eliminar la entidad:\n"
                        + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
}
}
