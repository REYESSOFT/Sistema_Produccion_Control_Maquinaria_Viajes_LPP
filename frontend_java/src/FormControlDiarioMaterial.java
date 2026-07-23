import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

    private final JComboBox<String> cmbCantera;
    private final JComboBox<String> cmbMaterial;
    private final JComboBox<String> cmbDestinoSector;

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

    private Integer idTarifaSeleccionada;
    private boolean cargandoCombos;
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

        this.idTarifaSeleccionada =
                null;

        this.cargandoCombos =
                false;

        this.guardado =
                false;

        setSize(
                760,
                750
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

        cmbCantera =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbCantera,
                gbc
        );

        /*
         * MATERIAL RECIBIDO
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Material recibido:"
                ),
                gbc
        );

        cmbMaterial =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbMaterial,
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

        cmbDestinoSector =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbDestinoSector,
                gbc
        );

        /*
         * CANTIDAD DE VIAJES
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
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
        gbc.gridy = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantidadViajes,
                gbc
        );

        /*
         * VOLUMEN RECIBIDO
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        gbc.gridy = 4;
        gbc.weightx = 1;

        panelFormulario.add(
                txtVolumenRecibido,
                gbc
        );

        /*
         * COSTO UNITARIO MATERIAL
         */
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Costo unitario material:"
                ),
                gbc
        );

        txtCostoUnitarioMaterial =
                new JTextField();

        txtCostoUnitarioMaterial.setEditable(
                false
        );

        txtCostoUnitarioMaterial.setBackground(
                new java.awt.Color(
                        240,
                        240,
                        240
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioMaterial,
                gbc
        );

        txtCostoUnitarioMaterial.setEditable(false);
        txtCostoUnitarioMaterial.setBackground(java.awt.Color.WHITE);

        /*
         * COSTO UNITARIO TRANSPORTE
         */
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel(
                        "Costo unitario transporte:"
                ),
                gbc
        );

        txtCostoUnitarioTransporte =
                new JTextField();

        txtCostoUnitarioTransporte.setEditable(
                false
        );

        txtCostoUnitarioTransporte.setBackground(
                new java.awt.Color(
                        240,
                        240,
                        240
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCostoUnitarioTransporte,
                gbc
        );
        txtCostoUnitarioTransporte.setEditable(false);
        txtCostoUnitarioTransporte.setBackground(java.awt.Color.WHITE);

        /*
         * CANTIDAD DE VOLQUETAS
         */
        gbc.gridx = 0;
        gbc.gridy = 7;
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
        gbc.gridy = 7;
        gbc.weightx = 1;

        panelFormulario.add(
                txtCantidadVolquetas,
                gbc
        );

        /*
         * HORAS DE VOLQUETA
         */
        gbc.gridx = 0;
        gbc.gridy = 8;
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
        gbc.gridy = 8;
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
                BorderFactory.createTitledBorder(
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
        gbc.gridy = 9;
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
        gbc.gridy = 10;
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
        gbc.gridy = 10;
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

        cmbCantera.addActionListener(
                e -> {

                    if (!cargandoCombos) {

                        cargarMaterialesPorCantera();
                    }
                }
        );

        cmbMaterial.addActionListener(
                e -> {

                    if (!cargandoCombos) {

                        cargarDestinosPorMaterial();
                    }
                }
        );

        cmbDestinoSector.addActionListener(
                e -> {

                    if (!cargandoCombos) {

                        cargarTarifaSeleccionada();
                    }
                }
        );

        /*
         * CARGA INICIAL
         */
        inicializarValoresNumericos();
        cargarCanteras();

        if (this.idControlMaterial != null) {

            cargarRegistro();
        }
    }

    private void inicializarValoresNumericos() {

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

    private void cargarCanteras() {

        cargandoCombos =
                true;

        try {

            cmbCantera.removeAllItems();
            cmbCantera.addItem("");

            List<String> canteras =
                    CatalogoCanteraMaterialDAO
                            .obtenerCanterasActivas();

            for (String cantera : canteras) {

                cmbCantera.addItem(
                        cantera
                );
            }

            cmbMaterial.removeAllItems();
            cmbMaterial.addItem("");

            cmbDestinoSector.removeAllItems();
            cmbDestinoSector.addItem("");

            limpiarTarifaSeleccionada();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las canteras:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();

        } finally {

            cargandoCombos =
                    false;
        }
    }

    private void cargarMaterialesPorCantera() {

        String cantera =
                obtenerTextoSeleccionado(
                        cmbCantera
                );

        cargandoCombos =
                true;

        try {

            cmbMaterial.removeAllItems();
            cmbMaterial.addItem("");

            cmbDestinoSector.removeAllItems();
            cmbDestinoSector.addItem("");

            limpiarTarifaSeleccionada();

            if (cantera.isBlank()) {

                return;
            }

            List<String> materiales =
                    CatalogoCanteraMaterialDAO
                            .obtenerMaterialesPorCantera(
                                    cantera
                            );

            for (String material : materiales) {

                cmbMaterial.addItem(
                        material
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los materiales:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();

        } finally {

            cargandoCombos =
                    false;
        }
    }

    private void cargarDestinosPorMaterial() {

        String cantera =
                obtenerTextoSeleccionado(
                        cmbCantera
                );

        String material =
                obtenerTextoSeleccionado(
                        cmbMaterial
                );

        cargandoCombos =
                true;

        try {

            cmbDestinoSector.removeAllItems();
            cmbDestinoSector.addItem("");

            limpiarTarifaSeleccionada();

            if (
                    cantera.isBlank()
                    || material.isBlank()
            ) {

                return;
            }

            List<String> destinos =
                    CatalogoCanteraMaterialDAO.obtenerDestinos(
                            cantera,
                            material
                    );

            for (String destino : destinos) {

                cmbDestinoSector.addItem(
                        destino
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los destinos o sectores:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();

        } finally {

            cargandoCombos =
                    false;
        }
    }

    private void cargarTarifaSeleccionada() {

        String cantera =
                obtenerTextoSeleccionado(
                        cmbCantera
                );

        String material =
                obtenerTextoSeleccionado(
                        cmbMaterial
                );

        String destinoSector =
                obtenerTextoSeleccionado(
                        cmbDestinoSector
                );

        if (
                cantera.isBlank()
                || material.isBlank()
                || destinoSector.isBlank()
        ) {

            limpiarTarifaSeleccionada();
            return;
        }

        try {

            CatalogoCanteraMaterialDAO.TarifaDetalle tarifa =
                    CatalogoCanteraMaterialDAO.obtenerTarifaActiva(
                            cantera,
                            material,
                            destinoSector
                    );

            idTarifaSeleccionada =
                    tarifa.idTarifa();

            txtCostoUnitarioMaterial.setText(
                    formatearNumeroEdicion(
                            tarifa.costoUnitarioMaterial()
                    )
            );

            txtCostoUnitarioTransporte.setText(
                    formatearNumeroEdicion(
                            tarifa.costoUnitarioTransporte()
                    )
            );

            calcularYMostrarCostosSilencioso();

        } catch (Exception ex) {

            limpiarTarifaSeleccionada();

            JOptionPane.showMessageDialog(
                    this,
                    "Error al obtener la tarifa:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    private void cargarRegistro() {

        try {

            ControlDiarioMaterialDAO.ControlMaterialDetalle detalle =
                    ControlDiarioMaterialDAO.obtenerPorId(
                            idControlMaterial
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

            seleccionarTarifaParaEdicion(
                    detalle
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

            ex.printStackTrace();
            dispose();
        }
    }

    private void seleccionarTarifaParaEdicion(
            ControlDiarioMaterialDAO.ControlMaterialDetalle detalle
    ) throws Exception {

        String canteraRegistro =
                detalle.cantera() == null
                        ? ""
                        : detalle.cantera().trim();

        String materialRegistro =
                detalle.materialRecibido() == null
                        ? ""
                        : detalle.materialRecibido().trim();

        cargandoCombos =
                true;

        try {

            seleccionarOAgregarItem(
                    cmbCantera,
                    canteraRegistro
            );

            cmbMaterial.removeAllItems();
            cmbMaterial.addItem("");

            for (
                    String material
                    : CatalogoCanteraMaterialDAO
                            .obtenerMaterialesPorCantera(
                                    canteraRegistro
                            )
            ) {

                cmbMaterial.addItem(
                        material
                );
            }

            seleccionarOAgregarItem(
                    cmbMaterial,
                    materialRegistro
            );

            cmbDestinoSector.removeAllItems();
            cmbDestinoSector.addItem("");

            List<CatalogoCanteraMaterialDAO.TarifaResumen> tarifas =
                    CatalogoCanteraMaterialDAO.buscar(
                            canteraRegistro,
                            materialRegistro,
                            "",
                            "ACTIVO"
                    );

            CatalogoCanteraMaterialDAO.TarifaResumen coincidencia =
                    buscarTarifaCoincidente(
                            tarifas,
                            detalle.costoUnitarioMaterial(),
                            detalle.costoUnitarioTransporte()
                    );

            for (
                    CatalogoCanteraMaterialDAO.TarifaResumen tarifa
                    : tarifas
            ) {

                agregarItemSiNoExiste(
                        cmbDestinoSector,
                        tarifa.destinoSector()
                );
            }

            if (coincidencia != null) {

                cmbDestinoSector.setSelectedItem(
                        coincidencia.destinoSector()
                );

                idTarifaSeleccionada =
                        coincidencia.idTarifa();

            } else {

                cmbDestinoSector.setSelectedIndex(
                        0
                );

                idTarifaSeleccionada =
                        null;
            }

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

        } finally {

            cargandoCombos =
                    false;
        }

        if (idTarifaSeleccionada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "El registro anterior no tiene una tarifa activa "
                            + "que coincida exactamente con sus costos.\n\n"
                            + "Seleccione un destino o sector antes de actualizar.",
                    "Tarifa pendiente",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private CatalogoCanteraMaterialDAO.TarifaResumen buscarTarifaCoincidente(
            List<CatalogoCanteraMaterialDAO.TarifaResumen> tarifas,
            double costoMaterial,
            double costoTransporte
    ) {

        final double tolerancia =
                0.0001;

        for (
                CatalogoCanteraMaterialDAO.TarifaResumen tarifa
                : tarifas
        ) {

            boolean coincideMaterial =
                    Math.abs(
                            tarifa.costoUnitarioMaterial()
                                    - costoMaterial
                    ) <= tolerancia;

            boolean coincideTransporte =
                    Math.abs(
                            tarifa.costoUnitarioTransporte()
                                    - costoTransporte
                    ) <= tolerancia;

            if (
                    coincideMaterial
                    && coincideTransporte
            ) {

                return tarifa;
            }
        }

        return null;
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

    private void calcularYMostrarCostosSilencioso() {

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

            mostrarCostos(
                    cantidadViajes
                            * costoUnitarioMaterial,
                    cantidadViajes
                            * costoUnitarioTransporte
            );

        } catch (Exception ex) {

            mostrarCostos(
                    0,
                    0
            );
        }
    }

    private void guardarRegistro() {

        try {

            String cantera =
                    obtenerSeleccionObligatoria(
                            cmbCantera,
                            "la cantera"
                    );

            String materialRecibido =
                    obtenerSeleccionObligatoria(
                            cmbMaterial,
                            "el material recibido"
                    );

            String destinoSector =
                    obtenerSeleccionObligatoria(
                            cmbDestinoSector,
                            "el destino o sector"
                    );

            if (
                    idTarifaSeleccionada == null
                    || idTarifaSeleccionada <= 0
            ) {

                throw new Exception(
                        "Debe seleccionar una tarifa válida del catálogo."
                );
            }

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
                        idTarifaSeleccionada,
                        materialRecibido,
                        cantera,
                        destinoSector,
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
                        idTarifaSeleccionada,
                        materialRecibido,
                        cantera,
                        destinoSector,
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

    private String obtenerTextoSeleccionado(
            JComboBox<String> combo
    ) {

        Object seleccionado =
                combo.getSelectedItem();

        return seleccionado == null
                ? ""
                : seleccionado.toString().trim();
    }

    private String obtenerSeleccionObligatoria(
            JComboBox<String> combo,
            String nombreCampo
    ) throws Exception {

        String valor =
                obtenerTextoSeleccionado(
                        combo
                );

        if (valor.isBlank()) {

            throw new Exception(
                    "Debe seleccionar "
                            + nombreCampo
                            + "."
            );
        }

        return valor;
    }

    private void limpiarTarifaSeleccionada() {

        idTarifaSeleccionada =
                null;

        txtCostoUnitarioMaterial.setText(
                "0"
        );

        txtCostoUnitarioTransporte.setText(
                "0"
        );

        mostrarCostos(
                0,
                0
        );
    }

    private void seleccionarOAgregarItem(
            JComboBox<String> combo,
            String valor
    ) {

        if (
                valor == null
                || valor.isBlank()
        ) {

            combo.setSelectedIndex(
                    0
            );

            return;
        }

        agregarItemSiNoExiste(
                combo,
                valor
        );

        combo.setSelectedItem(
                valor
        );
    }

    private void agregarItemSiNoExiste(
            JComboBox<String> combo,
            String valor
    ) {

        if (
                valor == null
                || valor.isBlank()
        ) {

            return;
        }

        for (
                int i = 0;
                i < combo.getItemCount();
                i++
        ) {

            String item =
                    combo.getItemAt(i);

            if (
                    item != null
                    && item.equalsIgnoreCase(
                            valor
                    )
            ) {

                return;
            }
        }

        combo.addItem(
                valor
        );
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