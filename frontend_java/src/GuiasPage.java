import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class GuiasPage extends JPanel {

    private JComboBox<String> cboEmpresa;
    private JComboBox<String> cboTipoGuia;
    private JTextField txtNumeroGuia;
    private JTable tablaGuias;
    private Usuario usuarioActual;

    public GuiasPage() {

        usuarioActual = SesionUsuario.getUsuarioActual();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(new Color(244, 246, 248));

        crearInterfaz();
    }

    private void crearInterfaz() {

        JLabel titulo = new JLabel("Guías de Trabajo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);


        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(
                new BoxLayout(panelFiltros, BoxLayout.Y_AXIS)
        );
        panelFiltros.setOpaque(false);

        JPanel panelBusqueda = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        panelBusqueda.setOpaque(false);

        JPanel panelAcciones = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        panelAcciones.setOpaque(false);

        cboEmpresa = new JComboBox<>(new String[]{
                "Todas",
                "EQUIPOS PRO",
                "DEVIALTRANSPORT"
        });
        if (
        usuarioActual != null
        && usuarioActual.esDigitadorGuias()
        && usuarioActual.getNombreEmpresa() != null
) {

    cboEmpresa.setSelectedItem(
            usuarioActual.getNombreEmpresa()
    );
    cboEmpresa.setEnabled(false);
}

        cboTipoGuia = new JComboBox<>(new String[]{
                "Todas",
                "Guía Producción Volquetas",
                "Guía Trabajo Diario Maquinaria",
                "Control Trabajo Volquetas",
                "Guía Despacho de Material"
        });
        if (
        usuarioActual != null
        && usuarioActual.esDigitadorGuias()
        && usuarioActual.getIdEmpresa() != null
) {

    cboTipoGuia.removeAllItems();
    cboTipoGuia.addItem("Todas");

    if (usuarioActual.getIdEmpresa() == 2) {

        // César - DEVIALTRANSPORT
        cboTipoGuia.addItem("Control Trabajo Volquetas");
        cboTipoGuia.addItem("Guía Despacho de Material");

    } else if (usuarioActual.getIdEmpresa() == 1) {

        // Jeny - EQUIPOS PRO
        cboTipoGuia.addItem("Guía Producción Volquetas");
        cboTipoGuia.addItem("Guía Trabajo Diario Maquinaria");
    }
}

        txtNumeroGuia = new JTextField(12);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnNuevaGuia = new JButton("Nueva Guía");
        JButton btnAprobar = new JButton("Aprobar guía");
        JButton btnEditar = new JButton("Editar guía");
        JButton btnEliminar = new JButton("Eliminar guía");
        JButton btnDetalle = new JButton("Detalle");

        panelBusqueda.add(new JLabel("Empresa:"));
        panelBusqueda.add(cboEmpresa);

        panelBusqueda.add(new JLabel("Tipo de guía:"));
        panelBusqueda.add(cboTipoGuia);

        panelBusqueda.add(new JLabel("N° Guía:"));
        panelBusqueda.add(txtNumeroGuia);

        panelBusqueda.add(btnBuscar);

        panelAcciones.add(btnNuevaGuia);
        panelAcciones.add(btnAprobar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnDetalle);

        panelFiltros.add(panelBusqueda);
        panelFiltros.add(panelAcciones);

        panelCentral.add(panelFiltros, BorderLayout.NORTH);


        String[] columnas = {
                "Empresa",
                "Tipo Guía",
                "N° Guía",
                "Fecha",
                "Chofer / Operador",
                "Placa",
                "M3",
                "Estado"
        };

        
        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {

                        @Override
                        public boolean isCellEditable(
                                int fila,
                                int columna
                        ) {
                                return false;
                        }
                };

        tablaGuias = new JTable(modelo);
        tablaGuias.setRowHeight(26);

        tablaGuias.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int columna = 0; columna < tablaGuias.getColumnCount(); columna++) {

                tablaGuias.getColumnModel()
                        .getColumn(columna)
                        .setPreferredWidth(140);
                }

                tablaGuias.getColumnModel()
                        .getColumn(0)
                        .setPreferredWidth(160);

                tablaGuias.getColumnModel()
                        .getColumn(1)
                        .setPreferredWidth(230);

                tablaGuias.getColumnModel()
                        .getColumn(4)
                        .setPreferredWidth(200);

                tablaGuias.getColumnModel()
                        .getColumn(7)
                        .setPreferredWidth(120);

        int columnaEstado = tablaGuias.getColumnModel()
                .getColumnIndex("Estado");

        tablaGuias.getColumnModel()
                .getColumn(columnaEstado)
                .setCellRenderer(new EstadoGuiaRenderer());

        tablaGuias.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        JScrollPane scroll = new JScrollPane(
                tablaGuias,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        cargarGuiasDesdeMySQL();

        panelCentral.add(scroll, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        btnNuevaGuia.addActionListener(e -> abrirSelectorGuia());

        btnAprobar.addActionListener(e -> aprobarGuiaSeleccionada());

        btnEditar.addActionListener(e -> editarGuiaSeleccionada());

        btnBuscar.addActionListener(e -> buscarGuias());

        txtNumeroGuia.addActionListener(e -> buscarGuias());

        btnEliminar.addActionListener(e -> eliminarGuiaSeleccionada());

        btnDetalle.addActionListener(e -> mostrarDetalleGuiaSeleccionada()
);
    }

    private void abrirSelectorGuia() {

        String empresa;

if (
        usuarioActual != null
        && usuarioActual.esDigitadorGuias()
        && usuarioActual.getNombreEmpresa() != null
) {

    empresa = usuarioActual.getNombreEmpresa();

} else {

    String[] empresas = {
            "EQUIPOS PRO",
            "DEVIALTRANSPORT"
    };

    empresa = (String) JOptionPane.showInputDialog(
            this,
            "Seleccione la empresa:",
            "Nueva Guía",
            JOptionPane.PLAIN_MESSAGE,
            null,
            empresas,
            empresas[0]
    );

    if (empresa == null) {
        return;
    }
}

        String[] tipos;

        if (empresa.equals("EQUIPOS PRO")) {

            tipos = new String[]{
                    "Guía Producción Volquetas",
                    "Guía Trabajo Diario Maquinaria"
            };

        } else {

            tipos = new String[]{
                    "Control Trabajo Volquetas",
                    "Guía Despacho de Material"
            };
        }

        String tipoGuia = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el tipo de guía:",
                "Nueva Guía",
                JOptionPane.PLAIN_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipoGuia == null) {
            return;
        }


        if (
                empresa.equals("EQUIPOS PRO")
                && tipoGuia.equals("Guía Producción Volquetas")
        ) {

                FormGuiaProduccionVolquetas formulario =
                        new FormGuiaProduccionVolquetas(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();

                } else if (
                        empresa.equals("EQUIPOS PRO")
                        && tipoGuia.equals("Guía Trabajo Diario Maquinaria")
                ) {

                FormGuiaTrabajoMaquinaria formulario =
                        new FormGuiaTrabajoMaquinaria(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();

                } else if (
                        empresa.equals("DEVIALTRANSPORT")
                        && tipoGuia.equals("Control Trabajo Volquetas")
                ) {

                FormControlTrabajoVolquetas formulario =
                        new FormControlTrabajoVolquetas(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();


                } else if (
                        empresa.equals("DEVIALTRANSPORT")
                        && tipoGuia.equals(
                                "Guía Despacho de Material"
                        )
                ) {

                FormGuiaDespachoMaterial formulario =
                        new FormGuiaDespachoMaterial(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);

                cargarGuiasDesdeMySQL();

        } else {

                JOptionPane.showMessageDialog(
                        this,
                        "El formulario de \"" + tipoGuia
                                + "\" todavía está pendiente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }


    private void cargarGuiasDesdeMySQL() {

    DefaultTableModel modelo =
            (DefaultTableModel) tablaGuias.getModel();

    modelo.setRowCount(0);

    try {

        List<GuiaAPI.GuiaResumen> guias =
                GuiaAPI.obtenerResumen();

        boolean limitarPorEmpresa =
                usuarioActual != null
                && usuarioActual.esDigitadorGuias()
                && usuarioActual.getNombreEmpresa() != null;

        for (GuiaAPI.GuiaResumen guia : guias) {

            if (
                    limitarPorEmpresa
                    && !usuarioActual.getNombreEmpresa()
                            .equalsIgnoreCase(
                                    guia.empresa()
                            )
            ) {
                continue;
            }

            modelo.addRow(
                    new Object[]{
                            guia.empresa(),
                            guia.tipoGuia(),
                            guia.numeroGuia(),
                            guia.fecha() == null
                                    ? ""
                                    : guia.fecha()
                                            .format(
                                                    java.time.format.DateTimeFormatter
                                                            .ofPattern("dd/MM/yyyy")
                                            ),
                            guia.choferOperador(),
                            guia.placa(),
                            guia.m3(),
                            guia.estado()
                    }
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar las guías:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

   private void buscarGuias() {

    DefaultTableModel modelo =
            (DefaultTableModel) tablaGuias.getModel();

    modelo.setRowCount(0);

    String empresaSeleccionada =
            cboEmpresa.getSelectedItem().toString();

    String tipoGuiaSeleccionado =
            cboTipoGuia.getSelectedItem().toString();

    String numeroGuia =
            txtNumeroGuia.getText()
                    .trim()
                    .toLowerCase();

    try {

        java.util.List<GuiaAPI.GuiaResumen> guias =
                GuiaAPI.obtenerResumen();

        for (GuiaAPI.GuiaResumen guia : guias) {

            boolean coincideEmpresa =
                    empresaSeleccionada.equals("Todas")
                    || empresaSeleccionada.equalsIgnoreCase(
                            guia.empresa()
                    );

            boolean coincideTipo =
                    tipoGuiaSeleccionado.equals("Todas")
                    || tipoGuiaSeleccionado.equalsIgnoreCase(
                            guia.tipoGuia()
                    );

            boolean coincideNumero =
                    numeroGuia.isEmpty()
                    || (
                            guia.numeroGuia() != null
                            && guia.numeroGuia()
                                    .toLowerCase()
                                    .contains(numeroGuia)
                    );

            if (
                    coincideEmpresa
                    && coincideTipo
                    && coincideNumero
            ) {

                modelo.addRow(
                        new Object[]{
                                guia.empresa(),
                                guia.tipoGuia(),
                                guia.numeroGuia(),
                                guia.fecha() == null
                                        ? ""
                                        : guia.fecha()
                                                .format(
                                                        java.time.format.DateTimeFormatter
                                                                .ofPattern("dd/MM/yyyy")
                                                ),
                                guia.choferOperador(),
                                guia.placa(),
                                guia.m3(),
                                guia.estado()
                        }
                );
            }
        }

        if (modelo.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron guías con los filtros seleccionados.",
                    "Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al buscar las guías:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}



    private void aprobarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una guía en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String empresa = tablaGuias
                .getValueAt(filaSeleccionada, 0)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String estadoActual = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();

        if (estadoActual.equalsIgnoreCase("APROBADO")) {
            JOptionPane.showMessageDialog(
                    this,
                    "La guía ya está aprobada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea aprobar la guía N° " + numeroGuia + "?",
                "Confirmar aprobación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            GuiaAPI.aprobarGuia(
                    empresa,
                    tipoGuia,
                    numeroGuia
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Guía aprobada correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarGuiasDesdeMySQL();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al aprobar la guía:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void editarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una guía en la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String estado = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();

        if (estado.equalsIgnoreCase("APROBADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Una guía aprobada no puede editarse.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
        }


        if (tipoGuia.equals("Guía Producción Volquetas")) {

    FormGuiaProduccionVolquetas formulario =
            new FormGuiaProduccionVolquetas(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();

} else if (
    tipoGuia.equals("Control Trabajo Volquetas")
) {



} else if (
        tipoGuia.equals(
                "Guía Trabajo Diario Maquinaria"
        )
) {

    FormGuiaTrabajoMaquinaria formulario =
            new FormGuiaTrabajoMaquinaria(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();


    } else if (
        tipoGuia.equals(
                "Guía Despacho de Material"
        )
) {

    FormGuiaDespachoMaterial formulario =
            new FormGuiaDespachoMaterial(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();

} else {

    JOptionPane.showMessageDialog(
            this,
            "La edición de este tipo de guía todavía no está disponible.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
    );
}

    }

    private void eliminarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una guía en la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        String empresa = tablaGuias
                .getValueAt(filaSeleccionada, 0)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String estado = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();

        if (estado.equalsIgnoreCase("APROBADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "No es posible eliminar una guía aprobada.",
                        "Operación no permitida",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la guía N° "
                        + numeroGuia
                        + "?\n\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
                return;
        }

       try {

    GuiaAPI.eliminarGuia(
            empresa,
            tipoGuia,
            numeroGuia
    );

    JOptionPane.showMessageDialog(
            this,
            "Guía eliminada correctamente.",
            "LPP Smart ERP",
            JOptionPane.INFORMATION_MESSAGE
    );

    cargarGuiasDesdeMySQL();

} catch (Exception e) {

    JOptionPane.showMessageDialog(
            this,
            "Error al eliminar la guía:\n"
                    + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
    );

    e.printStackTrace();
}
    }

    private void mostrarDetalleGuiaSeleccionada() {

    int filaSeleccionada =
            tablaGuias.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una guía en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    String empresa =
            tablaGuias
                    .getValueAt(
                            filaSeleccionada,
                            0
                    )
                    .toString();

    String tipoGuia =
            tablaGuias
                    .getValueAt(
                            filaSeleccionada,
                            1
                    )
                    .toString();

    String numeroGuia =
            tablaGuias
                    .getValueAt(
                            filaSeleccionada,
                            2
                    )
                    .toString();

    if (
            tipoGuia.equals(
                    "Control Trabajo Volquetas"
            )
    ) {

        DetalleControlTrabajoDialog detalle =
                new DetalleControlTrabajoDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        ),
                        empresa,
                        numeroGuia
                );

        detalle.setVisible(true);

        return;
    }

    if (
            tipoGuia.equals(
                    "Guía Trabajo Diario Maquinaria"
            )
    ) {

        DetalleGuiaTrabajoMaquinariaDialog detalle =
                new DetalleGuiaTrabajoMaquinariaDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        ),
                        empresa,
                        numeroGuia
                );

        detalle.setVisible(true);

        return;
    }

    if (
            tipoGuia.equals(
                    "Guía Despacho de Material"
            )
    ) {

        DetalleGuiaDespachoMaterialDialog detalle =
                new DetalleGuiaDespachoMaterialDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        ),
                        empresa,
                        numeroGuia
                );

        detalle.setVisible(true);

        return;
    }

    if (
            !tipoGuia.equals(
                    "Guía Producción Volquetas"
            )
    ) {

        JOptionPane.showMessageDialog(
                this,
                "La visualización del detalle todavía no está disponible "
                        + "para este tipo de guía.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );

        return;
    }

    try {

        GuiaAPI.GuiaProduccionDetalle guia =
                GuiaAPI.obtenerDetalleProduccion(
                        empresa,
                        numeroGuia,
                        tipoGuia
                );

        JDialog ventanaDetalle =
                new JDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        ),
                        "Detalle Guía N° "
                                + numeroGuia,
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        ventanaDetalle.setSize(
                950,
                600
        );

        ventanaDetalle.setLocationRelativeTo(
                this
        );

        ventanaDetalle.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

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

        JPanel panelCabecera =
                new JPanel(
                        new GridLayout(
                                4,
                                4,
                                10,
                                8
                        )
                );

        panelCabecera.add(
                new JLabel(
                        "Empresa:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.empresa()
                )
        );

        panelCabecera.add(
                new JLabel(
                        "N° Guía:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.numeroGuia()
                )
        );

        panelCabecera.add(
                new JLabel(
                        "Fecha:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.fecha() == null
                                ? ""
                                : guia.fecha()
                                        .format(
                                                java.time.format
                                                        .DateTimeFormatter
                                                        .ofPattern(
                                                                "dd/MM/yyyy"
                                                        )
                                        )
                )
        );

        panelCabecera.add(
                new JLabel(
                        "Estado:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.estado()
                )
        );

        panelCabecera.add(
                new JLabel(
                        "Chofer / Operador:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.choferOperador()
                )
        );

        panelCabecera.add(
                new JLabel(
                        "Placa:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        guia.placa()
                )
        );

        panelCabecera.add(
                new JLabel(
                        "M3:"
                )
        );

        panelCabecera.add(
                new JLabel(
                        String.valueOf(
                                guia.m3()
                        )
                )
        );

        panelCabecera.add(
                new JLabel()
        );

        panelCabecera.add(
                new JLabel()
        );

        String[] columnasDetalle = {
                "N°",
                "Proyecto",
                "Sector",
                "Cantera",
                "Material",
                "Hora Origen",
                "Hora Destino"
        };

        DefaultTableModel modeloDetalle =
                new DefaultTableModel(
                        columnasDetalle,
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

        JTable tablaDetalle =
                new JTable(
                        modeloDetalle
                );

        tablaDetalle.setRowHeight(
                26
        );

        tablaDetalle.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaDetalle
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        tablaDetalle
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);

        tablaDetalle
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        tablaDetalle
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(170);

        tablaDetalle
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(170);

        tablaDetalle
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        tablaDetalle
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(110);

        if (guia.detalle() != null) {

            for (
                    GuiaAPI.GuiaProduccionDetalleFila fila
                            : guia.detalle()
            ) {

                modeloDetalle.addRow(
                        new Object[]{
                                fila.numeroFila(),
                                fila.proyecto(),
                                fila.sector(),
                                fila.cantera(),
                                fila.material(),
                                fila.horaOrigen(),
                                fila.horaDestino()
                        }
                );
            }
        }

        JPanel panelInferior =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JTextArea txtInformacion =
                new JTextArea();

        txtInformacion.setEditable(
                false
        );

        txtInformacion.setLineWrap(
                true
        );

        txtInformacion.setWrapStyleWord(
                true
        );

        txtInformacion.setText(
                "Recibí conforme: "
                        + guia.recibiConforme()
                        + "\n\nObservaciones: "
                        + guia.observaciones()
        );

        JButton btnCerrar =
                new JButton(
                        "Cerrar"
                );

        btnCerrar.addActionListener(
                e -> ventanaDetalle.dispose()
        );

        JPanel panelBoton =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBoton.add(
                btnCerrar
        );

        panelInferior.add(
                new JScrollPane(
                        txtInformacion
                ),
                BorderLayout.CENTER
        );

        panelInferior.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        panelPrincipal.add(
                panelCabecera,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                new JScrollPane(
                        tablaDetalle
                ),
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        ventanaDetalle.setContentPane(
                panelPrincipal
        );

        ventanaDetalle.setVisible(
                true
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al mostrar el detalle:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

}