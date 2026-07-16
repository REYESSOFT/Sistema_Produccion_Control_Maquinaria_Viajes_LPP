import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SectorProyectoPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public SectorProyectoPage(
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
        cargarSectores();
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
                        "Catálogo de Sectores"
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
                .setPreferredWidth(220);

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
                        "Nuevo sector"
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
                e -> nuevoSector()
        );

        btnEditar.addActionListener(
                e -> editarSector()
        );

        btnEliminar.addActionListener(
                e -> eliminarSector()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarSectores() {

        try {

            modeloTabla.setRowCount(0);

            for (
                    SectorProyectoDAO.SectorResumen sector
                    : SectorProyectoDAO.obtenerActivos()
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                sector.idSector(),
                                sector.nombreSector(),
                                sector.descripcion()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar sectores:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void nuevoSector() {

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
                        "Nombre del sector:"
                )
        );

        datos.add(txtNombre);

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
                        "Nuevo sector",
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

            SectorProyectoDAO.insertar(
                    txtNombre
                            .getText()
                            .trim(),
                    txtDescripcion
                            .getText()
                            .trim()
            );

            cargarSectores();

            JOptionPane.showMessageDialog(
                    this,
                    "Sector guardado correctamente.",
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

    private void editarSector() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un sector.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idSector =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        try {

            SectorProyectoDAO.SectorResumen sector =
                    SectorProyectoDAO.obtenerPorId(
                            idSector
                    );

            JTextField txtNombre =
                    new JTextField(
                            sector.nombreSector()
                    );

            JTextArea txtDescripcion =
                    new JTextArea(
                            sector.descripcion(),
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
                            "Nombre del sector:"
                    )
            );

            datos.add(txtNombre);

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
                            "Editar sector",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (
                respuesta
                != JOptionPane.OK_OPTION
            ) {
                return;
            }

            SectorProyectoDAO.actualizar(
                    idSector,
                    txtNombre
                            .getText()
                            .trim(),
                    txtDescripcion
                            .getText()
                            .trim()
            );

            cargarSectores();

            JOptionPane.showMessageDialog(
                    this,
                    "Sector actualizado correctamente.",
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

    private void eliminarSector() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un sector.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        fila
                );

        int idSector =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        String nombre =
                modeloTabla
                        .getValueAt(
                                filaModelo,
                                1
                        )
                        .toString();

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el sector "
                                + nombre
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

        try {

            SectorProyectoDAO.eliminar(
                    idSector
            );

            cargarSectores();

            JOptionPane.showMessageDialog(
                    this,
                    "Sector eliminado correctamente.",
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