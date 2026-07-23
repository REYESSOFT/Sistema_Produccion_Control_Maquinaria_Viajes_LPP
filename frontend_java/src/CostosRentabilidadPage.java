import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CostosRentabilidadPage extends JPanel {

    private final Runnable accionVolver;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CostosRentabilidadPage(
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
        cargarDatos();
    }

    private void crearInterfaz() {

        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        add(
                crearTabla(),
                BorderLayout.CENTER
        );

        add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
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
                        "Costos y Rentabilidad"
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

        JLabel subtitulo =
                new JLabel(
                        "Resumen económico y avance por proyecto"
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

        JPanel panelTitulos =
                new JPanel();

        panelTitulos.setLayout(
                new BoxLayout(
                        panelTitulos,
                        BoxLayout.Y_AXIS
                )
        );

        panelTitulos.setOpaque(false);

        panelTitulos.add(titulo);
        panelTitulos.add(
                Box.createVerticalStrut(4)
        );
        panelTitulos.add(subtitulo);

        panel.add(
                btnVolver,
                BorderLayout.WEST
        );

        panel.add(
                panelTitulos,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
        "ID",
        "Código",
        "Proyecto",
        "Empresa",

        "<html><center>Metros Lineales<br>Contratados</center></html>",
        "<html><center>Metros Lineales<br>Ejecutados</center></html>",
        "<html><center>% avance<br>lineal</center></html>",

        "<html><center>Cantidad<br>contratada</center></html>",
        "<html><center>Volumen<br>acumulado</center></html>",
        "<html><center>Costo<br>acumulado</center></html>",
        "<html><center>% avance<br>físico</center></html>",
        "<html><center>% avance<br>contractual</center></html>",

        "Ingreso",
        "<html><center>Costo<br>material</center></html>",
        "<html><center>Costo<br>transporte</center></html>",
        "<html><center>Costo<br>maquinaria</center></html>",
        "<html><center>Costo<br>total</center></html>",
        "<html><center>Costo por<br>Metro Lineal</center></html>",
        "Utilidad",
        "<html><center>Utilidad por<br>Metro Lineal</center></html>",
        "<html><center>Rentabilidad<br>%</center></html>"
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
                new JTable(
                        modeloTabla
                );

        tabla.removeColumn(
                tabla.getColumnModel()
                        .getColumn(0)
        );
        tabla.removeColumn(
        tabla.getColumnModel()
                .getColumn(6)
);

        tabla.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tabla.setRowHeight(28);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabla.getTableHeader().setPreferredSize(
                new Dimension(
                        tabla.getTableHeader()
                                .getPreferredSize()
                                .width,
                        44
                )
        );

        int[] anchosColumnas = {
        110, // Código
        300, // Proyecto
        200, // Empresa

        125, // Metros contratados
        125, // Metros ejecutados
        105, // Avance lineal

        125, // Volumen acumulado
        130, // Costo acumulado
        110, // Avance físico
        120, // Avance contractual

        130, // Ingreso
        125, // Costo material
        135, // Costo transporte
        135, // Costo maquinaria
        125, // Costo total
        145, // Costo por metro
        130, // Utilidad
        150, // Utilidad por metro
        130  // Rentabilidad
};

        for (
                int i = 0;
                i < anchosColumnas.length;
                i++
        ) {
            tabla.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            anchosColumnas[i]
                    );
        }

        JScrollPane scroll =
                new JScrollPane(
                        tabla
                );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Resumen por proyecto"
                )
        );

        return scroll;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setOpaque(false);

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        btnActualizar.addActionListener(
                e -> cargarDatos()
        );

        panel.add(btnActualizar);

        return panel;
    }

    private void cargarDatos() {

        try {

            List<CostosRentabilidadDAO.CostosRentabilidadResumen> lista =
                    CostosRentabilidadDAO.obtenerResumen();

            modeloTabla.setRowCount(0);

            for (
                    CostosRentabilidadDAO.CostosRentabilidadResumen item
                    : lista
            ) {

                modeloTabla.addRow(
                        new Object[]{
                                item.idProyecto(),
                                item.codigoProyecto(),
                                item.descripcionProyecto(),
                                item.empresa(),

                                formatearNumero(
                                        item.metrosContratados()
                                ),

                                formatearNumero(
                                        item.metrosEjecutados()
                                ),

                                formatearPorcentaje(
                                        item.porcentajeAvance()
                                ),
                                formatearNumero(
        item.cantidadContratada()
),

formatearNumero(
        item.volumenAcumulado()
),

formatearMoneda(
        item.costoAcumulado()
),

formatearPorcentaje(
        item.porcentajeAvanceFisico()
),

formatearPorcentaje(
        item.porcentajeAvanceContractual()
),

                                formatearMoneda(
                                        item.ingreso()
                                ),

                                formatearMoneda(
                                        item.costoMaterial()
                                ),

                                formatearMoneda(
                                        item.costoTransporte()
                                ),

                                formatearMoneda(
                                        item.costoMaquinaria()
                                ),

                                formatearMoneda(
                                        item.costoTotal()
                                ),

                                formatearMoneda(
                                        item.costoPorMetroLineal()
                                ),

                                formatearMoneda(
                                        item.utilidad()
                                ),

                                formatearMoneda(
                                        item.utilidadPorMetroLineal()
                                ),

                                formatearPorcentaje(
                                        item.rentabilidad()
                                )
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar Costos y Rentabilidad:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private String formatearNumero(
            double valor
    ) {
        return String.format(
                "%,.2f",
                valor
        );
    }

    private String formatearPorcentaje(
            double valor
    ) {
        return String.format(
                "%.2f %%",
                valor
        );
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
