import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SelectorGuiasAprobadasDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;

    private ControlDiarioAPI.GuiaAprobadaItem guiaSeleccionada;

    private final List<ControlDiarioAPI.GuiaAprobadaItem> guias =
            new ArrayList<>();

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SelectorGuiasAprobadasDialog(
            Window owner
    ) {

        super(
                owner,
                "Seleccionar Guía Aprobada",
                ModalityType.APPLICATION_MODAL
        );

        setSize(
                1150,
                550
        );

        setLocationRelativeTo(owner);

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        crearInterfaz();
        cargarGuias();
    }

    private void crearInterfaz() {

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        JLabel titulo =
                new JLabel(
                        "GUÍAS APROBADAS DISPONIBLES",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        add(
                titulo,
                BorderLayout.NORTH
        );

        modelo =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Fecha",
                                "Empresa",
                                "Tipo",
                                "N° Guía",
                                "Proyecto",
                                "Sector",
                                "Material",
                                "Chofer",
                                "Placa",
                                "m³"
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
                new JTable(modelo);

        tabla.setRowHeight(26);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll =
                new JScrollPane(tabla);

        add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton btnAceptar =
                new JButton("Aceptar");

        JButton btnCancelar =
                new JButton("Cancelar");

        botones.add(btnAceptar);
        botones.add(btnCancelar);

        add(
                botones,
                BorderLayout.SOUTH
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnAceptar.addActionListener(
                e -> seleccionar()
        );

        tabla.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent evento
                    ) {

                        if (evento.getClickCount() == 2) {
                            seleccionar();
                        }
                    }
                }
        );
    }

    private void cargarGuias() {

        try {

            modelo.setRowCount(0);
            guias.clear();

            guias.addAll(
                    ControlDiarioAPI.obtenerGuiasAprobadas()
            );

            for (
                    ControlDiarioAPI.GuiaAprobadaItem guia
                    : guias
            ) {

                modelo.addRow(
                        new Object[]{
                                guia.idGuia(),
                                guia.fecha() == null
                                        ? ""
                                        : guia.fecha().format(FORMATO),
                                guia.empresa(),
                                guia.tipoGuia(),
                                guia.numeroGuia(),
                                guia.proyectoReferencia(),
                                guia.sector(),
                                guia.material(),
                                guia.choferOperador(),
                                guia.placa(),
                                guia.m3()
                        }
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las guías aprobadas:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    private void seleccionar() {

        int filaVista =
                tabla.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una guía.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(
                        filaVista
                );

        if (
                filaModelo < 0
                || filaModelo >= guias.size()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible identificar la guía seleccionada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        guiaSeleccionada =
                guias.get(filaModelo);

        dispose();
    }

    public ControlDiarioAPI.GuiaAprobadaItem
    getGuiaSeleccionada() {

        return guiaSeleccionada;
    }
}
