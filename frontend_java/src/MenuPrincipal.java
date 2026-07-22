import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private JPanel panelContenido;

    public MenuPrincipal() {

        setTitle("LPP Smart ERP");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout());

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(31, 41, 55));
        barraSuperior.setPreferredSize(new Dimension(1300, 60));

        JLabel titulo = new JLabel("  LPP Smart ERP");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel usuario = new JLabel("Usuario: admin  ");
        usuario.setForeground(Color.WHITE);
        usuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        barraSuperior.add(titulo, BorderLayout.WEST);
        barraSuperior.add(usuario, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);

        JPanel menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(17, 24, 39));
        menuLateral.setPreferredSize(new Dimension(240, 690));

       String[] opciones = {

                "Dashboard",
                "Guías",
                "Control y Proyección",
                "Costos y Rentabilidad"

        };

        for (String opcion : opciones) {

            JButton boton = crearBotonMenu(opcion);
            menuLateral.add(boton);
        }

        add(menuLateral, BorderLayout.WEST);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(244, 246, 248));

        mostrarPantalla("Dashboard Gerencial");

        add(panelContenido, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto) {

        JButton boton = new JButton(texto);

        boton.setMaximumSize(new Dimension(240, 48));
        boton.setPreferredSize(new Dimension(240, 48));
        boton.setMinimumSize(new Dimension(240, 48));

        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setBackground(new Color(17, 24, 39));
        boton.setForeground(Color.WHITE);

        boton.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        boton.addActionListener(e -> mostrarPantalla(texto));

        return boton;
    }

    private void mostrarPantalla(String nombreModulo) {

        panelContenido.removeAll();

        if (nombreModulo.equals("Guías")) {

    panelContenido.add(
            new GuiasPage(),
            BorderLayout.CENTER
    );

} else if (
        nombreModulo.equals("Control y Proyección")
) {

    panelContenido.add(
            new ControlProyeccionPage(),
            BorderLayout.CENTER
    );

} else if (
        nombreModulo.equals("Costos y Rentabilidad")
) {

    panelContenido.add(
            new CostosRentabilidadPage(
                    () -> mostrarPantalla("Dashboard")
            ),
            BorderLayout.CENTER
    );

} else {

            JLabel tituloModulo = new JLabel(
                    nombreModulo,
                    SwingConstants.CENTER
            );

            tituloModulo.setFont(
                    new Font("Segoe UI", Font.BOLD, 28)
            );

            panelContenido.add(
                    tituloModulo,
                    BorderLayout.CENTER
            );
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

}