import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TipoActividadProyectoPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public TipoActividadProyectoPage(
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
        cargarTiposActividad();
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

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Tipos de Actividad de Proyecto"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
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
                                "Tipo de actividad",
                                "Descripción"
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
                .setPreferredWidth(60);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(260);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(500);

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

        JButton btnNuevo =
                new JButton(
                        "Nuevo tipo"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );

        btnNuevo.addActionListener(
                e -> nuevoTipoActividad()
        );

        btnEditar.addActionListener(
                e -> editarTipoActividad()
        );

        btnEliminar.addActionListener(
                e -> eliminarTipoActividad()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarTiposActividad() {

        try {

            modeloTabla.setRowCount(0);

            for (
                    TipoActividadResumen actividad
                    : TipoActividadProyectoAPI.obtenerActivos()
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                actividad.idTipoActividad(),
                                actividad.nombreActividad(),
                                actividad.descripcion()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los tipos de actividad:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void nuevoTipoActividad() {

        JTextField txtNombre =
                new JTextField();

        JTextArea txtDescripcion =
                new JTextArea(
                        5,
                        25
                );

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                8,
                                8
                        )
                );

        JPanel datos =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                5,
                                5
                        )
                );

        datos.add(
                new JLabel(
                        "Nombre del tipo de actividad:"
                )
        );

        datos.add(
                txtNombre
        );

        panel.add(
                datos,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(
                        txtDescripcion
                ),
                BorderLayout.CENTER
        );

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Nuevo tipo de actividad",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
                respuesta
                        != JOptionPane.OK_OPTION
        ) {

            return;
        }

        try {

            TipoActividadProyectoAPI.insertar(
                    txtNombre
                            .getText()
                            .trim(),
                    txtDescripcion
                            .getText()
                            .trim()
            );

            cargarTiposActividad();

            JOptionPane.showMessageDialog(
                    this,
                    "Tipo de actividad guardado correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void editarTipoActividad() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un tipo de actividad.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idTipoActividad =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        try {

            TipoActividadResumen actividad =
                    TipoActividadProyectoAPI.obtenerPorId(
                            idTipoActividad
                    );

            JTextField txtNombre =
                    new JTextField(
                            actividad.nombreActividad()
                    );

            JTextArea txtDescripcion =
                    new JTextArea(
                            actividad.descripcion(),
                            5,
                            25
                    );

            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);

            JPanel panel =
                    new JPanel(
                            new BorderLayout(
                                    8,
                                    8
                            )
                    );

            JPanel datos =
                    new JPanel(
                            new GridLayout(
                                    2,
                                    1,
                                    5,
                                    5
                            )
                    );

            datos.add(
                    new JLabel(
                            "Nombre del tipo de actividad:"
                    )
            );

            datos.add(
                    txtNombre
            );

            panel.add(
                    datos,
                    BorderLayout.NORTH
            );

            panel.add(
                    new JScrollPane(
                            txtDescripcion
                    ),
                    BorderLayout.CENTER
            );

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Editar tipo de actividad",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (
                    respuesta
                            != JOptionPane.OK_OPTION
            ) {

                return;
            }

            TipoActividadProyectoAPI.actualizar(
                    idTipoActividad,
                    txtNombre
                            .getText()
                            .trim(),
                    txtDescripcion
                            .getText()
                            .trim()
            );

            cargarTiposActividad();

            JOptionPane.showMessageDialog(
                    this,
                    "Tipo de actividad actualizado correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarTipoActividad() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un tipo de actividad.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idTipoActividad =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        String nombreActividad =
                modeloTabla
                        .getValueAt(
                                filaModelo,
                                1
                        )
                        .toString();

        try {

            int proyectosRelacionados =
                    TipoActividadProyectoAPI
                            .contarProyectosRelacionados(
                                    idTipoActividad
                            );

            if (proyectosRelacionados > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se puede eliminar este tipo de actividad.\n\n"
                                + "Está asignado a "
                                + proyectosRelacionados
                                + (
                                    proyectosRelacionados == 1
                                            ? " proyecto."
                                            : " proyectos."
                                )
                                + "\n\n"
                                + "Puede editar su descripción, "
                                + "pero debe conservarse para proteger el historial.",
                        "Tipo de actividad en uso",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea eliminar el tipo de actividad "
                                    + nombreActividad
                                    + "?\n\n"
                                    + "El registro se desactivará "
                                    + "y no se borrará físicamente.",
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

            TipoActividadProyectoAPI.eliminar(
                    idTipoActividad
            );

            cargarTiposActividad();

            JOptionPane.showMessageDialog(
                    this,
                    "Tipo de actividad eliminado correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}