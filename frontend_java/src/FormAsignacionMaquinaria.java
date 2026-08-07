import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormAsignacionMaquinaria extends JDialog {

    private JComboBox<AsignacionMaquinariaAPI.ProyectoItem>
            cmbProyecto;

    private JComboBox<AsignacionMaquinariaAPI.MaquinariaItem>
            cmbMaquinaria;

    private JComboBox<String> cmbEstado;

    private JTextField txtPropietario;
    private JTextField txtTarifa;
    private JTextField txtCantidad;

    private JFormattedTextField txtFechaIngreso;
    private JFormattedTextField txtFechaSalida;

    private JTextArea txtObservaciones;

    private boolean guardado = false;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    private Integer idAsignacionEditar = null;
    private boolean modoEdicion = false;
    private boolean modoDetalle = false;

    public FormAsignacionMaquinaria(
            Window parent
    ) {

        super(
                parent,
                "Nueva asignación de maquinaria",
                ModalityType.APPLICATION_MODAL
        );

        setSize(850, 620);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        crearInterfaz();
        cargarCatalogos();
    }

    public boolean isGuardado() {

        return guardado;
    }

    public FormAsignacionMaquinaria(
        Window owner,
        int idAsignacion
) {

    this(owner);

    this.idAsignacionEditar = idAsignacion;
    this.modoEdicion = true;

    cargarAsignacion();
}
public FormAsignacionMaquinaria(
        Window owner,
        int idAsignacion,
        boolean detalle
) {

    this(owner);

    this.idAsignacionEditar = idAsignacion;
    this.modoDetalle = detalle;

    setTitle(
            "Detalle de asignación"
    );

    cargarAsignacion();

    bloquearFormulario();
}

    private void crearInterfaz() {

        JPanel principal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
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
                                8,
                                2,
                                10,
                                10
                        )
                );

        campos.setBorder(
                BorderFactory.createTitledBorder(
                        "Información de la asignación"
                )
        );

        cmbProyecto =
                new JComboBox<>();

        cmbMaquinaria =
                new JComboBox<>();

        txtPropietario =
                new JTextField();

        txtTarifa =
                new JTextField();

        txtCantidad =
                new JTextField("1");

        txtFechaIngreso =
                crearCampoFecha();

        txtFechaSalida =
                crearCampoFecha();

        cmbEstado =
                new JComboBox<>(
                        new String[]{
                                "ASIGNADA",
                                "EN_OBRA",
                                "RETIRADA",
                                "CANCELADA"
                        }
                );

        txtPropietario.setEditable(false);
        txtTarifa.setEditable(true);

        Color colorBloqueado =
                new Color(
                        235,
                        235,
                        235
                );

        txtPropietario.setBackground(
                colorBloqueado
        );

        aplicarFiltroEntero(
                txtCantidad
        );

        cmbMaquinaria.addActionListener(
                e -> completarDatosMaquinaria()
        );

        campos.add(
                new JLabel("Proyecto:")
        );

        campos.add(cmbProyecto);

        campos.add(
                new JLabel("Maquinaria:")
        );

        campos.add(cmbMaquinaria);

        campos.add(
                new JLabel("Propietario:")
        );

        campos.add(txtPropietario);

        campos.add(
                new JLabel("Tarifa hora:")
        );

        campos.add(txtTarifa);

        campos.add(
                new JLabel("Cantidad:")
        );

        campos.add(txtCantidad);

        campos.add(
                new JLabel(
                        "Fecha ingreso (dd/MM/yyyy):"
                )
        );

        campos.add(txtFechaIngreso);

        campos.add(
                new JLabel(
                        "Fecha salida (dd/MM/yyyy):"
                )
        );

        campos.add(txtFechaSalida);

        campos.add(
                new JLabel("Estado:")
        );

        campos.add(cmbEstado);

        txtObservaciones =
                new JTextArea(
                        5,
                        30
                );

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObservaciones =
                new JScrollPane(
                        txtObservaciones
                );

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
                e -> guardarAsignacion()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        botones.add(btnGuardar);
        botones.add(btnCancelar);

        JPanel inferior =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        inferior.add(
                scrollObservaciones,
                BorderLayout.CENTER
        );

        inferior.add(
                botones,
                BorderLayout.SOUTH
        );

        principal.add(
                campos,
                BorderLayout.CENTER
        );

        principal.add(
                inferior,
                BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    private void cargarCatalogos() {

        try {

            cmbProyecto.removeAllItems();

            for (
                    AsignacionMaquinariaAPI.ProyectoItem proyecto
                    : AsignacionMaquinariaAPI
                            .obtenerProyectosActivos()
            ) {

                cmbProyecto.addItem(
                        proyecto
                );
            }

            cmbMaquinaria.removeAllItems();

            for (
                    AsignacionMaquinariaAPI.MaquinariaItem maquinaria
                    : AsignacionMaquinariaAPI
                            .obtenerMaquinariasDisponibles()
            ) {

                cmbMaquinaria.addItem(
                        maquinaria
                );
            }

            completarDatosMaquinaria();

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

    private void completarDatosMaquinaria() {

    AsignacionMaquinariaAPI.MaquinariaItem maquinaria =
            (AsignacionMaquinariaAPI.MaquinariaItem)
                    cmbMaquinaria.getSelectedItem();

    if (maquinaria == null) {

        txtPropietario.setText("");
        txtTarifa.setText("");
        return;
    }

    txtPropietario.setText(
            maquinaria.propietario()
    );

    Double tarifaReferencia =
            maquinaria.tarifaReferencia();

    txtTarifa.setText(
            tarifaReferencia == null
                    ? ""
                    : String.format(
                            java.util.Locale.US,
                            "%.2f",
                            tarifaReferencia
                    )
    );
}

    private void guardarAsignacion() {

        AsignacionMaquinariaAPI.ProyectoItem proyecto =
                (AsignacionMaquinariaAPI.ProyectoItem)
                        cmbProyecto.getSelectedItem();

        if (proyecto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen proyectos activos disponibles.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        AsignacionMaquinariaAPI.MaquinariaItem maquinaria =
                (AsignacionMaquinariaAPI.MaquinariaItem)
                        cmbMaquinaria.getSelectedItem();

        if (maquinaria == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen maquinarias operativas disponibles.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int cantidad;

        try {

            cantidad =
                    Integer.parseInt(
                            txtCantidad
                                    .getText()
                                    .trim()
                    );

            if (cantidad <= 0) {

                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad debe ser un número "
                            + "entero mayor que cero.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCantidad.requestFocus();
            return;
        }

        LocalDate fechaIngreso;

        try {

            fechaIngreso =
                    convertirFechaObligatoria(
                            txtFechaIngreso.getText(),
                            "Fecha de ingreso"
                    );

        } catch (Exception e) {

            mostrarErrorFecha(
                    txtFechaIngreso,
                    e.getMessage()
            );

            return;
        }

        LocalDate fechaSalida;

        try {

            fechaSalida =
                    convertirFechaOpcional(
                            txtFechaSalida.getText(),
                            "Fecha de salida"
                    );

        } catch (Exception e) {

            mostrarErrorFecha(
                    txtFechaSalida,
                    e.getMessage()
            );

            return;
        }

        if (
            fechaSalida != null
            && fechaSalida.isBefore(
                    fechaIngreso
            )
        ) {

            mostrarErrorFecha(
                    txtFechaSalida,
                    "La fecha de salida no puede ser "
                            + "anterior a la fecha de ingreso."
            );

            return;
        }

        Double tarifaHora = null;

String textoTarifa =
        txtTarifa.getText().trim();

if (!textoTarifa.isEmpty()) {

    try {

        tarifaHora =
                Double.parseDouble(
                        textoTarifa.replace(",", ".")
                );

        if (tarifaHora < 0) {

            throw new NumberFormatException();
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "La tarifa debe contener un valor numérico.\n"
                        + "Ejemplo: 17,00",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtTarifa.requestFocus();
        txtTarifa.selectAll();
        return;
    }
}

        try {

    if (modoEdicion) {

        AsignacionMaquinariaAPI.actualizar(
                idAsignacionEditar,
                cantidad,
                fechaIngreso,
                fechaSalida,
                tarifaHora,
                cmbEstado
                        .getSelectedItem()
                        .toString(),
                txtObservaciones
                        .getText()
                        .trim()
        );

        JOptionPane.showMessageDialog(
                this,
                "Asignación actualizada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        AsignacionMaquinariaAPI.insertar(
                proyecto.idProyecto(),
                maquinaria.idMaquinaria(),
                cantidad,
                fechaIngreso,
                fechaSalida,
                tarifaHora,
                cmbEstado
                        .getSelectedItem()
                        .toString(),
                txtObservaciones
                        .getText()
                        .trim()
        );

        JOptionPane.showMessageDialog(
                this,
                "Asignación guardada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    guardado = true;

    dispose();

} catch (Exception e) {

    JOptionPane.showMessageDialog(
            this,
            "Error al guardar la asignación:\n"
                    + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
    );

    e.printStackTrace();
}
    }


    private JFormattedTextField crearCampoFecha() {

        try {

            MaskFormatter mascara =
                    new MaskFormatter(
                            "##/##/####"
                    );

            mascara.setPlaceholderCharacter('_');
            mascara.setAllowsInvalid(false);

            return new JFormattedTextField(
                    mascara
            );

        } catch (ParseException e) {

            throw new IllegalStateException(
                    "No fue posible crear el campo fecha.",
                    e
            );
        }
    }

    private LocalDate convertirFechaObligatoria(
            String texto,
            String nombreCampo
    ) throws Exception {

        String valor =
                limpiarFecha(texto);

        if (valor.isEmpty()) {

            throw new Exception(
                    nombreCampo
                            + " es obligatoria."
            );
        }

        return convertirFecha(
                valor,
                nombreCampo
        );
    }

    private LocalDate convertirFechaOpcional(
            String texto,
            String nombreCampo
    ) throws Exception {

        String valor =
                limpiarFecha(texto);

        if (valor.isEmpty()) {
            return null;
        }

        return convertirFecha(
                valor,
                nombreCampo
        );
    }

    private LocalDate convertirFecha(
            String texto,
            String nombreCampo
    ) throws Exception {

        try {

            return LocalDate.parse(
                    texto,
                    FORMATO_FECHA
            );

        } catch (DateTimeParseException e) {

            throw new Exception(
                    nombreCampo
                            + " debe tener formato dd/MM/yyyy.\n"
                            + "Ejemplo: 15/07/2026"
            );
        }
    }

    private String limpiarFecha(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        String valor =
                texto.trim();

        if (
            valor.isEmpty()
            || valor.equals("__/__/____")
        ) {

            return "";
        }

        return valor;
    }

    private void mostrarErrorFecha(
            JTextField campo,
            String mensaje
    ) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos incorrectos",
                JOptionPane.WARNING_MESSAGE
        );

        campo.requestFocus();
        campo.selectAll();
    }

    private void aplicarFiltroEntero(
            JTextField campo
    ) {

        ((AbstractDocument)
                campo.getDocument())
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
                                        fb.getDocument()
                                                .getText(
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
                                                offset
                                                        + longitud
                                        );

                                if (
                                    nuevo.matches(
                                            "\\d*"
                                    )
                                ) {

                                    fb.replace(
                                            offset,
                                            longitud,
                                            texto,
                                            atributos
                                    );

                                } else {

                                    Toolkit
                                            .getDefaultToolkit()
                                            .beep();
                                }
                            }
                        }
                );
    }

    private void seleccionarProyecto(
        int idProyecto
) {

    for (
            int i = 0;
            i < cmbProyecto.getItemCount();
            i++
    ) {

        AsignacionMaquinariaAPI.ProyectoItem item =
                cmbProyecto.getItemAt(i);

        if (
            item.idProyecto()
                    == idProyecto
        ) {

            cmbProyecto.setSelectedIndex(i);
            return;
        }
    }
}

private void seleccionarMaquinaria(
        int idMaquinaria
) {

    for (
            int i = 0;
            i < cmbMaquinaria.getItemCount();
            i++
    ) {

        AsignacionMaquinariaAPI.MaquinariaItem item =
                cmbMaquinaria.getItemAt(i);

        if (
            item.idMaquinaria()
                    == idMaquinaria
        ) {

            cmbMaquinaria.setSelectedIndex(i);
            return;
        }
    }
}

private void bloquearFormulario() {

    cmbProyecto.setEnabled(false);
    cmbMaquinaria.setEnabled(false);

    txtCantidad.setEditable(false);
    txtTarifa.setEditable(false);

    txtFechaIngreso.setEditable(false);
    txtFechaSalida.setEditable(false);

    cmbEstado.setEnabled(false);

    txtObservaciones.setEditable(false);
}

    private void cargarAsignacion() {

    try {

        AsignacionMaquinariaAPI.AsignacionDetalle asignacion =
                AsignacionMaquinariaAPI.obtenerPorId(
                        idAsignacionEditar
                );

        // Proyecto
        seleccionarProyecto(
                asignacion.idProyecto()
        );

        // Maquinaria
        seleccionarMaquinaria(
                asignacion.idMaquinaria()
        );

        // Bloquear cambios
        cmbProyecto.setEnabled(false);
        cmbMaquinaria.setEnabled(false);

        // Cantidad
        txtCantidad.setText(
                String.valueOf(
                        asignacion.cantidad()
                )
        );

        // Tarifa
        txtTarifa.setText(
                asignacion.tarifaHora() == null
                        ? ""
                        : String.valueOf(
                                asignacion.tarifaHora()
                        )
        );

        // Fecha ingreso
        if (asignacion.fechaIngreso() != null) {

            txtFechaIngreso.setText(
                    asignacion.fechaIngreso()
                            .format(FORMATO_FECHA)
            );
        }

        // Fecha salida
        if (asignacion.fechaSalida() != null) {

            txtFechaSalida.setText(
                    asignacion.fechaSalida()
                            .format(FORMATO_FECHA)
            );
        }

        // Estado
        cmbEstado.setSelectedItem(
                asignacion.estado()
        );

        // Observaciones
        txtObservaciones.setText(
                asignacion.observaciones()
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        dispose();
    }
}

}
