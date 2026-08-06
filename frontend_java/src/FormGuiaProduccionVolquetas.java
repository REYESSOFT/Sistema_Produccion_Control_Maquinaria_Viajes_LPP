import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

public class FormGuiaProduccionVolquetas extends JDialog {

    private JTextField txtNumeroGuia;
    private JFormattedTextField txtFecha;
    private JTextField txtChofer;
    private JTextField txtPlaca;
    private JSpinner spnM3;
    private JTable tablaDetalle;
    private JTextField txtRecibiConforme;
    private JTextArea txtObservaciones;
    private Integer idGuiaEdicion = null;
   

    public FormGuiaProduccionVolquetas(Window parent) {
        super(parent, "Guía Producción Volquetas - EQUIPOS PRO", ModalityType.APPLICATION_MODAL);

        setSize(1000, 680);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel(
                "GUÍA PRODUCCIÓN VOLQUETAS - EQUIPOS PRO",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(3, 4, 10, 10));

        txtNumeroGuia = new JTextField();

        txtFecha = crearCampoFecha();

        txtChofer = new JTextField();
        txtPlaca = new JTextField();

        spnM3 = new JSpinner(
                new SpinnerNumberModel(
                        0.00,
                        0.00,
                        999999.99,
                        0.50
                )
        );

        panelDatos.add(new JLabel("N° Guía:"));
        panelDatos.add(txtNumeroGuia);

        panelDatos.add(new JLabel("Fecha:"));
        panelDatos.add(txtFecha);

        panelDatos.add(new JLabel("Chofer:"));
        panelDatos.add(txtChofer);

        panelDatos.add(new JLabel("Placa:"));
        panelDatos.add(txtPlaca);

        panelDatos.add(new JLabel("M3:"));
        panelDatos.add(spnM3);

        panelDatos.add(new JLabel());
        panelDatos.add(new JLabel());

        String[] columnas = {
                "N°",
                "Proyecto",
                "Sector",
                "Cantera",
                "Material",
                "Hora Origen",
                "Hora Destino"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 10);

        for (int fila = 0; fila < 10; fila++) {
            modelo.setValueAt(fila + 1, fila, 0);
        }

        tablaDetalle = new JTable(modelo);
        tablaDetalle.setRowHeight(25);

        JScrollPane scrollTabla = new JScrollPane(tablaDetalle);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(panelDatos, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));

        JPanel panelExtra = new JPanel(new GridLayout(2, 2, 10, 10));

        txtRecibiConforme = new JTextField();

        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        panelExtra.add(new JLabel("Recibí conforme:"));
        panelExtra.add(txtRecibiConforme);

        panelExtra.add(new JLabel("Observaciones:"));
        panelExtra.add(new JScrollPane(txtObservaciones));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarGuia());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        panelInferior.add(panelExtra, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JFormattedTextField crearCampoFecha() {

        try {

            MaskFormatter mascara = new MaskFormatter("##/##/####");
            mascara.setPlaceholderCharacter('_');

            return new JFormattedTextField(mascara);

        } catch (ParseException e) {

            return new JFormattedTextField();
        }
    }

    public void cargarGuia(String numeroGuia) {

    try {

        GuiaAPI.GuiaProduccionDetalle guia =
                GuiaAPI.obtenerDetalleProduccion(
                        "EQUIPOS PRO",
                        numeroGuia,
                        "Guía Producción Volquetas"
                );

        idGuiaEdicion =
                guia.idGuia();

        txtNumeroGuia.setText(
                guia.numeroGuia()
        );

        txtFecha.setText(
                guia.fecha() == null
                        ? ""
                        : guia.fecha()
                                .format(
                                        java.time.format.DateTimeFormatter
                                                .ofPattern("dd/MM/yyyy")
                                )
        );

        txtChofer.setText(
                guia.choferOperador()
        );

        txtPlaca.setText(
                guia.placa()
        );

        spnM3.setValue(
                guia.m3()
        );

        txtRecibiConforme.setText(
                guia.recibiConforme()
        );

        txtObservaciones.setText(
                guia.observaciones()
        );

        DefaultTableModel modelo =
                (DefaultTableModel) tablaDetalle.getModel();

        for (
                int fila = 0;
                fila < modelo.getRowCount();
                fila++
        ) {

            for (
                    int columna = 1;
                    columna < modelo.getColumnCount();
                    columna++
            ) {

                modelo.setValueAt(
                        null,
                        fila,
                        columna
                );
            }
        }

        if (guia.detalle() != null) {

            for (
                    GuiaAPI.GuiaProduccionDetalleFila detalle
                            : guia.detalle()
            ) {

                int fila =
                        detalle.numeroFila() - 1;

                if (
                        fila < 0
                        || fila >= modelo.getRowCount()
                ) {
                    continue;
                }

                modelo.setValueAt(
                        detalle.proyecto(),
                        fila,
                        1
                );

                modelo.setValueAt(
                        detalle.sector(),
                        fila,
                        2
                );

                modelo.setValueAt(
                        detalle.cantera(),
                        fila,
                        3
                );

                modelo.setValueAt(
                        detalle.material(),
                        fila,
                        4
                );

                modelo.setValueAt(
                        detalle.horaOrigen(),
                        fila,
                        5
                );

                modelo.setValueAt(
                        detalle.horaDestino(),
                        fila,
                        6
                );
            }
        }

        setTitle(
                "Editar Guía Producción Volquetas - N° "
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


   private void guardarGuia() {

    if (
            txtNumeroGuia
                    .getText()
                    .trim()
                    .isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el número de guía.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtNumeroGuia.requestFocus();
        return;
    }

    String fechaTexto =
            txtFecha
                    .getText()
                    .trim();

    if (
            fechaTexto.isEmpty()
            || fechaTexto.contains("_")
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la fecha completa.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtFecha.requestFocus();
        return;
    }

    if (
            txtChofer
                    .getText()
                    .trim()
                    .isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el chofer.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtChofer.requestFocus();
        return;
    }

    if (
            txtPlaca
                    .getText()
                    .trim()
                    .isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la placa.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtPlaca.requestFocus();
        return;
    }

    try {

        java.time.LocalDate fecha =
                java.time.LocalDate.parse(
                        fechaTexto,
                        java.time.format.DateTimeFormatter
                                .ofPattern("dd/MM/yyyy")
                );

        java.util.List<
                GuiaAPI.GuiaProduccionGuardarFila
        > filas =
                new java.util.ArrayList<>();

        for (
                int fila = 0;
                fila < tablaDetalle.getRowCount();
                fila++
        ) {

            String proyecto =
                    obtenerTextoCelda(
                            fila,
                            1
                    );

            String sector =
                    obtenerTextoCelda(
                            fila,
                            2
                    );

            String cantera =
                    obtenerTextoCelda(
                            fila,
                            3
                    );

            String material =
                    obtenerTextoCelda(
                            fila,
                            4
                    );

            String horaOrigen =
                    obtenerTextoCelda(
                            fila,
                            5
                    );

            String horaDestino =
                    obtenerTextoCelda(
                            fila,
                            6
                    );

            boolean filaVacia =
                    proyecto.isEmpty()
                    && sector.isEmpty()
                    && cantera.isEmpty()
                    && material.isEmpty()
                    && horaOrigen.isEmpty()
                    && horaDestino.isEmpty();

            if (filaVacia) {
                continue;
            }

            filas.add(
                    new GuiaAPI.GuiaProduccionGuardarFila(
                            fila + 1,
                            proyecto,
                            sector,
                            cantera,
                            material,
                            horaOrigen,
                            horaDestino
                    )
            );
        }

        GuiaAPI.GuiaProduccionGuardar guia =
                new GuiaAPI.GuiaProduccionGuardar(
                        idGuiaEdicion,
                        txtNumeroGuia
                                .getText()
                                .trim(),
                        fecha,
                        txtPlaca
                                .getText()
                                .trim()
                                .toUpperCase(),
                        txtChofer
                                .getText()
                                .trim(),
                        ((Number) spnM3.getValue())
                                .doubleValue(),
                        txtRecibiConforme
                                .getText()
                                .trim(),
                        txtObservaciones
                                .getText()
                                .trim(),
                        filas
                );

        boolean esEdicion =
                idGuiaEdicion != null;

        GuiaAPI.GuiaProduccionDetalle resultado =
                GuiaAPI.guardarGuiaProduccion(
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

    
    private String obtenerTextoCelda(int fila, int columna) {

        Object valor = tablaDetalle.getValueAt(fila, columna);

        return valor == null
                ? ""
                : valor.toString().trim();
    }

    private void asignarHora(
            PreparedStatement statement,
            int indice,
            String hora
    ) throws Exception {

        if (hora == null || hora.isBlank()) {
            statement.setNull(indice, java.sql.Types.TIME);
            return;
        }

        if (!hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new Exception(
                    "La hora debe tener formato HH:mm. Valor incorrecto: "
                    + hora
            );
        }

        statement.setTime(
                indice,
                Time.valueOf(hora + ":00")
        );
    }
}