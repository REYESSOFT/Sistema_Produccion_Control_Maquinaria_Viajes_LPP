import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FormControlDiarioMaterial extends JDialog {

    private final int idControl;
    private final Integer idControlMaterial;

    private final JTextField txtMaterialRecibido;
    private final JTextField txtCantera;
    private final JTextField txtCantidadViajes;
    private final JTextField txtVolumenRecibido;
    private final JTextField txtCostoUnitarioMaterial;
    private final JTextField txtCostoUnitarioTransporte;
    private final JTextField txtCantidadVolquetas;
    private final JTextField txtHorasVolqueta;

    private final JTextArea txtObservaciones;

    private final JLabel lblCostoMaterial;
    private final JLabel lblCostoTransporte;
    private final JLabel lblCostoTotal;

    private final JButton btnGuardar;
    private final JButton btnCancelar;
    private final JButton btnCalcular;

    private boolean guardado;

    /*
     * NUEVO
     */
    public FormControlDiarioMaterial(
            Window owner,
            int idControl
    ) {

        this(
                owner,
                idControl,
                null
        );
    }

    /*
     * NUEVO / EDITAR
     */
    public FormControlDiarioMaterial(
            Window owner,
            int idControl,
            Integer idControlMaterial
    ) {

        super(
                owner,
                idControlMaterial == null
                        ? "Agregar material pétreo"
                        : "Editar material pétreo",
                ModalityType.APPLICATION_MODAL
        );

        this.idControl =
                idControl;

        this.idControlMaterial =
                idControlMaterial != null
                && idControlMaterial > 0
                        ? idControlMaterial
                        : null;

        this.guardado =
                false;

        setSize(
                760,
                690
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
                        7,
                        7,
                        7,
                        7
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        /*
         * MATERIAL RECIBIDO
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Material recibido:"
                ),
                gbc
        );

        txtMaterialRecibido =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        panelFormulario.add(
                txtMaterialRecibido,
                gbc
        );

        /*
         * CANTERA
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Cantera:"
                ),
                gbc
        );

        txtCantera =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantera,
                gbc
        );

        /*
         * CANTIDAD DE VIAJES
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Cantidad de viajes:"
                ),
                gbc
        );

        txtCantidadViajes =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantidadViajes,
                gbc
        );

        /*
         * VOLUMEN RECIBIDO
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Volumen recibido:"
                ),
                gbc
        );

        txtVolumenRecibido =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtVolumenRecibido,
                gbc
        );

        /*
         * COSTO UNITARIO MATERIAL
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Costo unitario material:"
                ),
                gbc
        );

        txtCostoUnitarioMaterial =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioMaterial,
                gbc
        );

        /*
         * COSTO UNITARIO TRANSPORTE
         */
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Costo unitario transporte:"
                ),
                gbc
        );

        txtCostoUnitarioTransporte =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioTransporte,
                gbc
        );

        /*
         * CANTIDAD DE VOLQUETAS
         */
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Cantidad de volquetas:"
                ),
                gbc
        );

        txtCantidadVolquetas =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantidadVolquetas,
                gbc
        );

        /*
         * HORAS DE VOLQUETA
         */
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Horas de volqueta:"
                ),
                gbc
        );

        txtHorasVolqueta =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1;

        panelFormulario.add(
                txtHorasVolqueta,
                gbc
        );

        /*
         * TOTALES
         */
        JPanel panelTotales =
                new JPanel(
                        new GridBagLayout()
                );

        panelTotales.setBorder(
                javax.swing.BorderFactory
                        .createTitledBorder(
                                "Costos calculados"
                        )
        );

        GridBagConstraints gbcTotal =
                new GridBagConstraints();

        gbcTotal.insets =
                new Insets(
                        5,
                        10,
                        5,
                        10
                );

        gbcTotal.anchor =
                GridBagConstraints.WEST;

        lblCostoMaterial =
                new JLabel(
                        "$0.00"
                );

        lblCostoTransporte =
                new JLabel(
                        "$0.00"
                );

        lblCostoTotal =
                new JLabel(
                        "$0.00"
                );

        gbcTotal.gridx = 0;
        gbcTotal.gridy = 0;

        panelTotales.add(
                new JLabel(
                        "Costo material:"
                ),
                gbcTotal
        );

        gbcTotal.gridx = 1;

        panelTotales.add(
                lblCostoMaterial,
                gbcTotal
        );

        gbcTotal.gridx = 0;
        gbcTotal.gridy = 1;

        panelTotales.add(
                new JLabel(
                        "Costo transporte:"
                ),
                gbcTotal
        );

        gbcTotal.gridx = 1;

        panelTotales.add(
                lblCostoTransporte,
                gbcTotal
        );

        gbcTotal.gridx = 0;
        gbcTotal.gridy = 2;

        panelTotales.add(
                new JLabel(
                        "Costo total:"
                ),
                gbcTotal
        );

        gbcTotal.gridx = 1;

        panelTotales.add(
                lblCostoTotal,
                gbcTotal
        );

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                panelTotales,
                gbc
        );

        /*
         * OBSERVACIONES
         */
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
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
                        4,
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
        gbc.gridy = 9;
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

        btnCalcular =
                new JButton(
                        "Calcular costos"
                );

        btnGuardar =
                new JButton(
                        this.idControlMaterial == null
                                ? "Guardar"
                                : "Actualizar"
                );

        btnCancelar =
                new JButton(
                        "Cancelar"
                );

        panelBotones.add(
                btnCalcular
        );

        panelBotones.add(
                btnGuardar
        );

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
        if (this.idControlMaterial != null) {

            cargarRegistro();

        } else {

            txtCantidadViajes.setText(
                    "0"
            );

            txtVolumenRecibido.setText(
                    "0"
            );

            txtCostoUnitarioMaterial.setText(
                    "0"
            );

            txtCostoUnitarioTransporte.setText(
                    "0"
            );

            txtCantidadVolquetas.setText(
                    "0"
            );

            txtHorasVolqueta.setText(
                    "0"
            );
        }

        /*
         * EVENTOS
         */
        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnCalcular.addActionListener(
                e -> calcularYMostrarCostos()
        );

        btnGuardar.addActionListener(
                e -> guardarRegistro()
        );
    }

    private void cargarRegistro() {

        try {

            ControlDiarioMaterialDAO.ControlMaterialDetalle detalle =
                    ControlDiarioMaterialDAO.obtenerPorId(
                            idControlMaterial
                    );

            txtMaterialRecibido.setText(
                    detalle.materialRecibido()
            );

            txtCantera.setText(
                    detalle.cantera()
            );

            txtCantidadViajes.setText(
                    formatearNumeroEdicion(
                            detalle.cantidadViajes()
                    )
            );

            txtVolumenRecibido.setText(
                    formatearNumeroEdicion(
                            detalle.volumenRecibido()
                    )
            );

            txtCostoUnitarioMaterial.setText(
                    formatearNumeroEdicion(
                            detalle.costoUnitarioMaterial()
                    )
            );

            txtCostoUnitarioTransporte.setText(
                    formatearNumeroEdicion(
                            detalle.costoUnitarioTransporte()
                    )
            );

            txtCantidadVolquetas.setText(
                    String.valueOf(
                            detalle.cantidadVolquetas()
                    )
            );

            txtHorasVolqueta.setText(
                    formatearNumeroEdicion(
                            detalle.horasVolqueta()
                    )
            );

            txtObservaciones.setText(
                    detalle.observaciones() == null
                            ? ""
                            : detalle.observaciones()
            );

            mostrarCostos(
                    detalle.costoMaterial(),
                    detalle.costoTransporte()
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el material pétreo:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            dispose();
        }
    }

    private void calcularYMostrarCostos() {

        try {

            double cantidadViajes =
                    convertirDoubleObligatorio(
                            txtCantidadViajes.getText(),
                            "cantidad de viajes"
                    );

            double costoUnitarioMaterial =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioMaterial.getText(),
                            "costo unitario del material"
                    );

            double costoUnitarioTransporte =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioTransporte.getText(),
                            "costo unitario del transporte"
                    );

            double costoMaterial =
                    cantidadViajes
                    * costoUnitarioMaterial;

            double costoTransporte =
                    cantidadViajes
                    * costoUnitarioTransporte;

            mostrarCostos(
                    costoMaterial,
                    costoTransporte
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void guardarRegistro() {

        try {

            String materialRecibido =
                    txtMaterialRecibido
                            .getText()
                            .trim();

            String cantera =
                    txtCantera
                            .getText()
                            .trim();

            double cantidadViajes =
                    convertirDoubleObligatorio(
                            txtCantidadViajes.getText(),
                            "cantidad de viajes"
                    );

            double volumenRecibido =
                    convertirDoubleObligatorio(
                            txtVolumenRecibido.getText(),
                            "volumen recibido"
                    );

            double costoUnitarioMaterial =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioMaterial.getText(),
                            "costo unitario del material"
                    );

            double costoUnitarioTransporte =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioTransporte.getText(),
                            "costo unitario del transporte"
                    );

            int cantidadVolquetas =
                    convertirEnteroObligatorio(
                            txtCantidadVolquetas.getText(),
                            "cantidad de volquetas"
                    );

            double horasVolqueta =
                    convertirDoubleObligatorio(
                            txtHorasVolqueta.getText(),
                            "horas de volqueta"
                    );

            if (idControlMaterial == null) {

                ControlDiarioMaterialDAO.insertar(
                        idControl,
                        materialRecibido,
                        cantera,
                        cantidadViajes,
                        volumenRecibido,
                        costoUnitarioMaterial,
                        costoUnitarioTransporte,
                        cantidadVolquetas,
                        horasVolqueta,
                        txtObservaciones.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Material pétreo guardado correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                ControlDiarioMaterialDAO.actualizar(
                        idControlMaterial,
                        idControl,
                        materialRecibido,
                        cantera,
                        cantidadViajes,
                        volumenRecibido,
                        costoUnitarioMaterial,
                        costoUnitarioTransporte,
                        cantidadVolquetas,
                        horasVolqueta,
                        txtObservaciones.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Material pétreo actualizado correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            guardado =
                    true;

            dispose();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Revise los valores numéricos ingresados.",
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

    private double convertirDoubleObligatorio(
            String texto,
            String nombreCampo
    ) throws Exception {

        if (
                texto == null
                || texto.isBlank()
        ) {

            throw new Exception(
                    "Debe ingresar "
                            + nombreCampo
                            + "."
            );
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

    private int convertirEnteroObligatorio(
            String texto,
            String nombreCampo
    ) throws Exception {

        if (
                texto == null
                || texto.isBlank()
        ) {

            throw new Exception(
                    "Debe ingresar "
                            + nombreCampo
                            + "."
            );
        }

        return Integer.parseInt(
                texto.trim()
        );
    }

    private void mostrarCostos(
            double costoMaterial,
            double costoTransporte
    ) {

        double costoTotal =
                costoMaterial
                + costoTransporte;

        lblCostoMaterial.setText(
                formatearMoneda(
                        costoMaterial
                )
        );

        lblCostoTransporte.setText(
                formatearMoneda(
                        costoTransporte
                )
        );

        lblCostoTotal.setText(
                formatearMoneda(
                        costoTotal
                )
        );
    }

    private String formatearNumeroEdicion(
            double valor
    ) {

        return String.valueOf(
                valor
        );
    }

    private String formatearMoneda(
            double valor
    ) {

        return String.format(
                "$%,.2f",
                valor
        );
    }

    public boolean isGuardado() {

        return guardado;
    }
}
