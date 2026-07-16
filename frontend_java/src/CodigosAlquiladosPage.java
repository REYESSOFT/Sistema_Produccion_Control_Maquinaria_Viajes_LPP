import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CodigosAlquiladosPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private LectorTXTCodigosHistoricos.ResultadoLectura
            ultimaLectura;

    private File ultimoArchivo;

    public CodigosAlquiladosPage(
            Runnable accionVolver
    ) {

        this.accionVolver = accionVolver;

        setLayout(
                new BorderLayout(12, 12)
        );

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
        cargarCodigosHistoricos();
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

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setOpaque(false);

        JButton btnVolver =
                new JButton("← Volver");

        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Códigos Alquilados e Históricos"
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

        panel.add(
                btnVolver,
                BorderLayout.WEST
        );

        panel.add(
                titulo,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                        "ID",
                        "Proveedor",
                        "Código anterior",
                        "Código actual",
                        "Descripción",
                        "Costo hora",
                        "Estado",
                        "Observaciones"
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

        tabla =
                new JTable(modeloTabla);

        tabla.setRowHeight(26);

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        return new JScrollPane(tabla);
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnValidar =
                new JButton(
                        "Validar archivo"
                );
        JButton btnNuevo =
                new JButton(
                        "Nuevo código"
                );
        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnDetalle =
                new JButton(
                        "Detalle"
                );
        
        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );
        JButton btnImportar =
                new JButton(
                        "Importar códigos"
                );
        btnNuevo.addActionListener(
                e -> abrirNuevoCodigo()
        );

        btnValidar.addActionListener(
                e -> validarArchivo()
        );
        btnImportar.addActionListener(
                e -> importarCodigos()
        );
        btnEditar.addActionListener(
                e -> editarCodigoHistorico()
        );
        btnDetalle.addActionListener(
                e -> mostrarDetalleCodigo()
        );
        btnEliminar.addActionListener(
                e -> eliminarCodigoHistorico()
        );

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnDetalle);
        panel.add(btnEliminar);
        panel.add(btnValidar);
        panel.add(btnImportar);

        return panel;
    }

    private void validarArchivo() {

        JFileChooser selector =
                new JFileChooser();

        selector.setDialogTitle(
                "Seleccionar códigos alquilados"
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

            LectorTXTCodigosHistoricos.ResultadoLectura lectura =
                    LectorTXTCodigosHistoricos.leer(
                            archivo
                    );
            ultimaLectura = lectura;
            ultimoArchivo = archivo;

            JOptionPane.showMessageDialog(
                    this,
                    "Validación terminada.\n\n"
                            + "Filas procesadas: "
                            + lectura.filasProcesadas()
                            + "\nFilas vinculables: "
                            + lectura.filasVinculables()
                            + "\nSin código actual: "
                            + lectura.filasSinCodigoActual()
                            + "\nFilas inválidas: "
                            + lectura.filasInvalidas()
                            + "\nPrecios normalizados: "
                            + lectura.preciosNormalizados()
                            + "\nAdvertencias: "
                            + lectura
                                    .advertencias()
                                    .size(),
                    "Resultado de validación",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (
                !lectura
                        .advertencias()
                        .isEmpty()
            ) {

                mostrarAdvertencias(
                        lectura.advertencias()
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al validar el archivo:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void cargarTabla(
            List<LectorTXTCodigosHistoricos
                    .FilaCodigoHistorico> filas
    ) {

        modeloTabla.setRowCount(0);

        for (
                LectorTXTCodigosHistoricos
                        .FilaCodigoHistorico fila
                : filas
        ) {

            modeloTabla.addRow(
                    new Object[]{
                            fila.numeroLinea(),
                            fila.razonSocial(),
                            fila.codigoAnterior(),
                            fila.codigoActual(),
                            fila.descripcion(),
                            String.format(
                                    "$%.2f",
                                    fila.costoHora()
                            ),
                            fila.estadoFila()
                    }
            );
        }
    }

    private void mostrarAdvertencias(
            List<String> advertencias
    ) {

        JTextArea area =
                new JTextArea();

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        StringBuilder texto =
                new StringBuilder();

        for (
                String advertencia
                : advertencias
        ) {

            texto.append(
                    advertencia
            ).append("\n");
        }

        area.setText(
                texto.toString()
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
                "Advertencias",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void importarCodigos() {

    if (
        ultimaLectura == null
        || ultimaLectura
                .filasValidas()
                .isEmpty()
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Primero debe validar el archivo "
                        + "de códigos alquilados.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "Archivo validado:\n"
                            + (
                                ultimoArchivo == null
                                        ? ""
                                        : ultimoArchivo.getName()
                            )
                            + "\n\n"
                            + "Registros a procesar: "
                            + ultimaLectura
                                    .filasValidas()
                                    .size()
                            + "\n\n"
                            + "¿Desea importar los códigos "
                            + "históricos en MySQL?",
                    "Confirmar importación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

    if (
        respuesta
        != JOptionPane.YES_OPTION
    ) {
        return;
    }

    try {

        MigradorCodigosHistoricos.ResultadoMigracion resultado =
                MigradorCodigosHistoricos.importar(
                        ultimaLectura.filasValidas()
                );

        JOptionPane.showMessageDialog(
                this,
                "Importación terminada.\n\n"
                        + "Procesadas: "
                        + resultado.procesadas()
                        + "\nInsertadas: "
                        + resultado.insertadas()
                        + "\nActualizadas: "
                        + resultado.actualizadas()
                        + "\nVinculadas: "
                        + resultado.vinculadas()
                        + "\nPendientes: "
                        + resultado.pendientes()
                        + "\nNo encontradas: "
                        + resultado.noEncontradas()
                        + "\nOmitidas: "
                        + resultado.omitidas()
                        + "\nErrores: "
                        + resultado.errores().size(),
                "Resultado de importación",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (
            !resultado.errores().isEmpty()
        ) {

            mostrarAdvertencias(
                    resultado.errores()
            );
        }
        cargarCodigosHistoricos();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al importar los códigos históricos:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

private void abrirNuevoCodigo() {

    FormCodigoHistorico formulario =
            new FormCodigoHistorico(
                    SwingUtilities
                            .getWindowAncestor(this)
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarCodigosHistoricos();
    }
}

private void cargarCodigosHistoricos() {

    try {

        modeloTabla.setRowCount(0);

        for (
                CodigoHistoricoDAO.CodigoHistoricoResumen registro
                : CodigoHistoricoDAO.obtenerActivos()
        ) {

            modeloTabla.addRow(
                    new Object[]{
                            registro.idCodigoHistorico(),
                            registro.proveedor(),
                            registro.codigoAnterior(),
                            registro.codigoActual(),
                            registro.descripcion(),
                            String.format(
                                    "$%.2f",
                                    registro.costoHora()
                            ),
                            registro.estadoVinculacion(),
                            registro.observaciones()
                    }
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al cargar los códigos históricos:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

private void editarCodigoHistorico() {

    int filaSeleccionada =
            tabla.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un código histórico "
                        + "en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaSeleccionada
            );

    Object valorId =
            modeloTabla.getValueAt(
                    filaModelo,
                    0
            );

    int idCodigoHistorico =
            Integer.parseInt(
                    valorId.toString()
            );

    FormCodigoHistorico formulario =
            new FormCodigoHistorico(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idCodigoHistorico
            );

    formulario.setVisible(true);

    if (formulario.isGuardado()) {

        cargarCodigosHistoricos();
    }
}

private void mostrarDetalleCodigo() {

    int filaSeleccionada =
            tabla.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un código histórico "
                        + "en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaSeleccionada
            );

    Object valorId =
            modeloTabla.getValueAt(
                    filaModelo,
                    0
            );

    int idCodigoHistorico =
            Integer.parseInt(
                    valorId.toString()
            );

    DetalleCodigoHistoricoDialog detalle =
            new DetalleCodigoHistoricoDialog(
                    SwingUtilities
                            .getWindowAncestor(this),
                    idCodigoHistorico
            );

    detalle.setVisible(true);
}

private void eliminarCodigoHistorico() {

    int filaSeleccionada =
            tabla.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un código histórico "
                        + "en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int filaModelo =
            tabla.convertRowIndexToModel(
                    filaSeleccionada
            );

    Object valorId =
            modeloTabla.getValueAt(
                    filaModelo,
                    0
            );

    Object valorCodigoAnterior =
            modeloTabla.getValueAt(
                    filaModelo,
                    2
            );

    int idCodigoHistorico =
            Integer.parseInt(
                    valorId.toString()
            );

    String codigoAnterior =
            valorCodigoAnterior == null
                    ? ""
                    : valorCodigoAnterior.toString();

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar el código histórico "
                            + codigoAnterior
                            + "?\n\n"
                            + "El registro dejará de aparecer "
                            + "en nuevos procesos,\n"
                            + "pero permanecerá almacenado "
                            + "para conservar el historial.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

    if (
        respuesta
        != JOptionPane.YES_OPTION
    ) {
        return;
    }

    try {

        CodigoHistoricoDAO.eliminar(
                idCodigoHistorico
        );

        JOptionPane.showMessageDialog(
                this,
                "Código histórico eliminado correctamente.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarCodigosHistoricos();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al eliminar el código histórico:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

}
