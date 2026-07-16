import javax.swing.*;
import java.awt.*;

public class DetalleMaquinariaDialog extends JDialog {

    public DetalleMaquinariaDialog(
            Window parent,
            int idMaquinaria
    ) {

        super(
                parent,
                "Detalle de maquinaria",
                ModalityType.APPLICATION_MODAL
        );

        setSize(850, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarDetalle(idMaquinaria);
    }

    private void cargarDetalle(
            int idMaquinaria
    ) {

        try {

            MaquinariaDAO.MaquinariaDetalle m =
                    MaquinariaDAO.obtenerPorId(
                            idMaquinaria
                    );

            JPanel principal =
                    new JPanel(
                            new BorderLayout(10, 10)
                    );

            principal.setBorder(
                    BorderFactory.createEmptyBorder(
                            15,
                            15,
                            15,
                            15
                    )
            );

            JPanel datos =
                    new JPanel(
                            new GridLayout(
                                    10,
                                    4,
                                    10,
                                    8
                            )
                    );

            agregarDato(
                    datos,
                    "Código interno:",
                    m.codigoInterno()
            );

            agregarDato(
                    datos,
                    "Código actual:",
                    m.codigoActual()
            );

            agregarDato(
                    datos,
                    "Código / placa:",
                    m.codigoPlaca()
            );

            agregarDato(
                    datos,
                    "Descripción:",
                    m.descripcion()
            );

            agregarDato(
                    datos,
                    "Tipo:",
                    m.tipoMaquinaria()
            );

            agregarDato(
                    datos,
                    "Modelo:",
                    m.modelo()
            );

            agregarDato(
                    datos,
                    "Serie máquina:",
                    m.serieMaquina()
            );

            agregarDato(
                    datos,
                    "Serie actual:",
                    m.serieActual()
            );

            agregarDato(
                    datos,
                    "Horómetro:",
                    m.horometroActual() == null
                            ? ""
                            : m.horometroActual().toString()
            );

            agregarDato(
                    datos,
                    "Confirmado:",
                    m.horometroConfirmado()
                            ? "Sí"
                            : "No"
            );

            agregarDato(
                    datos,
                    "Proveedor:",
                    m.proveedor()
            );

            agregarDato(
                    datos,
                    "Propietario:",
                    m.propietario()
            );

            agregarDato(
                    datos,
                    "Tipo propiedad:",
                    m.tipoPropiedad()
            );

            agregarDato(
                    datos,
                    "Estado:",
                    m.estadoOperativo()
            );

            agregarDato(
                    datos,
                    "Costo hora proveedor:",
                    String.format(
                            "$%.2f",
                            m.costoHoraProveedor()
                    )
            );

            agregarDato(
                    datos,
                    "Precio hora cliente:",
                    String.format(
                            "$%.2f",
                            m.precioHoraCliente()
                    )
            );

            principal.add(
                    datos,
                    BorderLayout.NORTH
            );

            JTextArea observaciones =
                    new JTextArea(
                            m.observaciones()
                    );

            observaciones.setEditable(false);
            observaciones.setLineWrap(true);
            observaciones.setWrapStyleWord(true);

            JScrollPane scroll =
                    new JScrollPane(observaciones);

            scroll.setBorder(
                    BorderFactory.createTitledBorder(
                            "Observaciones"
                    )
            );

            principal.add(
                    scroll,
                    BorderLayout.CENTER
            );

            JButton cerrar =
                    new JButton("Cerrar");

            cerrar.addActionListener(
                    e -> dispose()
            );

            JPanel botones =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.RIGHT
                            )
                    );

            botones.add(cerrar);

            principal.add(
                    botones,
                    BorderLayout.SOUTH
            );

            setContentPane(principal);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al mostrar el detalle:\n"
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

        panel.add(new JLabel(etiqueta));

        panel.add(
                new JLabel(
                        valor == null
                                ? ""
                                : valor
                )
        );
    }
}
