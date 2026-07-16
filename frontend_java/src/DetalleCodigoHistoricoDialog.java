import javax.swing.*;
import java.awt.*;

public class DetalleCodigoHistoricoDialog
        extends JDialog {

    public DetalleCodigoHistoricoDialog(
            Window parent,
            int idCodigoHistorico
    ) {

        super(
                parent,
                "Detalle del código histórico",
                ModalityType.APPLICATION_MODAL
        );

        setSize(850, 580);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        cargarDetalle(
                idCodigoHistorico
        );
    }

    private void cargarDetalle(
            int idCodigoHistorico
    ) {

        try {

            CodigoHistoricoDAO.CodigoHistoricoDetalle detalle =
                    CodigoHistoricoDAO.obtenerPorId(
                            idCodigoHistorico
                    );

            JPanel principal =
                    new JPanel(
                            new BorderLayout(
                                    12,
                                    12
                            )
                    );

            principal.setBorder(
                    BorderFactory.createEmptyBorder(
                            18,
                            18,
                            18,
                            18
                    )
            );

            JPanel datos =
                    new JPanel(
                            new GridLayout(
                                    8,
                                    2,
                                    12,
                                    10
                            )
                    );

            datos.setBorder(
                    BorderFactory.createTitledBorder(
                            "Información del código histórico"
                    )
            );

            agregarDato(
                    datos,
                    "ID:",
                    String.valueOf(
                            detalle.idCodigoHistorico()
                    )
            );

            agregarDato(
                    datos,
                    "Proveedor histórico:",
                    detalle.proveedorOriginal()
            );

            agregarDato(
                    datos,
                    "Código anterior:",
                    detalle.codigoAnterior()
            );

            agregarDato(
                    datos,
                    "Código actual:",
                    detalle.codigoActualOrigen()
            );

            agregarDato(
                    datos,
                    "Maquinaria vinculada:",
                    detalle.maquinariaActual()
            );

            agregarDato(
                    datos,
                    "Descripción histórica:",
                    detalle.descripcionOriginal()
            );

            agregarDato(
                    datos,
                    "Costo hora histórico:",
                    String.format(
                            "$%.2f",
                            detalle.costoHoraOriginal()
                    )
            );

            agregarDato(
                    datos,
                    "Estado de vinculación:",
                    detalle.estadoVinculacion()
            );

            principal.add(
                    datos,
                    BorderLayout.NORTH
            );

            JTextArea observaciones =
                    new JTextArea(
                            detalle.observaciones()
                    );

            observaciones.setEditable(false);
            observaciones.setLineWrap(true);
            observaciones.setWrapStyleWord(true);
            observaciones.setBackground(
                    new Color(
                            245,
                            245,
                            245
                    )
            );

            JScrollPane scroll =
                    new JScrollPane(
                            observaciones
                    );

            scroll.setBorder(
                    BorderFactory.createTitledBorder(
                            "Observaciones"
                    )
            );

            principal.add(
                    scroll,
                    BorderLayout.CENTER
            );

            JPanel inferior =
                    new JPanel(
                            new BorderLayout()
                    );

            JLabel fecha =
                    new JLabel(
                            "Fecha de registro: "
                                    + detalle.fechaRegistro()
                    );

            JButton btnCerrar =
                    new JButton("Cerrar");

            btnCerrar.addActionListener(
                    e -> dispose()
            );

            JPanel panelCerrar =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.RIGHT
                            )
                    );

            panelCerrar.add(btnCerrar);

            inferior.add(
                    fecha,
                    BorderLayout.WEST
            );

            inferior.add(
                    panelCerrar,
                    BorderLayout.EAST
            );

            principal.add(
                    inferior,
                    BorderLayout.SOUTH
            );

            setContentPane(principal);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el detalle:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
            dispose();
        }
    }

    private void agregarDato(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        JLabel lblEtiqueta =
                new JLabel(etiqueta);

        lblEtiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel lblValor =
                new JLabel(
                        valor == null
                                || valor.isBlank()
                                ? "-"
                                : valor
                );

        panel.add(lblEtiqueta);
        panel.add(lblValor);
    }
}
