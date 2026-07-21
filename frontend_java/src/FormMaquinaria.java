import javax.swing.*;
import java.awt.*;

public class FormMaquinaria extends JDialog {

    private JTextField txtCodigoInterno;
    private JTextField txtCodigoActual;
    private JTextField txtPlaca;
    private JTextField txtDescripcion;
    private JTextField txtModelo;
    private JTextField txtSerieMaquina;
    private JTextField txtSerieActual;
    private JTextField txtHorometro;

    private JTextField txtCostoProveedor;
    private JTextField txtCostoFijoProveedor;
    private JTextField txtPrecioCliente;
    private JTextField txtPrecioFijoCliente;

    private JComboBox<ItemCombo> cmbTipo;
    private JComboBox<ItemCombo> cmbProveedor;
    private JComboBox<ItemCombo> cmbPropietario;

    private JComboBox<String> cmbTipoPropiedad;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbTipoCobro;

    private JCheckBox chkHorometroConfirmado;
    private JTextArea txtObservaciones;

    private boolean guardado = false;
    private Integer idMaquinariaEdicion = null;

    public FormMaquinaria(Window parent) {

        super(
                parent,
                "Nueva maquinaria",
                ModalityType.APPLICATION_MODAL
        );

        setSize(900, 760);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarCatalogos();
        actualizarCamposTipoCobro();
    }

    public FormMaquinaria(
            Window parent,
            int idMaquinaria
    ) {

        this(parent);

        this.idMaquinariaEdicion =
                idMaquinaria;

        setTitle("Editar maquinaria");

        cargarDatosEdicion();
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JPanel panelCampos =
                new JPanel(
                        new GridLayout(
                                11,
                                4,
                                10,
                                8
                        )
                );

        txtCodigoInterno = new JTextField();
        txtCodigoActual = new JTextField();
        txtPlaca = new JTextField();
        txtDescripcion = new JTextField();
        txtModelo = new JTextField();
        txtSerieMaquina = new JTextField();
        txtSerieActual = new JTextField();
        txtHorometro = new JTextField();
        txtCostoProveedor = new JTextField();
        txtCostoFijoProveedor = new JTextField();
        txtPrecioCliente = new JTextField();
        txtPrecioFijoCliente = new JTextField();

        cmbTipo = new JComboBox<>();
        cmbProveedor = new JComboBox<>();
        cmbPropietario = new JComboBox<>();

        cmbTipoPropiedad =
                new JComboBox<>(
                        new String[]{
                                "PROPIA",
                                "ALQUILADA",
                                "TERCERO"
                        }
                );

        cmbEstado =
                new JComboBox<>(
                        new String[]{
                                "OPERATIVA",
                                "MANTENIMIENTO",
                                "INACTIVA",
                                "RETIRADA"
                        }
                );

        cmbTipoCobro =
                new JComboBox<>(
                        new String[]{
                                "POR_HORA",
                                "FIJO_DIARIO",
                                "FIJO_SERVICIO"
                        }
                );

        chkHorometroConfirmado =
                new JCheckBox("Sí");

        agregarCampo(panelCampos, "Código interno:", txtCodigoInterno);
        agregarCampo(panelCampos, "Código actual:", txtCodigoActual);
        agregarCampo(panelCampos, "Placa:", txtPlaca);
        agregarCampo(panelCampos, "Descripción:", txtDescripcion);
        agregarCampo(panelCampos, "Tipo de maquinaria:", cmbTipo);
        agregarCampo(panelCampos, "Modelo:", txtModelo);
        agregarCampo(panelCampos, "Serie máquina:", txtSerieMaquina);
        agregarCampo(panelCampos, "Serie actual:", txtSerieActual);
        agregarCampo(panelCampos, "Horómetro actual:", txtHorometro);
        agregarCampo(panelCampos, "Horómetro confirmado:", chkHorometroConfirmado);
        agregarCampo(panelCampos, "Proveedor:", cmbProveedor);
        agregarCampo(panelCampos, "Propietario:", cmbPropietario);
        agregarCampo(panelCampos, "Tipo de propiedad:", cmbTipoPropiedad);
        agregarCampo(panelCampos, "Estado operativo:", cmbEstado);
        agregarCampo(panelCampos, "Tipo de cobro:", cmbTipoCobro);
        agregarCampo(panelCampos, "Costo hora proveedor:", txtCostoProveedor);
        agregarCampo(panelCampos, "Costo fijo proveedor:", txtCostoFijoProveedor);
        agregarCampo(panelCampos, "Precio hora cliente:", txtPrecioCliente);
        agregarCampo(panelCampos, "Precio fijo cliente:", txtPrecioFijoCliente);

        panelCampos.add(new JLabel());
        panelCampos.add(new JLabel());

        cmbTipoCobro.addActionListener(
                e -> actualizarCamposTipoCobro()
        );

        panelPrincipal.add(
                panelCampos,
                BorderLayout.NORTH
        );

        txtObservaciones =
                new JTextArea(
                        5,
                        30
                );

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObservaciones =
                new JScrollPane(txtObservaciones);

        scrollObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        panelPrincipal.add(
                scrollObservaciones,
                BorderLayout.CENTER
        );

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(
                e -> guardarMaquinaria()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    private void agregarCampo(
            JPanel panel,
            String etiqueta,
            Component componente
    ) {

        panel.add(new JLabel(etiqueta));
        panel.add(componente);
    }

    private void cargarCatalogos() {

        try {

            cmbTipo.addItem(
                    new ItemCombo(
                            0,
                            "Seleccione"
                    )
            );

            for (
                    MaquinariaDAO.CatalogoItem item
                    : MaquinariaDAO.obtenerTiposMaquinaria()
            ) {

                cmbTipo.addItem(
                        new ItemCombo(
                                item.id(),
                                item.nombre()
                        )
                );
            }

            cmbProveedor.addItem(
                    new ItemCombo(
                            0,
                            "Sin asignar"
                    )
            );

            cmbPropietario.addItem(
                    new ItemCombo(
                            0,
                            "Sin asignar"
                    )
            );

            for (
                    MaquinariaDAO.CatalogoItem item
                    : MaquinariaDAO.obtenerEntidades()
            ) {

                cmbProveedor.addItem(
                        new ItemCombo(
                                item.id(),
                                item.nombre()
                        )
                );

                cmbPropietario.addItem(
                        new ItemCombo(
                                item.id(),
                                item.nombre()
                        )
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los catálogos:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void cargarDatosEdicion() {

        try {

            MaquinariaDAO.MaquinariaDetalle maquinaria =
                    MaquinariaDAO.obtenerPorId(
                            idMaquinariaEdicion
                    );

            txtCodigoInterno.setText(maquinaria.codigoInterno());
            txtCodigoActual.setText(maquinaria.codigoActual());
            txtPlaca.setText(maquinaria.codigoPlaca());
            txtDescripcion.setText(maquinaria.descripcion());
            txtModelo.setText(maquinaria.modelo());
            txtSerieMaquina.setText(maquinaria.serieMaquina());
            txtSerieActual.setText(maquinaria.serieActual());

            txtHorometro.setText(
                    maquinaria.horometroActual() == null
                            ? ""
                            : maquinaria.horometroActual().toString()
            );

            chkHorometroConfirmado.setSelected(
                    maquinaria.horometroConfirmado()
            );

            txtCostoProveedor.setText(
                    String.valueOf(
                            maquinaria.costoHoraProveedor()
                    )
            );

            txtCostoFijoProveedor.setText(
                    String.valueOf(
                            maquinaria.costoFijoProveedor()
                    )
            );

            txtPrecioCliente.setText(
                    String.valueOf(
                            maquinaria.precioHoraCliente()
                    )
            );

            txtPrecioFijoCliente.setText(
                    String.valueOf(
                            maquinaria.precioFijoCliente()
                    )
            );

            txtObservaciones.setText(
                    maquinaria.observaciones()
            );

            seleccionarItemPorId(
                    cmbTipo,
                    maquinaria.idTipoMaquinaria()
            );

            seleccionarItemPorId(
                    cmbProveedor,
                    maquinaria.idProveedor()
            );

            seleccionarItemPorId(
                    cmbPropietario,
                    maquinaria.idPropietario()
            );

            cmbTipoPropiedad.setSelectedItem(
                    maquinaria.tipoPropiedad()
            );

            cmbEstado.setSelectedItem(
                    maquinaria.estadoOperativo()
            );

            cmbTipoCobro.setSelectedItem(
                    maquinaria.tipoCobro()
            );

            actualizarCamposTipoCobro();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la maquinaria:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
            dispose();
        }
    }

    private void seleccionarItemPorId(
            JComboBox<ItemCombo> combo,
            Integer id
    ) {

        if (id == null) {

            combo.setSelectedIndex(0);
            return;
        }

        for (
                int i = 0;
                i < combo.getItemCount();
                i++
        ) {

            ItemCombo item =
                    combo.getItemAt(i);

            if (
                    item != null
                            && item.id == id
            ) {

                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void actualizarCamposTipoCobro() {

        Object seleccionado =
                cmbTipoCobro.getSelectedItem();

        String tipoCobro =
                seleccionado == null
                        ? "POR_HORA"
                        : seleccionado.toString();

        boolean usaHora =
                "POR_HORA".equals(tipoCobro);

        boolean usaFijo =
                "FIJO_DIARIO".equals(tipoCobro)
                        || "FIJO_SERVICIO".equals(tipoCobro);

        txtCostoProveedor.setEnabled(usaHora);
        txtPrecioCliente.setEnabled(usaHora);
        txtCostoFijoProveedor.setEnabled(usaFijo);
        txtPrecioFijoCliente.setEnabled(usaFijo);
    }

    private void guardarMaquinaria() {

        String codigoInterno =
                txtCodigoInterno.getText().trim();

        String codigoActual =
                txtCodigoActual.getText().trim();

        String placa =
                txtPlaca.getText().trim();

        String descripcion =
                txtDescripcion.getText().trim();

        if (
                codigoInterno.isEmpty()
                        && codigoActual.isEmpty()
                        && placa.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese al menos un código o placa.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (descripcion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la descripción de la maquinaria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtDescripcion.requestFocus();
            return;
        }

        ItemCombo tipo =
                (ItemCombo) cmbTipo.getSelectedItem();

        if (
                tipo == null
                        || tipo.id == 0
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el tipo de maquinaria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Double horometro =
                    convertirDecimalOpcional(
                            txtHorometro.getText()
                    );

            String tipoCobro =
                    cmbTipoCobro
                            .getSelectedItem()
                            .toString();

            double costoProveedor =
                    convertirDecimal(
                            txtCostoProveedor.getText()
                    );

            double costoFijoProveedor =
                    convertirDecimal(
                            txtCostoFijoProveedor.getText()
                    );

            double precioCliente =
                    convertirDecimal(
                            txtPrecioCliente.getText()
                    );

            double precioFijoCliente =
                    convertirDecimal(
                            txtPrecioFijoCliente.getText()
                    );

            if ("POR_HORA".equals(tipoCobro)) {

                costoFijoProveedor = 0.00;
                precioFijoCliente = 0.00;

            } else {

                costoProveedor = 0.00;
                precioCliente = 0.00;
            }

            ItemCombo proveedor =
                    (ItemCombo) cmbProveedor.getSelectedItem();

            ItemCombo propietario =
                    (ItemCombo) cmbPropietario.getSelectedItem();

            Integer idProveedor =
                    proveedor == null
                            || proveedor.id == 0
                            ? null
                            : proveedor.id;

            Integer idPropietario =
                    propietario == null
                            || propietario.id == 0
                            ? null
                            : propietario.id;

            if (idMaquinariaEdicion == null) {

                MaquinariaDAO.insertar(
                        codigoInterno,
                        codigoActual,
                        placa,
                        descripcion,
                        tipo.id,
                        txtModelo.getText().trim(),
                        txtSerieMaquina.getText().trim(),
                        txtSerieActual.getText().trim(),
                        horometro,
                        chkHorometroConfirmado.isSelected(),
                        idProveedor,
                        idPropietario,
                        cmbTipoPropiedad
                                .getSelectedItem()
                                .toString(),
                        cmbEstado
                                .getSelectedItem()
                                .toString(),
                        tipoCobro,
                        costoProveedor,
                        costoFijoProveedor,
                        precioCliente,
                        precioFijoCliente,
                        txtObservaciones.getText().trim()
                );

            } else {

                MaquinariaDAO.actualizar(
                        idMaquinariaEdicion,
                        codigoInterno,
                        codigoActual,
                        placa,
                        descripcion,
                        tipo.id,
                        txtModelo.getText().trim(),
                        txtSerieMaquina.getText().trim(),
                        txtSerieActual.getText().trim(),
                        horometro,
                        chkHorometroConfirmado.isSelected(),
                        idProveedor,
                        idPropietario,
                        cmbTipoPropiedad
                                .getSelectedItem()
                                .toString(),
                        cmbEstado
                                .getSelectedItem()
                                .toString(),
                        tipoCobro,
                        costoProveedor,
                        costoFijoProveedor,
                        precioCliente,
                        precioFijoCliente,
                        txtObservaciones.getText().trim()
                );
            }

            guardado = true;

            JOptionPane.showMessageDialog(
                    this,
                    idMaquinariaEdicion == null
                            ? "Maquinaria guardada correctamente."
                            : "Maquinaria actualizada correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Horómetro, costos y precios deben "
                            + "contener solamente números.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar la maquinaria:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private double convertirDecimal(
            String texto
    ) {

        if (
                texto == null
                        || texto.trim().isEmpty()
        ) {

            return 0.00;
        }

        return Double.parseDouble(
                texto.trim().replace(",", ".")
        );
    }

    private Double convertirDecimalOpcional(
            String texto
    ) {

        if (
                texto == null
                        || texto.trim().isEmpty()
        ) {

            return null;
        }

        return Double.parseDouble(
                texto.trim().replace(",", ".")
        );
    }

    private static class ItemCombo {

        private final int id;
        private final String nombre;

        private ItemCombo(
                int id,
                String nombre
        ) {

            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}