import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CatalogoCanteraMaterialPage extends JPanel {

    private final Runnable accionVolver;

    private JTextField txtCantera;
    private JTextField txtMaterial;
    private JTextField txtDestinoSector;
    private JComboBox<String> cmbEstado;

    private JTable tablaTarifas;
    private DefaultTableModel modeloTabla;

    public CatalogoCanteraMaterialPage(
            Runnable accionVolver
    ) {

        this.accionVolver =
                accionVolver;

        setLayout(
                new BorderLayout(
                        12,
                        12
                )
        );

        setBackground(
                new Color(
                        244,
                        246,
                        248
                )
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
        cargarTarifas();
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

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelPrincipal.setOpaque(false);

        JPanel panelTitulo =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelTitulo.setOpaque(false);

        JButton btnVolver =
                new JButton(
                        "← Volver"
                );

        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(
                e -> accionVolver.run()
        );

        JLabel titulo =
                new JLabel(
                        "Canteras - Material Pétreo"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titulo.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        panelTitulo.add(
                btnVolver,
                BorderLayout.WEST
        );

        panelTitulo.add(
                titulo,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        JPanel panelFiltros =
                new JPanel(
                        new GridLayout(
                                2,
                                5,
                                10,
                                8
                        )
                );

        panelFiltros.setBorder(
                BorderFactory.createTitledBorder(
                        "Filtros de búsqueda"
                )
        );

        txtCantera =
                new JTextField();

        txtMaterial =
                new JTextField();

        txtDestinoSector =
                new JTextField();

        cmbEstado =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "ACTIVO",
                                "INACTIVO"
                        }
                );

        JButton btnBuscar =
                new JButton(
                        "Buscar"
                );

        btnBuscar.addActionListener(
                e -> cargarTarifas()
        );

        panelFiltros.add(
                new JLabel(
                        "Cantera:"
                )
        );

        panelFiltros.add(
                new JLabel(
                        "Tipo de material:"
                )
        );

        panelFiltros.add(
                new JLabel(
                        "Destino / sector:"
                )
        );

        panelFiltros.add(
                new JLabel(
                        "Estado:"
                )
        );

        panelFiltros.add(
                new JLabel()
        );

        panelFiltros.add(
                txtCantera
        );

        panelFiltros.add(
                txtMaterial
        );

        panelFiltros.add(
                txtDestinoSector
        );

        panelFiltros.add(
                cmbEstado
        );

        panelFiltros.add(
                btnBuscar
        );

        panelPrincipal.add(
                panelFiltros,
                BorderLayout.CENTER
        );

        return panelPrincipal;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Cantera",
                                "Tipo de material",
                                "Destino / sector",
                                "Costo unitario material",
                                "Costo unitario transporte",
                                "Estado"
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

        tablaTarifas =
                new JTable(
                        modeloTabla
                );

        /*
         * Ocultar ID.
         */
        tablaTarifas.removeColumn(
                tablaTarifas
                        .getColumnModel()
                        .getColumn(0)
        );

        tablaTarifas.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaTarifas.setRowHeight(
                28
        );

        tablaTarifas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaTarifas
                .getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        tablaTarifas
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                tablaTarifas
                                        .getTableHeader()
                                        .getPreferredSize()
                                        .width,
                                42
                        )
                );

        /*
         * Después de ocultar ID:
         *
         * 0 = Cantera
         * 1 = Material
         * 2 = Destino
         * 3 = Costo material
         * 4 = Costo transporte
         * 5 = Estado
         */
        tablaTarifas
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(
                        220
                );

        tablaTarifas
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(
                        220
                );

        tablaTarifas
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(
                        220
                );

        tablaTarifas
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(
                        180
                );

        tablaTarifas
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(
                        190
                );

        tablaTarifas
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(
                        110
                );

        JScrollPane scroll =
                new JScrollPane(
                        tablaTarifas
                );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Tarifas de canteras y materiales"
                )
        );

        return scroll;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setOpaque(false);

        JButton btnNueva =
                new JButton(
                        "Nueva tarifa"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );

        JButton btnReactivar =
                new JButton(
                        "Reactivar"
                );

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        btnNueva.addActionListener(
                e -> nuevaTarifa()
        );

        btnEditar.addActionListener(
                e -> editarTarifa()
        );

        btnEliminar.addActionListener(
                e -> desactivarTarifa()
        );

        btnReactivar.addActionListener(
                e -> reactivarTarifa()
        );

        btnActualizar.addActionListener(
                e -> cargarTarifas()
        );

        panel.add(
                btnNueva
        );

        panel.add(
                btnEditar
        );

        panel.add(
                btnEliminar
        );

        panel.add(
                btnReactivar
        );

        panel.add(
                btnActualizar
        );

        return panel;
    }

    private void cargarTarifas() {

        String cantera =
                txtCantera == null
                        ? ""
                        : txtCantera
                                .getText()
                                .trim();

        String material =
                txtMaterial == null
                        ? ""
                        : txtMaterial
                                .getText()
                                .trim();

        String destinoSector =
                txtDestinoSector == null
                        ? ""
                        : txtDestinoSector
                                .getText()
                                .trim();

        String estado =
                cmbEstado == null
                || cmbEstado.getSelectedItem() == null
                        ? "Todos"
                        : cmbEstado
                                .getSelectedItem()
                                .toString();

        try {

            List<CatalogoCanteraMaterialDAO.TarifaResumen> lista =
                    CatalogoCanteraMaterialDAO.buscar(
                            cantera,
                            material,
                            destinoSector,
                            estado
                    );

            modeloTabla.setRowCount(
                    0
            );

            for (
                    CatalogoCanteraMaterialDAO.TarifaResumen tarifa
                    : lista
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                tarifa.idTarifa(),
                                tarifa.cantera(),
                                tarifa.material(),
                                tarifa.destinoSector(),
                                formatearMoneda(
                                        tarifa.costoUnitarioMaterial()
                                ),
                                formatearMoneda(
                                        tarifa.costoUnitarioTransporte()
                                ),
                                tarifa.activo()
                                        ? "ACTIVO"
                                        : "INACTIVO"
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las tarifas:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void nuevaTarifa() {

        Window owner =
                SwingUtilities.getWindowAncestor(
                        this
                );

        FormCatalogoCanteraMaterial formulario =
                new FormCatalogoCanteraMaterial(
                        owner
                );

        formulario.setVisible(
                true
        );

        if (formulario.isGuardado()) {

            cargarTarifas();
        }
    }

    private void editarTarifa() {

        Integer idTarifa =
                obtenerIdSeleccionado();

        if (idTarifa == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una tarifa en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Window owner =
                SwingUtilities.getWindowAncestor(
                        this
                );

        FormCatalogoCanteraMaterial formulario =
                new FormCatalogoCanteraMaterial(
                        owner,
                        idTarifa
                );

        formulario.setVisible(
                true
        );

        if (formulario.isGuardado()) {

            cargarTarifas();
        }
    }

    private void desactivarTarifa() {

        Integer idTarifa =
                obtenerIdSeleccionado();

        if (idTarifa == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una tarifa en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String estado =
                obtenerEstadoSeleccionado();

        if (
                estado != null
                && estado.equalsIgnoreCase(
                        "INACTIVO"
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "La tarifa seleccionada ya está inactiva.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar la tarifa seleccionada?\n\n"
                                + "La tarifa dejará de estar disponible "
                                + "para nuevos registros,\n"
                                + "pero permanecerá guardada para "
                                + "conservar el historial.",
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

            CatalogoCanteraMaterialDAO.desactivar(
                    idTarifa
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tarifa eliminada correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTarifas();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar la tarifa:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void reactivarTarifa() {

        Integer idTarifa =
                obtenerIdSeleccionado();

        if (idTarifa == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una tarifa en la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String estado =
                obtenerEstadoSeleccionado();

        if (
                estado != null
                && estado.equalsIgnoreCase(
                        "ACTIVO"
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "La tarifa seleccionada ya está activa.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea reactivar la tarifa seleccionada?",
                        "Confirmar reactivación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                respuesta
                != JOptionPane.YES_OPTION
        ) {

            return;
        }

        try {

            CatalogoCanteraMaterialDAO.reactivar(
                    idTarifa
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tarifa reactivada correctamente.",
                    "LPP Smart ERP",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTarifas();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al reactivar la tarifa:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private Integer obtenerIdSeleccionado() {

        int filaVista =
                tablaTarifas.getSelectedRow();

        if (filaVista == -1) {

            return null;
        }

        int filaModelo =
                tablaTarifas.convertRowIndexToModel(
                        filaVista
                );

        Object valor =
                modeloTabla.getValueAt(
                        filaModelo,
                        0
                );

        if (valor == null) {

            return null;
        }

        return Integer.valueOf(
                valor.toString()
        );
    }

    private String obtenerEstadoSeleccionado() {

        int filaVista =
                tablaTarifas.getSelectedRow();

        if (filaVista == -1) {

            return null;
        }

        int filaModelo =
                tablaTarifas.convertRowIndexToModel(
                        filaVista
                );

        Object valor =
                modeloTabla.getValueAt(
                        filaModelo,
                        6
                );

        return valor == null
                ? null
                : valor.toString();
    }

    private String formatearMoneda(
        double valor
) {

    return String.format(
            "$%,.2f",
            valor
    );
}
}