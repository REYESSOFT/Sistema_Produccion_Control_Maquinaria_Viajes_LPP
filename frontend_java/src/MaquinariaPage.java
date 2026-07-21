import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class MaquinariaPage extends JPanel {

    private final Runnable accionVolver;

    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbTipoMaquina;
    private JTextField txtProveedor;
    private JTextField txtCodigo;

    private JTable tablaMaquinaria;
    private DefaultTableModel modeloTabla;

    public MaquinariaPage(
            Runnable accionVolver
    ) {

        this.accionVolver = accionVolver;

        setLayout(new BorderLayout(12, 12));

        setBackground(
                new Color(244, 246, 248)
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        crearInterfaz();

        cargarTiposMaquinariaFiltro();

        cargarMaquinaria();
    }

    private void crearInterfaz() {

        add(
                crearPanelSuperior(),
                BorderLayout.NORTH
        );

        add(
                crearPanelTabla(),
                BorderLayout.CENTER
        );

        add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );
    }

    private JPanel crearPanelSuperior() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelPrincipal.setOpaque(false);

        JPanel panelTitulo =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelTitulo.setOpaque(false);

        JButton btnVolver =
                new JButton("← Volver");

        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Catálogo de Maquinaria"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titulo.setForeground(
                new Color(31, 41, 55)
        );

        panelTitulo.add(
                btnVolver,
                BorderLayout.WEST
        );

        panelTitulo.add(
                titulo,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        JPanel panelFiltros =
                new JPanel(
                        new GridLayout(
                                2,
                                5,
                                10,
                                8
                        )
                );

        panelFiltros.setBorder(
                BorderFactory.createTitledBorder(
                        "Filtros de búsqueda"
                )
        );

        cmbEstado =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "OPERATIVA",
                                "MANTENIMIENTO",
                                "INACTIVA",
                                "RETIRADA"
                        }
                );

        cmbTipoMaquina =
                new JComboBox<>();

        txtProveedor =
                new JTextField();

        txtCodigo =
                new JTextField();

        JButton btnBuscar =
                new JButton("Buscar");

        btnBuscar.addActionListener(
                e -> buscarMaquinaria()
        );

        panelFiltros.add(
                new JLabel("Estado:")
        );

        panelFiltros.add(
                new JLabel("Tipo de máquina:")
        );

        panelFiltros.add(
                new JLabel("Proveedor:")
        );

        panelFiltros.add(
                new JLabel("Código / placa:")
        );

        panelFiltros.add(
                new JLabel()
        );

        panelFiltros.add(cmbEstado);
        panelFiltros.add(cmbTipoMaquina);
        panelFiltros.add(txtProveedor);
        panelFiltros.add(txtCodigo);
        panelFiltros.add(btnBuscar);

        panelPrincipal.add(
                panelFiltros,
                BorderLayout.CENTER
        );

        return panelPrincipal;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Código",
                                "Descripción",
                                "Tipo de máquina",
                                "Proveedor",
                                "Propietario",
                                "Tipo de cobro",
                                "Costo",
                                "Precio",
                                "Estado"
                        },
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

        tablaMaquinaria =
                new JTable(modeloTabla);

        tablaMaquinaria.removeColumn(
                tablaMaquinaria
                        .getColumnModel()
                        .getColumn(0)
        );

        tablaMaquinaria.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaMaquinaria.setRowHeight(26);

        tablaMaquinaria.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMaquinaria.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tablaMaquinaria.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(120);

        tablaMaquinaria.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(240);

        tablaMaquinaria.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(170);

        tablaMaquinaria.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(190);

        tablaMaquinaria.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(190);

        tablaMaquinaria.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(130);

        tablaMaquinaria.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(145);

        tablaMaquinaria.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(145);

        tablaMaquinaria.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(120);

        return new JScrollPane(
                tablaMaquinaria
        );
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnNueva =
                new JButton("Nueva maquinaria");

        JButton btnImportar =
                new JButton("Importar listado");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnDetalle =
                new JButton("Detalle");

        btnNueva.addActionListener(e -> {

                FormMaquinaria formulario =
                        new FormMaquinaria(
                                SwingUtilities
                                        .getWindowAncestor(this)
                        );

                formulario.setVisible(true);

                if (formulario.isGuardado()) {
                        cargarMaquinaria();
                }
        });

        btnEditar.addActionListener(
                e -> editarMaquinaria()
        );


        btnImportar.addActionListener(
                e -> importarListadoMaquinaria()
        );

        btnEliminar.addActionListener(
                e -> desactivarMaquinaria()
        );

        btnDetalle.addActionListener(
                e -> mostrarDetalle()
        );

        panel.add(btnNueva);
        panel.add(btnImportar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnDetalle);

        return panel;
    }

    private void buscarMaquinaria() {

        cargarMaquinaria();
    }

    private void cargarTiposMaquinariaFiltro() {

    cmbTipoMaquina.removeAllItems();

    cmbTipoMaquina.addItem("Todos");

    try {

        for (
                MaquinariaDAO.CatalogoItem tipo
                : MaquinariaDAO.obtenerTiposMaquinaria()
        ) {

            cmbTipoMaquina.addItem(
                    tipo.nombre()
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar los tipos de maquinaria:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}


   private void importarListadoMaquinaria() {

    JFileChooser selector =
            new JFileChooser();

    selector.setDialogTitle(
            "Seleccionar listado de maquinaria"
    );

    selector.setFileFilter(
            new FileNameExtensionFilter(
                    "Texto delimitado por tabulaciones (*.txt)",
                    "txt"
            )
    );

    int resultado =
            selector.showOpenDialog(this);

    if (
        resultado
        != JFileChooser.APPROVE_OPTION
    ) {
        return;
    }

    File archivo =
            selector.getSelectedFile();

    try {

        LectorTXTMaquinaria.ResultadoLectura lectura =
                LectorTXTMaquinaria.leer(
                        archivo
                );

        StringBuilder mensaje =
                new StringBuilder();

        mensaje.append(
                "Lectura terminada correctamente.\n\n"
        );

        mensaje.append(
                "Filas procesadas: "
        ).append(
                lectura.filasProcesadas()
        ).append("\n");

        mensaje.append(
                "Filas válidas: "
        ).append(
                lectura.filasValidasCantidad()
        ).append("\n");

        mensaje.append(
                "Filas omitidas: "
        ).append(
                lectura.filasOmitidas()
        ).append("\n");

        mensaje.append(
                "Estados normalizados: "
        ).append(
                lectura.estadosNormalizados()
        ).append("\n");

        mensaje.append(
                "Horómetros normalizados: "
        ).append(
                lectura.horometrosNormalizados()
        ).append("\n");

        mensaje.append(
                "Precios normalizados: "
        ).append(
                lectura.preciosNormalizados()
        ).append("\n");

        mensaje.append(
                "Advertencias: "
        ).append(
                lectura.errores().size()
        );

        JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "Validación del listado",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (!lectura.errores().isEmpty()) {

            mostrarErroresLectura(
                    lectura.errores()
            );
        }

        int confirmacion =
        JOptionPane.showConfirmDialog(
                this,
                "¿Desea registrar ahora los tipos de maquinaria,\n"
                        + "proveedores y propietarios encontrados?\n\n"
                        + "Todavía no se importarán las maquinarias.",
                "Importar catálogos auxiliares",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

if (
    confirmacion
    != JOptionPane.YES_OPTION
) {
    return;
}

MigradorCatalogosMaquinaria.ResultadoCatalogos resultadoCatalogos =
        MigradorCatalogosMaquinaria.importar(
                lectura.filasValidas()
        );

JOptionPane.showMessageDialog(
        this,
        "Catálogos procesados correctamente.\n\n"
                + "Tipos nuevos: "
                + resultadoCatalogos.tiposCreados()
                + "\nTipos ya existentes: "
                + resultadoCatalogos.tiposEncontrados()
                + "\nEntidades nuevas: "
                + resultadoCatalogos.entidadesCreadas()
                + "\nEntidades ya existentes: "
                + resultadoCatalogos.entidadesEncontradas(),
        "Resultado de catálogos",
        JOptionPane.INFORMATION_MESSAGE
);

cargarTiposMaquinariaFiltro();


int confirmarMaquinaria =
        JOptionPane.showConfirmDialog(
                this,
                "Los catálogos auxiliares ya fueron procesados.\n\n"
                        + "¿Desea importar ahora las "
                        + lectura.filasValidasCantidad()
                        + " maquinarias válidas en MySQL?\n\n"
                        + "Las máquinas existentes serán actualizadas "
                        + "y las nuevas serán insertadas.",
                "Confirmar importación de maquinaria",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

if (
    confirmarMaquinaria
    != JOptionPane.YES_OPTION
) {
    return;
}

MigradorMaquinaria.ResultadoMigracion migracion =
        MigradorMaquinaria.importar(
                lectura.filasValidas()
        );

JOptionPane.showMessageDialog(
        this,
        "Importación terminada.\n\n"
                + "Filas procesadas: "
                + migracion.procesadas()
                + "\nMaquinarias insertadas: "
                + migracion.insertadas()
                + "\nMaquinarias actualizadas: "
                + migracion.actualizadas()
                + "\nFilas omitidas: "
                + migracion.omitidas()
                + "\nErrores: "
                + migracion.errores().size(),
        "Resultado de importación",
        JOptionPane.INFORMATION_MESSAGE
);

if (!migracion.errores().isEmpty()) {

    mostrarErroresLectura(
            migracion.errores()
    );
}

cargarMaquinaria();





    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al leer el archivo:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

    private void cargarMaquinaria() {

        String estado =
                cmbEstado.getSelectedItem() == null
                        ? "Todos"
                        : cmbEstado
                                .getSelectedItem()
                                .toString();

        String tipoMaquinaria =
                cmbTipoMaquina.getSelectedItem() == null
                        ? "Todos"
                        : cmbTipoMaquina
                                .getSelectedItem()
                                .toString();

        String proveedor =
                txtProveedor.getText().trim();

        String codigo =
                txtCodigo.getText().trim();

        try {

            List<MaquinariaDAO.MaquinariaResumen> lista =
                    MaquinariaDAO.buscar(
                            estado,
                            tipoMaquinaria,
                            proveedor,
                            codigo
                    );

            modeloTabla.setRowCount(0);

            for (
                    MaquinariaDAO.MaquinariaResumen maquinaria
                            : lista
            ) {

                MaquinariaDAO.MaquinariaDetalle detalle =
                        MaquinariaDAO.obtenerPorId(
                                maquinaria.idMaquinaria()
                        );

                String tipoCobro =
                        detalle.tipoCobro() == null
                                || detalle.tipoCobro().isBlank()
                                ? "POR_HORA"
                                : detalle.tipoCobro();

                modeloTabla.addRow(
                        new Object[]{
                                maquinaria.idMaquinaria(),
                                maquinaria.codigo(),
                                maquinaria.descripcion(),
                                maquinaria.tipoMaquinaria(),
                                maquinaria.proveedor(),
                                maquinaria.propietario(),
                                formatearTipoCobro(
                                        tipoCobro
                                ),
                                formatearCosto(
                                        detalle,
                                        tipoCobro
                                ),
                                formatearPrecio(
                                        detalle,
                                        tipoCobro
                                ),
                                maquinaria.estadoOperativo()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la maquinaria:\n"
        + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private String formatearTipoCobro(
            String tipoCobro
    ) {

        return switch (tipoCobro) {

            case "FIJO_DIARIO" ->
                    "FIJO DIARIO";

            case "FIJO_SERVICIO" ->
                    "FIJO SERVICIO";

            default ->
                    "POR HORA";
        };
    }

    private String formatearCosto(
            MaquinariaDAO.MaquinariaDetalle detalle,
            String tipoCobro
    ) {

        return switch (tipoCobro) {

            case "FIJO_DIARIO" ->
                    String.format(
                            "$%.2f / día",
                            detalle.costoFijoProveedor()
                    );

            case "FIJO_SERVICIO" ->
                    String.format(
                            "$%.2f / servicio",
                            detalle.costoFijoProveedor()
                    );

            default ->
                    String.format(
                            "$%.2f / hora",
                            detalle.costoHoraProveedor()
                    );
        };
    }

    private String formatearPrecio(
            MaquinariaDAO.MaquinariaDetalle detalle,
            String tipoCobro
    ) {

        return switch (tipoCobro) {

            case "FIJO_DIARIO" ->
                    String.format(
                            "$%.2f / día",
                            detalle.precioFijoCliente()
                    );

            case "FIJO_SERVICIO" ->
                    String.format(
                            "$%.2f / servicio",
                            detalle.precioFijoCliente()
                    );

            default ->
                    String.format(
                            "$%.2f / hora",
                            detalle.precioHoraCliente()
                    );
        };
    }


    private Integer obtenerIdSeleccionado() {

    int filaVista =
            tablaMaquinaria.getSelectedRow();

    if (filaVista == -1) {
        return null;
    }

    int filaModelo =
            tablaMaquinaria.convertRowIndexToModel(
                    filaVista
            );

    Object valor =
            modeloTabla.getValueAt(
                    filaModelo,
                    0
            );

    if (valor == null) {
        return null;
    }

    return Integer.valueOf(
            valor.toString()
    );
}
    

    private void validarSeleccion(
            String accion
    ) {

        Integer idMaquinaria =
                obtenerIdSeleccionado();

        if (idMaquinaria == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una maquinaria en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "La opción \"" + accion
                        + "\" se conectará posteriormente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void editarMaquinaria() {

    Integer idMaquinaria =
            obtenerIdSeleccionado();

    if (idMaquinaria == null) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una maquinaria en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    FormMaquinaria formulario =
            new FormMaquinaria(
                    SwingUtilities.getWindowAncestor(this),
                    idMaquinaria
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarMaquinaria();
    }
}

private void mostrarDetalle() {

    Integer idMaquinaria =
            obtenerIdSeleccionado();

    if (idMaquinaria == null) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una maquinaria en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    DetalleMaquinariaDialog detalle =
            new DetalleMaquinariaDialog(
                    SwingUtilities.getWindowAncestor(this),
                    idMaquinaria
            );

    detalle.setVisible(true);
}

private void desactivarMaquinaria() {

    Integer idMaquinaria =
            obtenerIdSeleccionado();

    if (idMaquinaria == null) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una maquinaria en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar la maquinaria seleccionada?\n\n"
                            + "La maquinaria dejará de estar disponible "
                            + "para nuevos procesos,\n"
                            + "pero permanecerá almacenada para conservar "
                            + "el historial.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }

    try {

        MaquinariaDAO.desactivar(
                idMaquinaria
        );

        JOptionPane.showMessageDialog(
                this,
                "Maquinaria eliminada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarMaquinaria();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al eliminar la maquinaria:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

private void mostrarErroresLectura(
        List<String> errores
) {

    JTextArea area =
            new JTextArea();

    area.setEditable(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);

    StringBuilder contenido =
            new StringBuilder();

    int limite =
            Math.min(
                    errores.size(),
                    100
            );

    for (
            int i = 0;
            i < limite;
            i++
    ) {

        contenido.append(
                errores.get(i)
        ).append("\n");
    }

    if (errores.size() > limite) {

        contenido.append(
                "\nSe omitieron "
        ).append(
                errores.size() - limite
        ).append(
                " advertencias adicionales."
        );
    }

    area.setText(
            contenido.toString()
    );

    area.setCaretPosition(0);

    JScrollPane scroll =
            new JScrollPane(area);

    scroll.setPreferredSize(
            new Dimension(
                    750,
                    400
            )
    );

    JOptionPane.showMessageDialog(
            this,
            scroll,
            "Advertencias del archivo",
            JOptionPane.WARNING_MESSAGE
    );
}

}