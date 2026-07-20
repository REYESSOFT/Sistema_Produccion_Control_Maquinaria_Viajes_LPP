import javax.swing.*;
import java.awt.*;

public class ControlProyeccionPage extends JPanel {


    private final CardLayout cardLayout;

    private final JPanel panelPantallas;

    private static final String PANTALLA_PRINCIPAL =
            "PRINCIPAL";

    private static final String PANTALLA_CATALOGOS =
            "CATALOGOS";
    
    private static final String PANTALLA_PROYECTOS =
            "PROYECTOS";
    private static final String PANTALLA_ASIGNACION_MAQUINARIA =
            "ASIGNACION_MAQUINARIA";
    private static final String PANTALLA_MANTENIMIENTO =
            "MANTENIMIENTO";


    public ControlProyeccionPage() {

    setLayout(new BorderLayout());

    setBackground(
            new Color(244, 246, 248)
    );

    cardLayout =
            new CardLayout();

    panelPantallas =
            new JPanel(cardLayout);

    panelPantallas.add(
            crearPantallaPrincipal(),
            PANTALLA_PRINCIPAL
    );

    panelPantallas.add(
            new CatalogosPage(
                    this::mostrarPantallaPrincipal
            ),
            PANTALLA_CATALOGOS
    );
    panelPantallas.add(
            new ProyectoPage(
                    this::mostrarPantallaPrincipal
            ),
        PANTALLA_PROYECTOS
    );
    panelPantallas.add(
                new AsignacionMaquinariaPage(
                        this::mostrarPantallaPrincipal
                ),
                PANTALLA_ASIGNACION_MAQUINARIA
        );
    panelPantallas.add(
                crearPantallaMantenimiento(),
                PANTALLA_MANTENIMIENTO
    );

    add(
            panelPantallas,
            BorderLayout.CENTER
    );

    mostrarPantallaPrincipal();
}

    private JPanel crearPantallaPrincipal() {

    JPanel panelPrincipal =
            new JPanel(
                    new BorderLayout(15, 15)
            );

    panelPrincipal.setBackground(
            new Color(244, 246, 248)
    );

    panelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(
                    20,
                    20,
                    20,
                    20
            )
    );

    JLabel titulo =
            new JLabel(
                    "Control y Proyección de Proyectos",
                    SwingConstants.LEFT
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

    panelPrincipal.add(
            titulo,
            BorderLayout.NORTH
    );

    JPanel panelOpciones =
            new JPanel(
                    new GridLayout(
                            0,
                            4,
                            15,
                            15
                    )
            );

    panelOpciones.setOpaque(false);

    panelOpciones.add(
            crearTarjeta(
                    "Dashboard de proyectos",
                    "Resumen de avance, costos y rentabilidad.",
                    null
            )
    );
    panelOpciones.add(
            crearTarjeta(
                   "Proyectos",
                   "Crear, editar y consultar proyectos.",
                   this::mostrarProyectos
            )
     );

    panelOpciones.add(
            crearTarjeta(
                    "Asignación de maquinaria",
                    "Asignar equipos y volquetas a cada proyecto.",
                    this::mostrarAsignacionMaquinaria
            )
    );
    panelOpciones.add(
            crearTarjeta(
                    "Mantenimiento",
                    "Gestión de mantenimiento de maquinaria. "
                            + "Estructura pendiente de definición.",
                    this::mostrarMantenimiento
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Control diario",
                    "Registrar avance, maquinaria y material.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Catálogos",
                    "Maquinaria, proveedores, canteras y tarifas.",
                    this::mostrarCatalogos
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Costos y rentabilidad",
                    "Comparar proyección contra ejecución real.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Reportes",
                    "Consultar y exportar información del proyecto.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Registro diario",
                    "Ubicación, piscina y actividad por fecha.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Isla Puná",
                    "Módulo pendiente de definición.",
                    null
            )
    );
    panelOpciones.setPreferredSize(
        new Dimension(
                1500,
                650
        )
);

    JScrollPane scrollOpciones =
        new JScrollPane(
                panelOpciones
        );

scrollOpciones.setBorder(null);

scrollOpciones.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
);

scrollOpciones.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
);

scrollOpciones.getHorizontalScrollBar()
        .setUnitIncrement(24);

scrollOpciones.getVerticalScrollBar()
        .setUnitIncrement(20);

scrollOpciones.getViewport()
        .setBackground(
                new Color(
                        244,
                        246,
                        248
                )
        );

panelPrincipal.add(
        scrollOpciones,
        BorderLayout.CENTER
);

    return panelPrincipal;
}
private JPanel crearPantallaMantenimiento() {

    JPanel panel =
            new JPanel(
                    new BorderLayout(
                            15,
                            15
                    )
            );

    panel.setBackground(
            new Color(
                    244,
                    246,
                    248
            )
    );

    panel.setBorder(
            BorderFactory.createEmptyBorder(
                    20,
                    20,
                    20,
                    20
            )
    );

    JPanel encabezado =
            new JPanel(
                    new BorderLayout(
                            10,
                            10
                    )
            );

    encabezado.setOpaque(false);

    JButton btnVolver =
            new JButton(
                    "← Volver"
            );

    btnVolver.setFocusPainted(false);

    btnVolver.addActionListener(
            e -> mostrarPantallaPrincipal()
    );

    JLabel titulo =
            new JLabel(
                    "Mantenimiento de Maquinaria"
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

    encabezado.add(
            btnVolver,
            BorderLayout.WEST
    );

    encabezado.add(
            titulo,
            BorderLayout.CENTER
    );

    JPanel contenido =
            new JPanel(
                    new GridBagLayout()
            );

    contenido.setBackground(
            Color.WHITE
    );

    contenido.setBorder(
            BorderFactory.createLineBorder(
                    new Color(
                            209,
                            213,
                            219
                    )
            )
    );

    JLabel mensaje =
            new JLabel(
                    "<html><div style='text-align:center;'>"
                            + "<b>Módulo creado.</b><br><br>"
                            + "La estructura, campos y reglas de mantenimiento "
                            + "se encuentran pendientes de definición."
                            + "</div></html>",
                    SwingConstants.CENTER
            );

    mensaje.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    17
            )
    );

    mensaje.setForeground(
            new Color(
                    75,
                    85,
                    99
            )
    );

    contenido.add(
            mensaje
    );

    panel.add(
            encabezado,
            BorderLayout.NORTH
    );

    panel.add(
            contenido,
            BorderLayout.CENTER
    );

    return panel;
}


    private JPanel crearTarjeta(
        String titulo,
        String descripcion,
        Runnable accion
    ) {

        JPanel tarjeta =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(209, 213, 219)
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblTitulo.setForeground(
                new Color(31, 41, 55)
        );

        JTextArea txtDescripcion =
                new JTextArea(descripcion);

        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(false);

        txtDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtDescripcion.setForeground(
                new Color(75, 85, 99)
        );

        JButton btnAbrir =
                new JButton("Abrir");

        btnAbrir.setFocusPainted(false);

        btnAbrir.addActionListener(e -> {

    if (accion != null) {

        accion.run();

    } else {

        JOptionPane.showMessageDialog(
                this,
                "El módulo \"" + titulo
                        + "\" se conectará más adelante.",
                "LPP Smart ERP",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
});

        JPanel panelBoton =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBoton.setOpaque(false);
        panelBoton.add(btnAbrir);

        tarjeta.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        tarjeta.add(
                txtDescripcion,
                BorderLayout.CENTER
        );

        tarjeta.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return tarjeta;
    }


    private void mostrarCatalogos() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_CATALOGOS
    );
}

private void mostrarPantallaPrincipal() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_PRINCIPAL
    );
}
private void mostrarProyectos() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_PROYECTOS
    );
}
private void mostrarAsignacionMaquinaria() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_ASIGNACION_MAQUINARIA
    );
}
private void mostrarMantenimiento() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_MANTENIMIENTO
    );
}
}
