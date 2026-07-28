import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private JPanel panelContenido;
    private Usuario usuarioActual;

    public MenuPrincipal() {

        usuarioActual = SesionUsuario.getUsuarioActual();

        if (usuarioActual == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "No existe una sesión activa.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );

            dispose();
            return;
        }

        setTitle("LPP Smart ERP");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout());

        crearBarraSuperior();
        crearMenuLateral();

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(244, 246, 248));

        add(panelContenido, BorderLayout.CENTER);

        mostrarPantallaInicial();
    }

    private void crearBarraSuperior() {

        JPanel barraSuperior =
                new JPanel(new BorderLayout());

        barraSuperior.setBackground(
                new Color(31, 41, 55)
        );

        barraSuperior.setPreferredSize(
                new Dimension(1300, 60)
        );

        JLabel titulo =
                new JLabel("  LPP Smart ERP");

        titulo.setForeground(Color.WHITE);

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        String empresa;

        if (usuarioActual.getNombreEmpresa() == null) {
            empresa = "Todas las empresas";
        } else {
            empresa = usuarioActual.getNombreEmpresa();
        }

        JLabel usuario =
                new JLabel(
                        "Usuario: "
                                + usuarioActual.getNombreCompleto()
                                + " | "
                                + empresa
                                + "  "
                );

        usuario.setForeground(Color.WHITE);

        usuario.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        barraSuperior.add(
                titulo,
                BorderLayout.WEST
        );

        barraSuperior.add(
                usuario,
                BorderLayout.EAST
        );

        add(
                barraSuperior,
                BorderLayout.NORTH
        );
    }

    private void crearMenuLateral() {

        JPanel menuLateral = new JPanel();

        menuLateral.setLayout(
                new BoxLayout(
                        menuLateral,
                        BoxLayout.Y_AXIS
                )
        );

        menuLateral.setBackground(
                new Color(17, 24, 39)
        );

        menuLateral.setPreferredSize(
                new Dimension(240, 690)
        );

        /*
         * ADMIN:
         * Acceso completo a los módulos existentes.
         */
        if (usuarioActual.esAdmin()) {

            agregarBotonMenu(
                    menuLateral,
                    "Dashboard Gerencial"
            );

            agregarBotonMenu(
                    menuLateral,
                    "Guías"
            );

            agregarBotonMenu(
                    menuLateral,
                    "Control y Proyección"
            );

        /*
         * CÉSAR Y JENY:
         * Ambos tienen rol DIGITADOR_GUIAS.
         * La empresa asignada determinará después
         * qué tipos de guía podrá utilizar cada uno.
         */
        } else if (
                usuarioActual.esDigitadorGuias()
        ) {

            agregarBotonMenu(
                    menuLateral,
                    "Guías"
            );

        /*
         * DANIELA:
         * Acceso al módulo Control y Proyección.
         */
        } else if (
                usuarioActual.esControlProyeccion()
        ) {

            agregarBotonMenu(
                    menuLateral,
                    "Control y Proyección"
            );

        } else {

            JLabel sinPermisos =
                    new JLabel(
                            "  Sin módulos asignados"
                    );

            sinPermisos.setForeground(Color.WHITE);

            sinPermisos.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );

            sinPermisos.setMaximumSize(
                    new Dimension(240, 48)
            );

            menuLateral.add(sinPermisos);
        }

        add(
                menuLateral,
                BorderLayout.WEST
        );
    }

    private void agregarBotonMenu(
            JPanel menuLateral,
            String texto
    ) {

        JButton boton =
                crearBotonMenu(texto);

        menuLateral.add(boton);
    }

    private JButton crearBotonMenu(
            String texto
    ) {

        JButton boton =
                new JButton(texto);

        boton.setMaximumSize(
                new Dimension(240, 48)
        );

        boton.setPreferredSize(
                new Dimension(240, 48)
        );

        boton.setMinimumSize(
                new Dimension(240, 48)
        );

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setBackground(
                new Color(17, 24, 39)
        );

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        boton.addActionListener(
                e -> mostrarPantalla(texto)
        );

        return boton;
    }

    private void mostrarPantallaInicial() {

        if (usuarioActual.esAdmin()) {

            mostrarPantalla(
                    "Dashboard Gerencial"
            );

        } else if (
                usuarioActual.esDigitadorGuias()
        ) {

            mostrarPantalla(
                    "Guías"
            );

        } else if (
                usuarioActual.esControlProyeccion()
        ) {

            mostrarPantalla(
                    "Control y Proyección"
            );

        } else {

            mostrarMensajeSinPermisos();
        }
    }

    private void mostrarPantalla(
            String nombreModulo
    ) {

        panelContenido.removeAll();

        if (
                nombreModulo.equals("Guías")
                && puedeAccederGuias()
        ) {

            panelContenido.add(
                    new GuiasPage(),
                    BorderLayout.CENTER
            );

        } else if (
                nombreModulo.equals(
                        "Control y Proyección"
                )
                && puedeAccederControlProyeccion()
        ) {

            panelContenido.add(
                    new ControlProyeccionPage(),
                    BorderLayout.CENTER
            );

        } else if (
                nombreModulo.equals(
                        "Canteras - Material Pétreo"
                )
                && puedeAccederControlProyeccion()
        ) {

            panelContenido.add(
                    new CatalogoCanteraMaterialPage(
                            () -> mostrarPantalla(
                                    "Control y Proyección"
                            )
                    ),
                    BorderLayout.CENTER
            );

        } else if (
                nombreModulo.equals(
                        "Costos y Rentabilidad"
                )
                && puedeAccederControlProyeccion()
        ) {

            panelContenido.add(
                    new CostosRentabilidadPage(
                            () -> mostrarPantalla(
                                    "Control y Proyección"
                            )
                    ),
                    BorderLayout.CENTER
            );

        } else if (
                nombreModulo.equals(
                        "Dashboard Gerencial"
                )
                && usuarioActual.esAdmin()
        ) {

            JLabel tituloModulo =
                    new JLabel(
                            "Dashboard Gerencial",
                            SwingConstants.CENTER
                    );

            tituloModulo.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            28
                    )
            );

            panelContenido.add(
                    tituloModulo,
                    BorderLayout.CENTER
            );

        } else {

            mostrarMensajeSinPermisos();
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private boolean puedeAccederGuias() {

        return usuarioActual.esAdmin()
                || usuarioActual.esDigitadorGuias();
    }

    private boolean puedeAccederControlProyeccion() {

        return usuarioActual.esAdmin()
                || usuarioActual.esControlProyeccion();
    }

    private void mostrarMensajeSinPermisos() {

        JLabel mensaje =
                new JLabel(
                        "No tiene permisos para acceder a este módulo.",
                        SwingConstants.CENTER
                );

        mensaje.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        panelContenido.add(
                mensaje,
                BorderLayout.CENTER
        );
    }
}