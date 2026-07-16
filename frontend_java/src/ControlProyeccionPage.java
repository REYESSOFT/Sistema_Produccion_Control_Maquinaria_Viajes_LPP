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
                            3,
                            3,
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
                    null
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

    panelPrincipal.add(
            panelOpciones,
            BorderLayout.CENTER
    );

    return panelPrincipal;
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
}
