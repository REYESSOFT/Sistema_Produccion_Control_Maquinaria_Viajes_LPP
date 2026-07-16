import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetalleGuiaDespachoMaterialDialog extends JDialog {

    private final String empresa;
    private final String numeroGuia;

    public DetalleGuiaDespachoMaterialDialog(
            Window parent,
            String empresa,
            String numeroGuia
    ) {

        super(
                parent,
                "Detalle Guía Despacho de Material N° "
                        + numeroGuia,
                ModalityType.APPLICATION_MODAL
        );

        this.empresa = empresa;
        this.numeroGuia = numeroGuia;

        setSize(950, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarDetalle();
    }

    private void cargarDetalle() {

        String sql = """
                SELECT
                    e.nombre_empresa,
                    g.numero_guia,
                    DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                    COALESCE(g.chofer_operador, '') AS chofer,
                    COALESCE(g.solicitante, '') AS solicitante,
                    COALESCE(g.sector, '') AS sector,
                    COALESCE(g.placa, '') AS placa,
                    COALESCE(g.m3, 0) AS cubicaje,
                    COALESCE(g.origen, '') AS origen,
                    COALESCE(g.destino, '') AS destino,
                    TIME_FORMAT(g.hora_inicio, '%H:%i') AS hora_entrada,
                    TIME_FORMAT(g.hora_fin, '%H:%i') AS hora_salida,
                    COALESCE(g.material, '') AS material,
                    COALESCE(g.observaciones, '') AS observaciones,
                    COALESCE(g.recibi_conforme, '') AS recibi_conforme,
                    g.estado
                FROM guias g
                INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa
                WHERE e.nombre_empresa = ?
                  AND g.tipo_guia =
                      'Guía Despacho de Material'
                  AND g.numero_guia = ?
                LIMIT 1
                """;

        try (
                Connection conexion =
                        ConexionDB.obtenerConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(1, empresa);
            ps.setString(2, numeroGuia);

            try (
                    ResultSet resultado =
                            ps.executeQuery()
            ) {

                if (!resultado.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se encontró la guía seleccionada.",
                            "Información",
                            JOptionPane.WARNING_MESSAGE
                    );

                    dispose();
                    return;
                }

                JPanel panelPrincipal =
                        new JPanel(
                                new BorderLayout(10, 10)
                        );

                panelPrincipal.setBorder(
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                );

                panelPrincipal.add(
                        crearPanelDatosGenerales(resultado),
                        BorderLayout.NORTH
                );

                panelPrincipal.add(
                        crearPanelDespacho(resultado),
                        BorderLayout.CENTER
                );

                panelPrincipal.add(
                        crearPanelInferior(resultado),
                        BorderLayout.SOUTH
                );

                setContentPane(panelPrincipal);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al mostrar el detalle:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
            dispose();
        }
    }

    private JPanel crearPanelDatosGenerales(
            ResultSet resultado
    ) throws Exception {

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

        agregarDato(
                panel,
                "Empresa:",
                resultado.getString("nombre_empresa")
        );

        agregarDato(
                panel,
                "N° Guía:",
                resultado.getString("numero_guia")
        );

        agregarDato(
                panel,
                "Fecha:",
                resultado.getString("fecha")
        );

        agregarDato(
                panel,
                "Estado:",
                resultado.getString("estado")
        );

        agregarDato(
                panel,
                "Chofer:",
                resultado.getString("chofer")
        );

        agregarDato(
                panel,
                "Solicitante:",
                resultado.getString("solicitante")
        );

        agregarDato(
                panel,
                "Sector:",
                resultado.getString("sector")
        );

        agregarDato(
                panel,
                "Placa:",
                resultado.getString("placa")
        );

        return panel;
    }

    private JPanel crearPanelDespacho(
            ResultSet resultado
    ) throws Exception {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                8
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Información del despacho"
                )
        );

        agregarDatoSimple(
                panel,
                "Cubicaje:",
                resultado.getString("cubicaje")
        );

        agregarDatoSimple(
                panel,
                "Lugar(es) de origen:",
                resultado.getString("origen")
        );

        agregarDatoSimple(
                panel,
                "Lugar de destino:",
                resultado.getString("destino")
        );

        agregarDatoSimple(
                panel,
                "Hora de entrada:",
                valorTexto(
                        resultado.getString("hora_entrada")
                )
        );

        agregarDatoSimple(
                panel,
                "Hora de salida:",
                valorTexto(
                        resultado.getString("hora_salida")
                )
        );

        agregarDatoSimple(
                panel,
                "Tipo(s) de material:",
                resultado.getString("material")
        );

        return panel;
    }

    private JPanel crearPanelInferior(
            ResultSet resultado
    ) throws Exception {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel panelObservaciones =
                new JPanel(new BorderLayout());

        panelObservaciones.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        JTextArea textoObservaciones =
                new JTextArea(
                        resultado.getString(
                                "observaciones"
                        )
                );

        textoObservaciones.setEditable(false);
        textoObservaciones.setLineWrap(true);
        textoObservaciones.setWrapStyleWord(true);

        panelObservaciones.add(
                new JScrollPane(textoObservaciones),
                BorderLayout.CENTER
        );

        JPanel panelRecibe =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelRecibe.add(
                new JLabel("Recibí conforme:"),
                BorderLayout.WEST
        );

        panelRecibe.add(
                new JLabel(
                        resultado.getString(
                                "recibi_conforme"
                        )
                ),
                BorderLayout.CENTER
        );

        JButton btnCerrar =
                new JButton("Cerrar");

        btnCerrar.addActionListener(
                e -> dispose()
        );

        JPanel panelBoton =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBoton.add(btnCerrar);

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(8, 8)
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
                panelBoton,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private void agregarDato(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        panel.add(new JLabel(etiqueta));
        panel.add(
                new JLabel(
                        valor == null ? "" : valor
                )
        );
    }

    private void agregarDatoSimple(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        panel.add(new JLabel(etiqueta));
        panel.add(
                new JLabel(
                        valor == null ? "" : valor
                )
        );
    }

    private String valorTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor;
    }
}