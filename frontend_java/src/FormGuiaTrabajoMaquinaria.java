import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class FormGuiaTrabajoMaquinaria extends JDialog {

    private JTextField txtNumeroGuia;
    private JTextField txtNumeroMaquina;
    private JTextField txtTipoMaquina;
    private JTextField txtCliente;
    private JTextField txtSector;
    private JFormattedTextField txtFecha;
    private JTextField txtOperador;
    private JTextField txtTrabajoRealizar;
    private JFormattedTextField txtHoraInicio;
    private JFormattedTextField txtHoraFin;
    private JCheckBox chkEngrase;

    private JTable tablaTurnos;

    private JTextField txtHorometroInicio;

    private JTextField txtHorometroFin;

    private JTextField txtHorometroRecorrido;

    private JTextField txtCombustible;

    private JTextField txtTotalHoras;

    private JTextArea txtObservaciones;
    private JTextField txtRecibiConforme;
    private Integer idGuiaEdicion = null;

    public FormGuiaTrabajoMaquinaria(Window parent) {

        super(
                parent,
                "Guía Trabajo Diario Maquinaria - EQUIPOS PRO",
                ModalityType.APPLICATION_MODAL
        );

        setSize(1100, 820);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(12, 12));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JLabel titulo =
                new JLabel(
                        "GUÍA DE TRABAJO DIARIO - MAQUINARIA PESADA",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel panelCentro =
                new JPanel(new BorderLayout(10, 10));

        panelCentro.add(
                crearPanelDatosGenerales(),
                BorderLayout.NORTH
        );

        JPanel panelMedio =
                new JPanel(new GridLayout(2, 1, 10, 10));

        panelMedio.add(
                crearPanelOperacion()
        );

        panelMedio.add(
                crearPanelEquipo()
        );

        panelCentro.add(
                panelMedio,
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

    private JPanel crearPanelDatosGenerales() {

        JPanel panel =
                new JPanel(new GridLayout(6, 4, 10, 8));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del trabajo"
                )
        );

        txtNumeroGuia = new JTextField();
        txtNumeroMaquina = new JTextField();
        txtTipoMaquina = new JTextField();
        txtCliente = new JTextField();
        txtSector = new JTextField();
        txtFecha = crearCampoFecha();
        txtOperador = new JTextField();
        txtTrabajoRealizar = new JTextField();
        txtHoraInicio = crearCampoHora();
        txtHoraFin = crearCampoHora();
        chkEngrase = new JCheckBox("Realizado");

        panel.add(new JLabel("N° Guía:"));
        panel.add(txtNumeroGuia);

        panel.add(new JLabel("N° Máquina:"));
        panel.add(txtNumeroMaquina);

        panel.add(new JLabel("Tipo de máquina:"));
        panel.add(txtTipoMaquina);

        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Cliente:"));
        panel.add(txtCliente);

        panel.add(new JLabel("Sector:"));
        panel.add(txtSector);

        panel.add(new JLabel("Operador:"));
        panel.add(txtOperador);

        panel.add(new JLabel("Trabajo a realizar:"));
        panel.add(txtTrabajoRealizar);

        panel.add(new JLabel("Hora inicio:"));
        panel.add(txtHoraInicio);

        panel.add(new JLabel("Hora fin:"));
        panel.add(txtHoraFin);

        panel.add(new JLabel("Chequeo diario de engrase:"));
        panel.add(chkEngrase);

        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel crearPanelOperacion() {

        JPanel panel =
                new JPanel(new BorderLayout(8, 8));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de operación"
                )
        );

        String[] columnas = {
                "Turno",
                "Inicio",
                "Terminación",
                "Total"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {
                        return columna == 1 || columna == 2;
                    }
                };

        modelo.addRow(new Object[]{"MAÑANA", "", "", ""});
        modelo.addRow(new Object[]{"TARDE", "", "", ""});
        modelo.addRow(new Object[]{"NOCHE", "", "", ""});

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
                calcularTotalGeneral();
            }
        });

        panel.add(
                new JScrollPane(tablaTurnos),
                BorderLayout.CENTER
        );

        JPanel panelTotal =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.RIGHT
                    )
            );

        txtTotalHoras =
            new JTextField(8);

        txtTotalHoras.setEditable(false);

        panelTotal.add(
                new JLabel("Total horas de trabajo:")
        );

        panelTotal.add(txtTotalHoras);

        panel.add(
                panelTotal,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel crearPanelEquipo() {

        JPanel panel =
                new JPanel(new GridLayout(4, 2, 10, 8));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del equipo"
                )
        );

        txtHorometroInicio = new JTextField();

        txtHorometroFin = new JTextField();

        txtHorometroRecorrido =
            new JTextField();

        txtHorometroRecorrido.setEditable(false);

        DocumentListener listenerHorometro =
            new DocumentListener() {

                @Override
                public void insertUpdate(
                        DocumentEvent evento
                ) {
                    calcularHorometroRecorrido();
                }

                @Override
                public void removeUpdate(
                        DocumentEvent evento
                ) {
                    calcularHorometroRecorrido();
                }

                @Override
                public void changedUpdate(
                        DocumentEvent evento
                ) {
                    calcularHorometroRecorrido();
                }
            };

        txtHorometroInicio.getDocument()
                .addDocumentListener(listenerHorometro);

        txtHorometroFin.getDocument()
                .addDocumentListener(listenerHorometro);

        txtCombustible = new JTextField();


        panel.add(new JLabel("Horómetro inicio:"));
        panel.add(txtHorometroInicio);

        panel.add(new JLabel("Horómetro fin:"));
        panel.add(txtHorometroFin);

        panel.add(new JLabel("Horómetro recorrido:"));
        panel.add(txtHorometroRecorrido);

        panel.add(new JLabel("Combustible abastecido:"));
        panel.add(txtCombustible);

        return panel;
    }

    private JPanel crearPanelInferior() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        JPanel panelObservaciones =
                new JPanel(new BorderLayout());

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

        JPanel panelRecibe =
                new JPanel(new BorderLayout(10, 10));

        txtRecibiConforme =
                new JTextField();

        panelRecibe.add(
                new JLabel("Recibí conforme:"),
                BorderLayout.WEST
        );

        panelRecibe.add(
                txtRecibiConforme,
                BorderLayout.CENTER
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
                new JPanel(new BorderLayout(8, 8));

        panelCentro.add(
                panelObservaciones,
                BorderLayout.CENTER
        );

        panelCentro.add(
                panelRecibe,
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

    private JFormattedTextField crearCampoHora() {

        try {

            MaskFormatter mascara =
                    new MaskFormatter("##:##");

            mascara.setPlaceholderCharacter('_');

            return new JFormattedTextField(mascara);

        } catch (ParseException e) {

            return new JFormattedTextField();
        }
    }

    private DefaultCellEditor crearEditorHora() {

        return new DefaultCellEditor(
                crearCampoHora()
        );
    }

    private void calcularTotalTurno(int fila) {

        String inicio =
                obtenerTextoTabla(
                        tablaTurnos,
                        fila,
                        1
                );

        String fin =
                obtenerTextoTabla(
                        tablaTurnos,
                        fila,
                        2
                );

        if (inicio.isEmpty() || fin.isEmpty()) {

            tablaTurnos.setValueAt(
                    "",
                    fila,
                    3
            );

            return;
        }

        try {

            int minutosInicio =
                    convertirHoraAMinutos(inicio);

            int minutosFin =
                    convertirHoraAMinutos(fin);

            int diferencia =
                    minutosFin - minutosInicio;

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

    private void calcularTotalGeneral() {

        int minutosTotales = 0;

        for (
                int fila = 0;
                fila < tablaTurnos.getRowCount();
                fila++
        ) {

            String total =
                    obtenerTextoTabla(
                            tablaTurnos,
                            fila,
                            3
                    );

            if (!total.isEmpty()) {

                try {
                    minutosTotales +=
                            convertirHoraAMinutos(total);
                } catch (Exception e) {
                    // Se ignora una fila inválida.
                }
            }
        }

        txtTotalHoras.setText(
                formatearMinutos(minutosTotales)
        );
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

        String[] partes =
                hora.split(":");

        return Integer.parseInt(partes[0]) * 60
                + Integer.parseInt(partes[1]);
    }

    private String formatearMinutos(
            int minutosTotales
    ) {

        int horas =
                minutosTotales / 60;

        int minutos =
                minutosTotales % 60;

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

        Object valor =
                tabla.getValueAt(
                        fila,
                        columna
                );

        if (valor == null) {
            return "";
        }

        String texto =
                valor.toString().trim();

        return texto.equals("__:__")
                ? ""
                : texto;
    }



    private String obtenerHoraCampo(
            JFormattedTextField campo
    ) {

        String texto =
                campo.getText().trim();

        if (
            texto.isEmpty()
            || texto.equals("__:__")
            || texto.contains("_")
        ) {
            return "";
        }

        return texto;
    }


    private double convertirTotalADecimal(
            String total
    ) throws Exception {

        if (
            total == null
            || total.isBlank()
        ) {
            return 0.00;
        }

        int minutos =
                convertirHoraAMinutos(total);

        return minutos / 60.0;
    }

    private void calcularHorometroRecorrido() {

    String textoInicio =
            txtHorometroInicio
                    .getText()
                    .trim()
                    .replace(",", ".");

    String textoFin =
            txtHorometroFin
                    .getText()
                    .trim()
                    .replace(",", ".");

    if (
        textoInicio.isEmpty()
        || textoFin.isEmpty()
    ) {

        txtHorometroRecorrido.setText("");
        return;
    }

    try {

        double horometroInicio =
                Double.parseDouble(textoInicio);

        double horometroFin =
                Double.parseDouble(textoFin);

        double recorrido =
                horometroFin - horometroInicio;

        if (recorrido < 0) {

            txtHorometroRecorrido.setText("");
            return;
        }

        txtHorometroRecorrido.setText(
                String.format(
                        "%.2f",
                        recorrido
                )
        );

    } catch (NumberFormatException e) {

        txtHorometroRecorrido.setText("");
    }
}



private List<GuiaTrabajoMaquinariaDAO.Turno>
        obtenerTurnosParaGuardar() throws Exception {

    List<GuiaTrabajoMaquinariaDAO.Turno> turnos =
            new ArrayList<>();

    for (
            int fila = 0;
            fila < tablaTurnos.getRowCount();
            fila++
    ) {

        String nombreTurno =
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

        String total =
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
                    "Complete la hora de inicio y la hora de fin "
                            + "del turno " + nombreTurno + "."
            );
        }

        if (!inicioVacio) {

            convertirHoraAMinutos(
                    horaInicio
            );

            convertirHoraAMinutos(
                    horaFin
            );
        }

        turnos.add(
                new GuiaTrabajoMaquinariaDAO.Turno(
                        nombreTurno,
                        horaInicio,
                        horaFin,
                        convertirTotalADecimal(total)
                )
        );
    }

    return turnos;
}



public void cargarGuia(String numeroGuia) {

    String sqlCabecera = """
            SELECT
                id_guia,
                numero_guia,
                DATE_FORMAT(fecha, '%d/%m/%Y') AS fecha,
                COALESCE(cliente, '') AS cliente,
                COALESCE(equipo, '') AS tipo_maquina,
                COALESCE(numero_maquina, '') AS numero_maquina,
                COALESCE(chofer_operador, '') AS operador,
                COALESCE(sector, '') AS sector,
                COALESCE(trabajo_realizar, '') AS trabajo_realizar,
                COALESCE(chequeo_engrase, 0) AS chequeo_engrase,
                TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin,
                horometro_inicial,
                horometro_final,
                horometro_recorrido,
                COALESCE(combustible, '') AS combustible,
                COALESCE(recibi_conforme, '') AS recibi_conforme,
                COALESCE(observaciones, '') AS observaciones
            FROM guias
            WHERE tipo_guia = 'Guía Trabajo Diario Maquinaria'
              AND numero_guia = ?
            LIMIT 1
            """;

    String sqlTurnos = """
            SELECT
                turno,
                TIME_FORMAT(hora_inicio, '%H:%i') AS hora_inicio,
                TIME_FORMAT(hora_fin, '%H:%i') AS hora_fin
            FROM trabajo_maquinaria_turnos
            WHERE id_guia = ?
            ORDER BY FIELD(
                turno,
                'MAÑANA',
                'TARDE',
                'NOCHE'
            )
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement psCabecera =
                    conexion.prepareStatement(sqlCabecera)
    ) {

        psCabecera.setString(
                1,
                numeroGuia
        );

        try (
                ResultSet resultado =
                        psCabecera.executeQuery()
        ) {

            if (!resultado.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la guía N° "
                                + numeroGuia + ".",
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

            txtTipoMaquina.setText(
                    resultado.getString("tipo_maquina")
            );

            txtNumeroMaquina.setText(
                    resultado.getString("numero_maquina")
            );

            txtOperador.setText(
                    resultado.getString("operador")
            );

            txtSector.setText(
                    resultado.getString("sector")
            );

            txtTrabajoRealizar.setText(
                    resultado.getString("trabajo_realizar")
            );

            chkEngrase.setSelected(
                    resultado.getBoolean("chequeo_engrase")
            );

            String horaInicio =
                    resultado.getString("hora_inicio");

            String horaFin =
                    resultado.getString("hora_fin");

            txtHoraInicio.setText(
                    horaInicio == null ? "" : horaInicio
            );

            txtHoraFin.setText(
                    horaFin == null ? "" : horaFin
            );

            Object horometroInicial =
                    resultado.getObject("horometro_inicial");

            Object horometroFinal =
                    resultado.getObject("horometro_final");

            Object horometroRecorrido =
                    resultado.getObject("horometro_recorrido");

            txtHorometroInicio.setText(
                    horometroInicial == null
                            ? ""
                            : horometroInicial.toString()
            );

            txtHorometroFin.setText(
                    horometroFinal == null
                            ? ""
                            : horometroFinal.toString()
            );

            txtHorometroRecorrido.setText(
                    horometroRecorrido == null
                            ? ""
                            : horometroRecorrido.toString()
            );

            txtCombustible.setText(
                    resultado.getString("combustible")
            );

            txtRecibiConforme.setText(
                    resultado.getString("recibi_conforme")
            );

            txtObservaciones.setText(
                    resultado.getString("observaciones")
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

            psTurnos.setInt(
                    1,
                    idGuiaEdicion
            );

            try (
                    ResultSet turnos =
                            psTurnos.executeQuery()
            ) {

                while (turnos.next()) {

                    int fila =
                            obtenerFilaTurno(
                                    turnos.getString("turno")
                            );

                    if (fila == -1) {
                        continue;
                    }

                    String inicio =
                            turnos.getString("hora_inicio");

                    String fin =
                            turnos.getString("hora_fin");

                    tablaTurnos.setValueAt(
                            inicio == null ? "" : inicio,
                            fila,
                            1
                    );

                    tablaTurnos.setValueAt(
                            fin == null ? "" : fin,
                            fila,
                            2
                    );

                    calcularTotalTurno(fila);
                }
            }
        }

        calcularTotalGeneral();

        setTitle(
                "Editar Guía Trabajo Diario Maquinaria - "
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


private int obtenerFilaTurno(
        String turno
) {

    if (turno == null) {
        return -1;
    }

    return switch (
            turno.trim().toUpperCase()
    ) {

        case "MAÑANA" -> 0;
        case "TARDE" -> 1;
        case "NOCHE" -> 2;

        default -> -1;
    };
}


private void guardarGuia() {

    String numeroGuia =
            txtNumeroGuia.getText().trim();

    String numeroMaquina =
            txtNumeroMaquina.getText().trim();

    String tipoMaquina =
            txtTipoMaquina.getText().trim();

    String cliente =
            txtCliente.getText().trim();

    String sector =
            txtSector.getText().trim();

    String fecha =
            txtFecha.getText().trim();

    String operador =
            txtOperador.getText().trim();

    String trabajoRealizar =
            txtTrabajoRealizar.getText().trim();

    String horaInicio =
            obtenerHoraCampo(txtHoraInicio);

    String horaFin =
            obtenerHoraCampo(txtHoraFin);

    String horometroInicioTexto =
            txtHorometroInicio
                    .getText()
                    .trim()
                    .replace(",", ".");

    String horometroFinTexto =
            txtHorometroFin
                    .getText()
                    .trim()
                    .replace(",", ".");

    String horometroRecorridoTexto =
            txtHorometroRecorrido
                    .getText()
                    .trim()
                    .replace(",", ".");

    String combustible =
            txtCombustible.getText().trim();

    String observaciones =
            txtObservaciones.getText().trim();

    String recibiConforme =
            txtRecibiConforme.getText().trim();

    String totalHoras =
            txtTotalHoras.getText().trim();

    /*
     * VALIDACIONES BÁSICAS
     */

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

    if (numeroMaquina.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el número de máquina.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtNumeroMaquina.requestFocus();
        return;
    }

    if (tipoMaquina.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el tipo de máquina.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtTipoMaquina.requestFocus();
        return;
    }

    if (fecha.contains("_")) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese una fecha completa.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtFecha.requestFocus();
        return;
    }

    /*
     * Si se ingresa una hora general,
     * deben ingresarse ambas.
     */
    if (
        horaInicio.isEmpty()
        != horaFin.isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Complete la hora de inicio y la hora de fin.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (!horaInicio.isEmpty()) {

        try {

            convertirHoraAMinutos(horaInicio);
            convertirHoraAMinutos(horaFin);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La hora debe tener formato HH:mm.\n"
                            + "Ejemplo: 07:30",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }
    }

    Double horometroInicio = null;
    Double horometroFin = null;
    Double horometroRecorrido = null;

    try {

        if (!horometroInicioTexto.isEmpty()) {

            horometroInicio =
                    Double.parseDouble(
                            horometroInicioTexto
                    );
        }

        if (!horometroFinTexto.isEmpty()) {

            horometroFin =
                    Double.parseDouble(
                            horometroFinTexto
                    );
        }

        if (!horometroRecorridoTexto.isEmpty()) {

            horometroRecorrido =
                    Double.parseDouble(
                            horometroRecorridoTexto
                    );
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "Los horómetros deben contener solamente números.\n"
                        + "Ejemplo: 1528.50",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    try {

    List<GuiaTrabajoMaquinariaDAO.Turno> turnos =
            obtenerTurnosParaGuardar();


    
    
    if (idGuiaEdicion == null) {

    GuiaTrabajoMaquinariaDAO.guardarNuevaGuia(

            numeroGuia,
            fecha,
            cliente,
            tipoMaquina,
            numeroMaquina,
            operador,
            convertirTotalADecimal(totalHoras),
            sector,
            trabajoRealizar,
            chkEngrase.isSelected(),
            horaInicio,
            horaFin,
            horometroInicio,
            horometroFin,
            horometroRecorrido,
            combustible,
            recibiConforme,
            observaciones,
            turnos
    );

} else {

    GuiaTrabajoMaquinariaDAO.actualizarGuia(

            idGuiaEdicion,

            numeroGuia,
            fecha,
            cliente,
            tipoMaquina,
            numeroMaquina,
            operador,
            convertirTotalADecimal(totalHoras),
            sector,
            trabajoRealizar,
            chkEngrase.isSelected(),
            horaInicio,
            horaFin,
            horometroInicio,
            horometroFin,
            horometroRecorrido,
            combustible,
            recibiConforme,
            observaciones,
            turnos
    );
}

    
    String mensaje =
        idGuiaEdicion == null
                ? "Guía de trabajo de maquinaria guardada correctamente."
                : "Guía de trabajo de maquinaria actualizada correctamente.";

JOptionPane.showMessageDialog(
        this,
        mensaje,
        "LPP Smart ERP",
        JOptionPane.INFORMATION_MESSAGE
);

    dispose();

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