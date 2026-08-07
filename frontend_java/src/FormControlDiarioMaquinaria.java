import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormControlDiarioMaquinaria extends JDialog {

    private final int idControl;
    private final Integer idControlMaquinaria;

    private JComboBox<ControlDiarioAPI.MaquinariaAsignadaItem>
            cmbMaquinaria;

    private JTextField txtHoras;
    private JTextArea txtObservaciones;

    private boolean guardado;

    public FormControlDiarioMaquinaria(
            Window propietario,
            int idControl,
            Integer idControlMaquinaria
    ) {

        super(
                propietario,
                idControlMaquinaria == null
                        ? "Agregar maquinaria"
                        : "Editar maquinaria",
                ModalityType.APPLICATION_MODAL
        );

        this.idControl =
                idControl;

        this.idControlMaquinaria =
                idControlMaquinaria;

        setSize(
                620,
                470
        );

        setLocationRelativeTo(
                propietario
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        crearInterfaz();
        cargarMaquinarias();

        if (
                idControlMaquinaria != null
        ) {

            cargarRegistro();
        }
    }

    public boolean isGuardado() {

        return guardado;
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        panelPrincipal.setBackground(
                new Color(
                        244,
                        246,
                        248
                )
        );

        panelPrincipal.add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                crearFormulario(),
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                crearBotones(),
                BorderLayout.SOUTH
        );

        setContentPane(
                panelPrincipal
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(
                false
        );

        JLabel titulo =
                new JLabel(
                        idControlMaquinaria == null
                                ? "Registrar maquinaria utilizada"
                                : "Editar maquinaria utilizada"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        JLabel subtitulo =
                new JLabel(
                        "Control Diario ID: "
                                + idControl
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                new Color(
                        75,
                        85,
                        99
                )
        );

        panel.add(
                titulo
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    private JPanel crearFormulario() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(
                false
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
                GridBagConstraints.NORTHWEST;

        JLabel lblMaquinaria =
                new JLabel(
                        "Maquinaria:"
                );

        cmbMaquinaria =
                new JComboBox<>();

        JLabel lblHoras =
                new JLabel(
                        "Horas trabajadas:"
                );

        txtHoras =
                new JTextField();

        JLabel lblObservaciones =
                new JLabel(
                        "Observaciones:"
                );

        txtObservaciones =
                new JTextArea(
                        6,
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

        gbc.gridx =
                0;

        gbc.gridy =
                0;

        gbc.weightx =
                0;

        panel.add(
                lblMaquinaria,
                gbc
        );

        gbc.gridx =
                1;

        gbc.weightx =
                1;

        panel.add(
                cmbMaquinaria,
                gbc
        );

        gbc.gridx =
                0;

        gbc.gridy =
                1;

        gbc.weightx =
                0;

        panel.add(
                lblHoras,
                gbc
        );

        gbc.gridx =
                1;

        gbc.weightx =
                1;

        panel.add(
                txtHoras,
                gbc
        );

        gbc.gridx =
                0;

        gbc.gridy =
                2;

        gbc.weightx =
                0;

        gbc.weighty =
                0;

        panel.add(
                lblObservaciones,
                gbc
        );

        gbc.gridx =
                1;

        gbc.weightx =
                1;

        gbc.weighty =
                1;

        gbc.fill =
                GridBagConstraints.BOTH;

        panel.add(
                scrollObservaciones,
                gbc
        );

        return panel;
    }

    private JPanel crearBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setOpaque(
                false
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
                e -> guardar()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );

        panel.add(
                btnGuardar
        );

        panel.add(
                btnCancelar
        );

        return panel;
    }

    private void cargarMaquinarias() {

        try {

            List<ControlDiarioAPI.MaquinariaAsignadaItem> lista =
                    ControlDiarioAPI.obtenerMaquinariasAsignadas(
                            idControl
                    );

            cmbMaquinaria.removeAllItems();

            for (
                    ControlDiarioAPI.MaquinariaAsignadaItem item
                    : lista
            ) {

                cmbMaquinaria.addItem(
                        item
                );
            }

            if (
                    lista.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "No existen maquinarias asignadas al proyecto "
                                + "en la fecha del Control Diario.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las maquinarias:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void cargarRegistro() {

        try {

            ControlDiarioAPI.ControlMaquinariaDetalle registro =
                    ControlDiarioAPI.obtenerMaquinariaPorId(
                            idControlMaquinaria
                    );

            seleccionarMaquinaria(
                    registro.idMaquinaria()
            );

            txtHoras.setText(
                    String.valueOf(
                            registro.horasTrabajadas()
                    )
            );

            txtObservaciones.setText(
                    registro.observaciones()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el registro:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
            dispose();
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

            ControlDiarioAPI.MaquinariaAsignadaItem item =
                    cmbMaquinaria.getItemAt(
                            i
                    );

            if (
                    item.idMaquinaria()
                            == idMaquinaria
            ) {

                cmbMaquinaria.setSelectedIndex(
                        i
                );

                return;
            }
        }
    }

    private void guardar() {

        try {

            ControlDiarioAPI.MaquinariaAsignadaItem maquinaria =
                    (ControlDiarioAPI.MaquinariaAsignadaItem)
                            cmbMaquinaria.getSelectedItem();

            if (
                    maquinaria == null
            ) {

                throw new Exception(
                        "Debe seleccionar una maquinaria."
                );
            }

            String textoHoras =
                    txtHoras.getText().trim();

            if (
                    textoHoras.isEmpty()
            ) {

                throw new Exception(
                        "Debe ingresar las horas trabajadas."
                );
            }

            double horasTrabajadas;

            try {

                horasTrabajadas =
                        Double.parseDouble(
                                textoHoras.replace(
                                        ",",
                                        "."
                                )
                        );

            } catch (NumberFormatException e) {

                throw new Exception(
                        "Las horas trabajadas deben ser un número válido."
                );
            }

            String observaciones =
                    txtObservaciones.getText().trim();

            ControlDiarioAPI.guardarMaquinaria(
                    idControl,
                    idControlMaquinaria,
                    maquinaria.idMaquinaria(),
                    horasTrabajadas,
                    observaciones
            );

            guardado =
                    true;

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
