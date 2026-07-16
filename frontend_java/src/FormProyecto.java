import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.text.MaskFormatter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FormProyecto extends JDialog {

    private JComboBox<ProyectoDAO.EmpresaItem> cmbEmpresa;
    private JComboBox<String> cmbEstado;

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JComboBox<ProyectoDAO.SectorItem>
            cmbSector;

    private JComboBox<ProyectoDAO.PiscinaItem>
            cmbPiscina;

    private JComboBox<ProyectoDAO.TipoActividadItem>
            cmbTipoActividad;
    private JTextField txtOrdenCompra;
    
    private JTextField txtFechaInicio;
    private JTextField txtFechaFinEstimada;
    private JTextField txtDiasEstimados;
    private JTextField txtArea;
    private JTextField txtEspesor;
    private JTextField txtFactorCompactacion;
    private JTextField txtCantidadContratada;
    private JTextField txtPrecioUnitario;

    private JTextArea txtObservaciones;

    private boolean guardado = false;
    private Integer idProyectoEdicion = null;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FormProyecto(
            Window parent
    ) {

        super(
                parent,
                "Nuevo proyecto",
                ModalityType.APPLICATION_MODAL
        );

        setSize(900, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarEmpresas();
        cargarSectores();
        cargarTiposActividad();
    }

    public FormProyecto(
        Window parent,
        int idProyecto
) {

    this(parent);

    this.idProyectoEdicion =
            idProyecto;

    setTitle("Editar proyecto");

    cargarDatosEdicion();
}


    public boolean isGuardado() {

        return guardado;
    }

    private void crearInterfaz() {

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

        JPanel campos =
                new JPanel(
                        new GridLayout(
                                16,
                                2,
                                10,
                                8
                        )
                );

        campos.setBorder(
                BorderFactory.createTitledBorder(
                        "Información del proyecto"
                )
        );

        txtCodigo = new JTextField();
        txtDescripcion = new JTextField();
        cmbEmpresa = new JComboBox<>();
        cmbSector =
                new JComboBox<>();

        cmbPiscina =
                new JComboBox<>();

        txtOrdenCompra =
                new JTextField();

        cmbTipoActividad =
                new JComboBox<>();

        cmbSector.addActionListener(
                e -> cargarPiscinas()
        );
        txtFechaInicio =
                crearCampoFecha();

        txtFechaFinEstimada =
                crearCampoFecha();

        
        txtFechaInicio.setToolTipText(
                "Formato: dd/MM/yyyy. Ejemplo: 15/07/2026"
        );

        txtFechaFinEstimada.setToolTipText(
                "Formato: dd/MM/yyyy. Ejemplo: 30/09/2026"
        );


        txtDiasEstimados = new JTextField();
        txtArea = new JTextField();
        txtEspesor = new JTextField();
        txtFactorCompactacion = new JTextField();
        txtCantidadContratada = new JTextField();
        txtPrecioUnitario = new JTextField();

        aplicarFiltroEntero(
                txtDiasEstimados
        );

        aplicarFiltroDecimal(
                txtArea
        );

        aplicarFiltroDecimal(
                txtEspesor
        );

        aplicarFiltroDecimal(
                txtFactorCompactacion
        );

        aplicarFiltroDecimal(
                txtCantidadContratada
        );

        aplicarFiltroDecimal(
                txtPrecioUnitario
        );

        cmbEstado =
                new JComboBox<>(
                        new String[]{
                                "PLANIFICADO",
                                "EN_EJECUCION",
                                "SUSPENDIDO",
                                "FINALIZADO",
                                "CANCELADO"
                        }
                );

        campos.add(new JLabel("Código del proyecto:"));
        campos.add(txtCodigo);

        campos.add(new JLabel("Descripción:"));
        campos.add(txtDescripcion);

        campos.add(new JLabel("Empresa:"));
        campos.add(cmbEmpresa);

        campos.add(
                new JLabel("Sector:")
        );

        campos.add(cmbSector);

        campos.add(
                new JLabel("Piscina:")
        );

        campos.add(cmbPiscina);

        campos.add(new JLabel("Orden de compra:"));
        campos.add(txtOrdenCompra);

        campos.add(
                new JLabel("Tipo de actividad:")
        );

        campos.add(cmbTipoActividad);

        campos.add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        campos.add(txtFechaInicio);

        campos.add(new JLabel("Fecha fin estimada (dd/MM/yyyy):"));
        campos.add(txtFechaFinEstimada);

        campos.add(new JLabel("Días estimados:"));
        campos.add(txtDiasEstimados);

        campos.add(new JLabel("Área m²:"));
        campos.add(txtArea);

        campos.add(new JLabel("Espesor:"));
        campos.add(txtEspesor);

        campos.add(new JLabel("Factor compactación:"));
        campos.add(txtFactorCompactacion);

        campos.add(new JLabel("Cantidad contratada:"));
        campos.add(txtCantidadContratada);

        campos.add(new JLabel("Precio unitario:"));
        campos.add(txtPrecioUnitario);

        campos.add(new JLabel("Estado:"));
        campos.add(cmbEstado);

        txtObservaciones =
                new JTextArea(5, 30);

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObservaciones =
                new JScrollPane(txtObservaciones);

        scrollObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        JPanel botones =
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
                e -> guardarProyecto()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        botones.add(btnGuardar);
        botones.add(btnCancelar);

        principal.add(
        new JScrollPane(campos),
        BorderLayout.CENTER
);

JPanel panelInferior =
        new JPanel(
                new BorderLayout(
                        10,
                        10
                )
        );

panelInferior.add(
        scrollObservaciones,
        BorderLayout.CENTER
);

panelInferior.add(
        botones,
        BorderLayout.SOUTH
);

principal.add(
        panelInferior,
        BorderLayout.SOUTH
);

setContentPane(principal);
    }

    private void cargarEmpresas() {

        try {

            cmbEmpresa.removeAllItems();

            for (
                    ProyectoDAO.EmpresaItem empresa
                    : ProyectoDAO.obtenerEmpresas()
            ) {

                cmbEmpresa.addItem(empresa);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar empresas:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void guardarProyecto() {
        if (!validarFormulario()) {
                return;
        }

        String codigo =
                txtCodigo.getText().trim();

        String descripcion =
                txtDescripcion.getText().trim();

        if (codigo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el código del proyecto.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (descripcion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la descripción del proyecto.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        ProyectoDAO.EmpresaItem empresa =
                (ProyectoDAO.EmpresaItem)
                        cmbEmpresa.getSelectedItem();

        if (empresa == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una empresa.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        ProyectoDAO.SectorItem sector =
        (ProyectoDAO.SectorItem)
                cmbSector.getSelectedItem();

ProyectoDAO.PiscinaItem piscina =
        (ProyectoDAO.PiscinaItem)
                cmbPiscina.getSelectedItem();

ProyectoDAO.TipoActividadItem actividad =
        (ProyectoDAO.TipoActividadItem)
                cmbTipoActividad.getSelectedItem();

Integer idSector =
        sector == null
                ? null
                : sector.idSector();

Integer idPiscina =
        piscina == null
                ? null
                : piscina.idPiscina();

Integer idTipoActividad =
        actividad == null
                ? null
                : actividad.idTipoActividad();

        try {

            if (
    idProyectoEdicion == null
) {

    ProyectoDAO.insertar(
            codigo,
            descripcion,
            empresa.idEmpresa(),
            idSector,
            idPiscina,
            txtOrdenCompra.getText().trim(),
            idTipoActividad,
            convertirFecha(
                    txtFechaInicio.getText()
            ),
            convertirFecha(
                    txtFechaFinEstimada.getText()
            ),
            null,
            convertirEntero(
                    txtDiasEstimados.getText()
            ),
            convertirDecimal(
                    txtArea.getText()
            ),
            convertirDecimal(
                    txtEspesor.getText()
            ),
            convertirDecimal(
                    txtFactorCompactacion.getText()
            ),
            convertirDecimal(
                    txtCantidadContratada.getText()
            ),
            convertirDecimal(
                    txtPrecioUnitario.getText()
            ),
            cmbEstado
                    .getSelectedItem()
                    .toString(),
            txtObservaciones
                    .getText()
                    .trim()
    );

} else {

    ProyectoDAO.actualizar(
            idProyectoEdicion,
            codigo,
            descripcion,
            empresa.idEmpresa(),
            idSector,
            idPiscina,
            txtOrdenCompra.getText().trim(),
            idTipoActividad,
            convertirFecha(
                    txtFechaInicio.getText()
            ),
            convertirFecha(
                    txtFechaFinEstimada.getText()
            ),
            null,
            convertirEntero(
                    txtDiasEstimados.getText()
            ),
            convertirDecimal(
                    txtArea.getText()
            ),
            convertirDecimal(
                    txtEspesor.getText()
            ),
            convertirDecimal(
                    txtFactorCompactacion.getText()
            ),
            convertirDecimal(
                    txtCantidadContratada.getText()
            ),
            convertirDecimal(
                    txtPrecioUnitario.getText()
            ),
            cmbEstado
                    .getSelectedItem()
                    .toString(),
            txtObservaciones
                    .getText()
                    .trim()
    );
}

            guardado = true;

            JOptionPane.showMessageDialog(
        this,
        idProyectoEdicion == null
                ? "Proyecto guardado correctamente."
                : "Proyecto actualizado correctamente.",
        "LPP Smart ERP",
        JOptionPane.INFORMATION_MESSAGE
);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el proyecto:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private LocalDate convertirFecha(
            String texto
    ) throws Exception {

        if (
            texto == null
            || texto.trim().isEmpty()
        ) {

            return null;
        }

        try {

            return LocalDate.parse(
                    texto.trim(),
                    FORMATO_FECHA
            );

        } catch (DateTimeParseException e) {

            throw new Exception(
                    "La fecha debe tener formato dd/MM/yyyy."
            );
        }
    }

    private Integer convertirEntero(
            String texto
    ) {

        if (
            texto == null
            || texto.trim().isEmpty()
        ) {

            return null;
        }

        return Integer.parseInt(
                texto.trim()
        );
    }

    private Double convertirDecimal(
            String texto
    ) {

        if (
            texto == null
            || texto.trim().isEmpty()
        ) {

            return null;
        }

        return Double.parseDouble(
                texto.trim()
                        .replace(",", ".")
        );
    }
    private boolean validarFormulario() {

    if (txtCodigo.getText().trim().isEmpty()) {

        mostrarErrorCampo(
                txtCodigo,
                "Ingrese el código del proyecto."
        );

        return false;
    }

    if (txtDescripcion.getText().trim().isEmpty()) {

        mostrarErrorCampo(
                txtDescripcion,
                "Ingrese la descripción del proyecto."
        );

        return false;
    }

    if (cmbEmpresa.getSelectedItem() == null) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una empresa.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        cmbEmpresa.requestFocus();
        return false;
    }

    if (!validarFechaCampo(
            txtFechaInicio,
            "Fecha de inicio"
    )) {
        return false;
    }

    if (!validarFechaCampo(
            txtFechaFinEstimada,
            "Fecha fin estimada"
    )) {
        return false;
    }

    if (!validarEnteroCampo(
            txtDiasEstimados,
            "Días estimados"
    )) {
        return false;
    }

    if (!validarDecimalCampo(
            txtArea,
            "Área"
    )) {
        return false;
    }

    if (!validarDecimalCampo(
            txtEspesor,
            "Espesor"
    )) {
        return false;
    }

    if (!validarDecimalCampo(
            txtFactorCompactacion,
            "Factor de compactación"
    )) {
        return false;
    }

    if (!validarDecimalCampo(
            txtCantidadContratada,
            "Cantidad contratada"
    )) {
        return false;
    }

    if (!validarDecimalCampo(
            txtPrecioUnitario,
            "Precio unitario"
    )) {
        return false;
    }

    return true;
}
private boolean validarFechaCampo(
        JTextField campo,
        String nombreCampo
) {

    String texto =
            campo.getText().trim();

    if (texto.isEmpty()) {
        return true;
    }

    try {

        LocalDate.parse(
                texto,
                FORMATO_FECHA
        );

        campo.setBackground(Color.WHITE);
        return true;

    } catch (DateTimeParseException e) {

        mostrarErrorCampo(
                campo,
                nombreCampo
                        + " debe tener el formato dd/MM/yyyy.\n"
                        + "Ejemplo: 15/07/2026"
        );

        return false;
    }
}
private boolean validarEnteroCampo(
        JTextField campo,
        String nombreCampo
) {

    String texto =
            campo.getText().trim();

    if (texto.isEmpty()) {
        return true;
    }

    try {

        Integer.parseInt(texto);

        campo.setBackground(Color.WHITE);
        return true;

    } catch (NumberFormatException e) {

        mostrarErrorCampo(
                campo,
                nombreCampo
                        + " debe contener un número entero.\n"
                        + "Ejemplo: 30"
        );

        return false;
    }
}

private boolean validarDecimalCampo(
        JTextField campo,
        String nombreCampo
) {

    String texto =
            campo.getText().trim();

    if (texto.isEmpty()) {
        return true;
    }

    try {

        Double.parseDouble(
                texto.replace(",", ".")
        );

        campo.setBackground(Color.WHITE);
        return true;

    } catch (NumberFormatException e) {

        mostrarErrorCampo(
                campo,
                nombreCampo
                        + " debe contener un valor numérico.\n"
                        + "Ejemplo: 12,50"
        );

        return false;
    }
}
private void mostrarErrorCampo(
        JTextField campo,
        String mensaje
) {

    campo.setBackground(
            new Color(
                    255,
                    230,
                    230
            )
    );

    JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos incorrectos",
            JOptionPane.WARNING_MESSAGE
    );

    campo.requestFocus();
    campo.selectAll();
}

private JFormattedTextField crearCampoFecha() {

    try {

        MaskFormatter formato =
                new MaskFormatter(
                        "##/##/####"
                );

        formato.setPlaceholderCharacter('_');
        formato.setAllowsInvalid(false);

        return new JFormattedTextField(
                formato
        );

    } catch (java.text.ParseException e) {

        throw new IllegalStateException(
                "No fue posible crear el campo fecha.",
                e
        );
    }
}


private void cargarDatosEdicion() {

    try {

        ProyectoDAO.ProyectoDetalle proyecto =
                ProyectoDAO.obtenerPorId(
                        idProyectoEdicion
                );

        txtCodigo.setText(
                proyecto.codigoProyecto()
        );

        txtDescripcion.setText(
                proyecto.descripcion()
        );

        txtOrdenCompra.setText(
                proyecto.ordenCompra()
        );

        txtFechaInicio.setText(
                formatearFecha(
                        proyecto.fechaInicio()
                )
        );

        txtFechaFinEstimada.setText(
                formatearFecha(
                        proyecto.fechaFinEstimada()
                )
        );

        txtDiasEstimados.setText(
                convertirTexto(
                        proyecto.diasEstimados()
                )
        );

        txtArea.setText(
                convertirTexto(
                        proyecto.areaM2()
                )
        );

        txtEspesor.setText(
                convertirTexto(
                        proyecto.espesor()
                )
        );

        txtFactorCompactacion.setText(
                convertirTexto(
                        proyecto.factorCompactacion()
                )
        );

        txtCantidadContratada.setText(
                convertirTexto(
                        proyecto.cantidadContratada()
                )
        );

        txtPrecioUnitario.setText(
                convertirTexto(
                        proyecto.precioUnitario()
                )
        );

        txtObservaciones.setText(
                proyecto.observaciones()
        );

        seleccionarEmpresa(
                proyecto.idEmpresa()
        );

        seleccionarSector(
                proyecto.idSector()
        );

        cargarPiscinas();

        seleccionarPiscina(
                proyecto.idPiscina()
        );

        seleccionarTipoActividad(
                proyecto.idTipoActividad()
        );

        cmbEstado.setSelectedItem(
                proyecto.estado()
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar el proyecto:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        dispose();
    }
}
private void seleccionarEmpresa(
        int idEmpresa
) {

    for (
            int i = 0;
            i < cmbEmpresa.getItemCount();
            i++
    ) {

        ProyectoDAO.EmpresaItem item =
                cmbEmpresa.getItemAt(i);

        if (
            item.idEmpresa() == idEmpresa
        ) {

            cmbEmpresa.setSelectedIndex(i);
            return;
        }
    }
}
private void seleccionarSector(
        Integer idSector
) {

    if (idSector == null) {
        return;
    }

    for (
            int i = 0;
            i < cmbSector.getItemCount();
            i++
    ) {

        ProyectoDAO.SectorItem item =
                cmbSector.getItemAt(i);

        if (
            item.idSector() == idSector
        ) {

            cmbSector.setSelectedIndex(i);
            return;
        }
    }
}
private void seleccionarPiscina(
        Integer idPiscina
) {

    if (idPiscina == null) {
        return;
    }

    for (
            int i = 0;
            i < cmbPiscina.getItemCount();
            i++
    ) {

        ProyectoDAO.PiscinaItem item =
                cmbPiscina.getItemAt(i);

        if (
            item.idPiscina() == idPiscina
        ) {

            cmbPiscina.setSelectedIndex(i);
            return;
        }
    }
}
private void seleccionarTipoActividad(
        Integer idTipoActividad
) {

    if (idTipoActividad == null) {
        return;
    }

    for (
            int i = 0;
            i < cmbTipoActividad.getItemCount();
            i++
    ) {

        ProyectoDAO.TipoActividadItem item =
                cmbTipoActividad.getItemAt(i);

        if (
            item.idTipoActividad()
                    == idTipoActividad
        ) {

            cmbTipoActividad.setSelectedIndex(i);
            return;
        }
    }
}
private String formatearFecha(
        LocalDate fecha
) {

    return fecha == null
            ? ""
            : fecha.format(
                    FORMATO_FECHA
            );
}
private String convertirTexto(
        Object valor
) {

    return valor == null
            ? ""
            : valor.toString();
}

private void aplicarFiltroEntero(
        JTextField campo
) {

    ((AbstractDocument) campo.getDocument())
            .setDocumentFilter(
                    new DocumentFilter() {

                        @Override
                        public void insertString(
                                FilterBypass fb,
                                int offset,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            reemplazar(
                                    fb,
                                    offset,
                                    0,
                                    texto,
                                    atributos
                            );
                        }

                        @Override
                        public void replace(
                                FilterBypass fb,
                                int offset,
                                int longitud,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            reemplazar(
                                    fb,
                                    offset,
                                    longitud,
                                    texto,
                                    atributos
                            );
                        }

                        private void reemplazar(
                                FilterBypass fb,
                                int offset,
                                int longitud,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            String actual =
                                    fb.getDocument().getText(
                                            0,
                                            fb.getDocument()
                                                    .getLength()
                                    );

                            String nuevo =
                                    actual.substring(
                                            0,
                                            offset
                                    )
                                    + (
                                        texto == null
                                                ? ""
                                                : texto
                                    )
                                    + actual.substring(
                                            offset + longitud
                                    );

                            if (nuevo.matches("\\d*")) {

                                fb.replace(
                                        offset,
                                        longitud,
                                        texto,
                                        atributos
                                );
                            } else {

                                Toolkit.getDefaultToolkit()
                                        .beep();
                            }
                        }
                    }
            );
}

private void aplicarFiltroDecimal(
        JTextField campo
) {

    ((AbstractDocument) campo.getDocument())
            .setDocumentFilter(
                    new DocumentFilter() {

                        @Override
                        public void insertString(
                                FilterBypass fb,
                                int offset,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            reemplazar(
                                    fb,
                                    offset,
                                    0,
                                    texto,
                                    atributos
                            );
                        }

                        @Override
                        public void replace(
                                FilterBypass fb,
                                int offset,
                                int longitud,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            reemplazar(
                                    fb,
                                    offset,
                                    longitud,
                                    texto,
                                    atributos
                            );
                        }

                        private void reemplazar(
                                FilterBypass fb,
                                int offset,
                                int longitud,
                                String texto,
                                AttributeSet atributos
                        ) throws BadLocationException {

                            String actual =
                                    fb.getDocument().getText(
                                            0,
                                            fb.getDocument()
                                                    .getLength()
                                    );

                            String nuevo =
                                    actual.substring(
                                            0,
                                            offset
                                    )
                                    + (
                                        texto == null
                                                ? ""
                                                : texto
                                    )
                                    + actual.substring(
                                            offset + longitud
                                    );

                            if (
                                nuevo.matches(
                                        "\\d*([.,]\\d*)?"
                                )
                            ) {

                                fb.replace(
                                        offset,
                                        longitud,
                                        texto,
                                        atributos
                                );

                            } else {

                                Toolkit.getDefaultToolkit()
                                        .beep();
                            }
                        }
                    }
            );
}
private void cargarSectores() {

    try {

        cmbSector.removeAllItems();

        for (
                ProyectoDAO.SectorItem sector
                : ProyectoDAO.obtenerSectores()
        ) {

            cmbSector.addItem(sector);
        }

        cargarPiscinas();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar sectores:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void cargarPiscinas() {

    try {

        cmbPiscina.removeAllItems();

        ProyectoDAO.SectorItem sector =
                (ProyectoDAO.SectorItem)
                        cmbSector.getSelectedItem();

        Integer idSector =
                sector == null
                        ? null
                        : sector.idSector();

        for (
                ProyectoDAO.PiscinaItem piscina
                : ProyectoDAO.obtenerPiscinasPorSector(
                        idSector
                )
        ) {

            cmbPiscina.addItem(piscina);
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar piscinas:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void cargarTiposActividad() {

    try {

        cmbTipoActividad.removeAllItems();

        for (
                ProyectoDAO.TipoActividadItem actividad
                : ProyectoDAO.obtenerTiposActividad()
        ) {

            cmbTipoActividad.addItem(
                    actividad
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar tipos de actividad:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
}