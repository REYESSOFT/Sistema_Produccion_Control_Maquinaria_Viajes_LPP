import javax.swing.*;
import java.awt.*;

public class FormEntidadMaquinaria extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> cmbTipoEntidad;
    private JTextField txtIdentificacion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextArea txtObservaciones;

    private boolean guardado = false;
    private Integer idEntidadEditar = null;
    private boolean modoEdicion = false;
    private boolean modoDetalle = false;

    public FormEntidadMaquinaria(
            Window owner
    ) {

        super(
                owner,
                "Nueva entidad",
                ModalityType.APPLICATION_MODAL
        );

        setSize(
                700,
                560
        );

        setLocationRelativeTo(
                owner
        );

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        crearInterfaz();
    }


    public FormEntidadMaquinaria(
        Window owner,
        int idEntidad
) {

    this(owner);

    this.idEntidadEditar =
            idEntidad;

    this.modoEdicion =
            true;

    setTitle(
            "Editar entidad"
    );

    cargarEntidad();
}

    public boolean isGuardado() {

        return guardado;
    }


    public FormEntidadMaquinaria(
        Window owner,
        int idEntidad,
        boolean detalle
) {

    this(owner);

    this.idEntidadEditar =
            idEntidad;

    this.modoDetalle =
            detalle;

    setTitle(
            "Detalle de la entidad"
    );

    cargarEntidad();

    bloquearFormulario();
}

    private void crearInterfaz() {

        JPanel principal =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        JPanel campos =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                10
                        )
                );

        campos.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de la entidad"
                )
        );

        txtNombre =
                new JTextField();

        cmbTipoEntidad =
                new JComboBox<>(
                        new String[]{
                                "EMPRESA",
                                "PERSONA",
                                "OTRO"
                        }
                );

        txtIdentificacion =
                new JTextField();

        txtTelefono =
                new JTextField();

        txtCorreo =
                new JTextField();

        campos.add(
                new JLabel(
                        "Nombre:"
                )
        );

        campos.add(
                txtNombre
        );

        campos.add(
                new JLabel(
                        "Tipo de entidad:"
                )
        );

        campos.add(
                cmbTipoEntidad
        );

        campos.add(
                new JLabel(
                        "Identificación:"
                )
        );

        campos.add(
                txtIdentificacion
        );

        campos.add(
                new JLabel(
                        "Teléfono:"
                )
        );

        campos.add(
                txtTelefono
        );

        campos.add(
                new JLabel(
                        "Correo:"
                )
        );

        campos.add(
                txtCorreo
        );

        txtObservaciones =
                new JTextArea(
                        6,
                        35
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
                new JButton(
                        "Guardar"
                );

        JButton btnCancelar =
                new JButton(
                        "Cancelar"
                );

        btnGuardar.addActionListener(
                e -> guardarEntidad()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        if (!modoDetalle) {

    botones.add(
            btnGuardar
    );

} else {

    btnCancelar.setText(
            "Cerrar"
    );
}

botones.add(
        btnCancelar
);

        principal.add(
                campos,
                BorderLayout.NORTH
        );

        principal.add(
                scrollObservaciones,
                BorderLayout.CENTER
        );

        principal.add(
                botones,
                BorderLayout.SOUTH
        );

        setContentPane(
                principal
        );
    }

    private void guardarEntidad() {

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        String tipoEntidad =
                cmbTipoEntidad
                        .getSelectedItem()
                        .toString();

        String identificacion =
                txtIdentificacion
                        .getText()
                        .trim();

        String telefono =
                txtTelefono
                        .getText()
                        .trim();

        String correo =
                txtCorreo
                        .getText()
                        .trim();

        String observaciones =
                txtObservaciones
                        .getText()
                        .trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return;
        }

        try {

    if (modoEdicion) {

        EntidadMaquinariaDAO.actualizar(
                idEntidadEditar,
                nombre,
                tipoEntidad,
                identificacion,
                telefono,
                correo,
                observaciones,
                "ACTIVO"
        );

        JOptionPane.showMessageDialog(
                this,
                "Entidad actualizada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        EntidadMaquinariaDAO.insertar(
                nombre,
                tipoEntidad,
                identificacion,
                telefono,
                correo,
                observaciones
        );

        JOptionPane.showMessageDialog(
                this,
                "Entidad guardada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    guardado = true;

    dispose();

} catch (Exception ex) {

    JOptionPane.showMessageDialog(
            this,
            "Error al guardar la entidad:\n"
                    + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
    );

    ex.printStackTrace();
}
    }

    private void bloquearFormulario() {

    txtNombre.setEditable(false);

    cmbTipoEntidad.setEnabled(false);

    txtIdentificacion.setEditable(false);

    txtTelefono.setEditable(false);

    txtCorreo.setEditable(false);

    txtObservaciones.setEditable(false);
}


    private void cargarEntidad() {

    try {

        EntidadMaquinariaDAO.EntidadDetalle entidad =
                EntidadMaquinariaDAO.obtenerPorId(
                        idEntidadEditar
                );

        txtNombre.setText(
                entidad.nombre()
        );

        cmbTipoEntidad.setSelectedItem(
                entidad.tipoEntidad()
        );

        txtIdentificacion.setText(
                entidad.identificacion()
        );

        txtTelefono.setText(
                entidad.telefono()
        );

        txtCorreo.setText(
                entidad.correo()
        );

        txtObservaciones.setText(
                entidad.observaciones()
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar la entidad:\n"
                        + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();

        dispose();
    }
}
}
