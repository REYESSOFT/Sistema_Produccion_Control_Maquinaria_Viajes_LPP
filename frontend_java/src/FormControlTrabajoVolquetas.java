import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class FormControlTrabajoVolquetas extends JDialog {

    private JTextField txtNumeroGuia;
    private JFormattedTextField txtFecha;
    private JTextField txtCliente;
    private JTextField txtSolicitante;
    private JTextField txtPlaca;
    private JTextField txtChofer;
    private JTextField txtSector;
    private JTextArea txtObservaciones;
    private JTextField txtEncargadoObra;

    private JTable tablaTurnos;
    private JTable tablaParalizaciones;

    private Integer idGuiaEdicion = null;

    public FormControlTrabajoVolquetas(Window parent) {

        super(
                parent,
                "Control de Trabajo de Volquetas - DEVIALTRANSPORT",
                ModalityType.APPLICATION_MODAL
        );

        setSize(1050, 760);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(15, 15));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        JLabel titulo = new JLabel(
                "CONTROL DE TRABAJO DE VOLQUETAS - INTERNOS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel panelCentro =
                new JPanel(new BorderLayout(12, 12));

        panelCentro.add(
                crearPanelCabecera(),
                BorderLayout.NORTH
        );

        JPanel panelTablas =
                new JPanel(new GridLayout(2, 1, 12, 12));

        panelTablas.add(
                crearPanelTurnos()
        );

        panelTablas.add(
                crearPanelParalizaciones()
        );

        panelCentro.add(
                panelTablas,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelCentro,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                crearPanelInferior(),
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelCabecera() {

        JPanel panel =
                new JPanel(new GridLayout(4, 4, 10, 10));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos generales"
                )
        );

        txtNumeroGuia = new JTextField();
        txtFecha = crearCampoFecha();

        txtCliente = new JTextField();
        txtSolicitante = new JTextField();

        txtPlaca = new JTextField();
        txtChofer = new JTextField();

        txtSector = new JTextField();

        panel.add(new JLabel("N° Guía:"));
        panel.add(txtNumeroGuia);

        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Cliente:"));
        panel.add(txtCliente);

        panel.add(new JLabel("Solicitante:"));
        panel.add(txtSolicitante);

        panel.add(new JLabel("Placa:"));
        panel.add(txtPlaca);

        panel.add(new JLabel("Chofer:"));
        panel.add(txtChofer);

        panel.add(new JLabel("Sector:"));
        panel.add(txtSector);

        panel.add(new JLabel());

        return panel;
    }



    private DefaultCellEditor crearEditorHora() {

    try {

        MaskFormatter mascara =
                new MaskFormatter("##:##");

        mascara.setPlaceholderCharacter('_');
        mascara.setAllowsInvalid(false);

        JFormattedTextField campoHora =
                new JFormattedTextField(mascara);

        campoHora.setFocusLostBehavior(
                JFormattedTextField.COMMIT_OR_REVERT
        );

        return new DefaultCellEditor(campoHora);

    } catch (ParseException e) {

        return new DefaultCellEditor(
                new JTextField()
        );
    }
}


    private JPanel crearPanelTurnos() {

        JPanel panel =
                new JPanel(new BorderLayout(8, 8));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Turnos de trabajo"
                )
        );

        String[] columnas = {
                "Turno",
                "Horario Inicio",
                "Horario Fin",
                "Total Horas Trabajadas"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {
                        // Solo se pueden escribir Hora Inicio y Hora Fin.
                        return columna == 1 || columna == 2;
                    }
                };

        modelo.addRow(
                new Object[]{
                        "MAÑANA",
                        "",
                        "",
                        ""
                }
        );

        modelo.addRow(
                new Object[]{
                        "TARDE",
                        "",
                        "",
                        ""
                }
        );

        modelo.addRow(
                new Object[]{
                        "NOCHE",
                        "",
                        "",
                        ""
                }
        );

        tablaTurnos = new JTable(modelo);
        tablaTurnos.setRowHeight(28);


        tablaTurnos.getColumnModel()
            .getColumn(1)
            .setCellEditor(crearEditorHora());

        tablaTurnos.getColumnModel()
            .getColumn(2)
            .setCellEditor(crearEditorHora());


        modelo.addTableModelListener(evento -> {

            int fila = evento.getFirstRow();
            int columna = evento.getColumn();

            if (
                fila >= 0
                && (columna == 1 || columna == 2)
            ) {
                calcularTotalTurno(fila);
            }
        });

        tablaTurnos.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(140);

        tablaTurnos.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(160);

        tablaTurnos.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(160);

        tablaTurnos.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(190);

        panel.add(
                new JScrollPane(tablaTurnos),
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearPanelParalizaciones() {

        JPanel panel =
                new JPanel(new BorderLayout(8, 8));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Paralizaciones"
                )
        );

        String[] columnas = {
                "Código",
                "Descripción",
                "Inicio",
                "Fin",
                "Total"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {

                        // Código y descripción son fijos.
                        return columna == 2 || columna == 3;
                    }
                };

        agregarParalizacion(
                modelo,
                1,
                "Clima"
        );

        agregarParalizacion(
                modelo,
                2,
                "Parada por daño"
        );

        agregarParalizacion(
                modelo,
                3,
                "Falta de área"
        );

        agregarParalizacion(
                modelo,
                4,
                "Mantenimiento"
        );

        agregarParalizacion(
                modelo,
                5,
                "Alimentación"
        );

        agregarParalizacion(
                modelo,
                6,
                "Abastecimiento de combustible"
        );

        agregarParalizacion(
                modelo,
                7,
                "Mantenimiento o reparación (Eq. encendido)"
        );

        agregarParalizacion(
                modelo,
                8,
                "Otros"
        );

        tablaParalizaciones = new JTable(modelo);
        tablaParalizaciones.setRowHeight(27);

        modelo.addTableModelListener(evento -> {

            int fila = evento.getFirstRow();
            int columna = evento.getColumn();

            if (
                fila >= 0
                && (columna == 2 || columna == 3)
            ) {
                calcularTotalParalizacion(fila);
            }
        });


        tablaParalizaciones.getColumnModel()
            .getColumn(2)
            .setCellEditor(crearEditorHora());

        tablaParalizaciones.getColumnModel()
            .getColumn(3)
            .setCellEditor(crearEditorHora());

        tablaParalizaciones.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaParalizaciones.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        tablaParalizaciones.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(350);

        tablaParalizaciones.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        tablaParalizaciones.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        tablaParalizaciones.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(130);

        panel.add(
                new JScrollPane(tablaParalizaciones),
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearPanelInferior() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        JPanel panelObservaciones =
                new JPanel(new BorderLayout(8, 8));

        panelObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        txtObservaciones =
                new JTextArea(3, 30);

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        panelObservaciones.add(
                new JScrollPane(txtObservaciones),
                BorderLayout.CENTER
        );


        JPanel panelEncargado = new JPanel(
            new GridLayout(1, 2, 10, 10)
        );

        txtEncargadoObra = new JTextField();

        panelEncargado.add(
                new JLabel("Encargado de obra:")
        );

        panelEncargado.add(
                txtEncargadoObra
        );

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnCancelar =
                new JButton("Cancelar");

        btnGuardar.addActionListener(
                e -> guardarGuia()
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

        JPanel panelCentro =
            new JPanel(new BorderLayout(10,10));

        panelCentro.add(
            panelObservaciones,
            BorderLayout.CENTER
        );

        panelCentro.add(
            panelEncargado,
            BorderLayout.SOUTH
        );

        panel.add(
            panelCentro,
            BorderLayout.CENTER
        );

        panel.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private void agregarParalizacion(
            DefaultTableModel modelo,
            int codigo,
            String descripcion
    ) {

        modelo.addRow(
                new Object[]{
                        codigo,
                        descripcion,
                        "",
                        "",
                        ""
                }
        );
    }

    private JFormattedTextField crearCampoFecha() {

        try {

            MaskFormatter mascara =
                    new MaskFormatter("##/##/####");

            mascara.setPlaceholderCharacter('_');

            return new JFormattedTextField(mascara);

        } catch (ParseException e) {

            return new JFormattedTextField();
        }
    }



    private void calcularTotalTurno(int fila) {

    String horaInicio =
            obtenerTextoTabla(tablaTurnos, fila, 1);

    String horaFin =
            obtenerTextoTabla(tablaTurnos, fila, 2);

    if (horaInicio.isEmpty() || horaFin.isEmpty()) {

        tablaTurnos.setValueAt(
                "",
                fila,
                3
        );

        return;
    }

    try {

        int minutosInicio =
                convertirHoraAMinutos(horaInicio);

        int minutosFin =
                convertirHoraAMinutos(horaFin);

        int diferencia =
                minutosFin - minutosInicio;

        /*
         * Si la hora final es menor, se considera
         * que el turno terminó al día siguiente.
         *
         * Ejemplo:
         * 22:00 a 02:00 = 04:00
         */
        if (diferencia < 0) {
            diferencia += 24 * 60;
        }

        tablaTurnos.setValueAt(
                formatearMinutos(diferencia),
                fila,
                3
        );

    } catch (Exception e) {

        tablaTurnos.setValueAt(
                "",
                fila,
                3
        );
    }
}



private int convertirHoraAMinutos(
        String hora
) throws Exception {

    if (!hora.matches(
            "([01]\\d|2[0-3]):[0-5]\\d"
    )) {

        throw new Exception(
                "Hora inválida: " + hora
        );
    }

    String[] partes = hora.split(":");

    int horas =
            Integer.parseInt(partes[0]);

    int minutos =
            Integer.parseInt(partes[1]);

    return horas * 60 + minutos;
}




private String formatearMinutos(
        int totalMinutos
) {

    int horas =
            totalMinutos / 60;

    int minutos =
            totalMinutos % 60;

    return String.format(
            "%02d:%02d",
            horas,
            minutos
    );
}



private String obtenerTextoTabla(
        JTable tabla,
        int fila,
        int columna
) {

    Object valor = tabla.getValueAt(fila, columna);

    if (valor == null) {
        return "";
    }

    String texto = valor.toString().trim();

    if (texto.equals("__:__")) {
        return "";
    }

    return texto;
}

private double convertirTotalADecimal(
        String total
) throws Exception {

    if (total == null || total.isBlank()) {
        return 0.00;
    }

    int minutos =
            convertirHoraAMinutos(total);

    return minutos / 60.0;
}



    private void calcularTotalParalizacion(int fila) {

    String horaInicio =
            obtenerTextoTabla(
                    tablaParalizaciones,
                    fila,
                    2
            );

    String horaFin =
            obtenerTextoTabla(
                    tablaParalizaciones,
                    fila,
                    3
            );

    if (horaInicio.isEmpty() || horaFin.isEmpty()) {

        tablaParalizaciones.setValueAt(
                "",
                fila,
                4
        );

        return;
    }

    try {

        int minutosInicio =
                convertirHoraAMinutos(horaInicio);

        int minutosFin =
                convertirHoraAMinutos(horaFin);

        int diferencia =
                minutosFin - minutosInicio;

        if (diferencia < 0) {
            diferencia += 24 * 60;
        }

        tablaParalizaciones.setValueAt(
                formatearMinutos(diferencia),
                fila,
                4
        );

    } catch (Exception e) {

        tablaParalizaciones.setValueAt(
                "",
                fila,
                4
        );
    }
}


public void cargarGuia(String numeroGuia) {

    try {

        ControlTrabajoVolquetasAPI.GuiaDetalle guia =
                ControlTrabajoVolquetasAPI.obtenerDetalle(
                        "DEVIALTRANSPORT",
                        numeroGuia,
                        "Control Trabajo Volquetas"
                );

        idGuiaEdicion = guia.idGuia();

        txtNumeroGuia.setText(guia.numeroGuia());

        txtFecha.setText(
                guia.fecha() == null
                        ? ""
                        : guia.fecha().format(
                                java.time.format.DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")
                        )
        );

        txtCliente.setText(guia.cliente());
        txtSolicitante.setText(guia.solicitante());
        txtPlaca.setText(guia.placa());
        txtChofer.setText(guia.choferOperador());
        txtSector.setText(guia.sector());
        txtObservaciones.setText(guia.observaciones());
        txtEncargadoObra.setText(guia.encargadoObra());

        for (
                int fila = 0;
                fila < tablaTurnos.getRowCount();
                fila++
        ) {
            tablaTurnos.setValueAt("", fila, 1);
            tablaTurnos.setValueAt("", fila, 2);
            tablaTurnos.setValueAt("", fila, 3);
        }

        if (guia.turnos() != null) {

            for (
                    ControlTrabajoVolquetasAPI.Turno turno
                            : guia.turnos()
            ) {

                int fila = obtenerFilaTurno(
                        turno.turno()
                );

                if (fila < 0) {
                    continue;
                }

                tablaTurnos.setValueAt(
                        turno.horaInicio() == null
                                ? ""
                                : turno.horaInicio(),
                        fila,
                        1
                );

                tablaTurnos.setValueAt(
                        turno.horaFin() == null
                                ? ""
                                : turno.horaFin(),
                        fila,
                        2
                );

                calcularTotalTurno(fila);
            }
        }

        for (
                int fila = 0;
                fila < tablaParalizaciones.getRowCount();
                fila++
        ) {
            tablaParalizaciones.setValueAt("", fila, 2);
            tablaParalizaciones.setValueAt("", fila, 3);
            tablaParalizaciones.setValueAt("", fila, 4);
        }

        if (guia.paralizaciones() != null) {

            for (
                    ControlTrabajoVolquetasAPI.Paralizacion paralizacion
                            : guia.paralizaciones()
            ) {

                int fila = paralizacion.codigo() - 1;

                if (
                        fila < 0
                        || fila >= tablaParalizaciones.getRowCount()
                ) {
                    continue;
                }

                tablaParalizaciones.setValueAt(
                        paralizacion.horaInicio() == null
                                ? ""
                                : paralizacion.horaInicio(),
                        fila,
                        2
                );

                tablaParalizaciones.setValueAt(
                        paralizacion.horaFin() == null
                                ? ""
                                : paralizacion.horaFin(),
                        fila,
                        3
                );

                calcularTotalParalizacion(fila);
            }
        }

        setTitle(
                "Editar Control de Trabajo de Volquetas - "
                        + numeroGuia
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar la guía:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

private int obtenerFilaTurno(String turno) {

    if (turno == null) {
        return -1;
    }

    return switch (turno.trim().toUpperCase()) {
        case "MAÑANA" -> 0;
        case "TARDE" -> 1;
        case "NOCHE" -> 2;
        default -> -1;
    };
}

private void detenerEdicionTablas() {

    if (tablaTurnos.isEditing()) {
        tablaTurnos.getCellEditor().stopCellEditing();
    }

    if (tablaParalizaciones.isEditing()) {
        tablaParalizaciones.getCellEditor().stopCellEditing();
    }
}

private void guardarGuia() {

    detenerEdicionTablas();

    String numeroGuia =
            txtNumeroGuia.getText().trim();

    String fechaTexto =
            txtFecha.getText().trim();

    if (numeroGuia.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el número de guía.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtNumeroGuia.requestFocus();
        return;
    }

    if (
            fechaTexto.isEmpty()
            || fechaTexto.contains("_")
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la fecha.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtFecha.requestFocus();
        return;
    }

    try {

        java.util.List<
                ControlTrabajoVolquetasAPI.Turno
        > turnos = new java.util.ArrayList<>();

        for (
                int fila = 0;
                fila < tablaTurnos.getRowCount();
                fila++
        ) {

            String turno =
                    obtenerTextoTabla(
                            tablaTurnos,
                            fila,
                            0
                    );

            String horaInicio =
                    obtenerTextoTabla(
                            tablaTurnos,
                            fila,
                            1
                    );

            String horaFin =
                    obtenerTextoTabla(
                            tablaTurnos,
                            fila,
                            2
                    );

            String totalHoras =
                    obtenerTextoTabla(
                            tablaTurnos,
                            fila,
                            3
                    );

            boolean inicioVacio =
                    horaInicio.isEmpty();

            boolean finVacio =
                    horaFin.isEmpty();

            if (inicioVacio != finVacio) {

                throw new Exception(
                        "Complete la hora de inicio y la hora de fin del turno "
                                + turno + "."
                );
            }

            if (!inicioVacio) {
                convertirHoraAMinutos(horaInicio);
                convertirHoraAMinutos(horaFin);
            }

            turnos.add(
                    new ControlTrabajoVolquetasAPI.Turno(
                            turno,
                            horaInicio,
                            horaFin,
                            convertirTotalADecimal(totalHoras)
                    )
            );
        }

        java.util.List<
                ControlTrabajoVolquetasAPI.Paralizacion
        > paralizaciones = new java.util.ArrayList<>();

        for (
                int fila = 0;
                fila < tablaParalizaciones.getRowCount();
                fila++
        ) {

            int codigo =
                    Integer.parseInt(
                            obtenerTextoTabla(
                                    tablaParalizaciones,
                                    fila,
                                    0
                            )
                    );

            String descripcion =
                    obtenerTextoTabla(
                            tablaParalizaciones,
                            fila,
                            1
                    );

            String horaInicio =
                    obtenerTextoTabla(
                            tablaParalizaciones,
                            fila,
                            2
                    );

            String horaFin =
                    obtenerTextoTabla(
                            tablaParalizaciones,
                            fila,
                            3
                    );

            String totalHoras =
                    obtenerTextoTabla(
                            tablaParalizaciones,
                            fila,
                            4
                    );

            boolean inicioVacio =
                    horaInicio.isEmpty();

            boolean finVacio =
                    horaFin.isEmpty();

            if (inicioVacio && finVacio) {
                continue;
            }

            if (inicioVacio != finVacio) {

                throw new Exception(
                        "Complete la hora de inicio y la hora de fin "
                                + "de la paralización "
                                + codigo + " - " + descripcion + "."
                );
            }

            convertirHoraAMinutos(horaInicio);
            convertirHoraAMinutos(horaFin);

            paralizaciones.add(
                    new ControlTrabajoVolquetasAPI.Paralizacion(
                            fila + 1,
                            codigo,
                            descripcion,
                            horaInicio,
                            horaFin,
                            convertirTotalADecimal(totalHoras)
                    )
            );
        }

        java.time.LocalDate fecha =
                java.time.LocalDate.parse(
                        fechaTexto,
                        java.time.format.DateTimeFormatter
                                .ofPattern("dd/MM/yyyy")
                );

        ControlTrabajoVolquetasAPI.GuiaGuardar guia =
                new ControlTrabajoVolquetasAPI.GuiaGuardar(
                        idGuiaEdicion,
                        numeroGuia,
                        fecha,
                        txtCliente.getText().trim(),
                        txtSolicitante.getText().trim(),
                        txtPlaca.getText().trim().toUpperCase(),
                        txtChofer.getText().trim(),
                        txtSector.getText().trim(),
                        txtObservaciones.getText().trim(),
                        txtEncargadoObra.getText().trim(),
                        turnos,
                        paralizaciones
                );

        boolean esEdicion =
                idGuiaEdicion != null;

        ControlTrabajoVolquetasAPI.GuiaDetalle resultado =
                ControlTrabajoVolquetasAPI.guardar(
                        guia
                );

        idGuiaEdicion =
                resultado.idGuia();

        JOptionPane.showMessageDialog(
                this,
                esEdicion
                        ? "Guía actualizada correctamente."
                        : "Guía guardada correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

    } catch (
            java.time.format.DateTimeParseException e
    ) {

        JOptionPane.showMessageDialog(
                this,
                "La fecha debe tener el formato dd/MM/yyyy.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtFecha.requestFocus();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al guardar la guía:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

}
