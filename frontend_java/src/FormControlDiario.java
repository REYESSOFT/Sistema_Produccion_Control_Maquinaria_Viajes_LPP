import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FormControlDiario extends JDialog {

    private final JComboBox<
            ControlDiarioDAO.ProyectoItem
            > cmbProyecto;

    private final JTextField txtFecha;
    private final JTextField txtMetrosLineales;
    private final JTextField txtAncho;
    private final JTextField txtEspesor;

    private final JTextArea txtObservaciones;

    private final JButton btnGuardar;
    private final JButton btnCancelar;

    private Integer idControl;
    private final boolean modoDetalle;

    private boolean guardado;
    private SelectorGuiasAprobadasDAO.GuiaAprobadaItem guiaSeleccionada;

    /*
     * NUEVO
     */
    public FormControlDiario(
            Window owner
    ) {

        this(
                owner,
                null,
                false
        );
    }

    /*
     * EDITAR
     */
    public FormControlDiario(
            Window owner,
            int idControl
    ) {

        this(
                owner,
                idControl,
                false
        );
    }

    /*
     * DETALLE
     */
    public FormControlDiario(
            Window owner,
            Integer idControl,
            boolean modoDetalle
    ) {

        super(
                owner,
                modoDetalle
                        ? "Detalle del Control Diario"
                        : idControl != null
                        && idControl > 0
                                ? "Editar Control Diario"
                                : "Nuevo Control Diario",
                ModalityType.APPLICATION_MODAL
        );

        this.idControl =
                        idControl != null
                        && idControl > 0
                                ? idControl
                                : null;

        this.modoDetalle =
                modoDetalle;

        this.guardado =
                false;

        setSize(
                650,
                500
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(
                owner
        );

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        /*
         * PANEL FORMULARIO
         */
        JPanel panelFormulario =
                new JPanel(
                        new GridBagLayout()
                );

        panelFormulario.setBorder(
                javax.swing.BorderFactory
                        .createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        /*
         * PROYECTO
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;

        panelFormulario.add(
                new JLabel(
                        "Proyecto:"
                ),
                gbc
        );

        cmbProyecto =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbProyecto,
                gbc
        );

        /*
         * FECHA
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Fecha (AAAA-MM-DD):"
                ),
                gbc
        );

        txtFecha =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtFecha,
                gbc
        );

        /*
         * METROS LINEALES
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Metros lineales:"
                ),
                gbc
        );

        txtMetrosLineales =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                txtMetrosLineales,
                gbc
        );

        /*
         * ANCHO
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Ancho:"
                ),
                gbc
        );

        txtAncho =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtAncho,
                gbc
        );

        /*
         * ESPESOR
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Espesor:"
                ),
                gbc
        );

        txtEspesor =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;

        panelFormulario.add(
                txtEspesor,
                gbc
        );

        /*
         * OBSERVACIONES
         */
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill =
                GridBagConstraints.HORIZONTAL;
        gbc.anchor =
                GridBagConstraints.NORTHWEST;

        panelFormulario.add(
                new JLabel(
                        "Observaciones:"
                ),
                gbc
        );

        txtObservaciones =
                new JTextArea(
                        5,
                        30
                );

        txtObservaciones.setLineWrap(
                true
        );

        txtObservaciones.setWrapStyleWord(
                true
        );

        JScrollPane scrollObservaciones =
                new JScrollPane(
                        txtObservaciones
                );

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill =
                GridBagConstraints.BOTH;

        panelFormulario.add(
                scrollObservaciones,
                gbc
        );

        add(
                panelFormulario,
                BorderLayout.CENTER
        );

        /*
         * BOTONES
         */
        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btnGuardar =
                new JButton(
                        this.idControl != null
                                ? "Actualizar"
                                : "Guardar"
                );

        btnCancelar =
                new JButton(
                        modoDetalle
                                ? "Cerrar"
                                : "Cancelar"
                );

        if (!modoDetalle) {

            panelBotones.add(
                    btnGuardar
            );
        }

        panelBotones.add(
                btnCancelar
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        /*
         * CARGA INICIAL
         */
        cargarProyectos();

        if (this.idControl != null) {

            cargarControl();
        } else {

            txtFecha.setText(
                    LocalDate.now().toString()
            );
        }

        if (modoDetalle) {

            activarModoDetalle();
        }

        /*
         * EVENTOS
         */
        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnGuardar.addActionListener(
                e -> guardarControl()
        );
    }

    private void cargarProyectos() {

        try {

            cmbProyecto.removeAllItems();

            for (
                    ControlDiarioDAO.ProyectoItem proyecto
                    : ControlDiarioDAO
                            .obtenerProyectosActivos()
            ) {

                cmbProyecto.addItem(
                        proyecto
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar proyectos:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarControl() {

        try {

            ControlDiarioDAO.ControlDiarioDetalle detalle =
                    ControlDiarioDAO.obtenerPorId(
                            idControl
                    );

            seleccionarProyecto(
                    detalle.idProyecto()
            );

            txtFecha.setText(
                    detalle.fecha() == null
                            ? ""
                            : detalle.fecha().toString()
            );

            txtMetrosLineales.setText(
                    String.valueOf(
                            detalle.metrosLineales()
                    )
            );

            txtAncho.setText(
                    detalle.ancho() == null
                            ? ""
                            : detalle.ancho().toString()
            );

            txtEspesor.setText(
                    detalle.espesor() == null
                            ? ""
                            : detalle.espesor().toString()
            );

            txtObservaciones.setText(
                    detalle.observaciones() == null
                            ? ""
                            : detalle.observaciones()
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el Control Diario:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            dispose();
        }
    }

    private void seleccionarProyecto(
            int idProyecto
    ) {

        for (
                int i = 0;
                i < cmbProyecto.getItemCount();
                i++
        ) {

            ControlDiarioDAO.ProyectoItem proyecto =
                    cmbProyecto.getItemAt(i);

            if (
                    proyecto.idProyecto()
                    == idProyecto
            ) {

                cmbProyecto.setSelectedIndex(i);
                return;
            }
        }
    }
    public void cargarDesdeGuia(
        SelectorGuiasAprobadasDAO.GuiaAprobadaItem guia
) {

    this.guiaSeleccionada = guia;

    if (guia == null) {
        return;
    }

    if (guia.fecha() != null) {
        txtFecha.setText(
                guia.fecha().toString()
        );
    }

    txtObservaciones.setText(
            """
            Guía: %s
            Empresa: %s
            Tipo: %s
            Proyecto: %s
            Sector: %s
            Material: %s
            Chofer: %s
            Placa: %s
            m³: %.2f
            """.formatted(
                    guia.numeroGuia(),
                    guia.empresa(),
                    guia.tipoGuia(),
                    guia.proyectoReferencia(),
                    guia.sector(),
                    guia.material(),
                    guia.choferOperador(),
                    guia.placa(),
                    guia.m3()
            )
    );

    for (int i = 0; i < cmbProyecto.getItemCount(); i++) {

        ControlDiarioDAO.ProyectoItem proyecto =
                cmbProyecto.getItemAt(i);

        String texto = proyecto.toString().toUpperCase();

        if (texto.contains(
                guia.proyectoReferencia().toUpperCase()
        )) {

            cmbProyecto.setSelectedIndex(i);
            break;
        }
    }
}

    private void guardarControl() {

        try {

            ControlDiarioDAO.ProyectoItem proyecto =
                    (ControlDiarioDAO.ProyectoItem)
                            cmbProyecto.getSelectedItem();

            if (proyecto == null) {

                throw new Exception(
                        "Debe seleccionar un proyecto."
                );
            }

            String fechaTexto =
                    txtFecha
                            .getText()
                            .trim();

            if (fechaTexto.isBlank()) {

                throw new Exception(
                        "La fecha es obligatoria."
                );
            }

            LocalDate fechaControl =
                    LocalDate.parse(
                            fechaTexto
                    );

            String metrosTexto =
                    txtMetrosLineales
                            .getText()
                            .trim();

            if (metrosTexto.isBlank()) {

                throw new Exception(
                        "Debe ingresar los metros lineales."
                );
            }

            double metrosLineales =
                    Double.parseDouble(
                            metrosTexto.replace(
                                    ",",
                                    "."
                            )
                    );

            Double ancho =
                    convertirDoubleOpcional(
                            txtAncho.getText()
                    );

            Double espesor =
                    convertirDoubleOpcional(
                            txtEspesor.getText()
                    );

           if (idControl == null) {

    this.idControl =
        ControlDiarioDAO.insertar(
                guiaSeleccionada == null
                        ? null
                        : guiaSeleccionada.idGuia(),
                proyecto.idProyecto(),
                fechaControl,
                metrosLineales,
                ancho,
                espesor,
                txtObservaciones.getText()
        );

    JOptionPane.showMessageDialog(
            this,
            "Control Diario guardado correctamente.",
            "LPP Smart ERP",
            JOptionPane.INFORMATION_MESSAGE
    );

} else {

    ControlDiarioDAO.actualizar(
            idControl,
            proyecto.idProyecto(),
            fechaControl,
            metrosLineales,
            ancho,
            espesor,
            txtObservaciones.getText()
    );

    JOptionPane.showMessageDialog(
            this,
            "Control Diario actualizado correctamente.",
            "LPP Smart ERP",
            JOptionPane.INFORMATION_MESSAGE
    );
}

            guardado =
        true;

DetalleControlDiarioMaquinariaDialog dialogoMaquinaria =
        new DetalleControlDiarioMaquinariaDialog(
                this,
                idControl
        );

dialogoMaquinaria.setVisible(
        true
);

DetalleControlDiarioMaterialDialog dialogoMaterial =
        new DetalleControlDiarioMaterialDialog(
                this,
                idControl
        );

dialogoMaterial.setVisible(
        true
);

dispose();

        } catch (
                java.time.format
                        .DateTimeParseException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato AAAA-MM-DD.\n"
                            + "Ejemplo: 2026-07-20",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Revise los valores numéricos.\n"
                            + "Metros lineales, ancho y espesor "
                            + "deben contener números válidos.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
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

    private Double convertirDoubleOpcional(
            String texto
    ) {

        if (
                texto == null
                || texto.isBlank()
        ) {

            return null;
        }

        return Double.parseDouble(
                texto
                        .trim()
                        .replace(
                                ",",
                                "."
                        )
        );
    }

    private void activarModoDetalle() {

        cmbProyecto.setEnabled(false);

        txtFecha.setEditable(false);
        txtMetrosLineales.setEditable(false);
        txtAncho.setEditable(false);
        txtEspesor.setEditable(false);
        txtObservaciones.setEditable(false);
    }

    public boolean isGuardado() {

        return guardado;
    }
}