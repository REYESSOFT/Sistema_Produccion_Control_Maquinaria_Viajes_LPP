import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AsignacionMaquinariaPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    public AsignacionMaquinariaPage(
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
        cargarAsignaciones();
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
                        "Asignación de Maquinaria a Proyectos"
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
                                "Maquinaria",
                                "Propietario",
                                "Cantidad",
                                "Fecha ingreso",
                                "Fecha salida",
                                "Tarifa hora",
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
                .setPreferredWidth(45);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(240);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(260);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(160);

        return new JScrollPane(
                tabla
        );
    }

    private JPanel crearBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnNueva =
                new JButton(
                        "Nueva asignación"
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

        btnNueva.addActionListener(
                e -> abrirNuevaAsignacion()
        );

        btnEditar.addActionListener(
                e -> validarSeleccionTemporal(
                        "Editar"
                )
        );

        btnDetalle.addActionListener(
                e -> validarSeleccionTemporal(
                        "Detalle"
                )
        );

        btnEliminar.addActionListener(
                e -> validarSeleccionTemporal(
                        "Eliminar"
                )
        );

        panel.add(btnNueva);
        panel.add(btnEditar);
        panel.add(btnDetalle);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarAsignaciones() {

        try {

            modeloTabla.setRowCount(0);

            for (
                    AsignacionMaquinariaDAO.AsignacionResumen asignacion
                    : AsignacionMaquinariaDAO.obtenerActivas()
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                asignacion.idAsignacion(),
                                asignacion.proyecto(),
                                asignacion.maquinaria(),
                                asignacion.propietario(),
                                asignacion.cantidad(),
                                formatearFecha(
                                        asignacion.fechaIngreso()
                                ),
                                formatearFecha(
                                        asignacion.fechaSalida()
                                ),
                                formatearTarifa(
                                        asignacion.tarifaHora()
                                ),
                                asignacion.estado()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar asignaciones:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
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

    private String formatearTarifa(
            Double tarifa
    ) {

        return tarifa == null
                ? ""
                : String.format(
                        "$%.2f",
                        tarifa
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
                    "Seleccione una asignación "
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
    private void abrirNuevaAsignacion() {

    FormAsignacionMaquinaria formulario =
            new FormAsignacionMaquinaria(
                    SwingUtilities
                            .getWindowAncestor(this)
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarAsignaciones();
    }
}
}
