import javax.swing.*;
import java.awt.*;

public class FormCodigoHistorico extends JDialog {

    private JComboBox<CodigoHistoricoAPI.ItemCatalogo>
            cmbProveedor;

    private JComboBox<CodigoHistoricoAPI.MaquinariaItem>
            cmbMaquinaria;

    private JTextField txtCodigoAnterior;
    private JTextField txtCodigoActual;
    private JTextField txtDescripcion;
    private JTextField txtCostoHora;

    private JTextArea txtObservaciones;

    private boolean guardado = false;
    private Integer idCodigoHistoricoEdicion = null;

    public FormCodigoHistorico(
            Window parent
    ) {

        super(
                parent,
                "Nuevo código alquilado o histórico",
                ModalityType.APPLICATION_MODAL
        );

        setSize(760, 560);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarCatalogos();
        completarDesdeMaquinaria();
    }

    public FormCodigoHistorico(
        Window parent,
        int idCodigoHistorico
) {

    this(parent);

    this.idCodigoHistoricoEdicion =
            idCodigoHistorico;

    setTitle(
            "Editar código alquilado o histórico"
    );

    cargarDatosEdicion();
}

    public boolean isGuardado() {

        return guardado;
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        panelPrincipal.add(
                crearPanelCampos(),
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                crearPanelObservaciones(),
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelCampos() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                10
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Información del código histórico"
                )
        );

        cmbProveedor =
                new JComboBox<>();

        cmbMaquinaria =
                new JComboBox<>();

        txtCodigoAnterior =
                new JTextField();

        txtCodigoActual =
                new JTextField();

        txtDescripcion =
                new JTextField();

        txtCostoHora =
                new JTextField();

        panel.add(
                new JLabel(
                        "Proveedor histórico:"
                )
        );

        panel.add(cmbProveedor);

        panel.add(
                new JLabel(
                        "Código anterior:"
                )
        );

        panel.add(txtCodigoAnterior);

        panel.add(
                new JLabel(
                        "Maquinaria actual:"
                )
        );

        panel.add(cmbMaquinaria);

        panel.add(
                new JLabel(
                        "Código actual de origen:"
                )
        );

        panel.add(txtCodigoActual);

        panel.add(
                new JLabel(
                        "Descripción histórica:"
                )
        );

        panel.add(txtDescripcion);

        panel.add(
                new JLabel(
                        "Costo hora histórico:"
                )
        );

        panel.add(txtCostoHora);

        cmbMaquinaria.addActionListener(
                e -> completarDesdeMaquinaria()
        );

        return panel;
    }

    private JScrollPane crearPanelObservaciones() {

        txtObservaciones =
                new JTextArea(5, 30);

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scroll =
                new JScrollPane(
                        txtObservaciones
                );

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        return scroll;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnCancelar =
                new JButton("Cancelar");

        btnGuardar.addActionListener(
                e -> guardarCodigoHistorico()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    private void cargarCatalogos() {

        try {

            cmbProveedor.removeAllItems();

            cmbProveedor.addItem(
                    new CodigoHistoricoAPI.ItemCatalogo(
                            0,
                            "Sin asignar"
                    )
            );

            for (
                    CodigoHistoricoAPI.ItemCatalogo proveedor
                    : CodigoHistoricoAPI.obtenerProveedores()
            ) {

                cmbProveedor.addItem(
                        proveedor
                );
            }

            cmbMaquinaria.removeAllItems();

            cmbMaquinaria.addItem(
                    new CodigoHistoricoAPI.MaquinariaItem(
                            0,
                            "",
                            "Sin vincular por el momento"
                    )
            );

            for (
                    CodigoHistoricoAPI.MaquinariaItem maquinaria
                    : CodigoHistoricoAPI
                            .obtenerMaquinariasActivas()
            ) {

                cmbMaquinaria.addItem(
                        maquinaria
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

    private void completarDesdeMaquinaria() {

    CodigoHistoricoAPI.MaquinariaItem maquinaria =
            (CodigoHistoricoAPI.MaquinariaItem)
                    cmbMaquinaria.getSelectedItem();

    if (
        maquinaria == null
        || maquinaria.idMaquinaria() == 0
    ) {

        /*
         * Sin maquinaria vinculada:
         * Daniela puede escribir los datos originales.
         */
        txtCodigoActual.setEditable(true);
        txtDescripcion.setEditable(true);

        txtCodigoActual.setBackground(Color.WHITE);
        txtDescripcion.setBackground(Color.WHITE);

        txtCodigoActual.setText("");
        txtDescripcion.setText("");

        return;
    }

    /*
     * Maquinaria vinculada:
     * los datos provienen del catálogo maestro.
     */
    txtCodigoActual.setText(
            maquinaria.codigo()
    );

    txtDescripcion.setText(
            maquinaria.descripcion()
    );

    txtCodigoActual.setEditable(false);
    txtDescripcion.setEditable(false);

    Color colorBloqueado =
            new Color(235, 235, 235);

    txtCodigoActual.setBackground(
            colorBloqueado
    );

    txtDescripcion.setBackground(
            colorBloqueado
    );
}


    private void cargarDatosEdicion() {

    try {

        CodigoHistoricoAPI.CodigoHistoricoDetalle detalle =
                CodigoHistoricoAPI.obtenerPorId(
                        idCodigoHistoricoEdicion
                );

        txtCodigoAnterior.setText(
                detalle.codigoAnterior()
        );

        txtCodigoActual.setText(
                detalle.codigoActualOrigen()
        );

        txtDescripcion.setText(
                detalle.descripcionOriginal()
        );

        txtCostoHora.setText(
                String.valueOf(
                        detalle.costoHoraOriginal()
                )
        );

        txtObservaciones.setText(
                detalle.observaciones()
        );

        seleccionarProveedorPorId(
                detalle.idProveedorOriginal()
        );

        seleccionarMaquinariaPorId(
                detalle.idMaquinaria()
        );

        completarDesdeMaquinaria();

        /*
         * Si no existe maquinaria vinculada,
         * conservamos los datos históricos.
         */
        if (
            detalle.idMaquinaria() == null
        ) {

            txtCodigoActual.setEditable(true);
            txtDescripcion.setEditable(true);

            txtCodigoActual.setBackground(
                    Color.WHITE
            );

            txtDescripcion.setBackground(
                    Color.WHITE
            );

            txtCodigoActual.setText(
                    detalle.codigoActualOrigen()
            );

            txtDescripcion.setText(
                    detalle.descripcionOriginal()
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar el código histórico:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
        dispose();
    }
}

    private void seleccionarProveedorPorId(
        Integer idProveedor
) {

    if (idProveedor == null) {

        cmbProveedor.setSelectedIndex(0);
        return;
    }

    for (
            int i = 0;
            i < cmbProveedor.getItemCount();
            i++
    ) {

        CodigoHistoricoAPI.ItemCatalogo item =
                cmbProveedor.getItemAt(i);

        if (
            item != null
            && item.id() == idProveedor
        ) {

            cmbProveedor.setSelectedIndex(i);
            return;
        }
    }
}

    private void seleccionarMaquinariaPorId(
        Integer idMaquinaria
) {

    if (idMaquinaria == null) {

        cmbMaquinaria.setSelectedIndex(0);
        return;
    }

    for (
            int i = 0;
            i < cmbMaquinaria.getItemCount();
            i++
    ) {

        CodigoHistoricoAPI.MaquinariaItem item =
                cmbMaquinaria.getItemAt(i);

        if (
            item != null
            && item.idMaquinaria()
                    == idMaquinaria
        ) {

            cmbMaquinaria.setSelectedIndex(i);
            return;
        }
    }
}

    private void guardarCodigoHistorico() {

        String codigoAnterior =
                txtCodigoAnterior
                        .getText()
                        .trim();

        if (codigoAnterior.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el código anterior.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCodigoAnterior.requestFocus();
            return;
        }

        String descripcion =
        txtDescripcion
                .getText()
                .trim();

if (descripcion.isEmpty()) {

    JOptionPane.showMessageDialog(
            this,
            "Ingrese o seleccione la descripción "
                    + "de la maquinaria.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
    );

    txtDescripcion.requestFocus();
    return;
}

        CodigoHistoricoAPI.ItemCatalogo proveedor =
                (CodigoHistoricoAPI.ItemCatalogo)
                        cmbProveedor.getSelectedItem();

        CodigoHistoricoAPI.MaquinariaItem maquinaria =
                (CodigoHistoricoAPI.MaquinariaItem)
                        cmbMaquinaria.getSelectedItem();

        Integer idProveedor =
                proveedor == null
                || proveedor.id() == 0
                        ? null
                        : proveedor.id();

        Integer idMaquinaria =
                maquinaria == null
                || maquinaria.idMaquinaria() == 0
                        ? null
                        : maquinaria.idMaquinaria();

        double costoHora;

        try {

            costoHora =
                    convertirDecimal(
                            txtCostoHora.getText()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El costo por hora debe ser numérico.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCostoHora.requestFocus();
            return;
        }

        try {

            if (
    idCodigoHistoricoEdicion == null
) {

    CodigoHistoricoAPI.insertar(
            idMaquinaria,
            codigoAnterior,
            idProveedor,
            txtCodigoActual
                    .getText()
                    .trim(),
            descripcion,
            costoHora,
            txtObservaciones
                    .getText()
                    .trim()
    );

} else {

    CodigoHistoricoAPI.actualizar(
            idCodigoHistoricoEdicion,
            idMaquinaria,
            codigoAnterior,
            idProveedor,
            txtCodigoActual
                    .getText()
                    .trim(),
            descripcion,
            costoHora,
            txtObservaciones
                    .getText()
                    .trim()
    );
}

            guardado = true;

            JOptionPane.showMessageDialog(
        this,
        idCodigoHistoricoEdicion == null
                ? "Código histórico guardado correctamente."
                : "Código histórico actualizado correctamente.",
        "LPP Smart ERP",
        JOptionPane.INFORMATION_MESSAGE
);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el código histórico:\n"
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

        String valor =
                texto.trim()
                        .replace("$", "")
                        .replace(" ", "");

        if (
            valor.contains(",")
            && valor.contains(".")
        ) {

            valor =
                    valor.replace(".", "")
                            .replace(",", ".");

        } else if (valor.contains(",")) {

            valor =
                    valor.replace(",", ".");
        }

        return Double.parseDouble(valor);
    }
}
