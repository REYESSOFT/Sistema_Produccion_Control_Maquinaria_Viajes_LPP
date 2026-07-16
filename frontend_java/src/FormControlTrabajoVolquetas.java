import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    String sqlCabecera = """
            SELECT
                g.id_guia,
                g.numero_guia,
                DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                COALESCE(g.cliente, '') AS cliente,
                COALESCE(g.placa, '') AS placa,
                COALESCE(g.chofer_operador, '') AS chofer,
                COALESCE(g.sector, '') AS sector,
                COALESCE(g.observaciones, '') AS observaciones,
                COALESCE(g.encargado_obra, '') AS encargado_obra

            FROM guias g
            WHERE g.id_empresa = 2
              AND g.tipo_guia = 'Control Trabajo Volquetas'
              AND g.numero_guia = ?
            LIMIT 1
            """;
            

    String sqlTurnos = """
            SELECT
                turno,
                TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin
            FROM control_trabajo_turnos
            WHERE id_guia = ?
            """;

    String sqlParalizaciones = """
            SELECT
                codigo,
                TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin
            FROM control_trabajo_paralizaciones
            WHERE id_guia = ?
            ORDER BY numero_fila
            """;

    try (
            Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement psCabecera =
                    conexion.prepareStatement(sqlCabecera)
    ) {

        psCabecera.setString(1, numeroGuia);

        try (
                ResultSet resultado =
                        psCabecera.executeQuery()
        ) {

            if (!resultado.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la guía N° " + numeroGuia + ".",
                        "Información",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            idGuiaEdicion =
                    resultado.getInt("id_guia");

            txtNumeroGuia.setText(
                    resultado.getString("numero_guia")
            );

            txtFecha.setText(
                    resultado.getString("fecha")
            );

            txtCliente.setText(
                    resultado.getString("cliente")
            );

            txtPlaca.setText(
                    resultado.getString("placa")
            );

            txtChofer.setText(
                    resultado.getString("chofer")
            );

            txtSector.setText(
                    resultado.getString("sector")
            );

            txtObservaciones.setText(
                    resultado.getString("observaciones")
            );

            txtEncargadoObra.setText(
                    resultado.getString("encargado_obra")
            );
        }

        // Limpiar los turnos antes de cargarlos.
        for (
                int fila = 0;
                fila < tablaTurnos.getRowCount();
                fila++
        ) {

            tablaTurnos.setValueAt("", fila, 1);
            tablaTurnos.setValueAt("", fila, 2);
            tablaTurnos.setValueAt("", fila, 3);
        }

        try (
                PreparedStatement psTurnos =
                        conexion.prepareStatement(sqlTurnos)
        ) {

            psTurnos.setInt(1, idGuiaEdicion);

            try (
                    ResultSet turnos =
                            psTurnos.executeQuery()
            ) {

                while (turnos.next()) {

                    String turno =
                            turnos.getString("turno");

                    int filaTurno =
                            obtenerFilaTurno(turno);

                    if (filaTurno >= 0) {

                        String horaInicio =
                                turnos.getString("hora_inicio");

                        String horaFin =
                                turnos.getString("hora_fin");

                        tablaTurnos.setValueAt(
                                horaInicio == null ? "" : horaInicio,
                                filaTurno,
                                1
                        );

                        tablaTurnos.setValueAt(
                                horaFin == null ? "" : horaFin,
                                filaTurno,
                                2
                        );

                        calcularTotalTurno(filaTurno);
                    }
                }
            }
        }

        // Limpiar las paralizaciones antes de cargarlas.
        for (
                int fila = 0;
                fila < tablaParalizaciones.getRowCount();
                fila++
        ) {

            tablaParalizaciones.setValueAt("", fila, 2);
            tablaParalizaciones.setValueAt("", fila, 3);
            tablaParalizaciones.setValueAt("", fila, 4);
        }

        try (
                PreparedStatement psParalizaciones =
                        conexion.prepareStatement(
                                sqlParalizaciones
                        )
        ) {

            psParalizaciones.setInt(
                    1,
                    idGuiaEdicion
            );

            try (
                    ResultSet paralizaciones =
                            psParalizaciones.executeQuery()
            ) {

                while (paralizaciones.next()) {

                    int codigo =
                            paralizaciones.getInt("codigo");

                    int fila =
                            codigo - 1;

                    if (
                        fila >= 0
                        && fila < tablaParalizaciones.getRowCount()
                    ) {

                        String horaInicio =
                                paralizaciones.getString(
                                        "hora_inicio"
                                );

                        String horaFin =
                                paralizaciones.getString(
                                        "hora_fin"
                                );

                        tablaParalizaciones.setValueAt(
                                horaInicio == null ? "" : horaInicio,
                                fila,
                                2
                        );

                        tablaParalizaciones.setValueAt(
                                horaFin == null ? "" : horaFin,
                                fila,
                                3
                        );

                        calcularTotalParalizacion(fila);
                    }
                }
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

    return switch (turno.toUpperCase()) {

        case "MAÑANA" -> 0;
        case "TARDE" -> 1;
        case "NOCHE" -> 2;

        default -> -1;
    };
}




private void guardarTurnos(
        Connection conexion,
        int idGuia
) throws Exception {

    String sql = """
            INSERT INTO control_trabajo_turnos (
                id_guia,
                turno,
                hora_inicio,
                hora_fin,
                total_horas
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    try (
            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

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

            ps.setInt(
                    1,
                    idGuia
            );

            ps.setString(
                    2,
                    turno
            );

            if (horaInicio.isEmpty()) {

                ps.setNull(
                        3,
                        java.sql.Types.TIME
                );

            } else {

                ps.setTime(
                        3,
                        java.sql.Time.valueOf(
                                horaInicio + ":00"
                        )
                );
            }

            if (horaFin.isEmpty()) {

                ps.setNull(
                        4,
                        java.sql.Types.TIME
                );

            } else {

                ps.setTime(
                        4,
                        java.sql.Time.valueOf(
                                horaFin + ":00"
                        )
                );
            }

            ps.setDouble(
                    5,
                    convertirTotalADecimal(
                            totalHoras
                    )
            );

            ps.addBatch();
        }

        ps.executeBatch();
    }
}




private void guardarParalizaciones(
        Connection conexion,
        int idGuia
) throws Exception {

    String sql = """
            INSERT INTO control_trabajo_paralizaciones (
                id_guia,
                numero_fila,
                codigo,
                hora_inicio,
                hora_fin,
                total_horas
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    try (
            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        for (
                int fila = 0;
                fila < tablaParalizaciones.getRowCount();
                fila++
        ) {

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

            if (
                horaInicio.isEmpty()
                && horaFin.isEmpty()
            ) {
                continue;
            }

            int codigo =
                    Integer.parseInt(
                            obtenerTextoTabla(
                                    tablaParalizaciones,
                                    fila,
                                    0
                            )
                    );

            String totalHoras =
                    obtenerTextoTabla(
                            tablaParalizaciones,
                            fila,
                            4
                    );

            ps.setInt(
                    1,
                    idGuia
            );

            ps.setInt(
                    2,
                    fila + 1
            );

            ps.setInt(
                    3,
                    codigo
            );

            ps.setTime(
                    4,
                    java.sql.Time.valueOf(
                            horaInicio + ":00"
                    )
            );

            ps.setTime(
                    5,
                    java.sql.Time.valueOf(
                            horaFin + ":00"
                    )
            );

            ps.setDouble(
                    6,
                    convertirTotalADecimal(
                            totalHoras
                    )
            );

            ps.addBatch();
        }

        ps.executeBatch();
    }
}


    private void guardarGuia() {

    if (txtNumeroGuia.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el número de guía."
        );

        txtNumeroGuia.requestFocus();
        return;
    }

    if (txtFecha.getText().contains("_")) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la fecha."
        );

        txtFecha.requestFocus();
        return;
    }




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

    boolean inicioVacio =
            horaInicio.isEmpty();

    boolean finVacio =
            horaFin.isEmpty();

    if (inicioVacio != finVacio) {

        JOptionPane.showMessageDialog(
                this,
                "Complete la hora de inicio y la hora de fin del turno "
                        + turno + ".",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (!inicioVacio) {

        try {

            convertirHoraAMinutos(horaInicio);
            convertirHoraAMinutos(horaFin);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Las horas del turno "
                            + turno
                            + " deben tener formato HH:mm.\n"
                            + "Ejemplo: 07:30",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }
    }

}




    /* ===== VALIDAR PARALIZACIONES ===== */

for (
        int fila = 0;
        fila < tablaParalizaciones.getRowCount();
        fila++
) {

    String codigo =
            obtenerTextoTabla(
                    tablaParalizaciones,
                    fila,
                    0
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

    boolean inicioVacio =
            horaInicio.isEmpty();

    boolean finVacio =
            horaFin.isEmpty();

    // Si ambas horas están vacías, la fila no se utilizará.
    if (inicioVacio && finVacio) {
        continue;
    }

    if (inicioVacio != finVacio) {

        JOptionPane.showMessageDialog(
                this,
                "Complete la hora de inicio y la hora de fin "
                        + "de la paralización "
                        + codigo + " - " + descripcion + ".",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    try {

        convertirHoraAMinutos(horaInicio);
        convertirHoraAMinutos(horaFin);

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Las horas de la paralización "
                        + codigo + " - " + descripcion
                        + " deben tener formato HH:mm.\n"
                        + "Ejemplo: 10:15",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }
}

/* ===== FIN VALIDAR PARALIZACIONES ===== */

    Connection conexion = null;

try {

    conexion = ConexionDB.obtenerConexion();
    conexion.setAutoCommit(false);

    int idGuia;

    if (idGuiaEdicion == null) {

        String sqlInsertarGuia = """
                INSERT INTO guias (
                    id_empresa,
                    tipo_guia,
                    numero_guia,
                    fecha,
                    cliente,
                    placa,
                    chofer_operador,
                    sector,
                    observaciones,
                    encargado_obra,
                    estado
                )
                VALUES (
                    2,
                    'Control Trabajo Volquetas',
                    ?,
                    STR_TO_DATE(?, '%d/%m/%Y'),
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    'PENDIENTE'
                )
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sqlInsertarGuia,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    txtNumeroGuia.getText().trim()
            );

            ps.setString(
                    2,
                    txtFecha.getText().trim()
            );

            ps.setString(
                    3,
                    txtCliente.getText().trim()
            );

            ps.setString(
                    4,
                    txtPlaca.getText().trim()
            );

            ps.setString(
                    5,
                    txtChofer.getText().trim()
            );

            ps.setString(
                    6,
                    txtSector.getText().trim()
            );

            ps.setString(
                    7,
                    txtObservaciones.getText().trim()
            );


            ps.setString(
                        8,
                        txtEncargadoObra.getText().trim()
            );

            ps.executeUpdate();

            try (
                    ResultSet claves =
                            ps.getGeneratedKeys()
            ) {

                if (!claves.next()) {

                    throw new Exception(
                            "No fue posible obtener el ID de la guía."
                    );
                }

                idGuia =
                        claves.getInt(1);
            }
        }

    } else {

        idGuia =
                idGuiaEdicion;

        String sqlActualizarGuia = """
                UPDATE guias
                SET
                    numero_guia = ?,
                    fecha = STR_TO_DATE(?, '%d/%m/%Y'),
                    cliente = ?,
                    placa = ?,
                    chofer_operador = ?,
                    sector = ?,
                    observaciones = ?,
                    encargado_obra = ?
                WHERE id_guia = ?
                  AND estado = 'PENDIENTE'
                """;

        try (
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sqlActualizarGuia
                        )
        ) {

            ps.setString(
                    1,
                    txtNumeroGuia.getText().trim()
            );

            ps.setString(
                    2,
                    txtFecha.getText().trim()
            );

            ps.setString(
                    3,
                    txtCliente.getText().trim()
            );

            ps.setString(
                    4,
                    txtPlaca.getText().trim()
            );

            ps.setString(
                    5,
                    txtChofer.getText().trim()
            );

            ps.setString(
                    6,
                    txtSector.getText().trim()
            );

            ps.setString(
                    7,
                    txtObservaciones.getText().trim()
            );

            ps.setString(
                    8,
                    txtEncargadoObra.getText().trim()
            );


            ps.setInt(
                9,
                idGuia
            );

            int filasActualizadas =
                    ps.executeUpdate();

            if (filasActualizadas == 0) {

                throw new Exception(
                        "La guía no pudo actualizarse. "
                                + "Puede estar aprobada."
                );
            }
        }

        // Eliminar los datos anteriores para reemplazarlos.
        try (
                PreparedStatement psEliminarTurnos =
                        conexion.prepareStatement(
                                """
                                DELETE FROM control_trabajo_turnos
                                WHERE id_guia = ?
                                """
                        );

                PreparedStatement psEliminarParalizaciones =
                        conexion.prepareStatement(
                                """
                                DELETE FROM control_trabajo_paralizaciones
                                WHERE id_guia = ?
                                """
                        )
        ) {

            psEliminarTurnos.setInt(
                    1,
                    idGuia
            );

            psEliminarTurnos.executeUpdate();

            psEliminarParalizaciones.setInt(
                    1,
                    idGuia
            );

            psEliminarParalizaciones.executeUpdate();
        }
    }

    guardarTurnos(
            conexion,
            idGuia
    );

    guardarParalizaciones(
            conexion,
            idGuia
    );

    conexion.commit();

    String mensaje =
            idGuiaEdicion == null
                    ? "Guía guardada correctamente."
                    : "Guía actualizada correctamente.";

    JOptionPane.showMessageDialog(
            this,
            mensaje,
            "LPP Smart ERP",
            JOptionPane.INFORMATION_MESSAGE
    );

    dispose();

} catch (Exception e) {

    if (conexion != null) {

        try {
            conexion.rollback();
        } catch (Exception rollbackError) {
            rollbackError.printStackTrace();
        }
    }

    JOptionPane.showMessageDialog(
            this,
            "Error al guardar la guía:\n"
                    + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
    );

    e.printStackTrace();

} finally {

    if (conexion != null) {

        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }
}

}
