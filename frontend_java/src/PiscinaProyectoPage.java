import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PiscinaProyectoPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PiscinaProyectoPage(
            Runnable accionVolver
    ) {

        this.accionVolver = accionVolver;

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
        cargarPiscinas();
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
                        "Catálogo de Piscinas"
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
                                "Sector",
                                "Piscina",
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
                        "Nueva piscina"
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
                e -> nuevaPiscina()
        );

        btnEditar.addActionListener(
                e -> editarPiscina()
        );

        btnEliminar.addActionListener(
                e -> eliminarPiscina()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarPiscinas() {

        try {

            modeloTabla.setRowCount(0);

            for (
                    PiscinaProyectoDAO.PiscinaResumen piscina
                    : PiscinaProyectoDAO.obtenerActivas()
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                piscina.idPiscina(),
                                piscina.nombreSector(),
                                piscina.nombrePiscina(),
                                piscina.descripcion()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar piscinas:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void nuevaPiscina() {

        JComboBox<ProyectoDAO.SectorItem> cmbSector =
                new JComboBox<>();

        JTextField txtNombre =
                new JTextField();

        JTextArea txtDescripcion =
                new JTextArea(
                        5,
                        25
                );

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        try {

            for (
                    ProyectoDAO.SectorItem sector
                    : ProyectoDAO.obtenerSectores()
            ) {

                cmbSector.addItem(sector);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar sectores:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JPanel datos =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                5,
                                5
                        )
                );

        datos.add(
                new JLabel(
                        "Sector:"
                )
        );

        datos.add(cmbSector);

        datos.add(
                new JLabel(
                        "Nombre de la piscina:"
                )
        );

        datos.add(txtNombre);

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                8,
                                8
                        )
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
                        "Nueva piscina",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
            respuesta
            != JOptionPane.OK_OPTION
        ) {
            return;
        }

        ProyectoDAO.SectorItem sector =
                (ProyectoDAO.SectorItem)
                        cmbSector.getSelectedItem();

        if (sector == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un sector.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            PiscinaProyectoDAO.insertar(
                    sector.idSector(),
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim()
            );

            cargarPiscinas();

            JOptionPane.showMessageDialog(
                    this,
                    "Piscina guardada correctamente.",
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

    private void editarPiscina() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una piscina.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idPiscina =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        try {

            PiscinaProyectoDAO.PiscinaResumen piscina =
                    PiscinaProyectoDAO.obtenerPorId(
                            idPiscina
                    );

            JComboBox<ProyectoDAO.SectorItem> cmbSector =
                    new JComboBox<>();

            for (
                    ProyectoDAO.SectorItem sector
                    : ProyectoDAO.obtenerSectores()
            ) {

                cmbSector.addItem(sector);

                if (
                    sector.idSector()
                            == piscina.idSector()
                ) {

                    cmbSector.setSelectedItem(
                            sector
                    );
                }
            }

            JTextField txtNombre =
                    new JTextField(
                            piscina.nombrePiscina()
                    );

            JTextArea txtDescripcion =
                    new JTextArea(
                            piscina.descripcion(),
                            5,
                            25
                    );

            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);

            JPanel datos =
                    new JPanel(
                            new GridLayout(
                                    4,
                                    1,
                                    5,
                                    5
                            )
                    );

            datos.add(
                    new JLabel(
                            "Sector:"
                    )
            );

            datos.add(cmbSector);

            datos.add(
                    new JLabel(
                            "Nombre de la piscina:"
                    )
            );

            datos.add(txtNombre);

            JPanel panel =
                    new JPanel(
                            new BorderLayout(
                                    8,
                                    8
                            )
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
                            "Editar piscina",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (
                respuesta
                != JOptionPane.OK_OPTION
            ) {
                return;
            }

            ProyectoDAO.SectorItem sector =
                    (ProyectoDAO.SectorItem)
                            cmbSector.getSelectedItem();

            if (sector == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe seleccionar un sector.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            PiscinaProyectoDAO.actualizar(
                    idPiscina,
                    sector.idSector(),
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim()
            );

            cargarPiscinas();

            JOptionPane.showMessageDialog(
                    this,
                    "Piscina actualizada correctamente.",
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

    private void eliminarPiscina() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una piscina.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idPiscina =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        String nombrePiscina =
                modeloTabla
                        .getValueAt(
                                filaModelo,
                                2
                        )
                        .toString();

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar la piscina "
                                + nombrePiscina
                                + "?\n\n"
                                + "La piscina se desactivará "
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

        try {

            PiscinaProyectoDAO.eliminar(
                    idPiscina
            );

            cargarPiscinas();

            JOptionPane.showMessageDialog(
                    this,
                    "Piscina eliminada correctamente.",
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