import javax.swing.*;
import java.awt.*;

public class CatalogosPage extends JPanel {

    private final Runnable accionVolverModulo;

    private final CardLayout cardLayout;
    private final JPanel panelPantallas;

    private static final String PANTALLA_CATALOGOS =
            "CATALOGOS";

    private static final String PANTALLA_MAQUINARIA =
            "MAQUINARIA";

    private static final String PANTALLA_CODIGOS_ALQUILADOS =
                "CODIGOS_ALQUILADOS";
    private static final String PANTALLA_SECTORES =
            "SECTORES";
    private static final String PANTALLA_PISCINAS =
            "PISCINAS";
    private static final String PANTALLA_TIPOS_ACTIVIDAD =
            "TIPOS_ACTIVIDAD";
    private static final String PANTALLA_ENTIDADES =
            "ENTIDADES";

    public CatalogosPage(
            Runnable accionVolverModulo
    ) {

        this.accionVolverModulo =
                accionVolverModulo;

        setLayout(new BorderLayout());

        cardLayout =
                new CardLayout();

        panelPantallas =
                new JPanel(cardLayout);

        panelPantallas.add(
                crearPantallaCatalogos(),
                PANTALLA_CATALOGOS
        );

        panelPantallas.add(
                new MaquinariaPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_MAQUINARIA
        );

        panelPantallas.add(
                new CodigosAlquiladosPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_CODIGOS_ALQUILADOS
        );
        panelPantallas.add(
                new SectorProyectoPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_SECTORES
        );
        panelPantallas.add(
                new PiscinaProyectoPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_PISCINAS
        );
        panelPantallas.add(
                new TipoActividadProyectoPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_TIPOS_ACTIVIDAD
        );
        panelPantallas.add(
                new EntidadMaquinariaPage(
                        this::mostrarCatalogos
                ),
                PANTALLA_ENTIDADES
        );

        add(
                panelPantallas,
                BorderLayout.CENTER
        );

        mostrarCatalogos();
    }

    private JPanel crearPantallaCatalogos() {

    JPanel panelPrincipal =
            new JPanel(
                    new BorderLayout(
                            15,
                            15
                    )
            );

    panelPrincipal.setBackground(
            new Color(
                    244,
                    246,
                    248
            )
    );

    panelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(
                    20,
                    20,
                    20,
                    20
            )
    );

    JPanel panelSuperior =
            new JPanel(
                    new BorderLayout(
                            10,
                            10
                    )
            );

    panelSuperior.setOpaque(false);

    JButton btnVolver =
            new JButton(
                    "← Volver"
            );

    btnVolver.setFocusPainted(false);

    btnVolver.addActionListener(
            e -> accionVolverModulo.run()
    );

    JLabel titulo =
            new JLabel(
                    "Catálogos del Proyecto"
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

    panelSuperior.add(
            btnVolver,
            BorderLayout.WEST
    );

    panelSuperior.add(
            titulo,
            BorderLayout.CENTER
    );

    panelPrincipal.add(
            panelSuperior,
            BorderLayout.NORTH
    );

    /*
     * Cero filas significa que Java creará automáticamente
     * todas las filas necesarias, manteniendo tres columnas.
     */
    JPanel panelOpciones =
            new JPanel(
                    new GridLayout(
                            3,
                            3,
                            20,
                            20
                    )
            );

    panelOpciones.setOpaque(false);

    panelOpciones.add(
            crearTarjeta(
                    "Maquinaria",
                    "Equipos propios, alquilados y de terceros.",
                    this::mostrarMaquinaria
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Proveedores y propietarios",
                    "Personas y empresas relacionadas "
                            + "con los equipos.",
                    this::mostrarEntidades
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Códigos alquilados",
                    "Códigos actuales, anteriores "
                            + "y equivalencias.",
                    this::mostrarCodigosAlquilados
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Sectores",
                    "Crear, editar y administrar "
                            + "los sectores de los proyectos.",
                    this::mostrarSectores
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Piscinas",
                    "Crear, editar y administrar "
                            + "las piscinas de cada sector.",
                    this::mostrarPiscinas
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Tipos de Actividad",
                    "Crear, editar y administrar "
                            + "los tipos de actividad de los proyectos.",
                    this::mostrarTiposActividad
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Canteras y materiales",
                    "Catálogo de canteras y tipos "
                            + "de material pétreo.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Tarifas de material",
                    "Costo por metro cúbico según "
                            + "cantera y material.",
                    null
            )
    );

    panelOpciones.add(
            crearTarjeta(
                    "Tarifas de transporte",
                    "Costo por viaje según cantera, "
                            + "sector y destino.",
                    null
            )
    );

    /*
     * Altura mínima de cada fila de tarjetas.
     * Como son nueve tarjetas y tres columnas,
     * se generan tres filas.
     */
    panelOpciones.setPreferredSize(
            new Dimension(
                    1100,
                    380
            )
    );

    JScrollPane scrollOpciones =
        new JScrollPane(
                panelOpciones
        );

scrollOpciones.setBorder(null);
scrollOpciones.setOpaque(false);

scrollOpciones.getViewport()
        .setOpaque(false);

scrollOpciones.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
);

scrollOpciones.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_NEVER
);

scrollOpciones.getHorizontalScrollBar()
        .setUnitIncrement(24);

panelPrincipal.add(
        scrollOpciones,
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
                    new BorderLayout(
                            12,
                            12
                    )
            );

    tarjeta.setBackground(
            Color.WHITE
    );

    tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            new Color(
                                    209,
                                    213,
                                    219
                            )
                    ),
                    BorderFactory.createEmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            )
    );

    tarjeta.setPreferredSize(
            new Dimension(
                    320,
                    220
            )
    );

    /*
     * Se utiliza JTextArea para permitir que los títulos
     * largos ocupen dos líneas sin mostrar puntos suspensivos.
     */
    JTextArea txtTitulo =
            new JTextArea(
                    titulo
            );

    txtTitulo.setEditable(false);
    txtTitulo.setFocusable(false);
    txtTitulo.setLineWrap(true);
    txtTitulo.setWrapStyleWord(true);
    txtTitulo.setOpaque(false);

    txtTitulo.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    18
            )
    );

    txtTitulo.setForeground(
            new Color(
                    17,
                    24,
                    39
            )
    );

    txtTitulo.setRows(2);

    JTextArea txtDescripcion =
            new JTextArea(
                    descripcion
            );

    txtDescripcion.setEditable(false);
    txtDescripcion.setFocusable(false);
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
            new Color(
                    55,
                    65,
                    81
            )
    );

    JButton btnAbrir =
            new JButton(
                    "Abrir"
            );

    btnAbrir.setFocusPainted(false);

    btnAbrir.addActionListener(
            e -> {

                if (accion != null) {

                    accion.run();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "El catálogo \""
                                    + titulo
                                    + "\" se conectará posteriormente.",
                            "LPP Smart ERP",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
    );

    JPanel panelBoton =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.RIGHT,
                            0,
                            0
                    )
            );

    panelBoton.setOpaque(false);

    panelBoton.add(
            btnAbrir
    );

    tarjeta.add(
            txtTitulo,
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

    private void mostrarMaquinaria() {

        cardLayout.show(
                panelPantallas,
                PANTALLA_MAQUINARIA
        );
    }

    private void mostrarCatalogos() {

        cardLayout.show(
                panelPantallas,
                PANTALLA_CATALOGOS
        );
    }

    private void mostrarCodigosAlquilados() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_CODIGOS_ALQUILADOS
    );
}
private void mostrarSectores() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_SECTORES
    );
}
private void mostrarPiscinas() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_PISCINAS
    );
}
private void mostrarTiposActividad() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_TIPOS_ACTIVIDAD
    );
}
private void mostrarEntidades() {

    cardLayout.show(
            panelPantallas,
            PANTALLA_ENTIDADES
    );
}

}
