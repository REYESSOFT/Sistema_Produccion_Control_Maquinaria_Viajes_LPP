import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FormGuiaDespachoMaterial extends JDialog {

    private JTextField txtNumeroGuia;
    private JFormattedTextField txtFecha;
    private JTextField txtChofer;
    private JTextField txtSolicitante;
    private JTextField txtSector;
    private JTextField txtPlaca;
    private JTextField txtCubicaje;

    private final Map<String, JCheckBox> opcionesOrigen =
        new LinkedHashMap<>();

    private JCheckBox chkOrigenOtros;
    private JTextField txtOtroOrigen;

    private JTextField txtDestino;
    private JFormattedTextField txtHoraEntrada;
    private JFormattedTextField txtHoraSalida;

    private final Map<String, JCheckBox> opcionesMaterial =
        new LinkedHashMap<>();

    private JCheckBox chkMaterialOtros;
    private JTextField txtOtroMaterial;

    private JTextArea txtObservaciones;
    private JTextField txtRecibiConforme;

    private Integer idGuiaEdicion = null;

    public FormGuiaDespachoMaterial(Window parent) {

        super(
                parent,
                "Guía de Despacho de Material - DEVIALTRANSPORT",
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
                        "GUÍA DE DESPACHO DE MATERIAL",
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
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );

        panelMedio.add(
                crearPanelOrigenDestino()
        );

        panelMedio.add(
                crearPanelMaterial()
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
                new JPanel(
                        new GridLayout(
                                4,
                                4,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos generales"
                )
        );

        txtNumeroGuia = new JTextField();
        txtFecha = crearCampoFecha();

        txtChofer = new JTextField();
        txtSolicitante = new JTextField();

        txtSector = new JTextField();
        txtPlaca = new JTextField();

        txtCubicaje = new JTextField();

        panel.add(new JLabel("N° Guía:"));
        panel.add(txtNumeroGuia);

        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Chofer:"));
        panel.add(txtChofer);

        panel.add(new JLabel("Solicitante:"));
        panel.add(txtSolicitante);

        panel.add(new JLabel("Sector:"));
        panel.add(txtSector);

        panel.add(new JLabel("Placa:"));
        panel.add(txtPlaca);

        panel.add(new JLabel("Cubicaje:"));
        panel.add(txtCubicaje);

        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JPanel crearPanelOrigenDestino() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Origen y destino"
                )
        );

        JPanel panelOrigen =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                8,
                                8
                        )
                );

        panelOrigen.setBorder(
                BorderFactory.createTitledBorder(
                        "Lugar de origen"
                )
        );

        
        agregarOpcionOrigen(
                panelOrigen,
                "Martínez"
        );

        agregarOpcionOrigen(
                panelOrigen,
                "Monte Azul"
        );

        agregarOpcionOrigen(
                panelOrigen,
                "Suárez"
        );

        agregarOpcionOrigen(
                panelOrigen,
                "Urgiles"
        );

        agregarOpcionOrigen(
                panelOrigen,
                "Marlon"
        );

        agregarOpcionOrigen(
                panelOrigen,
                "Neicer"
        );

        chkOrigenOtros =
                new JCheckBox("Otros");

        txtOtroOrigen =
                new JTextField();

        txtOtroOrigen.setEnabled(false);


        panelOrigen.add(chkOrigenOtros);
        panelOrigen.add(txtOtroOrigen);

        chkOrigenOtros.addActionListener(
                e -> actualizarCampoOtroOrigen()
        );

        JPanel panelDestino =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                8
                        )
                );

        panelDestino.setBorder(
                BorderFactory.createTitledBorder(
                        "Destino y horario"
                )
        );

        txtDestino =
                new JTextField();

        txtHoraEntrada =
                crearCampoHora();

        txtHoraSalida =
                crearCampoHora();

        panelDestino.add(
                new JLabel("Lugar de destino:")
        );

        panelDestino.add(txtDestino);

        panelDestino.add(
                new JLabel("Hora de entrada:")
        );

        panelDestino.add(txtHoraEntrada);

        panelDestino.add(
                new JLabel("Hora de salida:")
        );

        panelDestino.add(txtHoraSalida);

        panel.add(
                panelOrigen,
                BorderLayout.CENTER
        );

        panel.add(
                panelDestino,
                BorderLayout.EAST
        );

        return panel;
    }

    private JPanel crearPanelMaterial() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                7,
                                2,
                                8,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Tipo de material"
                )
        );

        agregarOpcionMaterial(
                panel,
                "Piedra 3/4"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra escollera"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra 3/8"
        );

        agregarOpcionMaterial(
                panel,
                "Cisco"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra 1/2"
        );

        agregarOpcionMaterial(
                panel,
                "Subbase"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra 4"
        );

        agregarOpcionMaterial(
                panel,
                "Base clase 1"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra 7/8"
        );

        agregarOpcionMaterial(
                panel,
                "Cascajo azul"
        );

        agregarOpcionMaterial(
                panel,
                "Piedra base"
        );

        agregarOpcionMaterial(
                panel,
                "Cascajo amarillo"
        );

        chkMaterialOtros =
                new JCheckBox("Otros");

        txtOtroMaterial =
                new JTextField();

        txtOtroMaterial.setEnabled(false);

        panel.add(chkMaterialOtros);
        panel.add(txtOtroMaterial);

        chkMaterialOtros.addActionListener(
                e -> actualizarCampoOtroMaterial()
        );

        return panel;
    }

    private JPanel crearPanelInferior() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JPanel panelObservaciones =
                new JPanel(
                        new BorderLayout()
                );

        panelObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        txtObservaciones =
                new JTextArea(
                        3,
                        30
                );

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        panelObservaciones.add(
                new JScrollPane(
                        txtObservaciones
                ),
                BorderLayout.CENTER
        );

        JPanel panelRecibe =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

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
                new JPanel(
                        new BorderLayout(
                                8,
                                8
                        )
                );

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

    private void agregarOpcionOrigen(
        JPanel panel,
        String nombre
) {

    JCheckBox opcion =
            new JCheckBox(nombre);

    opcionesOrigen.put(
            nombre,
            opcion
    );

    panel.add(opcion);
    panel.add(new JLabel());

    opcion.addActionListener(
            e -> actualizarCampoOtroOrigen()
    );
}

    private void agregarOpcionMaterial(
        JPanel panel,
        String nombre
) {

    JCheckBox opcion =
            new JCheckBox(nombre);

    opcionesMaterial.put(
            nombre,
            opcion
    );

    panel.add(opcion);
}

    private void actualizarCampoOtroOrigen() {

    boolean seleccionado =
            chkOrigenOtros.isSelected();

        txtOtroOrigen.setEnabled(
                seleccionado
        );

        if (!seleccionado) {
            txtOtroOrigen.setText("");
        }
    }

    private void actualizarCampoOtroMaterial() {

    boolean seleccionado =
            chkMaterialOtros.isSelected();

        txtOtroMaterial.setEnabled(
                seleccionado
        );

        if (!seleccionado) {
            txtOtroMaterial.setText("");
        }
    }

    private JFormattedTextField crearCampoFecha() {

        try {

            MaskFormatter mascara =
                    new MaskFormatter(
                            "##/##/####"
                    );

            mascara.setPlaceholderCharacter('_');

            return new JFormattedTextField(
                    mascara
            );

        } catch (ParseException e) {

            return new JFormattedTextField();
        }
    }

    private JFormattedTextField crearCampoHora() {

        try {

            MaskFormatter mascara =
                    new MaskFormatter(
                            "##:##"
                    );

            mascara.setPlaceholderCharacter('_');

            return new JFormattedTextField(
                    mascara
            );

        } catch (ParseException e) {

            return new JFormattedTextField();
        }
    }

    private String obtenerOrigenesSeleccionados() {

    List<String> seleccionados =
            new ArrayList<>();

    for (
            Map.Entry<String, JCheckBox> entrada
                    : opcionesOrigen.entrySet()
    ) {

        if (entrada.getValue().isSelected()) {

            seleccionados.add(
                    entrada.getKey()
            );
        }
    }

    if (chkOrigenOtros.isSelected()) {

        String otroOrigen =
                txtOtroOrigen.getText().trim();

        if (!otroOrigen.isEmpty()) {

            seleccionados.add(
                    "Otros: " + otroOrigen
            );

        } else {

            seleccionados.add("Otros");
        }
    }

    return String.join(
            ", ",
            seleccionados
    );
}

private String obtenerMaterialesSeleccionados() {

    List<String> seleccionados =
            new ArrayList<>();

    for (
            Map.Entry<String, JCheckBox> entrada
                    : opcionesMaterial.entrySet()
    ) {

        if (entrada.getValue().isSelected()) {

            seleccionados.add(
                    entrada.getKey()
            );
        }
    }

    if (chkMaterialOtros.isSelected()) {

        String otroMaterial =
                txtOtroMaterial.getText().trim();

        if (!otroMaterial.isEmpty()) {

            seleccionados.add(
                    "Otros: " + otroMaterial
            );

        } else {

            seleccionados.add("Otros");
        }
    }

    return String.join(
            ", ",
            seleccionados
    );
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

private boolean horaValida(
        String hora
) {

    return hora.matches(
            "([01]\\d|2[0-3]):[0-5]\\d"
    );
}



public void cargarGuia(String numeroGuia) {

    String sql = """
            SELECT
                id_guia,
                numero_guia,
                DATE_FORMAT(fecha, '%d/%m/%Y') AS fecha,
                COALESCE(solicitante, '') AS solicitante,
                COALESCE(placa, '') AS placa,
                COALESCE(chofer_operador, '') AS chofer,
                COALESCE(m3, 0) AS cubicaje,
                COALESCE(material, '') AS material,
                COALESCE(sector, '') AS sector,
                COALESCE(origen, '') AS origen,
                COALESCE(destino, '') AS destino,
                TIME_FORMAT(hora_inicio, '%H:%i') AS hora_entrada,
                TIME_FORMAT(hora_fin, '%H:%i') AS hora_salida,
                COALESCE(recibi_conforme, '') AS recibi_conforme,
                COALESCE(observaciones, '') AS observaciones
            FROM guias
            WHERE tipo_guia = 'Guía Despacho de Material'
              AND numero_guia = ?
            LIMIT 1
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setString(1, numeroGuia);

        try (
                java.sql.ResultSet resultado =
                        ps.executeQuery()
        ) {

            if (!resultado.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la guía.",
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

            txtSolicitante.setText(
                    resultado.getString("solicitante")
            );

            txtPlaca.setText(
                    resultado.getString("placa")
            );

            txtChofer.setText(
                    resultado.getString("chofer")
            );

            txtCubicaje.setText(
                    resultado.getString("cubicaje")
            );

            txtSector.setText(
                    resultado.getString("sector")
            );

            txtDestino.setText(
                    resultado.getString("destino")
            );

            String horaEntrada =
                    resultado.getString("hora_entrada");

            String horaSalida =
                    resultado.getString("hora_salida");

            txtHoraEntrada.setText(
                    horaEntrada == null ? "" : horaEntrada
            );

            txtHoraSalida.setText(
                    horaSalida == null ? "" : horaSalida
            );

            txtRecibiConforme.setText(
                    resultado.getString("recibi_conforme")
            );

            txtObservaciones.setText(
                    resultado.getString("observaciones")
            );

            cargarOrigenesSeleccionados(
                    resultado.getString("origen")
            );

            cargarMaterialesSeleccionados(
                    resultado.getString("material")
            );
        }

        setTitle(
                "Editar Guía Despacho de Material - "
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

private void cargarOrigenesSeleccionados(
        String texto
) {

    for (JCheckBox opcion : opcionesOrigen.values()) {
        opcion.setSelected(false);
    }

    chkOrigenOtros.setSelected(false);
    txtOtroOrigen.setText("");
    txtOtroOrigen.setEnabled(false);

    if (texto == null || texto.isBlank()) {
        return;
    }

    String[] valores = texto.split(",");

    for (String valor : valores) {

        String limpio =
                valor.trim();

        JCheckBox opcion =
                opcionesOrigen.get(limpio);

        if (opcion != null) {

            opcion.setSelected(true);

        } else if (
                limpio.startsWith("Otros:")
        ) {

            chkOrigenOtros.setSelected(true);
            txtOtroOrigen.setEnabled(true);
            txtOtroOrigen.setText(
                    limpio.substring(6).trim()
            );
        }
    }
}

private void cargarMaterialesSeleccionados(
        String texto
) {

    for (JCheckBox opcion : opcionesMaterial.values()) {
        opcion.setSelected(false);
    }

    chkMaterialOtros.setSelected(false);
    txtOtroMaterial.setText("");
    txtOtroMaterial.setEnabled(false);

    if (texto == null || texto.isBlank()) {
        return;
    }

    String[] valores = texto.split(",");

    for (String valor : valores) {

        String limpio =
                valor.trim();

        JCheckBox opcion =
                opcionesMaterial.get(limpio);

        if (opcion != null) {

            opcion.setSelected(true);

        } else if (
                limpio.startsWith("Otros:")
        ) {

            chkMaterialOtros.setSelected(true);
            txtOtroMaterial.setEnabled(true);
            txtOtroMaterial.setText(
                    limpio.substring(6).trim()
            );
        }
    }
}




private void guardarGuia() {

    String numeroGuia =
            txtNumeroGuia.getText().trim();

    String fecha =
            txtFecha.getText().trim();

    String chofer =
            txtChofer.getText().trim();

    String solicitante =
            txtSolicitante.getText().trim();

    String sector =
            txtSector.getText().trim();

    String placa =
            txtPlaca.getText().trim();

    String cubicajeTexto =
            txtCubicaje.getText()
                    .trim()
                    .replace(",", ".");

    String origen =
            obtenerOrigenesSeleccionados();

    String destino =
            txtDestino.getText().trim();

    String horaEntrada =
            obtenerHoraCampo(
                    txtHoraEntrada
            );

    String horaSalida =
            obtenerHoraCampo(
                    txtHoraSalida
            );

    String material =
            obtenerMaterialesSeleccionados();

    String observaciones =
            txtObservaciones.getText().trim();

    String recibiConforme =
            txtRecibiConforme.getText().trim();



    if (
        chkOrigenOtros.isSelected()
        && txtOtroOrigen.getText().trim().isEmpty()
) {

    JOptionPane.showMessageDialog(
            this,
            "Especifique el otro lugar de origen.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
    );

    txtOtroOrigen.requestFocus();
    return;
}

if (
        chkMaterialOtros.isSelected()
        && txtOtroMaterial.getText().trim().isEmpty()
) {

    JOptionPane.showMessageDialog(
            this,
            "Especifique el otro tipo de material.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
    );

    txtOtroMaterial.requestFocus();
    return;
}


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

    if (chofer.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el chofer.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtChofer.requestFocus();
        return;
    }

    if (placa.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la placa.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtPlaca.requestFocus();
        return;
    }

    if (origen.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione al menos un lugar de origen.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (destino.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el lugar de destino.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtDestino.requestFocus();
        return;
    }

    if (material.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione al menos un tipo de material.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (
        horaEntrada.isEmpty()
        != horaSalida.isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Complete la hora de entrada y la hora de salida.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (
        !horaEntrada.isEmpty()
        && (
            !horaValida(horaEntrada)
            || !horaValida(horaSalida)
        )
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Las horas deben tener formato HH:mm.\n"
                        + "Ejemplo: 07:30",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    double cubicaje;

    try {

        cubicaje =
                cubicajeTexto.isEmpty()
                        ? 0.00
                        : Double.parseDouble(
                                cubicajeTexto
                        );

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "El cubicaje debe contener solamente números.\n"
                        + "Ejemplo: 12.50",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtCubicaje.requestFocus();
        return;
    }





    String sql;

if (idGuiaEdicion == null) {

    sql = """
            INSERT INTO guias (
                id_empresa,
                tipo_guia,
                numero_guia,
                fecha,
                solicitante,
                placa,
                chofer_operador,
                m3,
                material,
                sector,
                origen,
                destino,
                hora_inicio,
                hora_fin,
                recibi_conforme,
                observaciones,
                estado
            )
            VALUES (
                (
                    SELECT id_empresa
                    FROM empresas
                    WHERE nombre_empresa = 'DEVIALTRANSPORT'
                    LIMIT 1
                ),
                'Guía Despacho de Material',
                ?,
                STR_TO_DATE(?, '%d/%m/%Y'),
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                'PENDIENTE'
            )
            """;

} else {

    sql = """
            UPDATE guias
            SET
                numero_guia = ?,
                fecha = STR_TO_DATE(?, '%d/%m/%Y'),
                solicitante = ?,
                placa = ?,
                chofer_operador = ?,
                m3 = ?,
                material = ?,
                sector = ?,
                origen = ?,
                destino = ?,
                hora_inicio = ?,
                hora_fin = ?,
                recibi_conforme = ?,
                observaciones = ?
            WHERE id_guia = ?
              AND tipo_guia = 'Guía Despacho de Material'
              AND estado = 'PENDIENTE'
            """;
}

    


    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
    ) {

        ps.setString(1, numeroGuia);
        ps.setString(2, fecha);
        ps.setString(3, solicitante);
        ps.setString(4, placa);
        ps.setString(5, chofer);
        ps.setDouble(6, cubicaje);
        ps.setString(7, material);
        ps.setString(8, sector);
        ps.setString(9, origen);
        ps.setString(10, destino);

        if (horaEntrada.isEmpty()) {

            ps.setNull(
                    11,
                    Types.TIME
            );

        } else {

            ps.setTime(
                    11,
                    java.sql.Time.valueOf(
                            horaEntrada + ":00"
                    )
            );
        }

        if (horaSalida.isEmpty()) {

            ps.setNull(
                    12,
                    Types.TIME
            );

        } else {

            ps.setTime(
                    12,
                    java.sql.Time.valueOf(
                            horaSalida + ":00"
                    )
            );
        }

        ps.setString(
                13,
                recibiConforme
        );

        ps.setString(
                14,
                observaciones
        );

        if (idGuiaEdicion != null) {

                ps.setInt(
                        15,
                        idGuiaEdicion
                );
        }


        int filasInsertadas =
                ps.executeUpdate();

        if (filasInsertadas == 0) {

            throw new Exception(
                    "No fue posible guardar la guía."
            );
        }

        String mensaje =
        idGuiaEdicion == null
                ? "Guía de despacho de material guardada correctamente."
                : "Guía de despacho de material actualizada correctamente.";

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