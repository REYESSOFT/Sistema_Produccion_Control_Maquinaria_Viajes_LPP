import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
    private final JLabel lblNumeroGuia;
private final JLabel lblEmpresaGuia;
private final JLabel lblTipoGuia;
private final JLabel lblProyectoGuia;
private final JLabel lblSectorGuia;
private final JLabel lblMaterialGuia;
private final JLabel lblChoferGuia;
private final JLabel lblPlacaGuia;
private final JLabel lblM3Guia;

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
                780,
                680
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
        new JPanel();

panelFormulario.setLayout(
        new BoxLayout(
                panelFormulario,
                BoxLayout.Y_AXIS
        )
);

panelFormulario.setBorder(
        BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15
        )
);

/*
 * DATOS PROVENIENTES DE LA GUÍA
 */
JPanel panelDatosGuia =
        new JPanel(
                new GridBagLayout()
        );

panelDatosGuia.setBorder(
        BorderFactory.createTitledBorder(
                "Datos provenientes de la guía"
        )
);

GridBagConstraints gbcGuia =
        new GridBagConstraints();

gbcGuia.insets =
        new Insets(
                6,
                8,
                6,
                8
        );

gbcGuia.fill =
        GridBagConstraints.HORIZONTAL;

gbcGuia.anchor =
        GridBagConstraints.WEST;

/*
 * PROYECTO
 */
gbcGuia.gridx = 0;
gbcGuia.gridy = 0;
gbcGuia.weightx = 0;

panelDatosGuia.add(
        new JLabel("Proyecto:"),
        gbcGuia
);

cmbProyecto =
        new JComboBox<>();

gbcGuia.gridx = 1;
gbcGuia.gridy = 0;
gbcGuia.weightx = 1;

panelDatosGuia.add(
        cmbProyecto,
        gbcGuia
);

/*
 * FECHA
 */
gbcGuia.gridx = 0;
gbcGuia.gridy = 1;
gbcGuia.weightx = 0;

panelDatosGuia.add(
        new JLabel("Fecha (AAAA-MM-DD):"),
        gbcGuia
);

txtFecha =
        new JTextField();

gbcGuia.gridx = 1;
gbcGuia.gridy = 1;
gbcGuia.weightx = 1;

panelDatosGuia.add(
        txtFecha,
        gbcGuia
);

/*
/*
 * INFORMACIÓN ESTRUCTURADA DE LA GUÍA
 */
JPanel panelFichaGuia =
        new JPanel(
                new GridBagLayout()
        );

GridBagConstraints gbcFicha =
        new GridBagConstraints();

gbcFicha.insets =
        new Insets(
                4,
                8,
                4,
                8
        );

gbcFicha.anchor =
        GridBagConstraints.WEST;

gbcFicha.fill =
        GridBagConstraints.HORIZONTAL;

/*
 * COLUMNA IZQUIERDA
 */
gbcFicha.gridx = 0;
gbcFicha.gridy = 0;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("N° Guía:"),
        gbcFicha
);

lblNumeroGuia =
        new JLabel("-");

gbcFicha.gridx = 1;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblNumeroGuia,
        gbcFicha
);

gbcFicha.gridx = 0;
gbcFicha.gridy = 1;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Empresa:"),
        gbcFicha
);

lblEmpresaGuia =
        new JLabel("-");

gbcFicha.gridx = 1;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblEmpresaGuia,
        gbcFicha
);

gbcFicha.gridx = 0;
gbcFicha.gridy = 2;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Tipo de guía:"),
        gbcFicha
);

lblTipoGuia =
        new JLabel("-");

gbcFicha.gridx = 1;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblTipoGuia,
        gbcFicha
);

gbcFicha.gridx = 0;
gbcFicha.gridy = 3;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Proyecto referencia:"),
        gbcFicha
);

lblProyectoGuia =
        new JLabel("-");

gbcFicha.gridx = 1;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblProyectoGuia,
        gbcFicha
);

gbcFicha.gridx = 0;
gbcFicha.gridy = 4;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Sector:"),
        gbcFicha
);

lblSectorGuia =
        new JLabel("-");

gbcFicha.gridx = 1;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblSectorGuia,
        gbcFicha
);

/*
 * COLUMNA DERECHA
 */
gbcFicha.gridx = 2;
gbcFicha.gridy = 0;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Material:"),
        gbcFicha
);

lblMaterialGuia =
        new JLabel("-");

gbcFicha.gridx = 3;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblMaterialGuia,
        gbcFicha
);

gbcFicha.gridx = 2;
gbcFicha.gridy = 1;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Chofer:"),
        gbcFicha
);

lblChoferGuia =
        new JLabel("-");

gbcFicha.gridx = 3;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblChoferGuia,
        gbcFicha
);

gbcFicha.gridx = 2;
gbcFicha.gridy = 2;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("Placa:"),
        gbcFicha
);

lblPlacaGuia =
        new JLabel("-");

gbcFicha.gridx = 3;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblPlacaGuia,
        gbcFicha
);

gbcFicha.gridx = 2;
gbcFicha.gridy = 3;
gbcFicha.weightx = 0;

panelFichaGuia.add(
        new JLabel("m³ transportados:"),
        gbcFicha
);

lblM3Guia =
        new JLabel("-");

gbcFicha.gridx = 3;
gbcFicha.weightx = 1;

panelFichaGuia.add(
        lblM3Guia,
        gbcFicha
);

/*
 * AGREGAR FICHA AL PANEL DE GUÍA
 */
gbcGuia.gridx = 0;
gbcGuia.gridy = 2;
gbcGuia.gridwidth = 2;
gbcGuia.weightx = 1;
gbcGuia.weighty = 1;
gbcGuia.fill =
        GridBagConstraints.BOTH;

panelDatosGuia.add(
        panelFichaGuia,
        gbcGuia
);

/*
 * DATOS DEL CONTROL DIARIO
 */
JPanel panelControlDiario =
        new JPanel(
                new GridBagLayout()
        );

panelControlDiario.setBorder(
        BorderFactory.createTitledBorder(
                "Datos que debe completar Daniela"
        )
);

GridBagConstraints gbcControl =
        new GridBagConstraints();

gbcControl.insets =
        new Insets(
                6,
                8,
                6,
                8
        );

gbcControl.fill =
        GridBagConstraints.HORIZONTAL;

gbcControl.anchor =
        GridBagConstraints.WEST;

/*
 * METROS LINEALES
 */
gbcControl.gridx = 0;
gbcControl.gridy = 0;
gbcControl.weightx = 0;

panelControlDiario.add(
        new JLabel("Metros lineales:"),
        gbcControl
);

txtMetrosLineales =
        new JTextField();

gbcControl.gridx = 1;
gbcControl.gridy = 0;
gbcControl.weightx = 1;

panelControlDiario.add(
        txtMetrosLineales,
        gbcControl
);

/*
 * ANCHO
 */
gbcControl.gridx = 0;
gbcControl.gridy = 1;
gbcControl.weightx = 0;

panelControlDiario.add(
        new JLabel("Ancho:"),
        gbcControl
);

txtAncho =
        new JTextField();

gbcControl.gridx = 1;
gbcControl.gridy = 1;
gbcControl.weightx = 1;

panelControlDiario.add(
        txtAncho,
        gbcControl
);

/*
 * ESPESOR
 */
gbcControl.gridx = 0;
gbcControl.gridy = 2;
gbcControl.weightx = 0;

panelControlDiario.add(
        new JLabel("Espesor:"),
        gbcControl
);

txtEspesor =
        new JTextField();

gbcControl.gridx = 1;
gbcControl.gridy = 2;
gbcControl.weightx = 1;

panelControlDiario.add(
        txtEspesor,
        gbcControl
);

/*
 * OBSERVACIONES ADICIONALES
 */
gbcControl.gridx = 0;
gbcControl.gridy = 3;
gbcControl.weightx = 0;
gbcControl.weighty = 0;
gbcControl.anchor =
        GridBagConstraints.NORTHWEST;

panelControlDiario.add(
        new JLabel("Observaciones adicionales:"),
        gbcControl
);

txtObservaciones =
        new JTextArea(
                4,
                35
        );

txtObservaciones.setLineWrap(true);
txtObservaciones.setWrapStyleWord(true);

JScrollPane scrollObservaciones =
        new JScrollPane(
                txtObservaciones
        );

gbcControl.gridx = 1;
gbcControl.gridy = 3;
gbcControl.weightx = 1;
gbcControl.weighty = 1;
gbcControl.fill =
        GridBagConstraints.BOTH;

panelControlDiario.add(
        scrollObservaciones,
        gbcControl
);

panelFormulario.add(
        panelDatosGuia
);

panelFormulario.add(
        Box.createVerticalStrut(12)
);

panelFormulario.add(
        panelControlDiario
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

    lblNumeroGuia.setText(
        guia.numeroGuia()
);

lblEmpresaGuia.setText(
        guia.empresa()
);

lblTipoGuia.setText(
        guia.tipoGuia()
);

lblProyectoGuia.setText(
        guia.proyectoReferencia()
);

lblSectorGuia.setText(
        guia.sector()
);

lblMaterialGuia.setText(
        guia.material()
);

lblChoferGuia.setText(
        guia.choferOperador()
);

lblPlacaGuia.setText(
        guia.placa()
);

lblM3Guia.setText(
        String.format(
                "%.2f m³",
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

    ControlDiarioContexto.establecerGuia(
            guia.idGuia(),
            guia.numeroGuia(),
            guia.fecha() == null ? "" : guia.fecha().toString(),
            guia.empresa(),
            guia.tipoGuia(),
            guia.proyectoReferencia(),
            guia.sector(),
            guia.material(),
            guia.choferOperador(),
            guia.placa(),
            guia.m3()
    );
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