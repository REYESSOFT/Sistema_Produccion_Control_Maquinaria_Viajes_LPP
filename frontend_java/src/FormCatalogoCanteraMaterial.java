import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormCatalogoCanteraMaterial extends JDialog {

    private final Integer idTarifa;

    private final JTextField txtCantera;
    private final JTextField txtMaterial;
    private final JTextField txtDestinoSector;
    private final JTextField txtCostoUnitarioMaterial;
    private final JTextField txtCostoUnitarioTransporte;

    private final JButton btnGuardar;
    private final JButton btnCancelar;

    private boolean guardado;

    /*
     * NUEVO
     */
    public FormCatalogoCanteraMaterial(
            Window owner
    ) {

        this(
                owner,
                null
        );
    }

    /*
     * NUEVO / EDITAR
     */
    public FormCatalogoCanteraMaterial(
            Window owner,
            Integer idTarifa
    ) {

        super(
                owner,
                idTarifa == null
                        ? "Nueva tarifa de material pétreo"
                        : "Editar tarifa de material pétreo",
                ModalityType.APPLICATION_MODAL
        );

        this.idTarifa =
                idTarifa != null
                && idTarifa > 0
                        ? idTarifa
                        : null;

        this.guardado =
                false;

        setSize(
                620,
                430
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
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
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
         * CANTERA
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridy = 0;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantera,
                gbc
        );

        /*
         * MATERIAL
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Tipo de material:"
                ),
                gbc
        );

        txtMaterial =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtMaterial,
                gbc
        );

        /*
         * DESTINO / SECTOR
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Destino / sector:"
                ),
                gbc
        );

        txtDestinoSector =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                txtDestinoSector,
                gbc
        );

        /*
         * COSTO UNITARIO MATERIAL
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
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
        gbc.gridy = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioMaterial,
                gbc
        );

        /*
         * COSTO UNITARIO TRANSPORTE
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        gbc.gridy = 4;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioTransporte,
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
                        this.idTarifa == null
                                ? "Guardar"
                                : "Actualizar"
                );

        btnCancelar =
                new JButton(
                        "Cancelar"
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
        if (this.idTarifa != null) {

            cargarTarifa();

        } else {

            txtCostoUnitarioMaterial.setText(
                    "0"
            );

            txtCostoUnitarioTransporte.setText(
                    "0"
            );
        }

        /*
         * EVENTOS
         */
        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnGuardar.addActionListener(
                e -> guardarTarifa()
        );
    }

    private void cargarTarifa() {

        try {

            CatalogoCanteraMaterialDAO.TarifaDetalle detalle =
                    CatalogoCanteraMaterialDAO.obtenerPorId(
                            idTarifa
                    );

            txtCantera.setText(
                    detalle.cantera()
            );

            txtMaterial.setText(
                    detalle.material()
            );

            txtDestinoSector.setText(
                    detalle.destinoSector()
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

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la tarifa:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

            dispose();
        }
    }

    private void guardarTarifa() {

        try {

            String cantera =
                    txtCantera
                            .getText()
                            .trim();

            String material =
                    txtMaterial
                            .getText()
                            .trim();

            String destinoSector =
                    txtDestinoSector
                            .getText()
                            .trim();

            double costoUnitarioMaterial =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioMaterial.getText(),
                            "el costo unitario del material"
                    );

            double costoUnitarioTransporte =
                    convertirDoubleObligatorio(
                            txtCostoUnitarioTransporte.getText(),
                            "el costo unitario del transporte"
                    );

            if (cantera.isBlank()) {

                throw new Exception(
                        "Debe ingresar la cantera."
                );
            }

            if (material.isBlank()) {

                throw new Exception(
                        "Debe ingresar el tipo de material."
                );
            }

            if (destinoSector.isBlank()) {

                throw new Exception(
                        "Debe ingresar el destino o sector."
                );
            }

            if (idTarifa == null) {

                CatalogoCanteraMaterialDAO.insertar(
                        cantera,
                        material,
                        destinoSector,
                        costoUnitarioMaterial,
                        costoUnitarioTransporte
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Tarifa guardada correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                CatalogoCanteraMaterialDAO.actualizar(
                        idTarifa,
                        cantera,
                        material,
                        destinoSector,
                        costoUnitarioMaterial,
                        costoUnitarioTransporte
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Tarifa actualizada correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            guardado =
                    true;

            dispose();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Revise los costos ingresados. "
                            + "Deben contener únicamente números.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
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

    private String formatearNumeroEdicion(
            double valor
    ) {

        return String.valueOf(
                valor
        );
    }

    public boolean isGuardado() {

        return guardado;
    }
}
